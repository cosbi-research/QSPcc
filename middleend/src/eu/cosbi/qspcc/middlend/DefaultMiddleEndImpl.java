package eu.cosbi.qspcc.middlend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.MiddleEnd;
import eu.cosbi.qspcc.interfaces.MiddleEndPass;
import eu.cosbi.qspcc.tree.listeners.FrontEndWalker;
import eu.cosbi.qspcc.tree.listeners.FunctionReferencesWalker;
import eu.cosbi.qspcc.tree.listeners.FunctionsNormalizerWalker;
import eu.cosbi.qspcc.tree.listeners.FunctionsWalker;
import eu.cosbi.qspcc.tree.listeners.StatementsWalker;
import eu.cosbi.qspcc.tree.listeners.VariablesWalker;

public class DefaultMiddleEndImpl implements MiddleEnd {
    Logger logger = LogManager.getLogger(MiddleEndPass.class);
    public static int MAX_PASS_EQUAL = 100;
    private Program program;
    private boolean functionsToBeReparsed = false;
    private ListException unrecoverable_errors;
    private GType defaultFunctionInputsType;
    // incomplete statement iteration counter
    private int is = 1;
    private String functionalEntryPoint;
    // % calculated_parameters_Liver_Tissue_Plasma MATRIX[83, 1] of SCALAR
    private static Pattern funTypePattern = Pattern.compile(
	    "(\\w+)\\s+(MATRIX\\[\\s*(?:[0-9]+|INT)(?:\\s*,\\s*(?:[0-9]+|INT))*\\s*\\]\\s+of\\s+(?:SCALAR|INT|BOOL)|SCALAR|INT|BOOL)");
    private static Pattern pend = Pattern.compile("==END==");

    public DefaultMiddleEndImpl(Program program) {
	this.program = program;
	this.unrecoverable_errors = new ListException(ErrorMessage.LIST_OF_ERRORS);
	defaultFunctionInputsType = GType.get(BType.STRUCT).setInput(null);
    }

    @Override
    public void defaultMainFunctionInputsType(GType defaultType) {
	defaultFunctionInputsType = defaultType;
    }

    @Override
    public boolean annotate(IFunction[] coreFunctions, boolean stopOnError) {
	try {
	    firstPass(coreFunctions, stopOnError);
	} catch (GException e) {
	    logger.debug("First-pass error translating program tree: " + e.getMessage(), e);
	    return false;
	}
	// if (unrecoverable_errors.isEmpty() && !program.walkNeeded() &&
	// !program.statementWalkNeeded())
	// this means that the entry point is a function with parameters.
	// define their function types using special ### FUNCTION ### syntax.
	try {
	    defineMainFunctionTypes(stopOnError);
	} catch (GException e) {
	    logger.debug("function header parsing error translating program tree: " + e.getMessage(), e);
	    return false;
	}
	// 2..N th pass to resolve function body and return value
	int i = 2;
	int i_eq = 1;
	Set<AASTNode> incompleteFunctions = null;
	while (unrecoverable_errors.isEmpty() && program.walkNeeded() && i_eq < MAX_PASS_EQUAL) {
	    Set<AASTNode> oldIncompleteFunctions = new HashSet<AASTNode>();
	    for (AAST compilationUnit : program.incompleteFunctions())
		oldIncompleteFunctions.addAll(compilationUnit.incompleteFunctionStatements());

	    incompleteFunctions = new HashSet<AASTNode>(oldIncompleteFunctions);
	    // add something only the first time to let me enter the while
	    oldIncompleteFunctions.add(new AASTNode(null, null, null));
	    while (unrecoverable_errors.isEmpty() && program.walkNeeded()
		    && !equals(oldIncompleteFunctions, incompleteFunctions)) {
		try {
		    List<String> ifstr = new ArrayList<String>(incompleteFunctions.size());
		    for (AASTNode ifun : incompleteFunctions)
			ifstr.add(ifun.child(NodeType.ID).name());
		    logger.debug("############## " + i + "th functions block iteration, missing functions "
			    + String.join(", ", ifstr) + " ################");
		    program.mainCompilationUnit().walkOnExit((Program ref, AAST curFile) -> {
			MiddleEndPass functionsWalker;

			if (curFile.walkNeeded()) {
			    LinkedList<AASTNode> fileIncompleteFunctions = (LinkedList<AASTNode>) curFile
				    .incompleteFunctionStatements();
			    List<String> iffstr = new ArrayList<String>(fileIncompleteFunctions.size());
			    for (AASTNode ifun : fileIncompleteFunctions)
				iffstr.add(ifun.child(NodeType.ID).name());
			    logger.debug("Resolving functions " + String.join(", ", iffstr) + " (in reverse order) in "
				    + curFile.sourcePath());
			    // reverse order of iteration evaluate inner functions first
			    Iterator<AASTNode> reverseIt = fileIncompleteFunctions.descendingIterator();
			    while (reverseIt.hasNext()) {
				AASTNode funNode = reverseIt.next();
				functionsWalker = new FunctionsWalker(curFile, funNode);
				try {
				    functionsWalker.annotate(stopOnError);
				} catch (GException e) {
				    curFile.addError(e);
				    unrecoverable_errors.add(e);
				    if (stopOnError)
					throw e;
				    logger.debug("Error resolving function types: " + e.getMessage(), e);
				}
			    }
			}

		    }, stopOnError);
		    i++;
		} catch (GException e) {
		    logger.debug("Second-pass error translating program tree: " + e.getMessage(), e);
		    return false;
		}

		// update missing functions
		oldIncompleteFunctions = incompleteFunctions;
		incompleteFunctions = new HashSet<>();
		for (AAST compilationUnit : program.incompleteFunctions())
		    incompleteFunctions.addAll(compilationUnit.incompleteFunctionStatements());
	    }

	    if (unrecoverable_errors.isEmpty() && program.statementWalkNeeded())
		walkIncompleteStatements(stopOnError);

	    i_eq++;
	}

	try {
	    lastPass(stopOnError);
	} catch (GException e) {
	    logger.debug("Last-pass error translating program tree: " + e.getMessage(), e);
	    return false;
	}
	return unrecoverable_errors.isEmpty() && !program.walkNeeded();
    }

    private void walkIncompleteStatements(boolean stopOnError) {
	try {
	    // if some statements left to be resolved
	    logger.debug("############## " + is++ + "th statements iteration ################");
	    program.mainCompilationUnit().walkOnEnter((Program ref, AAST curFile) -> {
		MiddleEndPass statementsWalker = new StatementsWalker(curFile);

		if (curFile.statementWalkNeeded())
		    try {
			statementsWalker.annotate(stopOnError);
		    } catch (GException e) {
			curFile.addError(e);
			unrecoverable_errors.add(e);
			if (stopOnError)
			    throw e;
			logger.debug("Error resolving function types: " + e.getMessage(), e);
		    }
	    }, stopOnError);
	} catch (GException e) {
	    logger.debug("Second-pass error translating program tree: " + e.getMessage(), e);
	}
    }

    private void defineMainFunctionTypes(boolean stopOnError) throws GException {
	// no clear entry point for this program.
	// this program WON'T compile in standalone mode, it will need a binding with an
	// external software
	// to be executed (like MEX matlab integration) or another script written by the
	// user to provide the needed inputs
	AAST mainCompilationUnit = program.mainCompilationUnit();
	// get top-level function
	AASTNode functionNode = null;
	Map<String, AASTNode> mainfunctionsMap = mainCompilationUnit.functions();
	Collection<AASTNode> functions = mainfunctionsMap.values();
	int toplevel = -1;
	for (AASTNode otherFun : functions) {
	    int candidateLevel = otherFun.parentDepth(NodeType.PROGRAM);
	    if (toplevel == -1 || candidateLevel < toplevel) {
		functionNode = otherFun;
		toplevel = candidateLevel;
	    }
	}
	if (functionNode == null) {
	    logger.debug(new GException(ErrorMessage.USER_MAIN_IS_NOT_FUNCTION,
		    mainCompilationUnit.rootNode().childs().get(0)).stringify());
	    return;
	}

	String functionName = functionNode.child(NodeType.ID).name();
	AASTNode stmt_list = functionNode.parent();
	List<AASTNode> params = functionNode.childs(NodeType.PARAMETER_LIST);
	List<AASTNode> outputs = functionNode.childs(NodeType.FUNCTION_RETURN);

	// search in stmt list comments that specify the input parameters
	// and fill the map varname -> vartype
	Map<String, GType> funInputTypes = new HashMap<String, GType>(params.size());
	boolean read_type_information = false;
	Pattern pstart = Pattern.compile("==FUNCTION==\\s+\"" + functionName + "\"");
	Matcher m = null;
	for (AASTNode child : stmt_list.childs()) {
	    if (child.type().equals(NodeType.LINECOMMENT))
		if (pstart.matcher(child.name()).find()) {
		    // found beginning of type information
		    read_type_information = true;
		} else if (pend.matcher(child.name()).find()) {
		    // found beginning of type information
		    read_type_information = false;
		} else if (read_type_information) {
		    // read types
		    m = funTypePattern.matcher(child.name());
		    if (m.find()) {
			String varName = m.group(1);
			String varType = m.group(2);
			varType = varType.replaceAll("\\s+", " ");
			varType = varType.replaceAll("^\\s|\\s$", "");
			funInputTypes.put(varName, GType.get(varName, varType));
		    }
		}
	}

	// define his input types
	GType[] funInputs = new GType[params.size()];
	GType[] funOutputs = new GType[outputs.size()];
	boolean rewalk = true;
	for (int k = 0; k < params.size(); ++k) {
	    AASTNode param = params.get(k);
	    if (funInputTypes.containsKey(param.name())) {
		funInputs[k] = funInputTypes.get(param.name());
		// always clone type
		param.expr(GType.get(funInputs[k]));
	    } else {
		rewalk = false;
		break;
		// throw new TypeException(ErrorMessage.MAIN_FUNCTION_INPUT_TYPES_UNDEFINED,
		// param, param.name());
	    }
	}

	if (rewalk) {
	    // define function type and re-walk
	    for (int k = 0; k < outputs.size(); ++k) {
		funOutputs[k] = GType.get(BType.UNKNOWN);
	    }
	    // define function type
	    functionNode.expr(GType.get(BType.FUNCTION, funInputs, funOutputs));
	    functionalEntryPoint = functionName;
	    // re-do walk on first compilation unit
	    MiddleEndPass mainMiddleEnd = new FrontEndWalker(mainCompilationUnit);
	    try {
		mainMiddleEnd.annotate(stopOnError);
	    } catch (GException e) {
		mainCompilationUnit.addError(e);
		unrecoverable_errors.add(e);
		logger.debug("Error resolving types: " + e.getMessage(), e);
		if (stopOnError)
		    throw e;
	    }
	}
    }

    protected void firstPass(IFunction[] coreFunctions, boolean stopOnError) throws GException {
	Map<String, AASTNode> globalFunctionNodes = new HashMap<String, AASTNode>();
	try {
	    program.mainCompilationUnit().walkOnExit((Program ref, AAST curFile) -> {
		// first-pass in topological order
		// from the function without requirements up to the main program
		FunctionsNormalizerWalker anonymousCleaner;
		MiddleEndPass mainMiddleEnd, envAnnotator;
		anonymousCleaner = new FunctionsNormalizerWalker(curFile, coreFunctions);

		try {
		    anonymousCleaner.annotate(stopOnError);
		} catch (GException e) {
		    curFile.addError(e);
		    unrecoverable_errors.add(e);
		    logger.debug("Error uniforming function definitions: " + e.getMessage(), e);
		    if (stopOnError)
			throw e;
		}
		// update list of functions found till now
		globalFunctionNodes.putAll(anonymousCleaner.functionNodes());

		envAnnotator = new VariablesWalker(curFile, globalFunctionNodes, coreFunctions);
		try {
		    envAnnotator.annotate(stopOnError);
		} catch (GException e) {
		    curFile.addError(e);
		    unrecoverable_errors.add(e);
		    logger.debug("Error discriminating local variables from environment variables: " + e.getMessage(),
			    e);
		    if (stopOnError)
			throw e;
		}

		mainMiddleEnd = new FrontEndWalker(curFile);

		try {
		    mainMiddleEnd.annotate(stopOnError);
		} catch (GException e) {
		    curFile.addError(e);
		    unrecoverable_errors.add(e);
		    logger.debug("Error resolving types: " + e.getMessage(), e);
		    if (stopOnError)
			throw e;
		}
	    }, stopOnError);
	} catch (GException e) {
	    logger.debug("First-pass error translating program tree: " + e.getMessage(), e);
	    throw e;
	}
    }

    protected void lastPass(boolean stopOnError) throws GException {
	// be sure that the function types are propagated to every corresponding ID node
	try {
	    program.mainCompilationUnit().walkOnExit((Program ref, AAST curFile) -> {
		// first-pass in topological order
		// from the function without requirements up to the main program
		FunctionReferencesWalker frefs = new FunctionReferencesWalker(curFile, ref.functions());

		try {
		    frefs.annotate(stopOnError);
		} catch (GException e) {
		    curFile.addError(e);
		    unrecoverable_errors.add(e);
		    logger.debug("Error propagating function types: " + e.getMessage(), e);
		    if (stopOnError)
			throw e;
		}

	    }, stopOnError);
	} catch (GException e) {
	    logger.debug("Last-pass error translating program tree: " + e.getMessage(), e);
	    throw e;
	}
    }

    public <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
	Set<T> tmp = new HashSet<T>();
	for (T x : setA)
	    if (setB.contains(x))
		tmp.add(x);
	return tmp;
    }

    public <T> boolean equals(Set<T> setA, Set<T> setB) {
	Set<T> tmp = new HashSet<T>(setA);
	tmp.removeAll(setB);

	Set<T> tmp2 = new HashSet<T>(setB);
	tmp2.removeAll(setA);

	return tmp.isEmpty() && tmp2.isEmpty();
    }

    @Override
    public String getFunctionalEntryPoint() {
	return functionalEntryPoint;
    }

}
