package eu.cosbi.qspcc.frontend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.frontend.corefunction.R2CFunction;
import eu.cosbi.qspcc.interfaces.CompilerFrontend;
import eu.cosbi.qspcc.interfaces.annotations.FrontendModule;
import eu.cosbi.utils.Tuple;

@FrontendModule(name = "r")
public class R extends CompilerFrontend {
    private Logger logger = LogManager.getLogger(R.class);
    private Program grProgram;
    private ANTLRInputStream rProgram;
    private RParser.ProgContext ast;
    private RParser rparser;
    private RCompilationUnit mainCompilationUnit;
    private Set<String> parsedFunctions;

    public R(String programPath, Map<String, List<String>> frontendSpecificOptions) {
	super(programPath);
    }

    @Override
    public Program walk(IFunction[] coreFunctions, List<Tuple<GType, String>> gvars) throws SyntaxException {
	grProgram = new Program(coreFunctions, gvars);
	parsedFunctions = new HashSet<String>();
	List<Tuple<String, AAST>> files = new ArrayList<Tuple<String, AAST>>();
	files.add(new Tuple<String, AAST>(mainScript, null));
	walkHelper(files);
	return grProgram;
    }

    private void walkHelper(List<Tuple<String, AAST>> files) {
	List<Tuple<String, AAST>> newfiles = new ArrayList<Tuple<String, AAST>>();

	for (Tuple<String, AAST> file : files) {
	    // avoid loops..
	    if (parsedFunctions.contains(file.first()))
		continue;
	    RCompilationUnit compilationUnit = null;
	    compilationUnit = new RCompilationUnit(grProgram, file.first());
	    AAST parent = file.second();
	    AAST aast = compilationUnit.walk(parent);
	    if (parent != null)
		parent.addChild(aast);

	    // initialize map
	    parsedFunctions.add(file.first());
	    // this is the main compilation unit
	    if (aast.sourcePath().equals(mainScript))
		grProgram.mainCompilationUnit(aast);
	    // other files to parse
	    List<String> externalRequirements = compilationUnit.subFiles();
	    for (String subFile : externalRequirements) {
		newfiles.add(new Tuple<String, AAST>(subFile, aast));
	    }
	}

	if (!newfiles.isEmpty())
	    // re-do on the files imported
	    walkHelper(newfiles);
    }

    @Override
    public IFunction[] globalFunctionsFor(String backendClassName) {
	if ("c".equalsIgnoreCase(backendClassName))
	    return R2CFunction.values();
	return R2CFunction.values();
    }

}
