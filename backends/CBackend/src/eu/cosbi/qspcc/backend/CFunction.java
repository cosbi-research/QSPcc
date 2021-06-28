package eu.cosbi.qspcc.backend;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.backend.errors.CErrorMessage;
import eu.cosbi.qspcc.backend.interfaces.Function;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.utils.Quadruple;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.TypeUtils;

public class CFunction extends C implements Function {
    private static final String RETURN_VALUE_SUFFIX = "_ret";

    private Logger logger = LogManager.getLogger(CFunction.class);

    protected List<String> functionParams;
    protected List<String> functionOutputSymbols;
    protected String functionName;
    // this is generated while processing a sub-block, then passed back to main
    // backend
    // for function definition
    Map<String, List<String>> symbolicParamMatrixDimension = null;

    // the return symbol for this code block, if it is a function
    // @see functionName
    String functionOutputSymbol = null;

    private AASTNode functionNode;
    private C parent;

    private String funOutTypeAsString;

    private boolean funOutIsPointer;

    public CFunction(C parent, IFunction[] coreFunctions, AASTNode funNode, String mainSourcePath, String destFolder,
	    String defineVariables, Integer indent, Map<String, List<String>> backendSpecificOptions, String name,
	    List<String> outputSymbols, List<String> params, StringBuffer globalVars, StringBuffer localStruct,
	    boolean dockerized) {
	super(coreFunctions, mainSourcePath, destFolder, defineVariables, indent, backendSpecificOptions, dockerized);
	this.parent = parent;
	this.functionalEntryPoint = parent.getFunctionalEntryPoint();
	// function specific fields
	this.functionName = name;
	this.functionOutputSymbols = outputSymbols;
	this.functionParams = params;
	this.functionNode = funNode;
	// don't inherit global variables from parent
	this.globalVars = new StringBuffer();
	// don't inherit initialization buffer from parent
	this.translationInitialization = new StringBuffer();
	// don't inherit local functions
	this.localFunctions = new StringBuffer();
	// don't inherit local structures
	this.localStruct = new StringBuffer();
	if (outputSymbols.size() == 1)
	    functionOutputSymbol = outputSymbols.get(0);
	else
	    // return struct
	    functionOutputSymbol = name + RETURN_VALUE_SUFFIX;

	translationBufferList.add(new Triple<AASTNode, StringBuffer, Set<String>>(
		functionNode.child(NodeType.STATEMENT_LIST), translation, new HashSet<String>()));
    }

    public List<String> getFunctionParams() {
	return functionParams;
    }

    public List<String> getFunctionOutputSymbols() {
	return functionOutputSymbols;
    }

    public List<AASTNode> inputs() {
	return functionNode.childs(NodeType.PARAMETER_LIST);
    }

    public List<AASTNode> outputs() {
	return functionNode.childs(NodeType.FUNCTION_RETURN);
    }

    public AASTNode getFunctionNode() {
	return functionNode;
    }

    /**
     * available only after "genCode" call.
     * 
     * @return
     */
    public String funOutTypeAsString() {
	return funOutTypeAsString;
    }

    public boolean funOutIsPointer() {
	return funOutIsPointer;
    }

    public String functionName() {
	return functionName;
    }

    @Override
    public void initializeRandomNumberGenerator() {
	parent.initializeRandomNumberGenerator();
    }

    @Override
    public void outputMainCode(StringBuffer writer, AAST program, Path path) throws IOException, GException {

	String def = null;
	// env.functionReturnSymbols(functionName);
	List<AASTNode> outputSymbols = functionNode.childs(NodeType.FUNCTION_RETURN);
	if (outputSymbols == null)
	    throw new SyntaxException(CErrorMessage.INTERNAL_TREE_FUNCTION_RETURN_VALUE_MISSING, functionNode);

	List<AASTNode> functionParams;
	functionParams = functionNode.childs(NodeType.PARAMETER_LIST);
	if (functionParams == null)
	    throw new SyntaxException(CErrorMessage.INTERNAL_TREE_FUNCTION_PARAMETER_LIST_MISSING, functionNode);

	List<AASTNode> functionEnvParams;
	functionEnvParams = (List<AASTNode>) functionNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);

	Quadruple<String, Boolean, Boolean, Boolean> fd = genFunctionDefinition();
	def = fd.first();
	boolean single_matrix_struct = fd.second();
	boolean struct_needed = fd.third();
	boolean struct_return_type = fd.fourth();

	// RETURN SYMBOL HANDLING
	if (struct_needed || struct_return_type) {
	    // write output symbol initialization
	    StringBuffer outSymbolInitialization = new StringBuffer();
	    outSymbolInitialization.append("/* function return value wrapped in struct */").append(NL);
	    String structName = null;
	    if (struct_return_type)
		structName = outputSymbols.get(0).expr().name();
	    else {
		createStructType(functionName + STRUCT_SUFFIX,
			TypeUtils.createComplexType(functionName, outputSymbols));
		structName = functionName;
	    }
	    // struct return type is true if the only output is a struct
	    outSymbolInitialization.append("static __thread ").append(structName).append(STRUCT_SUFFIX).append(" ")
		    .append(functionOutputSymbol).append(";").append(NL);
	    // actual write
	    writer.append(outSymbolInitialization.toString());
	} else {
	    GType outSymbol = outputSymbols.get(0).expr();
	    if (!GType.get(BType.VOID).equals(outSymbol))
		writer.append("static __thread " + exprTypeToCType(outSymbol) + " " + functionOutputSymbol + ";" + NL);
	}
	writer.append(def + "{" + NL);
	// write initialization
	pendingInitializationToBuffer();
	writer.append(translationInitialization.toString());
	// write body
	String main = getBody();
	// actual write
	writer.append(main);
	// return statement
	if (struct_needed) {
	    String fillCode = fillOutputStructureCode(outputSymbols);
	    writer.append(fillCode);
	    writer.append(indentTabs() + "return &" + functionOutputSymbol + ";" + NL);
	} else if (struct_return_type) {
	    writer.append(indentTabs() + "return &" + functionOutputSymbol + ";" + NL);
	} else
	    writer.append(indentTabs() + "return " + functionOutputSymbol + ";" + NL);
	writer.append("}" + NL + NL);

	if (functionName().equals(getFunctionalEntryPoint())) {
	    // if this function is the entry point of the program (C mex case)
	    // create his output structure 
	    for (AASTNode output : outputs()) {
		if (output.expr().equals(BType.STRUCT)) {
		    StructType sexpr = output.sexpr();
		    // create struct if it happens to be used just here
		    createStructType(getStructName(sexpr), sexpr);
		}
	    }
	}
    }

    /**
     * 
     * @return the return symbol for this function if it returns just one symbol.
     * null otherwise.
     */
    public String singleReturnSymbol() {
	if (functionOutputSymbols.size() == 1)
	    return functionOutputSymbol;
	return null;
    }

    public String fillOutputStructureCode() {
	return fillOutputStructureCode(functionNode.childs(NodeType.FUNCTION_RETURN));
    }

    private String fillOutputStructureCode(List<AASTNode> outputSymbols) {
	StringBuffer sb = new StringBuffer();
	for (AASTNode onode : outputSymbols) {
	    sb.append(indentTabs() + functionOutputSymbol + STRUCT_ACCESS + onode.symbol() + " = " + onode.symbol()
		    + ";" + NL);
	}
	return sb.toString();
    }

    @Override
    public String getBody() {
	return translation.toString();
    }

    public Quadruple<String, Boolean, Boolean, Boolean> genFunctionDefinition()
	    throws TypeException, UndefinedTranslationException {
	// env.functionReturnSymbols(functionName);
	List<AASTNode> outputSymbols = functionNode.childs(NodeType.FUNCTION_RETURN);
	if (outputSymbols == null)
	    throw new TypeException(CErrorMessage.INTERNAL_TREE_FUNCTION_RETURN_VALUE_MISSING, functionNode);

	Boolean isAnonimousFunction = false;
	// check if it's the anonymous function, for in this case we could get the
	// parameter dimension
	// in some other way
	if (functionNode.hasAttr(NodeAttr.FUNCTION_TYPE)
		&& functionNode.attr(NodeAttr.FUNCTION_TYPE).equals(NodeType.AT)) {
	    isAnonimousFunction = true;
	}

	List<AASTNode> functionParams;
	functionParams = functionNode.childs(NodeType.PARAMETER_LIST);
	if (functionParams == null)
	    throw new TypeException(CErrorMessage.INTERNAL_TREE_FUNCTION_PARAMETER_LIST_MISSING, functionNode);

	String ret = null;
	if (functionName == null)
	    throw new TypeException(CErrorMessage.INTERNAL_TREE_FUNCTION_NAME_MISSING, functionNode);

	int n_ret_values = outputSymbols.size();
	boolean single_matrix_struct = (n_ret_values == 1 && outputSymbols.get(0).expr() != null
		&& outputSymbols.get(0).expr().equals(BType.MATRIX))
		|| (outputSymbols.get(0).expr() != null
			&& outputSymbols.get(0).expr().equals(BType.MATRIX_ACCESS_SLICE));
	boolean struct_needed = needsOutputStructure(); // || single_matrix_struct;
	boolean struct_return_type = n_ret_values == 1 && outputSymbols.get(0).expr() != null
		&& outputSymbols.get(0).expr().equals(BType.STRUCT);

	// define a new struct to hold return types
	String funOutType = null;
	try {
	    funOutType = exprTypeToCType(functionName, struct_needed || struct_return_type, outputSymbols);
	} catch (TypeException e) {
	    e.refNode(functionNode);
	    throw e;
	}
	funOutTypeAsString = funOutType;
	funOutIsPointer = struct_needed || struct_return_type;
	ret = funOutType + " " + functionName + "(";

	List<String> strparams = new ArrayList<String>();
	// both actual and env params
	for (AASTNode param : functionParams) {

	    GType typeToTranslate = getExprGeneralized(param);
	    if (typeToTranslate == null || typeToTranslate.equals(BType.UNKNOWN)
		    || typeToTranslate.equals(BType.VOID)) {
		continue;
	    }

	    String paramType = null;
	    try {
		if (isAnonimousFunction && typeToTranslate instanceof MatrixType
			&& ((MatrixType) typeToTranslate).dims() == null) {
		    paramType = funOutType;
		} else {
		    paramType = exprTypeToCType(typeToTranslate);
		}
	    } catch (TypeException e) {
		e.refNode(functionNode);
		throw e;
	    }

	    String fullParam = paramType + " " + param.symbol();
	    strparams.add(fullParam);
	    // the symbolic type INPUT in C is the CSV
	    if (typeToTranslate.isInput() && GType.get(BType.STRUCT).equals(typeToTranslate)) {
		// add also the input structure
		strparams.add("INPUT *" + typeToTranslate.inputName());
	    }

	}
	ret += String.join(", ", strparams);

	ret += ")";
	return new Quadruple<String, Boolean, Boolean, Boolean>(ret, single_matrix_struct, struct_needed,
		struct_return_type);
    }

    public boolean needsOutputStructure() {
	return functionOutputSymbols != null && functionOutputSymbols.size() > 1;
    }

    public boolean isVoid() {
	return functionOutputSymbols == null || functionOutputSymbols.size() == 0;
    }

    @Override
    public String toString() {
	return functionName;
    }
}
