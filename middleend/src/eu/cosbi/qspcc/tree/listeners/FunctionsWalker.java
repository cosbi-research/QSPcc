package eu.cosbi.qspcc.tree.listeners;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.utils.TypeUtils;

/**
 * Re-parse each function that now have all the parameter types set and resolve
 * all the unknown types (including global variables referenced).
 * 
 * @author tomasoni
 *
 */
public class FunctionsWalker extends FrontEndWalker {
    private Logger logger = LogManager.getLogger(FunctionsWalker.class);
    private AASTNode curFunctionStatamentList;
    private AASTNode curFunctionNode;

    public FunctionsWalker(AAST mainAAST) {
	super(mainAAST);
	curFunctionNode = null;
    }

    public FunctionsWalker(AAST mainAAST, AASTNode functionNode) {
	super(mainAAST);
	curFunctionNode = functionNode;
    }

    /**
    * first pass annotation on types
    * 
    * @throws Exception
    */
    @Override
    public void annotate(boolean stopOnError) throws GException {
	this.stopOnError = stopOnError;
	if (curFunctionNode == null)
	    mainAAST.rootNode().walkFor(this, stopOnError);
	else {
	    curFunctionStatamentList = curFunctionNode.child(NodeType.STATEMENT_LIST);
	    clearFunctionBody();
	    // child mode: walk only on this statement list
	    curFunctionNode.walkFor(this, true, stopOnError);
	}
    }

    private void clearFunctionBody() throws SyntaxException, UnboundException {
	List<String> paramNames = (List<String>) curFunctionNode.attr(NodeAttr.FUNCTION_PARAMS);
	List<String> outputSymbols = (List<String>) curFunctionNode.attr(NodeAttr.FUNCTION_OUTPUT);
	AASTNode funNodeId = curFunctionNode.child(NodeType.ID);
	String funName = funNodeId.name();
	if (paramNames == null || outputSymbols == null)
	    throw new SyntaxException(ErrorMessage.USER_SYNTAX_ERROR, functionNode, "Function definition of '" + funName
		    + "' is probably hidden by some non-closed if/for/function statement");

	// scope is simply change an identifer attributes on a subtree
	logger.debug("Recursively clearing ids for new scope: " + String.join(", ", paramNames));
	// resetIdAttributes(curFunctionNode, curFunctionStatamentList, paramNames);
	// symboltable for this function
	Map<String, List<AASTNode>> funSymbols = curFunctionNode.compilationUnit().functionSymbols(funName);
	for (String paramName : paramNames)
	    if ("".equals(paramName)) {
		// none
	    } else if (funSymbols.containsKey(paramName)) {
		List<AASTNode> paramNodes = funSymbols.get(paramName);
		AASTNode formalParamNode = null;
		for (AASTNode node : paramNodes)
		    if (node.parentExists(NodeType.PARAMETER_LIST)) {
			formalParamNode = node;
			break;
		    }
		for (AASTNode node : paramNodes) {
		    // don't clear parameters
		    if (node != formalParamNode && !node.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM)) {
			node.clearAttrs();
			// add is_function_param = true
			node.attr(NodeAttr.IS_FUNCTION_PARAM, true);
			// copy type
			if (formalParamNode.exprs() != null)
			    node.exprs(formalParamNode.exprs(), false);
			node.symbol(formalParamNode.symbol());
		    }
		}
	    } else {
		throw new UnboundException(ErrorMessage.INTERNAL_FORMAL_PARAMETER_DOESNT_EXIST, curFunctionNode,
			funNodeId.name(), paramName);
	    }

	for (String outName : outputSymbols)
	    if ("".equals(outName)) {
		// pass
	    } else if (funSymbols.containsKey(outName))
		for (AASTNode node : funSymbols.get(outName)) {
		    // don't clear parameters
		    if (!node.parentExists(NodeType.FUNCTION_RETURN)) {
			// node.clearAttrs();
			// add is_function_param = true
			node.attr(NodeAttr.IS_FUNCTION_OUTPUT, true);
			// node.symbol();
		    }
		}
	    else
		throw new UnboundException(ErrorMessage.INTERNAL_FORMAL_OUTPUT_DOESNT_EXIST, curFunctionNode,
			funNodeId.name(), outName);
	// NodeAttr[] toRemove = new NodeAttr[] {};
	// curFunctionStatamentList.propagateAttribute(NodeAttr.IS_FUNCTION_PARAM, true,
	// paramNames, toRemove);
	// curFunctionStatamentList.propagateAttribute(NodeAttr.IS_FUNCTION_OUTPUT,
	// true, outputSymbols, toRemove);

	logger.debug("Evaluation of function '" + curFunctionNode + "' body..");
    }

    private void resetIdAttributes(AASTNode contextFunNode, AASTNode curNode, List<String> ids) {
	if (ids == null || ids.isEmpty()
	// or we changed context
		|| !contextFunNode.equals(curNode.parent(NodeType.FUNCTION)))
	    return;
	String symbol = curNode.name();
	if (curNode.type().equals(NodeType.ID) && ids.contains(symbol))
	    curNode.clearAttrs();
	for (AASTNode child : curNode.childs()) {
	    resetIdAttributes(contextFunNode, child, ids);
	}
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	if (curFunctionStatamentList == null && node.type().equals(NodeType.FUNCTION)
		&& node.childExists(NodeType.STATEMENT_LIST)
		// not completely resolved
		&& !node.hasAttr(NodeAttr.FUNCTION_RESOLVED)
		// but inputs resolved
		&& node.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED)) {
	    // if return type already set return.
	    // this happens on functions passed as parameters that get the type
	    // of the calling function formal parameter
	    // resolve function types by parsing the body
	    curFunctionStatamentList = node.child(NodeType.STATEMENT_LIST);
	    clearFunctionBody();
	    return;
	} else if (curFunctionStatamentList == null)
	    return;
	else if (curFunctionStatamentList.parentOf(node))
	    super.onEnter(aast, node);
    }

    @Override
    public void onExit(AAST aast, AASTNode node, Deque<Object> results) throws GException {
	if (node.type().equals(NodeType.STATEMENT_LIST) && node.equals(curFunctionStatamentList)) {
	    resolveFunctionTypes(curFunctionStatamentList.parent(NodeType.FUNCTION));
	    // reset
	    curFunctionStatamentList = null;
	    return;
	} else if (curFunctionStatamentList == null)
	    return;
	else if (curFunctionStatamentList.parentOf(node))
	    super.onExit(aast, node, results);
    }

    private void resolveFunctionTypes(AASTNode functionNode) {
	List<AASTNode> returnNodes = functionNode.childs(NodeType.FUNCTION_RETURN);

	// function types resolution finished, resolve function output type
	if (returnNodes.size() == 1) {
	    List<AASTNode> stmts;
	    stmts = functionNode.childs(NodeType.STATEMENT_LIST);
	    AASTNode stmt = stmts.get(stmts.size() - 1);
	    // last expr statement is the output value if this is an anonymous function
	    if (stmt.type().equals(NodeType.EXPR_STMT) && stmt.expr() != null
		    && !GType.get(BType.VOID).equals(stmt.expr()) && functionNode.hasAttr(NodeAttr.FUNCTION_TYPE)
		    && ((NodeType) functionNode.attr(NodeAttr.FUNCTION_TYPE)).equals(NodeType.AT)) {
		stmt.attr(NodeAttr.SYMBOL, returnNodes.get(0).name());
		stmt.attr(NodeAttr.TRANSLATE, true);
		// if return node type is already set don't update it, this is not the type of
		// the return variable
		if (!stmt.hasAttr(NodeAttr.SKIP_RETURN_NODE_PROPAGATION))
		    // update return node with the statement output overriding whatever previous
		    // value was set
		    returnNodes.get(0).expr(GType.get(stmt.expr()));
	    }
	}

	int max_exprs = -1;
	for (AASTNode retNode : returnNodes) {
	    if (NodeType.VOID.equals(retNode.type()))
		retNode.expr(GType.get(BType.VOID));
	    else if (retNode.expr() == null)
		retNode.expr(GType.get(GType.get(BType.UNKNOWN)));
	    if (retNode.exprs().size() > max_exprs)
		max_exprs = retNode.exprs().size();
	}
	List<AASTNode> params = functionNode.childs(NodeType.PARAMETER_LIST);
	for (AASTNode paramNode : params) {
	    // update parameters type
	    if (NodeType.VOID.equals(paramNode.type()))
		paramNode.expr(GType.get(BType.VOID));
	    else if (paramNode.expr() == null)
		paramNode.expr(GType.get(GType.get(BType.UNKNOWN)));
	    if (paramNode.exprs().size() > max_exprs)
		max_exprs = paramNode.exprs().size();
	}
	if (max_exprs < 0) {
	    // function without parameters
	    functionNode.expr(GType.get(BType.FUNCTION, new GType[] {}, new GType[] {}));
	} else {
	    List<List<GType>> returnNodesExprs = new ArrayList<>(returnNodes.size());
	    List<List<GType>> paramNodesExprs = new ArrayList<>(params.size());

	    for (int i = 0; i < max_exprs; ++i) {
		// return value
		LinkedList<GType> rtypes = new LinkedList<GType>();
		for (int r = 0; r < returnNodes.size(); ++r) {
		    List<GType> returnExprs = returnNodes.get(r).exprs();
		    if (returnExprs.size() > i) {
			GType newtype = TypeUtils.updateNameType(returnExprs.get(i), returnNodes.get(r));
			rtypes.add(newtype);
			if (i == 0)
			    returnNodesExprs.add(r, new LinkedList<GType>());
			// update also return node ith expression (without propagating type)
			returnNodesExprs.get(r).add(newtype);
			// returnNodes.get(r).expr(newtype, i, false);
		    } else
			rtypes.add(TypeUtils.updateNameType(((LinkedList<GType>) returnExprs).peekLast(),
				returnNodes.get(r)));
		}
		// params
		List<GType> lptypes = new LinkedList<GType>();
		for (int p = 0; p < params.size(); ++p) {
		    List<GType> paramExprs = params.get(p).exprs();
		    if (paramExprs.size() > i) {
			GType newType = TypeUtils.updateNameType(paramExprs.get(i), params.get(p));
			lptypes.add(p, newType);
			// update also param node ith expression (without propagating type)
			if (i == 0)
			    paramNodesExprs.add(p, new LinkedList<GType>());
			paramNodesExprs.get(p).add(newType);
			// params.get(p).expr(newType, i, false);
		    } else
			lptypes.add(p,
				TypeUtils.updateNameType(((LinkedList<GType>) paramExprs).peekLast(), params.get(p)));
		}

		// now I have list of return types and list of input types
		functionNode.expr(GType.get(BType.FUNCTION, lptypes, rtypes));
	    }

	    // update nodes
	    for (int r = 0; r < returnNodes.size(); ++r)
		returnNodes.get(r).exprs(returnNodesExprs.get(r), false);

	    for (int p = 0; p < params.size(); ++p)
		params.get(p).exprs(paramNodesExprs.get(p), false);

	}

	checkAndFlagFunctionReturnNode(functionNode);
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
