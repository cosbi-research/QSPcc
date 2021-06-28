package eu.cosbi.qspcc.expressions.type;

import eu.cosbi.qspcc.expressions.type.interfaces.LinkType;

/**
 * abstract representation of another type, param of instanceof-like functions
 * and return type of class-like functions
 * 
 * @author tomasoni
 *
 */
public class Type extends GType implements LinkType<GType> {
    GType of;

    public Type(BType type, Object... params) {
	super(type, params);
	if (params.length > 0)
	    of = (GType) params[0];
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public Type(GType o) {
	super(o);
	if (((Type) o).of == null)
	    of = null;
	else
	    of = GType.get(((Type) o).of);
    }

    @Override
    public GType of() {
	return of;
    }

}
