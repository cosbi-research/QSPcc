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
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.FunctionType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.utils.Tuple;

public enum R2CFunction implements IFunction {
    // constants
    NEXT(
	    "next",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.VOID)),
    BREAK(
	    "break",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.VOID)),
    PI(
	    "pi",
	    // is an env variable?
	    FunctionType.VARIABLE,
	    // return type
	    GType.get(BType.SCALAR, Math.PI)),
    RETURN(
	    "return",
	    //is env variable
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // updater for return type based on actual parameters
	    R2CUpdaters.updateFunctionReturnReturnType(),
	    // params
	    GType.get(BType.UNKNOWN)),
    LIST_TO_DATAFRAME(
	    "listToDataFrame",
	    //is env variable
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRUCT).name("MAT").setKnown(),
	    // params
	    GType.get(BType.STRING)),
    DATAFRAME(
	    "data.frame",
	    //is env variable
	    FunctionType.ND,
	    // return type
	    GType.get(BType.STRUCT).name("DATA.FRAME").setKnown(),
	    // params
	    GType.get(BType.UNKNOWN)),
    NAMES(
	    "names",
	    //is env variable
	    FunctionType.ND,
	    // return type
	    GType.get(BType.VOID),
	    // updater for return type based on actual parameters
	    R2CUpdaters.updateFunctionNamesReturnType(),
	    // params
	    GType.get(BType.UNKNOWN)),
    SYS_TIME(
	    "Sys.time",
	    //is env variable
	    FunctionType.ND,
	    // return type
	    GType.get(BType.SCALAR),
	    // params
	    GType.get(BType.VOID));

    private static Logger logger = LogManager.getLogger(R2CFunction.class);
    public static final String CSV_TYPE = "INPUT";
    private String name;
    private FunctionType variable;
    List<GType> paramTypes;
    Map<BType, GType> outTypes;
    int i = 0;
    private BiFunction<AASTNode, List<GType>, GType[]> retUpdater;
    private BiFunction<AASTNode, List<GType>, GType[]> paramsUpdater;

    R2CFunction(String name, FunctionType variable, Object outType, Object... inTypes) {
	init(name, variable, outType, null, null, inTypes);
    }

    R2CFunction(String name, FunctionType variable, Object outType,
	    BiFunction<AASTNode, List<GType>, GType[]> retUpdater, Object... inTypes) {
	init(name, variable, outType, retUpdater, null, inTypes);
    }

    R2CFunction(String name, FunctionType variable, Object outType,
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
