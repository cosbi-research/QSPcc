package eu.cosbi.qspcc.backend.errors;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.exceptions.ErrorCode;

public enum RErrorMessage implements ErrorCode {
    TRANSLATION_NOT_IMPLEMENTED,
    NOT_UNARY_OPERAND,
    UNKNOWN_OPERAND,
    CORE_FUNCTION_DOESNT_EXIST,
    CORE_FUNCTION_RETURNED_NULL_TRANSLATION,
    CORE_FUNCTION_WITHOUT_ARGUMENT_UNSUPPORTED,
    FUNCTION_WITHOUT_OUTPUTS,
    FUNCTION_REFERENCE_WITHOUT_REFERENCE;

    @Override
    public String stringify(ProgramNode node, Object... params) {
	String location = null;
	if (node != null)
	    location = "\n> " + node.code() + "\nFile: " + node.sourcePath() + " line: " + node.location() + "\n";
	else
	    location = "";

	switch (this) {
	case TRANSLATION_NOT_IMPLEMENTED:
	    return "R translation not implemented" + location;
	case FUNCTION_REFERENCE_WITHOUT_REFERENCE:
	    return "Tree error, function reference '" + params[0] + "' references a non-existing function" + location;
	case NOT_UNARY_OPERAND:
	    return "not an unary operand '" + params[0] + "'" + location;
	case UNKNOWN_OPERAND:
	    return "unknown operand" + location;
	case CORE_FUNCTION_DOESNT_EXIST:
	    return "Function '" + params[0] + "' is not a core function" + location;
	case CORE_FUNCTION_WITHOUT_ARGUMENT_UNSUPPORTED:
	    return "Undefined translation for '" + params[0] + "' statement without arguments" + location;
	case FUNCTION_WITHOUT_OUTPUTS:
	    return "Can't translate return statement for function without outputs " + location;
	case CORE_FUNCTION_RETURNED_NULL_TRANSLATION:
	    return "Can't translate core function '" + params[0] + "'" + location;
	default:
	    return "UNKNOWN_STRING_FOR_ERROR";
	}
    }

}
