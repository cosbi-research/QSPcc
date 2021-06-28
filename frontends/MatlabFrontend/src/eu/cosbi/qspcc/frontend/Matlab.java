package eu.cosbi.qspcc.frontend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.frontend.corefunction.M2CFunction;
import eu.cosbi.qspcc.frontend.corefunction.M2RFunction;
import eu.cosbi.qspcc.frontend.errors.MatlabParsingErrorMessage;
import eu.cosbi.qspcc.interfaces.CompilerFrontend;
import eu.cosbi.qspcc.interfaces.annotations.FrontendModule;
import eu.cosbi.utils.Tuple;

@FrontendModule(name = "matlab")
public class Matlab extends CompilerFrontend {
    private Logger logger = LogManager.getLogger(Matlab.class);
    Program matlabProgram;
    // the keys are the external files.
    // the values are the nodes that contains the definition
    // of the functions that the file needs.
    Map<String, Set<AASTNode>> externalFunctionCalls;
    Set<String> parsedFunctions;

    public Matlab(String programPath, Map<String, List<String>> frontendSpecificOptions) {
	super(programPath);
	externalFunctionCalls = new LinkedHashMap<String, Set<AASTNode>>();
	parsedFunctions = new HashSet<String>();
    }

    @Override
    public Program walk(IFunction[] coreFuns, List<Tuple<GType, String>> gvars) throws SyntaxException, ListException {
	matlabProgram = new Program(coreFuns, gvars);
	List<Tuple<String, AAST>> files = new ArrayList<Tuple<String, AAST>>();
	files.add(new Tuple<String, AAST>(mainScript, null));
	walkHelper(files);
	return matlabProgram;
    }

    private void walkHelper(List<Tuple<String, AAST>> files) throws SyntaxException, ListException {
	List<Tuple<String, AAST>> newfiles = new ArrayList<Tuple<String, AAST>>();
	for (Tuple<String, AAST> file : files) {
	    // avoid loops..
	    if (parsedFunctions.contains(file.first()))
		continue;
	    MatlabCompilationUnit compilationUnit = new MatlabCompilationUnit(matlabProgram, file.first());
	    if (compilationUnit.hasUnrecoverableSyntaxError())
		throw new SyntaxException(MatlabParsingErrorMessage.SYNTAX_ERROR_PARSING_TREE, null,
			compilationUnit.syntaxError());
	    AAST parent = file.second();
	    AAST aast = compilationUnit.walk(parent);
	    if (parent != null)
		parent.addChild(aast);

	    // initialize map
	    externalFunctionCalls.put(file.first(), new HashSet<AASTNode>());
	    parsedFunctions.add(file.first());
	    // this is the main compilation unit
	    if (aast.sourcePath().equals(mainScript))
		matlabProgram.mainCompilationUnit(aast);
	    // other files to parse
	    Map<String, Set<AASTNode>> externalRequirements = compilationUnit.subFiles();
	    for (String subFile : externalRequirements.keySet()) {
		// join values
		externalFunctionCalls.merge(subFile, externalRequirements.get(subFile),
			new BiFunction<Set<AASTNode>, Set<AASTNode>, Set<AASTNode>>() {
			    @Override
			    public Set<AASTNode> apply(Set<AASTNode> t, Set<AASTNode> u) {
				t.addAll(u);
				return t;
			    }
			});
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
	    return M2CFunction.values();
	else if ("r".equalsIgnoreCase(backendClassName))
	    return M2RFunction.values();
	else if ("mist".equalsIgnoreCase(backendClassName))
	    return M2CFunction.values();
	else {
	    logger.warn("Missing global functions table for frontend 'matlab' and backend '" + backendClassName + "'");
	    return new IFunction[] {};
	}
    }

}
