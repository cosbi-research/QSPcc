package eu.cosbi.qspcc.exceptions;

public class StackError extends AASTError {

    public StackError(String message, StackTraceElement[] stacktrace) {
	super(message, stacktrace);
    }

}
