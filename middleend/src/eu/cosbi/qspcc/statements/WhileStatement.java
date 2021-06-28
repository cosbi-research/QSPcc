package eu.cosbi.qspcc.statements;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.tree.listeners.FrontEndWalker;
import eu.cosbi.qspcc.type.StatementType;

@StatementClass(category = StatementClass.Type.WHILE)
public class WhileStatement extends Statement {
    AASTNode boolExpr, statement_list;
    ExpressionStatement boolExprStatement;
    FrontEndWalker statementListWalker;

    public WhileStatement(AAST program, AASTNode tokenValue) {
	super(program, tokenValue);
	boolExpr = tokenValue.child(NodeType.EXPRESSION);
	boolExprStatement = new ExpressionStatement(aast, nodesStack, boolExpr);
	statement_list = tokenValue.child(NodeType.STATEMENT_LIST);
	statementListWalker = new FrontEndWalker(aast, statement_list);
    }

    @Override
    public StatementType type() {
	return StatementType.CONTROL;
    }

    private boolean inExpression(AASTNode node) {
	if (boolExpr == null)
	    return false;

	return boolExpr.parentOf(node);
    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
	if (inExpression(node))
	    boolExprStatement.translateImpl(aast, node, value);

	// statement list translation deferred
    }

    @Override
    public void completeStatement() throws GException {
	boolean reparse = boolExprStatement.shouldBeReparsed();
	if (reparse)
	    markToBeReparsed();
	else
	    // while statement completed
	    markAsCompleted();

	// maybe this can be put only in the reparse false branch?
	statementListWalker.annotate(false);
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	// not needed
    }

    @Override
    public void pushTranslation(AASTNode tp) {
	if (inExpression(tp))
	    boolExprStatement.pushTranslation(tp);
    }

    @Override
    public void pushParameterList(AASTNode node) {
	if (inExpression(node))
	    boolExprStatement.pushParameterList(node);
    }

    @Override
    public void popParameterList(AASTNode node) {
	if (inExpression(node))
	    boolExprStatement.popParameterList(node);
    }

    @Override
    public void updateLastParameterList(AASTNode node) {
	if (inExpression(node))
	    boolExprStatement.updateLastParameterList(node);
    }

    @Override
    public void resetStructList(AASTNode node) {
	if (inExpression(node))
	    boolExprStatement.resetStructList(node);
    }

    @Override
    public void updateStructList(AASTNode node) {
	if (inExpression(node))
	    boolExprStatement.updateStructList(node);
    }

    @Override
    public void initVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    boolExprStatement.initVectorList(node);
    }

    @Override
    public void updateVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    boolExprStatement.updateVectorList(node);
    }

}
