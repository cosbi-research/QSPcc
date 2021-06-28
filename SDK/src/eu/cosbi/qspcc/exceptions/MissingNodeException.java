package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class MissingNodeException extends GException {
    private static final long serialVersionUID = -1617353464383155430L;

    public MissingNodeException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }
}
