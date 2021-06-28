package eu.cosbi.qspcc.frontend.corefunction;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;
import eu.cosbi.utils.TypeUtils;

public class R2CUpdaters {

    public static BiFunction<AASTNode, List<GType>, GType[]> updateFunctionReturnReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    // search for function and update his return value
	    if (node.parentExists(NodeType.FUNCTION)) {
		AASTNode functionNode = node.parent(NodeType.FUNCTION);
		List<AASTNode> returnNodes = functionNode.childs(NodeType.FUNCTION_RETURN);
		// in R the return value is always 1
		GType retType = paramTypes.get(0);
		if (retType != null) {
		    // avoid using this node return type as a function return type
		    node.attr(NodeAttr.SKIP_RETURN_NODE_PROPAGATION, true);
		    List<GType> l = new LinkedList<>();
		    l.add(GType.get(retType));
		    returnNodes.get(0).exprs(l, false);
		}
	    }
	    return new GType[] { GType.get(BType.VOID) };
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateFunctionNamesReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    //names returns void if on LHS, returns a 1d matrix of strings if on rhs
	    if (node.parentExists(NodeType.LHS)) {
		return new GType[] { GType.get(BType.VOID) };
	    } else {
		GType paramType = paramTypes.get(0);
		Integer idim = null;
		if (paramType instanceof DimsType) {
		    IntType[] dims = ((DimsType) paramType).dims();
		    if (dims != null && dims.length > 0)
			idim = dims[0].value();
		}
		IntType dim = (IntType) GType.get(BType.INT, idim).name(TypeUtils.matrixDimName(node, 1));
		// if rhs or expr statement
		return new GType[] { GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.STRING),
			new IntType[] { dim }) };
	    }
	};
    }

    public static BiFunction<AASTNode, List<GType>, GType[]> updateFunctionListReturnType() {
	return (AASTNode node, List<GType> paramTypes) -> {
	    StructType ret = new StructType(BType.STRUCT);
	    List<AASTNode> params = node.childs(NodeType.FUNCTION_PARAMETER_LIST);
	    for (int i = 0; i < params.size(); ++i) {
		AASTNode param = params.get(i);
		// resolve type if not already done
		param.expr(paramTypes.get(i));
		ret.addField(param);
	    }
	    return new GType[] { ret };
	};
    }

}
