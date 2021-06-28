package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class UndefinedTranslationException extends GException {
    private static final long serialVersionUID = -2560074773763277728L;

    public UndefinedTranslationException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }

    public UndefinedTranslationException(Throwable e) {
	super(e);
    }

}
