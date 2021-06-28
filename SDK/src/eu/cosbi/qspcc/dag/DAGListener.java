package eu.cosbi.qspcc.dag;

import java.util.Deque;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.exceptions.GException;

/**
 * the AST is walked in a Depth-first way. onEnter is the first time the walk
 * hits the given node, onExit is when the complete sub-tree of the given node
 * has been visited.
 * 
 * @author tomasoni
 *
 */
public interface DAGListener<Root, Node extends DAG<Root, Node> & ProgramNode, T> {
    public void onEnter(Root aast, Node node) throws GException;

    /**
     * 
     * @param aastRef
     *            AAST we are walking
     * @param curNode
     *            current node we are exiting from
     * @param results
     *            stack of results that each node optionally can pop from and
     *            push to with the result of the computation it wants to pass
     *            back to the parents
     * @author tomasoni
     * @throws Exception
     */
    public void onExit(Root ref, Node curNode, Deque<T> results) throws GException;

    public void onWalkCompleted(Root ref) throws GException;

    public void onWalkStarted(Root ref) throws GException;

}
