package eu.cosbi.qspcc.start;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

import eu.cosbi.logger.configurator.LoggerConfiguratorFactory;
import eu.cosbi.qspcc.CompilerController;
import eu.cosbi.reflection.ReflectionUtils;
import eu.cosbi.utils.Constants;

public class Starter {
    public static void main(String[] args) throws IOException {
	String translatingMachine = null;
	String installdir = null;
	boolean dockerized = false;

	String logdir = System.getProperty("user.dir");
	// initialize logger programmatically
	LoggerConfiguratorFactory.setInstallPath(logdir);
	ConfigurationFactory.setConfigurationFactory(new LoggerConfiguratorFactory());

	// READ GLOBAL ENV CONFIGURATION transparent to user
	String[] userargs;
	if (args.length >= 3) {
	    translatingMachine = args[0];
	    installdir = args[1];
	    dockerized = "yes".equals(args[2]);

	    if (args.length == 3) {
		userargs = new String[args.length - 3];
		System.arraycopy(args, 3, userargs, 0, args.length - 3);
	    } else {
		// Specifies the base package name (ex. com.cosbi) of language front ends
		// or back ends to prefer over standard modules, if available.
		// Front end and back end modules are matched by name.
		String fqn = args[3];
		if (fqn != null)
		    // load extra modules, useful to extend existing frontend/backend functionality
		    ReflectionUtils.preferModulesByFQN(fqn);

		userargs = new String[args.length - 4];
		System.arraycopy(args, 4, userargs, 0, args.length - 4);
	    }
	} else
	    userargs = args;

	Options options = new Options();

	/*
	 * Option input = new Option("v", "define-variables", true,
	 * "Allows to specify variables that should be treated as globally defined. This allows to overcome undefined variables issues while translating, and force QSPcc to generate the code also in presence of undefined symbols. Beware that variables defined this way should be defined after the translation and before the execution of the generated code."
	 * ); input.setArgName("VARS"); input.setRequired(false);
	 * options.addOption(input);
	 */

	List<String> frontendNames = ReflectionUtils.getFrontendNames();

	Option from = new Option("f", "from", true,
		"Specifies the source language. Supported language options for <LANG> are: "
			+ String.join(", ", frontendNames) + " (case-insensitive).");
	from.setArgName("LANG");
	from.setRequired(true);
	options.addOption(from);

	List<String> backednNames = ReflectionUtils.getBackendNames();

	Option to = new Option("t", "to", true,
		"Specifies the target language. Supported language options for <LANG> are: "
			+ String.join(", ", backednNames) + " (case-insensitive).");
	to.setArgName("LANG");
	to.setRequired(true);
	options.addOption(to);

	Option source = new Option("s", "source", true,
		"The path to the main source file to be translated. References to external files belonging in the same folder will be automatically detected and translated.");
	source.setArgName("MAIN-SOURCE-FILE");
	source.setRequired(true);
	options.addOption(source);

	Option dest = new Option("d", "dest", true, "The destination folder.");
	dest.setRequired(true);
	dest.setArgName("DESTINATION-FOLDER");
	options.addOption(dest);

	Option aastonly = new Option("ao", "aast", false,
		"Prints out only the aast in svg format and doesn't write the code. Requires two extra jars to be in the classpath: graphviz-java and the system-specific binding (j2v8_linux_*.jar on linux)");
	aastonly.setRequired(false);
	options.addOption(aastonly);

	// load frontends and backends options
	List<Option> frontendOptions = ReflectionUtils.getFrontendOptions();
	for (Option o : frontendOptions)
	    options.addOption(o);

	List<Option> backendOptions = ReflectionUtils.getBackendOptions();
	for (Option o : backendOptions)
	    options.addOption(o);

	CommandLineParser parser = new DefaultParser();
	HelpFormatter formatter = new HelpFormatter();
	formatter.setWidth(120);
	CommandLine cmd;

	try {
	    cmd = parser.parse(options, userargs);
	} catch (ParseException e) {
	    formatter.printHelp(Constants.programName, options, true);
	    System.exit(1);
	    return;
	}

	// String defineVariables = cmd.getOptionValue("define-variables");
	String defineVariables = null;
	String fromLanguage = cmd.getOptionValue("from");
	String toLanguage = cmd.getOptionValue("to");
	String mainSourcePath = cmd.getOptionValue("source");
	String destFolder = cmd.getOptionValue("dest");
	boolean aastOnly = cmd.hasOption("aast");

	Map<String, List<String>> outBackendOptions = new HashMap<String, List<String>>(backendOptions.size());
	Map<String, List<String>> outFrontendOptions = new HashMap<String, List<String>>(frontendOptions.size());
	for (Option ro : cmd.getOptions()) {
	    if (backendOptions.contains(ro))
		outBackendOptions.put(ro.getOpt(), ro.getValuesList());
	    else if (frontendOptions.contains(ro))
		outBackendOptions.put(ro.getOpt(), ro.getValuesList());
	}

	// ignore stdout/stderr

	/*
	 * System.setOut(new PrintStream(new OutputStream() {
	 * 
	 * @Override public void write(int b) throws IOException { // ignore
	 * 
	 * } }));
	 */
	// @formatter:off
/*
	System.setErr(new PrintStream(new OutputStream() {

	    public void write(int b) throws IOException { // ignore
	    }
	}));
*/
	// @formatter:on

	CompilerController c = new CompilerController(fromLanguage, toLanguage, defineVariables, mainSourcePath,
		destFolder, outBackendOptions, outFrontendOptions, translatingMachine, installdir, dockerized);
	try {
	    c.translate(aastOnly, false, false);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
