package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class UnfinishedTranslationException extends GException {
    private static final long serialVersionUID = -3787671081029609517L;

    public UnfinishedTranslationException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }

}
