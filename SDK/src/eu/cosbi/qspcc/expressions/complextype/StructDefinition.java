package eu.cosbi.qspcc.expressions.complextype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.utils.Tuple;

/**
 * a class that defines a structure.
 * Overrides hashCode and equals because we want a set of struct definitions in our program 
 * since a struct can have only a single definition, namely the list of the sub-fields and corresponding sub-types
 * 
 * @author tomasoni
 *
 */
public class StructDefinition implements Iterable {

    // allows us to keep a single instance for each structure
    static private Map<Integer, StructDefinition> structures = new HashMap<Integer, StructDefinition>();
    // structures that should be persisted among different translate() call
    static private Map<String, Map<Integer, StructDefinition>> default_structures = null;
    private LinkedList<List<GType>> subExprs;
    private LinkedList<String> subExprNames;
    // overrides GType name, because given a set of fields, the name should be uniquely determined
    private String name;

    private StructDefinition() {
	subExprs = new LinkedList<List<GType>>();
	subExprNames = new LinkedList<String>();
	name = null;
    }

    public static Integer get() {
	StructDefinition s = new StructDefinition();
	int shash = s.hashCode();

	if (!structures.containsKey(shash)) {
	    structures.put(shash, s);
	    return shash;
	} else
	    // if already in struct, use the existing one
	    return shash;
    }

    public static StructDefinition get(int shash) {
	// if already in struct, use the existing one
	return structures.get(shash);
    }

    /**
     * don't update values using this read-only set
     */
    public static Set<StructDefinition> structures() {
	Set<StructDefinition> r = new HashSet<>();
	r.addAll(structures.values());
	return r;
    }

    public static void clear(String lang) {
	structures.clear();
	if (default_structures != null && default_structures.containsKey(lang)) {
	    // put default values back
	    for (Map.Entry<Integer, StructDefinition> entry : default_structures.get(lang).entrySet())
		structures.put(new Integer(entry.getKey().intValue()), entry.getValue());
	}
    }

    public static void initDefaultValues(String lang) {
	if (default_structures == null)
	    default_structures = new HashMap<String, Map<Integer, StructDefinition>>();
	if (!default_structures.containsKey(lang)) {
	    // save default values
	    default_structures.put(lang, new HashMap<Integer, StructDefinition>());
	    for (Map.Entry<Integer, StructDefinition> entry : structures.entrySet())
		default_structures.get(lang).put(new Integer(entry.getKey().intValue()), entry.getValue());
	}
    }

    @Override
    public String toString() {
	List<String> fields = new ArrayList<String>(subExprNames.size());
	Iterator<List<GType>> exprIt = subExprs.iterator();
	Iterator<String> namesIt = subExprNames.iterator();
	while (namesIt.hasNext()) {
	    List<GType> exprs = exprIt.next();
	    List<String> estrs = new ArrayList<>(exprs.size());
	    for (GType e : exprs) {
		if (e.equals(BType.STRUCT) && ((((StructType) e).name() == null && getName() == null)
			|| (((StructType) e).name().equals(getName()))))
		    estrs.add(getName() + ": [SELF]");
		else
		    estrs.add(e.toString());
	    }
	    fields.add(namesIt.next() + ": [" + String.join(", ", estrs) + "]");
	}
	return name + ": [" + String.join(",", fields) + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	/*if (subExprNames == null)
	    result = prime * result;
	else {
	    for (String ename : subExprNames)
		result = prime * result + ename.hashCode();
	}
	if (subExprs == null)
	    result = prime * result;
	else {
	    for (List<GType> etypes : subExprs)
		for (GType type : etypes)
		    result = prime * result + type.hashCode();
	}*/
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	StructDefinition other = (StructDefinition) obj;

	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	/*if (subExprNames == null) {
	    if (other.subExprNames != null)
		return false;
	} else {
	    Iterator<String> namesIterator = subExprNames.iterator();
	    Iterator<String> onamesIterator = other.subExprNames.iterator();
	    while (namesIterator.hasNext() && onamesIterator.hasNext()) {
		if (!namesIterator.next().equals(onamesIterator.next()))
		    return false;
	    }
	}
	if (subExprs == null) {
	    if (other.subExprs != null)
		return false;
	} else {
	    Iterator<List<GType>> exprsIterator = subExprs.iterator();
	    Iterator<List<GType>> oexprsIterator = other.subExprs.iterator();
	
	    while (exprsIterator.hasNext() && oexprsIterator.hasNext()) {
		List<GType> exprs = exprsIterator.next();
		List<GType> oexprs = oexprsIterator.next();
	
		Iterator<GType> exprIterator = exprs.iterator();
		Iterator<GType> oexprIterator = oexprs.iterator();
	
		while (exprIterator.hasNext() && oexprIterator.hasNext())
		    if (!exprIterator.next().equals(oexprIterator.next()))
			return false;
	    }
	}*/
	return true;
    }

    /**
     * This method allows us to keep at any time only 
     * a single object for each distinct (according to hashcode and equals)
     * StructDefinition. 
     * Also if a structure is assigned with another name (the unique key that determines the hash)
     * the two structures will be joined together and the old structure won't exist (but the hash of the old structure will point to the new joined structure)
     * @param newname 
     * 
     * @return the new structDefinition, if last change happen to clash with an existing structdefinition, the new one will replace the old one
     */
    private Integer setUniqueStruct(String newname) {
	int oldhash = hashCode();
	String oldname = this.name;
	// update name and compute new hashcode
	this.name = newname;
	int newhash = hashCode();
	// reset this instance to original
	this.name = oldname;
	StructDefinition newstruct = null;
	// this structure already exists?
	if (!structures.containsKey(newhash)) {
	    // doesn't exist, create a new one with this name and return it.
	    newstruct = new StructDefinition();
	    newstruct.name = newname;
	    structures.put(newhash, newstruct);
	} else {
	    // if already contained, join fields and use the new value
	    newstruct = structures.get(newhash);

	    // add fields of this definition to the old one
	    Iterator<String> subExprNamesIter = subExprNames.iterator();
	    Iterator<List<GType>> subExprsIter = subExprs.iterator();
	    while (subExprNamesIter.hasNext() && subExprsIter.hasNext()) {
		newstruct.internalAddField(subExprsIter.next(), subExprNamesIter.next());
	    }
	}
	if (oldname != null && structures.containsKey(oldhash))
	    // overwrite old structure with the new one
	    structures.put(oldhash, newstruct);

	return newhash;
    }

    public String getName() {
	return name;
    }

    public Integer setName(String name) {
	// if this name is the same of another struct, use that structure
	return setUniqueStruct(name);
    }

    public void internalAddField(List<GType> exprs, String name) {
	if (!subExprNames.contains(name)) {
	    subExprs.add(exprs);
	    subExprNames.add(name);
	}
    }

    public void addField(List<GType> exprs, String name) {
	internalAddField(exprs, name);
    }

    public void addField(AASTNode field) {
	internalAddField(field.exprs(), field.name());
    }

    public void addFieldsFromNodes(List<AASTNode> list) {
	for (AASTNode n : list)
	    internalAddField(n.exprs(), n.name());
    }

    public void updateField(List<GType> expr, String newname) {
	int idx = subExprNames.indexOf(newname);
	if (idx >= 0) {
	    subExprs.set(idx, expr);
	} else {
	    // if not existing, add
	    addField(expr, newname);
	}
    }

    public void updateField(List<GType> expr, int expr_id, String newname) {
	int idx = subExprNames.indexOf(newname);
	if (idx >= 0) {
	    subExprs.get(idx).set(expr_id, expr.get(expr_id));
	} else {
	    // if not existing, add
	    addField(expr, newname);
	}
    }

    public int numberOfFields() {
	return subExprNames.size();
    }

    @Override
    public Iterator<Tuple<List<GType>, String>> iterator() {
	Iterator<Tuple<List<GType>, String>> it = new Iterator<Tuple<List<GType>, String>>() {

	    private Iterator<String> namesIter = subExprNames.iterator();
	    private Iterator<List<GType>> exprsIter = subExprs.iterator();

	    @Override
	    public boolean hasNext() {
		return namesIter.hasNext() && exprsIter.hasNext();
	    }

	    @Override
	    public Tuple<List<GType>, String> next() {
		return new Tuple<List<GType>, String>(exprsIter.next(), namesIter.next());
	    }

	    @Override
	    public void remove() {
		throw new UnsupportedOperationException();
	    }
	};
	return it;
    }

    public List<GType> getField(String name) {
	Iterator<Tuple<List<GType>, String>> it = iterator();

	while (it.hasNext()) {
	    Tuple<List<GType>, String> tp = it.next();
	    if (tp.second().equals(name))
		return tp.first();
	}
	return null;
    }

    public boolean isSubSetOf(StructDefinition otherStruct) {
	return !new LinkedList<String>(subExprNames).retainAll(otherStruct.subExprNames);
    }

}
