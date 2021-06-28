package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.ProgramNode;

public class TreeStructureException extends GException {

    public TreeStructureException(ErrorCode error, ProgramNode node, Object[] params) {
	super(error, node, params);
    }

    public TreeStructureException(ErrorCode error, ProgramNode node) {
	super(error, node);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -2578418450781573569L;

}
