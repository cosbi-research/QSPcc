package eu.cosbi.qspcc.exceptions;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.utils.Constants;
import eu.cosbi.utils.Tuple;

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
public enum ErrorMessage implements ErrorCode {
    LIST_OF_ERRORS,
    LIST_OF_TREE_ERRORS,
    LIST_OF_WARNINGS,
    INTERNAL_TREE_WALK_EXCEPTION,
    USER_MATRIX_DIMENSION_INCOMPATIBLE,
    INTERNAL_RETURN_WITHOUT_FUNCTION,
    TRANSPOSITION_N_DIMENSIONAL_MATRIX,
    FINAL_REPORTING_TEMPLATE,
    FUN_ENV_PARAM_NEVER_DEFINED,
    FUN_FORMAL_PARAMS_DONT_MATCH,
    FUN_FORMAL_PARAMS_UNKNOWN,
    FUN_FUNCTIONAL_PARAM_N_PARAM_VALUES_DONT_MATCH,
    FUN_FUNCTIONAL_PARAM_N_RETURN_VALUES_DONT_MATCH,
    FUN_PARAMETER_MISSING,
    FUN_PARAMETER_UNDEFINED,
    FUN_POSITIONAL_ARGUMENT_FOLLOWS_NAMED_ARGUMENT,
    INTERNAL_ANONYMOUS_FUNCTION_NODE_WITHOUT_REFERENCE_FUNCTION,
    INTERNAL_ASSIGN_MISSING_BRANCH,
    INTERNAL_ASSIGN_UNDEFINED_RHS_TYPE,
    INTERNAL_ASSIGN_UNRECOGNIZED_BRANCH,
    INTERNAL_AT_NODE_STRUCTURE,
    INTERNAL_BOOL_EXPR,
    INTERNAL_FORMAL_OUTPUT_DOESNT_EXIST,
    INTERNAL_FORMAL_PARAMETER_DOESNT_EXIST,
    INTERNAL_FUN_FUNCTIONAL_PARAM_DONT_MATCH,
    INTERNAL_FUN_PARAMETER_MISSING,
    INTERNAL_FUN_UNDEFINED,
    INTERNAL_INDEX_DIM_POSITION,
    INTERNAL_MATRIX_DERIVATE_TYPE_UNKNOWN,
    INTERNAL_MATRIX_SLICE_WITH_UNSUPPORTED_TYPE,
    INTERNAL_MATRIX_TYPE_SHOULD_HAVE_DERIVATE_TYPE,
    INTERNAL_MATRIX_VECTORS_HAVE_DIFFERENT_TYPES,
    INTERNAL_MISSING_MATRIX_VECTORS,
    INTERNAL_MISSING_STATEMENT_TYPE,
    INTERNAL_MULTIASSIGN_WITH_NDIMENSIONAL_RHS_UNSUPPORTED,
    INTERNAL_SCALAR_OP_RESULT_TYPE,
    INTERNAL_SLICE_WITHOUT_START_STOP,
    INTERNAL_STATEMENT_NOT_INITIALIZED,
    INTERNAL_STATEMENT,
    INTERNAL_STRUCTURE_EXPECTED_NODE_NOT_FOUND,
    INTERNAL_STRUCTURE_STACK_EMPTY,
    INTERNAL_STRUCTURE_UNEXPECTED_NODE_ON_STACK,
    INTERNAL_STRUCTURE_UNEXPECTED_PREVIOUS_NODE,
    INTERNAL_UNARY_UNSUPPORTED_TYPE,
    INTERNAL_UNSUPPORTED_OPERATION,
    INTERNAL_UNSUPPORTED_SLICE_TYPE,
    INTERNAL_VECTOR_STRUCTURE,
    INTERNAL_VECTOR_TYPE,
    INTERNAL_WRONG_IMPLICIT_TYPE,
    UNSUPPORTED_ASSIGN_ALL_OF_NONMATRIX,
    UNSUPPORTED_ASSIGN_INCOMPATIBLE_TYPES,
    UNSUPPORTED_ASSIGN_RHS_TYPES,
    UNSUPPORTED_ASSIGN_TO_MATRIX_PART,
    UNSUPPORTED_ASSIGN_UNKNOWN_NODE_IN_LHS,
    UNSUPPORTED_BOOL_EXPR_BETWEEN_INCOMPATIBLE_MATRICES,
    UNSUPPORTED_CORE_FUNCTION_TYPE_UPDATER,
    UNSUPPORTED_FOR_LOOP_EXPRESSION,
    UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES,
    UNSUPPORTED_MULTIASSIGN_WITH_LHS_PARAMS,
    UNSUPPORTED_OPERATOR_TYPES,
    UNSUPPORTED_SLICE_PART_TYPE,
    UNSUPPORTED_SLICE,
    UNSUPPORTED_STRUCT_ACCESS_BY_INDEX,
    UNSUPPORTED_TRANSPOSITION,
    UNSUPPORTED_VALUED_TYPE_WITH_SUB_TYPE,
    UNSUPPORTED,
    USER_ERROR_COPYING_INPUT_FILES,
    USER_MAIN_IS_NOT_FUNCTION,
    USER_NULL_ASSIGNMENT_ALL_MISSING,
    USER_SYNTAX_ERROR_CASE_BEFORE_SWITCH,
    USER_SYNTAX_ERROR_ELSE_BEFORE_IF,
    USER_SYNTAX_ERROR_FUNCTION_TYPES_HEADER,
    USER_SYNTAX_ERROR_LATE_DETECTION,
    USER_SYNTAX_ERROR,
    USER_UNDEFINED_SYMBOL_ENV,
    USER_UNDEFINED_SYMBOL,
    USER_UNKNOWN_BACKEND,
    USER_UNKNOWN_FRONTEND,
    USER_UNKNOWN_MIDDLEEND,
    WARN_ASSIGN_MORE_RHS_THAN_LHS,
    WARN_ASSIGN_TO_MATRIX_PART_UNSUPPORTED,
    WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH,
    WARN_CORE_FUNCTION_PARAM_TYPE_UNSUPPORTED_IGNORED,
    WARN_CORE_FUNCTION_PARAM_VALUE_NEEDED_IGNORED,
    WARN_FLAT_INDEX_OUT_OF_BOUNDS,
    WARN_FUN_FORMAL_PARAM_DONT_MATCH,
    WARN_VARIABLE_WITH_MULTIPLE_TYPES,
    WARN_USER_IMPLICITLY_PROMOTING_SCALAR_VARIABLE;

    ErrorMessage() {
    }

    public static String getLocationString(ProgramNode node) {
	StringBuffer location = new StringBuffer();
	boolean something = false;
	if (node != null) {
	    //if (node.sourcePath() != null)
	    //location.append("file " + node.sourcePath() + "\n");
	    ProgramNode pf = node.parentFunction();
	    ProgramNode pfid = null;
	    if (pf != null)
		pfid = pf.descendant(NodeType.ID);
	    if (pfid != null) {
		location.append("in function '").append(pfid.code()).append("' ");
		something = true;
	    }
	    if (pf != null && pf.location() != null) {
		location.append("defined at ").append(pf.location());
		something = true;
	    }
	    if (something)
		location.append("\n");
	}
	return location.toString();
    }

    /**
     * 
     * @param formalparam
     * @param actualParam can be null if formalparam is an env param
     * @return
     */
    public static String getNearestExpressionCode(ProgramNode formalparam) {
	ProgramNode nearest = formalparam.nearestExpression();
	if (nearest == null)
	    nearest = formalparam;
	return (!nearest.code().contains("\n")) ? nearest.code() : nearest.code().split("\\\n")[0] + "\t ...";
    }

    public static String getExtensionLink(ProgramNode node) {
	return "https://git.io/JnZDg";
    }

    public static String getFunctionEntryPointLink(ProgramNode node) {
	return "https://git.io/JnZDT";
    }

    public static String getCompilationOptionsLink(ProgramNode node) {
	return "https://git.io/JZaMQ";
    }

    public static String getFunctionEntryPointInfo(ProgramNode node) {
	return "See " + getFunctionEntryPointLink(node) + " for details on the syntax.";
    }

    public static String getSDKLink(ProgramNode node) {
	return "https://git.io/JGYA8";
    }

    public static String getSyntaxInfo(ProgramNode node) {
	return "See " + getSyntaxErrorsLink(node) + " for common syntax errors.";
    }

    public static String getSyntaxErrorsLink(ProgramNode node) {
	return "https://git.io/JnZXV";
    }

    public static String getSDKInfo(ProgramNode node) {
	return "To understand and fix the internals of " + Constants.programName + ", see SDK at " + getSDKLink(node);
    }

    public static String getExtensionInfo(ProgramNode node) {
	if (node != null && node.ancestor() != null && node.ancestor().type() != null
		&& (node.ancestor().type().equals(NodeType.APPLY) || node.type().equals(NodeType.TRANSPOSE)
			|| node.type().equals(NodeType.ELEMENTWISE_CCT)))
	    return "Comment this statement or provide an implementation, see " + getExtensionLink(node);
	else
	    return "";
    }

    public static String getFunctionMissingParameterDescription(ProgramNode errNode) {
	//if (errNode.ancestor().type().equals(NodeType.APPLY))
	// case f(), where f is undefined f(1)
	return "has not been defined. Sometimes, 'l=load(...); l." + getCode(errNode) + ";' may fix";
	//else
	// case fun(...,f) where f is undefined, or f*2 where a is undefined.
	//    return "";
    }

    public static String getCode(ProgramNode node) {
	if (node != null)
	    return getCodeString(node.code());
	else
	    return "";
    }

    public static String getCodeString(String s) {
	return (!s.contains("\n")) ? s : s.split("\\\n")[0] + "\t ...";
    }

    public static String getDefaultString(ProgramNode node, boolean first_occurrence) {
	StringBuffer sb = new StringBuffer();
	sb.append(getLocationString(node));
	sb.append("at ").append(node.location());
	if (first_occurrence)
	    sb.append(" (first occurrence)\n");
	else
	    sb.append("\n");
	sb.append(" " + getCode(node)).append("\n");
	sb.append("This statement cannot be translated due to previous errors.").append("\n");
	sb.append("\n");
	return sb.toString();
    }

    public static int collectExplanations(StringBuffer explanations, ProgramNode node) {
	ProgramNode curNode = null;
	int n_problems = 0;
	List<ProgramNode> curNodes = new LinkedList<>();
	if (node == null || node.descendants() == null)
	    return n_problems;

	curNodes.addAll(node.descendants());
	while (!curNodes.isEmpty()) {
	    // pop node
	    curNode = curNodes.remove(0);
	    // read errors
	    Map<ErrorMessage, Set<GException>> errors = (Map<ErrorMessage, Set<GException>>) curNode
		    .attr(NodeAttr.DEFERRABLE_ERRORS);
	    if (errors != null && !errors.isEmpty()) {
		String error_joiner = "This creates " + Integer.toString(errors.size()) + " additional error";
		if (errors.size() > 1)
		    error_joiner += "s";

		explanations.insert(0, error_joiner + "\n");

		for (Set<GException> errs : errors.values())
		    for (GException err : errs) {
			explanations.insert(0, err.stringify() + "\n");
			++n_problems;
		    }
	    }
	    // add childs
	    curNodes.addAll(curNode.descendants());
	}
	return n_problems;
    }

    @Override
    public String stringify(ProgramNode node, Object... params) {
	String location = getLocationString(node);
	String extension_info = getExtensionInfo(node);
	String ret;

	switch (this) {
	// USED ONLY IN COMBINATION WITH LISTEXCEPTION
	case LIST_OF_WARNINGS: {
	    Map<String, Set<GException>> warnings = (Map<String, Set<GException>>) params[0];
	    StringBuffer sb = new StringBuffer();
	    for (Map.Entry<String, Set<GException>> e : warnings.entrySet())
		if (!e.getValue().isEmpty()) {
		    sb.append(String.format("File %s contains %s %s\n\n",
			    // PARAMS
			    e.getKey(), e.getValue().size(), (e.getValue().size() == 1) ? "warning" : "warnings"));

		    for (GException w : e.getValue()) {
			sb.append(w.stringify());
			sb.append("\n\n");
		    }
		}
	    return sb.toString();
	}
	case LIST_OF_ERRORS: {
	    List<GException> unrecoverable_errors = (List<GException>) params[0];
	    StringBuffer sb = new StringBuffer();
	    for (GException e : unrecoverable_errors) {
		sb.append(e.stringify());
		sb.append("\n\n");
	    }
	    return "Here's the list of errors:\n\n" + sb.toString();
	}
	// USED ONLY IN COMBINATION WITH LISTEXCEPTION
	case LIST_OF_TREE_ERRORS: {

	    List<GException> unrecoverable_errors = (List<GException>) params[0];
	    StringBuffer sb = new StringBuffer();
	    int n_errors = 0;
	    for (GException e : unrecoverable_errors) {
		n_errors++;
		StringBuffer explanations = new StringBuffer();
		int n_problems = collectExplanations(explanations, e.node());
		if (n_problems > 0)
		    sb.append(explanations.toString());

		sb.append(e.stringify());
		sb.append("\n\n");
	    }
	    /*
	    String prefix = null;
	    GException first = unrecoverable_errors.get(0);
	    if (first.node() != null)
	    	prefix = "\n\tCannot translate source file '" + first.node().sourcePath() + "'.\n\tFound " + n_errors
	    			+ " unrecoverable " + ((n_errors == 1) ? "error" : "errors") + ".\n";
	    else
	    	prefix = "\n\tFound " + n_errors + " unrecoverable " + ((n_errors == 1) ? "error" : "errors") + ".\n";
	    */
	    return /*prefix + "\tHere's the list of errors:\n\n" +*/ sb.toString();
	}
	case FINAL_REPORTING_TEMPLATE: {
	    Program p = (Program) params[0];
	    StringBuffer finalMessage = new StringBuffer();
	    LinkedList<Tuple<AAST, String>> errorMessages = new LinkedList<Tuple<AAST, String>>();
	    LinkedList<Tuple<AAST, String>> undefStatementsMessages = new LinkedList<Tuple<AAST, String>>();
	    try {
		p.mainCompilationUnit().walkOnExit((Program ref, AAST icomp) -> {
		    //warning occoured during compilation
		    Set<GException> warnings = icomp.warnings();
		    //errors occoured during compilation
		    Set<GException> errors = icomp.errors();
		    Tuple<List<AASTNode>, Map<AASTNode, List<AASTNode>>> icompDetails = icomp.incompleteStatements();
		    // statements unresolved that are not inside a function
		    List<AASTNode> freeStatements = icompDetails.first();
		    // statements unresolved inside a function
		    Map<AASTNode, List<AASTNode>> functionStatementMap = icompDetails.second();
		    int funErrors = 0;
		    for (Map.Entry<AASTNode, List<AASTNode>> entry : functionStatementMap.entrySet())
			funErrors += entry.getValue().size();
		    int totalUndefStatements = freeStatements.size() + funErrors;
		    boolean onlyErrors = false;
		    StringBuffer msg = new StringBuffer();

		    if (!errors.isEmpty() && !warnings.isEmpty()) {
			onlyErrors = true;
			msg.append(String.format("file %s contains %s %s and %s %s\n",
				Paths.get(icomp.sourcePath()).getFileName(),
				// ERROR PARAMS
				errors.size(), (errors.size() == 1) ? "error" : "errors",
				// WARNING PARAMS
				warnings.size(), (warnings.size() == 1) ? "warning" : "warnings"));
		    } else if (!errors.isEmpty()) {
			onlyErrors = true;
			msg.append(
				String.format("file %s contains %s %s\n", Paths.get(icomp.sourcePath()).getFileName(),
					// ERROR PARAMS
					errors.size(), (errors.size() == 1) ? "error" : "errors"));
		    } else if (totalUndefStatements > 0 && !warnings.isEmpty())
			msg.append(String.format("file %s contains %s %s and %s %s\n",
				Paths.get(icomp.sourcePath()).getFileName(),
				// ERROR PARAMS
				totalUndefStatements,
				// this can be customized if you want to show something different in case of undefined statements only
				(totalUndefStatements == 1) ? "error" : "errors",
				// WARNING PARAMS
				warnings.size(), (warnings.size() == 1) ? "warning" : "warnings"));
		    else if (totalUndefStatements > 0) {
			msg.append(
				String.format("file %s contains %s %s\n", Paths.get(icomp.sourcePath()).getFileName(),
					// ERROR PARAMS
					totalUndefStatements, (totalUndefStatements == 1) ? "error" : "errors"));
		    } else if (!warnings.isEmpty()) {
			msg.append(
				String.format("file %s contains %s %s\n", Paths.get(icomp.sourcePath()).getFileName(),
					// WARNING PARAMS
					warnings.size(), (warnings.size() == 1) ? "warning" : "warnings"));
		    } else {
			// nothing to say about this file
			return;
		    }

		    if (!warnings.isEmpty())
			msg.append("Warnings:\n");

		    for (GException e : warnings) {
			msg.append(e.stringify()).append("\n");
		    }

		    if (onlyErrors || totalUndefStatements > 0) {
			msg.append("Errors:\n");
		    }
		    // the order is inner-first most fatal first
		    // errors
		    Set<ErrorCode> alreadySeen = new HashSet<>();
		    for (GException e : errors) {
			ErrorCode type = e.errorType();
			if (alreadySeen.contains(type))
			    continue;
			alreadySeen.add(type);
			StringBuffer explanations = new StringBuffer();
			int n_problems = collectExplanations(explanations, e.node());
			if (n_problems > 0)
			    msg.append(explanations.toString());

			msg.append(e.stringify());
			msg.append("\n\n");
		    }

		    if (!onlyErrors) {
			if (!freeStatements.isEmpty()) {
			    AASTNode freeStatement = freeStatements.get(0);
			    StringBuffer explanations = new StringBuffer();
			    int n_problems = collectExplanations(explanations, freeStatement);
			    if (n_problems > 0)
				msg.append(explanations);

			    msg.append(getDefaultString(freeStatement, freeStatements.size() > 1));
			}

			for (Map.Entry<AASTNode, List<AASTNode>> entry : functionStatementMap.entrySet()) {
			    if (!entry.getValue().isEmpty()) {
				AASTNode funStatement = entry.getValue().get(0);
				StringBuffer explanations = new StringBuffer();
				int n_problems = collectExplanations(explanations, funStatement);
				if (n_problems > 0)
				    msg.append(explanations);

				msg.append(getDefaultString(funStatement, entry.getValue().size() > 1));
			    }
			}
		    }

		    // store messages in order from inner-most to outer-most
		    if (onlyErrors)
			errorMessages.add(new Tuple<>(icomp, msg.toString()));
		    else
			undefStatementsMessages.add(new Tuple<>(icomp, msg.toString()));
		}, false);

		// iterate and print first messages with real errors
		for (Tuple<AAST, String> priorityMessages : errorMessages)
		    finalMessage.append(priorityMessages.second());
		// print then messages with undefined statements (can be due to real errors)
		for (Tuple<AAST, String> undefMessages : undefStatementsMessages)
		    finalMessage.append(undefMessages.second());

	    } catch (GException e) {
		finalMessage.append("Error generating the list of errors: " + e.stringify());
	    }
	    return finalMessage.toString();
	}
	// @formatter:off
	case USER_SYNTAX_ERROR:
	    /**
	     * generated_by: Frontend
	     * example: 
	     *   a // b
	     * output:
	     *   Syntax error: main.m line 1:6 no viable alternative at input '/ '
	     * param[0]: error message that comes from ANTLR parser 
	     */
	    ret = String.format(
	    	"Syntax error %s\n%s",
	    	((params.length > 0) ? ": " + params[0] : ""), // offending code
	    	getSyntaxInfo(node)
		);
	break;
	case USER_SYNTAX_ERROR_LATE_DETECTION:
	    /**
	     * generated_by: Middleend
	     * example: 
	     *  a.
	     * output:
	     *   line 4, char 7
	     *    a.
	     *   Syntax error: check your statement syntax
	     * notes:
	     *  transient error, should happen only if there is a bug in the implementation
	     */
	    ret = String.format(
			"Syntax error: check your statement syntax"

	    // ERROR message arguments
	    // ARG1	// DESCR
	    // NONE
		);
	break;
	case USER_SYNTAX_ERROR_ELSE_BEFORE_IF:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  elseif(1)
	     *   disp(2)
	     *  if(2)
	     *   disp(4)
	     *  end
	     * output:
	     *  line 2, char 3
	     *  elseif(1) ...
	     *  
	     *  Cannot translate ELSE block, missing IF block.
	     * param[0]: top-level statement node. Can be null.
	     */
	    ret = String.format(
			"Syntax Error: ELSE block is only allowed after an IF statement."

	    // ERROR message arguments
	    // ARG1	// DESCR
	    // NONE
		);
	break;
	case USER_SYNTAX_ERROR_CASE_BEFORE_SWITCH:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  case 1:
	     *   disp(2)
	     *  switch(2)
	     *  case 2:
	     *   disp(4)
	     *  end
	     * output:
	     *  line 2, char 3
	     *  elseif(1) ...
	     *  
	     *  Anonymous function 'f' not found.
	     * param[0]: top-level statement node. Can be null.
	     */
	    ret = String.format(
			"Syntax Error: CASE block is only allowed after a SWITCH statement."

	    // ERROR message arguments
	    // ARG1	// DESCR
	    // NONE
		);
	break;
	case INTERNAL_TREE_WALK_EXCEPTION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  
	     * notes:
	     *  Happens when a generic, unknown exception is raised during tree walk (Nullpointer, IndexOutOfBounds...)
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_BOOL_EXPR:
	    /**
	     * generated_by: Middleend
	     * example: 
	     *  Not available
	     * output:
	     *   line 4, char 7
	     *    a $ b
	     *   Logical operation is currently not supported
	     *   <extension_info>
	     * param[0]: the NodeType(enum) of the unsupported operation
	     * notes:
	     * transient error, should happen only if there is a bug in the implementation
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_BOOL_EXPR_BETWEEN_INCOMPATIBLE_MATRICES:
	    /**
	     * generated_by: Middleend
	     * example: 
	     *   a(:,:,1)=[1,2;3,4];
	     *   a(:,:,2)=[5,6;7,8];
	     *   b=[1,2;3,4];
	     *   disp(a < b);
	     * output:
	     *   <location>
	     *   line 4, char 7
	     *    a < b
	     *    
	     *   Logical operator '<' between matrices of incompatible sizes is currently not supported
	     *   Left matrix 3-dimensional, right matrix 2-dimensional.
	     *   Comment this statement or provide an implementation, see https://git.io/J35vG
	     */
	    ret = String.format(
		  "Logical operator '%s' between matrices of incompatible sizes is currently not supported.\nLeft matrix %s-dimensional, right matrix %s-dimensional\n%s",
	    // ERROR message arguments
		  node.typeAsString(), //the unsupported operator
		  params[0],  //the number of dimensions of the left matrix
		  params[1],  //the number of dimensions of the right matrix
	          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES:
	    /**
	     * generated_by: Middleend
	     * example 1: 
	     *   a(:,:,1)=[1,2;3,4];
	     *   a(:,:,2)=[5,6;7,8];
	     *   b=[1,2;3,4];
	     *   disp(a * b);
	     * example 2: 
	     *  a=[1,2;3,4] %  Int [2 x 2]
	     *  b=[1,2];    %  Int [1 x 2]
	     *  x=a/b
	     * output:
	     *   <location>
	     *   line 4, char 7
	     *    a * b
	     *   Operator '*' between matrices of incompatible sizes is currently not supported
	     *   Left matrix Int [2 x 2 x 2], right matrix Int [2 x 2].
	     *   <extension_info>
	     */
	    ret = String.format(
		"Operator '%s' between matrices of incompatible sizes is currently not supported.\nLeft matrix %s, right matrix %s\n%s" ,
	    // ERROR message arguments
		  node.typeAsString(), //the unsupported operator
		    params[0],  //the left matrix type
			 params[1],  //the right matrix type
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case INTERNAL_SCALAR_OP_RESULT_TYPE:
	    /**
	     * generated_by: Middleend
	     * example: 
	     *  a $ b
	     * output:
	     *   line 4, char 7
	     *    a $ b
	     *    
	     *   QSPcc error, operator '$' is currently not supported.
	     *   <extension_info>
	     * notes:
	     * transient error, should happen only if there is a bug in the implementation
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_FUN_FUNCTIONAL_PARAM_DONT_MATCH:
	    /**
	     * generated_by: Middleend
	     * example: 
	     * Not available
	     * output:
	     *  line 6, char 5
	     *  ode23(a,b,c)
	     *
	     *	QSPcc error, function 'ode23' is currently misconfigured on parameter 'a'.
	     *  'a' should be a function.
	     *  <extension_info>
	     * param[0]: function name
	     * param[1]: actual parameter node
	     * notes:
	     *  will be raised for core-function that takes a function as n-th parameter (by now only ode),
	     *  when the input n-th parameter is not a function.
	     *  This is usually masked by UNSUPPORTED_CORE_FUNCTION_TYPE_UPDATER
	     *  if core-function is properly configured.
	     */
	    ret = String.format("%s error, function '%s' is currently misconfigured on parameter '%s'.\n'%s' should be a function.\n%s",
	    // ERROR message arguments
		    	    Constants.programName,
			    params[0], // function name
			    ((AASTNode) params[1]).name(), // parameter name
			    ((AASTNode) params[1]).name(), // parameter name
			    extension_info
	    // NONE
		);
	break;
	case INTERNAL_FORMAL_PARAMETER_DOESNT_EXIST:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	    *  function x=f(a,b)
	    *    ....
	    *    ....
	    *  end
	    *	function 'f' is currently misconfigured on parameter named 'w'
	     * param[0]: function name
	     * param[1]: parameter name 
	     */
	    ret = String.format(
				"%s error, function '%s' is currently misconfigured on parameter '%s'.\n%s",
	    // ERROR message arguments
			    Constants.programName,
			    params[0], // function name
			    params[1],  // parameter name
			    extension_info
		);
	break;
	case INTERNAL_FORMAL_OUTPUT_DOESNT_EXIST:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	    *  function x=f(a,b)
	    *    ....
	    *    ....
	    *  end
	    *	function 'f' is currently misconfigured on return value 'x'
	     * param[0]: function name
	     * param[1]: parameter name 
	     */
	    ret = String.format(
				"%s error, function '%s' is currently misconfigured on return value '%s'.\n%s",
	    // ERROR message arguments
			    Constants.programName,
			    params[0], // function name
			    params[1],  // parameter name
			    extension_info
		);
	break;
	case FUN_FORMAL_PARAMS_UNKNOWN:{
	    /**
	     * generated_by: Middleend
	     * example: 
	     *  function function_parfor_obj_fun_v1(....)
	     *     m=simTrafficking(....,R_PM);
	     *     ....
	     *  end
	     * output:
	     *  file /home/ciropom/qsp-cc-extensions/tmp/function_parfor_obj_fun_v1.m
	     *  function 'function_parfor_obj_fun_v1' defined at line 1, char 0
	     *  contains the following error(s)
	     *  
	     *  line 25, char 169
	     *  simTrafficking(RatioTimeInst(end),EgfNm(EGF_100),false,true,true,false,false,simCblOE,optparameters,R_PM)
	     *  'R_PM' has not been defined
	     *  
	     *  
	     *  line 12, char 234
	     *  simTrafficking(RatioTimeInst(end),EgfNm(EGF_100),false,true,true,false,false,simCblOE,optparameters,R_PM)
	     *  'simTrafficking' has not been defined
	     *  Comment this statement or provide an implementation, see https://git.io/J35vG
	     *  
	     * param[0]: Unrecognized formal parameters (last param if actual longer than formal. 
	     *           can point to a env parameter, because env parameters are dynamically added on the fly)
	     * param[1]: Unrecognized actual parameters (null if env param)
	     * param[2]: Unrecognized actual env parameters (null if param)
	     * param[3]: function si a core function? if not null
	     * param[4]: function name
	     */
	    List<AASTNode> undefFormalParams = (List<AASTNode>) params[0];
	    List<AASTNode> undefActualParams = (List<AASTNode>) params[1];
	    List<AASTNode> undefActualEnvParams = (List<AASTNode>) params[2];
	    IFunction coreFun = (IFunction) params[3];
	    StringBuffer sb = new StringBuffer();

	    for (int i=0; i < undefFormalParams.size() ; ++i) {
		AASTNode fp = undefFormalParams.get(i);
		AASTNode ap = undefActualParams.get(i);
		AASTNode aep = undefActualEnvParams.get(i);
		
		List<AASTNode> refs = null;
		// formal param, or env param
		List<AASTNode> errNodes=new LinkedList<>();
		if(fp!=null) {
		    refs = (List<AASTNode>) fp.attr(NodeAttr.ENV_PARAM_REFERENCES);
		    if (refs != null && !refs.isEmpty())
			errNodes = refs;
		    else
			if(ap!=null)
			    errNodes.add(ap);
			else
			    // env parameter
			    errNodes.add(aep);
		}else {
		    if(ap!=null)
			errNodes.add(ap);
		    else
			// env parameter
			errNodes.add(aep);
		}
		for(AASTNode errNode : errNodes) {
        		String loc = errNode.location();
        		if (loc != null)
        		    sb.append("at ").append(loc).append("\n");
        
        		sb.append(" "+getNearestExpressionCode(errNode))
        			.append("\n")
        			.append("'")
        			.append(getCode(errNode))
        			.append("' ")
        			.append(getFunctionMissingParameterDescription(errNode));
        		String extinfo = getExtensionInfo(errNode);
        		if(!"".equals(extinfo))
        		    sb.append("\n").append(extinfo);
        		sb.append("\n");
		}
	    }
	    ret = sb.toString();
	    break;
	}
	case FUN_PARAMETER_UNDEFINED: {
	    /**
	     * generated_by: Middleend
	     * example: 
	     * Not available
	     * output:
	    *  line 6, char 5
	    *  f(z,3)
	    *
	    *	'z' has not been defined.
	     * param[0]: actual parameter code
	     * param[1]: function name
	     */
	    ret = String.format(
				"'%s' has not been defined.",
				    // ERROR message arguments
				params[0] // parameter code
		);
	break;
	}
	case FUN_ENV_PARAM_NEVER_DEFINED:{
	    /**
	     * generated_by: Middleend
	     * example:
	     *  function dy=f(t,y)
	     *    z=c;
	     *    y=w;
	     *  end
	     *  f(1,2);
	     * output:
	     *  file /home/ciropom/qsp-cc-extensions/tmp/main.m
	     *  contains the following error(s)
	     *  
	     *  
 	     *  line 5, char 0
	     *  f(1,2)
	     *  
 	     *  at line 2, char 6
 	     *  z=c;
	     *  'c' has not been defined
	     *  
	     *  
	     *  at line 3, char 6
 	     *  y=w;
 	     *  'w' has not been defined
	     * param[0]: function call example code
	     * param[1]: function name
	     * param[2]: function line xx, char yy
	     * param[3]: list of node symbols never defined 
	     */
	    List<AASTNode> envParams = (List<AASTNode>) params[3];
	    StringBuffer sb = new StringBuffer();
	    for (AASTNode fp : envParams) {
		@SuppressWarnings("unchecked")
		List<AASTNode> refs = (List<AASTNode>) fp.attr(NodeAttr.ENV_PARAM_REFERENCES);
		AASTNode errNode = fp;
		if (refs != null && !refs.isEmpty())
		    errNode = refs.get(0);
		String loc = errNode.location();
		if (loc != null)
		    sb.append("at ").append(loc).append("\n");
		sb.append(getNearestExpressionCode(errNode))
			.append("\n")
			.append("'")
			.append(getCode(errNode))
			.append("' ")
			.append(getFunctionMissingParameterDescription(errNode))
			.append("\n"+getExtensionInfo(errNode)+"\n\n");
	    }
	    
	    ret= sb.toString();
	break;
	}
	case FUN_FORMAL_PARAMS_DONT_MATCH:
	    /**
	     * generated_by: Middleend
	     * example: 
	     *  function x=f(a,b)
	     *    x=a*b
	     *  end
	     *  
	     *  disp(f(1,2,3,4));
	     * output:
	     *  line 5, char 5
	     *  f(1,2,3,4)
	     *
	     *  Function 'f' is applied to '1, 2, 3, 4' but expects 'a, b' 
	     * param[0]: function name
	     * param[1]: list of nodes of actual parameters
	     * param[2]: list of nodes of formal parameters
	     */
	    List<AASTNode> actualParams = (List<AASTNode>) params[1];
	    List<AASTNode> formalParams = (List<AASTNode>) params[2];
	    // more actual then formal parameters, error
	    List<String> actualParamsAsStr = new ArrayList<String>();
	    for (AASTNode actualParam : actualParams)
		actualParamsAsStr.add(actualParam.code());
	    List<String> formalParamsAsStr = new ArrayList<String>();
	    for (AASTNode formalParam : formalParams)
		formalParamsAsStr.add(formalParam.code());

	    ret = String.format(
				"Function '%s' is applied to '%s' but expects '%s'",

	    // ERROR message arguments
			    params[0],	// function name
			    String.join(", ", actualParamsAsStr), // list of actual parameters comma-separated
			    String.join(", ", formalParamsAsStr)
		);
	break;
	case WARN_FUN_FORMAL_PARAM_DONT_MATCH:
	    /**
	     * generated_by: Middleend
	     * example: 
	     *  function x=f(y,z)
	     *    x=y*z
	     *  end
	     *  
	     *  disp(f(1,2));
	     *  disp(f('a',3));
	     * output:
	     *  line 6, char 5
	     *  f('a',3)
	     *
	     *	Function 'f' has been applied to Int [2 x 2] '[1,2;2,4]', but previously was applied to 'Int'
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
	case INTERNAL_FUN_PARAMETER_MISSING: {
	    /**
	     * generated_by: Middleend
	     * relative_to: R/Python
	     * example: 
	     *  Not available
	     * output:
	     *   line 4, char 7
	     *    f(a,b,z=2)
	     *   Function 'f' is defined without 'z'.
	     * 
	     * 
	     * notes:
	     *  shouldn't happen in matlab, because it doesn't have named parameters
	     */
	    ret = String.format(
				"%s error, function '%s' is defined without '%s'.",
				Constants.programName,
	    // ERROR message arguments
				params[0], //function name
				params[1] //parameter name
		);
	break;
	}
	case FUN_PARAMETER_MISSING:
	    /**
	     * generated_by: Middleend
	     * relative_to: R/Python
	     * example: 
	     *  Not available
	     * output:
	     *   line 4, char 7
	     *    f(a,b,z=2)
	     *   Function 'f' is defined without 'z'.
	     * 
	     * 
	     * notes:
	     *  shouldn't happen in matlab, because it doesn't have named parameters
	     */
	    ret = String.format(
				"Function '%s' is defined without '%s'.",
	    // ERROR message arguments
				params[0], //function name
				params[1] //parameter name
		);
	break;
	case UNSUPPORTED_CORE_FUNCTION_TYPE_UPDATER: {
	    /**
	     * generated_by: Middleend
	     * example: 
	     * Not available
	     * output:
	     *  line 6, char 5
	     *  f(a,....,z)
	     *
	     *  Function 'f' applied to 
	     *      a_type
	     *      .
	     *      .
	     *      z_type
	     *  is currently not supported.
	     *  <custom text message coming from configured core-function>
	     *  Either check the 'f' signature, or implement your own variant. <extension_link>
	     *  
	     * param[0]: code of core-function call [like f(a,...,z)] that determined the error
	     * param[1]: list of actual parameter types [like a,....,z]
	     * param[2]: function name
	     * param[3]: custom text message coming from configured core-function
	     * notes:
	     *  raised by configured core functions if the actual parameters doesn't match the ones supported by the core function implementation
	     */
	    List<GType> aparams = (List<GType>) params[1];
	    String[] strs = new String[aparams.size()];
	    int i = 0;
	    for (GType p : aparams)
		strs[i++] = p.toShortString();
	    
	    String specifier="";
	    if(aparams.size()>1)
		specifier = " the following parameters";
	    
	    String err = "Function '" + params[2] + "' applied to"+specifier;
		    
            if(String.join(",", strs).length() > 40)
        	err+="\n\t"+ String.join("\n\t", strs) + "\n";
            else
        	err+=" "+String.join(", ", strs) + " ";
            
	    err+="is currently not supported.";
	    if (params.length > 2 && ((String)params[3]).length()>0)
		err+="\n"+params[3];
	    err+="\nEither check the '"+params[2]+"' signature, or implement your own variant. See "+getExtensionLink(node);
	    ret = err;
	    break;
	}
	case FUN_FUNCTIONAL_PARAM_N_RETURN_VALUES_DONT_MATCH:{
	    /**
	     * generated_by: Middleend
	     * example:
	     *  function [z,y]=f(t,y)
	    *    z=1;
	    *    y=t;
	    *  end
	     *
	    *  ode23(@f,b,c);
	    * output:
	    * 	line 6, char 0
	    *  ode23(@f,b,c)
	    *  
	    *  Function 'ode23' expects 'f' to return 1 value(s), but 2 are returned.
	    * param[0]: function name
	    * param[1]: functional name
	    * param[2]: number of actual return types
	    * param[3]: number of expected return types
	    * notes:
	     *  generated when the functional parameter has a number of outputs different from the expected
	     */
	    Integer expected_params = (Integer)params[3];
	    Integer actual_params = (Integer)params[2];
	    ret = String.format("Function '%s' expects '%s' to return %s value%s, but %s %s returned.",
		    		// ERROR message arguments
		    		params[0],	// function name
		    		params[1],      // functional name
				expected_params, // number of expected parameters
				(expected_params > 1)? "s" : "",          // plural form
				actual_params, // number of actual parameters
				(actual_params > 1)? "are" : "is"          // plural form
		);
	break;
	}
	case FUN_FUNCTIONAL_PARAM_N_PARAM_VALUES_DONT_MATCH:{
	    /**
	     * generated_by: Middleend
	     * example:
	     * function dy=f(t,y,c)
	    *        y=t;
	     * end
	    * c=1;
	     * ode23(@(t,y,c) f(t,y,c),b,[c]);
	     * output:
	    *  line 6, char 0
	     *  ode23(@(t,y,c) f(t,y,c),b,[c])
	     *  
	     *  Function 'ode23' expects 'f' to take 2 value(s), but 3 are provided.
	     *  param[0]: function name
	     *  param[1]: functional parameter name
	     *  param[2]: number of actual parameters
	     *  param[3]: number of expected parameters
	     * notes:
	     *  generated when the functional parameter has a number of inputs different from the expected
	     */
	    	Integer expected_params = (Integer)params[3];
	    	Integer actual_params = (Integer)params[2];
		ret = String.format(
				"Function '%s' expects '%s' to take %s value%s, but %s %s provided.",
				params[0], // function name
				params[1], // functional parameter name
				expected_params, // number of expected parameters
				(expected_params > 1)? "s" : "",          // plural form
				actual_params, // number of actual parameters
				(actual_params > 1)? "are" : "is"          // plural form
		);
	break;
	}
	case FUN_POSITIONAL_ARGUMENT_FOLLOWS_NAMED_ARGUMENT:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * to be checked:
	     *  - what is the problem?
	     *    - out of order parameter
	     *    - positional argument cannot follow named argument
	     * output:
	     *  line 1, char 20
	     *  
	     *  Function 'f' incompatible with named parameter 'np'
	     * param[0]: function name
	     * param[1]: parameter name
	     * notes:
	     *  cannot happen in matlab, since default parameters doesn't exist in the language
	     */
	    ret = String.format(
				"Ambiguous call to function '" + params[0]
			    + "', positional argument follows keyword argument "

	    // ERROR message arguments
	    // ARG1	// DESCR
	    // NONE
		);
	break;
	case UNSUPPORTED:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a(:,[1,2]) = [1,2;3,4]
	     * output:
	     *  line 1, char 0
	     *  a(:,[1,2]) = [1,2;3,4]
	     *
	     *  QSPcc error, this is currently not supported.
	     *  <extension_info>
	     */
	    ret = String.format(
			"%s error, this is currently not supported.\n%s",
	    // ERROR message arguments
		    	  Constants.programName,
		          extension_info	// static string with informations about how to extend qspcc
		);
	break;
	case INTERNAL_VECTOR_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	     *  [a, _, b]
	     *
	     *  QSPcc error, The type of _ is Unrecognized
	     * param[0]: vector element type
	     * param[1]: vector element node
	     * notes:
	     *  transient error, will happen only if there is a bug in the implementation
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_MISSING_MATRIX_VECTORS:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	     *  []
	     *
	     *  A matrix should be composed of at least one vector
	     * notes:
	     *  transient error, will happen only if there is a bug in the frontend implementation
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_MATRIX_VECTORS_HAVE_DIFFERENT_TYPES:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	     *  ['a',1;2,3]
	     *
	     *  Trying to add a vector of type String to a matrix of type Int
	     * notes:
	     *  transient error, will happen only if there is a bug in the middleend
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case USER_MATRIX_DIMENSION_INCOMPATIBLE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	     *  [a,1;2,3]
	     *
	     *  Matrix [a,1;2,3] dimension is Unrecognized
	     *  param[0]: node of the matrix that raised the error
	     */
	    ret = String.format(
				"Dimensions of arrays being concatenated are incompatible."
		);
	break;
	case UNSUPPORTED_TRANSPOSITION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a='xxx'
	     *  a'
	     * output:
	     *  line 3, char 1
	     *  a'
	     *  
	     *  Transposition of type 'String' is currently not supported.
	     *  param[0]: The type of the variable being transposed
	     */
	    ret = String.format(
			"Transposition of type '%s' is currently not supported.\n%s",
	    // ERROR message arguments
	    params[0],	// variable type
	    extension_info
		);
	break;
	case TRANSPOSITION_N_DIMENSIONAL_MATRIX:
	    /**
	     * generated_by: Middleend
	     * example:
	     *   a(:,:,1)=[1,2;3,4];
	     *   a(:,:,2)=[1,2;3,4];
	     *   a'
	     * output:
	     *  line 3, char 1
	    *  a'
	    *  
	    *  Transposition is defined only on 2-dimensional matrices. 'Int [2 x 2 x 2]' was provided
	    * param[0]: matrix type
	    * param[1]: transposed variable code
	     */
	    ret = String.format(
				"Transposition is defined only on 2-dimensional matrices. '%s' was provided.\n%s\nSometimes a simple 'squeeze(%s)' may fix.",
	    // ERROR message arguments
				params[0],
				extension_info,
				params[1]
		);
	break;
	case UNSUPPORTED_OPERATOR_TYPES:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a=[1,2];
	     *  b=[3,3]; 
	     *  a^b
	     * output:
	     *  line 4, char 1
	     *  a^b
	     *  
	    *  Operator '^' applied to 'Int [1 x 2]' and 'Int [1 x 2]' is currently not supported.
	    *  <extension_info>
	     */
	    ret = String.format(
				"Operator '%s' applied to '%s' and '%s' is currently not supported.\n%s",

	    // ERROR message arguments
			    params[0],
			    params[1],
			    params[2],
			    extension_info
		);
	break;
	case INTERNAL_UNARY_UNSUPPORTED_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	     *  -a
	     *
	     *  Unary operator applied to type 'String' not implemented
	     *  param[0]: type of variable
	     * notes:
	     *  transient error, will happen only if there is a bug in the middleend
	     */
	    ret = String.format(
				"Unary operator applied to type '%s' is currently not supported.\n%s",
	    // ERROR message arguments
				params[0],
				extension_info
		);
	break;
	case INTERNAL_SLICE_WITHOUT_START_STOP:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 0
	     *  1:
	     *
	     *  Slice operator needs at least a start and a stop, none has been provided
	     *  param[0]: type of variable
	     * notes:
	     *  transient error, will happen only if there is a bug in the middleend
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_SLICE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a=[1,2]
	     *  a:2:5
	     * output:
	     *  line 2, char 3
	     *  a:2:5
	     *  
	     *  Slices indexed with '<Int [1 x 2], Int, Int>' are currently not supported.
	     *  <extension_info>
	     *  param[0]: triple with slice types
	     */
	    ret = String.format(
			"Slices indexed with '%s' are currently not supported.\n%s",
			params[0],
	    // ERROR message arguments
			extension_info
		);
	break;
	case INTERNAL_FUN_UNDEFINED: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     * notes:
	     *  transient error, will happen only if there is a bug in the middleend
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	}
	case INTERNAL_MATRIX_SLICE_WITH_UNSUPPORTED_TYPE: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  a(d,b,c)
	     *  
	     *  Matrix 'a:Scalar [12]' cannot be sliced with 'd:String, b:Int, c:Scalar'
	     *  
	     *  param[0]: id node of the matrix node
	     *  param[1]: list of param nodes
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	}
	case INTERNAL_WRONG_IMPLICIT_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  z=a(d,b,c)
	     *  
	     *  Right hand side of assignment derives from the unknown identifier 'a', should be a matrix.
	     *  This leads to inconsistent type 'String' for 'a' maybe 'a' is a function?
	     *  
	     *  param[0]: id node of the matrix node
	     *  param[1]: list of param nodes
	     * notes:
	     *  same as BUG_MATRIX_SLICE_WITH_UNSUPPORTED_TYPE
	     *  but node type was speculated to come from a 'load' call or the environment.
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case USER_UNDEFINED_SYMBOL_ENV: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  a(d,b,c)
	     *  
	     *  Function or variable 'a' has not been defined.
	     *  
	     *  param[0]: name of the undefined function
	     */
	    ret = String.format(
			"Function or variable '%s' has not been defined. %s failed to find '%s' in the environment.",
			params[0], // function name
			Constants.programName, // qspcc
			params[0] // function name
		);
	break;
	}
	case USER_UNDEFINED_SYMBOL:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  a(d,b,c)
	     *  
	     *  Variable a is not defined
	     *  
	     *  param[0]: name of the undefined function
	     */
	    ret = String.format(
			"Function or variable '%s' has not been defined.",
			params[0] // function name
		);
	break;
	case INTERNAL_UNSUPPORTED_OPERATION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  a
	     *  
	     *  Operation type 'UNSUPPORTED_OPERATION' is not supported
	     *  
	     * notes:
	     *  Happens when an expression node that exists in the front-end is not handled in the middle-end.
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_STRUCT_ACCESS_BY_INDEX:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a.x=1;
	    *   a.z=2;
	    *   a(end)
	     * output:
	     *  line 3, char 2
	     *   end
	     *  
	    *  index access of struct not implemented yet.
	     *  
	     */
	    ret = String.format(
			"This struct indexing is currently not supported.\n%s",
		    extension_info
		);
	break;
	case INTERNAL_UNSUPPORTED_SLICE_TYPE: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  a(end)
	     *  
	     *  Invalid slice access of type 'String' when applying slice to 'a:String'
	     *  
	     * notes:
	     *  Happens when an a type doesn't implement the sliceable(DimsType) interface, but a variable with that type
	     *  is sliced.
	     */
	    AASTNode applyMatrix = (AASTNode) params[0];
	    ret = String.format(
			"This way of indexing a '%s' is currently not supported.\n%s",
			params[1],
		    extension_info
		);
	break;
	}
	case INTERNAL_ANONYMOUS_FUNCTION_NODE_WITHOUT_REFERENCE_FUNCTION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  @f
	     *  
	     *  Anonymous function 'f' not found.
	     * param[0]: function name
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_STATEMENT_NOT_INITIALIZED:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <unhandled code structure>
	     *  
	     *  Unexpected code
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_VECTOR_STRUCTURE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <unhandled matrix structure>
	     *  
	     *  Expected vector
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_STRUCTURE_EXPECTED_NODE_NOT_FOUND: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  Nodes of type 'LEFTDIV' not found on stack:  
	     *   <statement><id>
	     *   <statement><id>
	     *   <statement><id>
	     * param[0]: enum with type of node that caused the error
	     * param[1]: Dequeue<ProgramNode>
	     * notes:
	     *   internal error when the queue of nodes collected during walk doesn't contains the nodes it should contain
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	}
	case INTERNAL_STRUCTURE_UNEXPECTED_PREVIOUS_NODE: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  Previous node has type 'LEFTDIV', expected node of type 'RIGHTDIV'
	     * param[0]: enum with type of node found
	     * param[1]: enum with type of expected node
	     * notes:
	     *   internal error when the queue of nodes collected during walk doesn't contains the nodes it should contain
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	}
	case INTERNAL_STRUCTURE_STACK_EMPTY:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  Empty stack for node
	     * notes:
	     *   internal error when the queue of nodes collected during walk doesn't contains the nodes it should contain
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_STRUCTURE_UNEXPECTED_NODE_ON_STACK: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  you asked to translate element 'a' but the last element in tree is 'f(x)'
	     * notes:
	     *   internal error when the queue of nodes collected during walk doesn't contains the nodes it should contain
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	}
	case INTERNAL_STATEMENT:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  Unknown statement type 'PIPPO'
	     * notes:
	     *   internal error when the Middleend encounters a node type that represents a statement it can't handle
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_MISSING_STATEMENT_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  New statement is not annotated with a statement type
	     * notes:
	     *   internal error when the Middleend encounters a node type that represents a statement not configured in the enum of supported statements
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_FOR_LOOP_EXPRESSION: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a='xxx'
	     *  for i=a
	     *    disp('a')
	     *  end
	     * output:
	     *  line 2, char 6
	     *  a
	     *  
	     *  'for' statement with non-numeric iterator 'a' is currently non supported.
	     *  <extension_info>
	     */
	    AASTNode exprNode = (AASTNode) node;
	    ret = String.format(
			"'for' statement with non-numeric iterator '%s' is currently not supported.\n%s",
			exprNode.name(),
			extension_info
		);
	break;
	}
	case INTERNAL_ASSIGN_UNRECOGNIZED_BRANCH:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  Invalid assign part 'LEFTDIV'
	     * notes:
	     *   internal error when the Middleend encounters an assignment with an unknown sub-node
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_ASSIGN_MISSING_BRANCH:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  <statement>
	     *  
	     *  got a non-lhs/rhs node in assignment.
	     * notes:
	     *   internal error when the AssignStatement is asked to evaluate a node that doesn't belong to lhs or rhs
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_MULTIASSIGN_WITH_LHS_PARAMS:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  same as ASSIGN_UNKNOWN_NODE_IN_LHS
	     * output:
	     *  line 2, char 3
	     *  :,2
	     *  
	     *  Multiple assignments with left-hand-side parameters unsupported
	     * notes:
	     *   internal error when the AssignStatement has a too-complex left-hand-side
	     *   POSSIBLE IMPROVEMENT
	     */
	    ret = String.format(
			"%s error %s: The assignment expression on the left is currently non supported.\n%s",
			Constants.programName,
			ordinal(),
			extension_info
		);
	break;
	case UNSUPPORTED_ASSIGN_UNKNOWN_NODE_IN_LHS:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  function [x,y]=f(z)
	     *          x=[z, 2];
	     *          y=[1;3];
	     *  end
	     *  [w(:,2), k] = f(3)
	     * output:
	     *  line 2, char 3
	     *  :,2
	     *  
	     *  Undefined translation on left-hand assignment
	     * notes:
	     *   happens when the left-hand-side expression contains a sub-node it can't translate because it is not implemented
	     */
	    ret = String.format(
			"%s error %s: The assignment expression on the left is currently non supported.\n%s",
			Constants.programName,
			ordinal(),
			extension_info
		);
	break;
	case INTERNAL_ASSIGN_UNDEFINED_RHS_TYPE: {
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 3
	     *  a=f(x)
	     *  
	     *  The  'a' = 'f(x)'
	     * notes:
	     *   internal error when the AssignStatement is asked to update types for the left-hand side but the right-hand side doesn't have any type
	     */
	    List<AASTNode> lhs = (List<AASTNode>) params[0];
	    AASTNode rhsTp = (AASTNode) params[1];
	    List<String> lhsStr = new ArrayList<String>(lhs.size());
	    for (AASTNode l : lhs)
		lhsStr.add(l.code());

	    ret = String.format(
				"%s error %s: The assignment expression on the right is %s due to previous errors.\n%s",
				// ERROR message arguments
				Constants.programName,
				ordinal(),
				GType.get(BType.UNKNOWN),
				extension_info	
		);
	break;
	}
	case UNSUPPORTED_ASSIGN_INCOMPATIBLE_TYPES:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  a=[1,2;3,4];
	     *  a='xxx';
	     * output:
	     *  line 5, char 8
	     *  a='xxx';
	     *  
	     * param[0]: right hand side code
	     * param[1]: right hand side type
	     * param[2]: left hand side code
	     * param[3]: left hand side type
	     */

	    ret = String.format(
				"Variable '%s' has been assigned '%s', but previously was assigned '%s'.\n%s",
	    // ERROR message arguments
				params[2],
				params[1],
				params[3],
				(Constants.fromLanguage.equals("c"))? "This is not supported in "+Constants.fromLanguage.toUpperCase()+"." : "This is currently not supported"
		    );
	break;
	case INTERNAL_MULTIASSIGN_WITH_NDIMENSIONAL_RHS_UNSUPPORTED:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  function a=f(z)
	     *          a(:,:,1)=[1,2;3,4];
	     *          a(:,:,2)=[z,2;3,4];
	     *  end
	     *  [x,y] = f(1)
	     * output:
	     *  line 5, char 8
	     *  f(1)
	     *  
	     * Undefined translation for multiple assignment from 3D-matrix
	     * param[0]: number of dimensions of the right-hand-side matrix
	     */
	    // TODO: this is not supported by MATLAB
	    //       find a way to avoid handling code that the frontend
	    //       doesn't support
	    ret = String.format(
				"For languages with matrix multiple assignment capabilities only: Undefined translation for multiple assignment from " + params[0] + "D-matrix"

	    // ERROR message arguments
	    // ARG1	// DESCR
	    // NONE
		);
	break;
	case UNSUPPORTED_ASSIGN_TO_MATRIX_PART:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  z='a'
	     *  a(z) = [1,2]
	     * output:
	     *  line 5, char 8
	     *  a(z)
	     *  
	     *  Matrix access with 'z' of type 'String' is not supported.
	     * param[0]: name of unsupported parameter
	     * param[1]: type of unsupported parameter
	     */
	    GType unsupportedType = (GType)params[1];
		
	    ret = String.format(
				"Unable to use a value of type '%s' as an index.%s",
		    // ERROR message arguments
				unsupportedType,
		    (unsupportedType.equals(BType.STRING) && Constants.fromLanguage.equals("matlab"))? " This is currently not supported" : ""		    
		);
	break;
	case WARN_ASSIGN_TO_MATRIX_PART_UNSUPPORTED:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  z=1.0
	     *  a(z) = [1,2]
	     * output:
	     *  line 5, char 8
	     *  a(z)
	     *  
	     *  Index z of type Scalar is rounded to Int for matrix access.
	     *  
	     * param[0]: name of unsupported parameter
	     * param[1]: type of unsupported parameter
	     */
	    ret = String.format(
				"Index %s of type %s is always rounded to Int without checking its value.%s",
		    // HERE this location message should be changed to better display a warning
		    		params[0],
		    		params[1],
		    		// TODO: add extra strings like this also in other errors when there is a difference between qspcc and MATLAB
		    		(Constants.fromLanguage.equals("matlab"))? "\nMATLAB checks and accepts only whole "+params[1]+" (ex. 1.0)" : ""
		);
	break;
	case UNSUPPORTED_VALUED_TYPE_WITH_SUB_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  z=1.0;
	     *  z(12)=2;
	     * output:
	     *  line 2, char 0
	     *  z(12)=2;
	     *  
	     *  Valued type Scalar cannot have a derivate type Int nor a dimension 
	     *  
	     * param[0]: type of base variable accessed
	     * param[1]: type of variable used to access a location in the base variable
	     */
	    ret = String.format(
				"Using value '%s' as a matrix%s.",
	    // ERROR message arguments
				params[0],
		    (Constants.fromLanguage.equals("matlab"))? " is currently not supported": " is not allowed"
		);
	break;
	case INTERNAL_MATRIX_TYPE_SHOULD_HAVE_DERIVATE_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 0
	     *  <statement>
	     *  
	     *  Incomplete matrix type Scalar []
	     * param[0]: Incomplete type
	     * notes:
	     *  happens when for some reason, a matrix type is constructed without dimensions
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_MATRIX_DERIVATE_TYPE_UNKNOWN:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 0
	     *  <statement>
	     *  
	     *  Unknown translation for matrix of type String
	     * param[0]: Matrix type of unsupported sub-type
	     * notes:
	     *  happens when for some reason, a matrix type is constructed with a sub-type not supported
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_ASSIGN_RHS_TYPES:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 2, char 0
	     *  <statement>
	     *  
	     *  Can't translate assignment of unsupported type 'String'
	     * param[0]: Matrix type of unsupported sub-type
	     * notes:
	     *  happens when for some reason, an assignment right-hand side has an unsupported type
	     */
	    ret = String.format(
			"The assignment expression type '%s' on the right is currently non supported.\n%s",
			params[0],
			extension_info
		);
	break;
	case USER_UNKNOWN_FRONTEND:
	    /**
	     * generated_by: CompilerController
	     * example:
	     *  qspcc -s ... -d ... --from w -t c
	     * output:
	     *  Cannot find plugin for source language w
	     * param[0]: name of language requested
	     */
	    return String.format("Source language '%s' is currently not supported.\n%s", params[0], extension_info );
	case USER_UNKNOWN_BACKEND:
	    /**
	     * generated_by: CompilerController
	     * example:
	     *  qspcc -s ... -d ... --from matlab --to w
	     * output:
	     *  Cannot find plugin for target language w
	     * param[0]: name of language requested
	     */
	    return String.format("Target language '%s' is currently not supported.\n%s", params[0], extension_info );
	case USER_UNKNOWN_MIDDLEEND:
	    /**
	     * generated_by: CompilerController
	     * example:
	     *  Not available
	     * output:
	     *  Cannot find type inference plugin
	     * notes:
	     *  QSPcc supports multiple middle-ends. 
	     *  A reference implementation is provided in class DefaultMiddleEndImpl
	     *  If for some reason this class is not available in the classpath, this error will be raised.
	     */
	    return String.format(
			"%s error %s: Installation corrupted, please re-install.",
			Constants.programName,
			ordinal()
		    );
	case USER_ERROR_COPYING_INPUT_FILES:
	    /**
	     * generated_by: CompilerController
	     * example:
	     *  qspcc ... -d folder ...
	     *  and 'folder' is not writable
	     * output:
	     *  Error copying input file '/path/to/file'
	     * param[0]: File object that raised the I/O error
	     */
	    return String.format("The destination folder (--dest) is not writable");
	case USER_SYNTAX_ERROR_FUNCTION_TYPES_HEADER:
	    /**
	     * generated_by: CompilerController
	     * example:
	     *  function header with unsupported type
	     *  
	     *  % ==FUNCTION== "vectorized_objective_function_v1"
	     *  % ==PARAMETERS==  % ALLOWED PARAMETER TYPES: MATRIX, INT, SCALAR, BOOL
	     *  %   theta                UNKNOWN[7, 1]
	     *  % ==END==
	     *  
	     * output:
	     * param[0]: The input type specified by user in header, that is not recognized as a correct type.
	     */
	    
	    ret = String.format(
			"Syntax error: '%s' is unrecognized.\n%s", 
	    // ERROR message arguments
			params[0],
			getFunctionEntryPointInfo(node)
		);
	break;
	case USER_MAIN_IS_NOT_FUNCTION:
	    /**
	     * generated_by: CompilerController
	     * example:
	     *  script with header
	     *  
	     *  % ==FUNCTION== "..."
	     *  ...
	     *  % ==END==
	     *  
	     * output:
	     *  Main script is not a function, skipping type comments
	     * notes:
	     *  a script doesn't have input parameters
	     */
	    ret = String.format(
			"Syntax error: Custom function definition is not applicable here.\n%s", 
	    // ERROR message arguments
			getFunctionEntryPointInfo(node)
		);
	break;
	case INTERNAL_AT_NODE_STRUCTURE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 2
	     *  <statement>
	     * 
	     *  Unexpected function reference structure 
	     * notes:
	     *  an at-node is the anonymous function.
	     *  the generic tree requires that an anonymous function can be either
	     *  - a function reference @f
	     *  - an anonymous function definition @(t,y) ...
	     *  if none of this is true, this error is raised
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_INDEX_DIM_POSITION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 2
	     *  <statement>
	     * 
	     *  Trying to index an unknown matrix 
	     * notes:
	     *  internal, happens when an index type (end, :) is constructed to reference a non-matrix
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case INTERNAL_RETURN_WITHOUT_FUNCTION:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 2
	     *  <statement>
	     * 
	     *  Cannot return outside of a function 
	     * notes:
	     *  internal, happens when an a RETURN node is encountered outside of a function
	     */
	    ret = String.format(
				"%s error %s: This is not a fault in your code.\n%s",
				Constants.programName,
				ordinal(),
				getSDKInfo(node)
		);
	break;
	case UNSUPPORTED_SLICE_PART_TYPE:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     *  line 1, char 2
	     *  a:2:5
	     * 
	     *  Types not yet fully defined, impossible to resolve dependent type
	     * notes:
	     *  generated when a slice element has an unrecognized type
	     */
	    ret = String.format(
			"Slice element has an '%s' type",
		    GType.get(BType.UNDEFINED)
	    // ERROR message arguments
	    // ARG1	// DESCR
	    // NONE
		);
	break;
	case UNSUPPORTED_ASSIGN_ALL_OF_NONMATRIX:
	    /**
	     * generated_by: Middleend
	     * example:
	     *   z.a=1.0
	     *   k(:,12)=z
	     * output:
	     *   line 2, char 0
	     *    k(:,12)=z;
	     *   
	     *   Right-hand-side should be of type matrix or scalar, 'Struct[a: Scalar]' found
	     */
	    ret = String.format(
				"The assignment expression on the right, of type '%s'%s.",
		    // HERE this location message should be changed to better display a warning
		    		params[0],
		    		// TODO: add extra strings like this also in other errors when there is a difference between qspcc and MATLAB
		    		(Constants.fromLanguage.equals("matlab"))? " is currently not supported" : " is not allowed"
		);
	break;
	case WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH:
	    /**
	     * generated_by: Frontend core-functions
	     * example:
	     *   Not available
	     * output:
	     *   line 2, char 0
	     *    ode15s(...)
	     *   
	     *   Unexpected parameter type, given 'Scalar' expected 'Scalar [n]', forcing to 'Scalar'
	     * param[0]: parameter type
	     * param[1]: expected parameter type
	     * param[2]: parameter position (0-based)
	     * notes:
	     *   warning generated by core-functions when a recoverable error occours.
	     */
	    ret = String.format(
				"Parameter %s of type '%s' was automatically converted to type '%s'.",
		    // HERE this location message should be changed to better display a warning
				((Integer)params[2])+1,
		    		params[0],
		    		params[1]
		);
	break;
	case WARN_CORE_FUNCTION_PARAM_TYPE_UNSUPPORTED_IGNORED:
	    /**
	     * generated_by: Frontend core-functions
	     * example:
	     *   Not available
	     * output:
	     *   line 2, char 0
	     *    ode15s(...)
	     *   
	     *   Unexpected parameter type, given 'Scalar' expected 'Scalar [n]', ignoring.
	     * param[0]: parameter type
	     * param[1]: expected parameter type
	     * param[2]: parameter position (0-based)
	     * notes:
	     *   warning generated by core-functions when a recoverable error occours.
	     */
	    ret = String.format(
				"Ignoring the optional parameter %s of type '%s' that was provided with the wrong type '%s'.",
	    // ERROR message arguments
			((Integer)params[2])+1,
	    		params[0],
	    		params[1]
		);
	break;
	case WARN_CORE_FUNCTION_PARAM_VALUE_NEEDED_IGNORED:
	    /**
	     * generated_by: Frontend core-functions
	     * example:
	     *   Not available
	     * output:
	     *   line 2, char 0
	     *    ode15s(...)
	     *   
	     *   Warning evaluating parameter 2, ignoring.
	     * param[0]: number of parameter that generates the warning (1-based)
	     * notes:
	     *   warning generated by core-functions when a recoverable error occours.
	     */
	    ret = String.format(
				"Parameter %s must be a number and not a variable for C translation.",
				    // ERROR message arguments
				Integer.toString((Integer) params[0])
		);
	break;
	case USER_NULL_ASSIGNMENT_ALL_MISSING:
	    /**
	     * generated_by: Middleend
	     * example:
	     *   a=[1,2;3,4]
	     *   a(1,1)=[]
	     * output:
	     *   line 2, char 0
	     *    a(1,1)
	     *   
	     *   A null assignment should have at least one colon index.
	     */
	    ret = String.format(
			"To delete a dimension you should specify '%s' in assignment expression on the left",
			    // ERROR message arguments
			NodeType.COLON
		);
	break;
	case WARN_ASSIGN_MORE_RHS_THAN_LHS:
	    /**
	     * generated_by: Middleend
	     * example:
             *  a.z=1;
             *  a.x=2.2;
             *  a.w=[];
	     *
             *  [u,v]=a;
	     * output:
	     *   line 5, char 1
	     *    [u,v]=a;
	     *   
	     *   Error in assignment, right-hand-side has more values than left-hand-side.
	     *   Can't assign w.
	     * param[0]: name of the struct element that can't be assigned
	     * param[1]: the number of output elements ignored (0-based)
	     */
	    Integer howmany = (Integer) params[1];
	    ret = String.format(
				"%s extra value%s provided on the right side %s ignored.",
	    // ERROR message arguments
				howmany+1,
				(howmany==0)? "s": "",
				(howmany==0)? "is": "are"
					
		);
	break;
	case WARN_FLAT_INDEX_OUT_OF_BOUNDS:
	    /**
	     * generated_by: Middleend
	     * example:
	     *   Not available
	     * output:
	     *   line 2, char 0
	     *    a = <matrix>
	     *   
	     *   Attempt to grow array along ambiguous dimension, keep previous dimensions. 
	     * notes:
	     *   warning generated by middle end, when a statement make the number of dimensions of a matrix change in an unexpected way.
	     */
	    ret = String.format(
				"Attempt to grow array along ambiguous dimension, ignored."
		);
	break;
	case WARN_VARIABLE_WITH_MULTIPLE_TYPES:
	    /**
	     * generated_by: Middleend
	     * example:
	     *  Not available
	     * output:
	     * 
	     * notes:
	     *  happens when an identifier is assigned with two conflicting types by two different parts of the program
	     * 
	     * params[0] id that caused the error
	     * params[1] old type
	     * params[2] new type
	     */
	    ret = String.format(
				"'%s' can't be of type %s and '%s' at the same time, choosing '%s'.",
				((ProgramNode)params[0]).code(),
				params[1],
				params[2],
				params[2]
		);
	break;
	case WARN_USER_IMPLICITLY_PROMOTING_SCALAR_VARIABLE:
	    /**
	     * example:
	     * length(1)
	     * 
	     * length should be applied to matrices/vectors
	     * 
	     * params[0]: function name
	     * params[1]: parameter name
	     * params[2]: actual parameter type
	     */
	    ret = String.format(
				"Casting %s from %s to Matrix.%s\n%s",
				params[1],
				params[2],
				(Constants.toLanguage.equals("c"))? "\nIn C this isn't fully supported. If you see additional errors, try using two variables for "+params[2]+" and Matrix values." : "",
				extension_info
			);
	break;
	default:
	    ret = String.format("UNDEFINED_ERROR_STRING");
	    break;
	}
	
	// Add header with contextual info and return
	// TODO: handling of errors related to variables used hereafter
	return String.format(
					"%s%s\n%s\n"
					+ ret,
					
					location,			        // file...function..."following error(s)"
					(node != null)? node.location() : "",	// line X, char Y					
					" "+ getCode(node)	// offending code
			);

    }
// @formatter:on
}
