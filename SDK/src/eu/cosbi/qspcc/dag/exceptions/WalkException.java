package eu.cosbi.qspcc.dag.exceptions;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;

public class WalkException extends GException {

    /**
     * 
     */
    private static final long serialVersionUID = 6144991878425350061L;

    public WalkException(ProgramNode curNode, Exception e) {
	super(ErrorMessage.INTERNAL_TREE_WALK_EXCEPTION, curNode, e);
    }

    public WalkException(AASTNode node) {
	super(ErrorMessage.INTERNAL_TREE_WALK_EXCEPTION, node);
    }

}
