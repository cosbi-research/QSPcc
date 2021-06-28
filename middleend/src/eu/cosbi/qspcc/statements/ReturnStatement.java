package eu.cosbi.qspcc.statements;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.type.StatementType;

/*
 * No extra information needed for this node
 * */
@StatementClass(category = StatementClass.Type.RETURN)
public class ReturnStatement extends Statement {

    public ReturnStatement(AAST aast, AASTNode fullStatement) {
	super(aast, fullStatement);
    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
    }

    @Override
    public void completeStatement() throws GException {
	if (!statement.parentExists(NodeType.FUNCTION))
	    throw new SyntaxException(ErrorMessage.INTERNAL_RETURN_WITHOUT_FUNCTION, statement);
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	// TODO Auto-generated method stub

    }

    @Override
    public void pushTranslation(AASTNode tp) {
	// TODO Auto-generated method stub

    }

    @Override
    public StatementType type() {
	return StatementType.FUNCTIONAL;
    }

}
