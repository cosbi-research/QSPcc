package eu.cosbi.qspcc.expressions.type;

public class StringType extends ValuedType<String> {

    public StringType(BType type, Object... params) {
	super(type, params);
	if (params.length > 0) {
	    if (params[0] == null)
		value = null;
	    else
		value = (String) params[0];
	}
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public StringType(GType o) {
	super(o);
	value = ((StringType) o).value;
    }

    public String strValue() {
	if (hasValue())
	    return value;
	else
	    // dynamic symbol representing this int in the target language
	    return name();
    }

    @Override
    public boolean hasValue() {
	return value != null;
    }

    @Override
    public String valueAsString() {
	return value;
    }

    @Override
    public boolean hasDoubleValue() {
	return false;
    }

    @Override
    public Double valueAsDouble() {
	return null;
    }

    @Override
    public boolean hasBoolValue() {
	return hasValue();
    }

    @Override
    public boolean valueAsBool() {
	return hasValue();
    }

    @Override
    public boolean hasIntValue() {
	return false;
    }

    @Override
    public Integer valueAsInt() {
	return null;
    }

}
