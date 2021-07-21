package eu.cosbi.qspcc.expressions.type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.utils.TypeUtils;

public class MatrixType extends DimensionType {
    private Logger logger = LogManager.getLogger(MatrixType.class);
    private IntType[] dims;
    private boolean sparse;

    public MatrixType(BType type, Object... params) {
	super(type, params);
	// [0] is the name
	if (params.length == 2) {
	    of = (GType) params[1];
	    sparse = false;
	} else if (params.length == 3) {
	    of = (GType) params[1];
	    dims = (IntType[]) params[2];
	    sparse = false;
	} else if (params.length > 3) {
	    of = (GType) params[1];
	    dims = (IntType[]) params[2];
	    sparse = (boolean) params[3];
	}
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public MatrixType(GType o) {
	super(o);
	MatrixType mtype = ((MatrixType) o);
	of = GType.get(mtype.of);
	if (mtype.dims != null) {
	    dims = new IntType[mtype.dims.length];
	    for (int i = 0; i < mtype.dims.length; ++i)
		dims[i] = new IntType(mtype.dims[i]);
	}
	sparse = mtype.sparse;
    }

    @Override
    public IntType[] dims() {
	return dims;
    }

    @Override
    public boolean hasChild(Class<? extends GType> clazz) {
	return clazz.isInstance(of);
    }

    @Override
    public GType child(Class<? extends GType> clazz) {
	return of;
    }

    @Override
    public GType of() {
	return of;
    }

    @Override
    public GType toScalar() {
	of = GType.get(BType.SCALAR);
	return this;
    }

    /**
     * set default dimensions with symbolic name and no value
     * 
     * @param ndims
     * @param name
     */
    public void defaultDims(int ndims) {
	if (ndims == 1) {
	    dims = new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, 1)) };
	} else if (ndims == 2) {
	    dims = new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, 1)),
		    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, 2)) };
	} else {
	    addDimsImpl(ndims);
	}
    }

    /**
     * 
     * @param totalDims:
     *            the number of dimensions the matrix should have after this
     *            call
     */
    public void addDims(int totalDims) {
	if (dims == null)
	    defaultDims(totalDims);
	else {
	    int nnewdims = totalDims - dims.length;
	    addDimsImpl(nnewdims);
	}
    }

    private void addDimsImpl(int ndims) {
	int oldlen = 0;
	if (dims != null)
	    oldlen = dims.length;

	IntType[] newDims = new IntType[oldlen + ndims];

	if (oldlen > 0)
	    System.arraycopy(dims, 0, newDims, 0, oldlen);

	for (int i = oldlen; i < oldlen + ndims; ++i)
	    newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, i + 1));
	//set new dims
	dims = newDims;
    }

    /**
     * change dimensions of this matrix
     * @param newMatrixDimensions
     */
    public void updateDims(IntType[] newMatrixDimensions) {
	if (newMatrixDimensions == null || newMatrixDimensions.length == 0)
	    return;
	dims = new IntType[newMatrixDimensions.length];
	for (int i = 0; i < newMatrixDimensions.length; ++i)
	    dims[i] = (IntType) GType.get(newMatrixDimensions[i]);

    }

    public MatrixType setSparse() {
	sparse = true;
	return this;
    }

    public boolean isSparse() {
	return sparse;
    }

    @Override
    public GType nameall(String symbol) {
	this.name = symbol;
	for (int i = 1; i <= dims.length; ++i)
	    if (!dims[i - 1].hasValue())
		dims[i - 1].name(TypeUtils.matrixDimName(symbol, i));
	    else
		dims[i - 1].name(null);
	return this;
    }
}
