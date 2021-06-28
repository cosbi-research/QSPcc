package eu.cosbi.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.interfaces.CompilerBackend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.MiddleEnd;
import eu.cosbi.qspcc.interfaces.annotations.BackendModule;
import eu.cosbi.qspcc.interfaces.annotations.CliOption;
import eu.cosbi.qspcc.interfaces.annotations.CliOptions;
import eu.cosbi.qspcc.interfaces.annotations.FrontendModule;

public class ReflectionUtils {
    static Logger logger = LogManager.getLogger(ReflectionUtils.class);
    static String COSBI_FQN = "eu.cosbi.qspcc";
    static Set<Class<? extends MiddleEnd>> vmiddleEnds = new Reflections(COSBI_FQN).getSubTypesOf(MiddleEnd.class);
    static Set<Class<?>> vbackEnds = new Reflections(COSBI_FQN).getTypesAnnotatedWith(BackendModule.class, true);
    static Set<Class<?>> vfrontEnds = new Reflections(COSBI_FQN).getTypesAnnotatedWith(FrontendModule.class, true);
    static String preferredFQN = COSBI_FQN;

    /**
     * finds (and prefers, when the modules have the same name)
     * extra modules (other extensions written with SDK)
     * based on another fully qualified domain name
     * @param fqn
     */
    public static void preferModulesByFQN(String fqn) {
	Set<Class<?>> ebackEnds = new Reflections(fqn).getTypesAnnotatedWith(BackendModule.class, true);
	preferredFQN = fqn;
	// add new backends to vbackEnds, but remove pre-defined cosbi modules in case of name clash
	// only one module per name is allowed
	for (Class<?> nbackend : ebackEnds)
	    vbackEnds.add(nbackend);

	Set<Class<?>> efrontEnds = new Reflections(fqn).getTypesAnnotatedWith(FrontendModule.class, true);
	for (Class<?> nfront : efrontEnds)
	    vfrontEnds.add(nfront);

    }

    public static CompilerBackend getBackendInstance(String toLanguage, IFunction[] backendCoreFunctions,
	    String mainSourcePath, String destFolder, String inputFile, int indent,
	    Map<String, List<String>> backendSpecificOptions, boolean dockerized) {
	CompilerBackend backend = null;
	Class<?> found = null;
	for (Class<?> clazzBack : vbackEnds) {
	    BackendModule module = clazzBack.getAnnotation(BackendModule.class);

	    if (module.name().toLowerCase().equals(toLanguage.toLowerCase())) {
		found = clazzBack;
		if (clazzBack.getName().toLowerCase().startsWith(preferredFQN))
		    // use this one.. otherwise store it but continue scanning to look for the correct one (if exists)
		    break;
	    }
	}

	try {
	    Constructor<?> constructorBack = found.getConstructor(IFunction[].class, String.class, String.class,
		    String.class, Integer.class, Map.class, boolean.class);
	    backend = (CompilerBackend) constructorBack.newInstance(backendCoreFunctions, mainSourcePath, destFolder,
		    inputFile, indent, backendSpecificOptions, dockerized);
	    return backend;
	} catch (NullPointerException e) {
	    logger.error(
		    "Error: cannot find a backend constructor for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (NoSuchMethodException e) {
	    logger.error(
		    "Error: cannot find a backend constructor for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (SecurityException e) {
	    logger.error("Error: SecurityException for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (InstantiationException e) {
	    logger.error("Error: InstantiationException for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (IllegalAccessException e) {
	    logger.error("Error: IllegalAccessException for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (IllegalArgumentException e) {
	    logger.error("Error: IllegalArgumentException for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (InvocationTargetException e) {
	    logger.error("Error: InvocationTargetException for language '" + toLanguage + "': " + e.getMessage());
	    System.exit(1);
	}
	return backend;
    }

    public static CompilerFrontend getFrontendInstance(String fromLanguage, String constructorArg,
	    Map<String, List<String>> frontendSpecificOptions) {
	CompilerFrontend frontend = null;
	Class<?> found = null;
	for (Class<?> clazzFront : vfrontEnds) {
	    FrontendModule module = clazzFront.getAnnotation(FrontendModule.class);
	    if (module.name().toLowerCase().equals(fromLanguage.toLowerCase())) {
		found = clazzFront;
		if (clazzFront.getName().toLowerCase().startsWith(preferredFQN))
		    // use this one.. otherwise store it but continue scanning to look for the correct one (if exists)
		    break;
	    }
	}

	try {
	    Constructor<?> constructorFront = found.getConstructor(String.class, Map.class);
	    frontend = (CompilerFrontend) constructorFront.newInstance(constructorArg, frontendSpecificOptions);
	} catch (NullPointerException e) {
	    logger.error(
		    "Error: cannot find a backend constructor for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (NoSuchMethodException e) {
	    logger.error("Error: cannot find constructor for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (SecurityException e) {
	    logger.error("Error: SecurityException for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (InstantiationException e) {
	    logger.error("Error: InstantiationException for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (IllegalAccessException e) {
	    logger.error("Error: IllegalAccessException for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (IllegalArgumentException e) {
	    System.err
		    .println("Error: IllegalArgumentException for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	} catch (InvocationTargetException e) {
	    System.err
		    .println("Error: InvocationTargetException for language '" + fromLanguage + "': " + e.getMessage());
	    System.exit(1);
	}

	return frontend;
    }

    public static MiddleEnd getMiddleEndInstance(String middleEndName, Program program) {
	MiddleEnd translator = null;

	for (Class<?> clazzT : vmiddleEnds) {
	    if (clazzT.getSimpleName().toLowerCase().equals(middleEndName.toLowerCase()))
		try {
		    Constructor<?> constructorT = clazzT.getConstructor(Program.class);
		    translator = (MiddleEnd) constructorT.newInstance(program);
		} catch (NoSuchMethodException e) {
		    logger.error("Error: cannot find constructor for middle end: " + e.getMessage());
		    System.exit(1);
		} catch (SecurityException e) {
		    logger.error("Error: SecurityException for middle end: " + e.getMessage());
		    System.exit(1);
		} catch (InstantiationException e) {
		    logger.error("Error: InstantiationException for middle end: " + e.getMessage());
		    System.exit(1);
		} catch (IllegalAccessException e) {
		    logger.error("Error: IllegalAccessException for middle end: " + e.getMessage());
		    System.exit(1);
		} catch (IllegalArgumentException e) {
		    logger.error("Error: IllegalArgumentException for middle end: " + e.getMessage());
		    System.exit(1);
		} catch (InvocationTargetException e) {
		    logger.error("Error: InvocationTargetException for middle end: " + e.getMessage());
		    System.exit(1);
		}
	}
	return translator;
    }

    public static List<Option> getFrontendOptions() {
	return getFrontendOptions(vfrontEnds);
    }

    private static <T> List<Option> getFrontendOptions(Set<Class<? extends T>> classes) {
	List<Option> frontOptions = new LinkedList<Option>();
	for (Class<?> clazzFront : classes) {
	    CliOptions options = clazzFront.getAnnotation(CliOptions.class);
	    FrontendModule module = clazzFront.getAnnotation(FrontendModule.class);
	    String cname = module.name();
	    if (options != null && options.value() != null)
		for (CliOption option : options.value()) {
		    Option o = new Option(option.opt(), cname.toLowerCase() + "-" + option.longOpt(), option.hasArg(),
			    "(" + cname + " target) " + option.description());
		    o.setRequired(option.required());
		    o.setArgName(option.argName());
		    frontOptions.add(o);
		}
	}
	return frontOptions;
    }

    public static List<String> getFrontendNames() {
	return getFrontendModuleNames(vfrontEnds);
    }

    private static <T> List<String> getFrontendModuleNames(Set<Class<? extends T>> classes) {
	List<String> frontOptions = new LinkedList<String>();
	for (Class<?> clazzFront : classes) {
	    FrontendModule module = clazzFront.getAnnotation(FrontendModule.class);
	    frontOptions.add(module.name());
	}
	return frontOptions;
    }

    public static List<Option> getBackendOptions() {
	return getBackendOptions(vbackEnds);
    }

    private static <T> List<Option> getBackendOptions(Set<Class<? extends T>> classes) {
	List<Option> frontOptions = new LinkedList<Option>();
	for (Class<?> clazzFront : classes) {
	    CliOptions options = clazzFront.getAnnotation(CliOptions.class);
	    BackendModule module = clazzFront.getAnnotation(BackendModule.class);
	    String cname = module.name();
	    if (options != null && options.value() != null)
		for (CliOption option : options.value()) {
		    Option o = new Option(option.opt(), cname.toLowerCase() + "-" + option.longOpt(), option.hasArg(),
			    "(" + cname + " target) " + option.description());
		    o.setRequired(option.required());
		    o.setArgName(option.argName());
		    frontOptions.add(o);
		}
	}
	return frontOptions;
    }

    public static List<String> getBackendNames() {
	return getBackendModuleNames(vbackEnds);
    }

    private static <T> List<String> getBackendModuleNames(Set<Class<? extends T>> classes) {
	List<String> frontOptions = new LinkedList<String>();
	for (Class<?> clazzFront : classes) {
	    BackendModule module = clazzFront.getAnnotation(BackendModule.class);
	    frontOptions.add(module.name());
	}
	return frontOptions;
    }

}
