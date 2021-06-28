package eu.cosbi.qspcc.expressions.type;

public class BoolType extends IntType {

    public BoolType(BType type, Object... params) {
	super(type, params);
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public BoolType(GType o) {
	super(o);
    }

    public Boolean boolValue() {
	if (value == null)
	    return null;
	return (value.equals(1)) ? true : false;
    }

}
