package eu.cosbi.qspcc.tree.listeners;

import java.util.Deque;
import java.util.List;
import java.util.Map;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.interfaces.MiddleEndPass;

/**
 * Be sure that the ID nodes in APPLY [ f() ] own the same type as the function
 * node [ function y=f(x) ] Be sure that all used functions have a type.
 * 
 * @author tomasoni
 *
 */
public class FunctionReferencesWalker implements DAGListener<AAST, AASTNode, Object>, MiddleEndPass {

    private AAST mainAAST;
    private boolean stopOnError;
    private Map<String, AASTNode> functions;

    public FunctionReferencesWalker(AAST main, Map<String, AASTNode> functions) {
	mainAAST = main;
	this.functions = functions;
    }

    @Override
    public void annotate(boolean stopOnError) throws GException {
	this.stopOnError = stopOnError;
	mainAAST.rootNode().walkFor(this, stopOnError);
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	switch (node.type()) {
	case ID:
	    if (functions.containsKey(node.name()) && node.parentExists(NodeType.APPLY)) {
		AASTNode applyNode = node.parent(NodeType.APPLY);
		AASTNode funNode = aast.functionSymbolNode(node);
		node.exprs(funNode.exprs(), false);
		// check for functions called but never defined (due to wrong extra env
		// parameters)
		if (funNode.hasAttr(NodeAttr.FUNCTION_CALLS)) {
		    // consider only functions called at least once,
		    // the others are just ignored
		    if (!funNode.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED)
			    && !funNode.hasAttr(NodeAttr.FUNCTION_RESOLVED)) {
			// this function was called, but inputs never resolved.
			// if we are here this means that this function was never called with the
			// complete set of parameters,
			// but was called with less parameters than expected. This means that we wrongly
			// set some UNDEFINED nodes to environment parameters
			if (funNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS)) {
			    List<AASTNode> envParams = ((List<AASTNode>) funNode.attr(NodeAttr.FUNCTION_ENV_PARAMS));
			    throw new UndefinedTranslationException(ErrorMessage.FUN_ENV_PARAM_NEVER_DEFINED, applyNode,
				    applyNode.code(), funNode.child(NodeType.ID).code(), funNode.location(), envParams);
			}
		    }
		}
	    }
	default:
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
