package eu.cosbi.qspcc.statements;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.MissingNodeException;
import eu.cosbi.qspcc.exceptions.StatementStackException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.exceptions.UnfinishedTranslationException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;
import eu.cosbi.qspcc.type.StatementType;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

public abstract class Statement {
    Logger logger = LogManager.getLogger(Statement.class);
    public static String NL = "\n";

    protected AASTNode statement = null;
    Tuple<GType, String> translatedStatement = null;
    Deque<AASTNode> nodesStack = null;
    // count function parameters call
    protected List<AASTNode> struct_list = null;
    protected Deque<List<AASTNode>> parametersStack = null;
    protected Map<AASTNode, List<AASTNode>> vector_list = null;
    protected AAST aast;

    public Statement(AAST aast, AASTNode fullStatement) {
	init(aast, fullStatement);
	// init stack of sub-expressions to be translated
	nodesStack = new ArrayDeque<AASTNode>();
    }

    /**
     * create a new statement in "child" mode meaning that it will share the
     * nodesStack with the parent
     * 
     * @param fullStatement
     */
    public Statement(AAST aast, Deque<AASTNode> parentNodesStack, AASTNode fullStatement) {
	init(aast, fullStatement);
	// init stack of sub-expressions to be translated
	nodesStack = parentNodesStack;
    }

    protected void init(AAST aast, AASTNode fullStatement) {
	this.aast = aast;
	statement = fullStatement;
	resetParameterList(null);
	resetVectorList(null);
	resetStructList(null);
    }

    public void close() {
    }

    // -------------- SYMBOL LISTENER IMPLEMENTATION -----------------------
    // @Override
    // public void onNewSymbols(Tuple<ExprType, String> el, String symbol,
    // SymbolType stype, List<String> dimensions)
    // throws UndefinedTranslationException {
    // statementEnv.updateSingleSymbol(stype, el.first(), symbol, dimensions);
    // }
    // --------------- END ---------------------------------------------------

    public void add(AASTNode node) throws UnboundException {
	nodesStack.push(node);
    }

    public AASTNode removeNext() throws UnboundException {
	return nodesStack.pop();
    }

    /**
     * meant to be used by subclasses to check the current context
     * 
     * @param nodeType
     * @return
     * @throws UnboundException
     */
    public boolean existsPreviousNodeType(NodeType nodeType) throws UnboundException {
	for (AASTNode tp : nodesStack)
	    if (tp.type().equals(nodeType))
		return true;
	return false;
    }

    public String previousNodeTypeValue(NodeType nodeType) throws MissingNodeException {
	for (AASTNode tp : nodesStack)
	    if (tp.type().equals(nodeType))
		return tp.name();
	throw new MissingNodeException(ErrorMessage.INTERNAL_STRUCTURE_EXPECTED_NODE_NOT_FOUND, statement, nodeType,
		nodesStack);
    }

    /**
     * get previous node, if it has the expected type
     * 
     * @param type
     * @return
     * @throws Exception
     */
    private AASTNode previousNode(NodeType type) throws MissingNodeException {
	AASTNode prev = nodesStack.peekFirst();
	if (prev.type() == type)
	    return prev;
	throw new MissingNodeException(ErrorMessage.INTERNAL_STRUCTURE_UNEXPECTED_PREVIOUS_NODE, statement, prev, type);
    }

    public boolean existsPreviousNodeType(NodeType nodeType, int depth) throws UnboundException {
	for (AASTNode tp : nodesStack) {
	    if (depth-- == 0)
		break;
	    if (tp.type() == nodeType)
		return true;
	}
	return false;
    }

    /**
     * Translate a closed sub-tree (ex. TIMES 1 2 ) and fills the translation
     * string.
     * 
     * @param subTree the closed sub-tree to be translated
     * @return
     * @throws StatementStackException
     * @throws TypeException
     * @throws UndefinedTranslationException
     * @throws UnfinishedTranslationException
     */
    public void translate(AAST aast, AASTNode subTree) throws GException {
	// retrieves and remove
	AASTNode tp = null;
	if (subTree.type().equals(NodeType.LINECOMMENT))
	    return;
	try {
	    tp = nodesStack.pop();
	} catch (NoSuchElementException e) {
	    logger.debug(new StatementStackException(ErrorMessage.INTERNAL_STRUCTURE_STACK_EMPTY, subTree).getMessage(),
		    e);
	    // failed to translate, this happens when the nodes down the tree generated an
	    // error before
	    return;
	}

	AASTNode node = tp;
	String value = tp.name();
	// sanity check
	if (node.type() != subTree.type())
	    throw new StatementStackException(ErrorMessage.INTERNAL_STRUCTURE_UNEXPECTED_NODE_ON_STACK, subTree, node);

	translateImpl(aast, node, value);
    }

    /**
     * called on the onExit of a auto-contained sub-tree that can be translated
     * 
     * @param node
     * @param value
     * @throws Exception
     */
    public abstract void translateImpl(AAST aast, AASTNode node, String value) throws GException;

    /**
     * called on newline / ; before going on and parsing a new statement
     * 
     * @throws UnboundException
     * @throws TypeException
     * @throws UndefinedTranslationException
     * @throws UnfinishedTranslationException
     * @throws Exception
     */
    public abstract void completeStatement() throws GException;

    /**
     * called on the onEnter of the N+1-th level of this statement (useful to
     * discriminate what branch we are on, ex. LHS/RHS)
     * 
     * @param type
     * @throws UnboundException
     */
    public abstract void setBranch(AASTNode node, String tokenValue) throws UnboundException;

    /**
     * used by upper classes if they have to provide a custom translation
     * 
     * @return
     */
    public abstract void pushTranslation(AASTNode tp);

    /**
     * return the type of this statement
     * 
     * @return
     */
    public abstract StatementType type();

    /**
     * get initial statement
     * 
     * @return
     */
    public AASTNode getStatement() {
	return statement;
    }

    // STACK IMPLEMENTATION OF PARAMETERS LIST
    private void resetParameterList(AASTNode node) {
	parametersStack = new ArrayDeque<List<AASTNode>>();
    }

    public void pushParameterList(AASTNode node) {
	parametersStack.push(new ArrayList<AASTNode>());
    }

    public void popParameterList(AASTNode node) {
	parametersStack.pop();
    }

    public void updateLastParameterList(AASTNode node) {
	parametersStack.peek().add(node);
    }

    public List<AASTNode> lastParameterList() {
	return parametersStack.peek();
    }

    // STACK IMPLEMENTATION OF PARAMETERS LIST

    public void resetStructList(AASTNode node) {
	struct_list = new ArrayList<AASTNode>();
    }

    public void updateStructList(AASTNode node) {
	struct_list.add(node);
    }

    public void resetVectorList(AASTNode node) {
	vector_list = new HashMap<AASTNode, List<AASTNode>>();
    }

    public void initVectorList(AASTNode node) throws GException {
	if (!NodeType.VECTOR.equals(node.type()))
	    throw new TypeException(ErrorMessage.INTERNAL_VECTOR_STRUCTURE, node);
	vector_list.put(node, new ArrayList<AASTNode>());
    }

    public void updateVectorList(AASTNode node) throws GException {
	vector_list.get(previousNode(NodeType.VECTOR)).add(node);
    }

    /**
     * mark this statement (and his dependencies) to be reparsed
     * 
     * @author tomasoni
     */
    protected void markToBeReparsed() {
	TypeUtils.markStatementToBeReparsed(statement, false);
    }

    protected void markAsCompleted() {
	// automatically remove from list of still not defined nodes
	if (statement.hasAttr(NodeAttr.UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION)) {
	    statement.removeAttr(NodeAttr.UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION);
	}
    }

    protected GType specialIndexType(AASTNode node, BType specialIndexType) throws TypeException {
	if (node.parentExists(NodeType.APPLY)) {
	    // search backward for apply node
	    AASTNode parent = node.parent();
	    int parameter_list_position = -1;
	    while (!parent.type().equals(NodeType.APPLY)) {
		if (parent.type().equals(NodeType.FUNCTION_PARAMETER_LIST)) {
		    // record my position in parameter list
		    List<AASTNode> paramlist = parent.childs();
		    for (int i = 0; i < paramlist.size(); ++i)
			if (paramlist.get(i).parentOf(node)) {
			    parameter_list_position = i;
			    break;
			}
		}
		parent = parent.parent();
	    }
	    // the first parameter is the element we are accessing
	    AASTNode applyMatrix = parent.childs().get(0);
	    // get id of APPLY (the matrix we are accessing)
	    if (applyMatrix.expr() == null || applyMatrix.expr().equals(BType.UNKNOWN)
		    || applyMatrix.expr().equals(BType.UNDEFINED))
		return GType.get(specialIndexType);// GType.get(BType.UNKNOWN); no.. because it's needed by top nodes

	    // all elements of the n-th level of the struct (ex. lists in R)
	    else if (applyMatrix.expr() instanceof StructType) {
		throw new TypeException(ErrorMessage.UNSUPPORTED_STRUCT_ACCESS_BY_INDEX, node);
		// this is true even for ValuedTypes
		// because a(end) where a is INT is a itself. Needed by
		// matlab.
	    } else if (applyMatrix.expr() instanceof DimsType) {
		DimsType mtype = (DimsType) applyMatrix.expr();
		IntType dim = TypeUtils.firstNonEmptyDim(mtype.dims(), parameter_list_position).first();

		if (dim == null)
		    throw new TypeException(ErrorMessage.INTERNAL_UNSUPPORTED_SLICE_TYPE, node, applyMatrix,
			    specialIndexType);
		else {
		    // take out as the dimension of the matrix we
		    // are accessing
		    // use the first non-1 position
		    // IntType outEnd = dims[parameter_list_position];
		    return GType.get(specialIndexType, applyMatrix, dim);
		}
	    } else
		throw new TypeException(ErrorMessage.INTERNAL_UNSUPPORTED_SLICE_TYPE, node, applyMatrix,
			specialIndexType);

	} else
	    // end of what?
	    return GType.get(specialIndexType); // GType.get(BType.UNKNOWN); no.. because it's needed by top nodes
    }

}
