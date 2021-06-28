package eu.cosbi.qspcc.statements;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.tree.listeners.FrontEndWalker;
import eu.cosbi.qspcc.type.StatementType;

@StatementClass(category = StatementClass.Type.FOR)
public class ForStatement extends Statement {
    AASTNode idx, idxExpr, stmt_list;
    ExpressionStatement idxExprStatement;
    private FrontEndWalker statementListWalker;

    public ForStatement(AAST aast, AASTNode fullStatement) {
	super(aast, fullStatement);
	idx = fullStatement.child(NodeType.ID);
	idxExpr = fullStatement.child(NodeType.EXPRESSION);
	stmt_list = fullStatement.child(NodeType.STATEMENT_LIST);

	// sub-walkers
	idxExprStatement = new ExpressionStatement(aast, nodesStack, idxExpr);
	statementListWalker = new FrontEndWalker(aast, stmt_list);
    }

    private boolean inExpression(AASTNode node) {
	if (idxExpr == null)
	    return false;

	return idxExpr.parentOf(node);
    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
	if (inExpression(node))
	    idxExprStatement.translateImpl(aast, node, value);

	// statement list translation deferred
    }

    @Override
    public void completeStatement() throws GException {
	//define idx type
	if (idxExprStatement.shouldBeReparsed())
	    markToBeReparsed();
	AASTNode exprNode = idxExprStatement.getExpressionIdentifier();
	if (exprNode.expr().equals(BType.UNKNOWN)) {
	    // still can't resolve statement list.
	    idx.expr(GType.get(BType.UNKNOWN), false);
	    markToBeReparsed();
	    return;
	} else if (!(exprNode.expr() instanceof DimensionType))
	    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_FOR_LOOP_EXPRESSION, exprNode);

	DimensionType exprType = (DimensionType) exprNode.expr();

	if (exprType.of().equals(BType.UNKNOWN)) {
	    markToBeReparsed();
	    return;
	} else if (!(exprType.of() instanceof ValuedType))
	    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_FOR_LOOP_EXPRESSION, exprNode);

	ValuedType<?> ofType = (ValuedType<?>) exprType.of();
	// loop variables should always be generic since they change value at every iteration
	ofType.deleteValue();
	idx.expr(ofType, false);
	// propagate type to this subexpression only
	stmt_list.propagateAttribute(NodeAttr.ETYPE, ofType, idx.name());
	// ready to resolve statement list
	statementListWalker.annotate(false);
	// for statement completed
	markAsCompleted();
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	// not needed
    }

    @Override
    public void pushTranslation(AASTNode tp) {
	if (inExpression(tp))
	    idxExprStatement.pushTranslation(tp);
    }

    @Override
    public StatementType type() {
	return StatementType.CONTROL;
    }

    @Override
    public void pushParameterList(AASTNode node) {
	if (inExpression(node))
	    idxExprStatement.pushParameterList(node);
    }

    @Override
    public void popParameterList(AASTNode node) {
	if (inExpression(node))
	    idxExprStatement.popParameterList(node);
    }

    @Override
    public void updateLastParameterList(AASTNode node) {
	if (inExpression(node))
	    idxExprStatement.updateLastParameterList(node);
    }

    @Override
    public void resetStructList(AASTNode node) {
	if (inExpression(node))
	    idxExprStatement.resetStructList(node);
    }

    @Override
    public void updateStructList(AASTNode node) {
	if (inExpression(node))
	    idxExprStatement.updateStructList(node);
    }

    @Override
    public void initVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    idxExprStatement.initVectorList(node);
    }

    @Override
    public void updateVectorList(AASTNode node) throws GException {
	if (inExpression(node))
	    idxExprStatement.updateVectorList(node);
    }

}
