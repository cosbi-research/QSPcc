package eu.cosbi.qspcc.expressions.type;

import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;

public abstract class ValuedType<T> extends GType implements DimsType {
    protected T value;
    protected IntType dim = null;

    public ValuedType(BType type, Object... params) {
	super(type, params);
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public ValuedType(GType o) {
	super(o);
	value = ((ValuedType<T>) o).value;
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof BType)
	    return super.equals(o);
	else if (super.equals(o)) {
	    ValuedType<T> otheri = ((ValuedType<T>) o);
	    // warning a-symmetric check if this doesn't have a value then the check is done
	    // only on the type,
	    // if this has a value, the o value should be the same.
	    if ((value != null && otheri.value == null) || (value != null && !value.equals(otheri.value)))
		return false;
	    return true;
	} else
	    return false;
    }

    public T value() {
	return value;
    }

    public IntType[] dims() {
	// reuse always the same instance
	// important because of indextype (see indextype constructor)
	if (dim == null)
	    dim = (IntType) GType.get(BType.INT, 1);
	// every valued type is a
	// 1D matrix with dimension 1
	return new IntType[] { dim };
    }

    @Override
    public String toDebugString() {
	String n = "";
	// if(name!=null)
	// n = "{"+ name +"}";
	return (value == null) ? super.toString() + n : super.toString() + "[" + value.toString() + "]" + n;
    }

    public void deleteValue() {
	value = null;
	if (params != null && params.length == 1)
	    params[0] = null;
    }

    @Override
    public Object[] params() {
	if (params != null && (params.length > 0 && params[0] != null)) {
	} else if (value != null) {
	    params = new Object[] { value };
	}
	return params;
    }

    public abstract boolean hasValue();

    // conversion utilities
    public abstract boolean hasDoubleValue();

    public abstract boolean hasBoolValue();

    public abstract String valueAsString();

    public abstract boolean valueAsBool();

    /**
     * most generic value type
     * 
     * @return null if not castable to double. the equivalent double otherwise.
     */
    public abstract Double valueAsDouble();

    public abstract boolean hasIntValue();

    public abstract Integer valueAsInt();

}
