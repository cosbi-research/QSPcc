package eu.cosbi.qspcc.tree.listeners;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.MiddleEndPass;
import eu.cosbi.utils.TypeUtils;

/**
 * 1. remove AT nodes and replaces them with equivalent function structure. Give
 * also them a name. This step allows subsequent steps to skip anonymous
 * functions vs ordinary function distinction
 * 
 * 2. add flag IS_ENV_PARAM_NAMES to ids in functions that are not defined in
 * function and are not function parameters.
 * 
 * 3. add missing PARAMETER_LIST and FUNCTION_RETURN to functions without it.
 * @author tomasoni
 *
 */
public class FunctionsNormalizerWalker implements DAGListener<AAST, AASTNode, Object>, MiddleEndPass {
    Logger logger = LogManager.getLogger(FunctionsNormalizerWalker.class);
    // AAST we are annotating
    protected AAST mainAAST;
    private boolean stopOnError;
    private static int fnum = 0;
    private Map<String, AASTNode> funNodes;
    private IFunction[] coreFunctions;
    protected static Pattern anonFunctionNamePattern = Pattern.compile("\\s+(\\w+)\\(.*?(?=\\)|\\.\\.\\.)");

    public FunctionsNormalizerWalker(AAST main, IFunction[] coreFunctions) {
	mainAAST = main;
	funNodes = new HashMap<String, AASTNode>();
	this.coreFunctions = coreFunctions;
    }

    @Override
    public void annotate(boolean stopOnError) throws GException {
	this.stopOnError = stopOnError;
	mainAAST.rootNode().walkFor(this, stopOnError);
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	switch (node.type()) {
	case FUNCTION:
	    //remember this function
	    funNodes.put(node.child(NodeType.ID).name(), node);
	    if (!node.childExists(NodeType.FUNCTION_RETURN)) {
		//create it
		AASTNode fret = new AASTNode(mainAAST, NodeType.FUNCTION_RETURN, "", node.lineNumber(),
			node.colNumber(), node);
		node.addChild(fret);
		AASTNode voidNode = new AASTNode(mainAAST, NodeType.VOID, "", node.lineNumber(), node.colNumber(),
			fret);
		fret.addChild(voidNode);
	    }
	    List<AASTNode> functionReturnValues = node.childs(NodeType.FUNCTION_RETURN);
	    List<AASTNode> functionParameters = node.childs(NodeType.PARAMETER_LIST);

	    List<String> formalParamNames = new ArrayList<String>();
	    if (!node.childExists(NodeType.PARAMETER_LIST)) {
		//create it
		AASTNode fparam = new AASTNode(mainAAST, NodeType.PARAMETER_LIST, "", node.lineNumber(),
			node.colNumber(), node);
		functionParameters = new ArrayList<AASTNode>(1);
		// maintain correct position.. just in case backends will use the position
		AASTNode stmts = node.child(NodeType.STATEMENT_LIST);
		node.removeChild(stmts);
		node.addChild(fparam);
		node.addChild(stmts);
		AASTNode voidNode = new AASTNode(mainAAST, NodeType.VOID, "", node.lineNumber(), node.colNumber(),
			fparam);
		functionParameters.add(voidNode);
		fparam.addChild(voidNode);
	    } else {
		for (AASTNode param : functionParameters)
		    if (!param.type().equals(NodeType.VOID))
			formalParamNames.add(param.name());
	    }
	    node.attr(NodeAttr.FUNCTION_PARAMS, formalParamNames);

	    List<String> params = new ArrayList<String>(functionParameters.size());
	    for (AASTNode param : functionParameters)
		if (!param.type().equals(NodeType.VOID))
		    params.add(param.name());
	    boolean containsAll = true;
	    boolean containThis;
	    for (AASTNode ret : functionReturnValues) {
		containThis = params.contains(ret.name());
		if (containThis)
		    ret.attr(NodeAttr.IS_OUTPUT_INPUT, true);
		containsAll &= containThis;
	    }

	    if (containsAll)
		// if return values are in the input parameters
		node.attr(NodeAttr.FUN_OUTPUT_IS_INPUT, true);
	    break;
	case AT:
	    // change node type to function if not function.
	    // it can be AT for example but to avoid discriminating every
	    // time between function and at we join them an treat them the same
	    // way
	    // list of statements of this anonmymous function
	    AASTNode stmtListNode = node.child(NodeType.EXPRESSION);

	    if (stmtListNode != null) {
		// if an assign node exists we are in case
		// a = @(x,y) ...
		// mutate to a full function definition
		Integer assignDepth = node.parentDepth(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN });
		Integer parameterDepth = node.parentDepth(NodeType.FUNCTION_PARAMETER_LIST);
		// avoid taking a top-level assign after a function parameter list
		// see for example [t,y] = ode(@(t,y).., tspan, ..)
		AASTNode assignNode = null;
		if (assignDepth != null && (parameterDepth == null || assignDepth < parameterDepth))
		    assignNode = node.parent(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN });

		node.attr(NodeAttr.FUNCTION_TYPE, node.type());
		node.type(NodeType.FUNCTION);

		// create function id node
		// try to find a meaningful function name
		String anonFunctionName = null;
		// create a function return node
		String outSymbol = null;

		if (assignNode == null) {
		    anonFunctionName = "at_" + Integer.toString(fnum);
		    outSymbol = "at_" + Integer.toString(fnum) + "_out";
		    fnum++;
		} else {
		    AASTNode idNode = TypeUtils.getIDNode(assignNode.childs(NodeType.LHS).get(0));
		    if (idNode == null || !idNode.type().equals(NodeType.ID)) {
			logger.debug("Missing LHS id on " + assignNode.code() + " at " + assignNode.location());
			anonFunctionName = "at_" + Integer.toString(fnum);
			outSymbol = "at_" + Integer.toString(fnum) + "_out";
			fnum++;
		    } else {
			anonFunctionName = idNode.code();
			outSymbol = anonFunctionName + "_out";
		    }
		    // remove assignNode substitute with this
		    AASTNode parentAssign = assignNode.parent();
		    int assignPos = parentAssign.childPosition(assignNode);
		    parentAssign.removeChild(assignNode);
		    parentAssign.addChild(node, assignPos);
		    // change parent
		    node.parent(parentAssign);
		}

		if (assignNode == null) {
		    Matcher m = anonFunctionNamePattern.matcher(node.name());
		    if (m.find())
			anonFunctionName += "_" + node.name().substring(m.start(1), m.end(1));
		}
		node.attr(NodeAttr.SYMBOL, anonFunctionName);

		AASTNode parameterList = node.child(NodeType.PARAMETER_LIST);
		node.deleteChild(parameterList);

		AASTNode id = new AASTNode(mainAAST, NodeType.ID, anonFunctionName, node.lineNumber(), node.colNumber(),
			node);
		node.addChild(id);

		// re-add new parameter list
		AASTNode newParamList = new AASTNode(mainAAST, NodeType.PARAMETER_LIST, parameterList.code(),
			node.lineNumber(), node.colNumber(), node);
		node.addChild(newParamList);
		formalParamNames = new ArrayList<String>();
		// move params
		for (AASTNode param : parameterList.childs()) {
		    AASTNode newparam = new AASTNode(param.compilationUnit(), param.type(), param.code(),
			    param.lineNumber(), param.colNumber(), newParamList);
		    formalParamNames.add(param.code());
		    newParamList.addChild(newparam);
		    // remove it from tree
		    param.unlink();
		}
		node.attr(NodeAttr.FUNCTION_PARAMS, formalParamNames);
		// remove it from tree
		parameterList.unlink();

		AASTNode funret = new AASTNode(mainAAST, NodeType.FUNCTION_RETURN, outSymbol, node.lineNumber(),
			node.colNumber(), node);
		node.addChild(funret);
		id = new AASTNode(mainAAST, NodeType.ID, outSymbol, node.lineNumber(), node.colNumber(), funret);
		id.attr(NodeAttr.SYMBOL, outSymbol);
		funret.addChild(id);

		// create statement list
		AASTNode stmt = new AASTNode(mainAAST, NodeType.STATEMENT_LIST, stmtListNode.name(), 0, 0, node);
		node.addChild(stmt);
		AASTNode exprstmt = new AASTNode(mainAAST, NodeType.EXPR_STMT, stmtListNode.name(), 0, 0, stmt);
		stmt.addChild(exprstmt);
		// change parent
		//stmtListNode.parent(exprstmt);
		// remove from functionNode childs
		node.deleteChild(stmtListNode);

		// add to childs of new node, and remove old ones
		copySubTree(exprstmt, stmtListNode);

		// this means also this at function is useless.
		// because it just calls a single function. remember this.
		AASTNode applyExpression = (AASTNode) exprstmt.child(NodeType.APPLY);
		if (applyExpression != null)
		    node.attr(NodeAttr.APPLY_FUNCTION, applyExpression);

		// NEWLINE needed to reset statement
		exprstmt.addChild(new AASTNode(mainAAST, NodeType.NL, "", 0, 0, exprstmt));
	    } else {
		// reference to another already-existing named function
		// the REF_FUNCTION node attr will be set by env walker
		if (!node.childExists(NodeType.ID))
		    if (node.childExists(NodeType.APPLY)) {
			AASTNode apply = null;
			for (AASTNode c : node.childs())
			    if (c.type().equals(NodeType.APPLY)) {
				apply = c;
				break;
			    }
			node.removeChild(apply);
			AASTNode id = apply.child(NodeType.ID);
			node.addChild(id);
			id.parent(node);
		    } else
			throw new UndefinedTranslationException(ErrorMessage.INTERNAL_AT_NODE_STRUCTURE, node);
	    }
	    break;
	default:
	}

    }

    private void copySubTree(AASTNode target, AASTNode source) {
	AASTNode parent = target;
	AASTNode curNode = source;
	for (AASTNode child : curNode.childs()) {
	    AASTNode newchild = new AASTNode(child.compilationUnit(), child.type(), child.code(), child.lineNumber(),
		    child.colNumber(), parent);
	    parent.addChild(newchild);
	    copySubTree(newchild, child);
	    // remove it from tree
	    child.unlink();
	}
	// unlink source in the end
	curNode.unlink();
    }

    @Override
    public void onExit(AAST ref, AASTNode curNode, Deque<Object> results) throws GException {
	// TODO Auto-generated method stub

    }

    @Override
    public void onWalkCompleted(AAST ref) throws GException {
    }

    @Override
    public void onWalkStarted(AAST ref) throws GException {
	// TODO Auto-generated method stub

    }

    public Map<String, AASTNode> functionNodes() {
	return funNodes;
    }

}
