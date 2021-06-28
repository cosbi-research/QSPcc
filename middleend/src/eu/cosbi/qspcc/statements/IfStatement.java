package eu.cosbi.qspcc.statements;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.tree.listeners.FrontEndWalker;
import eu.cosbi.qspcc.type.StatementType;
import eu.cosbi.utils.Tuple;

@StatementClass(category = StatementClass.Type.IF)
public class IfStatement extends Statement {
    private static Integer globalIfNumber = 0;
    private Logger logger = LogManager.getLogger(IfStatement.class);
    boolean thenTranslated = false;
    private AASTNode curIf;
    // map condition type (IF,ELSEIF,ELSE)
    // -> condition + Path to the file that
    // contains the
    // then block
    private HashMap<AASTNode, Tuple<ExpressionStatement, FrontEndWalker>> cond2then;

    public IfStatement(AAST program, AASTNode tokenValue) {
	super(program, tokenValue);
	cond2then = new LinkedHashMap<AASTNode, Tuple<ExpressionStatement, FrontEndWalker>>();
	init(tokenValue, new FrontEndWalker(aast, tokenValue.child(NodeType.STATEMENT_LIST)),
		new ExpressionStatement(aast, nodesStack, tokenValue.child(NodeType.EXPRESSION)));
    }

    @Override
    public StatementType type() {
	return StatementType.CONTROL;
    }

    void init(AASTNode node, FrontEndWalker stmtWalker, ExpressionStatement expr) {
	this.curIf = node;
	// child mode, share nodesStack with this class
	cond2then.put(node, new Tuple<ExpressionStatement, FrontEndWalker>(expr, stmtWalker));
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	if (node.type().equals(NodeType.ELSEIF))
	    init(node, new FrontEndWalker(aast, node.child(NodeType.STATEMENT_LIST)),
		    new ExpressionStatement(aast, nodesStack, node.child(NodeType.EXPRESSION)));
	else if (node.type().equals(NodeType.ELSE))
	    init(node, new FrontEndWalker(aast, node.child(NodeType.STATEMENT_LIST)), null);

    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
	if (cond2then.get(curIf) == null)
	    // wait for new branch to be set or finalize to be called
	    return;

	Tuple<ExpressionStatement, FrontEndWalker> walktuple = cond2then.get(curIf);
	ExpressionStatement stmt = walktuple.first();

	if (inExpression(node)) {
	    stmt.translateImpl(aast, node, value);
	} else {
	    // translate statement_list deferred
	}
    }

    @Override
    public void completeStatement() throws GException {
	boolean markedToBeReparsed = false;
	for (Entry<AASTNode, Tuple<ExpressionStatement, FrontEndWalker>> ifStmtPart : cond2then.entrySet()) {
	    Tuple<ExpressionStatement, FrontEndWalker> walktuple = ifStmtPart.getValue();
	    if (!markedToBeReparsed && walktuple.first() != null && walktuple.first().shouldBeReparsed()) {
		markToBeReparsed();
		markedToBeReparsed = true;
	    }
	    FrontEndWalker stmtWalker = walktuple.second();
	    // annotate each statement_list
	    stmtWalker.annotate(false);
	}

	if (!markedToBeReparsed)
	    // if statement completed
	    markAsCompleted();
    }

    private boolean inExpression(AASTNode node) {
	if (curIf == null)
	    return false;

	AASTNode curIfExpr = curIf.child(NodeType.EXPRESSION);

	return (curIfExpr != null && curIfExpr.parentOf(node));
    }

    @Override
    public void pushTranslation(AASTNode tp) {
	if (inExpression(tp))
	    cond2then.get(curIf).first().pushTranslation(tp);
    }

    @Override
    public void pushParameterList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curIf).first().pushParameterList(node);
    }

    @Override
    public void popParameterList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curIf).first().popParameterList(node);
    }

    @Override
    public void updateLastParameterList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curIf).first().updateLastParameterList(node);
    }

    @Override
    public void resetStructList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curIf).first().resetStructList(node);
    }

    @Override
    public void updateStructList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curIf).first().updateStructList(node);
    }

    @Override
    public void initVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    cond2then.get(curIf).first().initVectorList(node);
    }

    @Override
    public void updateVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    cond2then.get(curIf).first().updateVectorList(node);
    }

}
