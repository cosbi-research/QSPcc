package eu.cosbi.qspcc.backend;

import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;

public class CTypeUtils {
	public static boolean isNullable(BType t) {
		switch(t) {
		case BOOL:
		case INT:
		case SCALAR:
			return false;
		default:
			return true;
		}
	}
}
