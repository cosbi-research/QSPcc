package eu.cosbi.qspcc.interfaces;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;

public abstract class CompilerBackend implements CompilerModule {
    public static String NL = "\n";
    public static String TAB = "\t";
    protected Path basePath;
    protected String outputFilePath;
    // main body
    protected StringBuffer translation;
    protected String destFolder;
    protected Map<String, List<String>> backendSpecificOptions;
    protected Integer indent;
    protected IFunction[] coreFunctions;
    protected boolean is_maincompilationunit;
    protected CompilerBackend maincompilationunit;
    protected String defineVariables;
    protected String programName;
    protected boolean dockerized;
    protected String functionalEntryPoint;

    public CompilerBackend(IFunction[] coreFunctions, String mainSourcePath, String destFolder, String defineVariables,
	    Integer indent, Map<String, List<String>> backendSpecificOptions, boolean dockerized) {
	this.is_maincompilationunit = false;
	maincompilationunit = null;
	this.dockerized = dockerized;
	this.defineVariables = defineVariables;
	this.coreFunctions = coreFunctions;
	this.indent = indent;
	this.destFolder = destFolder;
	// create if it does not exists
	new File(destFolder).mkdirs();

	this.backendSpecificOptions = backendSpecificOptions;
	this.basePath = Paths.get(mainSourcePath).getParent();
	programName = mainSourcePath.replaceFirst(".*/(\\w+).*", "$1");
	this.outputFilePath = mainPath().resolve(programName + "." + extension()).toString();
	translation = new StringBuffer();
    }

    public boolean isMainCompilationUnit() {
	return is_maincompilationunit;
    }

    public void incrementIndentLevel() {
	indent++;
    }

    public void decrementIndentLevel() {
	indent--;
    }

    // general
    public Path mainPath() {
	return Paths.get(destFolder);
    }

    public Integer indent() {
	return indent;
    }

    public String indentTabs(Integer indent) {
	StringBuffer s = new StringBuffer();
	for (int i = 0; i < indent; ++i)
	    s.append(TAB);
	return s.toString();
    }

    public String indentTabs() {
	return indentTabs(indent);
    }

    public Path outputFilePath() {
	return Paths.get(outputFilePath);
    }

    public void markAsMainCompilationUnit() {
	is_maincompilationunit = true;
    }

    public void setMainCompilationUnit(CompilerBackend main) {
	maincompilationunit = main;
    }

    public CompilerBackend getMainCompilationUnit() {
	return maincompilationunit;
    }

    public void setFunctionalEntryPoint(String funName) {
	functionalEntryPoint = funName;
    }

    public String getFunctionalEntryPoint() {
	return functionalEntryPoint;
    }

    public abstract String extension();

    /**
     * Start flushing to disk output files, prior call to translate()
     */
    public abstract void prepareOutput();

    /**
     * End flushing to disk output files, after translate() and genCode() completes
     * See here: https://github.com/cosbi-research/qsp-cc/blob/develop/backends/CBackend/src/eu/cosbi/translator/backend/C.java#L11697
     */
    public abstract void finalizeOutput();

    /**
     * This is called to perform code translation. 
     * Usually it walks the **AAST** (or compilation unit) bottom-up and fills inner structures for the subsequent genCode() call. 
     * See an example here: backends/CBackend/src/eu/cosbi/translator/backend/C.java#L316
     * @param aast
     * @param stopOnError
     * @throws GException
     */
    public abstract void translate(AAST aast, boolean stopOnError) throws GException;

    /**
     * This is called after translate() and writes the results of the translate() method to the output files.
     * @param program
     * @throws TypeException
     * @throws UnboundException
     * @throws SyntaxException
     */
    public abstract void genCode(AAST program) throws TypeException, UnboundException, SyntaxException;

    /**
     * This is called just before code generation starts. 
     * It's called once in a single backend instance, the one that refers to the main entry point of the program.
     * On this instance `isMainCompilationUnit() == true`.
     * It can be used to initialize *global* structures and *global* variables, 
     * see an example here: backends/CBackend/src/eu/cosbi/translator/backend/C.java#L12104
     * 
     * @param programName
     * @throws Exception
     */
    public abstract void onTranslationStart(Program programName) throws Exception;

    /**
     * This is called after code generation. 
     * It's called once in a single backend instance, the one that refers to the main entry point of the program.
     * On this instance `isMainCompilationUnit() == true`.
     * It can be used to clean up *global* variables, 
     * see an example here: backends/CBackend/src/eu/cosbi/translator/backend/C.java#L12240
     * 
     * @param programName
     * @param fun_names
     * @throws Exception
     */
    public abstract void onTranslationEnd(Program programName, List<String> fun_names) throws Exception;
}
