package eu.cosbi.qspcc.symbols.type;

/**
 * defines the type of the symbol ex. a SymbolType FUNCTION can have ExprType
 * SCALAR if it is a function that returns a scalar
 * 
 * @author tomasoni
 *
 */
public enum SymbolType {
    SCALAR,
    STRING,
    // a function handle (we don't know parameters/output and
    // we haven't translated it yet)
    ANONYMOUS_FUNCTION,
    FUNCTION,
    BOOL,
    INT,
    INPUT,
    MATRIX;

    public boolean isFunction() {
	return SymbolType.ANONYMOUS_FUNCTION.equals(this) || SymbolType.FUNCTION.equals(this);
    }

    public boolean isScalar() {
	return SymbolType.SCALAR.equals(this);
    }

    public boolean isInput() {
	return SymbolType.INPUT.equals(this);
    }
}
