package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class UnboundException extends GException {
    private static final long serialVersionUID = -6717583143495862512L;

    public UnboundException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }

}
