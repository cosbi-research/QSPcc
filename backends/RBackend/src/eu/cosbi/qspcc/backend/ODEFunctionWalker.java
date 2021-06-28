package eu.cosbi.qspcc.backend;

import java.util.HashMap;
import java.util.Map;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGFunctionalListener;
import eu.cosbi.qspcc.exceptions.GException;

//@formatter:off
/**
 * Changes the variables definition within tree for C-specific needs.
 * Does two things:
 * 
 * add environment parameter nodes 
 * ex. f(a) { c+1+a } becomes f(c,a) {c+1+a} 
 * ex. f(1) becomes f(c,1)
 * 
 * add persistent to the local, dynamically allocated variables,
 * so that the memory will be persisted between function calls
 * 
 * add CONTAINS_SPARSE_SUBEXPRESSION and SPARSE_EXPR on expression subtrees
 * 
 * @author tomasoni
 *
 */
//@formatter:on
public class ODEFunctionWalker implements DAGFunctionalListener<AAST, AASTNode> {
    Map<String, AASTNode> anonymousToODEFunctionMap = new HashMap<String, AASTNode>();

    public ODEFunctionWalker() {
	anonymousToODEFunctionMap = new HashMap<>();
    }

    @Override
    public void on(AAST aast, AASTNode node) throws GException {
	switch (node.type()) {
	case AT:
	    if (node.hasAttr(NodeAttr.REF_FUNCTION))
		anonymousToODEFunctionMap.put(node.child(NodeType.ID).name(),
			(AASTNode) node.attr(NodeAttr.REF_FUNCTION));
	    else if (node.hasAttr(NodeAttr.REF_CORE_FUNCTION))
		anonymousToODEFunctionMap.put(node.child(NodeType.ID).name(),
			(AASTNode) node.attr(NodeAttr.REF_CORE_FUNCTION));
	    break;
	case FUNCTION:
	    if (node.hasAttr(NodeAttr.FUNCTION_TYPE) && node.attr(NodeAttr.FUNCTION_TYPE).equals(NodeType.AT)) {
		if (node.hasAttr(NodeAttr.APPLY_FUNCTION))
		    anonymousToODEFunctionMap
			    .put(((AASTNode) node.attr(NodeAttr.APPLY_FUNCTION)).child(NodeType.ID).name(), node);
	    }
	    break;
	}
    }

    public Map<String, AASTNode> getOdeFunctionMap() {
	return anonymousToODEFunctionMap;
    }
}
