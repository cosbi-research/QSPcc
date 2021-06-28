package eu.cosbi.qspcc.symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;

public class Symbols {
    // keep track of temporary variables used to translate the expression
    static Integer vectorNumber = -1;
    static Integer idxVectorNumber = -1;
    static Integer stringNumber = -1;
    static Integer scalarNumber = -1;
    static Integer booleanNumber = -1;
    static Integer integerNumber = -1;
    static Integer genericNumber = -1;
    static Map<NodeType, Integer> typeNumber = new HashMap<NodeType, Integer>();
    static final String prefix = "_";

    public static void resetSymbols() {
	vectorNumber = -1;
	idxVectorNumber = -1;
	stringNumber = -1;
	scalarNumber = -1;
	booleanNumber = -1;
	integerNumber = -1;
	genericNumber = -1;
	typeNumber = new HashMap<NodeType, Integer>();
    }

    private static String getNewScalarVectorSymbol() {
	return "vector_" + (++vectorNumber);
    }

    private static String getNewIndexVectorSymbol() {
	return "idxs_" + (++idxVectorNumber);
    }

    private static String getNewStringSymbol() {
	return "str_" + (++stringNumber);
    }

    private static String getNewScalarSymbol() {
	return "scalar_" + (++scalarNumber);
    }

    private static String getNewBooleanSymbol() {
	return "bool_" + (++booleanNumber);
    }

    private static String getNewIntegerSymbol() {
	return "int_" + (++integerNumber);
    }

    private static String getNewStructSymbol() {
	return "s_" + (++integerNumber);
    }

    private static String getNewGenericSymbol() {
	return "g_" + (++genericNumber);
    }

    public static String getSymbol(NodeType type) {
	if (!typeNumber.containsKey(type)) {
	    typeNumber.put(type, -1);
	}
	Integer num = typeNumber.get(type);
	num++;
	typeNumber.put(type, num);
	return type + Integer.toString(num);
    }

    public static String getSymbol(AASTNode node) {
	switch (node.type()) {
	case FLOAT:
	case STRING:
	case INT:
	case TRUE:
	case FALSE:
	    return node.name();
	default:
	    String name = getSymbolName(node);
	    if (node.expr() == null)
		return getSymbolFromNodeType(name, node.type());
	    else
		return getSymbolFromType(name, node.expr().type());
	}
    }

    private static String getSymbolName(AASTNode node) {
	String suffix = "";
	switch (node.type()) {
	case FIELDACCESS:
	    return node.childs().get(0).name() + suffix;
	case APPLY:
	case FUNCTION:
	    return node.child(new NodeType[] { NodeType.ID, NodeType.FIELDACCESS }).name() + suffix;
	case ID:
	    return node.name() + suffix;
	default:
	    return "";
	}
    }

    public static String getSymbolFromType(String name, BType node) {
	String aname = prefix + name + "_";
	switch (node) {
	case BOOL:
	    return aname + getNewBooleanSymbol();
	case MATRIX_ACCESS_LAST:
	case INT:
	    return aname + getNewIntegerSymbol();
	case MATRIX_ACCESS_ALL:
	case MATRIX_ACCESS_SLICE:
	    return aname + getNewIndexVectorSymbol();
	case MATRIX:
	    return aname + getNewScalarVectorSymbol();
	case SCALAR:
	    return aname + getNewScalarSymbol();
	case STRING:
	    return aname + getNewStringSymbol();
	case STRUCT:
	    return aname + getNewStructSymbol();
	default:
	    return aname + getNewGenericSymbol();
	}
    }

    public static String getSymbolFromNodeType(String name, NodeType node) {
	String aname = prefix + name + "_";
	switch (node) {
	case FALSE:
	case TRUE:
	case LST:
	case GRT:
	case LOG_AND:
	case LOG_OR:
	case BIN_AND:
	case BIN_OR:
	case LSTE:
	case GRTE:
	case NEQ:
	case DOUBLE_EQ:
	    return aname + getNewBooleanSymbol();
	case END:
	case INT:
	    return aname + getNewIntegerSymbol();
	case COLON:
	    return aname + getNewIndexVectorSymbol();
	case VECTOR:
	case MATRIX:
	    return aname + getNewScalarVectorSymbol();
	case FLOAT:
	    return aname + getNewScalarSymbol();
	case STRING:
	    return aname + getNewStringSymbol();
	case FIELDACCESS:
	    return aname + getNewStructSymbol();
	default:
	    return aname + getNewGenericSymbol();
	}
    }

    public static String getStructSymbol(String name, List<String> structPredecessors, String sep) {
	return String.join(sep, structPredecessors) + sep + name;
    }

    public static String getInitStructSymbol(String name, List<String> structPredecessors) {
	// C doesn't allow STRUCT_SEP in declarations
	return String.join("_", structPredecessors) + "_" + name;
    }

    public static String getInitStructSymbol(String[] structElements) {
	if (structElements.length > 1) {
	    List<String> predecessors = new ArrayList<String>(structElements.length - 1);
	    for (int i = 0; i < structElements.length - 1; ++i)
		predecessors.add(structElements[i]);
	    return getInitStructSymbol(structElements[structElements.length - 1], predecessors);
	} else
	    return structElements[0];
    }
}
