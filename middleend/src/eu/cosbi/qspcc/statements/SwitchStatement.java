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

@StatementClass(category = StatementClass.Type.SWITCH)
public class SwitchStatement extends Statement {
    private static Integer globalSwitchNumber = 0;
    private Logger logger = LogManager.getLogger(IfStatement.class);
    boolean thenTranslated = false;
    private AASTNode curSwitch;
    // map condition type (IF,ELSEIF,ELSE)
    // -> condition + Path to the file that
    // contains the
    // then block
    private HashMap<AASTNode, Tuple<ExpressionStatement, FrontEndWalker>> cond2then;

    public SwitchStatement(AAST program, AASTNode tokenValue) {
	super(program, tokenValue);
	cond2then = new LinkedHashMap<AASTNode, Tuple<ExpressionStatement, FrontEndWalker>>();
	init(tokenValue, null, new ExpressionStatement(aast, nodesStack, tokenValue.child(NodeType.EXPRESSION)));
    }

    @Override
    public StatementType type() {
	return StatementType.CONTROL;
    }

    void init(AASTNode node, FrontEndWalker stmtWalker, ExpressionStatement expr) {
	this.curSwitch = node;
	// child mode, share nodesStack with this class
	cond2then.put(node, new Tuple<ExpressionStatement, FrontEndWalker>(expr, stmtWalker));
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	if (node.type().equals(NodeType.CASE))
	    init(node, new FrontEndWalker(aast, node.child(NodeType.STATEMENT_LIST)),
		    new ExpressionStatement(aast, nodesStack, node.child(NodeType.EXPRESSION)));

    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
	if (cond2then.get(curSwitch) == null)
	    // wait for new branch to be set or finalize to be called
	    return;

	Tuple<ExpressionStatement, FrontEndWalker> walktuple = cond2then.get(curSwitch);
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
	    if (stmtWalker != null)
		// annotate each statement_list
		stmtWalker.annotate(false);
	}
    }

    private boolean inExpression(AASTNode node) {
	if (curSwitch == null)
	    return false;

	AASTNode curIfExpr = curSwitch.child(NodeType.EXPRESSION);

	return (curIfExpr != null && curIfExpr.parentOf(node));
    }

    @Override
    public void pushTranslation(AASTNode tp) {
	if (inExpression(tp))
	    cond2then.get(curSwitch).first().pushTranslation(tp);
    }

    @Override
    public void pushParameterList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().pushParameterList(node);
    }

    @Override
    public void popParameterList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().popParameterList(node);
    }

    @Override
    public void updateLastParameterList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().updateLastParameterList(node);
    }

    @Override
    public void resetStructList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().resetStructList(node);
    }

    @Override
    public void updateStructList(AASTNode node) {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().updateStructList(node);
    }

    @Override
    public void initVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().initVectorList(node);
    }

    @Override
    public void updateVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    cond2then.get(curSwitch).first().updateVectorList(node);
    }

}
