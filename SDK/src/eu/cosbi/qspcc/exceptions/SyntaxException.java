package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;

public class SyntaxException extends GException {
    /**
     * 
     */
    private static final long serialVersionUID = -6698329488471540299L;

    public SyntaxException(ErrorCode error, AASTNode node, Object... params) {
	super(error, node, params);
    }

}
