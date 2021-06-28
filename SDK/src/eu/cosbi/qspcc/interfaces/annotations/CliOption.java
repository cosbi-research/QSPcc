package eu.cosbi.qspcc.interfaces.annotations;

public @interface CliOption {
    String opt();

    String longOpt();

    boolean hasArg();

    String description();

    String argName();

    boolean required();
}
