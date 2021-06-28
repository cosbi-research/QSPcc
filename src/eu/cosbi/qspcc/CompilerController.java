package eu.cosbi.qspcc;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.exceptions.OutputException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.expressions.complextype.StructDefinition;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerBackend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.MiddleEnd;
import eu.cosbi.qspcc.interfaces.MiddleEndPass;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.reflection.ReflectionUtils;
import eu.cosbi.utils.Constants;
import eu.cosbi.utils.ProgramUtils;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

public class CompilerController {
    private Logger logger = LogManager.getLogger(CompilerController.class);

    String programName = null;
    Path mainPath = null;
    private Path destPath = null;

    private CompilerFrontend mainCodeTranslator;

    private String defineVariables;

    private String fromLanguage;

    private String toLanguage;

    private String mainSourcePath;

    private String destFolder;

    private Map<String, List<String>> backendSpecificOptions, frontendSpecificOptions;

    // in the future we can also define different middle-ends
    private String middleEndClassName = "DefaultMiddleEndImpl";

    private Set<String> fun_names;
    private Map<AAST, Tuple<MiddleEndPass, CompilerBackend>> translators;
    private IFunction[] backendCoreFunctions;
    CompilerBackend mainBackend;
    private String middleEndFunctionsClassName;
    String translatingMachine = null;
    String installdir = null;
    boolean dockerized = false;

    private int translation_errors;

    public CompilerController(String fromLanguage, String toLanguage, String defineVariables, String mainSourcePath,
	    String destFolder, Map<String, List<String>> backendSpecificOptions,
	    Map<String, List<String>> frontendSpecificOptions, String translatingMachine, String installdir,
	    boolean dockerized) {
	this.translatingMachine = translatingMachine;
	this.installdir = installdir;
	this.dockerized = dockerized;

	this.fromLanguage = fromLanguage;
	this.toLanguage = toLanguage;
	Constants.fromLanguage = fromLanguage;
	Constants.toLanguage = toLanguage;
	this.defineVariables = defineVariables;

	this.mainSourcePath = mainSourcePath;
	this.destFolder = destFolder;
	this.destPath = Paths.get(destFolder);
	if (backendSpecificOptions != null)
	    this.backendSpecificOptions = backendSpecificOptions;
	else
	    this.backendSpecificOptions = new HashMap<String, List<String>>();
	if (frontendSpecificOptions != null)
	    this.frontendSpecificOptions = frontendSpecificOptions;
	else
	    this.frontendSpecificOptions = new HashMap<String, List<String>>();

	Path spath = Paths.get(mainSourcePath);
	this.mainPath = spath.getParent();
	if (mainPath == null)
	    mainPath = Paths.get(System.getProperty("user.dir"));
	this.programName = spath.getFileName().toString().replaceFirst("(\\w+)\\.[a-z]{1,3}", "$1");
	logger.debug("main source path: " + mainSourcePath);
	logger.debug("main path: " + mainPath);
	logger.debug("program name: " + programName);
	fun_names = new HashSet<String>();

	translators = new HashMap<AAST, Tuple<MiddleEndPass, CompilerBackend>>();
	CompilerFrontend frontend = ReflectionUtils.getFrontendInstance(fromLanguage, mainSourcePath,
		frontendSpecificOptions);

	mainCodeTranslator = frontend;
    }

    // command line execution
    public CompilerController(String fromLanguage, String toLanguage, String inputFile, String mainSourcePath,
	    String destFolder, Map<String, List<String>> backendSpecificOptions,
	    Map<String, List<String>> frontendSpecificOptions) {
	this(fromLanguage, toLanguage, inputFile, mainSourcePath, destFolder, backendSpecificOptions,
		frontendSpecificOptions, null, null, false);
    }

    public void translate() throws Exception {
	translate(false, false, false);
    }

    /**
     * actually translate the program
     * 
     * @throws Exception
     */
    public void translate(boolean onlyAAST, boolean testing, boolean stopAnnotationOnError) throws Exception {
	translate(onlyAAST, testing, stopAnnotationOnError, true);
    }

    public boolean translate(boolean onlyAAST, boolean testing, boolean stopAnnotationOnError, boolean finalMessage)
	    throws Exception {

	int curyear = LocalDate.now().getYear();
	logger.info(String.format("%s: Copyright 2021%s, COSBI\nTranslating project %s\n", Constants.programName,
		(curyear == 2021) ? "" : "-" + Integer.toString(curyear), Paths.get(mainSourcePath).getParent()));

	if (mainCodeTranslator == null) {
	    GException globalErrors = new GException(ErrorMessage.USER_UNKNOWN_FRONTEND, null, fromLanguage);
	    logger.fatal(globalErrors.stringify());
	    throw globalErrors;
	}
	Symbols.resetSymbols();
	Program program = null;
	Set<String> gvars = getInputGlobalVariables();
	// threat global variables from input file as 1D matrix variables
	List<Tuple<GType, String>> tgvars = new ArrayList<Tuple<GType, String>>();
	for (String gvar : gvars)
	    tgvars.add(
		    new Tuple<GType, String>(GType
			    .get(BType.MATRIX, gvar, GType.get(BType.SCALAR),
				    new IntType[] {
					    (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(gvar, 1)) })
			    .setInput(gvar), gvar));
	// initialized just to call iterfunctions
	logger.debug("Frontend " + fromLanguage + " is building AST..");
	// clean complex structures definitions
	StructDefinition.clear(toLanguage);
	backendCoreFunctions = mainCodeTranslator.globalFunctionsFor(toLanguage);
	// set structures defined during computation of types for global functions as to be kept among different translate calls
	StructDefinition.initDefaultValues(toLanguage);
	try {
	    program = mainCodeTranslator.walk(backendCoreFunctions, tgvars);
	} catch (SyntaxException | ListException e) {
	    logger.fatal(e.stringify());
	    if (e.getCause() != null)
		logger.fatal("Cause: " + e.getCause().getMessage());

	    logger.debug("Fatal error translating main script", e);
	    throw e;
	} catch (Exception e) {
	    logger.fatal("Unexpected code error: " + e.getMessage(), e);
	    throw e;
	}

	MiddleEnd mainMiddleEnd = ReflectionUtils.getMiddleEndInstance(middleEndClassName, program);
	if (mainMiddleEnd == null) {
	    GException globalErrors = new GException(ErrorMessage.USER_UNKNOWN_MIDDLEEND, null);
	    logger.fatal(globalErrors.stringify());
	    throw globalErrors;
	}
	logger.debug("Middleend is building AAST..");
	boolean ok = mainMiddleEnd.annotate(backendCoreFunctions, stopAnnotationOnError);

	logger.debug(" === Structures defined === ");
	for (StructDefinition struct : program.userStructures()) {
	    logger.debug(struct.toString());
	}
	logger.debug(" === Structures defined end === ");

	if (!ok) {
	    // error in middle-end stop here and report to user the errors/warnings
	    GException globalErrors = new GException(ErrorMessage.FINAL_REPORTING_TEMPLATE, null, program);
	    logger.error(globalErrors.stringify());
	    throw globalErrors;
	}

	Map<String, Set<GException>> warnings = new HashMap<String, Set<GException>>();
	if (onlyAAST) {
	    logger.info("Generating graphical AAST..");
	    if (!testing) {
		String mainSourcePath = program.mainCompilationUnit().sourcePath();

		try {
		    ProgramUtils.toSvg(program, Paths.get(mainSourcePath).getParent());
		} catch (Exception e) {
		    logger.error(e.getMessage());
		    logger.debug("Error writing AAST svg: " + e.getMessage(), e);
		    throw e;
		}
	    }
	    logger.info("Generating graphical AAST completed.");
	} else {
	    // serialize
	    logger.debug("Backend is serializing AAST..");
	    translation_errors = 0;
	    try {
		mainBackend = ReflectionUtils.getBackendInstance(toLanguage, backendCoreFunctions,
			program.mainCompilationUnit().sourcePath(), destFolder, defineVariables, 1,
			backendSpecificOptions, dockerized);
		if (mainBackend == null) {
		    logger.error(new GException(ErrorMessage.USER_UNKNOWN_BACKEND, null, toLanguage).stringify());
		    return false;
		}

		mainBackend.markAsMainCompilationUnit();
		mainBackend.setFunctionalEntryPoint(mainMiddleEnd.getFunctionalEntryPoint());
		mainBackend.onTranslationStart(program);

		program.mainCompilationUnit().walkOnExit((Program p, AAST curFile) -> {
		    CompilerBackend backend;

		    if (curFile.equals(p.mainCompilationUnit())) {
			backend = mainBackend;
		    } else {
			backend = ReflectionUtils.getBackendInstance(toLanguage, backendCoreFunctions,
				curFile.sourcePath(), destFolder, defineVariables, 1, backendSpecificOptions,
				dockerized);
			backend.setMainCompilationUnit(mainBackend);
		    }

		    backend.prepareOutput();

		    try {
			backend.translate(curFile, stopAnnotationOnError);
			backend.genCode(curFile);
		    } catch (GException e) {
			curFile.addError(e);
			translation_errors += 1;
			if (e.getCause() != null)
			    logger.error("Cause: " + e.getCause().getMessage());
			logger.debug("Error writing code: " + e.stringify(), e);
		    }
		    // store warnings
		    warnings.put(curFile.sourcePath(), curFile.warnings());
		    backend.finalizeOutput();
		}, stopAnnotationOnError);
	    } catch (GException e) {
		translation_errors += 1;
		logger.fatal(e.stringify());
		if (e.getCause() != null)
		    logger.fatal("Cause: " + e.getCause().getMessage());
		logger.debug("Error serializing program tree: " + e.getMessage(), e);
		throw e;
	    } catch (Exception e) {
		translation_errors += 1;
		logger.fatal(
			"Failed error initializing translation, do you have write permission on the destination folder?");
		if (e.getCause() != null)
		    logger.fatal("Cause: " + e.getCause().getMessage());
		logger.debug("Error serializing program tree: " + e.getMessage(), e);
		throw e;
	    }

	    // copy templates
	    try {
		fun_names.addAll(program.userFunctionNames());
		mainBackend.onTranslationEnd(program, new ArrayList<String>(fun_names));
	    } catch (Exception e) {
		translation_errors += 1;
		logger.error(e.getMessage());
		logger.debug("Error serializing program tree: " + e.getMessage(), e);
		throw e;
	    }

	    // copy input files if present (.csv or .mat files)
	    for (File f : mainPath.toFile().listFiles(new FileFilter() {
		@Override
		public boolean accept(File pathname) {
		    return pathname.isFile()
			    && (pathname.getName().endsWith(".mat") || pathname.getName().endsWith(".csv"));
		}
	    }))
		try {
		    FileUtils.copyFile(f, destPath.resolve(f.getName()).toFile());
		} catch (Exception e) {
		    logger.error(new OutputException(ErrorMessage.USER_ERROR_COPYING_INPUT_FILES, null, f).stringify());
		}
	}

	String languageDependent = "";
	if (toLanguage.toLowerCase().equals("c") || toLanguage.toLowerCase().equals("mist")) {
	    languageDependent = "Type 'make' to compile and run the generated program '" + programName + "'";
	} else if (toLanguage.toLowerCase().equals("r")) {
	    languageDependent = "Run the generated program '" + programName + ".R' with Rscript";
	}

	if (translation_errors > 0) {
	    GException globalErrors = new GException(ErrorMessage.FINAL_REPORTING_TEMPLATE, null, program);
	    logger.error(globalErrors.stringify());
	    throw globalErrors;
	} else if (finalMessage) {
	    if (!warnings.isEmpty()) {
		logger.warn(new GException(ErrorMessage.LIST_OF_WARNINGS, null, warnings).stringify());
	    }
	    logger.info("Translation to " + toLanguage.toUpperCase() + " completed successfully.\nGo to " + destFolder
		    + ".\n" + languageDependent);
	}
	return true;
    }

    private Set<String> getInputGlobalVariables() {
	Set<String> gvars = new HashSet<String>();
	if (defineVariables == null)
	    return gvars;
	for (String v : defineVariables.split(","))
	    gvars.add(v);
	return gvars;
    }

}
