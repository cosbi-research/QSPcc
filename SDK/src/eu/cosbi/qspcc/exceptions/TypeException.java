package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.ProgramNode;

public class TypeException extends GException {
    private static final long serialVersionUID = -6440475063914490763L;

    public TypeException(ErrorCode error, ProgramNode node, Object... params) {
	super(error, node, params);
    }
}
