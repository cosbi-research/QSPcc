package eu.cosbi.qspcc.frontend.corefunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.FunctionType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

public enum M2RFunction implements IFunction {

    // these comes from matlab
    // load of a file
    LOAD(
	    "load",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRUCT).name("INPUT").setKnown().setInput(null),
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		// change all UNDEFINED from now on to input struct
		node.compilationUnit().program()
			.undefinedType(M2CUpdaters.updateUndefinedAfterLoadSymbol(node.symbol()));

		NodeType[] assigntypes = new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN };
		if (node.parentExists(assigntypes)) {
		    AASTNode assign = node.parent(assigntypes);
		    AASTNode lhsNode = TypeUtils.getIDNode(assign.childs(NodeType.LHS).get(0));
		    return new GType[] { GType.get(BType.STRUCT).name(lhsNode.symbol()).setInput(node.symbol()) };
		} else
		    return new GType[] { GType.get(BType.STRUCT).name("INPUT").setInput(node.symbol()) };
	    },
	    // param types
	    GType.get(BType.STRING)),
    // read csv file
    CSVREAD(
	    "csvread",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "csv_out", GType.get(BType.SCALAR),
		    new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName("csv_out", 1)),
			    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName("csv_out", 2)) })
		    .setInput("load_x"),
	    (

		    AASTNode node, List<GType> paramTypes) -> {
		return new GType[] { GType
			.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR),
				new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1)),
					(IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2)) })
			.setInput(node.symbol()) };
	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

    // save to file
    SAVE(
	    "save",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING),
	    GType.get(BType.UNKNOWN)),
    // save to file
    CSVWRITE(
	    "csvwrite",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING),
	    GType.get(BType.UNKNOWN)),
    // display something to standard output
    DISP(
	    "disp",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // is inf (math)
    ISINF(
	    "isinf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL)),
    // is nan (math)
    ISNAN(
	    "isnan",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL)),
    // string comparison
    STRCMP(
	    "strcmp",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL),
	    // param types
	    GType.get(BType.STRING),
	    GType.get(BType.STRING)),
    // string concat
    STRCAT(
	    "strcat",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRING),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // sprintf
    SPRINTF(
	    "sprintf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRING),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // sprintf
    FPRINTF(
	    "fprintf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // addpath
    ADDPATH(
	    "addpath",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING)),
    // find
    FIND(
	    "find",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "find_out", GType.get(BType.INT), new IntType[] { (IntType) GType.get(BType.INT) }),
	    // update return type based on actual param types
	    M2CUpdaters.updateFindReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // addpath
    FILEREAD(
	    "fileread",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRING),
	    // param types
	    GType.get(BType.STRING)),
    // length of string
    LENGTH(
	    "length",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    // param types
	    GType.get(BType.STRING)),
    FLOOR(
	    "floor",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.INT)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX, GType.get(BType.MATRIX, "floor_out", GType.get(BType.INT))) },
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.isEmpty())
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType ptype = paramTypes.get(0);
		if (ptype.isCastableToScalar())
		    // copy constructor
		    return new GType[] { GType.get(BType.INT) };
		else {
		    DimensionType dtype = (DimensionType) GType.get(ptype.type(), TypeUtils.matrixName(node),
			    GType.get(BType.INT), ((DimensionType) ptype).dims());
		    return new GType[] { dtype };
		}

	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

    // ceil of scalar
    CEIL(
	    "ceil",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.INT)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX, GType.get(BType.MATRIX, "ceil_in", GType.get(BType.INT))) },
	    (AASTNode node, List<GType> paramTypes) -> {
		GType ptype = paramTypes.get(0);
		if (ptype.isCastableToScalar())
		    // copy constructor
		    return new GType[] { GType.get(BType.INT) };
		else {
		    DimensionType dtype = (DimensionType) GType.get(ptype.type(), TypeUtils.matrixName(node),
			    GType.get(BType.INT), ((DimensionType) ptype).dims());
		    return new GType[] { dtype };
		}

	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

    // log_e(x)
    LOG(
	    "log",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX, GType.get(BType.MATRIX, "log_in", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // linearly spaced sequence
    LINSPACE(
	    "linspace",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.MATRIX, "linspace_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateLinspaceReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // linearly spaced sequence
    LOGSPACE(
	    "logspace",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.MATRIX, "logspace_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateLinspaceReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // remainder after integer division
    MOD(
	    "mod",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // return type
	    GType.get(BType.INT),
	    // param types
	    GType.get(BType.INT),
	    GType.get(BType.INT)),
    // size of matrix
    // size of matrix
    SIZE(
	    "size",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "size_out", GType.get(BType.INT)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateSizeReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    FACTORIAL(
	    "factorial",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    GType.get(BType.INT)),

    // size of matrix
    IS_EMPTY(
	    "isempty",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // number of elements in a matrix
    NUMEL(
	    "numel",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    // updater for return type based on actual parameters
	    (AASTNode node, List<GType> paramTypes) -> {
		GType inputType = paramTypes.get(0);
		if (inputType.equals(BType.UNKNOWN))
		    return new GType[] { inputType };
		if (inputType.isCastableToScalar())
		    return new GType[] { GType.get(BType.INT, 1) };
		DimensionType dtype = (DimensionType) inputType;
		IntType[] dims = dtype.dims();
		int idim = 1;
		if (dims != null)
		    for (IntType dim : dims)
			if (dim.hasValue())
			    idim *= dim.valueAsInt();
			else
			    idim = -1;
		else
		    idim = -1;
		if (idim < 0)
		    return new GType[] { GType.get(BType.INT).name(Symbols.getSymbolFromType("numel", BType.INT)) };
		else
		    return new GType[] { GType.get(BType.INT, idim) };
	    },
	    // param types
	    GType.get(BType.MATRIX, "numel_in", GType.get(BType.UNKNOWN))),
    // matrix filled with zeros
    ZEROS(
	    "zeros",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "zeros_out", GType.get(BType.INT)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateMatrixInitializerReturnType(GType.get(BType.INT)),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // matrix filled with ones
    ONES(
	    "ones",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "ones_out", GType.get(BType.INT)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateMatrixInitializerReturnType(GType.get(BType.INT)),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // matrix filled with NAN
    NAN(
	    "nan",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "nan_out", GType.get(BType.SCALAR)),
	    // return type updater
	    M2CUpdaters.updateMatrixInitializerReturnType(GType.get(BType.SCALAR)),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // gaussian random number generator
    RANDN(
	    "randn",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "randn_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // uniform random number generator
    RAND(
	    "rand",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "rand_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // interpolation
    INTERP(
	    "interp1",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // param types
	    GType.get(BType.MATRIX, "interp1_in1", GType.get(BType.SCALAR)),
	    GType.get(BType.MATRIX, "interp1_in2", GType.get(BType.SCALAR)),
	    GType.get(BType.SCALAR),
	    GType.get(BType.STRING)),
    // sum of matrix elements
    SUM(
	    "sum",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // param types
	    GType.get(BType.MATRIX, "sum_out", GType.get(BType.SCALAR))),
    // maximum of scalar elements
    MAX(
	    "max",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // return type
	    GType.get(BType.SCALAR),
	    // update return type based on actual param types
	    M2CUpdaters.updateStatisticalReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    MIN(
	    "min",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // return type
	    GType.get(BType.SCALAR),
	    // update return type based on actual param types
	    M2CUpdaters.updateStatisticalReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // setting ODE parameters
    ODESET(
	    "odeset",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRUCT).name("odeset.x").setKnown().setInput(null),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // 23 stiff (ODEFUN, TSPAN, Y0)
    ODE23S(
	    "ode23s",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "ode23s_out", GType.get(BType.SCALAR)),
	    // update return type based on actual param types
	    M2CUpdaters.updateOdeReturnType(),
	    // update params types based on actual param types
	    M2RUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR),
			    GType.get(BType.MATRIX, "anon_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "anon_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "ode23s_tspan", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "ode23s_y0", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("odeset.x").setKnown()), // 45
    // stiff

    ODE45S(
	    "ode45s",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "ode45s_out", GType.get(BType.SCALAR)),
	    // update return type based on actual param types
	    M2CUpdaters.updateOdeReturnType(),
	    // update params types based on actual param types
	    M2RUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR),
			    GType.get(BType.MATRIX, "anon_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "anon_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "ode45s_tspan", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "ode45s_y0", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("odeset.x").setKnown()), // 45
    // stiff

    ODE15S(
	    "ode15s",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "ode15s_out", GType.get(BType.SCALAR)),
	    // update return type based on actual param types
	    M2CUpdaters.updateOdeReturnType(),
	    // update params types based on actual param types
	    M2RUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR),
			    GType.get(BType.MATRIX, "anon_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "anon_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "ode15s_tspan", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "ode15s_y0", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("odeset.x").setKnown()), // 45
    // stiff

    ODE23(
	    "ode23",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "ode23_out", GType.get(BType.SCALAR)),
	    // update return type based on actual param types
	    M2CUpdaters.updateOdeReturnType(),
	    // update params types based on actual param types
	    M2RUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR),
			    GType.get(BType.MATRIX, "anon_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "anon_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "ode23_tspan", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "ode23_y0", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("odeset.x").setKnown()), // 45
    // 45 non stiff

    ODE45(
	    "ode45",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "ode45_out", GType.get(BType.SCALAR)),
	    // update return type based on actual param types
	    M2CUpdaters.updateOdeReturnType(),
	    // update params types based on actual param types
	    M2RUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR),
			    GType.get(BType.MATRIX, "anon_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "anon_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "ode45_in_tspan", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "ode45_in_y0", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("odeset.x").setKnown()), // 45
    SIN(
	    "sin",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "sin_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    ABS(
	    "abs",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "abs_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(true),
	    // param types
	    GType.get(BType.UNKNOWN)),

    SIGN(
	    "sign",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "sign_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(true),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // cos (math)
    COS(
	    "cos",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "cos_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // tan (math)
    TAN(
	    "tan",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "tan_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    ATAN2(
	    "atan2",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "atan2_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // e^x (math)
    EXP(
	    "exp",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "exp_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    POWER(
	    "power",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "pow_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(true),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // log_e(x)

    // log_10(x)
    LOG10(
	    "log10",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "log10_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),
    SQRT(
	    "sqrt",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.SCALAR),
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // returns the type of the variable
    CLASS(
	    "class",
	    FunctionType.ND,
	    // return type
	    GType.get(BType.TYPE, GType.get(BType.UNKNOWN)),
	    // return type updater
	    (AASTNode node, List<GType> paramTypes) -> {
		return new GType[] { GType.get(BType.TYPE, paramTypes.get(0)) };
	    },
	    // param types
	    GType.get(BType.UNKNOWN)),
    // timing functions
    TIC(
	    "tic",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.SCALAR)),
    // end time
    TOC(
	    "toc",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.SCALAR)),
    // TRUE CONSTANT
    TRUE(
	    "true",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.BOOL, Boolean.TRUE)),
    // FALSE CONSTANT
    FALSE(
	    "false",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.BOOL, Boolean.FALSE)),
    // constants
    PI(
	    "pi",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.SCALAR, Math.PI)),
    INF(
	    "inf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),

    // Hilbert matrix of order n
    HILBERT(
	    "hilb",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.MATRIX, "hilbmatrix", GType.get(BType.SCALAR),
		    new IntType[] { (IntType) GType.get(BType.INT), (IntType) GType.get(BType.INT) }),
	    // param types
	    GType.get(BType.INT)),

    // LU factorization of a matrix
    LU(
	    "lu",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.STRUCT).name("LU_FACTORIZATION").setInput("lu_x"),
	    // param types
	    GType.get(BType.MATRIX, "lumatrix", GType.get(BType.SCALAR),
		    new IntType[] { (IntType) GType.get(BType.INT), (IntType) GType.get(BType.INT) })),

    // Schur decompostion of a matrix
    SCHUR(
	    "schur",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.STRUCT).name("SCHUR_DECOMPOSITION").setInput("lu_x"),
	    // param types
	    GType.get(BType.MATRIX, "schurmatrix", GType.get(BType.SCALAR),
		    new IntType[] { (IntType) GType.get(BType.INT), (IntType) GType.get(BType.INT) })),

    // Matrix exponential
    EXPM(
	    "expm",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.MATRIX, "expm_out", GType.get(BType.SCALAR)),
	    // param types
	    GType.get(BType.MATRIX, "expm_in", GType.get(BType.SCALAR))),

    // figure
    FIGURE(
	    "figure",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // figure
    SUBPLOT(
	    "subplot",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.INT)),
    // semilog plot
    SEMILOGY(
	    "semilogy",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.MATRIX, "semilogy_in1", GType.get(BType.SCALAR)),
	    GType.get(BType.MATRIX, "semilogy_in2", GType.get(BType.SCALAR)),
	    GType.get(BType.UNKNOWN)),

    // semilog plot
    PLOT(
	    "plot",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.MATRIX, "plot_in1", GType.get(BType.SCALAR)),
	    GType.get(BType.MATRIX, "plot_in2", GType.get(BType.SCALAR)),
	    GType.get(BType.UNKNOWN)),
    // semilog plot
    AXIS(
	    "axis",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.MATRIX, "axis_in1", GType.get(BType.SCALAR))),
    // semilog plot
    XLABEL(
	    "xlabel",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING)),
    // semilog plot
    YLABEL(
	    "ylabel",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING)),

    LEGEND(
	    "legend",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING)),

    TITLE(
	    "title",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING)),

    BREAK(
	    "break",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    // with a scalar I'll return a scalar
	    GType.get(BType.VOID)),
    NARGIN(
	    "nargin",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.INT));

    private static Logger logger = LogManager.getLogger(M2CFunction.class);
    public static final String CSV_TYPE = "INPUT";
    private String name;
    private FunctionType variable;
    List<GType> paramTypes;
    Map<BType, GType> outTypes;
    int i = 0;
    private BiFunction<AASTNode, List<GType>, GType[]> retUpdater;
    private BiFunction<AASTNode, List<GType>, GType[]> paramsUpdater;

    M2RFunction(String name, FunctionType variable, Object outType, Object... inTypes) {
	init(name, variable, outType, null, null, inTypes);
    }

    M2RFunction(String name, FunctionType variable, Object outType,
	    BiFunction<AASTNode, List<GType>, GType[]> retUpdater, Object... inTypes) {
	init(name, variable, outType, retUpdater, null, inTypes);
    }

    M2RFunction(String name, FunctionType variable, Object outType,
	    BiFunction<AASTNode, List<GType>, GType[]> retUpdater,
	    BiFunction<AASTNode, List<GType>, GType[]> paramsUpdater, Object... inTypes) {
	init(name, variable, outType, retUpdater, paramsUpdater, inTypes);
    }

    // outDimDependsOnInDim: output dimension depends on input dimension
    private void init(String name, FunctionType variable, Object outType,
	    BiFunction<AASTNode, List<GType>, GType[]> retUpdater,
	    BiFunction<AASTNode, List<GType>, GType[]> paramsUpdater, Object... inTypes) {
	this.name = name;
	this.variable = variable;
	this.retUpdater = retUpdater;
	this.paramsUpdater = paramsUpdater;
	assert (outType instanceof GType || outType instanceof Object[]);
	if (inTypes.length > 0)
	    assert (inTypes[0] instanceof GType);
	outTypes = new HashMap<BType, GType>();
	if (outType instanceof GType) {
	    // only one return value!
	    this.outTypes.put(BType.UNKNOWN, (GType) outType);
	} else if (outType instanceof Object[]) {
	    // more return values based on the input params
	    for (Object outTypeEl : (Object[]) outType)
		this.outTypes.put(((Tuple<BType, GType>) outTypeEl).first(),
			((Tuple<BType, GType>) outTypeEl).second());
	}

	paramTypes = new ArrayList<GType>();
	for (Object t : (Object[]) inTypes) {
	    paramTypes.add(((GType) t));
	}

    }

    @Override
    public BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater() {
	if (retUpdater == null)
	    return null;
	else
	    return (AASTNode node, List<GType> p) -> {
		GType[] ret = retUpdater.apply(node, p);
		List<GType> linkedList = new LinkedList<GType>();
		for (int i = 0; i < ret.length; ++i)
		    linkedList.add(ret[i]);
		return linkedList;
	    };
    };

    @Override
    public BiFunction<AASTNode, List<GType>, List<GType>> paramTypesUpdater() {
	if (paramsUpdater == null)
	    return null;
	else
	    return (AASTNode node, List<GType> p) -> {
		GType[] ret = paramsUpdater.apply(node, p);
		List<GType> linkedList = new LinkedList<GType>();
		for (int i = 0; i < ret.length; ++i)
		    linkedList.add(ret[i]);
		return linkedList;
	    };
    };

    @Override
    public FunctionType type() {
	return this.variable;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public List<GType> getParamTypes() {
	return paramTypes;
    }

    @Override
    public Map<BType, GType> getOutType() {
	return outTypes;
    }

    public GType outType() {
	// warn assume it is single
	return outTypes.get(BType.UNKNOWN);
    }

}
