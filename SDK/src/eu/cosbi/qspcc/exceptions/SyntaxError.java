package eu.cosbi.qspcc.exceptions;

public class SyntaxError extends AASTError {

    public SyntaxError(String message, StackTraceElement[] stacktrace) {
	super(message, stacktrace);
    }

}
