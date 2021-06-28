package eu.cosbi.qspcc.interfaces.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface StepType {
    public enum Function {
	COREFUNCTION,
	SCALAREXPRESSION,
	INT,
	SCALAR,
	INTV,
	BOOLMATRIX,
	INTMATRIX,
	SCALARMATRIX,
	BOOL,
	STRUCT,
	SCALARV,
	ASSIGN,
	UNARYSCALAREXPRESSION,
	POWER,
	BOOLEXPR,
	SUM,
	MUL,
	STRING,
	MATRIXSCALAREXPRESSION,
	MATRIXMATRIXEXPRESSION,
	MATRIXPOINTWISEEXPRESSION,
	USERFUNCTIONCALL,
	UNBOUND_WITH_PARAMS,
	UNBOUND_STRUCT,
	ALLOCATE_MATRIX,
	DEALLOCATE_MATRIX,
	MATRIX_ACCESS,
	MATRIX_BLOCKFILL,
	ABSTRACT_INDEX_MATRIX,
	COMMENT,
	TRANSPOSE,
	MATRIX_JOIN_1D,
	BEGIN_IF,
	END_IF,
	// function type that represents
	// unconditional jumps.
	JUMP,
	INDEX_MATRIX,
	SLICE,
	SCALAR_SLICE;
    }

    Function method();
}
