package eu.cosbi.qspcc.backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.backend.errors.RErrorMessage;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.MissingNodeException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TreeStructureException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IndexType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.SliceType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerBackend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.annotations.BackendModule;
import eu.cosbi.qspcc.interfaces.annotations.StepType;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

@BackendModule(name = "r")
public class R extends CompilerBackend implements DAGListener<AAST, AASTNode, String> {

    private Logger logger = LogManager.getLogger(R.class);
    public static final String LHS_VECTOR_SEPARATOR = "$";
    private boolean writingODE = false;
    List<StringBuffer> functionBufferList = new ArrayList<StringBuffer>();
    // tuple symbols already defined/buffer
    List<Tuple<Set<AASTNode>, StringBuffer>> buffers = new LinkedList<>();
    protected AASTNode skipTranslationUntil = null;
    protected static Set<String> skip_function_names = null;

    List<String> internalFunctionNames = new ArrayList<String>();
    Path mainSourceFolder;
    List<String> extLibraries = new ArrayList<String>();
    static Map<String, AASTNode> anonymousToODEFunctionMap;
    static Set<String> userFunctionNames = new HashSet<String>();
    static Program program;
    static Set<String> structName = new HashSet<String>();
    static Set<String> globalVars = new HashSet<String>();
    boolean writingGlobal = false;

    boolean writingATFunction = false;
    int indentLevel = 0;

    static Map<String, String> dict = new HashMap<String, String>() {
	{
	    put("RelTol", "rtol");
	    put("AbsTol", "atol");
	    // put("Jacobian", "jacfunc");
	    // put("JPattern", "jacfunc");
	    put("MaxStep", "hmax");
	    put("Stats", "verbose");
	    put("InitialStep", "hini");
	    put("MaxOrder", "maxords");
	    put("nearest", "constant");
	    put("linear", "linear");
	    put("previous", "constant");
	    put("next", "constant, f=1");
	    put("r", "red");
	    put("g", "green");
	    put("b", "blue");
	    put("m", "magenta");
	}
    };

    Set<String> supportedODESolvers = new HashSet<String>() {
	{
	    add("ode15i");
	    add("ode15s");
	    add("ode23");
	    add("ode23s");
	    add("ode45");
	    add("ode45s");
	}
    };

    Map<String, Map<String, String>> par_dictionary = new HashMap<String, Map<String, String>>();

    public R(IFunction[] coreFunctions, String mainSourcePath, String destFolder, String inputFile, Integer indent,
	    Map<String, List<String>> backendSpecificOptions, boolean dockerized) {
	super(coreFunctions, mainSourcePath, destFolder, inputFile, indent, backendSpecificOptions, dockerized);
	mainSourceFolder = Paths.get(mainSourcePath).getParent();

	int start = mainSourcePath.lastIndexOf(File.separator) + 1;
	int end = mainSourcePath.lastIndexOf(".");
	String outputName = mainSourcePath.substring(start, end);
	this.outputFilePath = destFolder + File.separator + outputName + "." + extension();
    }

    @Override
    public String extension() {
	return "R";
    }

    @Override
    public void prepareOutput() {
	// TODO Auto-generated method stub

    }

    @Override
    public void finalizeOutput() {
    }

    @Override
    public void onTranslationStart(Program program) throws Exception {
	userFunctionNames = new HashSet<String>();
	skip_function_names = new HashSet<String>();
	this.program = program;

	// get map of at functions -> at function name
	ODEFunctionWalker odefun = new ODEFunctionWalker();
	program.mainCompilationUnit().walkOnEnter((Program p, AAST curFile) -> {
	    if (!curFile.equals(program.mainCompilationUnit()))
		userFunctionNames.add(curFile.name());
	    curFile.rootNode().walkOnEnter(odefun, true);
	}, true);

	anonymousToODEFunctionMap = odefun.getOdeFunctionMap();
    }

    @Override
    public void onTranslationEnd(Program programName, List<String> fun_names) throws Exception {

    }

    @Override
    public void translate(AAST aast, boolean stopOnError) throws GException {
	// aast.rootNode().walkOnEnter(new IdWalker(), stopOnError);
	buffers.add(new Tuple<>(new HashSet<>(), translation));

	aast.rootNode().walkFor(this, stopOnError);
    }

    @Override
    public void genCode(AAST program) throws TypeException, UnboundException, SyntaxException {

	// empty row for nice formatting
	translation.insert(0, "\n");

	for (StringBuffer sb : functionBufferList) {
	    translation.insert(0, sb + "\n");
	}

	if (functionBufferList.size() > 0) {
	    translation.insert(0, "########## functions ##########\n");
	    translation.insert(0, "\n");
	}

	if (this.is_maincompilationunit) {
	    for (String fun : userFunctionNames) {
		if (!internalFunctionNames.contains(fun) && !fun.startsWith("at_")
			&& !skip_function_names.contains(fun))
		    translation.insert(0, "source(\"" + fun + ".R\")\n");
	    }

	    if (userFunctionNames.size() > 0) {
		translation.insert(0, "########## source ##########\n");
		translation.insert(0, "\n");
	    }
	}
	for (String lib : extLibraries) {
	    translation.insert(0, "library(" + lib + ")\n");
	}

	if (extLibraries.size() > 0) {
	    translation.insert(0, "########## libraries ##########\n");
	    translation.insert(0, "\n");
	}

	// Get the file reference
	Path path = Paths.get(outputFilePath);

	try {
	    BufferedWriter writer;
	    writer = Files.newBufferedWriter(path);

	    // outputHeaders(writer, path);
	    // writeGlobals(writer);
	    // writeGlobalStructures(swriter);

	    outputMainCode(writer, program, path);

	    writer.close();
	} catch (IOException e) {
	    logger.error("Error writing output program: " + e.getMessage());
	    logger.debug("Error writing output program: " + e.getMessage(), e);
	}

    }

    @Override
    public void onEnter(AAST aast, AASTNode curNode) throws GException {

	List<String> child_results;
	switch (curNode.type()) {

	case PARFOR:
	    logger.warn("Parallel implementation of for not available, fallback to normal for loop at "
		    + curNode.location() + " of " + curNode.compilationUnit().sourcePath());
	case IF:
	case WHILE:
	case FOR:
	    StringBuffer clauseBuffer = new StringBuffer();
	    buffers.add(new Tuple<Set<AASTNode>, StringBuffer>(new HashSet<>(), clauseBuffer));
	    indentLevel++;
	    break;

	case ELSEIF:
	    clauseBuffer = new StringBuffer();
	    buffers.add(new Tuple<Set<AASTNode>, StringBuffer>(new HashSet<>(), clauseBuffer));
	    break;
	case ELSE:
	    getCurrentBuffer().append(indentString() + "} else {\n");
	    indentLevel++;
	    break;
	case GLOBAL:
	    writingGlobal = true;
	    break;
	case FUNCTION:

	    String funcName = curNode.child(NodeType.ID).name();
	    if (!curNode.hasAttr(NodeAttr.FUNCTION_RESOLVED) || !((Boolean) curNode.attr(NodeAttr.FUNCTION_RESOLVED))) {
		skipTranslationUntil = curNode;
		String funName = curNode.child(NodeType.ID).name();
		logger.warn("Skipping translation of unused function '" + funName + "'");
		skip_function_names.add(funName);
		return;
	    }

	    if (curNode.hasAttr(NodeAttr.FUNCTION_TYPE))
		if (curNode.attr(NodeAttr.FUNCTION_TYPE).equals(NodeType.AT)) {
		    userFunctionNames.remove(curNode.attr(NodeAttr.SYMBOL));
		    writingATFunction = true;
		}

	    StringBuffer newBuffer = new StringBuffer();
	    Set<AASTNode> env = new HashSet<>();
	    for (AASTNode p : curNode.childs(NodeType.PARAMETER_LIST))
		env.add(TypeUtils.getIDNode(p));

	    // add name to functions defined inside the current file
	    internalFunctionNames.add(funcName);

	    newBuffer.append(indentString() + funcName + " <- function(");
	    List<String> params = new ArrayList<>(0);
	    List<String> withoutApplyParams;
	    List<AASTNode> applyEnvParams;
	    if (anonymousToODEFunctionMap.containsKey(funcName)) {
		AASTNode atNode = anonymousToODEFunctionMap.get(funcName);
		if (atNode.hasAttr(NodeAttr.FUNCTION_PARAMS))
		    params = (List<String>) atNode.attr(NodeAttr.FUNCTION_PARAMS);
		if (atNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS))
		    applyEnvParams = (List<AASTNode>) atNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);
		else
		    applyEnvParams = new ArrayList<>(0);
		withoutApplyParams = new ArrayList<>(params.size());
		for (String p : params) {
		    boolean found = false;
		    for (AASTNode e : applyEnvParams)
			if (e.name().equals(p)) {
			    found = true;
			    break;
			}
		    // add only parameters that are not in applyEnvParams
		    if (!found)
			withoutApplyParams.add(p);
		}

		newBuffer.append(String.join(",", withoutApplyParams));
		newBuffer.append(",parameters");
	    } else {
		if (curNode.hasAttr(NodeAttr.FUNCTION_PARAMS))
		    params = (List<String>) curNode.attr(NodeAttr.FUNCTION_PARAMS);
		newBuffer.append(String.join(",", params));
		if (writingATFunction)
		    newBuffer.append(",parameters");
	    }

	    newBuffer.append(") {\n");

	    indentLevel++;
	    AASTNode parameterListNode = curNode.child(NodeType.PARAMETER_LIST);
	    List<AASTNode> parameterNodes = parameterListNode.childs();

	    for (AASTNode p : parameterNodes)
		env.add(TypeUtils.getIDNode(p));

	    if (anonymousToODEFunctionMap.containsKey(funcName)) {
		AASTNode atNode = anonymousToODEFunctionMap.get(funcName);
		int j = 1;
		// define parameters that are env params of at function, but regular params of the inner function
		if (atNode.hasAttr(NodeAttr.APPLY_FUNCTION)) {
		    // actual params of inner function
		    AASTNode applyNode = (AASTNode) atNode.attr(NodeAttr.APPLY_FUNCTION);
		    List<AASTNode> actualParams = applyNode.childs(NodeType.FUNCTION_PARAMETER_LIST);
		    // env params of at function
		    if (atNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS)) {
			List<AASTNode> atEnvParams = (List<AASTNode>) atNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);
			// match actual params with atEnvParams, and define the corresponding formal param
			for (int i = 0; i < actualParams.size(); ++i) {
			    AASTNode p = actualParams.get(i);
			    for (AASTNode atEnvParam : atEnvParams)
				if (p.name().contains(atEnvParam.name())) {
				    // p is an env param of at function, take the corresponding formal param and define it
				    AASTNode fp = parameterNodes.get(i);
				    newBuffer.append(indentString() + fp.name() + " <- parameters[[" + (j++) + "]]\n");
				}
			}
		    }
		}
		// define env parameters of the inner function
		if (curNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS))
		    for (AASTNode envp : (List<AASTNode>) atNode.attr(NodeAttr.FUNCTION_ENV_PARAMS)) {
			newBuffer.append(indentString() + envp.name() + " <- parameters[[" + (j++) + "]]\n");
		    }
		// define parameters defined by the function that calls this function reference
		if (atNode.hasAttr(NodeAttr.FUNCTION_APPLY_ENV_PARAMS))
		    for (AASTNode envp : (List<AASTNode>) atNode.attr(NodeAttr.FUNCTION_APPLY_ENV_PARAMS)) {
			newBuffer.append(indentString() + envp.name() + " <- parameters[[" + (j++) + "]]\n");
		    }
	    }

	    buffers.add(new Tuple<Set<AASTNode>, StringBuffer>(env, newBuffer));
	    return;
	case APPLY:

	    if (curNode.childExists(NodeType.ID)) {
		writingODE = supportedODESolvers.contains(curNode.child(NodeType.ID).name());
		if (writingODE && !extLibraries.contains("deSolve"))
		    extLibraries.add("deSolve");
	    }
	    return;
	default:
	    return;

	}

    }

    @Override
    public void onExit(AAST ref, AASTNode curNode, Deque<String> results) throws GException {

	List<String> child_results;

	if (skipTranslationUntil != null) {
	    if (curNode.equals(skipTranslationUntil)) {
		skipTranslationUntil = null;
	    }
	    // skip translation
	    return;
	}

	switch (curNode.type()) {
	case PROGRAM:
	case EXPRESSION:
	case ID_NODE:
	case LHS:
	case RHS:
	case NL:
	case SEMI:
	case FUNCTION_RETURN:
	case PARAMETER_LIST:
	case STATEMENT_LIST:
	case ELSE:
	case HOLD:
	case CLOSE:
	case GRID:

	    // do nothing don't change stack
	    return;

	case AT:
	    return;
	case CLEARVARS:
	    child_results = getChildResults(results, curNode.childs().size());
	    getCurrentBuffer().append("rm(list=c(\"" + String.join("\",\"", child_results) + "\"))\n");
	    return;
	case RETURNS:
	    AASTNode curFunction = (AASTNode) curNode.parent(NodeType.FUNCTION);
	    if (curFunction.hasAttr(NodeAttr.FUNCTION_OUTPUT)) {
		ArrayList<String> ret = (ArrayList<String>) curFunction.attr(NodeAttr.FUNCTION_OUTPUT);
		String retStr = String.join(",", ret);
		retStr = "list(" + retStr + ")";
		getCurrentBuffer().append(indentString() + "return(" + retStr + ")\n");
	    } else
		throw new TreeStructureException(RErrorMessage.FUNCTION_WITHOUT_OUTPUTS, curNode);
	    return;
	case FUNCTION_PARAMETER_LIST:
	    return;
	case VOID:
	    results.push("");
	    return;
	case LINECOMMENT:
	    child_results = getChildResults(results, curNode.childs().size());

	    getCurrentBuffer().append(indentString() + translateComment(curNode.name()));
	    return;

	case GASSIGN:
	case ASSIGN:
	    child_results = getChildResults(results, curNode.childs().size() - 1);
	    List<AASTNode> lhs = (List<AASTNode>) curNode.attr(NodeAttr.LHS);
	    AASTNode rhs = (AASTNode) TypeUtils.getIDNode(curNode.childs(NodeType.RHS).get(0));
	    boolean rhsIsOde = rhs.type().equals(NodeType.APPLY) && rhs.childExists(NodeType.ID)
		    && supportedODESolvers.contains(rhs.child(NodeType.ID).name());
	    boolean splittableRhs = rhs.expr().isCastableToMatrix()
		    || (rhsIsOde && rhs.expr().type().equals(BType.STRUCT));
	    // String rhs_symbol = (String)rhs.attr(NodeAttr.SYMBOL);
	    Map<AASTNode, List<AASTNode>> lhs_params = (Map<AASTNode, List<AASTNode>>) curNode
		    .attr(NodeAttr.LHS_PARAMS);

	    List<String> lhs_symbols = new ArrayList<String>();
	    for (AASTNode lhsNode : lhs) {
		lhs_symbols.add(lhsNode.name());
	    }
	    String adhoc_var;
	    if (lhs.size() > 1)
		adhoc_var = String.join("_", lhs_symbols);
	    else
		adhoc_var = child_results.get(0);
	    for (int i = 0; i < lhs.size(); i++) {

		AASTNode id = TypeUtils.getIDNode(lhs.get(i));
		String rhs_symbol = child_results.get(child_results.size() - 1);
		String single_lhs_name = adhoc_var;
		// if multiple assignment, define first as matrix, then get rows
		// for assignments
		if (lhs.size() > 1) {
		    if (i == 0) {

			getCurrentBuffer().append(indentString()
				+ translateAssign(id, adhoc_var, lhs_params, rhs, rhs_symbol, curNode.type()));
			if (splittableRhs)
			    rhs_symbol = adhoc_var + "[," + (i + 1);
			else
			    rhs_symbol = adhoc_var + "[[" + (i + 1);
		    }

		    else if (i == lhs.size() - 1) {
			if (splittableRhs)
			    rhs_symbol = adhoc_var + "[," + "seq(" + (i + 1) + ",ncol(" + adhoc_var + "))";
			else
			    rhs_symbol = adhoc_var + "[[" + "seq(" + (i + 1) + ",length(" + adhoc_var + "))";
		    }

		    else {
			if (splittableRhs)
			    rhs_symbol = adhoc_var + "[," + (i + 1);
			else
			    rhs_symbol = adhoc_var + "[[" + (i + 1);
		    }
		    if (splittableRhs)
			rhs_symbol += "]";
		    else
			rhs_symbol += "]]";
		    single_lhs_name = id.name();
		}

		getCurrentBuffer().append(indentString()
			+ translateAssign(id, single_lhs_name, lhs_params, rhs, rhs_symbol, curNode.type()));
	    }

	    return;
	case ID:

	    // skip all
	    if (curNode.name().equals("all") || curNode.name().equals("on")) {
		return;
	    }
	    // translate pi
	    if (curNode.name().equals("pi")) {
		results.push("pi");
		return;
	    }
	    // translate inf
	    if (curNode.name().equals("inf")) {
		results.push("Inf");
		return;
	    }
	    // translate tic
	    if (curNode.name().equals("tic")) {
		results.push(translateTic());
		return;
	    }
	    // translate toc
	    if (curNode.name().equals("toc")) {
		results.push(translateToc());
		return;
	    }
	    // pwd
	    if (curNode.name().equals("pwd")) {
		results.push("getwd()");
		return;
	    }
	    // break
	    if (curNode.name().equals("break")) {
		results.push("break");
		return;
	    }

	    // avoid to write clear all
	    if (curNode.parent().type().equals(NodeType.CLEAR))
		return;

	    String name = curNode.name();

	    if (writingGlobal) {
		globalVars.add(name);
		return;
	    }
	    GType curType = curNode.expr();

	    if (!curNode.parentExists(NodeType.LHS) && curType != null && curType.equals(BType.STRUCT)
		    && curType.isInput() && !curType.isKnown()) {
		// define it as a load from input struct
		Tuple<Set<AASTNode>, StringBuffer> ctx = getCurrentContext();
		if (!alreadyDefined(ctx, curNode)) {
		    ctx.second().append(indentString()).append(name).append(" <- ")
			    .append(curType.inputName().replaceAll("_", ".")).append(NL);
		    define(ctx, curNode);
		}
	    }
	    results.push(name);
	    return;
	case APPLY:

	    if (curNode.hasAttr(NodeAttr.FUNCALL)) {

		// if parameter list, more children are needed
		if (curNode.childExists(NodeType.FUNCTION_PARAMETER_LIST)) {

		    child_results = getChildResults(results,
			    curNode.child(NodeType.FUNCTION_PARAMETER_LIST).childs().size() + 1);
		} else
		    child_results = getChildResults(results, curNode.childs().size());
		if (isCoreFunction(child_results.get(0))) {
		    String translation = translateCoreFunction(curNode, child_results.get(0),
			    child_results.subList(1, child_results.size()));
		    if (translation == null)
			throw new UndefinedTranslationException(RErrorMessage.CORE_FUNCTION_RETURNED_NULL_TRANSLATION,
				curNode, child_results.get(0));

		    results.push(translation);
		} else
		    results.push(translateFunctionCall(curNode, child_results));
	    } else if (curNode.hasAttr(NodeAttr.MATACCESS)) {

		// check if matrix has already been initialized, otherwise do it
		if (curNode.childExists(NodeType.ID)) {
		    AASTNode child = curNode.child(NodeType.ID);
		    Tuple<Set<AASTNode>, StringBuffer> ctx = getCurrentContext();
		    if (!alreadyDefined(ctx, child)) {
			IntType[] dims = ((MatrixType) child.expr()).dims();
			ArrayList<String> dimsValues = new ArrayList<String>();
			for (IntType type : dims) {
			    dimsValues.add(type.valueAsString());
			}
			ctx.second().append(indentString() + child.name() + " <- " + "array(NaN,c("
				+ String.join(",", dimsValues) + "))\n");
			define(ctx, child);
		    }
		}

		// check if children contains function_parameter_list, to count
		// how many parameters must be recovered from getChildresults
		int numOfResults = curNode.childs().size();
		if (curNode.childExists(NodeType.FUNCTION_PARAMETER_LIST)) {
		    AASTNode childNode = curNode.child(NodeType.FUNCTION_PARAMETER_LIST);
		    if (childNode.hasAttr(NodeAttr.PARAMS))

			numOfResults = ((ArrayList) childNode.attr(NodeAttr.PARAMS)).size();
		    else if (childNode.hasAttr(NodeAttr.MATRIX_PARAMS)) {
			AASTNode paramsNode = (AASTNode) childNode.attr(NodeAttr.MATRIX_PARAMS);

			if (paramsNode.hasAttr(NodeAttr.PARAMS))

			    numOfResults = ((ArrayList) paramsNode.attr(NodeAttr.PARAMS)).size();

		    }
		}
		child_results = getChildResults(results, numOfResults + 1);
		results.push(translateMatrixAccess(curNode, child_results));
	    }
	    return;
	case GLOBAL:
	    writingGlobal = false;
	    return;
	case INT:
	case FLOAT:
	    results.push(curNode.name());
	    return;

	case STRING:
	    results.push("\"" + curNode.name().replaceAll("'|\"", "") + "\"");
	    return;

	case END: // END2
	    if (!curNode.parentExists(NodeType.FUNCTION_PARAMETER_LIST))
		throw new UndefinedTranslationException(RErrorMessage.TRANSLATION_NOT_IMPLEMENTED, curNode);
	    IndexType itype = (IndexType) curNode.expr();
	    DimensionType dtype = (DimensionType) itype.of().expr();
	    IntType[] edims = dtype.dims();
	    if (TypeUtils.realDims(edims).length == 1)
		results.push("length(" + dtype.name() + ")");
	    else
		switch (itype.dimensionIndex()) {
		case 0: //row
		    results.push("nrow(" + dtype.name() + ")");
		    break;
		case 1: //col
		    results.push("ncol(" + dtype.name() + ")");
		    break;
		default:
		    results.push("dim(" + dtype.name() + ")[" + Integer.toString(itype.dimensionIndex()) + "]");
		}
	    return;

	case COLON:
	    if (curNode.expr() instanceof IndexType) {
		results.push(" ");
		return;
	    }

	    if (!curNode.parent().type().equals(NodeType.COLON)) {
		if (curNode.childExists(NodeType.COLON))
		    child_results = getChildResults(results, curNode.childs().size() + 1);
		else {
		    child_results = getChildResults(results, curNode.childs().size());
		    // implicit 1 for two-parameter slices
		    child_results.add(1, "1");
		}
		results.push(translateSliceOperator(curNode, child_results));
	    }

	    return;

	case PLUS:
	case MINUS:
	case TIMES:
	case ELEMENTWISE_TIMES:
	case EXP:
	case ELEMENTWISE_EXP:
	case GRT:
	case LST:
	case ELEMENTWISE_LEFTDIV:
	case LEFTDIV:
	case DOUBLE_EQ:
	case NEQ:
	case NEG:
	case GRTE:
	case LSTE:
	case BIN_AND:
	case LOG_AND:
	case LOG_OR:
	    child_results = getChildResults(results, curNode.childs().size());
	    if (curNode.childs().size() == 1) {
		// unary
		AASTNode left = curNode.childs().get(0);
		results.push(translateUnaryScalarExpression(curNode, left, child_results.get(0)));
	    } else {

		AASTNode left = TypeUtils.getIDNode(curNode.childs().get(0));
		AASTNode right = TypeUtils.getIDNode(curNode.childs().get(1));

		results.push(translateScalarExpression(curNode, left, right, child_results));
	    }
	    return;
	case PARENS:
	    child_results = getChildResults(results, curNode.childs().size());
	    results.push("(" + child_results.get(0) + ")");
	    return;
	case EXPR_STMT:
	    if (curNode.expr() == null)
		return;

	    if (curNode.hasAttr(NodeAttr.ETYPE_FROM_EXTERNAL_ENV)) {
		child_results = getChildResults(results, curNode.childs().size() - 1);
	    } else
		child_results = getChildResults(results, curNode.childs().size());

	    // when subroutine is imported, avoid the branch with its name,
	    // which is empty
	    if (child_results.size() < 1)
		return;
	    String stmStr = child_results.get(0);

	    if (child_results.size() == 2) {
		String assStr = child_results.get(1);

		if (writingATFunction) {
		    int index = assStr.indexOf("(");
		    index = index < 0 ? 0 : index;

		    String[] spl;
		    if (index > 0)
			spl = assStr.substring(index, assStr.length() - 1).split(",");
		    else
			spl = assStr.substring(index, assStr.length()).split(",");
		    int numOfPar = spl.length;
		    assStr = assStr.substring(0, index);
		    if (spl.length > 1)
			assStr += spl[0] + "," + spl[1];
		    else
			assStr += spl[0];

		    if (numOfPar > 2)
			for (int i = 0; i < numOfPar - 2; i++)
			    assStr += ",parameters[[" + (i + 1) + "]]";
		    if (spl.length > 1)
			assStr += ")";

		}
		stmStr += " <- " + assStr;
	    }

	    getCurrentBuffer().append(indentString() + stmStr).append(NL);
	    return;
	case VECTOR:
	    if (curNode.expr() == null) {
		child_results = getChildResults(results, curNode.childs().size() - 1);
	    } else {

		child_results = getChildResults(results, curNode.childs().size());
		results.push(translateVector(curNode, child_results));
	    }
	    return;
	case MATRIX:
	    // check if etype is present for this matrix, to avoid dummy strings
	    if (curNode.expr() != null) {
		child_results = getChildResults(results, curNode.childs().size());
		results.push(translateMatrix(curNode, child_results));
	    }
	    return;
	case ELEMENTWISE_CCT:
	case TRANSPOSE:
	    if (!curNode.parentExists(NodeType.MATRIX)
		    || TypeUtils.realDims(TypeUtils.getIDNode(curNode.childs().get(0)), 0).length > 1)
	    // if this is a row/column binding, skip transpose (rowbind/colbind will do the right thing
	    {
		child_results = getChildResults(results, curNode.childs().size());
		results.push("t( " + child_results.get(0) + " )");
	    }
	    return;
	case FUNCTION:

	    if (curNode.hasAttr(NodeAttr.FUNCTION_TYPE))
		if (curNode.attr(NodeAttr.FUNCTION_TYPE).equals(NodeType.AT)) {
		    writingATFunction = false;

		}

	    child_results = getChildResults(results, curNode.childs().size() - 1);

	    if (curNode.hasAttr(NodeAttr.FUNCTION_OUTPUT)) {
		ArrayList<String> ret = (ArrayList<String>) curNode.attr(NodeAttr.FUNCTION_OUTPUT);
		String retStr = String.join(",", ret);
		retStr = "list(" + retStr + ")";
		getCurrentBuffer().append(indentString() + "return(" + retStr + ")\n");
	    }
	    getCurrentBuffer().append("}\n");

	    indentLevel--;

	    // DT: removed because we don't know why at_0 function shouldn't be written
	    //if ((!curNode.hasAttr(NodeAttr.FUNCTION_TYPE) || !curNode.attr(NodeAttr.FUNCTION_TYPE).equals(NodeType.AT))
	    //    || !curNode.child(NodeType.STATEMENT_LIST).childs().get(0).hasAttr(NodeAttr.FUNCALL))

	    // && !curNode.parent().parentExists(NodeType.FUNCTION, 2))
	    functionBufferList.add(getCurrentBuffer());
	    // else {
	    // int index = getPreviousBuffer(getCurrentBuffer()).indexOf("{");
	    // getPreviousBuffer(getCurrentBuffer()).insert(index + 2, "\n" +
	    // getCurrentBuffer());
	    // }
	    // if (!curNode.hasAttr(NodeAttr.FUNCTION_TYPE) ||
	    // !curNode.attr(NodeAttr.FUNCTION_TYPE).equals(NodeType.AT)) {
	    writingATFunction = false;
	    buffers.remove(getCurrentContext());
	    // }

	    //	    if (curNode.hasAttr(NodeAttr.SYMBOL))
	    //		results.push((String) curNode.attr(NodeAttr.SYMBOL));
	    //	    else if (curNode.childExists(NodeType.ID)) {
	    //		AASTNode child = curNode.child(NodeType.ID);
	    //		results.push(child.name());
	    //	    }
	    return;

	case IF:
	case WHILE:
	case ELSEIF:
	case PARFOR:
	case FOR:

	    String command = "";
	    switch (curNode.type()) {
	    case IF:
		command = "if";
		break;
	    case ELSEIF:
		command = "} else if";
		break;
	    case WHILE:
		command = "while";
		break;
	    case PARFOR:
	    case FOR:
		command = "for";
		break;

	    }
	    String expr;
	    if (command.equals("for")) {
		child_results = getChildResults(results, 2);
		expr = child_results.get(0) + " in " + child_results.get(1);

	    } else {
		child_results = getChildResults(results, 1);
		expr = child_results.get(0);

	    }

	    if (!expr.startsWith("(") || !expr.endsWith(")"))
		expr = "(" + expr + ")";

	    indentLevel--;
	    expr = indentString() + command + expr + "{\n";
	    getCurrentBuffer().insert(0, expr);

	    if (!curNode.type().equals(NodeType.ELSEIF))
		getCurrentBuffer().append(indentString() + "}\n");

	    getPreviousBuffer(getCurrentContext()).append(getCurrentBuffer());
	    buffers.remove(getCurrentContext());

	    return;
	case CLEAR:
	    if (curNode.name().equals("clear"))
		getCurrentBuffer().append(indentString() + "rm(list = ls());").append(NL);
	    return;
	case DOT:
	    results.push("$");
	    return;
	case FIELDACCESS:
	    child_results = getChildResults(results, curNode.childs().size());
	    if (curNode.parentExists(NodeType.LHS)) {
		// IF ON LHS this is a definition
		Tuple<Set<AASTNode>, StringBuffer> ctx = getCurrentContext();
		AASTNode curBase = TypeUtils.getIDNode(curNode.childs().get(0));
		if (!alreadyDefined(ctx, curBase)) {
		    StructType stype = curBase.sexpr();
		    defineStruct(ctx, curBase.name(), stype);
		    // remember it was defined
		    define(ctx, curBase);
		}
	    }
	    results.push(String.join("", child_results));
	    return;
	default:
	    throw new UndefinedTranslationException(RErrorMessage.TRANSLATION_NOT_IMPLEMENTED, curNode);
	}

    }

    private void define(Tuple<Set<AASTNode>, StringBuffer> ctx, AASTNode curBase) {
	ctx.first().add(curBase);
    }

    private void defineStruct(Tuple<Set<AASTNode>, StringBuffer> ctx, String base, StructType stype) {
	Iterator<Tuple<List<GType>, String>> it = stype.iterFields();
	ctx.second().append(base).append(" <- ").append("list();").append(NL);
	StringBuffer idef = new StringBuffer();
	while (it.hasNext()) {
	    Tuple<List<GType>, String> tp = it.next();
	    if (tp.first().get(0).equals(BType.STRUCT)) {
		idef.append(tp.second());
		// recursively define substructs
		defineStruct(ctx, base + "$" + idef.toString(), (StructType) tp.first().get(0));
		idef.append("$");
	    }
	}
    }

    private boolean alreadyDefined(Tuple<Set<AASTNode>, StringBuffer> ctx, AASTNode curNode) {
	boolean found = false;
	int bi = buffers.indexOf(ctx);
	if (bi < 0)
	    return false;

	while (!found && ctx != null) {
	    Set<AASTNode> curSymbols = ctx.first();
	    found = symbolAlreadyDefined(curSymbols, curNode);
	    // get prev buffer (one level up in the hierarchy)
	    ctx = (bi > 0) ? buffers.get(--bi) : null;
	}
	return found;
    }

    private boolean symbolAlreadyDefined(Set<AASTNode> curSymbols, AASTNode curNode) {
	for (AASTNode defSymbol : curSymbols)
	    if (defSymbol.code().equals(curNode.code()))
		return true;

	return false;
    }

    @Override
    public void onWalkCompleted(AAST ref) throws GException {
	// TODO Auto-generated method stub

    }

    @Override
    public void onWalkStarted(AAST ref) throws GException {
	// TODO Auto-generated method stub

    }

    private List<String> getChildResults(Deque<String> results, int howmany) {
	LinkedList<String> child_results = new LinkedList<String>();
	// get my params from stack
	String res;
	for (int i = 0; i < howmany; ++i) {
	    res = results.poll();
	    if (res != null)
		child_results.addFirst(res);
	}
	return child_results;
    }

    private void outputMainCode(BufferedWriter writer, AAST program, Path path) throws IOException, TypeException {

	// write body
	String main = translation.toString();
	// actual write
	writer.write(main);
    }

    @StepType(method = StepType.Function.UNARYSCALAREXPRESSION)
    public String translateUnaryScalarExpression(AASTNode curRoot, AASTNode left, String left_translation)
	    throws TypeException {
	String operand = null;

	switch (curRoot.type()) {
	case PLUS:
	    operand = "+";
	    break;
	case MINUS:
	    operand = "-";
	    break;
	case NEG:
	    operand = "!";
	case ELEMENTWISE_TIMES:
	case TIMES:
	case ELEMENTWISE_LEFTDIV:
	case LEFTDIV:
	    operand = "/";
	case ELEMENTWISE_EXP:
	    throw new TypeException(RErrorMessage.NOT_UNARY_OPERAND, curRoot, left.code());
	default:
	    throw new TypeException(RErrorMessage.UNKNOWN_OPERAND, curRoot);
	}

	return operand + left_translation;
    }

    @StepType(method = StepType.Function.SCALAREXPRESSION)
    public String translateScalarExpression(AASTNode curRoot, AASTNode left, AASTNode right, List<String> child_results)
	    throws TypeException {
	String operand = null;
	boolean funcall = false;

	switch (curRoot.type()) {
	case PLUS:
	    operand = "+";
	    break;
	case MINUS:
	    operand = "-";
	    break;
	case TIMES:
	    // if type is not matrix and at least one children is matrix, it
	    // means that we have scalar product

	    if (left.expr() != null && right.expr() != null && left.expr().isCastableToMatrix()
		    && right.expr().isCastableToMatrix()) {
		operand = "%*%";
	    } else
		operand = "*";
	    break;
	case ELEMENTWISE_TIMES:
	    operand = "*";
	    break;
	case ELEMENTWISE_LEFTDIV:
	case LEFTDIV:
	    operand = "/";
	    break;
	case EXP:
	case ELEMENTWISE_EXP:
	    operand = "^";
	    break;
	case GRT:
	    operand = ">";
	    break;
	case LST:
	    operand = "<";
	    break;
	case DOUBLE_EQ:
	    operand = "==";
	    break;
	case NEQ:
	    operand = "!=";
	    break;
	case GRTE:
	    operand = ">=";
	    break;
	case LSTE:
	    operand = "<=";
	    break;
	case BIN_AND:
	    operand = "&";
	    break;
	case LOG_AND:
	    operand = "&&";
	    break;
	case LOG_OR:
	    operand = "||";
	    break;
	default:
	    throw new TypeException(RErrorMessage.UNKNOWN_OPERAND, curRoot);
	}

	String leftT = child_results.get(0);
	String rightT = child_results.get(1);
	if (!funcall)
	    return leftT + " " + operand + " " + rightT;
	else
	    return operand + "(" + leftT + ", " + rightT + ")";
    }

    public boolean isCoreFunction(String funName) {
	for (IFunction fun : coreFunctions)
	    if (fun.getName().equalsIgnoreCase(funName))
		return true;
	return false;
    }

    public String translateFunctionCall(AASTNode curNode, List<String> childResults) {

	String funcName = childResults.remove(0);
	String translation = funcName + "(" + String.join(",", childResults) + ")";

	if (curNode.expr().equals(BType.STRUCT))
	    // case parameter of function as a struct, keep as it is
	    return translation;
	else
	    // case parameter of function as a base type, unlist here
	    return translation + "[[1]]";
    }

    @StepType(method = StepType.Function.COREFUNCTION)
    public String translateCoreFunction(AASTNode curApplyNode, String functionName, List<String> paramsTranslated)
	    throws IllegalArgumentException, UndefinedTranslationException, TypeException, MissingNodeException {

	IFunction fenum = null;
	for (IFunction fun : coreFunctions)
	    if (fun.getName().equalsIgnoreCase(functionName))
		fenum = fun;
	if (fenum == null)
	    throw new UndefinedTranslationException(RErrorMessage.CORE_FUNCTION_DOESNT_EXIST, curApplyNode,
		    functionName);

	List<AASTNode> params = curApplyNode.childs(NodeType.FUNCTION_PARAMETER_LIST);
	// if preamble needed (a.k.a. output symbol needed)
	// use apply node as output node.
	AASTNode outSymbol = curApplyNode;

	AASTNode first;

	String paramsJoined = String.join(",", paramsTranslated);

	switch (fenum.getName().toLowerCase()) {
	case "pi":
	    return "pi";
	case "false":
	    return "false";
	case "true":
	    return "true";
	case "ceil":
	    return "ceiling(" + paramsTranslated.get(0) + ")";
	case "size":
	    AASTNode sin = TypeUtils.getIDNode(params.get(0));
	    IntType[] rdims = TypeUtils.realDims(sin, 0);
	    if (rdims.length == 1)
		return "length(" + paramsTranslated.get(0) + ")";
	    else if (paramsTranslated.size() == 1)
		return "if(is.null(dim(" + paramsJoined + "))){ length(" + paramsJoined + ") }else{ dim(" + paramsJoined
			+ ") }";
	    else if (paramsTranslated.size() > 1)
		return "dim(" + paramsTranslated.get(0) + ")[" + paramsTranslated.get(1) + "]";
	case "factorial":
	    return "factorial(" + paramsJoined + ")";
	case "length":
	    return "length(" + paramsJoined + ")";
	case "unique":
	    return "unique(" + paramsJoined + ")";
	case "strcat":
	    return "paste(" + paramsJoined + ",sep=\"\")";
	case "sprintf":
	    return "sprintf(" + paramsJoined + ")";
	case "fprintf":
	    return "fprintf(" + paramsJoined + ")";
	case "addpath":
	    if (!extLibraries.contains("R.utils"))
		extLibraries.add("R.utils");
	    if (paramsJoined.contains("\\"))
		paramsJoined = paramsJoined.replaceAll("\\", "/");
	    return "sourceDirectory(" + paramsJoined + ")";
	case "strcmp":
	    return paramsTranslated.get(0) + " == " + paramsTranslated.get(1);
	case "isinf":
	    return "is.infinite(" + paramsJoined + ")";
	case "isnan":
	    return "is.nan(" + paramsJoined + ")";
	case "disp":
	    return "print(" + paramsJoined + ")";
	case "find":
	    if (curApplyNode.child(NodeType.FUNCTION_PARAMETER_LIST).hasAttr(NodeAttr.PARAMS)) {
		if (!curApplyNode.child(NodeType.FUNCTION_PARAMETER_LIST).attr(NodeAttr.PARAMS).equals(NodeType.ID)) {
		    return "which(" + paramsTranslated.get(0) + ")";
		}
	    }
	    // matlab case find(x), it means find(X!=0)
	    return "which(" + paramsTranslated.get(0) + "!=0)";
	case "linspace":
	    if (paramsTranslated.size() == 2) {
		return "seq(" + paramsTranslated.get(0) + ", " + paramsTranslated.get(1) + ", length=100)";
	    } else {
		// inverted end and step
		return "seq(" + paramsTranslated.get(0) + ", " + paramsTranslated.get(1) + ", length="
			+ paramsTranslated.get(2) + ")";
	    }
	case "logspace":
	    if (!extLibraries.contains("pracma"))
		extLibraries.add("pracma");
	    if (paramsTranslated.size() == 2) {
		return "logspace(" + paramsTranslated.get(0) + ", " + paramsTranslated.get(1) + ", n = 50)";
	    } else {
		// inverted end and step
		return "logspace(" + paramsTranslated.get(0) + ", " + paramsTranslated.get(1) + ", n="
			+ paramsTranslated.get(2) + ")";
	    }
	case "sum":
	    if (paramsTranslated.size() == 1)
		return "sum(" + paramsTranslated.get(0) + ")";
	    else if (paramsTranslated.get(1).equals("1"))
		return "colSums(" + paramsTranslated.get(0) + ")";
	    else if (paramsTranslated.get(1).equals("2"))
		return "rowSums(" + paramsTranslated.get(0) + ")";
	case "max":
	    return "max(" + paramsJoined + ")";
	case "min":
	    return "min(" + paramsJoined + ")";
	case "ones":
	case "zeros":
	case "nan":

	    paramsJoined = String.join(",", paramsTranslated);

	    switch (fenum.getName().toLowerCase()) {
	    case "ones":
		return "array(1, c(" + paramsJoined + "))";
	    case "zeros":
		return "array(0, c(" + paramsJoined + "))";
	    case "nan":
		return "array(NaN, c(" + paramsJoined + "))";
	    default:
		throw new UndefinedTranslationException(RErrorMessage.CORE_FUNCTION_DOESNT_EXIST, curApplyNode,
			fenum.getName());
	    }

	case "randn":
	    return "rnorm(" + paramsTranslated.get(0) + ")";

	case "odeset":

	    Map<String, String> par_map = new HashMap<String, String>();
	    String ret = "odeset <- data.frame(";

	    for (int i = 1; i < paramsTranslated.size(); i = i + 2) {

		String id = paramsTranslated.get(i - 1);
		String value = paramsTranslated.get(i);
		switch (id) {
		case "\"Stats\"":
		    value = (value.equals("\"on\"")) ? "TRUE" : "FALSE";

		}
		ret += value + ",";
		par_map.put(paramsTranslated.get(i - 1), value);
	    }

	    ret = ret.substring(0, ret.length() - 1);
	    ret += ")\n";
	    ret += indentString() + "names(odeset) <- c(";
	    for (int i = 0; i < paramsTranslated.size(); i = i + 2)
		ret += paramsTranslated.get(i) + ",";
	    ret = ret.substring(0, ret.length() - 1);
	    ret += ")\n";

	    par_dictionary.put("odeset", par_map);
	    return ret;

	case "mod":
	    return paramsTranslated.get(0) + " %% " + paramsTranslated.get(1);

	case "abs":
	case "sign":
	case "sin":
	case "cos":
	case "tan":
	case "asin":
	case "acos":
	case "atan":
	case "atan2":
	case "power":
	case "exp":
	case "log":
	case "log10":
	case "floor":
	case "sqrt":
	    String fun_str = "";

	    switch (fenum.getName()) {
	    case "power":
		fun_str = "`^`";

		break;
	    case "exp":
		fun_str = "exp";

		break;
	    case "log":
		fun_str = "log";

		break;
	    case "log10":
		fun_str = "log10";

		break;
	    case "floor":
		fun_str = "floor";

		break;
	    case "sqrt":
		fun_str = "sqrt";

		break;
	    case "abs":
		fun_str = "abs";

		break;
	    case "sign":
		fun_str = "sign";

		break;
	    case "sin":
		fun_str = "sin";

		break;
	    case "cos":
		fun_str = "cos";

		break;
	    case "tan":
		fun_str = "tan";

		break;
	    case "asin":
		fun_str = "asin";

		break;
	    case "acos":
		fun_str = "acos";

		break;
	    case "atan":
		fun_str = "atan";

		break;
	    case "atan2":
		fun_str = "atan2";

		break;
	    default:
		throw new UndefinedTranslationException(RErrorMessage.CORE_FUNCTION_DOESNT_EXIST, curApplyNode,
			functionName);
	    }
	    switch (params.size()) {
	    case 0:
		throw new UndefinedTranslationException(RErrorMessage.CORE_FUNCTION_WITHOUT_ARGUMENT_UNSUPPORTED,
			curApplyNode, functionName);
	    case 1:
	    case 2:
		first = params.get(0);
		// a scalar is a matrix 1x1

		// if it is a scalar
		return fun_str + "(" + paramsJoined + ")";
	    default:
		throw new UndefinedTranslationException(RErrorMessage.TRANSLATION_NOT_IMPLEMENTED, curApplyNode);
	    }

	case "ode15i":
	case "ode15s":
	case "ode23":
	case "ode23s":
	case "ode45":
	case "ode45s":
	    writingODE = false;
	    // necessary when at function
	    String parameters = "list()";

	    // the first parameter is the anonimous function to integrate
	    AASTNode anonimousFun = params.get(0);
	    // get the child FUNCTION under the Expression
	    AASTNode origFunctionNode = anonimousFun.child(NodeType.FUNCTION);
	    if (origFunctionNode == null) {
		// maybe is a reference to a function
		AASTNode refNode = anonimousFun.child(NodeType.AT);
		if (!refNode.hasAttr(NodeAttr.REF_FUNCTION))
		    throw new MissingNodeException(RErrorMessage.FUNCTION_REFERENCE_WITHOUT_REFERENCE, curApplyNode,
			    anonimousFun.name());
		origFunctionNode = (AASTNode) refNode.attr(NodeAttr.REF_FUNCTION);
	    }

	    List<String> parametersList = new LinkedList<>();
	    if (origFunctionNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS))
		for (AASTNode envparam : (List<AASTNode>) origFunctionNode.attr(NodeAttr.FUNCTION_ENV_PARAMS))
		    parametersList.add(envparam.name());
	    // function apply env params actual values
	    if (paramsTranslated.size() > 4)
		for (String p : paramsTranslated.subList(4, params.size()))
		    parametersList.add(p);

	    if (parametersList.isEmpty())
		parameters = "list()";
	    else
		parameters = "list(" + String.join(",", parametersList) + ")";

	    String function;
	    if (origFunctionNode.hasAttr(NodeAttr.APPLY_FUNCTION))
		function = ((AASTNode) origFunctionNode.attr(NodeAttr.APPLY_FUNCTION)).child(NodeType.ID).name();
	    else
		function = origFunctionNode.child(NodeType.ID).name();

	    String odeStr = "ode(func=" + function + ",times=" + paramsTranslated.get(1) + ",y="
		    + paramsTranslated.get(2);

	    if (par_dictionary.containsKey("odeset")) {
		for (Entry<String, String> entry : par_dictionary.get("odeset").entrySet()) {
		    if (dict.get(entry.getKey().replaceAll("\"", "")) != null) {
			if (dict.get(entry.getKey().replaceAll("\"", "")).equals("maxords")
				&& (fenum.getName().equals("ode15s") || fenum.getName().equals("ode15i"))) {
			    continue;
			}
			odeStr += "," + dict.get(entry.getKey().replaceAll("\"", "")) + "=odeset$"
				+ entry.getKey().replaceAll("\"", "");
		    }

		}

	    }

	    if (!par_dictionary.containsKey("odeset")
		    || !par_dictionary.get("odeset").keySet().contains("\"MaxStep\"")) {
		odeStr += ", hmax=floor(abs(" + paramsTranslated.get(1) + "[length(" + paramsTranslated.get(1) + ")] - "
			+ paramsTranslated.get(1) + "[1]" + ")/10)";
	    }
	    if (!par_dictionary.containsKey("odeset")
		    || !par_dictionary.get("odeset").keySet().contains("\"RelTol\"")) {
		odeStr += ", rtol=1e-3";
	    }

	    String method;
	    switch (fenum.getName()) {
	    case "ode23":

		method = "\"ode23\"";
		break;
	    case "ode45":
		method = "\"ode45\"";
		break;
	    case "ode23s":
	    case "ode45s":
	    case "ode15s":
	    case "ode15i":
		method = "lsodes";
		break;
	    default:
		method = "lsoda";
		break;
	    }
	    odeStr += ",method=" + method;
	    odeStr += ",parms=" + parameters;
	    odeStr += ")";
	    return odeStr;
	case "load":
	    // if (!extLibraries.contains("R.matlab"))
	    // extLibraries.add("R.matlab");
	    StringBuffer bf = new StringBuffer();
	    bf.append("library(R.matlab)\n");
	    bf.append("loadMat<-function(filename){\n");
	    bf.append("\t ll<-readMat(filename, fixNames=FALSE)\n");
	    bf.append("\t df<- lapply(ll, data.frame, stringsAsFactors=FALSE)\n");
	    bf.append("\t if(length(ll)==1){\n");
	    bf.append("\t\t return (df[[1]][[1]])\n");
	    bf.append("\t } else{\n");
	    bf.append("\t\t df<- simplify2array(ll, higher=TRUE)\n");
	    bf.append("\t\t return (df)\n");
	    bf.append("\t }\n");
	    bf.append("}");

	    File parentFolder = outputFilePath().toFile().getParentFile();
	    File f = new File(parentFolder.getPath() + File.separator + "loadMat.R");

	    Path path = Paths.get(f.getPath());

	    try {
		BufferedWriter writer;
		writer = Files.newBufferedWriter(path);
		// write body
		// actual write
		writer.write(bf.toString());
		writer.close();
	    } catch (IOException e) {
		logger.error("Error writing output program: " + e.getMessage());
		logger.debug("Error writing output program: " + e.getMessage(), e);
	    }

	    int index = paramsTranslated.get(0).lastIndexOf("/");
	    String fileName = paramsTranslated.get(0);// .substring(index + 1,
						      // paramsTranslated.get(0).length()
						      // - 1);

	    userFunctionNames.add("loadMat");
	    // getCurrentBuffer().insert(0, "source(\"loadMat.R\")\n");
	    String loadStr;
	    if (curApplyNode.parentExists(NodeType.RHS))
		loadStr = "loadMat(" + fileName + ")\n";
	    else
		loadStr = curApplyNode.expr().inputName().replaceAll("_", ".") + " <- loadMat(" + fileName + ")\n";
	    return loadStr;

	case "numel":
	    return "length(" + paramsTranslated.get(0) + ")";
	case "interp1":
	    if (!extLibraries.contains("stats"))
		extLibraries.add("stats");
	    String command = "approx";
	    if (paramsTranslated.size() > 3 && paramsTranslated.get(3).contains("spline")) {
		command = "spline";
	    }

	    String interp = command + "(" + paramsTranslated.get(0) + "," + paramsTranslated.get(1) + ","
		    + paramsTranslated.get(2);
	    if (paramsTranslated.size() > 3) {
		String methodKey = paramsTranslated.get(3);
		String methodValue = dict.get(methodKey.replaceAll("\"", ""));
		interp += ", method=\"" + methodValue;

	    }
	    interp += "\")$y";
	    return interp;
	case "csvwrite":
	    if (!extLibraries.contains("utils"))
		extLibraries.add("utils");
	    String sep = " ";
	    String saveStr = "write.table(";
	    if (paramsTranslated.get(0).endsWith(".tsv\""))
		sep = "\\t";
	    else if (paramsTranslated.get(0).endsWith(".csv\""))
		saveStr = "write.table(";

	    saveStr += paramsTranslated.get(1) + ", " + paramsTranslated.get(0);
	    if (!paramsTranslated.get(0).endsWith(".csv\""))
		saveStr += ", sep=\"" + sep + "\"";
	    else
		saveStr += ", sep=\",\", row.names = FALSE, col.names = FALSE";
	    saveStr += ")";
	    return saveStr;
	case "save":
	    if (!extLibraries.contains("R.matlab"))
		extLibraries.add("R.matlab");
	    sep = " ";
	    saveStr = "writeMat(";
	    saveStr += paramsTranslated.get(0) + ", " + paramsTranslated.get(1);
	    saveStr += ")";
	    return saveStr;

	case "csvread":

	    bf = new StringBuffer();
	    bf.append("library(utils)\n");
	    bf.append("library(BBmisc)\n");
	    bf.append("loadCSV <- function(filename){\n");
	    bf.append("\t df <- read.csv(filename)\n");
	    bf.append("\t ret <- convertColsToList(df)\n");
	    bf.append("\t ret <- lapply(ret, na.omit)\n");
	    bf.append("\t return (ret[[1]])\n");
	    bf.append("}");

	    parentFolder = outputFilePath().toFile().getParentFile();
	    f = new File(parentFolder.getPath() + File.separator + "loadCSV.R");

	    path = Paths.get(f.getPath());

	    try {
		BufferedWriter writer;
		writer = Files.newBufferedWriter(path);
		// write body
		// actual write
		writer.write(bf.toString());
		writer.close();
	    } catch (IOException e) {
		logger.error("Error writing output program: " + e.getMessage());
		logger.debug("Error writing output program: " + e.getMessage(), e);
	    }

	    userFunctionNames.add("loadCSV");
	    return "loadCSV(" + paramsTranslated.get(0) + ")";
	case "clc":
	case "close all":
	case "tic":
	case "toc":
	case "all":
	    return "";

	case "hilb":
	case "lu":
	case "schur":
	case "expm":
	    if (!extLibraries.contains("Matrix"))
		extLibraries.add("Matrix");

	    switch (fenum.getName()) {
	    case "hilb":
		return "Hilbert(" + paramsTranslated.get(0) + ")";

	    case "lu":
		return "expand(lu(Matrix(" + paramsTranslated.get(0) + ")))";

	    case "schur":
		return "Schur(Matrix(" + paramsTranslated.get(0) + "))";

	    case "expm":
		return "expm(Matrix(" + paramsTranslated.get(0) + "))";
	    }
	case "figure":
	    return "";

	// read file as text, size necessary to allocate bytes
	case "fileread":
	    fileName = paramsTranslated.get(0);
	    return "readChar(" + fileName + ", file.info(" + fileName + ")$size)";
	case "semilogy":
	case "plot":
	    String plotStr = "plot(" + paramsTranslated.get(0) + ", " + paramsTranslated.get(1);
	    if (paramsTranslated.size() > 2) {

		String color = paramsTranslated.get(2);
		color = color.trim();
		color = color.substring(1, color.length() - 1);
		plotStr += ", col=\"" + dict.get(color) + "\"";

	    }
	    if (fenum.getName().equals("semilogy"))
		plotStr += ",log=\"y\"";
	    plotStr += ")";
	    return plotStr;
	case "axis":
	case "xlabel":
	case "ylabel":
	case "legend":
	case "title":
	case "set":
	case "drawnow":
	case "keyboard":
	case "saveas":
	case "suptitle":
	    return "";
	case "subplot":
	    String[] nums = paramsTranslated.get(0).split("");
	    if (nums[nums.length - 1].equals("1"))
		return "par(mfrow=c(" + nums[0] + "," + nums[1] + "))";
	    else
		return "";
	}
	return null;
    }

    private String translateAssign(AASTNode lhsNode, String lhs_symbol, Map<AASTNode, List<AASTNode>> lhs_params,
	    AASTNode rhsNode, String rhs_symbol, NodeType type) throws UndefinedTranslationException, TypeException {
	String assSymbol;
	switch (type) {

	case GASSIGN:
	case ASSIGN:
	default:
	    if (globalVars.contains(lhs_symbol))
		assSymbol = " <<- ";
	    else

		assSymbol = " <- ";
	    break;
	}

	define(getCurrentContext(), lhsNode);
	return lhs_symbol + assSymbol + rhs_symbol + "\n";
    }

    private String translateVector(AASTNode node, List<String> childResults) {
	if (childResults.size() == 1)
	    return childResults.get(0);

	String ret = String.join(",", childResults);

	if (GType.get(BType.VOID).equals(node.expr()))
	    return "";

	MatrixType a = (MatrixType) node.parent(NodeType.MATRIX).expr();
	IntType firstDim = a.dims()[0];
	boolean singleRowVector = (firstDim.hasValue() && firstDim.value() == 1) ? true : false;

	if (node.attr(NodeAttr.VECTOR_MATRIX_VALUES).equals(false)
		&& node.attr(NodeAttr.VECTOR_MIXED_VALUES).equals(false))
	    // so it's a vector of numbers.. use c
	    singleRowVector = true;

	String command = !singleRowVector ? "cbind" : "c";
	((MatrixType) node.parent().expr()).dims();

	ret = command + "(" + ret + ")";
	return ret;
    }

    private String translateMatrix(AASTNode node, List<String> childResults) {

	if (childResults.size() == 1)
	    if ("".equals(childResults.get(0)))
		return "c()";
	    else
		return childResults.get(0);

	String reducedName = node.name().substring(1, node.name().length() - 1);
	for (AASTNode child : node.childs()) {
	    reducedName = reducedName.replace(child.name(), "");
	}
	String concCommand;
	boolean vector = TypeUtils.realDims(node, 0).length == 1;
	if (vector)
	    concCommand = "c";
	else
	    switch (reducedName) {
	    case ",":
		concCommand = "cbind";
		break;
	    case ";":
	    default:
		concCommand = "rbind";
	    }

	String ret = concCommand + "(" + String.join(",", childResults) + ")";

	return ret;
    }

    private String translateComment(String curNodeName) {

	int index = curNodeName.indexOf("%");
	String comment = "# " + curNodeName.substring(index + 1) + "\n";
	return comment;

    }

    public String translateSliceOperator(AASTNode sliceNode, List<String> childResults) {

	SliceType stype = (SliceType) sliceNode.expr();
	return "seq(" + childResults.get(0) + ", " + childResults.get(2) + ", by=" + childResults.get(1) + ")";
    }

    private String translateMatrixAccess(AASTNode curNode, List<String> childResults) {
	// get name of matrix and remove from list
	String matName = childResults.remove(0);
	String ret = matName + "[";

	ret += String.join(",", childResults);

	ret += "]";

	return ret;
    }

    private String indentString() {
	String ret = "";
	for (int i = 0; i < indentLevel; i++) {
	    ret += "\t";
	}
	return ret;
    }

    private StringBuffer getCurrentBuffer() {
	return buffers.get(buffers.size() - 1).second();
    }

    private Tuple<Set<AASTNode>, StringBuffer> getCurrentContext() {
	return buffers.get(buffers.size() - 1);
    }

    private Tuple<Set<AASTNode>, StringBuffer> getPreviousContext(Tuple<Set<AASTNode>, StringBuffer> tb) {
	int index = buffers.indexOf(tb);
	if (index < 1)
	    return null;
	return buffers.get(index - 1);
    }

    private StringBuffer getPreviousBuffer(Tuple<Set<AASTNode>, StringBuffer> tb) {
	int index = buffers.indexOf(tb);
	if (index < 1)
	    return null;
	return buffers.get(index - 1).second();
    }

    private String containsAnotherFunction(AASTNode node, String ret) {

	for (AASTNode child : node.childs()) {
	    if (userFunctionNames.contains(child.name())) {
		ret = child.name();
	    }
	    ret = containsAnotherFunction(child, ret);

	}
	return ret;
    }

    private AASTNode getChildOfType(AASTNode node, NodeType type) {

	for (AASTNode child : node.childs()) {
	    if (child.type().equals(type)) {
		node = child;
	    }
	    node = getChildOfType(node, type);

	}
	return node;
    }

    private String translateTic() {

	// if (this.is_maincompilationunit) {
	// String ticFun = String.join("\n",
	// "tic <- function(gcFirst = TRUE,type=c(\"elapsed\",
	// \"user.self\",\"sys.self\"))", "{",
	// " type <- match.arg(type)", "assign(\".type\", type,
	// envir=baseenv())", "if(gcFirst) gc(FALSE)",
	// "tic <- proc.time()[type]", "assign(\".tic\", tic,envir=baseenv())",
	// "invisible(tic)", "}");
	// boolean isAlreadyWritten = false;
	// for (StringBuffer sb : functionBufferList) {
	// if (sb.toString().contains(ticFun)) {
	// isAlreadyWritten = true;
	// break;
	// }
	//
	// }
	//
	// if (!isAlreadyWritten) {
	// StringBuffer newBuffer = new StringBuffer();
	// buffers.add(newBuffer);
	// getCurrentBuffer().append(ticFun);
	// functionBufferList.add(getCurrentBuffer());
	// buffers.remove(getCurrentBuffer());
	// }
	// }

	// String ret = "tic()";
	String ret = "tic <- Sys.time()";
	return ret;
    }

    private String translateToc() {

	// if (this.is_maincompilationunit) {
	// String tocFun = String.join("\n", "toc <- function()", "{", "type <-
	// get(\".type\", envir=baseenv())",
	// "toc <- proc.time()[type]", "tic <- get(\".tic\", envir=baseenv())",
	// "print(toc - tic)",
	// "invisible(toc)", "}");
	// boolean isAlreadyWritten = false;
	// for (StringBuffer sb : functionBufferList) {
	// if (sb.toString().contains(tocFun)) {
	// isAlreadyWritten = true;
	// break;
	// }
	//
	// }
	//
	// if (!isAlreadyWritten) {
	// StringBuffer newBuffer = new StringBuffer();
	// buffers.add(newBuffer);
	// getCurrentBuffer().append(tocFun);
	// functionBufferList.add(getCurrentBuffer());
	// buffers.remove(getCurrentBuffer());
	// }
	// }
	//
	// String ret = "toc()";
	String ret = "toc <- Sys.time() - tic\n";
	ret += translateTic() + "\n";
	getCurrentBuffer().append(ret);

	return "toc";
    }

}
