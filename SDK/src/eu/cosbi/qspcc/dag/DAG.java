package eu.cosbi.qspcc.dag;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.dag.exceptions.WalkException;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.utils.Tuple;

/**
 * general purpose dag implementation with non-recursive DFS visit in pre-order
 * 
 * @author tomasoni
 *
 */
public class DAG<Root, Node extends DAG<Root, Node> & ProgramNode> {
    private Logger logger = LogManager.getLogger(DAG.class);
    // reference to global AAST (where maps and globals are stored)
    protected Root treeRef;
    protected Node parent;
    protected List<Node> childs;
    // for DFS
    protected boolean visited = false;

    public DAG(Root r, Node p) {
	this.childs = new ArrayList<Node>();
	this.treeRef = r;
	this.parent = p;
    }

    /**
     * quick and easy way to walk the tree without using a full listener will be
     * called when the node will be seen for the first time
     * 
     * @throws Exception
     */
    public void walkOnExit(DAGFunctionalListener<Root, Node> listener, boolean stopOnError) throws GException {
	walkFor(new DAGListener<Root, Node, Object>() {
	    @Override
	    public void onEnter(Root aast, Node node) throws GException {
	    }

	    @Override
	    public void onExit(Root ref, Node curNode, Deque<Object> results) throws GException {
		listener.on(ref, curNode);
	    }

	    @Override
	    public void onWalkCompleted(Root ref) throws GException {
	    }

	    @Override
	    public void onWalkStarted(Root ref) throws GException {
	    }
	}, stopOnError);
    }

    /**
     * quick and easy way to walk the tree without using a full listener will be
     * called when all the node childs has been visited
     * 
     * @throws Exception
     */
    public void walkOnEnter(DAGFunctionalListener<Root, Node> listener, boolean stopOnError) throws GException {
	walkFor(new DAGListener<Root, Node, Object>() {
	    @Override
	    public void onEnter(Root aast, Node node) throws GException {
		listener.on(aast, node);
	    }

	    @Override
	    public void onExit(Root ref, Node curNode, Deque<Object> results) throws GException {
	    }

	    @Override
	    public void onWalkCompleted(Root ref) throws GException {
	    }

	    @Override
	    public void onWalkStarted(Root ref) throws GException {
	    }
	}, stopOnError);
    }

    public <T> void walkFor(DAGListener<Root, Node, T> listener, boolean stopOnError) throws GException {
	walkFor(listener, false, stopOnError);
    }

    /**
     * let the input ASTListener be notified of the next walk on this AAST
     * Implements a pre-ordered visit of the tree in DFS
     * 
     * @param stopOnError stop walk on error
     * @param stopOnThis  do not go up to the parent of the node on which we are
     *                    called
     * @throws Exception
     */
    public <T> void walkFor(DAGListener<Root, Node, T> listener, boolean stopOnThis, boolean stopOnError)
	    throws GException {
	listener.onWalkStarted(treeRef);
	walkHelper(listener, stopOnThis, stopOnError);
	this.visited = false;
	listener.onWalkCompleted(treeRef);
    }

    private <T> void walkHelper(DAGListener<Root, Node, T> listener, boolean stopOnThis, boolean stopOnError)
	    throws GException {
	// queue of results for curNode
	Deque<T> results = new ArrayDeque<T>();
	Deque<Node> nodes = new ArrayDeque<Node>();
	ListException ex = new ListException(ErrorMessage.LIST_OF_TREE_ERRORS);

	nodes.push((Node) this);
	// fill toNotify stack with nodes
	// in reverse order
	while (!nodes.isEmpty()) {
	    Node curNode = nodes.pop();
	    // enter logic
	    try {
		listener.onEnter(treeRef, curNode);
	    } catch (Exception e) {
		if (e instanceof GException)
		    ex.add((GException) e);
		else
		    ex.add(new WalkException(curNode, e));
		if (stopOnError)
		    throw ex.get(0);
		logger.debug("Error onEnter '" + curNode.code() + "' at " + curNode.location() + "", e);
	    }
	    curNode.visited = true;

	    List<Node> childs = curNode.childs();

	    // on exit check if parent has all visited childrens
	    if (childs.isEmpty()) {
		// leaf node
		try {
		    listener.onExit(treeRef, curNode, results);
		} catch (Exception e) {
		    if (e instanceof GException)
			ex.add((GException) e);
		    else
			ex.add(new WalkException(curNode, e));
		    if (stopOnError)
			throw ex.get(0);
		    logger.debug("Error onExit '" + curNode.code() + "' at " + curNode.location() + "", e);
		}
		// check also parents of leaf node
		Node parentExit = curNode.parent;
		while (parentExit != null)
		    if (parentExit.visitedChilds()) {
			try {
			    listener.onExit(treeRef, parentExit, results);
			} catch (Exception e) {
			    GException curEx = null;
			    if (e instanceof GException)
				curEx = (GException) e;
			    else
				curEx = new WalkException(curNode, e);
			    ex.add(curEx);
			    // TODO: set error in the AST otherwise it won't be printed
			    if (stopOnError)
				throw ex.get(0);
			    logger.debug("Error onExit '" + curNode.code() + "' at " + curNode.location() + "", e);
			}
			parentExit.unvisitChilds();

			if (stopOnThis && parentExit == this)
			    // break if I shouldn't go to parent of this
			    break;

			parentExit = parentExit.parent;
		    } else
			break;
	    }
	    // pre-order visit
	    Collections.reverse(childs);
	    for (Node child : childs) {
		nodes.push(child);
	    }

	}
	// raise exception now if not already done before
	if (!ex.isEmpty())
	    throw ex;
    }

    /**
     * return the others list ordered by nearest (in tree) symbol node that defines
     * this symbol name. This is the implementation of the static scope.
     * 
     * @param symbolNode
     * @return
     */
    public List<Node> nearestBefore(List<Node> others) {
	List<Integer> distances = new ArrayList<Integer>();
	List<Node> ordered_nodes = new ArrayList<Node>();

	for (Node other : others) {
	    if (other.equals((Node) this))
		continue;

	    Tuple<Node, Integer> common_ancestor_s = commonAncestorBefore((Node) this, other);
	    if (common_ancestor_s == null)
		continue;
	    int distance = common_ancestor_s.second();
	    if (distances.isEmpty()) {
		distances.add(distance);
		ordered_nodes.add(other);
	    } else {
		// insertion sort
		int insertion_idx = -1;
		for (int i = 0; i < distances.size(); ++i) {
		    Integer old_distance = distances.get(i);
		    if (distance < old_distance) {
			// insert now, otherwise wait the first
			// old distance lower
			insertion_idx = i;
			break;
		    }
		}
		if (insertion_idx == -1) {
		    // add in tail
		    distances.add(distance);
		    ordered_nodes.add(other);
		} else {
		    distances.add(insertion_idx, distance);
		    ordered_nodes.add(insertion_idx, other);
		}
	    }
	}
	return ordered_nodes;
    }

    /**
     * returns the common ancestor in tree
     * 
     * @param after
     * @param before
     * @return the common ancestor and the distance between first node and common
     *         ancestor and distance between second node and common ancestor
     *         respectively.
     */
    public Tuple<Node, Integer> commonAncestorBefore(Node after, Node before) {
	if (after == null || before == null)
	    return null;
	else if (after == before)
	    return new Tuple<Node, Integer>(after, 0);

	LinkedHashSet<Node> aparents = after.inclusiveParents();
	LinkedHashSet<Node> bparents = before.inclusiveParents();

	LinkedHashSet<Node> intersection = new LinkedHashSet<Node>(aparents);
	// intersection now will contain the intersection
	intersection.retainAll(bparents);
	// return first one added
	if (intersection.isEmpty())
	    return null;
	Node common_ancestor = intersection.iterator().next();

	// be sure after is after before
	boolean beforeFound = false;
	boolean afterFound = false;
	int distance = 0;
	for (int i = 0; i < common_ancestor.childs.size(); ++i) {
	    Node achild = common_ancestor.childs.get(i);
	    if (!beforeFound && bparents.contains(achild)) {
		beforeFound = true;
		distance = i;
	    }
	    if (!afterFound && aparents.contains(achild)) {
		afterFound = true;
		distance = i - distance;
	    }

	    if (afterFound && !beforeFound)
		// before was found after after, neglect this result
		return null;
	    // found both
	    else if (afterFound && beforeFound)
		break;
	}

	return new Tuple<Node, Integer>(common_ancestor, distance);
    }

    public List<Node> childs() {
	return new ArrayList<Node>(childs);
    }

    public boolean removeChild(Node c) {
	return childs.remove(c);
    }

    public int childPosition(Node c) {
	return childs.indexOf(c);
    }

    public int childPosition(String code) {
	if (code == null)
	    return -1;
	int i = 0;
	Iterator<Node> it = childs.iterator();
	while (it.hasNext()) {
	    Node c = it.next();
	    if (code.equals(c.code()))
		return i;
	    i++;
	}
	return -1;
    }

    /**
     * parents that include the node itself
     * 
     * @return
     */
    public LinkedHashSet<Node> inclusiveParents() {
	return ancestors(true);
    }

    /**
     * parents that doesn't include the node itself
     * 
     * @return
     */
    public LinkedHashSet<Node> parents() {
	return ancestors(false);
    }

    protected LinkedHashSet<Node> ancestors(boolean inclusive) {
	LinkedHashSet<Node> parents = new LinkedHashSet<Node>();
	if (inclusive)
	    parents.add((Node) this);
	Node curNode = (Node) this;
	while (curNode.parent != null) {
	    parents.add(curNode.parent);
	    curNode = curNode.parent;
	}
	return parents;
    }

    public Node parent() {
	return parent;
    }

    public void parent(Node newParent) {
	this.parent = newParent;
    }

    public void root(Root newtree) {
	treeRef = newtree;
	// recursively change tree
	for (Node child : childs())
	    child.root(newtree);
    }

    public void addChild(Node genericTree, int position) {
	childs.add(position, genericTree);
    }

    public void addChild(Node genericTree) {
	childs.add(genericTree);
    }

    public int deleteChild(Node genericTree) {
	int pos = childs.indexOf(genericTree);
	if (pos >= 0)
	    childs.remove(pos);
	return pos;
    }

    protected void unvisitChilds() {
	for (Node child : childs)
	    // if (child.parentsVisited())
	    child.visited = false;
    }

    /**
     * returns true if all children visited (condition to trigger onExit)
     */
    protected boolean visitedChilds() {
	boolean ret = true;
	for (Node child : childs)
	    ret &= child.visited;
	return ret;
    }

}
