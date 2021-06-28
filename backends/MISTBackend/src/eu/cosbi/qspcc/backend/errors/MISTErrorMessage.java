package eu.cosbi.qspcc.backend.errors;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.exceptions.ErrorCode;

public enum MISTErrorMessage implements ErrorCode {
    UNSUPPORTED_TAG_ASSIGNMENT,
    UNSUPPORTED_ODE_ASSIGNMENT,
    CODE_UNSUPPORTED_TAG,
    MATRIX_DIMENSION_UNKNOWN,
    ASSIGNMENT_TYPE_UNSUPPORTED,
    MULTIPLE_PARAMETERS_UNSUPPORTED,
    NON_INT_PARAMETER_UNSUPPORTED,
    UNKNOWN_INT_PARAMETER_VALUE_UNSUPPORTED;

    @Override
    public String stringify(ProgramNode node, Object... params) {
	String location = " in " + node.code() + " line " + node.location() + " at " + node.sourcePath();

	switch (this) {
	case UNSUPPORTED_TAG_ASSIGNMENT:
	    return "Unsupported assignment for tag " + params[0] + " not supported" + location;
	case UNSUPPORTED_ODE_ASSIGNMENT:
	    return "Unsupported assignment for ODE: '" + node.code()
		    + "': left-hand-side should be an access to array with 1 dimension" + location;
	case CODE_UNSUPPORTED_TAG:
	    return "Programming error: tag '" + params[0] + "' not supported " + location;
	case MATRIX_DIMENSION_UNKNOWN:
	    return "Unsupported assignment for tag " + params[0] + ": '" + params[1]
		    + "' matrix dimensions unknown. Skipping." + location;
	case ASSIGNMENT_TYPE_UNSUPPORTED:
	    return "Unsupported assignment for tag " + params[0] + ": '" + params[1] + "' assignment of type '"
		    + params[2] + "' not supported. Skipping." + location;
	case MULTIPLE_PARAMETERS_UNSUPPORTED:
	    return "Undefined translation only one parameter supported " + location;
	case NON_INT_PARAMETER_UNSUPPORTED:
	    return "Undefined translation parameter type should be INT " + location;
	case UNKNOWN_INT_PARAMETER_VALUE_UNSUPPORTED:
	    return "Undefined translation parameter type should be known at compile-time " + location;
	default:
	    return "UNKNOWN_ERROR_MESSAGE";
	}
    }

}
