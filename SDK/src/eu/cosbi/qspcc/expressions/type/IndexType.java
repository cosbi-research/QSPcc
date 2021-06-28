package eu.cosbi.qspcc.expressions.type;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;
import eu.cosbi.qspcc.expressions.type.interfaces.LinkType;
import eu.cosbi.utils.TypeUtils;

/**
 * end/all special indexes have this type. in matlab end/:
 * 
 * @author tomasoni
 *
 */
public class IndexType extends IntType implements LinkType<AASTNode> {
    // end of what matrix?
    AASTNode of;
    // end of which dimension of matrix 'of'?
    Integer dimension;

    public IndexType(GType o) {
	super(o);
	IndexType ot = (IndexType) o;
	of = ot.of;
	dimension = ot.dimension;
    }

    public IndexType(BType type, Object[] params) {
	super(type);
	this.value = null;
	if (params.length > 0)
	    of = (AASTNode) params[0];
	if (params.length > 1) {
	    DimsType matrix = (DimsType) of.expr();

	    if (params[1] instanceof IntType) {
		int i;
		if (matrix instanceof SliceType)
		    // only one dimension
		    i = 0;
		else {
		    boolean found = false;
		    for (i = 0; i < matrix.dims().length; ++i)
			// reference equality
			if (matrix.dims()[i] == params[1]) {
			    found = true;
			    break;
			}
		    if (!found)
			logger.error("Error in " + of.name() + " type deduction");
		}
		// take dimension name from name of matrix (look at of to see the source of the
		// matrix dimension)
		dimension = i;
	    } else if (params[1] instanceof Integer)
		dimension = (Integer) params[1];
	    else
		logger.error(new TypeException(ErrorMessage.INTERNAL_INDEX_DIM_POSITION, of).stringify());
	    // the value of this indextype is the value of the dimension pointed by this
	    // indextype
	    IntType dim = matrix.dims()[dimension];
	    this.value = (dim != null && dim.hasIntValue()) ? dim.valueAsInt() : null;
	}
    }

    @Override
    public AASTNode of() {
	return of;
    }

    /**
     * @return the index on 'of' matrix to which this 'end' type refers to.
     */
    public IntType dimension() {
	if (of != null && ((DimsType) of.expr()).dims() != null)
	    return TypeUtils.firstNonEmptyDim(((DimsType) of.expr()).dims(), dimension).first();
	else
	    return null;
    }

    public Integer dimensionIndex() {
	if (of != null && ((DimsType) of.expr()).dims() != null)
	    return TypeUtils.firstNonEmptyDim(((DimsType) of.expr()).dims(), dimension).second();
	else
	    return null;
    }

    @Override
    public String toString() {
	return (of == null) ? super.toString()
		: super.toString() + " of " + ((of.expr() == null) ? of.toString() : of.expr().toString()) + " dim "
			+ ((dimension() == null) ? null : dimension().toString());
    }

    @Override
    public boolean hasValue() {
	return dimension() != null && dimension().hasValue();
    }

    @Override
    public boolean hasDoubleValue() {
	return hasValue();
    }

    @Override
    public boolean hasBoolValue() {
	return false;
    }

    @Override
    public String valueAsString() {
	return dimension().valueAsString();
    }

    @Override
    public boolean valueAsBool() {
	if (hasValue())
	    return dimension().valueAsBool();
	return false;
    }

    @Override
    public Double valueAsDouble() {
	if (hasValue())
	    return dimension().valueAsDouble();
	return null;
    }

    @Override
    public boolean hasIntValue() {
	return hasValue();
    }

    @Override
    public Integer valueAsInt() {
	if (hasValue())
	    return dimension().valueAsInt();
	return null;
    }

}
