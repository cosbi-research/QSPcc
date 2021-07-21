package eu.cosbi.qspcc.expressions.type;

import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;
import eu.cosbi.qspcc.expressions.type.interfaces.LinkType;

public abstract class DimensionType extends GType implements LinkType<GType>, DimsType {
    // container of what?
    protected GType of;

    public DimensionType(BType type, Object[] params) {
	super(type, params);
	if (params.length > 0)
	    name = (String) params[0];
    }

    public DimensionType(GType o) {
	super(o);
    }

    public boolean hasKnownDimensions() {
	for (IntType dim : dims())
	    if (!dim.hasValue())
		return false;
	return true;
    }

    /**
     * convert the given type such that it contains a scalar type
     * @return
     */
    public abstract GType toScalar();
}
