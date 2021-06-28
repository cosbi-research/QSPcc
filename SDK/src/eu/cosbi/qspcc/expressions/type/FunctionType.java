package eu.cosbi.qspcc.expressions.type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FunctionType extends GType {
    private Logger logger = LogManager.getLogger(FunctionType.class);
    private List<GType> inputs;
    private List<GType> outputs;

    public FunctionType(BType type, Object... params) {
	super(type, params);
	if (params.length > 1) {
	    if (params[0] instanceof List) {
		inputs = (List<GType>) params[0];
		outputs = (List<GType>) params[1];
	    } else {
		inputs = new LinkedList<GType>();
		outputs = new LinkedList<GType>();
		for (GType t : ((GType[]) params[0]))
		    inputs.add(t);
		for (GType t : ((GType[]) params[1]))
		    outputs.add(t);
	    }
	} else {
	    logger.error("Too few pameter for function initialization");
	}
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public FunctionType(GType o) {
	super(o);

	FunctionType fo = (FunctionType) o;
	inputs = new LinkedList<GType>();
	Iterator<GType> foit = fo.inputs.iterator();
	while (foit.hasNext())
	    inputs.add(GType.get(foit.next()));

	outputs = new LinkedList<GType>();
	foit = fo.outputs.iterator();
	while (foit.hasNext())
	    outputs.add(GType.get(foit.next()));

    }

    public List<GType> inputs() {
	return inputs;
    }

    public void inputs(List<GType> newinputs) {
	inputs = newinputs;
    }

    public void input(int i, GType newtype) {
	inputs.set(i, newtype);
    }

    public List<GType> outputs() {
	return outputs;
    }

    public void outputs(List<GType> newoutputs) {
	outputs = newoutputs;
    }

    public void output(int i, GType newtype) {
	outputs.set(i, newtype);
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof BType)
	    return super.equals(o);
	else if (super.equals(o)) {
	    FunctionType otherf = ((FunctionType) o);
	    if (inputs.size() != otherf.inputs.size())
		return false;
	    // check also parameters
	    Iterator<GType> it = inputs.iterator();
	    Iterator<GType> oit = otherf.inputs.iterator();

	    while (it.hasNext() && oit.hasNext()) {
		GType in = it.next();
		GType oin = oit.next();

		if (in.equals(BType.UNKNOWN) && !in.equals(oin))
		    return false;
	    }
	    if (outputs.size() != otherf.outputs.size())
		return false;

	    it = outputs.iterator();
	    oit = otherf.outputs.iterator();
	    // check also return values
	    while (it.hasNext() && oit.hasNext()) {
		GType in = it.next();
		GType oin = oit.next();

		if (in.equals(BType.UNKNOWN) && !in.equals(oin))
		    return false;
	    }
	    return true;
	} else
	    return false;
    }

    @Override
    public FunctionType name(String symbol) {
	super.name(symbol);
	return this;
    }

    @Override
    public boolean hasChild(Class<? extends GType> clazz) {
	for (GType input : inputs)
	    if (clazz.isInstance(input))
		return true;

	for (GType output : outputs)
	    if (clazz.isInstance(output))
		return true;
	return false;
    }

    @Override
    public GType child(Class<? extends GType> clazz) {
	for (GType input : inputs)
	    if (clazz.isInstance(input))
		return input;
	for (GType output : outputs)
	    if (clazz.isInstance(output))
		return output;
	return null;
    }

}
