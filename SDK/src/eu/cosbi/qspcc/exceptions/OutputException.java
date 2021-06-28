package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.ProgramNode;

public class OutputException extends GException {
    private static final long serialVersionUID = 7950270504575684702L;

    public OutputException(ErrorCode error, ProgramNode node, Object... params) {
	super(error, node, params);
    }
}
