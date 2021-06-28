package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.ProgramNode;

/**
 * Generic exception. Exception messages are handled through ErrorCodes that
 * each module can define by implementing the ErrorCode interface.
 * 
 * The stringify method is used to get a strig that can be shown to the user
 * (via logger or other mechanisms)
 * 
 * @author tomasoni
 *
 */
public class GException extends Exception implements Comparable<GException> {
    private static final long serialVersionUID = -7265386083849787839L;
    ErrorCode error;
    Object[] params;
    private ProgramNode node;

    public GException(ErrorCode error, ProgramNode node, Object... params) {
	super();
	this.error = error;
	this.node = node;
	this.params = params;
    }

    public ErrorCode errorType() {
	return error;
    }

    @Override
    public int compareTo(GException o2) {
	if (node() == null && o2.node() == null)
	    return 0;
	else if (node() == null && o2.node() != null)
	    return 1;
	else if (node() != null && o2.node() == null)
	    return -1;
	else
	    return node().compareTo(o2.node());
    }

    public void resetParams(Object... params) {
	this.params = params;
    }

    public GException(Throwable cause) {
	super(cause);
    }

    public String stringify() {
	if (error != null)
	    return error.stringify(node, params);
	if (getCause() instanceof GException) {
	    GException ge = (GException) getCause();
	    return ge.stringify();
	} else {
	    return "Unhandled code exception: " + getCause().getMessage();
	}
    }

    public ErrorCode code() {
	return error;
    }

    public void refNode(ProgramNode node) {
	this.node = node;
    }

    public ProgramNode node() {
	return node;
    }

    @Override
    public String getMessage() {
	return stringify();
    }

    /**
     * add to this exception a cause
     * 
     * @param e
     */
    public void addCause(GException e) {
	e.node().setException(e);
    }
}
