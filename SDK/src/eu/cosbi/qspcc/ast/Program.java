package eu.cosbi.qspcc.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.expressions.complextype.StructDefinition;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.FunctionType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.utils.Tuple;

/**
 * A program is a DAG of compilation units where a compilation unit is an AAST
 * coming from the Frontend, and a directed edge between compilation units can
 * be interpreted as "the parent compilation unit calls the child compilation
 * unit"
 * 
 * @author tomasoni
 *
 */
public class Program implements ProgramRoot {
    Logger logger = LogManager.getLogger(Program.class);
    // root of the DAG
    AAST mainCompilationUnit;
    // global functions table
    protected Map<String, AASTNode> globalFunctions;
    private Set<String> envFunctions;
    // global variables table
    private Map<String, List<AASTNode>> globalVariables;
    private Set<String> envVariables;
    protected Set<AAST> nGlobalIncompleteFunction;
    private int nGlobalIncompleteStatements;
    // used to decide type of undefined nodes
    private Function<AASTNode, List<GType>> undefInputTypeAlgorithm;
    private Set<String> userFunctions;

    public Program(IFunction[] coreFuns, List<Tuple<GType, String>> gvars) {
	nGlobalIncompleteStatements = 0;
	nGlobalIncompleteFunction = new HashSet<AAST>();
	globalFunctions = new HashMap<String, AASTNode>();
	globalVariables = new HashMap<String, List<AASTNode>>();
	envFunctions = new HashSet<String>();
	envVariables = new HashSet<String>();
	userFunctions = new HashSet<String>();
	undefInputTypeAlgorithm = null;
	fillcoreFuns(coreFuns);
	fillVars(gvars);
    }

    private void fillVars(List<Tuple<GType, String>> gvars) {
	for (Tuple<GType, String> var : gvars) {
	    envVariables.add(var.second());
	    List<AASTNode> lst = new ArrayList<AASTNode>();
	    lst.add(iVariableToAASTNode(var.first(), var.second()));
	    globalVariables.put(var.second(), lst);
	}

    }

    private void fillcoreFuns(IFunction[] coreFuns) {
	for (IFunction fun : coreFuns) {
	    if (fun.type().equals(FunctionType.VARIABLE)) {
		envVariables.add(fun.getName());
		List<AASTNode> lst = new ArrayList<AASTNode>();
		lst.add(iVariableToAASTNode(fun));
		globalVariables.put(fun.getName(), lst);
	    } else {
		envFunctions.add(fun.getName().toLowerCase());
		globalFunctions.put(fun.getName().toLowerCase(), iFunctionToAASTNode(fun));
	    }
	}
    }

    /**
     * create from IFunction an AAST so that from an AAST perspective a core
     * variable is like an user-defined global variable.
     * 
     * @param fun
     * @return
     */
    private AASTNode iVariableToAASTNode(IFunction fun) {
	AAST main = null;
	Map<BType, GType> retType = fun.getOutType();
	GType varType = retType.get(BType.UNKNOWN);
	AASTNode id = new AASTNode(main, NodeType.ID, fun.getName(), 0, 0, null);
	id.expr(varType, true);
	return id;
    }

    private AASTNode iVariableToAASTNode(GType type, String name) {
	AAST main = null;
	AASTNode id = new AASTNode(main, NodeType.ID, name, 0, 0, null);
	id.expr(type, true);
	return id;
    }

    /**
     * create from IFunction an AAST so that from an AAST perspective a core
     * function is as an user-defined function
     * 
     * @param fun
     * @return
     */
    private AASTNode iFunctionToAASTNode(IFunction fun) {
	AAST main = null;
	Map<BType, GType> retType = fun.getOutType();
	List<GType> params = fun.getParamTypes();
	List<String> paramNames = new ArrayList<String>();
	String fun_out = fun.getName() + "_out";
	for (int i = 0; i < params.size(); ++i)
	    paramNames.add("p" + Integer.toString(i));

	AASTNode fundef = new AASTNode(main, NodeType.FUNCTION,
		"function " + fun.getName() + "(" + String.join(", ", paramNames) + ") returns " + fun_out, 0, 0, null);
	fundef.attr(NodeAttr.CORE_FUNCTION, fun);
	// fun name
	AASTNode id = new AASTNode(main, NodeType.ID, fun.getName(), 0, 0, fundef);
	fundef.addChild(id);

	// function return values
	AASTNode funret = new AASTNode(main, NodeType.FUNCTION_RETURN, fun_out, 0, 0, fundef);
	fundef.addChild(funret);
	AASTNode idret = new AASTNode(main, NodeType.ID, fun_out, 0, 0, funret);
	funret.addChild(idret);

	// function parameters
	AASTNode funpara = new AASTNode(main, NodeType.PARAMETER_LIST, "(" + String.join(", ", paramNames) + ")", 0, 0,
		fundef);
	fundef.addChild(funpara);

	if (retType.size() == 1) {

	    GType[] inputs = new GType[params.size()];
	    for (int i = 0; i < params.size(); ++i) {
		GType param = params.get(i);
		id = new AASTNode(main, NodeType.ID, paramNames.get(i), 0, 0, funpara);
		id.expr(param, false);
		funpara.addChild(id);
		inputs[i] = param;
	    }

	    GType[] outputs = new GType[retType.size()];
	    int k = 0;
	    for (Map.Entry<BType, GType> entry : retType.entrySet()) {
		BType inType = entry.getKey();
		GType rType = entry.getValue();

		idret.expr(rType, false);
		outputs[k++] = rType;
	    }
	    fundef.expr(GType.get(BType.FUNCTION, inputs, outputs), false);
	} else {
	    // the key of the map is the input that causes this output
	    // add input parameter
	    id = new AASTNode(main, NodeType.ID, paramNames.get(0), 0, 0, funpara);
	    funpara.addChild(id);
	    for (Map.Entry<BType, GType> entry : retType.entrySet()) {
		BType inType = entry.getKey();
		GType iType = GType.get(inType);
		GType rType = entry.getValue();

		// expr for input parameter
		id.expr(iType, false);

		idret.expr(rType, false);
		fundef.expr(GType.get(BType.FUNCTION, new GType[] { iType }, new GType[] { rType }), false);
	    }
	}

	return fundef;
    }

    public void mainCompilationUnit(AAST root) {
	if (this.mainCompilationUnit == null)
	    this.mainCompilationUnit = root;
    }

    public AAST mainCompilationUnit() {
	return mainCompilationUnit;
    }

    public boolean functionExists(String id) {
	return globalFunctions.containsKey(id.toLowerCase());
    }

    public boolean symbolExists(String nodeText) {
	return globalVariables.containsKey(nodeText);
    }

    protected List<AASTNode> variables(AASTNode node) {
	return globalVariables.get(node.name());
    }

    public Map<String, AASTNode> functions() {
	return globalFunctions;
    }

    public Set<String> userFunctionNames() {
	return userFunctions;
    }

    public Set<StructDefinition> userStructures() {
	return StructDefinition.structures();
    }

    protected void delFunction(AAST aast, String nodeText, AASTNode parent) {
	if (!globalFunctions.containsKey(nodeText.toLowerCase()))
	    // nothing to remove
	    return;

	userFunctions.remove(nodeText);
	globalFunctions.remove(nodeText.toLowerCase());
    }

    protected void newFunction(AAST aast, String name, AASTNode functionNode) {
	if (globalFunctions.containsKey(name.toLowerCase())) {
	    // case insensitive
	    AASTNode programNode = globalFunctions.get(name.toLowerCase());
	    AAST subProgram = programNode.compilationUnit();
	    String subprogramName = null;
	    if (subProgram == null)
		subprogramName = "global environment";
	    else
		subprogramName = subProgram.sourcePath();
	    if (!aast.sourcePath().equals(subprogramName))
		logger.warn("Overriding function definition '" + name + "' in '" + aast.sourcePath()
			+ "' was previously defined in '" + subprogramName + "' line " + programNode.lineNumber());
	}
	// preserve case here
	userFunctions.add(name);
	globalFunctions.put(name.toLowerCase(), functionNode);
    }

    /**
     * returns the function definition of the function named 'functionName' or
     * null if it doesn't exist (yet).
     * 
     * @param functionName
     * @return
     */
    public AASTNode functionNode(String functionName) {
	return globalFunctions.get(functionName.toLowerCase());
    }

    /**
     * remove this node from global variables
     * @param aastNode
     * @param nodeText
     */
    public void delVariable(AASTNode aastNode, String nodeText) {
	if (!globalVariables.containsKey(nodeText))
	    // nothing to delete
	    return;
	List<AASTNode> nodes = variables(aastNode);
	if (!nodes.contains(aastNode))
	    // nothing to delete
	    return;
	nodes.remove(aastNode);
    }

    /**
     * new node that represents a global variable
     * 
     * @param aastNode
     * @param nodeText
     */
    protected void newVariable(AASTNode aastNode, String nodeText) {
	if (!globalVariables.containsKey(nodeText))
	    globalVariables.put(nodeText, new ArrayList<AASTNode>());

	List<AASTNode> nodes = variables(aastNode);
	if (!nodes.contains(aastNode)) {
	    List<GType> types;
	    if (!nodes.isEmpty())
		types = nodes.get(0).exprs();
	    else {
		types = new LinkedList<GType>();
		types.add(GType.get(BType.UNKNOWN));
	    }
	    // they should have the same type
	    aastNode.exprs(types, false);
	    // remember that this comes from global env
	    aastNode.attr(NodeAttr.ETYPE_FROM_EXTERNAL_ENV, true);
	    nodes.add(aastNode);
	}
    }

    @Override
    public List<AAST> incompleteFunctions() {
	return new LinkedList<AAST>(nGlobalIncompleteFunction);
    }

    @Override
    public void pushWalk(AAST name) {
	nGlobalIncompleteFunction.add(name);
    }

    @Override
    public void popWalk(AAST name) {
	nGlobalIncompleteFunction.remove(name);
    }

    @Override
    public boolean walkNeeded() {
	return nGlobalIncompleteFunction.size() > 0;
    }

    public void popStatementWalk() {
	nGlobalIncompleteStatements--;
    }

    public void pushStatementWalk() {
	nGlobalIncompleteStatements++;
    }

    @Override
    public boolean statementWalkNeeded() {
	return nGlobalIncompleteStatements > 0;
    }

    public void undefinedType(Function<AASTNode, List<GType>> fun) {
	undefInputTypeAlgorithm = fun;
    }

    public boolean hasCustomUndefinedType() {
	return undefInputTypeAlgorithm != null;
    }

    public List<GType> undefinedType(AASTNode node) {
	if (undefInputTypeAlgorithm == null) {
	    List<GType> ret = new LinkedList<GType>();
	    if (node.parentExists(NodeType.FIELDACCESS, 2)) {
		AASTNode fieldaccess = node.parent(NodeType.FIELDACCESS);
		if (fieldaccess.childs().get(0).equals(node)) {
		    node.attr(NodeAttr.IMPLICITLY_DEFINED, node);
		    // if this is the first part of a fieldaccess, this is a struct
		    ret.add(GType.get(BType.STRUCT).name(node.name()));
		    return ret;
		} else {
		    // subelements of structures are unknown
		    ret.add(GType.get(BType.UNKNOWN));
		    return ret;
		}
	    }

	    ret.add(GType.get(BType.UNDEFINED));
	    return ret;
	} else
	    return undefInputTypeAlgorithm.apply(node);
    }

}
