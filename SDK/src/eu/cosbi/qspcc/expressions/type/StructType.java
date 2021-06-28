package eu.cosbi.qspcc.expressions.type;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.expressions.complextype.StructDefinition;
import eu.cosbi.utils.Tuple;

/**
 * 
 * @author tomasoni
 *
 */
public class StructType extends GType {
    public static final String NESTED_STRUCT_NAME_SEP = "__";
    public static final String JOINED_STRUCT_NAME_SEP = "___";
    private Logger logger = LogManager.getLogger(StructType.class);
    // only for structs, usually true on function returns
    private boolean explodeOnAssignment;
    /**
     * complete name of the struct including his predecessors. for example a
     * structure named 'a' but used inside another struct 'b' has fullname b.a while
     * his name is a
     */
    private String fullname = null;
    /**
     * the inner structure of this struct, the name of the fields and their type
     */
    protected Integer structpointer = null;

    public static StructType get() {
	return get(new Object[0]);
    }

    public static StructType get(Object... params) {
	return new StructType(BType.STRUCT, params);
    }

    public StructType(BType type, Object... params) {
	super(type, params);
	explodeOnAssignment = false;
	structpointer = StructDefinition.get();

	if (params.length > 0) {
	    getStructDefinition().addFieldsFromNodes((List<AASTNode>) params[0]);
	}

    }

    protected StructDefinition getStructDefinition() {
	return StructDefinition.get(structpointer);
    }

    /**
     * copy constructor
     * 
     * @param o
     */
    public StructType(GType o) {
	super(o);
	StructType os = (StructType) o;
	this.fullname = os.fullname;
	this.explodeOnAssignment = os.explodeOnAssignment;
	this.structpointer = os.structpointer;
    }

    @Override
    public StructType name(String symbol) {
	super.name(symbol);
	// update struct so that we will be using the same object
	// as other structures with the same name.
	// this method joins the old structure and the new structure in a single object
	// pointed by both structpointers
	structpointer = getStructDefinition().setName(symbol);
	return this;
    }

    @Override
    public String name() {
	return getStructDefinition().getName();
    }

    public String fullname() {
	return fullname;
    }

    public GType fullname(String newfullname) {
	fullname = newfullname;
	// set name if not known
	if (name() == null && fullname != null) {
	    String[] nestedstructnames = fullname.split("\\" + StructType.NESTED_STRUCT_NAME_SEP);
	    name(nestedstructnames[nestedstructnames.length - 1]);
	}
	return this;
    }

    public GType addField(List<GType> expr, String name) {
	getStructDefinition().addField(expr, name);
	return this;
    }

    public GType updateField(AASTNode field) {
	getStructDefinition().updateField(field.exprs(), field.name());
	return this;
    }

    public GType updateField(AASTNode field, int expr_id) {
	getStructDefinition().updateField(field.exprs(), expr_id, field.name());
	return this;
    }

    public GType updateField(List<GType> expr, String name) {
	getStructDefinition().updateField(expr, name);
	return this;
    }

    public GType addField(AASTNode field) {
	getStructDefinition().addField(field);
	return this;
    }

    public List<GType> getField(String name) {
	return getStructDefinition().getField(name);
    };

    public Iterator<Tuple<List<GType>, String>> iterFields() {
	return getStructDefinition().iterator();
    };

    public boolean explodeOnAssignment() {
	return this.explodeOnAssignment;
    }

    public void explodeOnAssignment(boolean b) {
	this.explodeOnAssignment = b;
    }

    public int numberOfFields() {
	return getStructDefinition().numberOfFields();
    }

    @Override
    public String toDebugString() {
	return getStructDefinition().toString() + " input=" + inputName() + " known=" + knownType + " explode="
		+ explodeOnAssignment;
    }

    public boolean isSubSetOf(StructType otherStruct) {
	return getStructDefinition().isSubSetOf(otherStruct.getStructDefinition());
    }
}
