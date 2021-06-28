package eu.cosbi.qspcc.statements;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.type.StatementType;

@StatementClass(category = StatementClass.Type.PERSISTENT)
public class PersistentStatement extends Statement {

    public PersistentStatement(AAST aast, AASTNode fullStatement) {
	super(aast, fullStatement);
    }

    @Override
    public StatementType type() {
	return StatementType.FUNCTIONAL;
    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
	switch (node.type()) {
	case ID:
	    if (node.expr() == null)
		node.expr(GType.get(BType.UNKNOWN));
	    return;
	}
    }

    @Override
    public void completeStatement() throws GException {
	// TODO Auto-generated method stub

    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	// TODO Auto-generated method stub

    }

    @Override
    public void pushTranslation(AASTNode tp) {
	// TODO Auto-generated method stub

    }

}
