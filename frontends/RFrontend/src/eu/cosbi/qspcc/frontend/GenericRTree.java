package eu.cosbi.qspcc.frontend;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;

public class GenericRTree extends AASTNode {

    public GenericRTree(AAST aast, NodeType type, String statement, Integer line, Integer charPosition,
	    AASTNode parent) {
	super(aast, type, statement, line, charPosition, parent);
    }

    public GenericRTree(AAST aast, NodeType type, String statement) {
	super(aast, type, statement);
    }

}
