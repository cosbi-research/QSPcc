package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.ProgramNode;

public class InputException extends GException {

    private static final long serialVersionUID = -7831929981333800428L;

    public InputException(ErrorCode error, ProgramNode node, Object... params) {
	super(error, node, params);
    }

    public InputException(Throwable e) {
	super(e);
    }

}
