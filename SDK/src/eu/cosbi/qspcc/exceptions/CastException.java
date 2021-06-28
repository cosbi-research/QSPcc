package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class CastException extends GException {
    private static final long serialVersionUID = -6726855435684662472L;

    public CastException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }

}
