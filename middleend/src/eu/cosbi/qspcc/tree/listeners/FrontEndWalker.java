package eu.cosbi.qspcc.tree.listeners;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.AASTError;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.StackError;
import eu.cosbi.qspcc.exceptions.StatementStackException;
import eu.cosbi.qspcc.exceptions.TypeError;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundError;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.exceptions.UnfinishedTranslationException;
import eu.cosbi.qspcc.expressions.type.FunctionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerBackend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.MiddleEndPass;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.statements.AssignStatement;
import eu.cosbi.qspcc.statements.ClearStatement;
import eu.cosbi.qspcc.statements.ExpressionStatement;
import eu.cosbi.qspcc.statements.ForStatement;
import eu.cosbi.qspcc.statements.IfStatement;
import eu.cosbi.qspcc.statements.PersistentStatement;
import eu.cosbi.qspcc.statements.ReturnStatement;
import eu.cosbi.qspcc.statements.Statement;
import eu.cosbi.qspcc.statements.SwitchStatement;
import eu.cosbi.qspcc.statements.WhileStatement;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.qspcc.type.StatementType;

public class FrontEndWalker implements DAGListener<AAST, AASTNode, Object>, MiddleEndPass {
    private Logger logger = LogManager.getLogger(FrontEndWalker.class);

    protected List<Statement> prevStatements = new ArrayList<Statement>();
    protected Statement curStatement = null;
    protected StatementClass.Type curStmtType;

    protected List<String> outputSymbols;
    protected List<AASTNode> params;
    // true if we are inside a sub-block definition, i.e. function definition
    protected boolean functionDefinition = false;
    // true if we are inside an anonymous function definition
    protected boolean anonFunctionDefinition = false;

    // name found in function definition
    protected String name;

    // present only on function walkers
    protected CompilerBackend fbackend;

    // remember that we removed childs to avoid translation of subchilds
    protected boolean deferTranslation = false;
    private AASTNode restartTranslationFrom;

    // AAST we are annotating
    protected AAST mainAAST;
    // the function name we are currently defining
    protected AASTNode functionNode;

    // CHILD MODE walk on a specific statement list of an AAST
    private AASTNode statement_list;
    // for debug purposes. in production false.
    protected boolean stopOnError;

    // needed if this is a function sub-block to infer types of arguments
    public FrontEndWalker(AAST mainAAST) {
	this.mainAAST = mainAAST;
	statement_list = null;
	this.params = new ArrayList<AASTNode>();
	this.outputSymbols = new ArrayList<String>();
    }

    public FrontEndWalker(AAST mainAAST, AASTNode statement_list) {
	this.mainAAST = mainAAST;
	this.statement_list = statement_list;
	this.params = new ArrayList<AASTNode>();
	this.outputSymbols = new ArrayList<String>();
    }

    /**
     * first pass annotation on types
     * 
     * @throws Exception
     */
    @Override
    public void annotate(boolean stopOnError) throws GException {
	this.stopOnError = stopOnError;
	if (statement_list == null)
	    mainAAST.rootNode().walkFor(this, stopOnError);
	else
	    // child mode: walk only on this statement list
	    statement_list.walkFor(this, true, stopOnError);
    }

    // different from null only if this is a function walker
    public CompilerBackend functionBackend() {
	return fbackend;
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	if (deferTranslation) {
	    if (restartTranslationFrom.equals(node)) {
		endFunctionBody(node);
	    } else
		return;
	}

	try {
	    // statements can update their own environment internally
	    // only assign statements and function statements actually add
	    // symbols
	    // to their environment
	    switch (node.type()) {
	    case PARAMETER_LIST:
	    case FUNCTION_RETURN:
		break;
	    case PROGRAM:
		break;
	    case STATEMENT_LIST:
		// anonymous function definition in the onExit of the
		// PARAMETER_LIST
		// node (since it is defined inline)
		if (functionDefinition) {
		    // save node from which we should restart the translation
		    // after a
		    // function definition has been found
		    restartTranslationFrom = findRestartNode(node, NodeType.FUNCTION);
		    beginFunctionBody(node, functionNode, name, outputSymbols, params);
		} else if (curStatement != null && curStatement.type().equals(StatementType.CONTROL))
		    // STATEMENT_LIST is available as a node only for control
		    // nodes
		    // (group the then/else blocks)
		    genericTranslateStart(node, node.name());

		break;

	    // ----------- SUB-STATEMENTS ALLOCATION ------------
	    case SWITCH:
	    case IF:
	    case WHILE:
	    case FOR:
	    case PARFOR:
	    case EXPR_STMT:
	    case CLEAR:
	    case CLEARVARS:
	    case CLOSE:
	    case HOLD:
	    case GRID:
	    case FORMAT:
	    case GLOBAL:
	    case GASSIGN:
	    case ASSIGN:
	    case RETURNS:
	    case PERSISTENT:
		allocateStatement(aast, node);
		break;

	    case NL:
	    case SEMI:
	    case LINECOMMENT:
		// if functional statement reset it at end of line
		if (curStatement != null && curStatement.type().equals(StatementType.FUNCTIONAL))
		    resetCurStatement();
		break;
	    case FUNCTION:
		functionNode = node;
		functionDefinition = true;
		logger.debug("------ BEGIN FUNCTION DEFINITION: '" + node.child(NodeType.ID).name() + "' -----------");
		break;

	    case ID:
	    case DEFAULT_VALUE:
	    case INT:
	    case STRING:
	    case FLOAT:
	    case VOID:
		if (functionDefinition || anonFunctionDefinition) {
		    if (!node.type().equals(NodeType.ID))
			// node id can be also a function name.
			// the logic for the right id name is in symbol() method
			node.attr(NodeAttr.SYMBOL, node.name());
		    if ((node.type().equals(NodeType.ID) && !node.parentExists(NodeType.DEFAULT_VALUE))
			    || node.type().equals(NodeType.VOID)) {
			if (node.parentExists(NodeType.FUNCTION_RETURN)) {
			    node.attr(NodeAttr.IS_FUNCTION_OUTPUT, true);
			    outputSymbols.add(node.name());
			} else if (node.parentExists(NodeType.PARAMETER_LIST)) {
			    node.attr(NodeAttr.IS_FUNCTION_PARAM, true);
			    params.add(node);
			} else if (node.parentExists(NodeType.FUNCTION, 1)) {
			    node.attr(NodeAttr.CALLABLE, true);
			    name = node.name();
			}
		    } else if (node.parentExists(NodeType.DEFAULT_VALUE)) {
			// everything done inside beginFunctionBody
		    }
		} else
		    genericTranslateStart(node, node.name());
		break;
	    case AT:
		if (!node.hasAttr(NodeAttr.REF_CORE_FUNCTION) && (!node.hasAttr(NodeAttr.REF_FUNCTION) || node.attr(NodeAttr.REF_FUNCTION) == null))
		    throw new UndefinedTranslationException(
			    ErrorMessage.INTERNAL_ANONYMOUS_FUNCTION_NODE_WITHOUT_REFERENCE_FUNCTION, node,
			    node.child(NodeType.ID).name());
		if(!node.hasAttr(NodeAttr.REF_CORE_FUNCTION)) {
			functionNode = node;
			anonFunctionDefinition = true;
			// if inside an anonymous function definition
			String functionName = (String) functionNode.attr(NodeAttr.SYMBOL);
			// save node from which we should restart the translation after
			// a
			// function definition has been found
			restartTranslationFrom = findRestartNode(node, NodeType.AT);
			beginFunctionBody(node, functionNode, functionName, outputSymbols, params);
			logger.debug(" ---- BEGIN ANONYMOUS FUNCTION PARAMETER NAMES IDENTIFICATION ---- ");			
		}else
			// this is a core function reference, skip.
			genericTranslateStart(node, node.name());
		break;

	    case LHS:
	    case RHS:
		if (curStatement instanceof AssignStatement) {
		    ((AssignStatement) curStatement).setBranch(node, node.name());
		    // this should be below the precedent line
		    // if (node.type().equals(AASTNode.NodeType.RHS))
		    // ((AssignStatement)
		    // curStatement).addTypeSolvedListener(this);
		}
		genericTranslateStart(node, node.name());
		break;
	    case ELSEIF:
	    case ELSE:
		if (curStatement == null || !(curStatement.type().equals(StatementType.CONTROL)))
		    throw new UndefinedTranslationException(ErrorMessage.USER_SYNTAX_ERROR_ELSE_BEFORE_IF, node,
			    curStatement);
		if (node.parent(NodeType.IF).equals(curStatement.getStatement()))
		    // set branch only if this else/elseif belongs to the if of the current statement
		    // (avoid setting branch to a parent if, if this if is nested
		    curStatement.setBranch(node, node.name());
		genericTranslateStart(node, node.name());
		break;
	    case CASE:
		if (curStatement == null || !(curStatement.type().equals(StatementType.CONTROL)))
		    throw new UndefinedTranslationException(ErrorMessage.USER_SYNTAX_ERROR_CASE_BEFORE_SWITCH, node,
			    curStatement);
		if (node.parent(NodeType.SWITCH).equals(curStatement.getStatement()))
		    // set branch only if this else/elseif belongs to the if of the current statement
		    // (avoid setting branch to a parent if, if this if is nested
		    curStatement.setBranch(node, node.name());
		genericTranslateStart(node, node.name());
		break;

	    default:
		genericTranslateStart(node, node.name());
	    }

	} catch (StatementStackException e) {
	    node.error(new StackError(e.getMessage(), e.getStackTrace()));
	    logger.debug("Statement stack error: " + e.getMessage(), e);
	    throw e;
	} catch (UndefinedTranslationException e) {
	    node.error(new AASTError(e.getMessage(), e.getStackTrace()));
	    logger.debug("Cannot translate: " + e.getMessage(), e);
	    throw e;
	} catch (TypeException e) {
	    node.error(new TypeError(e.getMessage(), e.getStackTrace()));
	    logger.debug("Type error applying operator: " + e.getMessage(), e);
	    throw e;
	} catch (UnfinishedTranslationException e) {
	    node.error(new AASTError(e.getMessage(), e.getStackTrace()));
	    logger.debug("Missing data on stack: " + e.getMessage(), e);
	    throw e;
	} catch (UnboundException e) {
	    node.error(new UnboundError(e.getMessage(), e.getStackTrace()));
	    logger.debug("Reference to unbound variable: " + e.getMessage(), e);
	    throw e;
	}
    }

    private AASTNode findRestartNode(AASTNode node, NodeType function) {
	AASTNode restartTranslationFrom = node.parent();
	AASTNode childRestartFrom = node;
	while (restartTranslationFrom.type().equals(NodeType.EXPRESSION)
		|| restartTranslationFrom.type().equals(function)) {
	    restartTranslationFrom = restartTranslationFrom.parent();
	    childRestartFrom = childRestartFrom.parent();
	}
	// look for eventual childs of restartTranslationFrom we
	// haven't translated yet
	int idx = restartTranslationFrom.childs().indexOf(childRestartFrom);
	if (idx != -1 && idx < restartTranslationFrom.childs().size() - 1) {
	    // next child to be translated
	    restartTranslationFrom = restartTranslationFrom.childs().get(idx + 1);
	}
	return restartTranslationFrom;
    }

    protected void genericTranslateStart(AASTNode node, String tokenValue)
	    throws UnboundException, UndefinedTranslationException, GException {

	logger.trace("adding node '" + node + "' to current statement");

	if (NodeType.FUNCTION_PARAMETER_LIST.equals(node.type()))
	    // add a new parameter list
	    curStatement.pushParameterList(node);

	// push element on stack for curStatement
	if (curStatement != null) {
	    curStatement.add(node);
	    // initialize vector elements list if this node is a vector
	    if (NodeType.VECTOR.equals(node.type()))
		curStatement.initVectorList(node);
	} else
	    throw new UnboundException(ErrorMessage.INTERNAL_STATEMENT_NOT_INITIALIZED, node);

    }

    protected void allocateStatement(AAST aast, AASTNode node) throws GException {
	if (curStatement == null || curStatement.type().equals(StatementType.FUNCTIONAL)) {
	    switch (node.type()) {
	    // -------- CONTROL STATEMENTS --------
	    case SWITCH:
		// IF
		curStatement = new SwitchStatement(aast, node);
		// curStatement.addTypeSolvedListener(this);
		break;
	    case IF:
		// IF
		curStatement = new IfStatement(aast, node);
		// curStatement.addTypeSolvedListener(this);
		break;
	    case PARFOR:
	    case FOR:
		// IF
		curStatement = new ForStatement(aast, node);
		// curStatement.addTypeSolvedListener(this);
		break;
	    case WHILE:
		// IF
		curStatement = new WhileStatement(aast, node);
		// curStatement.addTypeSolvedListener(this);
		break;
	    // ------- FUNCTIONAL STATEMENTS ------
	    case EXPR_STMT:
		// EXPRESSIONS [1,0].*1*2
		// init new free expression (ex: disp('step 1') or 1+2*3
		curStatement = new ExpressionStatement(aast, node);
		// curStatement.addTypeSolvedListener(this);
		break;
	    case CLEAR:
	    case CLOSE:
	    case CLEARVARS:
	    case HOLD:
	    case GRID:
	    case FORMAT:
		// CLEAR CLOSE
		curStatement = new ClearStatement(aast, node);
		break;
	    case GLOBAL:
	    case PERSISTENT:
		curStatement = new PersistentStatement(aast, node);
		break;
	    case ASSIGN:
	    case GASSIGN:
		// ASSIGNMENT a = b
		curStatement = new AssignStatement(aast, node);
		break;
	    case RETURNS:
		curStatement = new ReturnStatement(aast, node);
		break;
	    case LINECOMMENT:
		break;
	    default:
		throw new UndefinedTranslationException(ErrorMessage.INTERNAL_STATEMENT, node);
	    }

	    if (!node.type().equals(NodeType.LINECOMMENT)) {
		// notify optimizer of the new statement
		StatementClass classType = curStatement.getClass().getAnnotation(StatementClass.class);
		if (classType == null)
		    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_MISSING_STATEMENT_TYPE, node);
		curStmtType = classType.category();
		if (curStmtType == null)
		    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_MISSING_STATEMENT_TYPE, node);
	    }

	} else
	    genericTranslateStart(node, node.name());

	logger.debug("STATEMENT: '" + node.name() + "' of type " + curStmtType);
    }

    protected void resetCurStatement() throws GException {
	prevStatements.add(curStatement);
	curStatement.completeStatement();
	// curStatement.removeTypeSolvedListener(this);
	curStatement.close();
	curStatement = null;
	// mainAAST.debugVariables();
	// optimizer.closeStatement();
    }

    protected void beginFunctionBody(AASTNode stmtListNode, AASTNode functionNode, String name,
	    List<String> outputSymbols, List<AASTNode> params) throws GException {
	// those are the parameters defined in the parameters list in the source
	// language.
	// since some languages allow use of variables defined in a "global env"
	// in order to get a generic function definition extra-identifiers not
	// present in the parameters
	// list will be added during the evaluation of the body if needed
	// blockEnv.updateFunctionParamNames(functionNode, outputSymbols,
	// params); // define
	functionNode.attr(NodeAttr.SYMBOL, name);
	AASTNode parameterList = functionNode.child(NodeType.PARAMETER_LIST);
	AASTNode functionReturn = functionNode.child(NodeType.FUNCTION_RETURN);

	if (functionNode.hasAttr(NodeAttr.REF_FUNCTION) && functionNode.attr(NodeAttr.REF_FUNCTION) != null) {
	    functionNode = (AASTNode) functionNode.attr(NodeAttr.REF_FUNCTION);
	    parameterList = functionNode.child(NodeType.PARAMETER_LIST);
	    functionReturn = functionNode.child(NodeType.FUNCTION_RETURN);
	    outputSymbols.clear();
	    for (AASTNode out : functionReturn.childs())
		outputSymbols.add(out.name());
	    params.clear();
	    for (AASTNode in : parameterList.childs())
		params.add(in);
	}

	// add void nodes if missing
	if (parameterList.childs().isEmpty()) {
	    AASTNode voidNode = new AASTNode(parameterList.compilationUnit(), NodeType.VOID,
		    Symbols.getSymbol(NodeType.VOID), parameterList.lineNumber(), parameterList.colNumber(),
		    parameterList);
	    voidNode.expr(GType.get(BType.VOID));
	    parameterList.addChild(voidNode);
	}
	if (params.isEmpty())
	    for (AASTNode param : parameterList.childs()) {
		param.attr(NodeAttr.IS_FUNCTION_PARAM, true);
		if (NodeType.VOID.equals(param.type()))
		    param.expr(GType.get(BType.VOID));
		params.add(param);
	    }

	if (functionReturn.childs().isEmpty()) {
	    AASTNode voidNode = new AASTNode(functionReturn.compilationUnit(), NodeType.VOID,
		    Symbols.getSymbol(NodeType.VOID), functionReturn.lineNumber(), functionReturn.colNumber(),
		    functionReturn);
	    voidNode.expr(GType.get(BType.VOID));
	    functionReturn.addChild(voidNode);
	}
	if (outputSymbols.isEmpty())
	    for (AASTNode ret : functionReturn.childs()) {
		ret.attr(NodeAttr.IS_FUNCTION_OUTPUT, true);
		if (NodeType.VOID.equals(ret.type()))
		    ret.expr(GType.get(BType.VOID));
		outputSymbols.add(ret.name());
	    }

	// true if function parameters already known
	// happens on anonymous functions
	boolean allParametersKnown = true;

	for (AASTNode paramNode : params) {
	    if (paramNode.type().equals(NodeType.VOID)) {
		paramNode.expr(GType.get(BType.VOID));
	    } else {
		paramNode.attr(NodeAttr.IS_FUNCTION_PARAM, true);
		// infer type from default value if possible
		paramNode.defaultType();
		if (paramNode.expr() == null)
		    // because the type can be already set
		    paramNode.expr(GType.get(BType.UNKNOWN), false);

		allParametersKnown &= !paramNode.expr().equals(BType.UNKNOWN);
	    }
	}

	List<AASTNode> outNodes = functionNode.childs(NodeType.FUNCTION_RETURN);

	for (AASTNode outNode : outNodes) {
	    outNode.attr(NodeAttr.IS_FUNCTION_OUTPUT, true);
	    if (outNode.expr() == null)
		// because the type can be already set
		outNode.expr(GType.get(BType.UNKNOWN), false);
	}

	// if procedure finishes with expression and only one result
	AASTNode stmt_list = stmtListNode;
	functionNode.attr(NodeAttr.FUNCTION_OUTPUT, outputSymbols);

	// new walker for this statement list
	if (allParametersKnown) {
	    // check return type: if set -> function type resolved
	    checkAndFlagFunctionNode(functionNode);
	    // if (functionNode.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED))
	    // subFunctionWalk(functionNode);
	} // else
	  // subBlockWalk(stmt_list);

	functionDefinition = false;
	anonFunctionDefinition = false;
	deferTranslation = true;
	this.outputSymbols = outputSymbols;
    }

    private void subFunctionWalk(AASTNode funNode) throws Exception {
	FunctionsWalker functionWalker = new FunctionsWalker(mainAAST, funNode);
	try {
	    functionWalker.annotate(stopOnError);
	} catch (Exception e) {
	    functionNode.error(new AASTError(e.getMessage(), e.getStackTrace()));
	    logger.debug("Error during evaluation of function '" + functionNode + "': " + e.getMessage(), e);
	    throw e;
	}
    }

    // used also by FunctionsWalker/statementWalker
    protected void checkAndFlagFunctionReturnNode(AASTNode functionNode) {
	// for funexpr in functionnode, for returntype in funexpr check if
	// some unknown -> remove attr/setattr otherwise don't remove
	boolean allset;
	if (functionNode.exprs() != null) {
	    allset = functionOutputTypesSet(functionNode);
	    // it's possible that even outputs are already set.
	    // but this doesn't imply we don't have to parse this subfunction
	    // because it can contain other functions/statements to be resolved
	    if (allset && !functionNode.hasAttr(NodeAttr.INHIBITS_RESOLUTION))
		functionNode.attr(NodeAttr.FUNCTION_RESOLVED, true);
	    // inhibition works only 1 time
	    functionNode.removeAttr(NodeAttr.INHIBITS_RESOLUTION);
	}
    }

    private boolean functionOutputTypesSet(AASTNode functionNode) {
	Iterator<GType> fexprsIt = functionNode.exprs().iterator();
	while (fexprsIt.hasNext()) {
	    FunctionType curType = ((FunctionType) fexprsIt.next());
	    Iterator<GType> foutIt = curType.outputs().iterator();
	    while (foutIt.hasNext()) {
		if (foutIt.next().equals(BType.UNKNOWN))
		    return false;
	    }
	}
	return true;
    }

    private boolean functionInputTypesSet(AASTNode functionNode) {
	Iterator<GType> fexprsIt = functionNode.exprs().iterator();
	while (fexprsIt.hasNext()) {
	    FunctionType curType = ((FunctionType) fexprsIt.next());
	    Iterator<GType> finIt = curType.inputs().iterator();
	    while (finIt.hasNext()) {
		if (finIt.next().equals(BType.UNKNOWN))
		    return false;
	    }
	}
	return true;
    }

    protected void checkAndFlagFunctionNode(AASTNode functionNode) {
	// for funexpr in functionnode, for returntype in funexpr check if
	// some unknown -> remove attr/setattr otherwise don't remove
	boolean allset;
	if (functionNode.childsEmpty(NodeType.PARAMETER_LIST)) {
	    // if no function parameters
	    functionNode.attr(NodeAttr.FUNCTION_INPUTS_RESOLVED, true);
	} else if (functionNode.exprs() != null) {
	    allset = functionInputTypesSet(functionNode);
	    // it's possible that even outputs are already set.
	    // but this doesn't imply we don't have to parse this subfunction
	    // because it can contain other functions/statements to be resolved
	    if (allset)
		functionNode.attr(NodeAttr.FUNCTION_INPUTS_RESOLVED, true);
	}
    }

    protected void endFunctionBody(AASTNode node) throws UnboundException, GException {
	AASTNode funNode = functionNode;
	if (funNode.hasAttr(NodeAttr.REF_FUNCTION) && funNode.attr(NodeAttr.REF_FUNCTION) != null)
	    funNode = (AASTNode) funNode.attr(NodeAttr.REF_FUNCTION);

	AASTNode stmt_list = funNode.child(NodeType.STATEMENT_LIST);

	AASTNode lastStmt = stmt_list.childs().get(stmt_list.childs().size() - 1);
	List<AASTNode> outSymbols = funNode.childs(NodeType.FUNCTION_RETURN);
	List<AASTNode> params = funNode.childs(NodeType.PARAMETER_LIST);
	// if last one expr statement, implicitly assign to function
	// return node
	if (lastStmt.expr() != null && lastStmt.type().equals(NodeType.EXPR_STMT)) {
	    // this is the output value.
	    // copy to outSymbols
	    for (AASTNode outsymbol : outSymbols) {
		outsymbol.expr(GType.get(lastStmt.expr()));
	    }
	}

	// resolve as it is (all unknown until functions walker is called)
	// but at least recognize this as a function, this allows 
	// further pass to recognize that this symbol is a function
	GType[] rtypes = new GType[outSymbols.size()];
	for (int r = 0; r < outSymbols.size(); ++r) {
	    rtypes[r] = outSymbols.get(r).expr();
	}
	GType[] ptypes = new GType[params.size()];
	for (int r = 0; r < params.size(); ++r) {
	    ptypes[r] = params.get(r).expr();
	}
	// now I have list of return types and list of input types
	functionNode.expr(GType.get(BType.FUNCTION, ptypes, rtypes));

	if (curStatement != null) {
	    // remove nodes to be translated until node type function (included)
	    // because they were already handled
	    AASTNode nextNodeToBeTranslated = curStatement.removeNext();
	    while (!nextNodeToBeTranslated.equals(restartTranslationFrom.parent()))
		nextNodeToBeTranslated = curStatement.removeNext();
	    // readd last one (the actual node we should restart the translation
	    // from)
	    curStatement.add(nextNodeToBeTranslated);
	    // this function (already translated) is the new node on the stack
	    curStatement.pushTranslation(functionNode);
	    updateParameters(functionNode);
	}

	logger.debug("------ END FUNCTION DEFINITION: '" + functionNode.name() + "' -----------");
	// reset
	this.functionNode = null;
	this.restartTranslationFrom = null;
	this.outputSymbols = new ArrayList<String>();
	this.params = new ArrayList<AASTNode>();
	this.name = null;
	// restart walking
	this.deferTranslation = false;
    }

    @Override
    public void onExit(AAST aast, AASTNode node, Deque<Object> results) throws GException {

	if (node.type().equals(NodeType.NL) || node.type().equals(NodeType.SEMI) || node.type().equals(NodeType.PROGRAM)
		|| functionDefinition || anonFunctionDefinition || deferTranslation || curStatement == null) {
	    // do nothing
	} else if (node.equals(curStatement.getStatement()) && curStatement.type().equals(StatementType.CONTROL)) {
	    // if node is statement_list and curStatement != null (thanks to
	    // previous if)
	    resetCurStatement();
	} else {
	    curStatement.translate(aast, node);

	    // update list of parameters for function call..
	    updateParameters(node);
	}

    }

    protected void updateParameters(AASTNode node) throws GException {
	if (NodeType.FUNCTION_PARAMETER_LIST.equals(node.type()))
	    curStatement.popParameterList(node);
	else if (NodeType.FIELDACCESS.equals(node.type()))
	    curStatement.resetStructList(node);
	else if (curStatement.existsPreviousNodeType(NodeType.FIELDACCESS, 1) && !NodeType.DOT.equals(node.type()))
	    curStatement.updateStructList(node);
	else if (curStatement.existsPreviousNodeType(NodeType.FUNCTION_PARAMETER_LIST, 1))
	    curStatement.updateLastParameterList(node);
	// update list of elements belonging to a vector..
	else if (curStatement.existsPreviousNodeType(NodeType.VECTOR, 1))
	    curStatement.updateVectorList(node);
    }

    @Override
    public void onWalkCompleted(AAST aast) throws GException {
    }

    /**
     * function name or null if this block is not a function
     */
    public String name() {
	return name;
    }

    @Override
    public void onWalkStarted(AAST aastRef) throws GException {
    }

}
