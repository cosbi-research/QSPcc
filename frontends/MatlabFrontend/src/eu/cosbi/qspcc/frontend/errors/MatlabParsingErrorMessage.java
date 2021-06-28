package eu.cosbi.qspcc.frontend.errors;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.exceptions.ErrorCode;

public enum MatlabParsingErrorMessage implements ErrorCode {
    SYNTAX_ERROR_PARSING_TREE,
    UNEXPECTED_TOKEN;

    @Override
    public String stringify(ProgramNode node, Object... params) {
	String location;
	if (node == null)
	    location = "";
	else
	    location = " in " + node.code() + " line " + node.location() + " at " + node.sourcePath();

	switch (this) {
	case SYNTAX_ERROR_PARSING_TREE:
	    return "Error parsing input program: " + params[0] + location;
	case UNEXPECTED_TOKEN:
	    if (node != null)
		return "Unexpected syntax error in " + node.code() + location;
	    else
		return "Unexpected syntax error in source file";
	default:
	    return "UNKNOWN_STRING_FOR_ERROR";
	}
    }

}
