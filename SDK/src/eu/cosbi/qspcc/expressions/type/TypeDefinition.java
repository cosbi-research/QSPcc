package eu.cosbi.qspcc.expressions.type;

public interface TypeDefinition {
    public enum BType {
	// MATRIX: double matrix, INDEX_MATRIX: matrix of integers used to index
	// other real matrices
	VOID(GType.class),
	BOOL(BoolType.class),
	INT(IntType.class),
	SCALAR(ScalarType.class),
	STRING(StringType.class),
	VECTOR(MatrixType.class),
	MATRIX(MatrixType.class),
	// in matlab END
	MATRIX_ACCESS_LAST(IndexType.class),
	// in matlab :
	MATRIX_ACCESS_ALL(IndexType.class),
	// a known function
	FUNCTION(FunctionType.class),
	// symbols still to be inferred..
	UNKNOWN(GType.class),
	// symbols never defined in all the parent environments
	UNDEFINED(GType.class),
	// slice
	MATRIX_ACCESS_SLICE(SliceType.class),
	// complex object with sub-fields
	STRUCT(StructType.class),
	// type this type represent the type of a variable
	// implemented as a linkType to any of the previous types.
	// thought to be used as type for instanceof() like functions
	TYPE(Type.class);

	private Class<? extends GType> refClass;

	private BType(Class<? extends GType> clazz) {
	    refClass = clazz;
	}

	protected Class<? extends GType> type() {
	    return refClass;
	}

    }

    public boolean hasChild(Class<? extends GType> clazz);

    public GType child(Class<? extends GType> clazz);
}
