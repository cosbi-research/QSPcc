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

public enum M2CFunction implements IFunction {

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
    READMATRIX(
	    "readmatrix",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "csv_out", GType.get(BType.SCALAR),
		    new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName("csv_out", 1)),
			    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName("csv_out", 2)) })
		    .setInput(null),
	    M2CUpdaters.csvRead(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    CSVREAD(
	    "csvread",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "csv_out", GType.get(BType.SCALAR),
		    new IntType[] { (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName("csv_out", 1)),
			    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName("csv_out", 2)) })
		    .setInput(null),
	    M2CUpdaters.csvRead(),
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
    WRITEMATRIX(
	    "writematrix",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING),
	    GType.get(BType.UNKNOWN)),
    // display something to standard output
    SPRINTF(
	    "sprintf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRING),
	    // param types
	    GType.get(BType.UNKNOWN)),
    FPRINTF(
	    "fprintf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRING),
	    // param types
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
    // display something to standard output
    ERROR(
	    "error",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING)),
    // clearvars
    CLEARVARS(
	    "clearvars",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.STRING),
	    GType.get(BType.UNKNOWN)),
    // is inf (math)
    ISINF(
	    "isinf",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.BOOL)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX, GType.get(BType.BOOL, "isnan_out", GType.get(BType.BOOL))) },
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType ptype = paramTypes.get(0);
		if (ptype.isCastableToScalar())
		    // copy constructor
		    return new GType[] { GType.get(BType.BOOL) };
		else {
		    DimensionType dtype = (DimensionType) GType.get(ptype.type(), TypeUtils.matrixName(node),
			    GType.get(BType.BOOL), ((DimensionType) ptype).dims());
		    return new GType[] { dtype };
		}

	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

    // is nan (math)
    ISNAN(
	    "isnan",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.BOOL)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX, GType.get(BType.BOOL, "isnan_out", GType.get(BType.BOOL))) },
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType ptype = paramTypes.get(0);
		if (ptype.isCastableToScalar())
		    // copy constructor
		    return new GType[] { GType.get(BType.BOOL) };
		else {
		    DimensionType dtype = (DimensionType) GType.get(ptype.type(), TypeUtils.matrixName(node),
			    GType.get(BType.BOOL), ((DimensionType) ptype).dims());
		    return new GType[] { dtype };
		}

	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

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
    // string comparison
    MOD(
	    "mod",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 2) {
		    if (paramTypes.get(0).equals(BType.UNKNOWN) || paramTypes.get(1).equals(BType.UNKNOWN)) {
			return new GType[] { GType.get(BType.UNKNOWN) };
		    }
		    //check if the parameters are integer
		    else if (paramTypes.get(0).isCastableToBool() && paramTypes.get(1).isCastableToBool())
			return new GType[] { GType.get(BType.INT) };
		    else if (paramTypes.get(0).isCastableToScalar() && paramTypes.get(1).isCastableToScalar())
			return new GType[] { GType.get(BType.SCALAR) };
		    else {
			return new GType[] { GType.get(BType.UNDEFINED) };
		    }
		} else
		    return new GType[] { GType.get(BType.UNDEFINED) };
	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

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
    // size of matrix
    SETDIFF(
	    "setdiff",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "setdiff_out", GType.get(BType.INT)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateSetDiffReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    UNION(
	    "union",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "union_out", GType.get(BType.INT)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateSetDiffReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // size of matrix
    IS_EMPTY(
	    "isempty",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // length of string
    LENGTH(
	    "length",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateLenReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // ceil of double
    CEIL(
	    "ceil",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.INT)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX, GType.get(BType.MATRIX, "ceil_out", GType.get(BType.INT))) },
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType ptype = paramTypes.get(0);
		if (ptype.equals(BType.UNKNOWN))
		    return new GType[] { GType.get(BType.UNKNOWN) };
		else if (ptype.equals(BType.UNDEFINED))
		    return new GType[] { GType.get(BType.UNDEFINED) };
		else if (ptype.isCastableToScalar())
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
    ROUND(
	    "round",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType ptype = paramTypes.get(0);
		if (ptype.equals(BType.UNKNOWN))
		    return new GType[] { GType.get(BType.UNKNOWN) };
		else if (ptype.equals(BType.UNDEFINED))
		    return new GType[] { GType.get(BType.UNDEFINED) };
		else if (ptype.isCastableToScalar())
		    // copy constructor
		    return new GType[] { GType.get(BType.INT) };
		else {
		    DimensionType dtype = (DimensionType) GType.get(BType.MATRIX, TypeUtils.matrixName(node),
			    GType.get(BType.INT), ((DimensionType) ptype).dims());
		    return new GType[] { dtype };
		}

	    },
	    // param types
	    GType.get(BType.UNKNOWN)),
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
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType ptype = paramTypes.get(0);
		if (ptype.equals(BType.UNKNOWN))
		    return new GType[] { GType.get(BType.UNKNOWN) };
		else if (ptype.equals(BType.UNDEFINED))
		    return new GType[] { GType.get(BType.UNDEFINED) };
		else if (ptype.isCastableToScalar())
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
    // ==== FUNCTIONS IMPLEMENTED AS EMPTY FUNCTIONS (WITH WARNING IN BACKEND) ====
    // plotting functions
    FIGURE(
	    "figure",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    SUBPLOT(
	    "subplot",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    PLOT(
	    "plot",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    SEMILOGX(
	    "semilogx",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    ERRORBAR(
	    "errorbar",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    LEGEND(
	    "legend",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    TITLE(
	    "title",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    YLABEL(
	    "ylabel",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    XLABEL(
	    "xlabel",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    YLIM(
	    "ylim",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    XLIM(
	    "xlim",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // generic p-norm
    NORM(
	    "norm",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType inputType = paramTypes.get(0);
		if (inputType.equals(BType.UNKNOWN))
		    return new GType[] { inputType };
		else if (inputType.equals(BType.UNDEFINED))
		    return new GType[] { inputType };
		else
		    return new GType[] { GType.get(BType.SCALAR) };
	    },
	    // param types
	    GType.get(BType.UNKNOWN)),

    // sin (math)
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
    TANH(
    	    "tanh",
    	    // is an env variable?
    	    FunctionType.ND,
    	    // return type
    	    new Object[] {
    		    // with a scalar I'll return a scalar
    		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
    		    // with a matrix I'll return a matrix
    		    new Tuple<BType, GType>(BType.MATRIX,
    			    GType.get(BType.MATRIX, "tanh_out", GType.get(BType.SCALAR))) },
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
    LOG(
	    "log",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    new Object[] {
		    // with a scalar I'll return a scalar
		    new Tuple<BType, GType>(BType.SCALAR, GType.get(BType.SCALAR)),
		    // with a matrix I'll return a matrix
		    new Tuple<BType, GType>(BType.MATRIX,
			    GType.get(BType.MATRIX, "log_out", GType.get(BType.SCALAR))) },
	    M2CUpdaters.updateMathReturnType(false),
	    // param types
	    GType.get(BType.UNKNOWN)),

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
    // number of elements in a matrix
    NUMEL(
	    "numel",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    // updater for return type based on actual parameters
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

		GType inputType = paramTypes.get(0);
		if (inputType.equals(BType.UNKNOWN))
		    return new GType[] { inputType };
		else if (inputType.equals(BType.UNDEFINED))
		    return new GType[] { inputType };
		else if (inputType.isCastableToScalar())
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
		    return new GType[] { GType.get(BType.INT).name(Symbols.getSymbolFromType("len", BType.INT)) };
		else
		    return new GType[] { GType.get(BType.INT, idim) };
	    },
	    // param types
	    GType.get(BType.MATRIX, "numel_param", GType.get(BType.UNKNOWN))),

    NUM2STR(
	    "num2str",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.STRING),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // remove useless '1' dimensions
    SQUEEZE(
	    "squeeze",
	    // is an env variable?
	    FunctionType.ND,
	    // return type is always a matrix
	    GType.get(BType.MATRIX, "squeeze_out", GType.get(BType.UNKNOWN)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateSqueezeReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // linearly spaced meshes. like colon operator, but always include start/end
    // linspace(start,end,n) has n points spaced (end-start)/(n-1)
    LINSPACE(
	    "linspace",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "linspace_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateLinspaceReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // logarithmically spaced meshes.
    // python implementation
    // >>> start=1
    // >>> end=5
    // >>> space=(end-start)/(7-1.0)
    // >>> logspace = list(map(lambda x : math.pow(10, start + space*x),
    // range(0,7)))
    //
    // generates logarithmically spaced values in range 10^start,10^end
    LOGSPACE(
	    "logspace",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "logspace_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateLinspaceReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // matrix filled with zeros
    SPARSE(
	    "sparse",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "sparse_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateSparseReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
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
    EYE(
	    "eye",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "eye_out", GType.get(BType.INT)),
	    // return type updater
	    M2CUpdaters.updateMatrixInitializerReturnType(GType.get(BType.INT)),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // TRUE CONSTANT
    TRUE(
	    "true",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL, Boolean.TRUE),
	    // update return type based on actual param types
	    M2CUpdaters.updateMatrixInitializerReturnType(GType.get(BType.BOOL, Boolean.TRUE), true),
	    GType.get(BType.UNKNOWN)),
    // FALSE CONSTANT
    FALSE(
	    "false",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL, Boolean.FALSE),
	    // update return type based on actual param types
	    M2CUpdaters.updateMatrixInitializerReturnType(GType.get(BType.BOOL, Boolean.FALSE), true),
	    GType.get(BType.UNKNOWN)),
    DIAG(
	    "diag",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "diag_out", GType.get(BType.INT)),
	    // return type updater
	    M2CUpdaters.updateDiagReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    TRIU(
	    "triu",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "triu_out", GType.get(BType.INT)),
	    // return type updater
	    M2CUpdaters.updateTriReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    TRIL(
	    "tril",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on input dimensions
	    // FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.MATRIX, "tril_out", GType.get(BType.INT)),
	    // return type updater
	    M2CUpdaters.updateTriReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    UNIQUE(
	    "unique",
	    // env var?
	    FunctionType.ND,
	    GType.get(BType.MATRIX, "unique_out", GType.get(BType.SCALAR)),
	    // ret type updater
	    M2CUpdaters.updateUniqueReturnType(),
	    //param types
	    GType.get(BType.UNKNOWN)),
    SORT(
	    "sort",
	    // env var?
	    FunctionType.ND,
	    GType.get(BType.MATRIX, "sort_out", GType.get(BType.SCALAR)),
	    // ret type updater
	    M2CUpdaters.updateSortReturnType(),
	    //param types
	    GType.get(BType.UNKNOWN)),
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
    // gaussian random number generator
    RANDN(
	    "randn",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    RANDPERM(
	    "randperm",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "randperm_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandPermReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // uniform random number generator
    RAND(
	    "rand",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // uniform random number generator
    RANDI(
	    "randi",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRandiReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // Kronecker product
    KRON(
	    "kron",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateKronReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    REPMAT(
	    "repmat",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateRepmatReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // compute eigenvalues/eigenvectors
    EIG(
	    "eig",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateEigReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // says whether a scalar (or an array) is contained in an array
    IS_MEMBER(
	    "ismember",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.BOOL),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateIsMemberReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // reshape a matrix into another matrix with given dimensions
    RESHAPE(
	    "reshape",
	    // is an env variable?
	    FunctionType.ND,
	    // output dimension depends on parameters
	    GType.get(BType.MATRIX, "reshape_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    M2CUpdaters.updateReshapeReturnType(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // cumulative sum of matrix elements
    CUMSUM(
	    "cumsum",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // output dimension depends on parameters
	    GType.get(BType.MATRIX, "cumsum_out", GType.get(BType.SCALAR)),
	    // updater for return type based on actual parameters
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 1 && paramTypes.get(0).equals(BType.UNKNOWN)) {
		    return new GType[] { GType.get(BType.UNKNOWN) };
		}
		//check if the parameters are integer
		else if (paramTypes.size() == 1 && paramTypes.get(0).isCastableToMatrix()) {
		    String matrixName = TypeUtils.matrixName(node);

		    GType of = GType.get(((DimensionType) paramTypes.get(0)).of());
		    IntType[] dims = ((DimensionType) paramTypes.get(0)).dims();
		    IntType[] newDims = new IntType[dims.length];
		    for (int i = 0; i < dims.length; i++) {
			if (dims[i].hasIntValue()) {
			    newDims[i] = (IntType) GType.get(dims[i]);
			} else {
			    newDims[i] = (IntType) GType.get(BType.INT)
				    .name(TypeUtils.matrixDimName(matrixName, i + 1));
			}
		    }

		    return new GType[] { GType.get(BType.MATRIX, matrixName, of, dims) };
		} else {
		    return new GType[] { GType.get(BType.UNDEFINED) };
		}
	    },
	    // param types
	    GType.get(BType.UNKNOWN)),
    // interpolation
    INTERP(
	    "interp1",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    M2CUpdaters.updatInterp(),
	    // can't update param types in this way yet
	    //M2CUpdaters.updatInterpParams(),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // sum of matrix elements
    SUM(
	    "sum",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // return type
	    GType.get(BType.SCALAR),
	    // update return type based on actual param types
	    M2CUpdaters.updateSumReturnType(1, false),
	    // param types
	    GType.get(BType.UNKNOWN)),
    MEAN(
	    "mean",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // return type
	    GType.get(BType.SCALAR),
	    // update return type based on actual param types
	    M2CUpdaters.updateSumReturnType(1, true),
	    // param types
	    GType.get(BType.UNKNOWN)),
    STD(
	    "std",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // return type
	    GType.get(BType.SCALAR),
	    // update return type based on actual param types
	    M2CUpdaters.updateSumReturnType(2, true),
	    // param types
	    GType.get(BType.UNKNOWN)),
    VAR(
	    "var",
	    // is an env variable?
	    FunctionType.STATISTICAL,
	    // return type
	    GType.get(BType.SCALAR),
	    // update return type based on actual param types
	    M2CUpdaters.updateSumReturnType(2, true),
	    // param types
	    GType.get(BType.UNKNOWN)),
    // minimum of matrices (component-wise)
    // returns a matrix of minimum values
    // the input matrices should have compatible types
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
    // maximum of matrices (component-wise)
    // returns a matrix of maximum values
    // the input matrices should have compatible types
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
    // setting optimize parameters
    OPTIMOPTIONS(
    	    "optimoptions",
    	    // is an env variable?
    	    FunctionType.ND,
    	    // return type
    	    GType.get(BType.STRUCT).name("cmaes_t").setKnown(),
    	    // param types
    	    GType.get(BType.UNKNOWN)),
    LSQNONLIN(
    		"lsqnonlin",
    	    // is an env variable?
    	    FunctionType.ND,
    	    // return type
    	    // return type
    	    GType.get(BType.MATRIX, "ode23s_out", GType.get(BType.SCALAR)),
    	    // update return type based on actual param types
    	    M2CUpdaters.updateLsqNonLinReturnType(),
    	    // update params types based on actual param types
    	    M2CUpdaters.updateLsqNonLinParams(),
    	    // PARAM TYPES
    	    // first parameter is a function that takes  a
    	    // matrix of scalars and return an optimized matrix of scalars
    	    GType.get(BType.FUNCTION,
    		    new GType[] { GType.get(BType.MATRIX, "opt_in", GType.get(BType.SCALAR)) },
    		    new GType[] { GType.get(BType.MATRIX, "opt_out", GType.get(BType.SCALAR)) }),
    	    // second parameter is an initial guess
    	    GType.get(BType.MATRIX, "opt_in0", GType.get(BType.SCALAR)),
    	    // the third parameter is the lowerbound (set to [] to disable)
    	    GType.get(BType.MATRIX, "opt_lb", GType.get(BType.SCALAR)),
    	    // the third parameter is the upperbound (set to [] to disable)
    	    GType.get(BType.MATRIX, "opt_ub", GType.get(BType.SCALAR)),
    	    // fourth parameter is the options struct
    	    GType.get(BType.STRUCT).name("cmaes_t").setKnown() // 45
    		),
    // setting ODE parameters
    ODESET(
	    "odeset",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRUCT).name("INTEGRATION_OPTS").setKnown(),
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
	    M2CUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR), GType.get(BType.MATRIX, "at_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "at_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "tspan_in", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "y0_in", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("INTEGRATION_OPTS").setKnown()), // 45
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
	    M2CUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR), GType.get(BType.MATRIX, "at_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "at_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "tspan_in", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "y0_in", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("INTEGRATION_OPTS").setKnown()), // 45
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
	    M2CUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR), GType.get(BType.MATRIX, "at_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "at_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "tspan_in", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "y0_in", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("INTEGRATION_OPTS").setKnown()), // 45
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
	    M2CUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR), GType.get(BType.MATRIX, "at_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "at_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "tspan_in", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "y0_in", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("INTEGRATION_OPTS").setKnown()), // 45
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
	    M2CUpdaters.updateOdeParams(),
	    // PARAM TYPES
	    // first parameter is a function that takes a scalar and a
	    // matrix of scalars
	    GType.get(BType.FUNCTION,
		    new GType[] { GType.get(BType.SCALAR), GType.get(BType.MATRIX, "at_in", GType.get(BType.SCALAR)) },
		    new GType[] { GType.get(BType.MATRIX, "at_out", GType.get(BType.SCALAR)) }),
	    // second parameter is a timespan
	    GType.get(BType.MATRIX_ACCESS_SLICE, "tspan_in", GType.get(BType.SCALAR)),
	    // third parameter is a matrix of scalars
	    GType.get(BType.MATRIX, "y0_in", GType.get(BType.SCALAR)),
	    // fourth parameter is the options struct
	    GType.get(BType.STRUCT).name("INTEGRATION_OPTS").setKnown()),
    FIND(
	    "find",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.MATRIX, "find_out", GType.get(BType.INT), new IntType[] { (IntType) GType.get(BType.INT) }),
	    // update return type based on actual param types
	    M2CUpdaters.updateFindReturnType(),
	    GType.get(BType.UNKNOWN)),
    FACTORIAL(
	    "factorial",
	    // is an env variable?
	    FunctionType.ND,
	    // return type
	    GType.get(BType.INT),
	    GType.get(BType.INT)),
    // returns the type of the variable
    CLASS(
	    "class",
	    FunctionType.ND,
	    // return type
	    GType.get(BType.TYPE, GType.get(BType.UNKNOWN)),
	    // return type updater
	    (AASTNode node, List<GType> paramTypes) -> {
		if (paramTypes.size() == 0)
		    // doesn't support execution on 0 parameters
		    return new GType[] { GType.get(BType.UNKNOWN) };

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
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // return type updater
	    (AASTNode node, List<GType> paramTypes) -> {
		// execution on 0 parameters -> default
		return new GType[] { GType.get(BType.SCALAR) };
	    },
	    // param types
	    GType.get(BType.SCALAR)),
    // constants
    PI(
	    "pi",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.SCALAR, Math.PI)),
    BREAK(
	    "break",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.VOID)),
    CONTINUE(
	    "continue",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
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

    M2CFunction(String name, FunctionType variable, Object outType, Object... inTypes) {
	init(name, variable, outType, null, null, inTypes);
    }

    M2CFunction(String name, FunctionType variable, Object outType,
	    BiFunction<AASTNode, List<GType>, GType[]> retUpdater, Object... inTypes) {
	init(name, variable, outType, retUpdater, null, inTypes);
    }

    M2CFunction(String name, FunctionType variable, Object outType,
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
