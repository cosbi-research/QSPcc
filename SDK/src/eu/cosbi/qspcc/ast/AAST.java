package eu.cosbi.qspcc.ast;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.attrs.Attr;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAG;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.exceptions.MissingNodeException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

public class AAST extends DAG<Program, AAST> implements ProgramNode, ProgramCompilationUnit {
    private Logger logger = LogManager.getLogger(AAST.class);
    protected AASTNode root;
    protected String sourcePath;
    // dependencies map, each symbol mapped to statement that uses it
    protected Map<String, Set<AASTNode>> dependencies;
    // dependencies map, each symbol mapped to statement that defines it
    protected Map<String, Set<AASTNode>> outDependencies;
    // nodes that uses the same ID in this AAST
    // but doesn't belong to any function (like global variables to be used just in
    // this compilation unit)
    protected Map<String, List<AASTNode>> staticVariables;
    // nodes that uses the same ID in this AAST
    // and belong to a specific function. map function -> id -> list of nodes
    protected Map<String, Map<String, List<AASTNode>>> localVariables;
    // map function name to function node
    protected Map<String, AASTNode> localFunctions;
    protected LinkedHashMap<AASTNode, List<AASTNode>> nIncompleteFunction;
    private LinkedList<AASTNode> nIncompleteStatements;
    private Set<GException> warnings;
    private Set<GException> errors;

    public AAST(Program p) {
	super(p, null);
	init();
    }

    public AAST(Program p, AAST parent) {
	super(p, parent);
	init();
    }

    private void init() {
	nIncompleteStatements = new LinkedList<AASTNode>();
	nIncompleteFunction = new LinkedHashMap<>();
	staticVariables = new HashMap<String, List<AASTNode>>();
	localVariables = new HashMap<String, Map<String, List<AASTNode>>>();
	localFunctions = new HashMap<String, AASTNode>();
	warnings = new TreeSet<GException>();
	errors = new TreeSet<GException>();
    }

    public String name() {
	return Paths.get(sourcePath()).getFileName().toString().replaceAll("\\.\\w+", "");
    }

    @Override
    public NodeType type() {
	return null;
    }

    @Override
    public Object attr(Attr attr) {
	return null;
    }

    @Override
    public ProgramNode nearestExpression() {
	return null;
    }

    @Override
    public ProgramNode parentFunction() {
	return null;
    }

    @Override
    public ProgramNode descendant(NodeType t) {
	return null;
    }

    @Override
    public void setException(GException e) {
    }

    @Override
    public int compareTo(ProgramNode o2) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public List<ProgramNode> descendants() {
	List<AAST> cs = childs();
	List<ProgramNode> l = new ArrayList<>(cs.size());
	l.addAll(cs);
	return l;
    }

    @Override
    public ProgramNode ancestor() {
	return parent();
    }

    @Override
    public boolean ancestorOf(ProgramNode p) {
	AAST node = (AAST) p;
	if (node == null)
	    return false;
	else if (node.equals(this))
	    return true;
	else
	    return ancestorOf(node.parent());
    }

    public Map<String, AASTNode> functions() {
	return localFunctions;
    }

    /**
     * get the symbolstable defined in the function defined by funName
     * 
     * @param funName
     * @return
     */
    public Map<String, List<AASTNode>> functionSymbols(String funName) {
	return localVariables.get(funName);
    }

    /**
     * variable nodes used locally (from env or defined locally) excluding global
     * (program-wide) variables.
     * 
     * @param id
     * @return
     */
    protected List<AASTNode> variables(AASTNode node) {
	List<AASTNode> localVars = localSymbols(node);
	Set<AASTNode> envVars = new HashSet<AASTNode>();
	if (localVars == null)
	    return localVars;

	if (node.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM)) {
	    if (parent != null && parent.symbolExists(node))
		envVars.addAll(parent.variables(node));
	    else if (treeRef.symbolExists(node.name()))
		envVars.addAll(treeRef.variables(node));
	    else {
		// env variable from the same compilation unit
		// do nothing
	    }
	}
	localVars.addAll(envVars);
	return localVars;
    }

    public void rootNode(AASTNode root) {
	if (this.root == null)
	    this.root = root;
    }

    public AASTNode rootNode() {
	return root;
    }

    public void setSourcePath(String mainScript) {
	this.sourcePath = mainScript;
    }

    @Override
    public String id() {
	return sourcePath;
    }

    @Override
    public String toString() {
	return Paths.get(sourcePath).getFileName().toString();
    }

    /**
     * the full program path, main script file included
     * 
     * @param mainScript
     */
    public String sourcePath() {
	return sourcePath;
    }

    /**
     * update the type for this AASTNode. if it's an ID propagate the change to all
     * the ids in this sub-tree.
     * 
     * @param aastNode
     * @param newtype
     * @throws MissingNodeException
     */
    protected void updateVariableType(AASTNode aastNode, Attr newattr, Object newtype) {
	List<AASTNode> otherNodes = variables(aastNode);
	List<AASTNode> globalVariables = treeRef.variables(aastNode);
	boolean isExpressionUpdate = NodeAttr.ETYPE.equals(newattr);
	List<AASTNode> toUpdate;

	if (otherNodes != null && !otherNodes.isEmpty())
	    // if locally exists, update locally
	    toUpdate = otherNodes;
	else
	    toUpdate = globalVariables;

	boolean typeWasUpdated = false;
	List<GType> newtypelist = null;

	if (isExpressionUpdate) {
	    // true if type was updated, not just set for the first time
	    if (newtype instanceof List) {
		// override, or set the list of types for this node.
		// every other single-type update will only set a
		// value in this list.
		// The list is shared among all nodes that refer
		// to the same variable, this allows us to avoid
		// performing time and memory-consuming updates for every node
		newtypelist = (List<GType>) newtype;
		// checkIfFunctionInput(node);
	    } else {
		Tuple<List<GType>, Boolean> newTypeStruct = (Tuple<List<GType>, Boolean>) newtype;
		typeWasUpdated = newTypeStruct.second();
		newtypelist = newTypeStruct.first();
	    }

	}

	for (AASTNode node : aastNode.excludeDifferentEnvNodes(toUpdate)) {
	    // if array update all, if single type, update last one
	    if (isExpressionUpdate) {
		// set list to this node, so that this node is implicitly linked with
		// the node that originated the update through the list they share
		node.exprs(newtypelist, false);

		boolean update_stmt = true;
		// if node in a function parameter list -> force reparse of function
		// even if already resolved
		if (/* (typeWasUpdated || structType) && */ node.parentExists(NodeType.FUNCTION_PARAMETER_LIST)) {
		    AASTNode applyNode = node.parent(NodeType.APPLY);
		    AASTNode idNode = applyNode.child(NodeType.ID);
		    if (idNode != null && functionSymbolExists(idNode)) {
			AASTNode functionNode = idNode.compilationUnit().functionSymbolNode(idNode);
			// avoid running this update on library functions that do have a type already
			// defined
			if (functionNode.coreFunction() == null) {
			    AASTNode parameter = node.parentBefore(NodeType.FUNCTION_PARAMETER_LIST);
			    if (TypeUtils.getIDNode(parameter).id().equals(node.id())) {
				// if parameter reduces back to node, it isn't an expression..
				// so force update.
				int node_pos = parameter.parent().childPosition(parameter);
				try {
				    update_stmt = forceResolveAgainApplyFunctionTypes(node, node_pos, applyNode);
				} catch (GException e) {
				    logger.error(e.stringify());
				}
			    }
			}
		    }
		    // update statements where type just changed
		} else if (node.parentExists(NodeType.PARAMETER_LIST)) {
		    // the parameter of the function in which we are was changed!
		    update_stmt = forceResolveAgainFunctionTypes(node.parent(NodeType.FUNCTION), node);
		}

		if (update_stmt && typeWasUpdated)
		    TypeUtils.markAffectedStatementsToBeReparsed(node);

	    } else
		// generic attribute update
		node.attr(newattr, newtype);
	}
    }

    private boolean forceResolveAgainFunctionTypes(AASTNode functionNode, AASTNode changedParam) {
	if (functionNode.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED) && functionNode.hasAttr(NodeAttr.FUNCTION_RESOLVED))
	    unresolveFunction(functionNode, functionNode.childs(NodeType.PARAMETER_LIST));
	return false;
    }

    /**
     * force update the called function type if some actual parameter change value.
     * 
     * @param changedParamIdNode
     * @param applyNode
     * @return true if the full statement should be reparsed
     * @throws GException 
     */
    private boolean forceResolveAgainApplyFunctionTypes(AASTNode changedParamIdNode, int changed_param_position,
	    AASTNode applyNode) throws GException {
	AASTNode funIdNode = applyNode.child(NodeType.ID);
	AASTNode actualParamsNode = applyNode.child(NodeType.FUNCTION_PARAMETER_LIST);
	AASTNode functionNode = funIdNode.compilationUnit().functionSymbolNode(funIdNode);

	if (!functionNode.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED)
		|| !functionNode.hasAttr(NodeAttr.FUNCTION_RESOLVED))
	    // function not resolved yet
	    return false;

	// check function params. change changedParamNode formal type.
	// if all types set, re-add function_inputs_resolved
	List<AASTNode> formalParams = functionNode.childs(NodeType.PARAMETER_LIST);
	List<AASTNode> actualParams = actualParamsNode.childs();

	Tuple<AASTNode, Boolean> tp = null;
	try {
	    tp = TypeUtils.matchFormalParam(applyNode, functionNode, changedParamIdNode, changed_param_position,
		    formalParams, false);
	} catch (GException e1) {
	    throw e1;
	} catch (Exception e) {
	    throw new MissingNodeException(ErrorMessage.INTERNAL_FUN_PARAMETER_MISSING, applyNode,
		    functionNode.child(NodeType.ID).name(), changedParamIdNode.code());
	}
	AASTNode formalParam = tp.first();
	AASTNode actualParam = TypeUtils.getIDNode(actualParams.get(changed_param_position));
	GType changedParamIdExpr = changedParamIdNode.expr();
	GType formalParamExpr = formalParam.expr();
	boolean changedParamHasType = changedParamIdExpr != null;

	if (changedParamHasType && actualParam.type().equals(NodeType.FIELDACCESS)
		&& !actualParam.childs().get(actualParam.childs().size() - 1).equals(changedParamIdNode)) {
	    // this means we matched a parameter like a.b.c,
	    // changedParamIdNode can be either a, b or c.
	    // to be sure take last element of actualParam
	    boolean start = false;
	    StructType changedParamStruct = (StructType) changedParamIdExpr;
	    for (AASTNode c : actualParam.childs())
		if (c.type().equals(NodeType.DOT)) {
		} else if (start) {
		    LinkedList<GType> ptype = ((LinkedList<GType>) changedParamStruct.getField(c.name()));
		    if (ptype == null) {
			changedParamIdExpr = GType.get(BType.UNKNOWN);
			LinkedList<GType> l = new LinkedList<GType>();
			l.add(changedParamIdExpr);
			changedParamStruct.addField(l, c.name());
		    } else
			changedParamIdExpr = ptype.peekLast();

		    if (changedParamIdExpr == null) {
			changedParamHasType = false;
			break;
		    } else if (changedParamIdExpr.equals(BType.STRUCT))
			changedParamStruct = (StructType) changedParamIdExpr;
		    else
			// can't go on
			break;
		} else if (c.equals(changedParamIdNode))
		    start = true;
	}

	// change formal parameter type
	boolean valueChanged = formalParamExpr != null && (changedParamIdExpr instanceof ValuedType)
		&& (formalParamExpr instanceof ValuedType)
		&& (!(((ValuedType) changedParamIdExpr).hasValue() && ((ValuedType) formalParamExpr).hasValue())
			|| !formalParamExpr.equalValue(changedParamIdExpr));
	boolean typeChanged = formalParamExpr != null && (!formalParamExpr.canBeForcedToRepresent(changedParamIdExpr));
	if (changedParamHasType && (valueChanged || typeChanged)) {

	    if (typeChanged) {
		logger.debug("Parameter " + changedParamIdNode + " of function " + funIdNode.name() + " changed from "
			+ formalParam.expr() + " to " + changedParamIdExpr + " force re-evaluation of function types.");
		formalParam.expr(TypeUtils.updateNameType(changedParamIdExpr, formalParam));

		unresolveFunction(functionNode, formalParams);
	    } else {
		// if value changed
		// change just that value from node without reparsing anything
		ValuedType<?> v = (ValuedType<?>) changedParamIdExpr;
		v.deleteValue();
		formalParam.expr(v, false);
	    }
	}
	return false;
    }

    private void unresolveFunction(AASTNode functionNode, List<AASTNode> formalParams) {
	functionNode.removeAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED);
	functionNode.removeAttr(NodeAttr.FUNCTION_RESOLVED);
	// remove function type, remove return type
	// functionNode.removeAttr(NodeAttr.ETYPE);
	// for(AASTNode retnode : functionNode.childs(NodeType.FUNCTION_RETURN))
	// retnode.removeAttr(NodeAttr.ETYPE);

	// check if they are all defined. -> inputs_resolved
	boolean alldefined = true;
	for (AASTNode fp : formalParams)
	    if (fp.expr() == null || fp.expr().equals(BType.UNKNOWN)) {
		alldefined = false;
		break;
	    }

	if (alldefined)
	    functionNode.attr(NodeAttr.FUNCTION_INPUTS_RESOLVED, true);
    }

    private boolean isVariable(AASTNode aastNode, String nodeText) {
	if (!aastNode.type().equals(NodeType.ID)
		// allow as new variables only first element of structure access
		|| aastNode.parentExists(NodeType.FIELDACCESS)
			&& !aastNode.parent(NodeType.FIELDACCESS).childs().isEmpty())
	    return false;
	// skip global functions
	if (treeRef.functionExists(nodeText) && !localSymbol(aastNode, nodeText))
	    return false;
	return true;
    }

    /**
     * remove a node from the variables map
     * 
     * @param aastNode
     * @param nodeText
     */
    public void delVariable(AASTNode aastNode, String nodeText) {
	if (!isVariable(aastNode, nodeText))
	    return;
	if (
	// ASSIGNMENT TO GLOBAL VARIABLE
	(aastNode.parentExists(NodeType.LHS) && aastNode.parentExists(NodeType.GASSIGN))
		// access to global variable
		|| (!staticVariables.containsKey(nodeText) && treeRef.symbolExists(nodeText))
		|| aastNode.parentExists(NodeType.GLOBAL) || aastNode.hasAttr(NodeAttr.GLOBAL_VAR)) {
	    treeRef.delVariable(aastNode, nodeText);
	} else {
	    // local variables.. check if in function first
	    AASTNode funNode = aastNode.parent(NodeType.FUNCTION);
	    List<AASTNode> nodes = null;
	    String funName = null;
	    while (funNode != null) {
		if (funNode != null)
		    if (funNode.childExists(NodeType.ID))
			funName = funNode.child(NodeType.ID).name();
		    else if (aastNode.parent().type().equals(NodeType.FUNCTION)) {
			// I am the id.. add this to the list of variables anyway
			// it will be a function reference
			funName = aastNode.name();
		    }

		if (funName != null) {
		    if (localVariables.containsKey(funName)) {
			Map<String, List<AASTNode>> functionLocalVariables = localVariables.get(funName);
			if (functionLocalVariables.containsKey(nodeText)) {
			    nodes = functionLocalVariables.get(nodeText);
			    if (nodes.contains(aastNode))
				// delete this node
				nodes.remove(aastNode);
			}
		    }
		}
		// get parent function
		funNode = funNode.parent(NodeType.FUNCTION);
	    }
	    // not ELSE, it can be both
	    // if not inside a function, it's a static variable (a global variable visible
	    // only in this aast)
	    if (staticVariables.containsKey(nodeText)) {
		nodes = staticVariables.get(nodeText);
		if (nodes.contains(aastNode))
		    // delete this node
		    nodes.remove(aastNode);
	    }
	}
    }

    /**
     * save a map between program identifiers and their nodes in the program tree
     * 
     * @param aastNode
     * @param nodeText
     */
    protected void newVariable(AASTNode aastNode, String nodeText) {
	if (!isVariable(aastNode, nodeText))
	    return;

	if (
	// ASSIGNMENT TO GLOBAL VARIABLE
	(aastNode.parentExists(NodeType.LHS) && aastNode.parentExists(NodeType.GASSIGN))
		// access to global variable
		|| (!staticVariables.containsKey(nodeText) && treeRef.symbolExists(nodeText))
		|| aastNode.parentExists(NodeType.GLOBAL) || aastNode.hasAttr(NodeAttr.GLOBAL_VAR)) {
	    treeRef.newVariable(aastNode, nodeText);
	} else {
	    // local variables.. check if in function first
	    AASTNode funNode = aastNode.parent(NodeType.FUNCTION);

	    updateEnvironmentWithVariable(funNode, aastNode, nodeText);
	}
    }

    protected void updateEnvironmentWithVariable(AASTNode funNode, AASTNode aastNode, String nodeText) {
	List<AASTNode> nodes = null;
	String funName = null;
	if (funNode != null)
	    if (funNode.childExists(NodeType.ID))
		funName = funNode.child(NodeType.ID).name();
	    else if (aastNode.parent().type().equals(NodeType.FUNCTION)) {
		// I am the id.. add this to the list of variables anyway
		// it will be a function reference
		funName = aastNode.name();
	    }

	if (funName != null) {
	    if (!localVariables.containsKey(funName))
		localVariables.put(funName, new HashMap<String, List<AASTNode>>());
	    Map<String, List<AASTNode>> functionLocalVariables = localVariables.get(funName);
	    if (!functionLocalVariables.containsKey(nodeText))
		functionLocalVariables.put(nodeText, new ArrayList<AASTNode>());
	    nodes = functionLocalVariables.get(nodeText);
	} else {
	    // if not inside a function, it's a static variable (a global variable visible
	    // only in this aast)
	    if (!staticVariables.containsKey(nodeText))
		staticVariables.put(nodeText, new ArrayList<AASTNode>());
	    nodes = staticVariables.get(nodeText);
	}
	if (!nodes.contains(aastNode))
	    nodes.add(aastNode);
    }

    /**
     * check if this symbol is defined locally as a local function parameter or as a
     * local variable
     * 
     * @param aastNode
     * @param nodeText
     * @return
     */
    private boolean localSymbol(AASTNode aastNode, String nodeText) {
	if (nodeText == null)
	    return false;
	else if (aastNode.parent().type().equals(NodeType.FUNCTION))
	    // if immediate parent is function, this is the function name, not local
	    return false;

	AASTNode functionNode = aastNode.parent(NodeType.FUNCTION);
	if (functionNode != null) {
	    if (aastNode.parentExists(NodeType.PARAMETER_LIST))
		// I'm in a parameter list definition, I'm a parameter -> local
		return true;
	    else if (aastNode.parent().type().equals(NodeType.FUNCTION))
		// i'm the name of the function, not a local symbol
		return false;
	    else {
		// check if this symbol used in statement list is defined in a local function
		// parameter
		List<AASTNode> funParams = functionNode.childs(NodeType.PARAMETER_LIST);
		if (funParams != null)
		    for (AASTNode p : funParams)
			if (p.name().equals(nodeText))
			    return true;
	    }
	}
	// check if exists a local assignment for this symbol
	AASTNode assign = null;
	for (AASTNode stmt : aastNode.parent(NodeType.STATEMENT_LIST).childs()) {
	    if (stmt.equals(NodeType.ASSIGN))
		assign = stmt;
	    else if (stmt.equals(NodeType.GASSIGN))
		assign = stmt;

	    if (assign != null) {
		for (AASTNode lhsSymbol : assign.childs(NodeType.LHS)) {
		    while (!lhsSymbol.equals(NodeType.ID) && !lhsSymbol.equals(NodeType.FIELDACCESS)
			    && !lhsSymbol.equals(NodeType.MATRIX) && !lhsSymbol.equals(NodeType.VOID))
			lhsSymbol = lhsSymbol.childs().get(0);
		    if (lhsSymbol.equals(NodeType.ID) && nodeText.equals(lhsSymbol.name()))
			return true;
		    else if ((lhsSymbol.equals(NodeType.MATRIX) || lhsSymbol.equals(NodeType.FIELDACCESS))
			    && lhsSymbol.name().contains(nodeText)) {
			return true;
		    }
		}
		assign = null;
	    }
	}
	return false;
    }

    protected void delFunction(AASTNode parent, String nodeText) {
	if (!localFunctions.containsKey(nodeText.toLowerCase()))
	    // nothing to delete
	    return;

	localFunctions.remove(nodeText.toLowerCase());
	treeRef.delFunction(this, nodeText, parent);
    }

    protected void newFunction(AASTNode aastNode, String name) {
	localFunctions.put(name.toLowerCase(), aastNode);
	treeRef.newFunction(this, name, aastNode);
    }

    public boolean localFunctionSymbolExist(AASTNode node, AASTNode funNode) {
	String funName = funNode.child(NodeType.ID).name();

	if (localVariables.containsKey(funName)) {
	    Map<String, List<AASTNode>> functionLocalVariables = localVariables.get(funName);
	    if (functionLocalVariables.containsKey(node.name())) {
		List<AASTNode> nodes = functionLocalVariables.get(node.name());
		boolean exists_in_function = nodes.size() > 1 || (nodes.size() == 1 && !nodes.get(0).equals(node));
		if (exists_in_function)
		    return true;
	    } // otherwise check in static variables...
	}
	return false;
    }

    /**
     * check if this symbol is defined in this AAST, locally in the same function,
     * or in an enclosing function, or in a static variable (available only in this
     * compilation unit)
     * 
     * @param node
     * @return
     */
    public boolean localSymbolExists(AASTNode node) {
	AASTNode funNode = node.parent(NodeType.FUNCTION);
	while (funNode != null) {
	    if (localFunctionSymbolExist(node, funNode))
		return true;
	    // check if another enclosing function exists, if yes re-check
	    funNode = funNode.parent(NodeType.FUNCTION);
	}
	if (staticVariables.containsKey(node.name())) {
	    List<AASTNode> nodes = staticVariables.get(node.name());
	    return nodes.size() > 1 || (nodes.size() == 1 && !nodes.get(0).equals(node));
	} else
	    return false;
    }

    /**
     * given an ID node referencing a function, returns the corresponding node of
     * type FUNCTION
     * 
     * @param node
     * @return
     */
    public AASTNode functionSymbolNode(AASTNode node) {
	if (localFunctions.containsKey(node.name().toLowerCase()))
	    return localFunctions.get(node.name().toLowerCase());
	else if (parent != null)
	    return parent.functionSymbolNode(node);
	else
	    return functionNode(node.name());
    }

    public boolean functionSymbolExists(AASTNode node) {
	if (node == null)
	    return false;
	else if (localFunctions.containsKey(node.name().toLowerCase()))
	    return true;
	else if (parent != null)
	    return parent.functionSymbolExists(node);
	else
	    return functionExists(node.name());
    }

    /**
     * list of nodes equivalent to this one defined locally
     * 
     * @param node
     * @return
     */
    public List<AASTNode> localSymbols(AASTNode node) {
	return symbols(node, node.name(), true);
    }

    public List<AASTNode> symbols(AASTNode node, String varname, boolean all) {
	List<AASTNode> nodes = new LinkedList<AASTNode>();
	AASTNode funNode = node.parent(NodeType.FUNCTION);
	while (funNode != null) {
	    String funName = funNode.child(NodeType.ID).name();
	    if (localVariables.containsKey(funName)) {
		Map<String, List<AASTNode>> functionLocalVariables = localVariables.get(funName);
		if (functionLocalVariables.containsKey(varname)) {
		    List<AASTNode> curnodes = functionLocalVariables.get(varname);
		    if (curnodes != null) {
			nodes.addAll(curnodes);
			if (!all)
			    // we don't want all nodes, we need just the first set
			    break;
		    }
		} // otherwise check in static variables...
	    }
	    // check in enclosing function
	    funNode = funNode.parent(NodeType.FUNCTION);
	}
	if (staticVariables.containsKey(varname)) {
	    List<AASTNode> curnodes = staticVariables.get(varname);
	    if (curnodes != null)
		nodes.addAll(curnodes);
	}

	if (nodes.isEmpty())
	    return null;
	else
	    return nodes;
    }

    /**
     * returns the list of nodes that defines this symbol name (including symbols
     * defined locally)
     * 
     * @param name
     * @return
     */
    public List<AASTNode> symbols(AASTNode node) {
	if (localSymbolExists(node))
	    return variables(node);
	else
	    return parentSymbols(node);
    }

    /**
     * returns the list of nodes that defines this symbol name (defined exclusively
     * outside this compilation unit) in other words, the environment nodes with the
     * same node name.
     * 
     * @param name
     * @return
     */
    public List<AASTNode> parentSymbols(AASTNode node) {
	if (parent == null)
	    if (globalSymbolExists(node.name()))
		return treeRef.variables(node);
	    else if (functionExists(node.name())) {
		List<AASTNode> t = new ArrayList<AASTNode>();
		t.add(functionNode(node.name()));
		return t;
	    } else
		return new ArrayList<AASTNode>();
	else
	    return parent.symbols(node);
    }

    public boolean symbolExists(AASTNode node) {
	if (localSymbolExists(node))
	    return true;
	else if (parent == null)
	    if (globalSymbolExists(node.name()))
		return true;
	    else if (functionExists(node.name()))
		return true;
	    else
		return false;
	else
	    return parent.symbolExists(node);
    }

    public AASTNode nearestSymbol(AASTNode symbolNode) {
	return nearestSymbol(symbolNode, true);
    }

    /**
     * compute the nearest node with the same symbol name, used to infer the symbol
     * type (static scope)
     * 
     * @param symbolNode
     * @param type:      try to return a typed node. Default: true
     * @return
     */
    public AASTNode nearestSymbol(AASTNode symbolNode, boolean typed) {
	List<AASTNode> parentNodes = symbolNode.excludeDifferentEnvNodes(symbols(symbolNode));
	if (parentNodes == null || parentNodes.isEmpty())
	    return null;
	// parent nodes in this compilation unit
	List<AASTNode> inThisCompilationUnit = new ArrayList<AASTNode>();
	for (AASTNode pnode : parentNodes)
	    if (pnode.compilationUnit() == this)
		inThisCompilationUnit.add(pnode);
	if (!inThisCompilationUnit.isEmpty()) {
	    // if this node is defined in this compilation unit
	    // return the nearest one before this one with an expression set
	    List<AASTNode> nearestOrdered = symbolNode.nearestBefore(inThisCompilationUnit);
	    if (nearestOrdered == null)
		return null;

	    if (!typed && !nearestOrdered.isEmpty())
		// don't care about type
		return nearestOrdered.get(0);

	    for (AASTNode near : nearestOrdered)
		if (near.hasAttr(NodeAttr.ETYPE))
		    return near;
	    // none has a type, so
	    // remove inThisCompilationUnit from parentNodes
	    for (AASTNode n : inThisCompilationUnit)
		parentNodes.remove(n);

	    if (parentNodes.isEmpty())
		// don't use symbol here, to avoid stack overflow with AASTNode.symbol() that
		// calls this function
		if (globalSymbolExists(symbolNode.name())) {
		    List<AASTNode> vars = treeRef.variables(symbolNode);
		    // search first local variable
		    for (AASTNode var : vars)
			if (var.compilationUnit() == symbolNode.compilationUnit() && !var.equals(symbolNode)
				&& var.expr() != null && !var.expr().equals(BType.UNKNOWN))
			    return var;

		    // otherwise search for global variable
		    for (AASTNode var : vars)
			if (var.compilationUnit() == null)
			    return var;

		    return vars.get(vars.size() - 1);
		} else
		    // never defined and doesn't exists
		    return null;
	    else {
		// take last one in the nearest parent with a type
		for (int i = parentNodes.size() - 1; i >= 0; --i) {
		    AASTNode pnode = parentNodes.get(i);
		    if (pnode.expr() != null)
			return pnode;
		}

		// last one in the nearest parent
		return parentNodes.get(parentNodes.size() - 1);
	    }
	} else
	    // no symbol in the same compilation unit
	    return null;

    }

    public boolean globalSymbolExists(String symbol) {
	return treeRef.symbolExists(symbol);
    }

    private boolean functionExists(String id) {
	return treeRef.functionExists(id);
    }

    private AASTNode functionNode(String functionName) {
	return treeRef.functionNode(functionName);
    }

    @Override
    public String code() {
	return Paths.get(sourcePath).getFileName().toString();
    }

    @Override
    public String location() {
	return sourcePath;
    }

    @Override
    public List<AASTNode> incompleteFunctionStatements() {
	return new LinkedList<AASTNode>(nIncompleteFunction.keySet());
    }

    @Override
    public Tuple<List<AASTNode>, Map<AASTNode, List<AASTNode>>> incompleteStatements() {
	return new Tuple<List<AASTNode>, Map<AASTNode, List<AASTNode>>>(nIncompleteStatements, nIncompleteFunction);
    }

    /**
     * increase the number of functions in this compilation unit to be reparsed in a
     * future compilation unit walk. When the number of push equals the number of
     * pop the compilation unit won't be parsed anymore.
     */
    @Override
    public void pushWalk(AASTNode name) {
	if (!nIncompleteFunction.containsKey(name))
	    nIncompleteFunction.put(name, new LinkedList<>());
	treeRef.pushWalk(this);
    }

    /**
     * decrease the number of functions in this compilation unit to be reparsed in a
     * future compilation unit walk. When the number of push equals the number of
     * pop the compilation unit won't be parsed anymore.
     */
    @Override
    public void popWalk(AASTNode name) {
	nIncompleteFunction.remove(name);
	if (nIncompleteFunction.isEmpty())
	    // remove only when no incomplete functions left in this compilation unit
	    treeRef.popWalk(this);
    }

    @Override
    public boolean walkNeeded() {
	return nIncompleteFunction.size() > 0;
    }

    @Override
    public void popStatementWalk(AASTNode name, AASTNode inFunction) {

	if (inFunction != null && nIncompleteFunction.containsKey(inFunction))
	    nIncompleteFunction.get(inFunction).remove(name);
	else
	    nIncompleteStatements.remove(name);

	if (name.hasAttr(NodeAttr.STATEMENT_RESOLVED))
	    treeRef.popStatementWalk();
    }

    @Override
    public void pushStatementWalk(AASTNode name, AASTNode inFunction) {
	if (inFunction != null) {
	    if (!nIncompleteFunction.containsKey(inFunction))
		nIncompleteFunction.put(inFunction, new LinkedList<>());
	    nIncompleteFunction.get(inFunction).add(name);
	} else
	    nIncompleteStatements.add(name);

	if (name.hasAttr(NodeAttr.REPARSE_STATEMENT))
	    treeRef.pushStatementWalk();
    }

    @Override
    public boolean statementWalkNeeded() {
	return !nIncompleteStatements.isEmpty();
    }

    public Program program() {
	return treeRef;
    }

    public List<GType> undefinedType(AASTNode node) {
	return treeRef.undefinedType(node);
    }

    @Override
    public String typeAsString() {
	return "compilation unit";
    }

    public boolean hasCustomUndefinedType() {
	return treeRef.hasCustomUndefinedType();
    }

    /**
     * store warnings that will be displayed only once at the end
     * @param ex
     */
    public void addWarning(GException ex) {
	warnings.add(ex);
    }

    /**
     * store exceptions that will be displayed only once at the end
     * @param ex
     */
    public void addError(GException ex) {
	if (ex instanceof ListException) {
	    for (GException cure : (ListException) ex)
		errors.add(cure);
	} else
	    errors.add(ex);
    }

    public Set<GException> warnings() {
	return warnings;
    }

    public Set<GException> errors() {
	return errors;
    }

}
