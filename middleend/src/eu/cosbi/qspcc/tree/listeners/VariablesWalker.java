package eu.cosbi.qspcc.tree.listeners;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.MiddleEndPass;
import eu.cosbi.utils.TypeUtils;

/**
 * set attributes: IS_FUNCTION_LOCAL_VAR IS_FUNCTION_ENV_PARAM
 * FUNCTION_ENV_PARAMS REF_FUNCTION, FUNCTION_CALLS
 * 
 * @author tomasoni
 *
 */
public class VariablesWalker implements DAGListener<AAST, AASTNode, Object>, MiddleEndPass {

    protected AAST mainAAST;
    private boolean stopOnError;
    // variables defined locally
    protected Set<AASTNode> localVars;
    private Map<String, AASTNode> funNodes;
    private IFunction[] coreFunctions;
    private Logger logger = LogManager.getLogger(VariablesWalker.class);

    public VariablesWalker(AAST main, Map<String, AASTNode> funNodes, IFunction[] coreFunctions) {
	mainAAST = main;
	this.funNodes = funNodes;
	localVars = new HashSet<AASTNode>();
	this.coreFunctions = coreFunctions;
    }

    @Override
    public void annotate(boolean stopOnError) throws GException {
	mainAAST.rootNode().walkFor(this, stopOnError);
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	switch (node.type()) {
	case ID:
	    /*
	    // if this variable/matrix used in an apply... 
	    if (!node.hasAttr(NodeAttr.PREFER_INT) && node.parentExists(NodeType.FUNCTION_PARAMETER_LIST)
	        && !aast.functionSymbolExists(node)) {
	    AASTNode params = node.parent(NodeType.FUNCTION_PARAMETER_LIST);
	    AASTNode applyId = params.parent(NodeType.APPLY).child(NodeType.ID);
	    //.... to slice a matrix
	    if (!aast.functionSymbolExists(applyId)) {
	        // because variables/matrices used to address other matrices should be of type int/bool
	        propagateAttributeToSymbols(node, NodeAttr.PREFER_INT, true);
	    }
	    }*/

	    if (!node.hasAttr(NodeAttr.DYNAMIC_ID) && node.parentExists(NodeType.RHS)) {
		// check if in the case a = [a; x]
		// in a for/while loop
		AASTNode lhsNode = TypeUtils.getIDNode(node.parent(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN })
			.child(NodeType.LHS).childs().get(0));
		if (lhsNode.type().equals(NodeType.ID) || lhsNode.type().equals(NodeType.APPLY)
			|| (lhsNode.type().equals(NodeType.FIELDACCESS)
				&& node.parent().type().equals(NodeType.FIELDACCESS))) {
		    AASTNode curNode;
		    if (lhsNode.type().equals(NodeType.FIELDACCESS))
			curNode = node.parent();
		    else if (lhsNode.type().equals(NodeType.APPLY)) {
			lhsNode = lhsNode.child(new NodeType[] { NodeType.ID, NodeType.FIELDACCESS });
			curNode = node;
		    } else
			curNode = node;
		    String lhsId = lhsNode.symbol();
		    if (lhsId.equals(curNode.symbol())) {
			// situation a = ... a ...
			// in this case a can't be static
			propagateAttributeToSymbols(node, NodeAttr.DYNAMIC_ID, true);
		    }
		}
	    }

	    String functionName = node.name();
	    IFunction fenum = null;
	    for (IFunction fun : coreFunctions)
		if (fun.getName().toLowerCase().equals(functionName.toLowerCase())) {
		    fenum = fun;
		    break;
		}
	    /* add reference to core function definition */
	    if (fenum != null)
		node.attr(NodeAttr.REF_CORE_FUNCTION, fenum);

	    boolean persistent = node.hasAttr(NodeAttr.PERSISTENT_VAR);
	    boolean global = node.hasAttr(NodeAttr.GLOBAL_VAR);

	    if (!persistent && node.parentExists(NodeType.PERSISTENT)) {
		node.attr(NodeAttr.PERSISTENT_VAR, true, true);
		persistent = true;
	    }
	    // global variables are not local, are not env variables
	    if (global)
		return;
	    else if (node.parentExists(NodeType.GLOBAL)) {
		// "user-defined" global variable
		AASTNode stmt = node.parent(NodeType.STATEMENT_LIST);
		stmt.propagateAttribute(NodeAttr.GLOBAL_VAR, true, node.name(),
			new NodeAttr[] { NodeAttr.ETYPE_FROM_EXTERNAL_ENV });
		global = true;
		// in this case this variable can't be external variable in a function because
		// it's global.
		return;
	    }

	    // mark all environment and local parameters
	    if (node.parentExists(NodeType.FUNCTION)) {
		AASTNode fnode = node.parent(NodeType.FUNCTION);
		// function name
		if (fnode.child(NodeType.ID).name().equals(node.name()))
		    // function name
		    return;
		// function return values
		for (AASTNode p : fnode.childs(NodeType.FUNCTION_RETURN))
		    if (node.name().equals(p.name()))
			// is a function return value
			return;
		// formal params
		for (AASTNode p : fnode.childs(NodeType.PARAMETER_LIST))
		    if (node.name().equals(p.name()))
			// is a function parameter
			return;
		// function symbol
		if (aast.functionSymbolExists(node))
		    return;
		// "core" global variable
		if (node.hasAttr(NodeAttr.ETYPE_FROM_EXTERNAL_ENV))
		    return;
		// local variable
		if (node.parentExists(NodeType.LHS) || node.parent().type().equals(NodeType.FOR)
			|| node.parent().type().equals(NodeType.PARFOR) || persistent) {
		    localVars.add(node);
		    node.attr(NodeAttr.IS_FUNCTION_LOCAL_VAR, true);
		    return;
		}
		if (aast.localSymbolExists(node)) {
		    for (AASTNode localVar : localVars)
			if (// same symbol
			localVar.name().equals(node.name())
				// same compilation unit
				&& localVar.compilationUnit().equals(node.compilationUnit())
				// same function (if inside function)
				&& (!localVar.parentExists(NodeType.FUNCTION)
					|| localVar.parent(NodeType.FUNCTION).equals(node.parent(NodeType.FUNCTION)))) {
			    node.attr(NodeAttr.IS_FUNCTION_LOCAL_VAR, true);
			    return;
			}
		}

		// type inferred from environment
		AASTNode parent = node.parent();
		boolean irrelevant = parent.type().equals(NodeType.CLEAR) || parent.type().equals(NodeType.CLOSE)
			|| parent.type().equals(NodeType.CLEARVARS) || parent.type().equals(NodeType.HOLD)
			|| parent.type().equals(NodeType.GRID) || parent.type().equals(NodeType.FORMAT);
		if (!irrelevant
			&& (!parent.type().equals(NodeType.FIELDACCESS) || parent.childs().get(0).equals(node))) {
		    // if inside function, check if in the function parameters
		    AASTNode functionNode = node.parent(NodeType.FUNCTION);
		    // env params should be only the non-fieldaccess, or the first element of the
		    // field access
		    node.attr(NodeAttr.IS_FUNCTION_ENV_PARAM, functionNode);
		    if (!fnode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS))
			fnode.attr(NodeAttr.FUNCTION_ENV_PARAMS, new ArrayList<AASTNode>());
		    ((List<AASTNode>) fnode.attr(NodeAttr.FUNCTION_ENV_PARAMS)).add(node);

		    updateFunctionDefinition(aast, functionNode, node);
		    updateParentEnvironmentApplyNodes(aast, functionNode, node);
		}
	    }

	    if (node.parent().type().equals(NodeType.APPLY) && aast.functionSymbolExists(node)) {
		AASTNode functionNode = aast.functionSymbolNode(node);
		if (!functionNode.hasAttr(NodeAttr.FUNCTION_CALLS))
		    functionNode.attr(NodeAttr.FUNCTION_CALLS, new LinkedList<AASTNode>());
		((List<AASTNode>) functionNode.attr(NodeAttr.FUNCTION_CALLS)).add(node);
	    }
	    break;
	case AT:
	    AASTNode stmtListNode = node.child(NodeType.EXPRESSION);
	    // set function references
	    if (stmtListNode == null) {
		AASTNode funNode = node.child(NodeType.ID);
		if (funNode == null)
		    funNode = node.child(NodeType.APPLY).child(NodeType.ID);
		String referencedFunName = funNode.name();
		AASTNode referencedFun = funNodes.get(referencedFunName);
		node.attr(NodeAttr.REF_FUNCTION, referencedFun);
	    }
	    break;
	default:
	}

    }

    private void updateParentEnvironmentApplyNodes(AAST aast, AASTNode functionNode, AASTNode node) {
	// TODO Auto-generated method stub

    }

    private void updateFunctionDefinition(AAST aast, AASTNode functionNode, AASTNode node) {
	// -- ADD ENVIRONMENT PARAMETER NODES --
	String symbol = node.name();

	List<String> formalParamNames = (List<String>) functionNode.attr(NodeAttr.FUNCTION_PARAMS);
	if (!formalParamNames.contains(symbol)) {
	    // env parameter --> add a new node
	    // add to params
	    formalParamNames.add(symbol);
	    // logger.warn(new UnboundException(ErrorMessage.WARN_NEW_ENV_PARAM, node,
	    // symbol,
	    // functionNode.child(NodeType.ID).name()).stringify());
	    functionNode.attr(NodeAttr.FUNCTION_PARAMS, formalParamNames);
	    // add to tree
	    AASTNode functionParametersNode = functionNode.child(NodeType.PARAMETER_LIST);
	    AASTNode newFormalParam = new AASTNode(aast, NodeType.ID, symbol, functionParametersNode.lineNumber(),
		    functionParametersNode.colNumber(), functionParametersNode);
	    // same types
	    newFormalParam.mergeAttrs(node);
	    newFormalParam.attr(NodeAttr.IS_FUNCTION_ENV_PARAM, functionNode);
	    newFormalParam.attr(NodeAttr.IS_FUNCTION_PARAM, true);
	    List<String> symbols = new ArrayList<String>();
	    symbols.add(symbol);
	    // from statement list on set this symbol to be a function
	    // parameter
	    List<AASTNode> affectedNodes = new LinkedList<AASTNode>();
	    functionNode.child(NodeType.STATEMENT_LIST).propagateAttribute(affectedNodes, NodeAttr.IS_FUNCTION_PARAM,
		    true, symbols, new NodeAttr[] {});
	    newFormalParam.attr(NodeAttr.ENV_PARAM_REFERENCES, affectedNodes);

	    functionParametersNode.addChild(newFormalParam);
	}
    }

    private void propagateAttributeToSymbols(AASTNode node, NodeAttr attr, Object value) {
	// get scope of variable
	/**
	 * not needed! use symbol table
	 *
	AASTNode stmt = node.parent(NodeType.STATEMENT_LIST);
	while (!stmt.parent().type().equals(NodeType.FUNCTION) && !stmt.parent().type().equals(NodeType.PROGRAM))
	    stmt = stmt.parent(NodeType.STATEMENT_LIST);
	
	stmt.propagateAttribute(attr, value, node.symbol());
	*/
	if (node.parent().type().equals(NodeType.FIELDACCESS)) {
	    List<AASTNode> fieldpath = node.parent().childs();
	    if (fieldpath.get(fieldpath.size() - 1).equals(node))
		// update only last element of access
		node.attr(attr, value);
	    // in a struct, only the base struct element is saved as a variable in symbol table
	    List<AASTNode> otherNodes = node.compilationUnit().symbols(fieldpath.get(0));
	    for (AASTNode otherNode : otherNodes) {
		if (otherNode.parent().type().equals(NodeType.FIELDACCESS)) {
		    List<AASTNode> otherpath = otherNode.parent().childs();
		    if (fieldpath.size() == otherpath.size()) {
			boolean sameaccess = true;
			for (int i = 0; i < fieldpath.size(); ++i)
			    if (!TypeUtils.getIDNode(fieldpath.get(i)).code()
				    .equals(TypeUtils.getIDNode(otherpath.get(i)).code())) {
				// field access to a different field, skip
				sameaccess = false;
				break;
			    }
			if (sameaccess)
			    otherpath.get(otherpath.size() - 1).attr(attr, value);
		    }
		}
	    }
	} else {
	    List<AASTNode> otherNodes = node.compilationUnit().symbols(node);
	    node.attr(attr, value);
	    for (AASTNode otherNode : otherNodes)
		otherNode.attr(attr, value);
	}
    }

    @Override
    public void onExit(AAST ref, AASTNode curNode, Deque<Object> results) throws GException {
	// TODO Auto-generated method stub

    }

    @Override
    public void onWalkCompleted(AAST ref) throws GException {
	// TODO Auto-generated method stub

    }

    @Override
    public void onWalkStarted(AAST ref) throws GException {
	// TODO Auto-generated method stub

    }

}
