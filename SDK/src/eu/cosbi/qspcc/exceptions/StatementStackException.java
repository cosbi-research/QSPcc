package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class StatementStackException extends GException {
    private static final long serialVersionUID = -1617356594383155430L;

    public StatementStackException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }
}
