package eu.cosbi.qspcc.ast;

import java.util.List;

import eu.cosbi.qspcc.ast.attrs.Attr;
import eu.cosbi.qspcc.exceptions.GException;

/**
 * Program node is a generic node in a DAG. Implemented by AAST and AASTNode
 * 
 * @author tomasoni
 *
 */
public interface ProgramNode extends Comparable<ProgramNode> {
    /**
     * @return an unique identifier of this node
     */
    public String id();

    /**
     * @return the portion of code that this node represents
     */
    public String code();

    /**
     * @return the attribute of type Attr stored in this node, or null
     */
    public Object attr(Attr atype);

    /**
     * 
     * @return the id of the function that contains this statement, or null
     */
    public ProgramNode parentFunction();

    /**
     * 
     * @return parent of this node. automatically by parent() implemented if the
     *         class extends DAG
     */
    public ProgramNode ancestor();

    /**
     * 
     * @param p: a node
     * @return true if p is in the subtree with root this, false otherwise
     */
    public boolean ancestorOf(ProgramNode p);

    /**
     * 
     * @return childs of this node. automatically implemented by childs() if the
     *         class extends DAG
     */
    public List<ProgramNode> descendants();

    /**
     * 
     * @return child of this node of the given type. automatically implemented by
     *         child() if the class extends DAG
     */
    public ProgramNode descendant(NodeType t);

    /**
     * @return the location of the code that this node represents
     */
    public String location();

    /**
     * 
     * @return the source path of the file containing this node
     */
    public String sourcePath();

    /**
     * 
     * @return the type of the given node.
     */
    public NodeType type();

    /**
     * 
     * @return the type of the given node as a string. Useful for printing only.
     */
    public String typeAsString();

    /**
     * 
     * @return the minimal top-node that allows to understand the context where the
     *         node is used
     */
    public ProgramNode nearestExpression();

    /**
     * 
     * @param e the exception to associate to this node. Useful to gather causes of
     *          top-level exceptions
     */
    public void setException(GException e);

}
