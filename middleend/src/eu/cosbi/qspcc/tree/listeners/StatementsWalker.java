package eu.cosbi.qspcc.tree.listeners;

import java.util.Deque;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;

/**
 * Re-parse each function that now have all the parameter types set and resolve
 * all the unknown types (including global variables referenced).
 * 
 * @author tomasoni
 *
 */
public class StatementsWalker extends FrontEndWalker {
    private AASTNode curStatement;

    public StatementsWalker(AAST mainAAST) {
	super(mainAAST);
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	if (curStatement == null
		// should be reparsed
		&& node.hasAttr(NodeAttr.REPARSE_STATEMENT)) {
	    // resolve function types
	    curStatement = node;
	    super.onEnter(aast, node);
	} else if (curStatement == null)
	    return;
	else
	    super.onEnter(aast, node);
    }

    @Override
    public void onExit(AAST aast, AASTNode node, Deque<Object> results) throws GException {
	if (node.hasAttr(NodeAttr.REPARSE_STATEMENT) && node.equals(curStatement)) {
	    // complete statement
	    super.onExit(aast, node, results);

	    checkAndMarkAsResolved(node);
	    // reset
	    curStatement = null;
	    return;
	} else if (curStatement == null)
	    return;
	else
	    super.onExit(aast, node, results);
    }

    private void checkAndMarkAsResolved(AASTNode node) {
	// allow being re-set to be reparsed if needed
	// for funexpr in functionnode, for returntype in funexpr check if
	// some unknown -> remove attr/setattr otherwise don't remove

	if (allNodesSet(true, node)) {
	    node.removeAttr(NodeAttr.REPARSE_STATEMENT);
	    AASTNode n = node.parent(NodeType.FUNCTION);
	    if (n == null)
		n = new AASTNode(null, null, null);
	    node.attr(NodeAttr.STATEMENT_RESOLVED, n);
	}
    }

    private boolean allNodesSet(boolean allset, AASTNode node) {
	allset &= node.expr() == null || !node.expr().equals(BType.UNKNOWN);
	for (AASTNode curChild : node.childs()) {
	    if (!allset)
		return false;
	    allset &= allNodesSet(allset, curChild);
	}
	return allset;
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
