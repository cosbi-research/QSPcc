package eu.cosbi.qspcc.backend;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGFunctionalListener;
import eu.cosbi.qspcc.exceptions.GException;

public class IdWalker implements DAGFunctionalListener<AAST, AASTNode> {

    @Override
    public void on(AAST aast, AASTNode node) throws GException {

	if (node.type().equals(NodeType.ID)) {
	    if (node.name().contains("_")) {
		Object o = node.attr(NodeAttr.SYMBOL);
		node.attr(NodeAttr.SYMBOL, node.name().replaceAll("_", "\\."));
		return;
	    }
	}
	return;

    }

}
