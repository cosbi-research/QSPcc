package eu.cosbi.qspcc.expressions.type;

public class ScalarType extends ValuedType<Double> {

    public ScalarType(BType type, Object... params) {
	super(type, params);
	if (params.length > 0) {
	    if (params[0] == null)
		value = null;
	    else
		value = ((Number) params[0]).doubleValue();
	}
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public ScalarType(GType o) {
	super(o);
	value = ((ScalarType) o).value;
    }

    public Double scalarValue() {
	return value;
    }

    @Override
    public boolean hasValue() {
	return value != null;
    }

    @Override
    public String valueAsString() {
	if (hasValue())
	    return Double.toString(value);
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
	return value;
    }

    @Override
    public Integer valueAsInt() {
	return value.intValue();
    }

    @Override
    public boolean hasBoolValue() {
	return hasValue();
    }

    @Override
    public boolean valueAsBool() {
	if (value == null)
	    return false;
	return (value == 0.0) ? false : true;
    }

    @Override
    public boolean hasIntValue() {
	return hasValue();
    }

}
