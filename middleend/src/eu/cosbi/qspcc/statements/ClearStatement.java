package eu.cosbi.qspcc.statements;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.exceptions.StatementStackException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.exceptions.UnfinishedTranslationException;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.type.StatementType;

@StatementClass(category = StatementClass.Type.CLEAR)
public class ClearStatement extends Statement {

    public ClearStatement(AAST aast, AASTNode fullStatement) {
	super(aast, fullStatement);
    }

    @Override
    public StatementType type() {
	return StatementType.FUNCTIONAL;
    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws UndefinedTranslationException,
	    TypeException, UnfinishedTranslationException, StatementStackException, UnboundException {
	// TODO
    }

    @Override
    public void completeStatement()
	    throws UnfinishedTranslationException, UndefinedTranslationException, TypeException, UnboundException {
	// do nothing
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
    }

    @Override
    public void pushTranslation(AASTNode tp) {
    }

}
