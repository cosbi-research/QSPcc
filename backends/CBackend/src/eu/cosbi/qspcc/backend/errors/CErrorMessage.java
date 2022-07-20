package eu.cosbi.qspcc.backend.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.backend.C;
import eu.cosbi.qspcc.exceptions.ErrorCode;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.utils.Constants;

/**
 * ERROR LEVELS - Assign one, and only one, most appropriate level to each enum:
 *  - INTERNAL__    : something is known to be not correctly handled internally and needs intervention
 *  - UNSUPPORTED__ : user constuct currently not supported. Intervention need to extend the language in some way https://git.io/J35vG
 *  - USER__        : error deemed to be rooted in the user's  syntax or semantics
 *  - WARN__        : warning, not an error
 * TODO Some errors are matlab-centric
 *      ex. SLICE_*
 *      Keep here only generic error messages and implement a new frontend-specific subclass with matlab/python/r-specific errors
 * @author tomasoni
 *
 */
public enum CErrorMessage implements ErrorCode {
    INTERNAL_ACCESS_WITH_UNDEFINED_SLICE,
    INTERNAL_APPLY_TO_NON_MATRIX,
    INTERNAL_APPLY_TYPE_UNDEFINED,
    INTERNAL_ASSIGN_INCOMPATIBLE_TYPES,
    INTERNAL_ASSIGN_TO_MATRIX_WITH_MULTIPLE_ROWS,
    INTERNAL_BOOLEAN_EXPRESSION_TYPES,
    INTERNAL_CONTROL_STATEMENT_CONDITION_UNDEFINED,
    INTERNAL_CREATE_UNDEFINED_SLICE,
    INTERNAL_DOT_PRODUCT_BETWEEN_NON_2D_MATRICES,
    INTERNAL_FOR_NEEDS_INDEX_AND_EXPRESSION,
    INTERNAL_FRONTEND_FUNCTION_OUTPUT,
    INTERNAL_FUNCTION_REFERENCE_WITHOUT_REFERENCE,
    INTERNAL_LINEAR_SOLVER_BETWEEN_NON_2D_MATRICES,
    INTERNAL_MATRIX_DIMENSIONS,
    INTERNAL_MATRIX_VECTOR_DIMENSION_UNKNOWN,
    INTERNAL_METHOD_TYPE_UNSUPPORTED,
    INTERNAL_MISMATCHED_MATRIX_ACCESS,
    INTERNAL_MISSING_LHS_TRANSLATION,
    INTERNAL_MISSING_PARAMETER_CONTEXT,
    INTERNAL_MULTIPLE_OUTPUT_SYMBOLS_NOT_STRUCT_POINTER,
    INTERNAL_NOT_UNARY_OPERAND,
    INTERNAL_NULL_ARGUMENT,
    INTERNAL_NULL_VALUE,
    INTERNAL_OUTPUT_MATRIX_DIMENSIONS,
    INTERNAL_OUTPUT_TYPE,
    INTERNAL_SIZE_OF_NON_MATRIX,
    INTERNAL_SLICE_UNSUPPORTED,
    INTERNAL_SLICE_WITH_MORE_THAN_2_DIMENSIONS,
    INTERNAL_STRUCT_ASSIGN_INVALID_LHS_NUMBER,
    INTERNAL_SUNDIALS_WRONG_INTEGRAND_FUNCTION_RETURN_TYPE,
    INTERNAL_SUNDIALS_WRONG_INTEGRAND_FUNCTION_TYPE,
    INTERNAL_TRANSPOSE_OF_MATRIX_WITHOUT_DIMENSIONS,
    INTERNAL_TREE_FUNCTION_NAME_MISSING,
    INTERNAL_TREE_FUNCTION_PARAMETER_LIST_MISSING,
    INTERNAL_TREE_FUNCTION_RETURN_VALUE_MISSING,
    INTERNAL_TREE_WALK_EXCEPTION,
    INTERNAL_UNDEFINED_VISIBILITY_LEVEL,
    INTERNAL_USER_FUNCTION_NOT_FOUND,
    INTERNAL_WARN_PARAMETER_TYPE_UNSUPPORTED,
    TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONINT_ARGUMENT,
    TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSCALAR_ARGUMENT,
    TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSTRING_ARGUMENT,
    TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS,
    TODO_INTERNAL_SLICE_TYPE,
    TODO_USER_ACCESS_INDEXES_GREATER_THAN_MATRIX_DIMENSIONS,
    TODO_USER_WARN_PARAMETER_TYPE,
    UNSUPPORTED_CONTROL_STATEMENT_CONDITION,
    UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
    UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS,
    UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
    UNSUPPORTED_FRONTEND_FUNCTION,
    UNSUPPORTED_FUN_ACTUAL_PARAM_DONT_MATCH,
    UNSUPPORTED_LOOP,
    UNSUPPORTED_MATRIX_ACCESS,
    UNSUPPORTED_MATRIX_DIMENSIONS_INCONSISTENT,
    UNSUPPORTED_MATRIX_ELEMENT_TYPE,
    UNSUPPORTED_MATRIX_MATRIX_OPERATION,
    UNSUPPORTED_NODE,
    UNSUPPORTED_OPERAND,
    UNSUPPORTED_OPERATOR_TYPES,
    UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION,
    UNSUPPORTED_SCALAR_SLICE_OPERATION,
    UNSUPPORTED_SPARSE_MATRIX_OPERATION,
    UNSUPPORTED_TRANSPOSE_OF_NDIMENSIONAL_MATRIX,
    UNSUPPORTED_TRANSPOSE_OF_NONMATRIX,
    UNSUPPORTED_TRANSPOSE_OF_UNSUPPORTED_DERIVATE_TYPE,
    UNSUPPORTED_TYPE,
    USER_CLI_PARAM_EXCEPTION,
    USER_FUNCTION_UNEXPECTED_PARAMETERS,
    USER_IO_ERROR_WRITING_LIBRARIES,
    USER_IO_INVALID_INPUT_EXCEPTION,
    WARN_FUNCTION_MATRIX_SIZE_CAST,
    WARN_FUNCTION_MATRIX_TYPE_CAST,
    WARN_FUNCTION_MISSING_PARAMETERS,
    WARN_IF_SCALAR,
    WARN_MATRIX_ACCESS_WITH_SCALAR,
    WARN_MATRIX_DIMENSIONS_EXCEDED,
    WARN_VOID_IMPLEMENTATION,
    WARN_SKIP_FUNCTION;

    @Override
    public String stringify(ProgramNode node, Object... params) {
	String location = ErrorMessage.getLocationString(node);
	String extension_info = ErrorMessage.getExtensionInfo(node);
	String sdk_info = ErrorMessage.getSDKInfo(node);
	String ret;
	String source = "While generating C code";

// @formatter:off	
	
	switch (this) {
	case TODO_INTERNAL_SLICE_TYPE:
	    /**
	     * TODO: catch this error in the middle-end, based on input language
	     */
	    ret = String.format(
					"%s error %s: Slice of type " + params[0] + "is not supported.\n%s",
					// ERROR message arguments
					Constants.programName,
					ordinal(),
					sdk_info
			);
	break;
	case TODO_USER_ACCESS_INDEXES_GREATER_THAN_MATRIX_DIMENSIONS:
	    /**
	     * Happens when actual parameters are more than the matrix dimensions in the context of matrix access
	     * This check should be moved to middle-end
	     */
		ret = String.format(
			"Matrix '%s' has %s dimensions, but is accessed as a %s dimensional one.",
			params[0], // matrix name name
			params[1], // number of matrix dimensions
			params[2] // number of parameters
		);
	break;
	case TODO_USER_WARN_PARAMETER_TYPE:
	    /**
	     * spostare in middleend
	     * 
	     * params[0]: function parameter name
	     * params[1]: function parameter actual type
	     * params[2]: function parameter expected type
	     * params[3]: function name
	     */
	    ret = String.format(
					"Parameter %s of type '%s' was automatically converted to type '%s'.",
					// ERROR message arguments
					params[0],
					params[1],
					params[2]
			);
	    break;
	case INTERNAL_UNDEFINED_VISIBILITY_LEVEL:
	case INTERNAL_TREE_WALK_EXCEPTION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  
	     * notes:
	     *  Happens when a generic, unknown exception is raised during tree walk (Nullpointer, IndexOutOfBounds...)
	     *  in the C backend
	     */
	case INTERNAL_WARN_PARAMETER_TYPE_UNSUPPORTED:
	case INTERNAL_SIZE_OF_NON_MATRIX:
	case INTERNAL_FOR_NEEDS_INDEX_AND_EXPRESSION:
	    /**
	     * Happens when a for node in tree, miss the subnodes translation.
	     * This indicates an error in the subnodes of the for statement.
	     */
	case INTERNAL_CONTROL_STATEMENT_CONDITION_UNDEFINED:
	    /**
	     * Happens if if/while condition is missing or the translation of the condition
	     * generates an error
	     */
	case INTERNAL_MISSING_PARAMETER_CONTEXT:
	    /**
	     * Happens when a function_parameter_list node doesn't have the necessary attributes
	     */
	case INTERNAL_APPLY_TYPE_UNDEFINED:
	    /**
	     * Happens when an apply node doesn't have the necessary attributes
	     */
	case INTERNAL_MISSING_LHS_TRANSLATION:
	    /**
	     * Happens when translating an assignment and no translation was generated for some of the left-hand nodes
	     */
	case INTERNAL_STRUCT_ASSIGN_INVALID_LHS_NUMBER:
	    /**
	     * invalid struct assignment because left nodes are not correctly translated
	     */
	case INTERNAL_TRANSPOSE_OF_MATRIX_WITHOUT_DIMENSIONS:
	    /**
	     * Missing dimension fields in matrix type
	     */
	case INTERNAL_MATRIX_VECTOR_DIMENSION_UNKNOWN: 
	    /**
	     * Happens when a matrix type doesn't contain necessary sub-fields
	     */
	case INTERNAL_MATRIX_DIMENSIONS:
	case INTERNAL_ACCESS_WITH_UNDEFINED_SLICE:
	case INTERNAL_CREATE_UNDEFINED_SLICE:
	case INTERNAL_OUTPUT_TYPE:
	    /**
	     * Happens when function output type is not as expected
	     * 
	     * params[0]: function name
	     * params[1]: formal return type
	     * params[2]: expected return type
	     */
	case INTERNAL_ASSIGN_INCOMPATIBLE_TYPES:
	    /**
	     * happens when the lhs is incompatible with rhs
	     */
	case INTERNAL_NOT_UNARY_OPERAND:
	    /**
	     * ^2
	     */
	case INTERNAL_DOT_PRODUCT_BETWEEN_NON_2D_MATRICES:
	case INTERNAL_LINEAR_SOLVER_BETWEEN_NON_2D_MATRICES:
	case INTERNAL_FRONTEND_FUNCTION_OUTPUT:
	case INTERNAL_OUTPUT_MATRIX_DIMENSIONS:
	case INTERNAL_SUNDIALS_WRONG_INTEGRAND_FUNCTION_TYPE:
	case INTERNAL_SUNDIALS_WRONG_INTEGRAND_FUNCTION_RETURN_TYPE:
	case INTERNAL_NULL_ARGUMENT:
	case INTERNAL_MULTIPLE_OUTPUT_SYMBOLS_NOT_STRUCT_POINTER:
	case INTERNAL_BOOLEAN_EXPRESSION_TYPES:
	case INTERNAL_SLICE_UNSUPPORTED:
	case INTERNAL_METHOD_TYPE_UNSUPPORTED:
	case INTERNAL_TREE_FUNCTION_RETURN_VALUE_MISSING:
	case INTERNAL_TREE_FUNCTION_PARAMETER_LIST_MISSING:
	case INTERNAL_TREE_FUNCTION_NAME_MISSING:
	case INTERNAL_FUNCTION_REFERENCE_WITHOUT_REFERENCE:
	case INTERNAL_SLICE_WITH_MORE_THAN_2_DIMENSIONS:
	case INTERNAL_ASSIGN_TO_MATRIX_WITH_MULTIPLE_ROWS:
	case INTERNAL_USER_FUNCTION_NOT_FOUND:
	case INTERNAL_NULL_VALUE:
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				sdk_info
		);
	break;
	case INTERNAL_MISMATCHED_MATRIX_ACCESS:
	    /**
	     * happens when accessing a matrix with a wrong number/type of indexes
	     */
	    ret = String.format(
				"%s error %s: Using Matrix as a Scalar or vice-versa.\n%s",
				Constants.programName,
				ordinal(),
				"This is probably due to variables re-use, see: "+ErrorMessage.getReuseSameVarErrorLink(node)
		);
	break;
	case INTERNAL_APPLY_TO_NON_MATRIX:
	    /**
	     * Happens when trying to access to a variable that is not a matrix.
	     * If we arrive here, this means something went wrong in middle-end,
	     * should be catched by ErrorMessage.UNSUPPORTED_VALUED_TYPE_WITH_SUB_TYPE
	     */
	    ret = String.format(
				"%s error %s: Using value '%s' as a matrix%s.",
				Constants.programName,
				ordinal(),
				// ERROR message arguments
				params[0],
		    (Constants.fromLanguage.equals("matlab"))? " is currently not supported": " is not allowed"
		);
	break;
	case UNSUPPORTED_CONTROL_STATEMENT_CONDITION:
	    /**
	     * unsupported if/while of struct or matrix of scalar for instance
	     */
	    ret = String.format(
			"Only %s value or 1x1 matrix are currently supported in C.\n%s",
	    // ERROR message arguments
			// type names should depend on frontend
			GType.get(BType.BOOL)+"/"+GType.get(BType.INT),
		    	extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_OPERATOR_TYPES:
	    /**
	     * Happens when an expression between two matrices of incompatible type
	     * 
	     * params[0]: left expression
	     * params[1]: right expression
	     */
	    ret = String.format(
				"Operator '%s' applied to '%s' and '%s' is currently not supported in C.\n%s",
	    // ERROR message arguments
			    node.type(),
			    params[0],
			    params[1],
			    extension_info
		);

	break;
	case UNSUPPORTED_TRANSPOSE_OF_NONMATRIX:
	    /**
	     * Transpose of a variable that is not a matrix. 
	     * If this error happens here, 
	     * this means something went wrong in the middle-end.
	     * Should be catched by ErrorMessage.UNSUPPORTED_TRANSPOSITION
	     */
	    ret = String.format(
			"%s error %s: Transposition of type '%s' is currently not supported in C.\n%s",
			Constants.programName,
			ordinal(),
	    // ERROR message arguments
	    params[0],	// variable type
	    extension_info
		);
	break;
	case UNSUPPORTED_TRANSPOSE_OF_NDIMENSIONAL_MATRIX:
	    /**
	     * Transpose of a variable that is not a matrix. 
	     * If this error happens here, 
	     * this means something went wrong in the middle-end.
	     * Should be catched by ErrorMessage.TRANSPOSITION_N_DIMENSIONAL_MATRIX
	     * 
	     * params[0]: matrix type
	     */
	    ret = String.format(
				"%s error %s: Transposition is defined only on 2-dimensional matrices. '%s' was provided.\n%s",
				    // ERROR message arguments
				Constants.programName,
				ordinal(),
				params[0],
				sdk_info
		);
	break;
	case UNSUPPORTED_TRANSPOSE_OF_UNSUPPORTED_DERIVATE_TYPE:
	    /**
	     * Happens when trying to transpose a matrix of non-numeric elements.
	     * If we arrive here, this means something went wrong in middle-end,
	     * should be catched by ErrorMessage.UNSUPPORTED_VALUED_TYPE_WITH_SUB_TYPE
	     */
	    ret = String.format(
				"%s error %s: Transposition is supported only on %s matrices. '%s' was provided.\n%s",
				"numeric",
				Constants.programName,
				ordinal(),
	    // ERROR message arguments
				params[0],
				sdk_info
		);
	break;
	case UNSUPPORTED_MATRIX_DIMENSIONS_INCONSISTENT:
	    /**
	     * Happens when trying to combine matrices with incompatible sizes
	     * a=[1,2,3]
	     * b=[1,2;3,4]
	     * [a,b]
	     * If we arrive here, this means something went wrong in middle-end,
	     * should be catched by ErrorMessage.USER_MATRIX_DIMENSION_INCOMPATIBLE
	     */
	    ret = String.format(
				"%s error %s: Dimensions of arrays being concatenated are incompatible.\n%s",
				Constants.programName,
				ordinal(),
				sdk_info
		);
	break;
	case WARN_VOID_IMPLEMENTATION:
	    /**
	     * ex. figure(...)
	     * subplot(...)
	     * 
	     */
	    ret = String.format(
					"This is the first occurrence of a statement with no algorithmic impact. Currently ignored.\n%s",
					extension_info
			);
	break;
	case WARN_IF_SCALAR:
	    ret = String.format(
			"A %s is false when its value is below %s",
			GType.get(BType.SCALAR),
			// ERROR message arguments
			C.CONDITION_DOUBLE_ZERO
		);
	    break;
	case UNSUPPORTED_NODE:
	    /**
	     * equivalent to ErrorMessage.UNSUPPORTED
	     */
	    ret = String.format(
			"%s error, this is currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_FUN_ACTUAL_PARAM_DONT_MATCH:
	    /**
	     * function call applied to parameters not supported.
	     * Similar to ErrorMessage.WARN_FUN_FORMAL_PARAM_DONT_MATCH
	     * 
	     * param[0]: function name
	     * param[1]: actual parameter code
	     * param[2]: actual parameter type
	     * param[3]: formal parameter code
	     * param[4]: formal parameter type
	     */
	    ret = String.format(
				"Function '%s' has been applied to %s '%s', but previously was applied to '%s'",

	    // ERROR message arguments
			    params[0],	// function name
			    params[2],  // actual parameter type
			    params[1],  // actual parameter code
			    params[4]   // formal parameter type
		);
	break;
	case UNSUPPORTED_MATRIX_ACCESS:
	    /**
	     * Access of matrix with a matrix of an unsupported type
	     */
	    ret = String.format(
				"Index %s of type %s is currently not supported in C.\n%s",
		    // HERE this location message should be changed to better display a warning
		    		params[0],
		    		params[1],
		    		extension_info
		    		);
	break;
	case UNSUPPORTED_MATRIX_ELEMENT_TYPE:
	    /**
	     * params[0]: matrix elements type
	     * params[1]: matrix name
	     */
	    ret = String.format(
			"%s error, Matrix elements of type %s are currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  params[0],
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_OPERAND:
	    /**
	     * 1$2
	     */
	    ret = String.format(
			"%s error, Operand %s is currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  node.typeAsString(),
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_MATRIX_MATRIX_OPERATION:
	    ret = String.format(
			"%s error, Matrix operation %s is currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  node.typeAsString(),
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_SCALAR_SLICE_OPERATION:
	    ret = String.format(
			"%s error, Slice operation %s is currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  node.typeAsString(),
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_FRONTEND_FUNCTION:
	    /**
	     * params[0]: function name
	     */
	    ret = String.format(
			"%s error, Function '%s' is currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  params[0],
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT:
	    /**
	     * params[0]: function name
	     */
	    ret = String.format(
			"%s error, No paramenters provided. Function '%s' is currently supported only applied to parameters.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  params[0],
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT:
	    /**
	     * params[0]: function name
	     * params[1]: parameter name
	     */
	    ret = String.format(
			"%s error, Function '%s' applied to argument '%s' is currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  params[0],
		    	  params[1],
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS:
	    /**
	     * params[0]: function name
	     * params[1]: number of arguments
	     */
	    ret = String.format(
			"%s error, Function '%s' can accept %s %s, but this is currently not supported in C.\nSometimes, using a simpler version of the function may fix.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  params[0],
		    	  params[1],
		    	  (((Integer)params[1])>1)? "arguments" : "argument",
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS:
	case TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSTRING_ARGUMENT:
	case TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSCALAR_ARGUMENT:
	case TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONINT_ARGUMENT:
	    ret = String.format(
					"Function 'odeset' requires name-value paired arguments. See MATLAB manual."
			);
	break;
	case UNSUPPORTED_TYPE:
	    /**
	     * this type is currently not supported in C
	     * params[0]: type not supported
	     */
	    ret = String.format(
			"%s error, Elements of type %s are currently not supported in C.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		    	  params[0],
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case USER_IO_ERROR_WRITING_LIBRARIES:
	case USER_IO_INVALID_INPUT_EXCEPTION:
	    Exception e = (Exception) params[0];
	    ret = String.format(
					e.getMessage()
			);
	break;
	case USER_CLI_PARAM_EXCEPTION:
	    /**
	     * params[0]: the command line parameter
	     * params[1]: the range of allowed values
	     * params[2]: the parameter provided
	     */
	    List<String> availableOptions = (List<String>)params[1];
	    List<String> sorted = new ArrayList<String>(availableOptions.size());
	    Collections.copy(availableOptions, sorted);
	    Collections.sort(sorted);
	    ret = String.format(
					"Argument '%s' accepts '%s', but '%s' was provided",
					// ERROR message arguments
		    			params[0],
		    			String.join(", ",sorted),
		    			params[2]
			);
	break;
	case UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION:
	    /**
	     * core function (library function) not implemented
	     * params[0]: function name
	     */
	    ret = String.format(
				"Function '%s' is currently not supported in C.\n%s",
				params[0],
				extension_info
			);
	break;
	case UNSUPPORTED_LOOP:
	    /**
	     * Example code:
	     * a= 'asdf'
	     * > for i=a
	     *   ...
	     * Error:
	     * Iteration over 'String' is currently not supported in C.
	     * <extension_info>
	     */
	    ret = String.format(
					"Iteration over type '%s' is currently not supported in C.\n%s",
					params[0],
					extension_info
			);
	break;
	case WARN_MATRIX_DIMENSIONS_EXCEDED:
	    /**
	     * Example code:
	     * a=[1,2,3];
	     * b = 1;
	     * a(2, b)
	     * 
	     * Warning:
	     * Index 'b' can exceed Matrix bounds.
	     * To enable bounds check re-run with `-prealloccheck' and compile with `make sanitychecks=on', see <compilation options link>
	     */
	    ret = String.format(
				"Value of index '%s' is not checked.\nFor bounds checking when accessing Matrices re-run with `-prealloccheck' and compile with `make sanitychecks=on', see %s",
				params[0],
				ErrorMessage.getCompilationOptionsLink(node)
			);
	break;
	case USER_FUNCTION_UNEXPECTED_PARAMETERS:
	    /**
	     * at line...
	     * > find(a,b,c)
	     * Function 'find' expects 'Matrix' but 'a, b, c' was given.
	     * <extension_info>
	     * 
	     * params[0]: fun name
	     * params[1]: actual value
	     * params[2]: expected value
	     * params[3]: (optional) hint
	     */
	    String hint="";
	    if(params.length > 2)
		hint = "\n"+((String)params[3]);
		
	    ret = String.format(
				"Function '%s' expects '%s' but '%s' was provided.%s\n%s",
		    		params[0],
		    		String.join(", ", (String[]) params[2]),
		    		String.join(", ", (String[]) params[1]),
		    		hint,
		    		extension_info
			);
	break;
	case WARN_FUNCTION_MISSING_PARAMETERS:
	    /**
	     * Example code:
	     * 
	     * function f(a, b)
	     *   if nargin == 0
	     *     print('1')
	     *   else
	     *     print('0')
	     *   end
	     * end
	     * 
	     * f();
	     * 
	     * Warning emitted:
	     * Function 'f' expects 'a, b' but '' was given.
	     * Check your inputs in function 'f'
	     */
	    ret = String.format(
				"Function '%s' expects '%s' but '%s' was provided.\nCheck required inputs for function '%s'\n",
		    		params[0],
		    		String.join(", ", (String[]) params[2]),
		    		String.join(", ", (String[]) params[1]),
		    		params[0]
			);
	break;
	case WARN_FUNCTION_MATRIX_TYPE_CAST:
	    /**
	     * Warning emitted:
	     * Parameter 'b' of function 'f' should be 'Scalar[n x 1]' but 'Int[n x 1]' was given.
	     * Multiplying 'b' by a Scalar may fix.
	     * It will be converted automatically at run-time.
	     * 
	     * params[0]: nome parametro attuale
	     * params[1]: nome funzione
	     * params[2]: tipo formale
	     * params[3]: tipo attuale
	     */
	    ret = String.format("Function '%s' expects parameter '%s' to be '%s' but '%s' was provided.\nExplicitly multiplying '%s' by a %s will clear this warning.\nAutomatically casting to %s at run-time.",
		    		params[1],
		    		params[0],
		    		params[2],
		    		params[3],
		    		params[0],
		    		(Constants.fromLanguage.equals("matlab")) ? GType.get(BType.SCALAR) : "float",
				(Constants.fromLanguage.equals("matlab")) ? GType.get(BType.SCALAR) : "float"
			);
	break;
	case WARN_FUNCTION_MATRIX_SIZE_CAST:
	    /**
	     * > f(b)
	     * 
	     * Warning emitted:
	     * Parameter 'b' of function 'f' should be 'Scalar[n x n]' but 'Scalar[n x 1]' was given.
	     * 'f' can exceed Matrix bounds.
	     * To enable bounds check re-run with `-prealloccheck' and compile with `make sanitychecks=on', see <compilation options link>
	     * 
	     * params[0]: nome parametro attuale
	     * params[1]: nome funzione
	     * params[2]: tipo formale
	     * params[3]: tipo attuale
	     */
	    ret = String.format("Function '%s' expects parameter '%s' to be '%s' but '%s' was provided.\nMatrix accesses to '%s' are not checked.\nFor bounds checking when accessing Matrices re-run with `-prealloccheck' and compile with `make sanitychecks=on', see %s",
		    		params[1],
		    		params[0],
		    		params[2],
		    		params[3],
		    		params[1],
				ErrorMessage.getCompilationOptionsLink(node)
			);
	break;
	case WARN_MATRIX_ACCESS_WITH_SCALAR:
	    /**
	     * > a(2.0)
	     * 
	     * Warning message (adapted from ErrorMessage.UNSUPPORTED_ASSIGN_TO_MATRIX_PART):
	     * Using a value of type 'Scalar' as an index.
	     * Mantissa will be ignored.
	     *  
	     * params[0]: matrix name
	     * params[1]: index name
	     */
	    ret = String.format("Using a value of type '%s' as an index.\nMantissa will be ignored.",
		    GType.get(BType.SCALAR)
			);
	break;
	case WARN_SKIP_FUNCTION:
	    ret = String.format("Function '%s' has been defined and never used.",
		    params[0]
		    );
	    break;
	case UNSUPPORTED_SPARSE_MATRIX_OPERATION:
	    /**
	     * Error:
	     * 'reshape' of sparse Matrix is currently not supported in C.
	     * <extensio_info>
	     * 
	     * params[0]: function name
	     */
	    ret = String.format("Function '%s' applied to a sparse Matrix is currently not supported in C.\n%s",
		    params[0],
		    extension_info
			);
	break;
	default:
	    ret = String.format(
				"%s error: This is not a fault in your code.\n%s",
				Constants.programName,
				sdk_info
		);
	break;
	}
	
	// Add header with contextual info and return
	// TODO: handling of errors related to variables used hereafter
	return String.format(
					"%s %s%s\n%s\n"
					+ ret,
					
					source,				// where the message is coming from
					location,			// file...function..."following error(s)"
					(node!=null)? node.location() : "",		// line X, char Y
					" "+ ErrorMessage.getCode(node)	// offending code
			);

    }

// @formatter:on

}
