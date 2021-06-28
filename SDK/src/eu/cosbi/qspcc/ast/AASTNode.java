package eu.cosbi.qspcc.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.attrs.Attr;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAG;
import eu.cosbi.qspcc.exceptions.AASTError;
import eu.cosbi.qspcc.exceptions.ErrorCode;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.SliceType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.utils.Quadruple;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

/**
 * Represents a frontend AST node
 * 
 * @author tomasoni
 *
 */
public class AASTNode extends DAG<AAST, AASTNode> implements ProgramNode {
    Logger logger = LogManager.getLogger(AASTNode.class);

    protected NodeType type;
    private String nodeText;
    // the node attributes for this specific AAST
    private Map<Attr, Object> attributes;
    private Integer line;
    private Integer charPosition;

    private String nodeId;

    public AASTNode(AAST aast, NodeType type, String statement) {
	super(aast, null);
	this.type = type;
	this.nodeId = Symbols.getSymbol(type);
	this.nodeText = statement;
	init(type, nodeText, parent);
    }

    public AASTNode(AAST aast, NodeType type, String statement, Integer line, Integer charPosition, AASTNode parent) {
	super(aast, parent);
	this.type = type;
	this.nodeId = Symbols.getSymbol(type);
	this.line = line;
	this.charPosition = charPosition;
	nodeText = statement;
	init(type, nodeText, parent);
    }

    /**
     * deep copy constructor
     * 
     * @param node
     */
    public AASTNode(AASTNode node) {
	super(node.treeRef, node.parent);
	deepcopy(node);
    }

    private void assignBaseAttributes(AASTNode node) {
	this.type = node.type;
	this.nodeId = Symbols.getSymbol(type);
	this.line = node.line;
	this.charPosition = node.charPosition;
	this.nodeText = node.nodeText;
    }

    private void deepcopy(AASTNode node) {
	assignBaseAttributes(node);
	// deep copy of childs
	for (AASTNode child : node.childs)
	    addChild(new AASTNode(child));
	init(type, nodeText, parent);
    }

    void init(NodeType type, String statement, AASTNode parent) {
	attributes = new HashMap<Attr, Object>();
	// notifies tree of this new ID
	// check on treeRef needed because core functions doesn't have a parent
	// tree
	if (treeRef != null && type != null && type.equals(NodeType.ID)) {
	    treeRef.newVariable(this, nodeText);
	    if (parent != null && parent.type().equals(NodeType.FUNCTION))
		treeRef.newFunction(parent, nodeText);
	    return;
	}

    }

    /**
     * remove this node from AAST tree, de-register it from symboltable and so on.
     */
    public void unlink() {
	if (treeRef != null && type != null && type.equals(NodeType.ID)) {
	    treeRef.delVariable(this, nodeText);
	    if (parent != null && parent.type().equals(NodeType.FUNCTION))
		treeRef.delFunction(parent, nodeText);
	}
	attributes = null;
	parent = null;
	treeRef = null;
	childs = null;
    }

    @Override
    public int compareTo(ProgramNode o2) {
	int l = this.lineNumber().compareTo(((AASTNode) o2).lineNumber());
	if (l == 0) {
	    return this.colNumber().compareTo(((AASTNode) o2).colNumber());
	} else
	    return l;
    }

    public AAST compilationUnit() {
	return treeRef;
    }

    public Integer lineNumber() {
	return line == null ? 0 : line;
    }

    public Integer colNumber() {
	return charPosition == null ? 0 : charPosition;
    }

    /**
     * @param nodes      the list of nodes to be filtered
     * @param targetNode the node that represent our actual position in tree
     * @return nodes variable, where each AASTNode element that belongs to a
     *         different env has been removed
     */
    public List<AASTNode> excludeDifferentEnvNodes(List<AASTNode> nodes) {
	List<AASTNode> sameEnv = new ArrayList<AASTNode>();
	// can be null
	AASTNode parentFunction = this.parent(NodeType.FUNCTION);
	AASTNode parent = this.parent();
	boolean inFunction = parentFunction != null;
	boolean funParam = inFunction && parentFunction.child(NodeType.PARAMETER_LIST).childPosition(this.code()) >= 0;
	boolean funOutput = inFunction
		&& parentFunction.child(NodeType.FUNCTION_RETURN).childPosition(this.code()) >= 0;
	boolean funLocalVar = this.hasAttr(NodeAttr.IS_FUNCTION_LOCAL_VAR);
	boolean isInStruct = parent != null && parent.type().equals(NodeType.FIELDACCESS);
	boolean limitChangesToFun = inFunction && (funParam || funOutput || funLocalVar);

	// list of predecessors in node inside structure
	// ex. a.b.c node is c[ID12] structPredecessors is [a, b]
	List<String> structPredecessors = null;
	if (isInStruct) {
	    structPredecessors = new LinkedList<String>();
	    structPredecessors.addAll(parent.structPredecessors(this));
	}

	if (nodes != null)
	    for (AASTNode node : nodes)
		// do not update myself
		if (!node.equals(this)
			// global variables should always be included
			&& (node.hasAttr(NodeAttr.GLOBAL_VAR) ||
			// do not update if in a different
			// environment (if in a different function)
				((!limitChangesToFun
					// or in the same function
					|| node.parent(NodeType.FUNCTION) == parentFunction
					// or in a subfunction that is an env param of the parentFunction
					|| (inFunction && parentFunction.parentOf(node.parent(NodeType.FUNCTION))
						&& node.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM)))
					// do not update if outside function (or env param) and this
					// node is in a function
					&& (limitChangesToFun
						// if in function, and this in function, the function of this should be
						// parent of
						// the function of node
						// f(){ a = 1; g{ a + 1; }() } yes
						// f(){ a = 1; } g(){ a + 1; } no
						|| ((!node.parentExists(NodeType.FUNCTION)
							|| (inFunction && node.parent(NodeType.FUNCTION)
								.parentOf(parentFunction) /* && funEnvVar */)
							// a = 1; g{ a+1 }() yes
							|| (!inFunction
								&& node.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM)))))))
			// check if this node is in the same structure
			// of node == this, or it isn't in any structure
			&& (
			// both don't belong to structure
			(!isInStruct && (node.parent() == null || !node.parent().type().equals(NodeType.FIELDACCESS)))
				// both belong to structure and they have the same path
				|| (isInStruct && node.parent() != null
					&& node.parent().type().equals(NodeType.FIELDACCESS))
					&& structPredecessors.equals(node.parent().structPredecessors(node))
				// or this : a.b.c = []; -> update rhs of b=a;
				|| (isInStruct && (node.parent() == null
					|| !node.parent().type().equals(NodeType.FIELDACCESS)))
					&& structPredecessors.isEmpty()
				// or this : a = b; -> update lhs of a.b.c = [];
				|| (!isInStruct && node.parent() != null
					&& node.parent().type().equals(NodeType.FIELDACCESS))
					&& node.parent().structPredecessors(node).isEmpty()))
		    sameEnv.add(node);

	return sameEnv;
    }

    /**
     * get name of childrens excluding the ones that match the given nodetype
     * 
     * @param dot
     * @return
     */
    public List<String> structPredecessors(AASTNode upTo) {
	List<String> r = new LinkedList<String>();
	for (AASTNode n : childs())
	    if (n.equals(upTo))
		// stop when current node reached
		// a.b.c and a.b have just a,b in common
		break;
	    else if (!n.type().equals(NodeType.DOT))
		r.add(n.name());
	return r;
    }

    public boolean parentExists(NodeType[] types) {
	for (NodeType t : types)
	    if (parentExists(t))
		return true;
	return false;
    }

    public boolean parentExists(List<NodeType> types) {
	for (NodeType t : types)
	    if (parentExists(t))
		return true;
	return false;
    }

    public boolean parentExists(List<NodeType> types, int depth) {
	for (NodeType t : types)
	    if (parentExists(t, depth))
		return true;
	return false;
    }

    public boolean parentExists(NodeType type) {
	// no limit
	return parentExists(type, 0);
    }

    public boolean hasType(List<NodeType> types) {
	for (NodeType t : types)
	    if (type().equals(t))
		return true;
	return false;
    }

    public boolean hasType(NodeType[] types) {
	for (NodeType t : types)
	    if (type().equals(t))
		return true;
	return false;
    }

    public boolean parentExists(NodeType type, int depth) {
	AASTNode curParent = parent;
	for (int i = 0; i < depth || depth == 0; ++i) {
	    if (curParent == null)
		return false;

	    if (curParent.type().equals(type))
		return true;

	    curParent = curParent.parent();
	}
	return false;
    }

    public Integer parentDepth(NodeType[] types) {
	Integer mindepth = null;
	for (NodeType type : types) {
	    Integer depth = parentDepth(type);
	    if (mindepth == null || depth != null && depth < mindepth)
		mindepth = depth;
	}
	return mindepth;
    }

    public Integer parentDepth(NodeType type) {
	Tuple<AASTNode, Integer> tp = _parent(type, 0);
	if (tp != null)
	    return tp.second();
	return null;
    }

    /**
     * the parent node just before the parent with type 'type'
     * 
     * @param type
     * @return
     */
    public AASTNode parentBefore(NodeType type) {
	AASTNode curParent = this;
	while (curParent != null) {
	    if (curParent.parent().type().equals(type))
		return curParent;

	    curParent = curParent.parent();
	}
	return null;
    }

    public AASTNode parent(NodeType[] types) {
	return parent(types, 0);
    }

    public AASTNode parent(NodeType[] types, int depth) {
	AASTNode curParent = parent;
	for (int i = 0; i < depth || depth == 0; ++i) {
	    if (curParent == null)
		return null;

	    for (NodeType type : types)
		if (type.equals(curParent.type()))
		    return curParent;

	    curParent = curParent.parent();
	}
	return null;
    }

    public AASTNode parent(List<NodeType> types) {
	return parent(types.toArray(new NodeType[types.size()]), 0);
    }

    public AASTNode parent(List<NodeType> types, int depth) {
	return parent(types.toArray(new NodeType[types.size()]), depth);
    }

    @Override
    public ProgramNode parentFunction() {
	if (this.type().equals(NodeType.FUNCTION))
	    return this;
	else {
	    return parent(NodeType.FUNCTION);
	}
    }

    @Override
    public ProgramNode nearestExpression() {
	return parent(TypeUtils.expressionLimitNodes());
    }

    public AASTNode parent(NodeType type) {
	return parent(new NodeType[] { type }, 0);
    }

    private Tuple<AASTNode, Integer> _parent(NodeType type, int depth) {
	if (parent == null)
	    return null;
	if (parent.type().equals(type))
	    return new Tuple<AASTNode, Integer>(parent, depth + 1);
	else
	    return parent._parent(type, depth + 1);
    }

    public String name() {
	return nodeText;
    }

    /**
     * get unique name that takes into consideration if this id is in a struct
     * 
     * @return
     */
    public String fullname() {
	return completename(GType.STRUCT_SEP);
    }

    /**
     * get unique name for structure name
     * 
     * @return
     */
    public String structname() {
	return completename(StructType.NESTED_STRUCT_NAME_SEP);
    }

    private String completename(String sep) {
	if (parentExists(NodeType.FIELDACCESS)) {
	    AASTNode fieldAccessParent = parent(NodeType.FIELDACCESS);
	    if (!fieldAccessParent.childs().isEmpty())
		return String.join(sep, fieldAccessParent.structPredecessors(this)) + sep + name();
	    else
		return name();
	} else
	    return name();
    }

    @Override
    public String id() {
	return nodeId;
    }

    @Override
    public NodeType type() {
	return type;
    }

    @Override
    public String typeAsString() {
	return type.toString();
    }

    public void type(NodeType newtype) {
	this.type = newtype;
    }

    public boolean equals(NodeType o) {
	return type().equals(o);
    }

    public String toStringComplete() {
	return this.name() + "[" + nodeId + "]\n\n" + stringAttrs();
    }

    public String toString() {
	return this.name() + "[" + nodeId + "]";
    }

    public IFunction coreFunction() {
	return (IFunction) attributes.get(NodeAttr.CORE_FUNCTION);
    }

    @Override
    public String code() {
	return name();
    }

    @Override
    public String location() {
	return "line " + lineNumber() + ", char " + colNumber();
    }

    public void removeAttr(Attr attrtype) {
	if (attrtype.equals(NodeAttr.UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION) && attributes.containsKey(attrtype)) {
	    AASTNode parentFun = parent(NodeType.FUNCTION);
	    treeRef.popStatementWalk(this, parentFun);
	}
	attributes.remove(attrtype);
    }

    public void attr(Attr attrtype, Object obj) {
	attr(attrtype, obj, false);
    }

    @Override
    public Object attr(Attr attr) {
	return attributes.get(attr);
    }

    public void attr(Attr attrtype, Object obj, boolean updateOthers) {

	boolean notetype = true;
	if (attrtype.equals(NodeAttr.ETYPE)) {
	    notetype = false;
	    if (obj instanceof List)
		exprs((List<GType>) obj, updateOthers);
	    else
		expr((GType) obj, updateOthers);
	} else {

	    if (attrtype.equals(NodeAttr.FUNCTION_RESOLVED) && !this.hasAttr(NodeAttr.FUNCTION_RESOLVED)) {
		// mark this compilation unit to be completely defined
		String name = this.child(NodeType.ID).name();
		treeRef.popWalk(this);
		logger.debug("Function types resolved for '" + name + "' " + this.stringAttrs());
	    } else if (attrtype.equals(NodeAttr.FUNCTION_INPUTS_RESOLVED) && this.childExists(NodeType.STATEMENT_LIST)
	    // and not already marked to be resolved
		    && !this.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED)
		    // and not already resolved
		    && !this.hasAttr(NodeAttr.FUNCTION_RESOLVED)) {
		// mark this statement list in this compilation unit to be
		// reparsed
		String name = this.child(NodeType.ID).name();
		treeRef.pushWalk(this);
		logger.debug("Function '" + name + "' marked to be reparsed with parameters " + this.stringAttrs());
		// if set to be reparsed and not already resolved
	    } else if (attrtype.equals(NodeAttr.REPARSE_STATEMENT) && !this.hasAttr(NodeAttr.REPARSE_STATEMENT)
		    && !this.hasAttr(NodeAttr.STATEMENT_RESOLVED)) {
		pushStatementWalk(attrtype, obj);
		logger.debug("Statement '" + this.name() + "' marked to be reparsed");
		// if it was reparsed and now is resolved
	    } else if (attrtype.equals(NodeAttr.STATEMENT_RESOLVED) && !this.hasAttr(NodeAttr.STATEMENT_RESOLVED)) {
		AASTNode n = (AASTNode) obj;
		// this attr is used by pushStatementsWalk
		attributes.put(attrtype, obj);
		treeRef.popStatementWalk(this, (n.compilationUnit() == null) ? null : n);
		logger.debug("Statement '" + this.name() + "' resolved as " + this.stringAttrs());
	    } else if (attrtype.equals(NodeAttr.UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION)
		    && !this.hasAttr(NodeAttr.UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION)) {
		pushStatementWalk(attrtype, obj);
	    } else if (attrtype.equals(NodeAttr.IS_FUNCTION_ENV_PARAM)
		    && !this.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM)) {
		// add it to the static variables of the parent environment
		AASTNode functionNode = (AASTNode) obj;
		AASTNode parentFunctionNode = functionNode.parent(NodeType.FUNCTION);
		if (parentFunctionNode != null) {
		    while (parentFunctionNode != null && !treeRef.localFunctionSymbolExist(this, parentFunctionNode))
			parentFunctionNode = parentFunctionNode.parent(NodeType.FUNCTION);

		    // parentFunctionNode contains the parent function (or null) that contains
		    // 'this' variable
		    treeRef.updateEnvironmentWithVariable(parentFunctionNode, this, nodeText);
		} else {
		    // i'm inside a script, add my node here
		    treeRef.updateEnvironmentWithVariable(parentFunctionNode, this, nodeText);
		}
	    }

	    attributes.put(attrtype, obj);
	}

	if (notetype && updateOthers)
	    updateOthers(attrtype, obj);
    }

    private void pushStatementWalk(Attr attrtype, Object obj) {
	AASTNode n = (AASTNode) obj;
	// this attr is used by pushStatementsWalk
	attributes.put(attrtype, obj);
	if (n.compilationUnit() == null)
	    treeRef.pushStatementWalk(this, null);
	else
	    treeRef.pushStatementWalk(this, n);
    }

    public boolean hasAttr(Attr value) {
	return attributes.containsKey(value);
    }

    public List<GType> exprs() {
	return (List<GType>) attributes.get(NodeAttr.ETYPE);
    }

    public ValuedType vexpr() {
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	// always work on last type
	return (ValuedType) types.peekLast();
    }

    public IntType iexpr() {
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	// always work on last type
	return (IntType) types.peekLast();
    }

    public StructType sexpr() {
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	// always work on last type
	return (StructType) types.peekLast();
    }

    public MatrixType mexpr() {
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	// always work on last type
	return (MatrixType) types.peekLast();
    }

    public SliceType slexpr() {
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	// always work on last type
	return (SliceType) types.peekLast();
    }

    public GType expr() {
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	if (types == null || types.isEmpty())
	    return null;

	// always work on last type
	return types.peekLast();
    }

    public void expr(GType newtype) {
	// if not unknown update everywhere in the tree
	expr(newtype, !GType.get(BType.UNKNOWN).equals(newtype));
    }

    public void expr(GType newtype, boolean updateOthers) {
	// ADD newtype to array
	LinkedList<GType> types = (LinkedList<GType>) attributes.get(NodeAttr.ETYPE);
	// type actually changed
	boolean typeUpdated = false;
	// type was really updated or we just re-set the same type again?
	boolean typeReset = false;
	// type was not necessarily updated, but value was updated
	// this is true from the second times on that a node is assigned
	boolean valueUpdated = false;
	// if updated value is exactly the same as the previous
	boolean exactlyEqual = false;
	boolean type_array_initialized = types != null;
	// second part of if is important because limits multiple types to
	// functions only.
	// functions can be polimorphic and depending on inputs variables can
	// have different types
	// in non-functions this cannot happen.
	if (!type_array_initialized || (!parentExists(NodeType.FUNCTION) && !type().equals(NodeType.FUNCTION))) {
	    // if no type, or i'm not inside function definition
	    GType type = null;
	    // only 1 type allowed here
	    if (type_array_initialized) {
		// always reuse the same list
		// so that the other nodes that use the same list
		// will be automatically updated
		type = types.peekFirst();
		types.clear();
	    } else {
		// initialize type array for this node. this will always remain the same
		// and will be shared among all the nodes that represent the same variable
		// so that we can change array values as we want and automatically update
		// the type of all the nodes that share the same array
		types = new LinkedList<GType>();
	    }

	    Quadruple<Boolean, Boolean, Boolean, GType> status = compareType(type, newtype);
	    typeUpdated = status.first();
	    typeReset = status.second();
	    valueUpdated = status.third();
	    GType foundType = status.fourth();

	    if (foundType != null) {
		types.add(foundType);
	    } else {
		// new type is not compatible with old one
		// throw old away and use just the new one
		if (!hasAttr(NodeAttr.USER_WARNING)) {
		    AASTNode parentStmt = this.parent(TypeUtils.expressionLimitNodes());
		    if (parentStmt == null)
			parentStmt = this;
		    GException e = new GException(ErrorMessage.WARN_VARIABLE_WITH_MULTIPLE_TYPES, parentStmt, this,
			    type, newtype);
		    treeRef.addWarning(e);
		    attr(NodeAttr.USER_WARNING, true);
		}
		types.add(newtype);
	    }
	    attributes.put(NodeAttr.ETYPE, types);
	    GType updatedType = types.peekFirst();
	    if (updatedType != null && updatedType.equals(BType.UNKNOWN))
		// remove previously defined symbol if present
		attributes.remove(NodeAttr.SYMBOL);
	} else {
	    // if type present, and inside function definition
	    boolean found = false;
	    Iterator<GType> it = types.iterator();
	    int i = 0;
	    while (!found && it.hasNext()) {
		GType type = it.next();
		// if unknown or already the same type, replace it
		// BE CAREFUL: type.equals(newtype) checks type and value (if
		// present)
		// type.equals(newtype.type()) checks only type (returns true if
		// value different)
		// we should check against type, not value (@see
		// ExpressionStatement.checkAndUpdateFunctionParameters)
		Quadruple<Boolean, Boolean, Boolean, GType> status = compareType(type, newtype);
		typeUpdated = status.first();
		typeReset = status.second();
		valueUpdated = status.third();
		GType foundType = status.fourth();
		boolean typeWasFound = foundType != null;
		exactlyEqual = typeWasFound && foundType == type;
		if (typeWasFound) {
		    found = true;
		    if (!exactlyEqual)
			types.set(i, foundType);
		}
		++i;
	    }
	    // if all types in array are known and not overridable
	    if (!found) {
		typeUpdated = true;
		// add new one
		types.add(newtype);
	    }

	}

	if (updateOthers && !exactlyEqual && typeUpdated)
	    updateOthers(NodeAttr.ETYPE, new Tuple<List<GType>, Boolean>(types,
		    // if this is true, AAST will mark the corresponding statements
		    // to be reparsed because the type significantly changed
		    // (excluding M[19] -> M[1x19] and similar "equivalent" updates
		    typeReset));
    }

    private Quadruple<Boolean, Boolean, Boolean, GType> compareType(GType type, GType newtype) {
	Boolean typeUpdated = false;
	Boolean typeReset = false;
	Boolean valueUpdated = false;
	GType foundType = null;

	if (type == null || type.equals(BType.UNKNOWN) || type.equals(BType.VOID)) {
	    typeUpdated = newtype != null && !newtype.equals(BType.UNKNOWN);
	    foundType = newtype;
	    if (newtype.equals(BType.UNKNOWN))
		// remove previously defined symbol if present
		attributes.remove(NodeAttr.SYMBOL);
	} else if (newtype == null || newtype.equals(BType.UNKNOWN)) {
	    foundType = type;
	} else if (type.lessGenericThan(newtype)) {
	    // case type=INT newtype=SCALAR
	    typeUpdated = typeReset = !type.deeplyEqual(newtype);
	    valueUpdated = newtype instanceof ValuedType && (typeReset || !type.equalValue(newtype));
	    if (valueUpdated)
		// a valuedtype got a different value than before,
		// remove value from newtype
		// because value is added *ONLY* if it's the only value
		// that this variable will ever get
		((ValuedType<?>) newtype).deleteValue();
	    Tuple<IntType[], Boolean> tp = type.dimsEqual(newtype);
	    if (newtype instanceof MatrixType && typeReset && !tp.second()) {

		if (type.isCastableToMatrix()) {
		    // a matrixtype really updated (with different matrix dimensions)
		    // shouldn't have static matrix dimension values
		    // ex a=[]; a = [a; [1;2]] should be MATRIX[n x n]
		    // even if it will be a nx1 matrix forever (because at first it was a 0D matrix)
		    DimensionType mtype = (DimensionType) type;
		    DimensionType mnewtype = (DimensionType) newtype;
		    try {
			((MatrixType) newtype).updateDims(TypeUtils.newMatrixDimensions(mnewtype.name(),
				mnewtype.dims(), mtype.dims(), mtype.dims().length));
		    } catch (Exception e) {
			// amen
		    }
		    // check again now if the dimensions are equal
		    typeUpdated = typeReset = !type.deeplyEqual(newtype);
		} else {
		    // scalar
		    typeUpdated = typeReset = true;
		}
	    }
	    foundType = newtype;
	} else if (newtype.lessGenericThan(type)) {
	    // case type=SCALAR newtype=INT
	    // case type=MATRIX of INT newtype=INT
	    // keep old one and doesn't update
	    foundType = type;
	} else if (type.equals(BType.STRUCT) && newtype.equals(BType.STRUCT)) {
	    StructType oldType = (StructType) type;
	    StructType newType = (StructType) newtype;
	    // if two structs belongs to the same id, join them, but keep the original input
	    // always
	    foundType = TypeUtils.joinStructs(oldType, newType).setInput(oldType.inputName());
	    typeUpdated = typeReset = !oldType.deeplyEqual(foundType);
	} else if (type.equals(BType.STRUCT)) {
	    foundType = type;
	} else if (newtype.equals(BType.STRUCT)) {
	    typeUpdated = typeReset = true;
	    foundType = newtype;
	}

	return new Quadruple<Boolean, Boolean, Boolean, GType>(typeUpdated, typeReset, valueUpdated, foundType);
    }

    public void exprs(List<GType> types, boolean updateOthers) {
	attributes.put(NodeAttr.ETYPE, types);

	// update related identifiers
	if (updateOthers)
	    updateOthers(NodeAttr.ETYPE, types);
    }

    // object can be array of GType or GType
    private void updateOthers(Attr attr, Object newtype) {
	if (treeRef != null && this.type.equals(NodeType.ID)) {
	    // ATTENTION: possible infinite loop
	    // treeRef can be null for core functions
	    treeRef.updateVariableType(this, attr, newtype);
	}
	// else
	// update subtree types
	// example a*x*2 the toplevel becomes SCALAR -> also a and x
	// should
	// be scalar
	// propagateType(this, type);
    }

    public String symbol() {
	return TypeUtils.getIDNode(this).symbol(null);
    }

    public String symbol(String value) {
	AASTNode node = this;
	String symbol;
	if (node.hasAttr(NodeAttr.SYMBOL))
	    return (String) node.attr(NodeAttr.SYMBOL);
	else if (node.type().equals(NodeType.ID)) {
	    boolean is_function_id = false;
	    if (node.parentExists(NodeType.LHS)
		    || (node.parent() != null && node.parent().equals(NodeType.PARAMETER_LIST)))
		// this is a variable definition, through assignment or function local parameter
		is_function_id = false;
	    else if (node.parent() != null
		    && (node.parent().equals(NodeType.FUNCTION) || node.parent().equals(NodeType.AT)))
		// this is a function definition
		is_function_id = true;
	    else {
		// check if it is a variable or a function defined somewhere
		List<AASTNode> others = compilationUnit().symbols(node);
		boolean variable = false;
		for (AASTNode other : others)
		    if (other.parentExists(NodeType.LHS)) {
			variable = true;
			break;
		    }

		if (!variable)
		    // symbol not defined up as a variable, maybe it's defined down as a function.
		    // check in whole program
		    is_function_id = compilationUnit().functionSymbolExists(node);
	    }

	    if (is_function_id)
		// if function, get new symbol for output
		symbol = Symbols.getSymbol(node);
	    else if (node.parent() != null && node.parent().type().equals(NodeType.FIELDACCESS)
		    && !node.parent().structPredecessors(node).isEmpty())
		symbol = Symbols.getStructSymbol(node.name(), node.parent().structPredecessors(node), GType.STRUCT_SEP);
	    else
		symbol = node.name();
	    node.attr(NodeAttr.SYMBOL, symbol);
	    return symbol;
	} else if (node.type().equals(NodeType.FIELDACCESS)) {
	    // use same id as for the last element of his childrens.
	    symbol = node.childs().get(node.childs().size() - 1).symbol();
	    node.attr(NodeAttr.SYMBOL, symbol);
	    return symbol;
	} else {
	    symbol = value;
	    if (symbol == null)
		symbol = Symbols.getSymbol(this);// nodeId;
	    node.attr(NodeAttr.SYMBOL, symbol);
	    logger.debug("Added symbol '" + symbol + "' on " + node + ": " + node.stringAttrs());
	    return symbol;
	}
    }

    public String stringAttrs() {
	List<String> parts = new ArrayList<String>();
	for (Entry<Attr, Object> entry : attributes.entrySet())
	    if (!entry.getKey().equals(NodeAttr.DEFERRABLE_ERRORS) && !entry.getKey().equals(NodeAttr.ERRORS)
		    && entry.getValue() != null)
		if (entry.getValue() instanceof List) {
		    List<Object> entryList = (List<Object>) entry.getValue();
		    String[] valuestr = new String[entryList.size()];
		    for (int i = 0; i < entryList.size(); ++i) {
			valuestr[i] = (entryList == null || entryList.get(i) == null) ? null
				: entryList.get(i).toString();
		    }
		    parts.add(entry.getKey() + " : [" + String.join(", ", valuestr) + "]");
		} else
		    parts.add(entry.getKey() + " : " + entry.getValue());

	String attrs = "{" + String.join(", ", parts) + "}";
	String errors = "";
	if (attributes.containsKey(NodeAttr.ERRORS) && attributes.get(NodeAttr.ERRORS) != null) {
	    errors += "\nERRORS:\n";
	    for (AASTError err : (List<AASTError>) attributes.get(NodeAttr.ERRORS))
		errors += err.message() + "\n";
	    for (AASTError err : ((Map<ErrorCode, AASTError>) attributes.get(NodeAttr.DEFERRABLE_ERRORS)).values())
		errors += err.message() + "\n";
	}
	return attrs + errors;
    }

    public void error(AASTError err) {
	if (!attributes.containsKey(NodeAttr.ERRORS))
	    attributes.put(NodeAttr.ERRORS, new LinkedList<AASTError>());
	((List<AASTError>) attributes.get(NodeAttr.ERRORS)).add(err);
    }

    public void deferrableError(ErrorCode etype, GException err) {
	if (!attributes.containsKey(NodeAttr.DEFERRABLE_ERRORS))
	    attributes.put(NodeAttr.DEFERRABLE_ERRORS, new HashMap<ErrorCode, Set<GException>>());
	Map<ErrorCode, Set<GException>> defErrors = (Map<ErrorCode, Set<GException>>) attributes
		.get(NodeAttr.DEFERRABLE_ERRORS);
	if (!defErrors.containsKey(etype))
	    defErrors.put(etype, new TreeSet<>());
	defErrors.get(etype).add(err);
    }

    public void clearDeferrableError(ErrorCode etype) {
	if (!attributes.containsKey(NodeAttr.DEFERRABLE_ERRORS))
	    return;
	((HashMap<ErrorCode, AASTError>) attributes.get(NodeAttr.DEFERRABLE_ERRORS)).remove(etype);
    }

    public void clearAttrs() {
	logger.trace("Clearing attributes of node " + this + " attrs: " + stringAttrs());
	if (attributes.containsKey(NodeAttr.SYMBOL)) {
	    String symbol = (String) attributes.get(NodeAttr.SYMBOL);
	    attributes.clear();
	    attributes.put(NodeAttr.SYMBOL, symbol);
	} else
	    attributes.clear();
    }

    @Override
    public ProgramNode ancestor() {
	return parent();
    }

    @Override
    public List<ProgramNode> descendants() {
	List<AASTNode> cs = childs();
	List<ProgramNode> l = new ArrayList<>(cs.size());
	l.addAll(cs);
	return l;
    }

    @Override
    public ProgramNode descendant(NodeType t) {
	return child(t);
    }

    /**
     * return the childs of the subnode with the given type or null
     * 
     * @param type
     * @return
     */
    public List<AASTNode> childs(NodeType type) {
	for (AASTNode child : childs())
	    if (child.type().equals(type))
		return child.childs();
	return null;
    }

    public boolean childsEmpty(NodeType type) {
	for (AASTNode child : childs())
	    if (child.type().equals(type)) {
		boolean empty = true;
		for (AASTNode subchild : child.childs())
		    if (!subchild.type().equals(NodeType.VOID)) {
			empty = false;
			break;
		    }
		return empty;
	    }
	return true;
    }

    /**
     * 
     * @param type
     * @return true if there exist at least a child matching the given type
     */
    public boolean childExists(NodeType type) {
	return child(type) != null;
    }

    public boolean childExists(List<NodeType> types) {
	for (NodeType type : types)
	    if (childExists(type))
		return true;
	return false;
    }

    /**
     * @param type
     * @return nested child node if there exist at least a child, or a child of a
     *         child (restrincting to 1 child nodes) with the given type.
     */
    public AASTNode nestedChild(NodeType type) {
	if (this.type.equals(type))
	    return this;
	else if (childs.size() != 1)
	    return null;
	else
	    return childs.get(0).nestedChild(type);
    }

    /**
     * get the first child matching the type
     * 
     * @param type
     * @return
     */
    public AASTNode child(NodeType type) {
	for (AASTNode child : childs())
	    if (child.type().equals(type))
		return child;
	return null;
    }

    public AASTNode child(NodeType[] types) {
	for (AASTNode child : childs())
	    for (NodeType t : types)
		if (child.type().equals(t))
		    return child;
	return null;
    }

    public AASTNode lastChild(NodeType type) {
	AASTNode lastNode = null;
	for (AASTNode child : childs())
	    if (child.type().equals(type))
		lastNode = child;
	return lastNode;
    }

    /**
     * find first attribute value in this subtree, (meant to be used for attributes
     * added with propagateAttribute)
     * 
     * @param attr
     * @return
     */
    public Object findAttr(NodeAttr attr) {
	if (type().equals(NodeType.ID) && hasAttr(attr))
	    return attr(attr);

	Object attrval;
	for (AASTNode child : childs()) {
	    attrval = child.findAttr(attr);
	    if (attrval != null)
		return attrval;
	}
	return null;
    }

    /**
     * look for nodes with name 'name' and add to them the given attribute
     * 
     * @param isFunctionOutput
     * @param name2
     */
    public void propagateAttribute(Attr newattr, Object newvalue, String name) {
	List<String> names = new ArrayList<String>();
	names.add(name);
	propagateAttribute(null, (type().equals(NodeType.STATEMENT_LIST)) ? this : this.parent(NodeType.STATEMENT_LIST),
		this, newattr, newvalue, names, new NodeAttr[] {});
    }

    public void propagateAttribute(Attr newattr, Object newvalue, String name, NodeAttr[] toRemove) {
	List<String> names = new ArrayList<String>();
	names.add(name);
	propagateAttribute(null, (type().equals(NodeType.STATEMENT_LIST)) ? this : this.parent(NodeType.STATEMENT_LIST),
		this, newattr, newvalue, names, toRemove);
    }

    public void propagateAttribute(List<AASTNode> affectedNodes, Attr newattr, Object newvalue, List<String> names,
	    NodeAttr[] toRemove) {
	if (names == null || names.isEmpty())
	    return;
	propagateAttribute(affectedNodes,
		(type().equals(NodeType.STATEMENT_LIST)) ? this : this.parent(NodeType.STATEMENT_LIST), this, newattr,
		newvalue, names, toRemove);
    }

    public void propagateAttribute(Attr newattr, Object newvalue, List<String> names, NodeAttr[] toRemove) {
	if (names == null || names.isEmpty())
	    return;
	propagateAttribute(null, (type().equals(NodeType.STATEMENT_LIST)) ? this : this.parent(NodeType.STATEMENT_LIST),
		this, newattr, newvalue, names, toRemove);
    }

    private void propagateAttribute(List<AASTNode> affectedNodes, AASTNode contextFunctionNode, AASTNode node,
	    Attr newattr, Object newvalue, List<String> names, NodeAttr[] toRemove) {
	// changed scope -> terminate propagation
	if (!node.equals(contextFunctionNode) && !contextFunctionNode.parentOf(node))
	    return;
	if (node.type().equals(NodeType.ID) && names.contains(node.symbol())) {
	    // we found a matching node
	    // add/remove the attribute based on newvalue
	    if (newvalue != null)
		node.attr(newattr, newvalue);
	    else
		node.removeAttr(newattr);
	    // remove attributes set to be removed
	    for (NodeAttr rattr : toRemove)
		node.removeAttr(rattr);

	    // remember node that changed
	    if (affectedNodes != null)
		affectedNodes.add(node);
	}

	for (AASTNode child : node.childs())
	    propagateAttribute(affectedNodes, contextFunctionNode, child, newattr, newvalue, names, toRemove);
    }

    /**
     * copy all the attributes available in 'copy' to this node. The attributes in
     * this node, that aren't on 'copy' are left untouched. The attributes in this
     * node that are on 'copy' will be overwritten.
     * 
     * @param copy
     */
    public void mergeAttrs(AASTNode copy) {
	if (copy != null)
	    for (Attr attr : copy.attributes.keySet())
		attr(attr, copy.attr(attr));
    }

    @Override
    public boolean ancestorOf(ProgramNode p) {
	return parentOf((AASTNode) p);
    }

    @Override
    public void setException(GException e) {
	deferrableError(e.code(), e);
    }

    /**
     * 
     * @param node
     * @return true if node is a child of this.
     */
    public boolean parentOf(AASTNode node) {
	if (node == null)
	    return false;
	else if (node.equals(this))
	    return true;
	else
	    return parentOf(node.parent());
    }

    public void defaultType() {
	if (!childExists(NodeType.DEFAULT_VALUE))
	    return;
	// only one child in default value
	AASTNode node = child(NodeType.DEFAULT_VALUE).childs().get(0);
	// if inside the parameter list of a function
	boolean globally = parentExists(NodeType.PARAMETER_LIST);

	// if default value for another id
	if (node.type().equals(NodeType.INT)) {
	    node.expr(GType.get(BType.INT, Integer.valueOf(node.name())));
	} else if (node.type().equals(NodeType.FLOAT)) {
	    node.expr(GType.get(BType.SCALAR, Double.valueOf(node.name())));
	} else if (node.type().equals(NodeType.STRING)) {
	    node.expr(GType.get(BType.STRING, node.name()));
	} else if (node.type().equals(NodeType.MATRIX)) {
	    attr(NodeAttr.MATRIX_VALUES, node.attr(NodeAttr.MATRIX_VALUES));
	} else
	    logger.error("Unknown default type " + node);

	// set default type/value
	expr(node.expr(), globally);
    }

    @Override
    public String sourcePath() {
	if (compilationUnit() != null)
	    return compilationUnit().sourcePath();
	else
	    return null;
    }

}
