package eu.cosbi.qspcc.ast.attrs;

/**
 * attributes that can be attached to a node
 * 
 * @author tomasoni
 *
 */
public enum NodeAttr implements Attr {
    // attributes on ID that defines a variable
    ETYPE,
    SYMBOL,
    // store errors found on node
    ERRORS,
    // store errors that can be solved in a future walk (in case they won't be
    // resolved)
    DEFERRABLE_ERRORS,
    // node values in vector (if VECTOR_JOIN is non-null is single-valued)
    VECTOR_VALUES,
    // if non-null, this vector is the union of scalars and 1D matrices
    VECTOR_JOIN,
    // vector values in matrix
    MATRIX_VALUES,
    // formal parameter names, and output values names for this function
    FUNCTION_PARAMS,
    // LHS parameters, or FUNCTION_PARAMETER_LIST parameters
    PARAMS,
    // true if lhs contains more than 1 variable
    MULTIPLE_ASSIGN,
    // true if this vector contains native values and matrix values
    VECTOR_MIXED_VALUES,
    // true if this vector contains only matrix values
    VECTOR_MATRIX_VALUES,
    // array of boolean, one for each vector in this matrix.
    // i-th value is true if the 1D submatrices of the i-th vector should be
    // interpreted
    // as row vectors, false if they should be interpreted as column vectors
    ONE_DIMENSIONAL_VECTORS_INTERPRETATION,
    // true if the matrix doesn't involve copying other matrices
    // ex. a = [b; c] where b and c are matrices, FIXED_MATRIX=false
    FIXED_MATRIX,
    // true if this value should be translated
    TRANSLATE,
    // symboltype of this vector values
    VECTOR_TYPES,
    // added to ASSIGN nodes, to define their lhs/rhs and lhs parameters
    LHS,
    LHS_PARAMS,
    RHS,
    FUNCTION_OUTPUT,
    // funcall_params can be attached to a FUNCTION_PARAMETER_LIST node to say
    // 'these params are for a function call'
    FUNCALL_PARAMS,
    // matrix_params can be attached to a FUNCTION_PARAMETER_LIST node to say
    // 'these params are for a matrix'. the value is the AASTNode of the matrix.
    MATRIX_PARAMS,
    // list of AAST nodes that should be included in function definition
    // because they are used inside the function body, and are locally defined in an
    // enclosing function
    FUNCTION_ENV_PARAMS,
    // list of AAST nodes that should be included in function definition
    // because they are implicitly defined by another function that takes this
    // function as input.
    // ex. ode(@f, .... , a, b, c) -> f(t, y, a, b, c)
    FUNCTION_APPLY_ENV_PARAMS,
    // list of AAST APPLY nodes that represents calls to this function
    FUNCTION_CALLS,
    // this ID is a function parameter or output of a function
    IS_FUNCTION_PARAM,
    IS_FUNCTION_OUTPUT,
    // this node represents the name of a function
    CALLABLE,
    // a variable marked with this flag has its (local) scope defined in a parent
    // env defined outside this file (in another aast)
    IS_FUNCTION_ENV_PARAM,
    // if IS_FUNCTION_ENV_PARAM = true
    // list of nodes that use the this env param (useful for user
    // reporting)
    ENV_PARAM_REFERENCES,
    // a variable marked with this flag has its scope defined locally
    IS_FUNCTION_LOCAL_VAR,
    // true if this function has the formal types completely defined
    FUNCTION_INPUTS_RESOLVED,
    // statement needs to be reparsed, and is not in a function with undefined input
    // parameters
    REPARSE_STATEMENT,
    // statement needs to be reparsed, but it is in a function with undefined input
    // parameters
    // and thus only the containing function will be reparsed and this statement
    // will be reparsed indirectly
    // as a consequence
    UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION,
    // statement resolved
    STATEMENT_RESOLVED,
    // prevent resolution of function
    INHIBITS_RESOLUTION,
    // true if the input/output types and the inner variable types have been
    // completely defined. If this is set, FUNCTION_INPUTS_RESOLVED should be
    // removed (and is not added with attr())
    FUNCTION_RESOLVED,
    // this APPLY is a function call
    FUNCALL,
    // this APPLY is a MATRIX ACCESS (in r a[[2]])
    MATACCESS,
    // this APPLY is a MATRIX SLICE (in r a[2])
    MATSLICE,
    // used on multiple LHS nodes with matrix on RHS side
    // tells backend the portion of RHS matrix that should be copied to each LHS
    // node
    MATRIX_SLICES,
    // when a node of type function but with a different node type (AT)
    // is transformed into a plain function, his original type is stored here
    FUNCTION_TYPE,
    // contains the IFunction instance for this corefunction, or is not defined
    // for non-core functions
    CORE_FUNCTION,
    // if set on a node, that node type can't be used to infer return type of the
    // parent function
    SKIP_RETURN_NODE_PROPAGATION,
    // attribute present only in AT nodes, specify the FUNCTION node that this node
    // references.
    REF_FUNCTION,
    // this id refers to a core functions coming from frontend
    REF_CORE_FUNCTION,
    // a function that just calls another function. the value is the APPLY node for
    // the function called.
    APPLY_FUNCTION,
    // prevents print of multiple warnings related to the same code
    USER_WARNING,
    // marks persistent variables
    PERSISTENT_VAR,
    // marks global variables
    GLOBAL_VAR,
    // true if the expression for this node is inherited from the
    // globalVariables (external input)
    // differs from isInput flag on GType beacause the isInput flag is
    // propagated
    // to all the nodes that inherits the same type, while this is set only on
    // IDs that are unassigned without an externally-defined variable
    // ex. a = b; c = a; -> all have isInput=True but only b has
    // ETYPE_FROM_EXTERNAL_ENV
    ETYPE_FROM_EXTERNAL_ENV,
    // true on an ASSIGN (or GASSIGN) node
    // that deletes a column/row on the LHS matrix
    DELETE_ASSIGN_MATRIX,
    // true on an ID node
    // not defined.
    // the type of this node should be
    // interpreted as a speculation-based inference.
    IMPLICITLY_DEFINED,
    // ids with this flag should be forced to dynamic dimensions
    // if matrices because they depends on their own value.
    DYNAMIC_ID,
    // ids with this flag should be kept as integers if possible,
    // because they are used to address matrix elements
    PREFER_INT,
    // since output value of function is static, at every function call it is copied
    // to the left hand side.
    // but if one of the inputs is also the output, the input will be used as output
    // memory and we can avoid
    // memcpy. This attribute will be attached to the function node
    FUN_OUTPUT_IS_INPUT,
    // since output value of function is static, at every function call it is copied
    // to the left hand side.
    // but if one of the inputs is also the output, the input will be used as output
    // memory and we can avoid
    // memcpy. This attribute will be attached to the single output node (useful if
    // not all of the return types are also input types)
    IS_OUTPUT_INPUT
}
