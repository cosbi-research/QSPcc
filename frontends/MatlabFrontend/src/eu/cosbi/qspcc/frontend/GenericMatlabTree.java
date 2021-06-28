package eu.cosbi.qspcc.frontend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;

public class GenericMatlabTree extends AASTNode {
    private Logger logger = LogManager.getLogger(GenericMatlabTree.class);

    public GenericMatlabTree(MatlabAAST tree, NodeType type, String statement, Integer line, Integer charPosition,
	    AASTNode parent) {
	super(tree, type, statement, line, charPosition, parent);
	if (type.equals(NodeType.STATEMENT_LIST))
	    tree.statementList(this);
    }

    public GenericMatlabTree(AAST tree, NodeType type, String statement) {
	super(tree, type, statement);
    }

    public void changeParent(AASTNode newnode) {
	this.parent = newnode;
    }

}
