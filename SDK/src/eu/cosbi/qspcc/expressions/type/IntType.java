package eu.cosbi.qspcc.expressions.type;

public class IntType extends ValuedType<Integer> {

    public IntType(BType type, Object... params) {
	super(type, params);
	if (params.length > 0) {
	    if (params[0] == null)
		value = null;
	    else if (params[0] instanceof Integer)
		value = (Integer) params[0];
	    else if (params[0] instanceof Double)
		value = (((Double) params[0])).intValue();
	    else if (params[0] instanceof Boolean)
		value = (((Boolean) params[0])) ? 1 : 0;
	}
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public IntType(GType o) {
	super(o);
	value = ((IntType) o).value;
    }

    @Override
    public boolean hasValue() {
	return value != null;
    }

    @Override
    public String valueAsString() {
	if (hasValue())
	    // because if it has value, this means that this value will never change
	    return Integer.toString(value);
	else
	    // dynamic symbol representing this int in the target language
	    return name();
    }

    @Override
    public boolean hasDoubleValue() {
	return hasValue();
    }

    @Override
    public Double valueAsDouble() {
	return value.doubleValue();
    }

    @Override
    public boolean hasBoolValue() {
	return hasValue();
    }

    @Override
    public Integer valueAsInt() {
	return value;
    }

    @Override
    public boolean valueAsBool() {
	if (value == null)
	    return false;
	return (value == 0) ? false : true;
    }

    @Override
    public boolean hasIntValue() {
	return hasValue();
    }

}
