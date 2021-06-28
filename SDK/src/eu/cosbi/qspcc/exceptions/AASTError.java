package eu.cosbi.qspcc.exceptions;

public class AASTError {
    private StackTraceElement[] stacktrace;
    private String message;

    public AASTError(String message, StackTraceElement[] stacktrace) {
	this.stacktrace = stacktrace;
	this.message = message;
    }

    public String message() {
	return new String(message);
    }
}
