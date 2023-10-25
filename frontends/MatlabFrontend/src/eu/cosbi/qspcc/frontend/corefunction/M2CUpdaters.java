package eu.cosbi.qspcc.frontend.corefunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.CastException;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.FunctionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.ScalarType;
import eu.cosbi.qspcc.expressions.type.StringType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.Type;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.utils.ArrayUtils;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.TypeUtils;

public class M2CUpdaters {
    private static Logger logger = LogManager.getLogger(M2CUpdaters.class);

    /**
     * decide the output type of undefined symbols. called only after a load
     * statement (or as configured in M2CFunction)
     * 
     * @return
     */
    public static Function<AASTNode, List<GType>> updateUndefinedAfterLoadSymbol(String loadStructureName) {
	return (AASTNode node) -> {
	    node.attr(NodeAttr.IMPLICITLY_DEFINED, node);
	    List<GType> list = new LinkedList<GType>();
	    if (node.parentExists(NodeType.FIELDACCESS, 2)) {
		AASTNode fieldaccess = node.parent(NodeType.FIELDACCESS);
		if (fieldaccess.childs().get(0).equals(node)) {
		    // if this is the first part of a fieldaccess, this is a struct
		    list.add(GType.get(BType.STRUCT).name(node.name()).setInput(loadStructureName));
		    return list;
		}
	    }
	    String mname = TypeUtils.matrixName(node);
	    // every other node is treated as matrix 1D
	    list.add(GType
		    .get(BType.MATRIX, mname, GType.get(BType.SCALAR),
			    new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(mname, 1)) })
		    .setInput(loadStructureName));
	    return list;
	};
    }

    /**
     * returns the list of rhs matrices
     */
    private static List<AASTNode> lhsMatrices(AASTNode node) {
	List<AASTNode> lhsNodes = new ArrayList<AASTNode>(2);
	boolean assign = node.parentExists(NodeType.ASSIGN);
	boolean gassign = node.parentExists(NodeType.GASSIGN);
	if (assign || gassign) {
	    AASTNode lhs = null;
	    if (assign)
		lhs = node.parent(NodeType.ASSIGN).child(NodeType.LHS);
	    else
		lhs = node.parent(NodeType.GASSIGN).child(NodeType.LHS);
	    AASTNode idNode = lhs.childs().get(0);
	    // skip idnode and expression nodes
	    while (idNode.type().equals(NodeType.ID_NODE) || idNode.type().equals(NodeType.EXPRESSION))
		idNode = idNode.childs().get(0);
	    // here idnode is id if single assignment a = ode..
	    // matrix if multiple assignment [a, b] = ode..
	    if (idNode.type().equals(NodeType.ID)) {
		lhsNodes.add(idNode);
		return lhsNodes;
	    } else if (idNode.type().equals(NodeType.MATRIX)) {
		// assume only one vector..
		AASTNode vector = idNode.childs().get(0);
		for (AASTNode el : vector.childs()) {
		    while (el.type().equals(NodeType.ID_NODE) || el.type().equals(NodeType.EXPRESSION))
			el = el.childs().get(0);
		    lhsNodes.add(el);
		}
		return lhsNodes;
	    } else {
		if (!node.hasAttr(NodeAttr.USER_WARNING)) {
		    logger.warn("Unexpected left-hand-side " + idNode + " ignoring.");
		    node.attr(NodeAttr.USER_WARNING, true);
		}
		return null;
	    }
	}
	// no lhs matrices
	return null;
    }

    private static List<GType> odeReturnTypeForOutputNode(AASTNode outNode, List<GType> paramTypes) {
	return odeReturnTypeForOutputNode(outNode, paramTypes, 0, 0);
    }

    private static List<GType> odeReturnTypeForOutputNode(AASTNode outNode, List<GType> paramTypes, int outNumber,
	    int totalOutputNodes) {
	IntType unknownDim1 = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(outNode, 1));
	IntType unknownDim2 = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(outNode, 2));
	List<GType> ret = new LinkedList<GType>();
	if (paramTypes.get(2).equals(BType.UNKNOWN)) {
	    ret.add(GType.get(BType.MATRIX, TypeUtils.matrixName(outNode), GType.get(BType.SCALAR),
		    new IntType[] { unknownDim1, unknownDim2 }));
	    return ret;
	}
	// dimension is driven by y0
	DimensionType y0 = (DimensionType) paramTypes.get(2);
	IntType[] y0Dims = TypeUtils.realDims(y0.dims());
	IntType[] newDimensions = new IntType[2];
	newDimensions[0] = unknownDim1;
	if (y0Dims == null)
	    // y0 dimension not known
	    newDimensions[1] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(outNode, 2));
	else if (y0Dims.length == 0 && y0.dims()[0].valueAsInt() == 1)
	    // y0Dims is empty because is a matrix 1x1 -> matrix Nx2 (1
	    // for t and 1 for y)
	    newDimensions[1] = outDimensionBasedOnColumnNumber(outNode, 2, outNumber, totalOutputNodes);
	else {
	    // y0Dims not empty
	    if (y0Dims[0].hasValue()) {
		int ncols = y0Dims[0].valueAsInt() + 1;
		newDimensions[1] = outDimensionBasedOnColumnNumber(outNode, ncols, outNumber, totalOutputNodes);
	    } else {
		String dim = y0Dims[0].name();
		newDimensions[1] = outDimensionBasedOnColumnNumber(outNode, dim, outNumber, totalOutputNodes);
	    }
	}

	ret.add(GType.get(BType.MATRIX, TypeUtils.matrixName(outNode), GType.get(BType.SCALAR), newDimensions));
	return ret;
    }

    private static IntType outDimensionBasedOnColumnNumber(AASTNode outNode, int ncols, int outNumber,
	    int totalOutputNodes) {
	IntType ret;
	if (outNumber == 0)
	    ret = (IntType) GType.get(BType.INT, ncols).name(TypeUtils.matrixDimName(outNode, 2));
	else if (outNumber <= ncols && outNumber < totalOutputNodes)
	    ret = (IntType) GType.get(BType.INT, 1).name(TypeUtils.matrixDimName(outNode, 2));
	else if (outNumber <= ncols && outNumber == totalOutputNodes)
	    ret = (IntType) GType.get(BType.INT, ncols - totalOutputNodes + 1)
		    .name(TypeUtils.matrixDimName(outNode, 2));
	else {
	    logger.error("Trying to split matrix with 2 columns in " + Integer.toString(totalOutputNodes)
		    + " submatrices in ODE call, returning whole matrix.");
	    ret = (IntType) GType.get(BType.INT, ncols).name(TypeUtils.matrixDimName(outNode, 2));
	}
	return ret;
    }

    private static IntType outDimensionBasedOnColumnNumber(AASTNode outNode, String dim, int outNumber,
	    int totalOutputNodes) {
	IntType ret;
	if (outNumber == 0)
	    ret = (IntType) GType.get(BType.INT).name(dim);
	else if (outNumber < totalOutputNodes)
	    ret = (IntType) GType.get(BType.INT, 1).name(TypeUtils.matrixDimName(outNode, 2));
	else if (outNumber == totalOutputNodes)
	    ret = (IntType) GType.get(BType.INT).name(dim + " - " + Integer.toString(totalOutputNodes));
	else {
	    logger.error("Trying to split matrix with 2 columns in " + Integer.toString(totalOutputNodes)
		    + " submatrices in ODE call, returning whole matrix.");
	    ret = (IntType) GType.get(BType.INT).name(dim);
	}
	return ret;
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateLinspaceReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    ValuedType start, end, step;
	    ValuedType n_points;
	    IntType dim;
	    GType[] unknownResult = new GType[] { GType.get(BType.UNKNOWN) };
	    if (paramTypes.size() >= 2) {
		GType gstart = paramTypes.get(0);
		GType gend = paramTypes.get(1);
		if (gstart.equals(BType.UNKNOWN) || gend.equals(BType.UNKNOWN))
		    return unknownResult;
		else if (!(gstart instanceof ValuedType) || !(gend instanceof ValuedType))
		    return new GType[] { GType.get(BType.UNDEFINED) };
		start = (ValuedType) gstart;
		end = (ValuedType) paramTypes.get(1);
		if (paramTypes.size() >= 3) {
		    if (paramTypes.get(2).equals(BType.UNKNOWN))
			return unknownResult;
		    n_points = (ValuedType) paramTypes.get(2);
		} else
		    // by default 100 points
		    n_points = (ValuedType) GType.get(BType.SCALAR, 50.0);
	    } else {
		logger.debug("Too few parameters for function 'linspace'");
		return unknownResult;
	    }

	    if (n_points.hasValue()) {
		// I can know how many elements the output matrix will have
		dim = (IntType) GType.get(BType.INT, n_points.valueAsInt());
		if (end.hasValue() && start.hasValue()) {
		    Double stepVal = (end.valueAsDouble() - start.valueAsDouble()) / (n_points.valueAsDouble() - 1);
		    if (stepVal % 1 == 0)
			// if whole
			step = (IntType) GType.get(BType.INT, stepVal.intValue());
		    else
			step = (ScalarType) GType.get(BType.SCALAR, stepVal.doubleValue());
		} else
		    step = (ScalarType) GType.get(BType.SCALAR);
	    } else {
		dim = (IntType) GType.get(BType.INT);
		step = (ScalarType) GType.get(BType.SCALAR);
	    }

	    GType of;
	    Triple<GType, GType, GType> slice;
	    if (start.equals(BType.INT) && end.equals(BType.INT) && step.equals(BType.INT)) {
		// int slice
		of = GType.get(BType.INT);
		slice = new Triple<GType, GType, GType>(start, step, end);
	    } else {
		// double slice
		of = GType.get(BType.SCALAR);
		slice = new Triple<GType, GType, GType>(start.castTo(BType.SCALAR), step.castTo(BType.SCALAR),
			end.castTo(BType.SCALAR));
	    }
	    GType ret = GType.get(BType.MATRIX_ACCESS_SLICE, "_tmp_", of, slice, dim);
	    ret.name(TypeUtils.matrixName(node));
	    dim.name(TypeUtils.matrixDimName(node, 1));
	    return new GType[] { ret };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateSparseReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 1 && paramTypes.get(0).isCastableToMatrix()) {
		DimensionType dtype = (DimensionType) paramTypes.get(0);
		//GType ofType = GType.get(dtype.of());
		// assume sparse matrix is always of scalar
		GType ofType = GType.get(BType.SCALAR);

		IntType[] dims = new IntType[dtype.dims().length];
		for (int i = 0; i < dtype.dims().length; ++i) {
		    dims[i] = (IntType) GType.get(dtype.dims()[i]);
		    if (!dims[i].hasValue())
			dims[i].name(TypeUtils.matrixDimName(node, i + 1));
		}
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), ofType, dims, true) };
	    } else if (paramTypes.size() == 2 && paramTypes.get(0).isCastableToScalar()
		    && paramTypes.get(1).isCastableToScalar()) {
		BiFunction<AASTNode, List<GType>, GType[]> fun = updateMatrixInitializerReturnType(
			GType.get(BType.SCALAR));
		return fun.apply(node, paramTypes);
	    } else if (paramTypes.size() >= 3) {
		if (paramTypes.get(0).isCastableToScalar() && paramTypes.get(1).isCastableToScalar()
			&& paramTypes.get(2).isCastableToScalar()) {
		    // i,j,v a(i,j) = v with i,j,v scalars
		    IntType[] dims = new IntType[2];
		    int row = 0;
		    int col = 1;
		    if (paramTypes.size() == 5) {
			row = 3;
			col = 4;
		    }
		    dims[0] = (IntType) paramTypes.get(row).castTo(BType.INT);
		    if (!dims[0].hasValue())
			dims[0].name(TypeUtils.matrixDimName(node, 1));
		    dims[1] = (IntType) paramTypes.get(col).castTo(BType.INT);
		    if (!dims[1].hasValue())
			dims[1].name(TypeUtils.matrixDimName(node, 2));
		    //GType ofType = GType.get(paramTypes.get(2).type());
		    // assume sparse matrix is always of scalar
		    GType ofType = GType.get(BType.SCALAR);
		    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), ofType, dims, true) };
		} else if (paramTypes.get(0).isCastableToMatrix() && paramTypes.get(1).isCastableToMatrix()
			&& paramTypes.get(2).isCastableToMatrix()) {
		    // (i,j,v) a(i,j) = v with (i,j,v) vectors
		    // return a matrix with dimension max(i) * max(j) if paramTypes.size == 3,
		    // m,n if paramtypes.size == 5
		    IntType[] dims = new IntType[2];
		    if (paramTypes.size() < 5) {
			dims[0] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));
			dims[1] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2));
		    } else {
			dims[0] = (IntType) paramTypes.get(3).castTo(BType.INT);
			if (!dims[0].hasValue())
			    dims[0].name(TypeUtils.matrixDimName(node, 1));
			dims[1] = (IntType) paramTypes.get(4).castTo(BType.INT);
			if (!dims[1].hasValue())
			    dims[1].name(TypeUtils.matrixDimName(node, 2));
		    }
		    //GType ofType = GType.get(((DimensionType) paramTypes.get(2)).of());
		    // assume sparse matrix is always of scalar
		    GType ofType = GType.get(BType.SCALAR);
		    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), ofType, dims, true) };
		} else
		    return new GType[] { GType.get(BType.UNDEFINED) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateTriReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() >= 1 && paramTypes.size() <= 2 && paramTypes.get(0).isCastableToMatrix()) {
		// returns the same matrix, with triangular upper/lower set
		DimensionType dtype = (DimensionType) paramTypes.get(0);
		IntType[] dims = dtype.dims();
		if (dims.length == 2) {
		    IntType row = dims[0];
		    IntType col = dims[1];
		    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(dtype.of()),
			    new IntType[] { (IntType) GType.get(row), (IntType) GType.get(col) }) };
		} else {
		    // 1 d matrix not implemented yet
		    if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
				paramTypes.get(0), BType.INT, 0).stringify());
			node.attr(NodeAttr.USER_WARNING, true);
		    }
		    return new GType[] { GType.get(BType.UNDEFINED) };
		}
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateDiagReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (paramTypes.get(0).isCastableToMatrix()) {
		// return the min(nrow, ncols)x1 vector whose values are the diagonal values of
		// input matrix
		DimensionType dtype = (DimensionType) paramTypes.get(0);
		IntType[] dims = dtype.dims();
		IntType[] realDims = TypeUtils.realDims(dims);
		if (realDims.length == 1) {
		    // return a diagonal matrix
		    IntType row = realDims[0];
		    IntType rdim, cdim;
		    if (row.hasIntValue()) {
			rdim = (IntType) GType.get(BType.INT, row.valueAsInt());
			cdim = (IntType) GType.get(BType.INT, row.valueAsInt());
		    } else {
			rdim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));
			cdim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2));
		    }
		    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), dtype.of(),
			    new IntType[] { rdim, cdim }) };
		} else if (dims.length == 2) {
		    // 2d matrix extract his diagonal
		    IntType row = dims[0];
		    IntType col = dims[1];
		    IntType vdim;
		    if (row.hasIntValue() && col.hasIntValue()) {
			if (row.valueAsInt() < col.valueAsInt())
			    vdim = (IntType) GType.get(BType.INT, row.valueAsInt());
			else
			    vdim = (IntType) GType.get(BType.INT, col.valueAsInt());
		    } else
			vdim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));

		    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), dtype.of(),
			    new IntType[] { vdim, (IntType) GType.get(BType.INT, 1) }) };
		} else
		// 1 d matrix not implemented yet
		if (!node.hasAttr(NodeAttr.USER_WARNING)) {
		    logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
			    paramTypes.get(0), BType.INT, 0).stringify());
		    node.attr(NodeAttr.USER_WARNING, true);
		}
		return new GType[] { GType.get(BType.UNDEFINED) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};

    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateMatrixInitializerReturnType(GType defaultOfType) {
	return updateMatrixInitializerReturnType(defaultOfType, false);
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateMatrixInitializerReturnType(GType defaultOfType,
	    boolean defaultType) {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		if (!defaultType)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };
		else
		    return new GType[] { defaultOfType };

	    List<IntType> dims = new LinkedList<IntType>();
	    GType ofType = defaultOfType;
	    for (int i = 0; i < paramTypes.size(); ++i) {
		if (paramTypes.size() == 1 && paramTypes.get(i).equals(BType.UNKNOWN))
		    return new GType[] { paramTypes.get(i) };

		if (i == paramTypes.size() - 1 && paramTypes.get(i).equals(BType.TYPE)) {
		    ofType = ((Type) paramTypes.get(i)).of();
		    if (ofType.isCastableToMatrix())
			ofType = ((DimensionType) ofType).of();
		    break;
		}

		if (i == 0 && paramTypes.size() <= 2 && paramTypes.get(i).isCastableToMatrix()) {
		    // if given a matrix, the matrix values should be interpreted as
		    // the dimensions of the output matrix, but we don't know the numbers inside the
		    // matrix, so add generic int.
		    DimensionType expr = ((DimensionType) paramTypes.get(i));
		    IntType[] rdims = TypeUtils.realDims(expr.dims());
		    if (rdims.length > 1)
			return new GType[] { GType.get(BType.UNDEFINED) };
		    if (rdims[0].hasValue())
			for (int j = 0; j < rdims[0].valueAsInt(); ++j) {
			    IntType dimsj = (IntType) GType.get(BType.INT);
			    dimsj.name(TypeUtils.matrixDimName(node, j + 1));
			    dims.add(dimsj);
			}
		    else {
			// wait for dimension to be known
			return new GType[] { GType.get(BType.UNKNOWN) };
		    }
		} else {
		    GType paramType = paramTypes.get(i);

		    IntType dimsi = null;
		    if (paramType.isCastableToScalar())
			// this string is the name of the output type
			dimsi = (IntType) paramType.castTo(BType.INT);
		    else if (paramType.equals(BType.UNKNOWN))
			// we believe that ones(x , x) means x are integers
			dimsi = (IntType) GType.get(BType.INT);
		    else {
			if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			    logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
				    paramType, BType.INT, i).stringify());
			    node.attr(NodeAttr.USER_WARNING, true);
			}
			dimsi = (IntType) GType.get(BType.INT);
		    }
		    if (!dimsi.hasValue())
			dimsi.name(TypeUtils.matrixDimName(node, i + 1));
		    dims.add(dimsi);

		    if (paramTypes.size() == 1)
			// just 1 dimension means nxn not 1xn
			dims.add((IntType) GType.get(dimsi).name(TypeUtils.matrixDimName(node, i + 2)));
		}
	    }
	    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), ofType,
		    dims.toArray(new IntType[dims.size()])) };
	};

    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateFindReturnType() {
	/*
	 * I = find(X) returns the linear indices corresponding to the nonzero entries
	 * of the array X. X may be a logical expression. Use IND2SUB(SIZE(X),I) to
	 * calculate multiple subscripts from the linear indices I.
	 */
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    GType paramType = paramTypes.get(0);
	    if (paramType.equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (paramType.isCastableToScalar())
		return new GType[] { GType.get(paramType) };
	    else if (paramType.isCastableToMatrix()) {
		IntType dim = null;
		if (paramTypes.size() <= 1)
		    dim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));
		else {
		    GType glimit = paramTypes.get(1);
		    // always return a matrix even if limit is 1 (because think about how to check
		    // for 0 return elements)
		    if (glimit.isCastableToInt()) {
			ValuedType<?> limit = (ValuedType<?>) glimit;
			if (limit.hasValue())
			    dim = (IntType) GType.get(BType.INT, limit.valueAsInt())
				    .name(TypeUtils.matrixDimName(node, 1));
			else
			    dim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));
		    } else {
			logger.error("Unsupported second parameter type " + glimit + " in 'find'");
			return new GType[] { GType.get(BType.UNDEFINED) };
		    }
		}
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.INT),
			new IntType[] { dim }) };
	    } else {
		logger.error("Unsupported type " + paramType + " in 'find'");
		return new GType[] { GType.get(BType.UNDEFINED) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateMathReturnType(boolean clone) {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    GType ptype = paramTypes.get(0);
	    if (ptype.equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (ptype.isCastableToMatrix()) {
		DimensionType rtype = (DimensionType) GType.get(ptype);
		IntType[] dims = ((DimensionType) rtype).dims();
		for (int i = 1; i <= dims.length; ++i)
		    dims[i - 1].name(TypeUtils.matrixDimName(node, i));
		return new GType[] {
			GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR), dims) };
	    } else if (ptype.isCastableToScalar()) {
		if (clone) {
		    // check if integer negative (case 10^-5)
		    if (paramTypes.size() > 1 && ptype.equals(BType.INT)
			    && ((ValuedType<?>) paramTypes.get(1)).equals(BType.INT)
			    && ((ValuedType<?>) paramTypes.get(1)).hasValue()
			    && ((ValuedType<?>) paramTypes.get(1)).valueAsInt() < 0)
			return new GType[] { GType.get(BType.SCALAR) };
		    else
			// copy constructor
			return new GType[] { GType.get(ptype) };
		} else
		    // always return scalar
		    return new GType[] { GType.get(BType.SCALAR) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    /**
     * statistical function reduces the dimensionality of the input matrix of 1. (a
     * 2d matrix becomes a 1d matrix, a 1d matrix becomes a scalar)
     * 
     * @return
     */
    public static BiFunction<AASTNode, List<GType>, GType[]> updateStatisticalReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNDEFINED) };

	    GType ptype = paramTypes.get(0);
	    if (ptype.isCastableToMatrix() && paramTypes.size() == 1) {
		DimensionType rtype = (DimensionType) GType.get(ptype);
		IntType[] dims = ((DimensionType) rtype).dims();
		List<IntType> finalDims = new LinkedList<>();
		// if 1d matrix, return a scalar
		if (TypeUtils.realDims(dims).length <= 1)
		    return new GType[] { rtype.of() };

		boolean first = true;
		for (int i = 1; i <= dims.length; ++i) {
		    if (first) {
			if (!dims[i - 1].hasValue() || dims[i - 1].valueAsInt() > 1) {
			    // max/min operates on the first dimension
			    finalDims.add((IntType) GType.get(BType.INT, 1).name(TypeUtils.matrixDimName(node, i)));
			    first = false;
			} // else skip this dimension
		    } else
			finalDims.add((IntType) dims[i - 1].name(TypeUtils.matrixDimName(node, i)));
		}

		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(rtype.of()),
			finalDims.toArray(new IntType[finalDims.size()])) };
	    } else if (ptype.isCastableToMatrix() || paramTypes.size() > 1) {
		// in this case the function operates point-wise on two identically-sized
		// matrices
		DimensionType rtype = null;
		if (ptype.isCastableToMatrix())
		    rtype = (DimensionType) GType.get(ptype);
		else if (paramTypes.get(1).isCastableToMatrix()) {
		    // use second parameter
		    rtype = (DimensionType) GType.get(paramTypes.get(1));
		} else {
		    // copy constructor (two scalars)
		    if (ptype.canRepresent(paramTypes.get(1)))
			return new GType[] { GType.get(ptype) };
		    else
			return new GType[] { GType.get(paramTypes.get(1)) };
		}
		IntType[] dims = ((DimensionType) rtype).dims();
		for (int i = 1; i <= dims.length; ++i)
		    if (!dims[i - 1].hasValue())
			dims[i - 1].name(TypeUtils.matrixDimName(node, i));
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(rtype.of()), dims) };
	    } else {
		// copy constructor
		if (paramTypes.size() > 1)
		    if (ptype.canRepresent(paramTypes.get(1)))
			return new GType[] { GType.get(ptype) };
		    else
			return new GType[] { GType.get(paramTypes.get(1)) };
		else
		    return new GType[] { GType.get(ptype) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateSumReturnType(int dimParameterIndex,
	    boolean forceToScalar) {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    GType ptype = paramTypes.get(0);
	    if (ptype.equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (ptype.isCastableToScalar())
		return new GType[] { GType.get(ptype) };
	    else if (ptype.isCastableToMatrix()) {
		DimensionType rtype = (DimensionType) GType.get(ptype);
		IntType[] dims = ((DimensionType) rtype).dims();
		List<IntType> finalDims = new LinkedList<>();
		// if 1d matrix, return a scalar
		if (TypeUtils.realDims(dims).length <= 1)
		    return new GType[] { rtype.of() };

		int dim = 1;
		if (paramTypes.size() > dimParameterIndex && paramTypes.get(dimParameterIndex).isCastableToScalar())
		    dim = ((ValuedType) paramTypes.get(dimParameterIndex)).valueAsInt();

		for (int i = 1; i <= dims.length; ++i) {
		    if (i == dim) {
			if (dims[i - 1].hasValue() && dims[i - 1].valueAsInt().equals(1)) {
			    // consider the next one
			    dim++;
			}
			finalDims.add((IntType) GType.get(BType.INT, 1));
		    } else {
			IntType dimn = (IntType) GType.get(dims[i - 1]);
			if (!dimn.hasValue())
			    dimn.name(TypeUtils.matrixDimName(node, i));
			finalDims.add(dimn);
		    }
		}

		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node),
			forceToScalar ? GType.get(BType.SCALAR) : GType.get(rtype.of()),
			finalDims.toArray(new IntType[finalDims.size()])) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateOdeReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    // check if output node on the left hand side is a variable or a matrix of
	    // variables
	    List<AASTNode> lhsMat = lhsMatrices(node);
	    if (lhsMat == null || lhsMat.size() == 1) {
		AASTNode outNode = null;
		if (lhsMat == null)
		    outNode = node;
		else
		    outNode = lhsMat.get(0);

		List<GType> ret = odeReturnTypeForOutputNode(outNode, paramTypes);
		return ret.toArray(new GType[ret.size()]);
	    } else {
		// more than one return type, wrap everything in a struct
		String sname = Symbols.getSymbolFromType("ode_out", BType.STRUCT);
		StructType outStruct = (StructType) GType.get(BType.STRUCT).name(sname);
		outStruct.explodeOnAssignment(true);
		int n = 1;
		for (AASTNode lhsId : lhsMat)
		    outStruct.addField(odeReturnTypeForOutputNode(lhsId, paramTypes, n++, lhsMat.size()),
			    TypeUtils.matrixName(lhsId));
		return new GType[] { outStruct };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateOdeParams() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    IntType[] newDimensions = null;
	    IntType[] newOutDimensions = null;
	    // integrator type should always be a function
	    GType integratorType;
	    boolean has_extra_params = paramTypes.size() > 3;
	    GType[] extra_params = null;
	    if (has_extra_params)
		extra_params = Arrays.copyOfRange(paramTypes.toArray(new GType[paramTypes.size()]), 3,
			paramTypes.size());

	    GType initialConditions = paramTypes.get(2);
	    if (!initialConditions.equals(BType.UNKNOWN)) {
		if (!(initialConditions instanceof DimensionType))
		    return new GType[] { GType.get(BType.UNDEFINED).name(
			    "Function ODE initial conditions should be of type matrix, not " + initialConditions) };
		DimensionType y0 = (DimensionType) paramTypes.get(2);
		IntType[] y0Dims = y0.dims();
		newDimensions = new IntType[y0Dims.length];
		newOutDimensions = new IntType[y0Dims.length];
		for (int i = 0; i < y0Dims.length; ++i) {
		    newDimensions[i] = (IntType) GType.get(y0Dims[i]);
		    newOutDimensions[i] = (IntType) GType.get(y0Dims[i]);
		}

		if (paramTypes.size() > 4) {
		    GType[] integrator_extra_params = Arrays
			    .copyOfRange(paramTypes.toArray(new GType[paramTypes.size()]), 4, paramTypes.size());
		    integratorType = GType.get(BType.FUNCTION,
			    ArrayUtils.concatenate(new GType[] { GType.get(BType.SCALAR),
				    GType.get(BType.MATRIX, y0.name(), GType.get(BType.SCALAR), newDimensions) },
				    integrator_extra_params),
			    new GType[] { GType.get(BType.MATRIX, "dydt", GType.get(BType.SCALAR), newOutDimensions) });
		} else
		    integratorType = GType.get(BType.FUNCTION,
			    new GType[] { GType.get(BType.SCALAR),
				    GType.get(BType.MATRIX, y0.name(), GType.get(BType.SCALAR), newDimensions) },
			    new GType[] { GType.get(BType.MATRIX, "dydt", GType.get(BType.SCALAR), newOutDimensions) });

	    } else
		integratorType = GType.get(BType.FUNCTION,
			new GType[] { GType.get(BType.UNKNOWN), GType.get(BType.UNKNOWN) },
			new GType[] { GType.get(BType.UNKNOWN) });

	    GType[] ret;
	    // @function timespan initial conditions
	    ret = new GType[] { integratorType, GType.get(paramTypes.get(1)), GType.get(paramTypes.get(2)) };
	    if (has_extra_params)
		ret = ArrayUtils.concatenate(ret, extra_params);
	    return ret;

	};
    }
    
    public static BiFunction<AASTNode, List<GType>, GType[]> updateLsqNonLinReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };
	    
		DimensionType y0 = (DimensionType) paramTypes.get(1);
		IntType[] y0Dims = y0.dims();
		IntType[] newDimensions = new IntType[y0Dims.length];
		for (int i = 0; i < y0Dims.length; ++i) {
		    newDimensions[i] = (IntType) GType.get(y0Dims[i]);
		}
		
	    // check if output node on the left hand side is a variable or a matrix of
	    // variables
	    List<AASTNode> lhsMat = lhsMatrices(node);
		AASTNode outNode = null;
		if (lhsMat == null)
		    outNode = node;
		else
		    outNode = lhsMat.get(0);
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(outNode), GType.get(BType.SCALAR), newDimensions) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateLsqNonLinParams() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
	    	// doesn't support execution on 0 parameters
	    	return new GType[] { GType.get(BType.UNKNOWN) };
	    // integrator type should always be a function
	    FunctionType integratorType;

	    GType initialConditions = paramTypes.get(1);
	    if (!initialConditions.equals(BType.UNKNOWN)) {
		if (!(initialConditions instanceof DimensionType))
		    return new GType[] { GType.get(BType.UNDEFINED).name(
			    "Function lsqnonlin initial conditions should be of type matrix, not " + initialConditions) };

		// update param and return type
	    integratorType = (FunctionType) GType.get(paramTypes.get(0));
	    integratorType.input(0, GType.get(initialConditions));
	    integratorType.output(0, GType.get(initialConditions));
	    
	    } else
		integratorType = (FunctionType) GType.get(BType.FUNCTION,
			new GType[] { GType.get(BType.UNKNOWN) },
			new GType[] { GType.get(BType.UNKNOWN) });

	    GType lb = GType.get(paramTypes.get(2));
	    GType ub = GType.get(paramTypes.get(3));
	    List<AASTNode> formalParametersUnknown = new ArrayList<AASTNode>(2);
	    List<AASTNode> actualParametersUnknown = new ArrayList<AASTNode>(2);
	    List<AASTNode> envParametersUnknown = new ArrayList<AASTNode>(2);
	    List<AASTNode> params = node.childs(NodeType.FUNCTION_PARAMETER_LIST);
    	AASTNode funIdNode = node.child(NodeType.ID);
    	IFunction coreFun = funIdNode.coreFunction();
    	
	    if(lb.equals(BType.UNKNOWN) || lb.equals(BType.UNDEFINED)) {
	    	actualParametersUnknown.add(params.get(2));
	    	List<GType> formalParamTypes = coreFun.getParamTypes();
	    	GType lb_t = formalParamTypes.get(2);
	    	AASTNode lb_fakeNode = new AASTNode(null, NodeType.ID, lb_t.name());
	    	lb_fakeNode.expr(lb_t);
	    	formalParametersUnknown.add(lb_fakeNode);
	    	envParametersUnknown.add(null);
	    }
	    if(ub.equals(BType.UNKNOWN) || ub.equals(BType.UNDEFINED)) {
	    	actualParametersUnknown.add(params.get(3));
	    	List<GType> formalParamTypes = coreFun.getParamTypes();
	    	GType ub_t = formalParamTypes.get(3);
	    	AASTNode ub_fakeNode = new AASTNode(null, NodeType.ID, ub_t.name());
	    	ub_fakeNode.expr(ub_t);
	    	formalParametersUnknown.add(ub_fakeNode);
	    	envParametersUnknown.add(null);
	    }
	    if(!actualParametersUnknown.isEmpty()) {
	    	TypeException ex = new TypeException(ErrorMessage.FUN_FORMAL_PARAMS_UNKNOWN, node,
			    formalParametersUnknown, actualParametersUnknown, envParametersUnknown,
			    coreFun, funIdNode.name());
		    node.deferrableError(ErrorMessage.FUN_FORMAL_PARAMS_UNKNOWN, ex);
	    }	    
	    return new GType[] { integratorType, GType.get(initialConditions), lb, ub, GType.get(paramTypes.get(4)) };

	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateRandPermReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() > 0) {
		GType end = paramTypes.get(0);
		if (end.equals(BType.UNDEFINED))
		    return new GType[] { GType.get(BType.UNDEFINED) };
		else if (end.equals(BType.UNKNOWN))
		    return new GType[] { GType.get(BType.UNKNOWN) };

		IntType dim = null;
		if (paramTypes.size() > 1) {
		    GType howmany = paramTypes.get(1);
		    if (howmany.isCastableToBool())
			dim = (IntType) GType.get(howmany);
		    else {
			if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			    logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
				    howmany, BType.INT, 1).stringify());
			    node.attr(NodeAttr.USER_WARNING, true);
			}

			dim = (IntType) howmany.castTo(BType.INT);
		    }
		} else if (end.isCastableToBool())
		    dim = (IntType) GType.get(end);
		else {
		    if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node, end,
				BType.INT).stringify());
			node.attr(NodeAttr.USER_WARNING, true);
		    }

		    dim = (IntType) end.castTo(BType.INT);
		}

		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR),
			new IntType[] { dim }) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };

	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateRandReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {

	    IntType[] dims = null;
	    if (paramTypes.isEmpty())
		return new GType[] { GType.get(BType.SCALAR) };
	    else if (paramTypes.size() == 1 && paramTypes.get(0).isCastableToMatrix())
		dims = ((DimensionType) paramTypes.get(0)).dims();
	    else {
		dims = new IntType[(paramTypes.size() == 1) ? 2 : paramTypes.size()];
		int i;
		for (i = 0; i < paramTypes.size(); ++i) {
		    GType paramType = paramTypes.get(i);
		    if (paramType.equals(BType.UNKNOWN))
			return new GType[] { paramTypes.get(i) };
		    if (paramType.equals(BType.INT))
			dims[i] = (IntType) GType.get(paramType);
		    else {
			if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			    GException we = new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
				    paramType, BType.INT, i);
			    node.compilationUnit().addWarning(we);
			    node.attr(NodeAttr.USER_WARNING, true);
			}
			dims[i] = (IntType) paramType.castTo(BType.INT);
		    }
		    if (!dims[i].hasValue())
			dims[i].name(TypeUtils.matrixDimName(node, i + 1));
		}

		if (i == 1) {
		    dims[i] = (IntType) GType.get(dims[0]);
		    if (!dims[i].hasValue())
			dims[i].name(TypeUtils.matrixDimName(node, 2));
		}
	    }
	    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR), dims) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateKronReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    IntType[] dims = null;
	    GType of = null;

	    if (paramTypes.isEmpty() || paramTypes.size() < 2)
		return new GType[] { GType.get(BType.UNDEFINED) };
	    else if (paramTypes.get(0).isCastableToScalar() && paramTypes.get(1).isCastableToScalar()) {
		if (paramTypes.get(0).lessGenericThan(paramTypes.get(1)))
		    return new GType[] { GType.get(paramTypes.get(1)) };
		else
		    return new GType[] { GType.get(paramTypes.get(0)) };
	    } else if (paramTypes.get(0).isCastableToScalar() && paramTypes.get(1).isCastableToMatrix()) {
		// a * B has dimensions of B
		DimensionType dtype = (DimensionType) paramTypes.get(1);
		dims = new IntType[dtype.dims().length];
		if (dtype.of().lessGenericThan(paramTypes.get(0)))
		    of = GType.get(paramTypes.get(0));
		else
		    of = GType.get(dtype.of());
		int i = 0;
		for (IntType dim : dtype.dims())
		    dims[i] = (IntType) GType.get(dim).name(TypeUtils.matrixDimName(node, ++i));
	    } else if (paramTypes.get(1).isCastableToScalar() && paramTypes.get(0).isCastableToMatrix()) {
		// A * b has dimensions of A
		DimensionType dtype = (DimensionType) paramTypes.get(0);
		dims = new IntType[dtype.dims().length];
		if (dtype.of().lessGenericThan(paramTypes.get(1)))
		    of = GType.get(paramTypes.get(1));
		else
		    of = GType.get(dtype.of());
		int i = 0;
		for (IntType dim : dtype.dims())
		    dims[i] = (IntType) GType.get(dim).name(TypeUtils.matrixDimName(node, ++i));
	    } else if (paramTypes.get(0).isCastableToMatrix() && paramTypes.get(1).isCastableToMatrix()) {
		// A * B has dimension a1*b1*..., a2*b2*...
		DimensionType atype = (DimensionType) paramTypes.get(0);
		DimensionType btype = (DimensionType) paramTypes.get(1);
		dims = new IntType[atype.dims().length];
		if (atype.of().lessGenericThan(btype.of()))
		    of = GType.get(btype.of());
		else
		    of = GType.get(atype.of());

		for (int i = 0; i < atype.dims().length; ++i) {
		    IntType adim = atype.dims()[i];
		    IntType bdim = btype.dims()[i];
		    if (adim.hasIntValue() && bdim.hasIntValue())
			dims[i] = (IntType) GType.get(BType.INT, adim.value() * bdim.value())
				.name(TypeUtils.matrixDimName(node, i + 1));
		    else
			// discard eventual value, that will be decided at execution time
			dims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i + 1));
		}
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), of, dims) };

	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateRepmatReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    IntType[] dims = null;
	    GType of = null;

	    if (paramTypes.isEmpty() || paramTypes.size() < 2 || !paramTypes.get(0).isCastableToMatrix())
		return new GType[] { GType.get(BType.UNDEFINED) };
	    else {
		// A * B has dimension a1*b1*..., a2*b2*...
		DimensionType atype = (DimensionType) paramTypes.get(0);
		dims = new IntType[atype.dims().length];
		of = GType.get(atype.of());

		Integer[] multipliers = new Integer[atype.dims().length];
		if (paramTypes.size() == 2 && paramTypes.get(1).isCastableToScalar()) {
		    ValuedType<?> firstParam = ((ValuedType<?>) paramTypes.get(1));

		    if (firstParam.hasIntValue())
			for (int i = 0; i < atype.dims().length; ++i)
			    multipliers[i] = firstParam.valueAsInt();
		} else {
		    for (int i = 1; i < atype.dims().length + 1 && i < paramTypes.size(); ++i) {
			if (!paramTypes.get(i).isCastableToScalar())
			    // unsupported
			    return new GType[] { GType.get(BType.UNDEFINED) };
			ValuedType<?> param = (ValuedType<?>) paramTypes.get(i);
			if (param.hasIntValue())
			    multipliers[i - 1] = param.valueAsInt();
		    }
		}

		for (int i = 0; i < atype.dims().length; ++i) {
		    IntType adim = atype.dims()[i];
		    if (adim.hasIntValue() && multipliers[i] != null)
			dims[i] = (IntType) GType.get(BType.INT, adim.value() * multipliers[i])
				.name(TypeUtils.matrixDimName(node, i + 1));
		    else
			// discard eventual value, that will be decided at execution time
			dims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i + 1));
		}
	    }
	    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), of, dims) };

	};

    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateRandiReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {

	    IntType[] dims = null;
	    if (paramTypes.isEmpty())
		return new GType[] { GType.get(BType.INT) };
	    else if (paramTypes.size() == 1 && paramTypes.get(0).isCastableToMatrix())
		dims = ((DimensionType) paramTypes.get(0)).dims();
	    else {
		if (paramTypes.size() == 1)
		    return new GType[] { GType.get(BType.INT) };

		if (paramTypes.size() == 2)
		    // randi(imax,n) returns an n-by-n matrix of pseudorandom integers
		    // drawn from the discrete uniform distribution on the interval [1,imax].
		    paramTypes.add(GType.get(paramTypes.get(1)));

		dims = new IntType[paramTypes.size() - 1];
		// skip the first one that is the range of the random numbers
		for (int i = 1; i < paramTypes.size(); ++i) {
		    GType paramType = paramTypes.get(i);
		    if (paramType.equals(BType.UNKNOWN))
			return new GType[] { paramTypes.get(i) };
		    if (paramType.equals(BType.INT))
			dims[i - 1] = (IntType) GType.get(paramType);
		    else {
			if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			    GException we = new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
				    paramType, BType.INT, i);
			    node.compilationUnit().addWarning(we);
			    node.attr(NodeAttr.USER_WARNING, true);
			}

			dims[i - 1] = (IntType) paramType.castTo(BType.INT);
		    }
		    if (!dims[i - 1].hasValue())
			dims[i - 1].name(TypeUtils.matrixDimName(node, i));
		}
	    }
	    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.INT), dims) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateIsMemberReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {

	    if (paramTypes.size() == 1 && paramTypes.get(0).equals(BType.UNKNOWN) || paramTypes.size() == 2
		    && (paramTypes.get(0).equals(BType.UNKNOWN) || paramTypes.get(1).equals(BType.UNKNOWN))) {
		return new GType[] { GType.get(BType.UNKNOWN) };
		// if the first member is a scalar and the second one is a matrix, returns a
		// boolean
	    } else if (paramTypes.size() == 2 && paramTypes.get(0).isCastableToScalar()
		    && paramTypes.get(1).isCastableToMatrix())
		return new GType[] { GType.get(BType.BOOL) };
	    // if the first member is a matrix and the second one is a matrix, returns a
	    // boolean
	    // matrix of the same dimensions of the first one
	    else if (paramTypes.size() == 2 && paramTypes.get(0).isCastableToMatrix()
		    && paramTypes.get(1).isCastableToMatrix()) {

		GType ptype = paramTypes.get(0);
		DimensionType rtype = (DimensionType) GType.get(ptype);
		IntType[] dims = ((DimensionType) rtype).dims();
		for (int i = 1; i <= dims.length; ++i)
		    if (!dims[i - 1].hasValue())
			dims[i - 1].name(TypeUtils.matrixDimName(node, i));
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.BOOL), dims) };
	    } else {
		return new GType[] { GType.get(BType.UNDEFINED) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateReshapeReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    Boolean hasOnlyIntTypesAfterFirstDimension = true;
	    if (paramTypes.size() > 1) {
		for (int i = 1; i < paramTypes.size(); i++) {
		    if (paramTypes.get(i).equals(BType.UNKNOWN))
			return new GType[] { GType.get(BType.UNKNOWN) };

		    if (!(paramTypes.get(i).isCastableToScalar() || (paramTypes.get(i)) instanceof DimensionType
			    && TypeUtils.isScalarMatrix(((DimensionType) paramTypes.get(i)).dims()))
			    && (((DimensionType) paramTypes.get(i)).of().isCastableToScalar())) {
			hasOnlyIntTypesAfterFirstDimension = false;
			break;
		    }
		}
	    }

	    if (paramTypes.size() > 2 && paramTypes.get(0).isCastableToMatrix() && hasOnlyIntTypesAfterFirstDimension) {
		// check if it's a double or an int
		DimensionType paramTypes0AsDimensionType = (DimensionType) paramTypes.get(0);
		GType basicType = paramTypes0AsDimensionType.of();

		// dimension number is the number of parameters beyond the first
		List<IntType> dimList = new ArrayList<IntType>();
		for (int i = 1; i < paramTypes.size(); ++i) {
		    GType paramType = paramTypes.get(i);
		    if (paramType.isCastableToScalar() && ((ValuedType) paramType).hasIntValue()
			    && ((ValuedType) paramType).valueAsInt().equals(1)) {

			if (!paramType.isCastableToBool())
			    if (!node.hasAttr(NodeAttr.USER_WARNING)) {
				logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
					paramType, BType.INT, i).stringify());
				node.attr(NodeAttr.USER_WARNING, true);
			    }

			dimList.add((IntType) GType.get(paramType.castTo(BType.INT)));
		    } else
			// if input known, output not known (can be lower)
			dimList.add((IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i)));
		}

		// delete all the final dimension equals to one
		int lastNonOnePosition = 1;
		for (int j = 0; j < dimList.size(); j++) {
		    if (j > lastNonOnePosition && (!dimList.get(j).hasIntValue() || dimList.get(j).valueAsInt() != 1)) {
			lastNonOnePosition = j;
		    }
		}

		Iterator<IntType> listIterator = dimList.iterator();
		int pos = 0;
		while (listIterator.hasNext()) {
		    listIterator.next();
		    if (pos > lastNonOnePosition) {
			listIterator.remove();
		    }
		    pos++;
		}

		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), basicType,
			dimList.toArray(new IntType[dimList.size()])) };
	    } else {
		return new GType[] { GType.get(BType.UNDEFINED) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateSortReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.isEmpty())
		return new GType[] { GType.get(BType.UNDEFINED) };
	    GType inType = paramTypes.get(0);
	    if (inType.equals(BType.UNDEFINED))
		return new GType[] { GType.get(BType.UNDEFINED) };
	    else if (inType.equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (!inType.isCastableToMatrix())
		return new GType[] { GType.get(BType.UNDEFINED) };

	    DimensionType dtype = (DimensionType) inType;
	    IntType[] newDims = new IntType[dtype.dims().length];
	    AASTNode assignNode = null;
	    if (node.parentExists(NodeType.GASSIGN))
		assignNode = node.parent(NodeType.ASSIGN);
	    else if (node.parentExists(NodeType.ASSIGN))
		assignNode = node.parent(NodeType.ASSIGN);

	    if (assignNode != null && assignNode.child(NodeType.LHS).childExists(NodeType.MATRIX)) {
		// form [unique_values, unique_values_matrix_indexes] = unique(...)
		// return a struct
		String sname = Symbols.getSymbolFromType("sort", BType.STRUCT);
		StructType uStruct = (StructType) GType.get(BType.STRUCT).name(sname);
		List<AASTNode> vectors = assignNode.child(NodeType.LHS).childs(NodeType.MATRIX);
		if (vectors.size() > 1)
		    return new GType[] { GType.get(BType.UNDEFINED) };
		AASTNode vector = vectors.get(0);
		if (vector.childs().size() == 1) {
		    int i = 0;
		    for (IntType d : dtype.dims()) {
			if (d.hasValue() && d.value().equals(1))
			    newDims[i] = (IntType) GType.get(dtype.dims()[i]);
			else
			    // if input known, output not known (can be lower)
			    newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i + 1));

			i++;
		    }

		    return new GType[] {
			    GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(dtype.of()), newDims) };
		}
		if (vector.childs().size() > 2)
		    return new GType[] { GType.get(BType.UNDEFINED) };

		AASTNode values = vector.childs().get(0);
		AASTNode mappings = vector.childs().get(1);
		String valuesName = values.name();
		String mappingsName = mappings.name();
		int i = 0;
		for (IntType d : dtype.dims()) {
		    if (d.hasValue())
			newDims[i] = (IntType) GType.get(d);
		    else
			newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(valuesName, i + 1));

		    i++;
		}

		GType valuesMatrix = GType.get(BType.MATRIX, valuesName, GType.get(dtype.of()), newDims);
		List<GType> types = new LinkedList<GType>();
		types.add(valuesMatrix);
		uStruct.addField(types, "values");
		// copy to avoid having the same instances on values and mappings
		i = 0;
		for (IntType d : dtype.dims()) {
		    if (d.hasValue())
			newDims[i] = (IntType) GType.get(d);
		    else
			newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(mappingsName, i + 1));

		    i++;
		}

		GType mappingMatrix = GType.get(BType.MATRIX, mappingsName, GType.get(BType.INT), newDims);
		types = new LinkedList<GType>();
		types.add(mappingMatrix);
		uStruct.addField(types, "mappings");

		return new GType[] { uStruct };
	    } else {
		int i = 0;
		for (IntType d : dtype.dims()) {
		    if (d.hasValue())
			newDims[i] = (IntType) GType.get(d);
		    else
			newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i + 1));

		    i++;
		}

		return new GType[] {
			GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(dtype.of()), newDims) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateUniqueReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.isEmpty())
		return new GType[] { GType.get(BType.UNDEFINED) };
	    GType paramType = paramTypes.get(0);
	    if (paramType.equals(BType.UNDEFINED))
		return new GType[] { GType.get(BType.UNDEFINED) };
	    else if (paramType.equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (paramType.isCastableToScalar())
		return new GType[] { GType.get(paramType) };
	    else if (paramType.isCastableToMatrix()) {
		DimensionType dtype = (DimensionType) paramType;
		IntType[] newDims = new IntType[dtype.dims().length];

		AASTNode assignNode = null;
		if (node.parentExists(NodeType.GASSIGN))
		    assignNode = node.parent(NodeType.ASSIGN);
		else if (node.parentExists(NodeType.ASSIGN))
		    assignNode = node.parent(NodeType.ASSIGN);

		if (assignNode != null && assignNode.child(NodeType.LHS).childExists(NodeType.MATRIX)) {
		    // form [unique_values, unique_values_matrix_indexes] = unique(...)
		    // return a struct
		    String sname = Symbols.getSymbolFromType("unique", BType.STRUCT);
		    StructType uStruct = (StructType) GType.get(BType.STRUCT).name(sname);
		    List<AASTNode> vectors = assignNode.child(NodeType.LHS).childs(NodeType.MATRIX);
		    if (vectors.size() > 1)
			return new GType[] { GType.get(BType.UNDEFINED) };
		    AASTNode vector = vectors.get(0);
		    if (vector.childs().size() == 1) {
			int i = 0;
			for (IntType d : dtype.dims()) {
			    if (d.hasValue() && d.value().equals(1))
				newDims[i] = (IntType) GType.get(dtype.dims()[i]);
			    else
				// if input known, output not known (can be lower)
				newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i + 1));

			    i++;
			}

			return new GType[] {
				GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(dtype.of()), newDims) };
		    }
		    if (vector.childs().size() > 2)
			return new GType[] { GType.get(BType.UNDEFINED) };

		    AASTNode values = vector.childs().get(0);
		    AASTNode mappings = vector.childs().get(1);
		    String valuesName = values.name();
		    String mappingsName = mappings.name();
		    int i = 0;
		    for (IntType d : dtype.dims()) {
			if (d.hasValue() && d.value().equals(1))
			    newDims[i] = (IntType) GType.get(dtype.dims()[i]);
			else
			    // if input known, output not known (can be lower)
			    newDims[i] = (IntType) GType.get(BType.INT)
				    .name(TypeUtils.matrixDimName(valuesName, i + 1));

			i++;
		    }

		    GType valuesMatrix = GType.get(BType.MATRIX, valuesName, GType.get(dtype.of()), newDims);
		    List<GType> l = new LinkedList<GType>();
		    l.add(valuesMatrix);
		    uStruct.addField(l, "values");

		    // mappings matrix is always n x 1
		    newDims[0] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(mappingsName, 1));
		    for (i = 1; i < dtype.dims().length; ++i)
			newDims[i] = (IntType) GType.get(BType.INT, 1);

		    GType mappingMatrix = GType.get(BType.MATRIX, mappingsName, GType.get(BType.INT), newDims);
		    l = new LinkedList<GType>();
		    l.add(mappingMatrix);
		    uStruct.addField(l, "mappings");

		    return new GType[] { uStruct };
		} else {
		    int i = 0;
		    for (IntType d : dtype.dims()) {
			if (d.hasValue() && d.value().equals(1))
			    newDims[i] = (IntType) GType.get(dtype.dims()[i]);
			else
			    // if input known, output not known (can be lower)
			    newDims[i] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, i + 1));

			i++;
		    }

		    return new GType[] {
			    GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(dtype.of()), newDims) };
		}
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateSetDiffReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 2) {
		GType startSet = paramTypes.get(0);
		GType diffSet = paramTypes.get(1);
		if (startSet == null || startSet.equals(BType.UNKNOWN) || diffSet == null
			|| diffSet.equals(BType.UNKNOWN))
		    return new GType[] { GType.get(BType.UNKNOWN) };
		else if (startSet.isCastableToMatrix() && diffSet.isCastableToMatrix()) {
		    DimensionType sdtype = (DimensionType) startSet;
		    DimensionType ddtype = (DimensionType) diffSet;

		    AASTNode assignNode = null;
		    if (node.parentExists(NodeType.GASSIGN))
			assignNode = node.parent(NodeType.ASSIGN);
		    else if (node.parentExists(NodeType.ASSIGN))
			assignNode = node.parent(NodeType.ASSIGN);

		    if (assignNode != null && assignNode.child(NodeType.LHS).childExists(NodeType.MATRIX)) {
			// form [v, m] = setdiff(...)
			// return a struct
			String sname = Symbols.getSymbolFromType("setdiff", BType.STRUCT);
			StructType uStruct = (StructType) GType.get(BType.STRUCT).name(sname);
			List<AASTNode> vector = assignNode.child(NodeType.LHS).childs(NodeType.MATRIX).get(0).childs();
			if (vector.size() != 2)
			    return new GType[] { GType.get(BType.UNDEFINED) };

			AASTNode values = vector.get(0);
			AASTNode mappings = vector.get(1);
			String valuesName = values.name();
			String mappingsName = mappings.name();
			int i = 0;
			IntType[] newDims = new IntType[sdtype.dims().length];
			for (IntType d : sdtype.dims()) {
			    newDims[i] = (IntType) GType.get(BType.INT)
				    .name(TypeUtils.matrixDimName(valuesName, i + 1));

			    i++;
			}

			GType valuesMatrix = GType.get(BType.MATRIX, valuesName, GType.get(sdtype.of()), newDims);
			List<GType> l = new LinkedList<GType>();
			l.add(valuesMatrix);
			uStruct.addField(l, "values");
			// copy to avoid having the same instances on values and mappings
			i = 0;
			for (IntType d : sdtype.dims()) {
			    newDims[i] = (IntType) GType.get(BType.INT)
				    .name(TypeUtils.matrixDimName(mappingsName, i + 1));

			    i++;
			}

			GType mappingMatrix = GType.get(BType.MATRIX, mappingsName, GType.get(BType.INT), newDims);
			l = new LinkedList<GType>();
			l.add(mappingMatrix);
			uStruct.addField(l, "mappings");

			return new GType[] { uStruct };
		    } else {
			// return 1xn or nx1 depending on first parameter
			IntType[] dims = sdtype.dims();
			if (dims[0].hasValue() && dims[0].valueAsInt().equals(1))
			    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node),
				    GType.get(sdtype.of()), new IntType[] { (IntType) GType.get(BType.INT, 1),
					    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2)) }) };
			else
			    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node),
				    GType.get(sdtype.of()),
				    new IntType[] {
					    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1)),
					    (IntType) GType.get(BType.INT, 1) }) };
		    }

		} else
		    return new GType[] { GType.get(BType.UNDEFINED) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateSizeReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() > 0) {
		GType paramType = paramTypes.get(0);
		IntType dimNumber = null;
		if (paramTypes.size() > 1)
		    if (paramTypes.get(1).isCastableToInt()) {
			dimNumber = (IntType) paramTypes.get(1).castTo(BType.INT);
			if (!dimNumber.hasIntValue()) {
			    dimNumber = null;
			    if (!node.hasAttr(NodeAttr.USER_WARNING)) {
				logger.warn(
					new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_VALUE_NEEDED_IGNORED,
						node, 2).stringify());
				node.attr(NodeAttr.USER_WARNING, true);
			    }
			}

		    } else if (!node.hasAttr(NodeAttr.USER_WARNING)) {
			logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_UNSUPPORTED_IGNORED,
				node, paramTypes.get(1), BType.INT, 1).stringify());
			node.attr(NodeAttr.USER_WARNING, true);
		    }
		if (paramType.equals(BType.UNKNOWN))
		    return new GType[] { GType.get(BType.UNKNOWN) };
		else if (paramType.isCastableToMatrix()) {
		    DimensionType dtype = (DimensionType) paramTypes.get(0);
		    IntType[] dims = dtype.dims();
		    if (dims == null)
			return new GType[] { GType.get(BType.MATRIX, "size_out", GType.get(BType.INT)) };

		    AASTNode assignNode = null;
		    if (node.parentExists(NodeType.GASSIGN))
			assignNode = node.parent(NodeType.ASSIGN);
		    else if (node.parentExists(NodeType.ASSIGN))
			assignNode = node.parent(NodeType.ASSIGN);

		    if (dimNumber == null)
			if (assignNode != null && assignNode.child(NodeType.LHS).childExists(NodeType.MATRIX)) {
			    // form [xval, yval, ...] = unique(...)
			    // return a struct
			    String sname = Symbols.getSymbolFromType("size", BType.STRUCT);
			    StructType uStruct = (StructType) GType.get(BType.STRUCT).name(sname);
			    List<AASTNode> vectors = assignNode.child(NodeType.LHS).childs(NodeType.MATRIX);
			    if (vectors.size() != 1)
				return new GType[] { GType.get(BType.UNDEFINED,
					"Expected row-vector on the left side of assignment.") };
			    AASTNode vector = vectors.get(0);

			    if (dtype.dims().length != vector.childs().size())
				// EX. rhs dimensions are more than the vector outputs
				return new GType[] { GType.get(BType.UNDEFINED, "" + dtype.dims().length
					+ "-dimensional matrix assigned to " + vector.childs().size() + " variables") };

			    for (int n = 0; n < vector.childs().size(); ++n) {
				AASTNode outNode = TypeUtils.getIDNode(vector.childs().get(n));
				IntType dim = dtype.dims()[n];
				String name;
				if (outNode.type().equals(NodeType.VOID))
				    // skip ~
				    name = "dim" + (n + 1);
				else
				    name = outNode.name();
				// fill ustruct
				List<GType> l = new LinkedList<GType>();
				l.add(GType.get(dim));
				uStruct.addField(l, name);
			    }
			    return new GType[] { uStruct };
			} else
			    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node),
				    GType.get(BType.INT),
				    new IntType[] {
					    (IntType) GType.get(BType.INT, 1).name(TypeUtils.matrixDimName(node, 1)),
					    (IntType) GType.get(BType.INT, dims.length)
						    .name(TypeUtils.matrixDimName(node, 2)) }) };
		    else {
			// dimNumber contains the dimension number we should return back
			Integer dimn = dimNumber.valueAsInt();
			return new GType[] { GType.get(dims[dimn - 1]) };
		    }

		} else
		    return new GType[] { GType.get(BType.UNDEFINED) };
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateEigReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {

	    if (paramTypes.isEmpty() || !paramTypes.get(0).isCastableToMatrix())
		return new GType[] { GType.get(BType.UNDEFINED) };

	    DimensionType inType = (DimensionType) paramTypes.get(0);
	    IntType[] dims = inType.dims();
	    if (dims.length > 2) {
		logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
			paramTypes.get(0), "2D matrix", 0).stringify());
		node.attr(NodeAttr.USER_WARNING, true);
		return new GType[] { GType.get(BType.UNDEFINED) };
	    }
	    IntType row = dims[0];
	    IntType col = dims[1];
	    if (row.hasIntValue() && col.hasIntValue() && !row.valueAsInt().equals(col.valueAsInt())) {
		logger.warn(new TypeException(ErrorMessage.WARN_CORE_FUNCTION_PARAM_TYPE_MISMATCH, node,
			paramTypes.get(0), "Square matrix").stringify());
		node.attr(NodeAttr.USER_WARNING, true);
		return new GType[] { GType.get(BType.UNDEFINED) };
	    }

	    AASTNode assignNode = null;
	    if (node.parentExists(NodeType.GASSIGN))
		assignNode = node.parent(NodeType.ASSIGN);
	    else if (node.parentExists(NodeType.ASSIGN))
		assignNode = node.parent(NodeType.ASSIGN);

	    if (assignNode != null && assignNode.child(NodeType.LHS).childExists(NodeType.MATRIX)) {
		// form [xval, yval, ...] = eig(...)
		// return eigenvalues and eigenvectors, see matlab eig doc
		String name = Symbols.getSymbolFromType("eig", BType.STRUCT);
		StructType uStruct = (StructType) GType.get(BType.STRUCT).name(name);
		List<AASTNode> vectors = assignNode.child(NodeType.LHS).childs(NodeType.MATRIX);
		AASTNode vector = vectors.get(0);

		if (vector.childs().size() == 2) {
		    // first matrix of eigenvectors (one per column)
		    // second diagonal matrix of eigenvalues so that A*V = V*D
		    AASTNode eigenvectors = vector.childs().get(0);
		    AASTNode eigenvalues = vector.childs().get(1);
		    String eigenvectorName = eigenvectors.name();
		    String eigenvalueName = eigenvalues.name();

		    // matrix of vectors (col-wise)
		    GType eigenVectorType = GType.get(BType.MATRIX, eigenvectorName, GType.get(BType.SCALAR),
			    new IntType[] { (IntType) GType.get(row), (IntType) GType.get(col) });

		    List<GType> l = new LinkedList<GType>();
		    l.add(eigenVectorType);

		    uStruct.addField(l, "eigenvectors");
		    // diagonal matrix of eigenvalues
		    GType eigenValueType = GType.get(BType.MATRIX, eigenvalueName, GType.get(BType.SCALAR),
			    new IntType[] { (IntType) GType.get(row), (IntType) GType.get(col) });
		    l = new LinkedList<GType>();
		    l.add(eigenValueType);
		    uStruct.addField(l, "eigenvalues");
		    return new GType[] { uStruct };
		} else
		    return new GType[] { GType.get(BType.UNDEFINED) };
	    } else {
		// simple assign, just return eigenvalues
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR),
			new IntType[] { (IntType) GType.get(row), (IntType) GType.get(BType.INT, 1) }) };

	    }

	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateSqueezeReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {

	    if (paramTypes.isEmpty())
		return new GType[] { GType.get(BType.UNDEFINED) };
	    else if (paramTypes.get(0).equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (!paramTypes.get(0).isCastableToMatrix())
		return new GType[] { GType.get(BType.UNDEFINED) };

	    // is a matrix here
	    DimensionType dtype = (DimensionType) paramTypes.get(0);
	    if (dtype.equals(BType.MATRIX_ACCESS_SLICE))
		// return itself
		return new GType[] { GType.get(dtype) };

	    // real matrix
	    IntType[] realDims = TypeUtils.realDims(dtype.dims());
	    return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(dtype.of()), realDims) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> csvRead() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    return new GType[] { GType
		    .get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR),
			    new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1)),
				    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2)) })
		    .setInput(null) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updatInterp() {
	// updater for return type based on actual parameters
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    GType xs = paramTypes.get(2);
	    GType ys = paramTypes.get(1);
	    if (ys == null || GType.get(BType.UNKNOWN).equals(ys))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (ys.equals(BType.SCALAR))
		return new GType[] { GType.get(BType.SCALAR) };
	    else if (ys.equals(BType.MATRIX)) {
		IntType[] ysDims = TypeUtils.realDims(((DimsType) ys).dims());
		IntType[] xsDims = null;
		int dims_to_remove = -1;
		if (xs.equals(BType.SCALAR))
		    xsDims = new IntType[] { (IntType) GType.get(BType.INT, 1).name(TypeUtils.matrixDimName(node, 1)) };
		else
		    xsDims = TypeUtils.realDims(((DimsType) xs).dims());

		dims_to_remove = xsDims.length;

		if (ysDims.length <= dims_to_remove)
		    return new GType[] { GType.get(BType.SCALAR) };
		else {
		    IntType[] dims = new IntType[ysDims.length];
		    int i = 0;
		    for (; i < xsDims.length; ++i)
			dims[i] = (IntType) GType.get(xsDims[i]);
		    for (; i < ysDims.length; ++i)
			dims[i] = (IntType) GType.get(ysDims[i]);
		    return new GType[] {
			    GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR), dims) };
		}

	    } else {
		return new GType[] { GType.get(BType.UNKNOWN) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updatInterpParams() {
	// TODO Auto-generated method stub
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() < 3)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    boolean has_extra_params = paramTypes.size() > 3;
	    GType[] extra_params = null;
	    if (has_extra_params)
		extra_params = Arrays.copyOfRange(paramTypes.toArray(new GType[paramTypes.size()]), 3,
			paramTypes.size());

	    GType x = paramTypes.get(0);
	    GType ys = paramTypes.get(1);
	    GType xq = paramTypes.get(2);
	    GType xUpd, xqUpd;

	    if (x == null || GType.get(BType.UNKNOWN).equals(x))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (x.equals(BType.MATRIX)) {
		IntType[] xDims = TypeUtils.realDims(((DimsType) x).dims());
		xUpd = GType.get(BType.MATRIX, x.name(), GType.get(BType.SCALAR), xDims);
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };

	    if (xq == null || GType.get(BType.UNKNOWN).equals(xq))
		return new GType[] { GType.get(BType.UNKNOWN) };
	    else if (xq.equals(BType.MATRIX)) {
		IntType[] xqDims = TypeUtils.realDims(((DimsType) xq).dims());
		xqUpd = GType.get(BType.MATRIX, xq.name(), GType.get(BType.SCALAR), xqDims);
	    } else
		return new GType[] { GType.get(BType.UNDEFINED) };

	    GType[] ret;
	    // @function timespan initial conditions
	    ret = new GType[] { xUpd, ys, xqUpd };
	    if (has_extra_params)
		ret = ArrayUtils.concatenate(ret, extra_params);
	    return ret;
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateLenReturnType() {
	// TODO Auto-generated method stub
	return (AASTNode node, List<GType> paramTypes) -> {
	    if (paramTypes.size() == 0)
		// doesn't support execution on 0 parameters
		return new GType[] { GType.get(BType.UNKNOWN) };

	    GType inputType = paramTypes.get(0);
	    if (inputType.equals(BType.UNKNOWN))
		return new GType[] { inputType };

	    if (inputType.equals(BType.STRING)) {
		StringType strType = ((StringType) inputType);
		if (strType.hasValue())
		    return new GType[] { GType.get(BType.INT, strType.valueAsString().length()) };
		else
		    return new GType[] { GType.get(BType.INT).name(Symbols.getSymbolFromType("len", BType.INT)) };
	    }

	    if (inputType.isCastableToScalar()) {
		node.compilationUnit()
			.addWarning(new CastException(ErrorMessage.WARN_USER_IMPLICITLY_PROMOTING_SCALAR_VARIABLE, node,
				node.child(NodeType.ID).code(),
				node.child(NodeType.FUNCTION_PARAMETER_LIST).childs().get(0).code(), inputType));
		return new GType[] { GType.get(BType.INT, 1) };
	    }
	    DimensionType dtype = (DimensionType) inputType;
	    IntType[] dims = dtype.dims();
	    int idim = -1;
	    if (dims != null)
		for (IntType dim : dims)
		    if (dim.hasValue() && dim.valueAsInt() > idim)
			idim = dim.valueAsInt();
		    else if (!dim.hasValue()) {
			// if after having a first candidate for max dim
			// I find another dimension without known value
			// the return value is an int without known value
			idim = -2;
			break;
		    }
	    if (idim < 0)
		return new GType[] { GType.get(BType.INT).name(Symbols.getSymbolFromType("len", BType.INT)) };
	    else
		return new GType[] { GType.get(BType.INT, idim) };
	};
    }

}
