package eu.cosbi.qspcc.backend.attributes;

import eu.cosbi.qspcc.ast.attrs.Attr;

public enum CAttr implements Attr {
    TRANSLATE,
    // contains a list of ids to avoid in the function parameters
    IDS_TO_AVOID_IN_PARAMETERST,
    SURVIVED_DIMESIONS,
    // flag for empty vector
    VOID_VECTOR,
    // in C, this variable should be static
    PERSISTENT_VAR,
    // this expression involves just sparse matrices and scalars
    SPARSE_EXPR,
    // sparse expr is true at top level and implies every operation is sparse.
    // but if sparse expression is false, some subnode can still be sparse,
    // this flag indicates this case
    CONTAINS_SPARSE_SUBEXPRESSION,
    //set to applyNode 'slice of boolean matrix' flag
    //needed in case this apply is then in used in an expression loop
    // (in that case it shouldn't use the canonical EXPR_INDEX iterator
    //  but a custom one since it can be shorter than other matrices)
    APPLY_OF_BOOLEAN_MATRIX,
    // this attribute will be present on variables that are allocated at the top of the function.
    // for those variables the ensureCheckCapacity inside the loop can be totally skipped
    PREALLOCATED,
    // this expression requires the creation of an intermediary variable (dot product, linear solve)
    MATRIX_EXPR,
    // openmp multiprocessing directives through @qspcc comment
    OMP_DIRECTIVE
}
