package eu.cosbi.qspcc.frontend.corefunction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.utils.TypeUtils;

public class M2RUpdaters {
    public static BiFunction<AASTNode, List<GType>, GType[]> updateOdeReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    IntType unknownDim1 = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));
	    IntType unknownDim2 = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2));
	    if (paramTypes.get(2).equals(BType.UNKNOWN))
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR),
			new IntType[] { unknownDim1, unknownDim2 }) };
	    // dimension is driven by y0
	    DimensionType y0 = (DimensionType) paramTypes.get(2);
	    IntType[] y0Dims = TypeUtils.realDims(y0.dims());
	    IntType[] newDimensions = new IntType[2];
	    newDimensions[0] = unknownDim1;
	    if (y0Dims == null)
		// y0 dimension not known
		newDimensions[1] = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2));
	    else if (y0Dims.length == 0 && y0.dims()[0].valueAsInt() == 1)
		// y0Dims is empty because is a matrix 1x1 -> matrix Nx2 (1
		// for t and 1 for y)
		newDimensions[1] = (IntType) GType.get(BType.INT, 2).name(TypeUtils.matrixDimName(node, 2));
	    else {
		// y0Dims not empty
		if (y0Dims[0].hasValue())
		    newDimensions[1] = (IntType) GType.get(BType.INT, y0Dims[0].valueAsInt() + 1)
			    .name(TypeUtils.matrixDimName(node, 2));
		else
		    newDimensions[1] = (IntType) GType.get(y0Dims[0]).name(TypeUtils.matrixDimName(node, 2));
	    }

	    return new GType[] {
		    GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.SCALAR), newDimensions) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateOdeParams() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    BiFunction<AASTNode, List<GType>, GType[]> cfun = M2CUpdaters.updateOdeParams();
	    // add FUNCTION_ENV_PARAMS attribute
	    boolean has_extra_params = paramTypes.size() > 4;
	    if (has_extra_params) {
		List<AASTNode> params = node.childs(NodeType.FUNCTION_PARAMETER_LIST);
		AASTNode[] extra_params = Arrays.copyOfRange(params.toArray(new AASTNode[params.size()]), 4,
			params.size());
		// the first parameter is the anonimous function to integrate
		AASTNode anonimousFun = params.get(0);
		// get the child FUNCTION under the Expression
		AASTNode origFunctionNode = anonimousFun.child(NodeType.FUNCTION);
		if (origFunctionNode == null) {
		    // maybe is a reference to a function
		    AASTNode refNode = anonimousFun.child(NodeType.AT);
		    if (!refNode.hasAttr(NodeAttr.REF_FUNCTION))
			return new GType[] { GType.get(BType.UNDEFINED) };
		    origFunctionNode = (AASTNode) refNode.attr(NodeAttr.REF_FUNCTION);
		}
		List<AASTNode> functionParams = origFunctionNode.childs(NodeType.PARAMETER_LIST);
		List<AASTNode> envparams;
		List<String> explicitparams;
		if (!origFunctionNode.hasAttr(NodeAttr.FUNCTION_APPLY_ENV_PARAMS)) {
		    envparams = new LinkedList<AASTNode>();
		    origFunctionNode.attr(NodeAttr.FUNCTION_APPLY_ENV_PARAMS, envparams);
		} else
		    envparams = (List<AASTNode>) origFunctionNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);

		if (!origFunctionNode.hasAttr(NodeAttr.FUNCTION_PARAMS)) {
		    explicitparams = new LinkedList<String>();
		    origFunctionNode.attr(NodeAttr.FUNCTION_PARAMS, params);
		} else
		    explicitparams = (List<String>) origFunctionNode.attr(NodeAttr.FUNCTION_PARAMS);

		// remove from explicit params, add to env params.
		// this assumes this function is used only here as ode parameter
		for (int i = 2; i < extra_params.length + 2; ++i) {
		    envparams.add(TypeUtils.getIDNode(functionParams.get(i)));
		}
	    }
	    // apply c logic
	    return cfun.apply(node, paramTypes);
	};
    }
}
