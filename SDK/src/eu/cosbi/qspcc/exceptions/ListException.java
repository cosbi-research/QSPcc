package eu.cosbi.qspcc.exceptions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import eu.cosbi.qspcc.ast.ProgramNode;

public class ListException extends GException implements List<GException> {

    private static final long serialVersionUID = -3640535000959232178L;
    private List<GException> ex = null;
    private Map<String, Integer> sourceFileMap = null;

    public ListException(ErrorMessage msg) {
	super(msg, null);
	ex = new LinkedList<GException>();
	sourceFileMap = new LinkedHashMap<String, Integer>();
	resetParams(ex);
    }

    @Override
    public ProgramNode node() {
	if (ex.isEmpty())
	    return null;
	else
	    return ex.get(0).node();
    }

    @Override
    public int size() {
	return ex.size();
    }

    @Override
    public boolean isEmpty() {
	return ex.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
	return ex.contains(o);
    }

    @Override
    public Iterator<GException> iterator() {
	return ex.iterator();
    }

    @Override
    public Object[] toArray() {
	return ex.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
	return (T[]) ex.toArray(a);
    }

    /**
     * ensure add doesn't add a list on top of another list. the lists will be
     * joined by source file name
     */
    @Override
    public boolean add(GException e) {
	if (e instanceof ListException) {
	    boolean added = true;
	    if (code().equals(ErrorMessage.LIST_OF_ERRORS)) {
		// list of list of exceptions, every list belongs to a different file
		Integer listIdx = null;
		if (sourceFileMap.containsKey(e.node().sourcePath())) {
		    listIdx = sourceFileMap.get(e.node().sourcePath());
		    ListException subList = (ListException) ex.get(listIdx);
		    for (GException cure : (ListException) e)
			added &= subList.add(cure);
		} else {
		    Integer curIdx = ex.size();
		    added &= ex.add(e);
		    sourceFileMap.put(e.node().sourcePath(), curIdx);
		}
	    } else if (code().equals(ErrorMessage.LIST_OF_TREE_ERRORS)) {
		// list of of exceptions, all from the same compilation unit
		for (GException cure : (ListException) e)
		    added &= ex.add(cure);
	    }
	    return added;
	} else {
	    // report only top-level errors, and leave the inner exceptions as deferrable
	    // explanations
	    boolean added = true;
	    if (ex.isEmpty())
		added &= ex.add(e);
	    else {
		int lastIdx = ex.size() - 1;
		GException lastEx = ex.get(lastIdx);
		if (lastEx.node() == null || e.node() == null) {
		    // two different subtrees
		    added &= ex.add(e);
		} else if (lastEx.node().equals(e.node())) {
		    // skip this error
		} else if (lastEx.node().ancestorOf(e.node()))
		    lastEx.addCause(e);
		else if (e.node().ancestorOf(lastEx.node())) {
		    e.addCause(lastEx);
		    // replace with top-level node
		    ex.set(lastIdx, e);
		} else
		    // two different subtrees
		    added &= ex.add(e);
	    }

	    return added;
	}
    }

    @Override
    public boolean remove(Object o) {
	return ex.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
	return ex.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends GException> c) {
	return ex.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends GException> c) {
	return ex.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
	return ex.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
	return ex.retainAll(c);
    }

    @Override
    public void clear() {
	ex.clear();
    }

    @Override
    public GException get(int index) {
	return (GException) ex.get(index);
    }

    @Override
    public GException set(int index, GException element) {
	return (GException) ex.set(index, element);
    }

    @Override
    public void add(int index, GException element) {
	ex.add(index, element);
    }

    @Override
    public GException remove(int index) {
	return (GException) ex.remove(index);
    }

    @Override
    public int indexOf(Object o) {
	return ex.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
	return ex.lastIndexOf(o);
    }

    @Override
    public ListIterator<GException> listIterator() {
	return ex.listIterator();
    }

    @Override
    public ListIterator<GException> listIterator(int index) {
	return ex.listIterator(index);
    }

    @Override
    public List<GException> subList(int fromIndex, int toIndex) {
	return ex.subList(fromIndex, toIndex);
    }

}
