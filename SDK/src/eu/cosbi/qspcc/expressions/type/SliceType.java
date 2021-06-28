package eu.cosbi.qspcc.expressions.type;

import eu.cosbi.utils.Triple;
import eu.cosbi.utils.TypeUtils;

public class SliceType extends DimensionType {
    private Triple<GType, GType, GType> slices;
    IntType dim;

    public SliceType(BType type, Object... params) {
	super(type, params);
	// [0] is the name		
	if (params.length > 1) {
	    of = (GType) params[1];
	}
	if (params.length > 2) {
	    slices = (Triple<GType, GType, GType>) params[2];
	}

	if (params.length > 3 && params[3] != null)
	    dim = (IntType) params[3];
	else
	    dim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, 1));
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public SliceType(GType o) {
	super(o);
	SliceType sltype = ((SliceType) o);
	if (sltype.of != null)
	    of = GType.get(sltype.of);
	if (sltype.slices != null)
	    slices = new Triple<GType, GType, GType>(GType.get(sltype.slices.first()),
		    GType.get(sltype.slices.second()), GType.get(sltype.slices.third()));
	if (sltype.dim != null)
	    dim = new IntType(sltype.dim);
	else
	    dim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(name, 1));
    }

    /**
     * 
     * @return null if the sequence size is not known, otherwise the sequence size.
     * ex. 1:4 -> 4
     */
    public Integer sequenceSize() {
	if (slices == null || slices.first().equals(BType.UNKNOWN) || slices.second().equals(BType.UNKNOWN)
		|| slices.third().equals(BType.UNKNOWN))
	    return null;
	else {
	    ValuedType start = (ValuedType) slices.first();
	    ValuedType step = (ValuedType) slices.second();
	    ValuedType end = (ValuedType) slices.third();
	    if (start.hasValue() && step.hasValue() && end.hasValue()) {
		if (step.valueAsInt().equals(0))
		    return null;
		Double len = new Double((end.valueAsDouble() - start.valueAsDouble() + 1) / step.valueAsDouble());
		return len.intValue();
	    } else
		return null;
	}
    }

    public Triple<GType, GType, GType> slices() {
	return slices;
    }

    public IntType dim() {
	if (!dim.hasValue() && name != null)
	    dim.name(TypeUtils.matrixDimName(name, 1));
	return dim;
    }

    @Override
    public IntType[] dims() {
	if (dim != null) {
	    if (!dim.hasValue() && name != null)
		dim.name(TypeUtils.matrixDimName(name, 1));
	    // matrix 1xdim
	} else {
	    dim = (IntType) GType.get(BType.INT);
	    if (name != null)
		dim.name(TypeUtils.matrixDimName(name, 1));
	    // matrix 1xunknown
	}
	return new IntType[] { (IntType) GType.get(BType.INT, 1), dim };
    }

    @Override
    public boolean hasChild(Class<? extends GType> clazz) {
	return clazz.isInstance(of);
    }

    @Override
    public GType child(Class<? extends GType> clazz) {
	return of;
    }

    @Override
    public GType of() {
	return of;
    }

    public void of(GType newOf) {
	of = newOf;
    }

}
