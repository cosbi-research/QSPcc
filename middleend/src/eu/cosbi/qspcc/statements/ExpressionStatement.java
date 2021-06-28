package eu.cosbi.qspcc.statements;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.exceptions.WalkException;
import eu.cosbi.qspcc.exceptions.CastException;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.MatrixError;
import eu.cosbi.qspcc.exceptions.SyntaxError;
import eu.cosbi.qspcc.exceptions.TreeStructureError;
import eu.cosbi.qspcc.exceptions.TypeError;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundError;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.exceptions.UnfinishedTranslationException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.FunctionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IndexType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.ScalarType;
import eu.cosbi.qspcc.expressions.type.SliceType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.qspcc.type.StatementType;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

@StatementClass(category = StatementClass.Type.EXPRESSION)
public class ExpressionStatement extends Statement {

    private Logger logger = LogManager.getLogger(ExpressionStatement.class);
    Deque<AASTNode> nodesTranslationStack = null;
    private String outputSymbol = null;
    private List<String> fieldAccessIds = null;
    // true if this instance is used as subroutine for another statement
    private boolean childMode = false;
    private boolean shouldBeReparsed = false;

    public ExpressionStatement(AAST aast, AASTNode fullStatement) {
	super(aast, fullStatement);
	reinit();
    }

    public ExpressionStatement(AAST aast, Deque<AASTNode> parentNodesStack, AASTNode fullStatement) {
	super(aast, parentNodesStack, fullStatement);
	childMode = true;
	reinit();
    }

    private void reinit() {
	nodesTranslationStack = new ArrayDeque<AASTNode>();
    }

    @Override
    public StatementType type() {
	return StatementType.FUNCTIONAL;
    }

    public boolean shouldBeReparsed() {
	return shouldBeReparsed;
    }

    @Override
    public void completeStatement()
	    throws UnfinishedTranslationException, UndefinedTranslationException, TypeException, UnboundException {
	AASTNode rhs = getExpressionIdentifier();

	if (rhs != null && rhs.expr() != null && !rhs.expr().equals(BType.UNKNOWN))
	    // expression statement completed
	    markAsCompleted();

	// move final output to top-level node
	statement.mergeAttrs(rhs);
    }

    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {
	try {
	    AASTNode tp = translateImplHelper(aast, node, value);
	    if (tp != null && tp.expr() != null && tp.expr().equals(BType.UNKNOWN))
		if (!childMode) {
		    // mark this statement (and his dependencies) to be reparsed
		    markToBeReparsed();
		} else
		    shouldBeReparsed = true;

	    if (tp != null)
		nodesTranslationStack.push(tp);

	} catch (GException e) {
	    // rethrow
	    throw e;
	} catch (Exception e) {
	    logger.debug(e.getMessage(), e);
	    throw new WalkException(node, e);
	}

	// print stack
	if (logger.isTraceEnabled())
	    printStack(Level.TRACE);
    }

    private void printStack(Level level) {
	StringBuffer sb = new StringBuffer();
	for (AASTNode tp : nodesTranslationStack)
	    sb.append(tp.name()).append(" : ").append(tp.stringAttrs()).append(", ");

	logger.log(level, "-- CURRENT STACK -- " + sb.toString());
    }

    /**
     * meant to be used by AssignStatement.
     * 
     * @return the symbols used to represent the result of the expression, or the
     *         full expression translation if no such symbol exist.
     * @throws UnfinishedTranslationException
     */
    public AASTNode getExpressionIdentifier() throws UnfinishedTranslationException {
	// if no vector symbol -> only scalar expression or call to
	// function/access to multi-dimensional variable
	// return the translation itself
	AASTNode tp = nodesTranslationStack.peek();
	return tp;
    }

    @Override
    public void pushTranslation(AASTNode tp) {
	nodesTranslationStack.push(tp);
    }

    protected AASTNode popTranslation() {
	if (nodesTranslationStack.peek() != null)
	    return nodesTranslationStack.pop();
	logger.debug("Tried to POP from an empty translation stack");
	return null;
    }

    protected AASTNode peekTranslation() {
	return nodesTranslationStack.peek();
    }

    protected Iterator<AASTNode> iterTranslation() {
	return nodesTranslationStack.iterator();
    }

    // called only on a closed, translatable sub-tree
    public AASTNode translateImplHelper(AAST aast, AASTNode node, String value) throws GException {
	AASTNode leftOperand;
	AASTNode rightOperand;
	IntType[] dims;
	String newSymbol;
	// symbol to be added to this level expression
	String exprSymbol;
	GType exprType;
	boolean scalar;

	switch (node.type()) {
	// BASE TYPES
	case VOID: {
	    // add types to tree
	    exprType = GType.get(BType.VOID);
	    node.expr(exprType);
	    // no parameters
	    return node;
	}
	case TRUE: {
	    // add types to tree
	    exprType = GType.get(BType.BOOL, Boolean.TRUE);
	    node.expr(exprType);
	    return node;
	}
	case FALSE: {
	    // add types to tree
	    exprType = GType.get(BType.BOOL, Boolean.FALSE);
	    node.expr(exprType);
	    return node;
	}
	case INT: {
	    if (node.parent().equals(NodeType.DEFAULT_VALUE))
		return null;
	    Integer i = Integer.parseInt(value);
	    // add types to tree, normal number assume them to be scalar
	    exprType = GType.get(BType.INT, i);
	    node.expr(exprType);
	    return node;
	}
	case FLOAT:
	    if (node.parent().equals(NodeType.DEFAULT_VALUE))
		return null;
	    double d = Double.parseDouble(value);
	    exprType = GType.get(BType.SCALAR, d);
	    node.expr(exprType);
	    return node;

	// end OF STRING
	case STRING:
	    if (node.parent().equals(NodeType.DEFAULT_VALUE))
		return null;
	    // strip ' / "
	    String stripvalue = value.substring(1, value.length() - 1);
	    // translate
	    node.expr(GType.get(BType.STRING, stripvalue));
	    return node;

	case DEFAULT_VALUE:
	    if (node.parentExists(NodeType.ID)) {
		AASTNode id = node.parent(NodeType.ID);
		// infer type from default value
		id.defaultType();
	    }
	    return null;

	case FIELDACCESS: {
	    LinkedList<AASTNode> structs = new LinkedList<AASTNode>();
	    if (struct_list == null || struct_list.isEmpty())
		throw new UndefinedTranslationException(ErrorMessage.USER_SYNTAX_ERROR_LATE_DETECTION, node);
	    // pop struct path..
	    for (int i = 0; i < struct_list.size(); i++)
		structs.add(popTranslation());

	    // reset right order
	    // Collections.reverse(structs);

	    // to infer his type, search in env for base structure
	    // AASTNode baseStruct = structs.get(0);
	    // AASTNode lastElement = structs.get(structs.size() - 1);

	    // since they are in reverse order.. last is the first
	    // type of fieldaccess is just the type of the last element
	    node.exprs(structs.peekFirst().exprs(), false);
	    return node;
	}
	case ID:
	    Tuple<AASTNode, String> id_tp = null;
	    // is symbol defined in this script?
	    boolean isSymbol = node.exprs() != null && !GType.get(BType.UNKNOWN).equals(node.expr());

	    // if it's not an existing symbol
	    if (!isSymbol)

		if (node.hasAttr(NodeAttr.IS_FUNCTION_PARAM)) {
		    // use param type
		    AASTNode funNode = node.parent(NodeType.FUNCTION);
		    AASTNode lastFunNode = funNode;
		    boolean found = false;
		    while (funNode != null && !found) {
			List<AASTNode> params = funNode.childs(NodeType.PARAMETER_LIST);
			if (params != null)
			    for (AASTNode param : params)
				if (param.name().equals(node.name())) {
				    node.expr(param.expr(), true);
				    found = true;
				    break;
				}
			// search previous function node (access parent environments)
			lastFunNode = funNode;
			funNode = funNode.parent(NodeType.FUNCTION);
		    }
		    if (!found) {
			logger.warn("Cannot find '" + node.name() + "' in function '"
				+ lastFunNode.child(NodeType.ID).name() + "' inputs list in file "
				+ node.compilationUnit().sourcePath() + " at line " + node.lineNumber() + ":"
				+ node.colNumber());
			node.expr(GType.get(BType.UNKNOWN));
		    }
		    // if symbol exists in one of the parent envs
		} else if (aast.functionSymbolExists(node)) {
		    // case core function
		    List<GType> idType = null;
		    GType idTypeVal = null;
		    boolean coreFunWith0Params = false;
		    boolean partOfApply = node.parent().type().equals(NodeType.APPLY);
		    boolean inParameterList = node.parentExists(NodeType.FUNCTION_PARAMETER_LIST);
		    boolean isCoreFunction = node.hasAttr(NodeAttr.REF_CORE_FUNCTION);
		    if (isCoreFunction) {
			// reset to actual parameter type for core functions
			IFunction coreFun = (IFunction) node.attr(NodeAttr.REF_CORE_FUNCTION);
			BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater = coreFun.returnTypeUpdater();
			// get output for case no parameters
			if (returnTypeUpdater != null) {
			    idType = returnTypeUpdater.apply(node, new ArrayList<GType>());
			    idTypeVal = idType.get(0);
			} else {
			    // if no return type updater, use the constant value
			    idTypeVal = coreFun.getOutType().get(BType.UNKNOWN);
			    idType = new LinkedList<GType>();
			    idType.add(idTypeVal);
			}
			// case user-defined function, if part of apply, doesn't execute it
			coreFunWith0Params = !partOfApply && idTypeVal != null && !idTypeVal.equals(BType.UNDEFINED)
				&& !idTypeVal.equals(BType.UNKNOWN);
		    }

		    AASTNode funNode = aast.functionSymbolNode(node);
		    FunctionType funType = (FunctionType) funNode.expr();
		    // if 0 inputs allowed
		    if (coreFunWith0Params) {
			// 0 inputs -> assign outputs
			node.exprs(idType, false);
			node.attr(NodeAttr.FUNCALL, node.name());
		    } else if (funType != null && (funType.inputs().size() == 0 || (funType.inputs().size() == 1
			    && (GType.get(BType.VOID).equals(funType.inputs().get(0)))))) {
			// 0 inputs -> assign outputs
			List<AASTNode> outSymbols = funNode.childs(NodeType.FUNCTION_RETURN);
			if (outSymbols.size() > 1) {
			    StructType complexType = TypeUtils.createComplexType(node.name(), 0, outSymbols);
			    node.expr(complexType, false);
			} else
			    node.exprs(funType.outputs(), false);
			node.attr(NodeAttr.FUNCALL, node.name());
		    } else if (!partOfApply) {
			if (inParameterList) {
			    // if inside a parameter list, this is a function handle
			    if (!isCoreFunction)
				node.attr(NodeAttr.REF_FUNCTION, funNode);
			    node.exprs(funNode.exprs(), false);
			} else {
			    // if not inside a function list this is a function call without parameters
			    List<AASTNode> outSymbols = funNode.childs(NodeType.FUNCTION_RETURN);
			    if (outSymbols.size() > 1) {
				StructType complexType = TypeUtils.createComplexType(node.name(), 0,
					funNode.childs(NodeType.FUNCTION_RETURN));
				node.expr(complexType, false);
			    } else
				node.exprs(funType.outputs(), false);
			    node.attr(NodeAttr.FUNCALL, node.name());
			}
		    } else {
			// if apply with non-0 params
			if (!isCoreFunction)
			    node.attr(NodeAttr.REF_FUNCTION, funNode);
			node.exprs(funNode.exprs(), false);
		    }

		} else {
		    List<GType> types = nearestSymbolTypes(node);
		    if (existsPreviousNodeType(NodeType.APPLY, 1))
			node.attr(NodeAttr.CALLABLE, true);

		    // propagate type to all id nodes of this AAST
		    // parentAAST.rootNode().propagateAttribute(NodeAttr.ETYPE,
		    // parentAASTNode.attr(NodeAttr.ETYPE),
		    // node.name());
		    node.exprs(types, true);
		}
	    // if is symbol already defined
	    else {
	    }
	    return node;

	case NEG:
	    // boolean operations
	    rightOperand = popTranslation();
	    if (rightOperand.exprs() != null && GType.get(BType.UNDEFINED).equals(rightOperand.exprs())) {
		// operation with undefined is undefined
		node.expr(GType.get(BType.UNDEFINED));
		return node;
	    }

	    node.exprs(TypeUtils.resultingTypeBoolOp(node, rightOperand), true);
	    return node;

	case GRTE:
	case GRT:
	case LSTE:
	case LST:
	case DOUBLE_EQ:
	case LOG_AND:
	case LOG_OR:
	case BIN_AND:
	case BIN_OR:
	case NEQ:
	    // boolean operations
	    rightOperand = popTranslation();
	    leftOperand = popTranslation();

	    if ((leftOperand.exprs() != null && GType.get(BType.UNDEFINED).equals(leftOperand.exprs()))
		    || (rightOperand.exprs() != null && GType.get(BType.UNDEFINED).equals(rightOperand.exprs()))) {
		// operation with undefined is undefined
		node.expr(GType.get(BType.UNDEFINED));
		return node;
	    }

	    node.exprs(TypeUtils.resultingTypeBoolOp(node, leftOperand, rightOperand), true);
	    node.expr().name(node.symbol());
	    return node;
	case VECTOR: {
	    // compute vector length based on number of scalars on stack
	    GType vectorType = null;
	    GType finalVectorType = null;
	    boolean allequals = true;
	    scalar = false;
	    boolean mixedScalarMatrix = false;
	    List<AASTNode> vectorvalues = new ArrayList<AASTNode>();

	    List<GType> types = new ArrayList<GType>();
	    boolean unknown = false;
	    boolean empty = false;
	    AASTNode dynamicId = null;
	    // check if in the case a = [a; x]
	    // in an assignment
	    AASTNode lhsNode = null;
	    if (node.parentExists(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN }))
		lhsNode = TypeUtils.getIDNode(node.parent(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN })
			.child(NodeType.LHS).childs().get(0));
	    for (int n = 0; n < vector_list.get(node).size(); n++) {
		AASTNode tp = TypeUtils.getIDNode(popTranslation());

		if (lhsNode != null)
		    if (tp.type().equals(NodeType.ID) && tp.hasAttr(NodeAttr.DYNAMIC_ID)
			    && tp.symbol().equals(lhsNode.symbol()))
			dynamicId = tp;
		    else if (tp.type().equals(NodeType.FIELDACCESS)
			    && tp.lastChild(NodeType.ID).hasAttr(NodeAttr.DYNAMIC_ID)
			    && tp.lastChild(NodeType.ID).fullname().equals(lhsNode.fullname()))
			dynamicId = tp.lastChild(NodeType.ID);

		vectorvalues.add(tp);
		// assume only one type here
		GType etype = tp.expr();
		switch (etype.type()) {
		case INT:
		case BOOL:
		case SCALAR:
		case STRING:
		    allequals = allequals && (vectorType == null || vectorType.isCastableToScalar());
		    scalar = true;
		    vectorType = TypeUtils.updateScalarVectorType(vectorType, etype.type());
		    finalVectorType = vectorType;
		    break;
		case MATRIX_ACCESS_LAST:
		    allequals = allequals && (vectorType == null || vectorType.isCastableToScalar());
		    scalar = true;
		    vectorType = GType.get(etype);
		    finalVectorType = TypeUtils.updateScalarVectorType(vectorType,
			    ((IndexType) etype).dimension().type());
		    break;
		case MATRIX:
		    IntType[] dim = ((MatrixType) etype).dims();
		    if (dim == null || dim.length == 0) {
			String msg = "Sub-matrix with unknown dimension at annotation time '" + tp.name() + "'";
			logger.warn(msg);
			tp.error(new MatrixError(msg, Thread.currentThread().getStackTrace()));
			allequals = allequals && (vectorType == null || vectorType.equals(BType.MATRIX));
			vectorType = GType.get(etype);
			// since each matrix of matrices will be flattened
			finalVectorType = TypeUtils.updateScalarVectorType(finalVectorType,
				((DimensionType) etype).of().type());
		    } else if (dim.length == 1 && dim[0].hasValue() && dim[0].valueAsInt() == 1
			    || dim.length == 2 && dim[0].hasValue() && dim[0].valueAsInt() == 1 && dim[1].hasValue()
				    && dim[1].valueAsInt() == 1) {
			allequals = allequals && (vectorType == null || vectorType.isCastableToScalar());
			vectorType = GType.get(etype);
			// since each matrix of matrices will be flattened
			finalVectorType = TypeUtils.updateScalarVectorType(finalVectorType,
				((DimensionType) etype).of().type());
			scalar = true;
		    } else {
			allequals = allequals && (vectorType == null || vectorType.equals(BType.MATRIX));
			vectorType = GType.get(etype);
			// since each matrix of matrices will be flattened
			finalVectorType = TypeUtils.updateScalarVectorType(finalVectorType,
				((DimensionType) etype).of().type());
		    }
		    break;
		case MATRIX_ACCESS_SLICE:
		    allequals = allequals && (vectorType == null || vectorType.equals(BType.MATRIX_ACCESS_SLICE));
		    vectorType = GType.get(etype);
		    // since each matrix of matrices will be flattened
		    finalVectorType = TypeUtils.updateScalarVectorType(finalVectorType,
			    ((DimensionType) etype).of().type());
		    break;
		case UNKNOWN:
		    unknown = true;
		    vectorType = GType.get(BType.UNKNOWN);
		    break;
		case VOID:
		    empty = true;
		    vectorType = GType.get(BType.VOID);
		    break;
		default:
		    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_VECTOR_TYPE, node, etype, tp);
		}
		types.add(vectorType);
	    }

	    if (unknown) {
		// stop here if unknown
		node.expr(GType.get(BType.UNKNOWN));
		return node;
	    } else if (empty) {
		// stop here if void
		node.expr(GType.get(BType.VOID));
		return node;
	    }

	    // put it in right order
	    Collections.reverse(vectorvalues);
	    Collections.reverse(types);
	    node.attr(NodeAttr.VECTOR_TYPES, types);
	    if (dynamicId != null) {
		List<Integer> general_dims = new LinkedList<Integer>();
		if (vectorvalues.size() > 1) {
		    // set dynamic column dimension
		    // we are in case [a, b, c..]
		    // a[1x2] -> a[1xn]
		    general_dims.add(1);
		} else
		    // we are in case [a; b ...]
		    general_dims.add(0);
		node.parent(NodeType.STATEMENT_LIST).propagateAttribute(NodeAttr.DYNAMIC_ID, general_dims,
			dynamicId.symbol());
	    }
	    // list of scalar values
	    List<AASTNode> values = new ArrayList<AASTNode>();
	    // list of scalar values / 1D matrix to join, not empty only if at
	    // least 1 matrix among scalars exist
	    List<Tuple<List<AASTNode>, AASTNode>> vectorJoin = new ArrayList<Tuple<List<AASTNode>, AASTNode>>();
	    int vdim = 0;
	    boolean unknownMatrixDim = false;
	    boolean matrix = false;
	    boolean matrix1d = false;
	    boolean matrix2d = false;

	    for (AASTNode tp : vectorvalues) {
		matrix = tp.expr() != null && tp.expr().isCastableToMatrix();
		// if vector contains scalars and we got a matrix
		if (scalar && matrix) {
		    mixedScalarMatrix = true;
		    // if vector contains scalar, but nevertheless it contains
		    // matrices, they should be 1D
		    // and they should be flattened in this vector
		    int tmp = TypeUtils.matrixTotalDimensions((DimensionType) tp.expr());
		    if (tmp == -1)
			unknownMatrixDim = true;
		    else
			vdim += tmp;
		    // join scalars and matrix
		    vectorJoin.add(new Tuple<List<AASTNode>, AASTNode>(
			    // list of scalar values
			    new ArrayList<AASTNode>(values),
			    // 1D matrix to join
			    tp));

		    values.clear();
		    // } else if (!scalar && tp.expr() != null &&
		    // tp.expr().isCastableToMatrix()) {
		} else {
		    if (matrix) {
			// vector of matrices
			int tmp = TypeUtils.matrixTotalDimensions((DimensionType) tp.expr());
			if (tmp == -1)
			    unknownMatrixDim = true;
			else
			    vdim += tmp;
		    } else
			// vector of scalars
			vdim++;
		    // append translation
		    values.add(tp);
		}
	    }

	    if (mixedScalarMatrix) {
		if (!values.isEmpty()) {
		    // add ending values
		    vectorJoin.add(new Tuple<List<AASTNode>, AASTNode>(new ArrayList<AASTNode>(values), null));
		    // be sure it is empty
		    values.clear();
		}
		// optimizer.translateScalarMatrixJoin(vectorSymbol, vectorDim,
		// vectorJoin);
		// update ENV with new vector

		// it has become a single matrix containing everything in this
		// vector (both 1D matrices and scalars)
		scalar = false;
		// only element
		values.add(node);
		node.attr(NodeAttr.VECTOR_JOIN, vectorJoin);
	    }

	    // vector of finalVectorType with dimensions 1xvdim
	    IntType vdimtype = null;
	    AASTNode parentMatrix = node.parent();
	    if (unknownMatrixDim)
		vdimtype = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(parentMatrix, 2));
	    else
		// DON'T assign a name, because this vector doesn't exist in translated code
		// it's just an inner node.
		// and we know everything about it
		vdimtype = (IntType) GType.get(BType.INT, vdim);
	    String vectorSymbol = TypeUtils.matrixName(parentMatrix);
	    node.expr(GType.get(BType.VECTOR, vectorSymbol, finalVectorType,
		    new IntType[] { (IntType) GType.get(BType.INT, 1), vdimtype }));
	    node.symbol(vectorSymbol);
	    node.attr(NodeAttr.VECTOR_VALUES, vectorvalues);
	    node.attr(NodeAttr.VECTOR_MIXED_VALUES, mixedScalarMatrix);
	    node.attr(NodeAttr.VECTOR_MATRIX_VALUES, !scalar);
	    // if this vector definition is not inside a matrix definition
	    // actually build the vector
	    if (!existsPreviousNodeType(NodeType.MATRIX, 1))
		node.error(new TreeStructureError("A VECTOR element should be inside a MATRIX element",
			Thread.currentThread().getStackTrace()));

	    return node;
	}
	case MATRIX:
	    // only 1xn matrix supported until now.
	    List<AASTNode> vectors = new ArrayList<AASTNode>();
	    GType mVectorType = null;
	    boolean someVectorsContainMatrices = false;
	    boolean unknown = false;
	    for (int i = 0; i < node.childs().size(); ++i) {
		// do not remove
		leftOperand = peekTranslation();
		// if not vector or type different
		if (!GType.get(BType.VECTOR).equals(leftOperand.expr())
			&& !GType.get(BType.UNKNOWN).equals(leftOperand.expr())
			&& !GType.get(BType.VOID).equals(leftOperand.expr())) {
		    break;
		} else if (unknown || leftOperand.expr() != null && leftOperand.expr().equals(BType.UNKNOWN)) {
		    // just to remove from stack
		    AASTNode vectorNode = popTranslation();
		    unknown = true;
		} else if (leftOperand.expr() != null && leftOperand.expr().equals(BType.VOID)) {
		    // just to remove this vector from stack and go on
		    AASTNode vectorNode = popTranslation();
		} else {
		    // if vector check vector type: scalar or matrix?
		    GType lof = ((MatrixType) leftOperand.expr()).of();
		    if (mVectorType == null || !mVectorType.canRepresent(lof) && lof.canRepresent(mVectorType))
			mVectorType = lof;
		    // if type equal
		    if (mVectorType.canRepresent(lof)) {
			AASTNode vectorNode = popTranslation();
			vectors.add(vectorNode);
			someVectorsContainMatrices |= (leftOperand.hasAttr(NodeAttr.VECTOR_MIXED_VALUES)
				&& (Boolean) leftOperand.attr(NodeAttr.VECTOR_MIXED_VALUES))
				|| (leftOperand.hasAttr(NodeAttr.VECTOR_MATRIX_VALUES)
					&& (Boolean) leftOperand.attr(NodeAttr.VECTOR_MATRIX_VALUES));
		    } else
			// if type different, this means we are building a
			// matrix of matrices and we have
			// ex. i=[[1,2;5,6];[3,4;7,8]]
			// stack: CURRENT STACK -- false|7$8 : VECTOR, false|3$4
			// : VECTOR, true|v0 : VECTOR,
			// Translating: [3,4;7,8]
			break;
		}
	    }

	    Boolean[] one_dimensional_as_row = new Boolean[vectors.size()];

	    if (unknown) {
		// stop here if unknown
		node.expr(GType.get(BType.UNKNOWN));
		return node;
	    } else if (vectors.isEmpty()) {
		// stop here if empty matrix
		node.expr(GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(BType.BOOL),
			new IntType[] { (IntType) GType.get(BType.INT, 0).name(TypeUtils.matrixDimName(node, 1)) }));
		node.attr(NodeAttr.FIXED_MATRIX, true);
		// should be translated only if toplevel AND ON RHS
		translateMatrix(node, one_dimensional_as_row);
		return node;
	    }

	    // put it in right order
	    Collections.reverse(vectors);

	    if (vectors.isEmpty())
		throw new TypeException(ErrorMessage.INTERNAL_MISSING_MATRIX_VECTORS, node);

	    // matrix symbol, matrix dimensions (row times cols) used only for
	    // matrices == true case
	    List<List<AASTNode>> subMatrices = new ArrayList<List<AASTNode>>();
	    List<IntType> n_rows = new ArrayList<IntType>();
	    List<IntType> n_cols = new ArrayList<IntType>();
	    LinkedList<IntType> n_extra_dims = null;
	    // used only for matrices == false case
	    List<List<AASTNode>> matrix_values = new ArrayList<List<AASTNode>>();
	    boolean firstIteration = true;
	    for (int i = 0; i < vectors.size(); ++i) {
		AASTNode row_vector = vectors.get(i);
		// is a matrix vector? v0,v1,v2... REDUNDANT CHECK BUT JUST TO
		// BE SURE
		MatrixType mtype = ((MatrixType) row_vector.expr());
		GType curMatrixVector = mtype.of();
		// get list of expressions (v0,v1,v2 or 1,2,3 or ..)
		List<AASTNode> vector_col_symbols = (List<AASTNode>) row_vector.attr(NodeAttr.VECTOR_VALUES);
		List<GType> vector_col_symbols_expr = (List<GType>) row_vector.attr(NodeAttr.VECTOR_TYPES);
		if (!mVectorType.canRepresent(curMatrixVector)) {
		    TypeException ex = new TypeException(ErrorMessage.INTERNAL_MATRIX_VECTORS_HAVE_DIFFERENT_TYPES,
			    node, curMatrixVector, mVectorType);
		    node.error(new MatrixError(ex.stringify(), Thread.currentThread().getStackTrace()));
		    throw ex;
		}
		// 1x1 matrix of matrices.. return it directly.
		if (someVectorsContainMatrices && vectors.size() == 1 && vector_col_symbols.size() == 1) {
		    node.expr(GType.get(BType.MATRIX, mtype.name(), curMatrixVector, mtype.dims()));
		    node.attr(NodeAttr.FIXED_MATRIX, !(Boolean) vectors.get(0).attr(NodeAttr.VECTOR_MATRIX_VALUES));
		    // should be translated only if toplevel AND ON RHS
		    one_dimensional_as_row[0] = true;
		    translateMatrix(node, one_dimensional_as_row);
		    return node;
		}
		// here
		// all of the same type (matrix or scalar)
		IntType vector_rows = null;
		IntType vector_cols = null;
		LinkedList<IntType> vector_extra_dims = null;
		// VECTOR_MIXED_VALUES = true or all matrices in this vector
		if (someVectorsContainMatrices) {
		    List<GType> row_cols = new ArrayList<GType>();
		    List<AASTNode> subMatricesCols = new ArrayList<AASTNode>();
		    // add sub-matrices row
		    subMatrices.add(subMatricesCols);
		    // how many rows do these column-bind matrices have?
		    // take the first one as reference
		    GType vector_col_type = vector_col_symbols_expr.get(0);
		    // can be matrix or slice
		    IntType[] reference_row_dims;
		    if (vector_col_type.isCastableToMatrix()) {
			DimensionType rowmtype = ((DimensionType) vector_col_type);
			reference_row_dims = (IntType[]) rowmtype.dims();
		    } else if (vector_col_type.isCastableToScalar())
			// 1d matrix of 1 element
			reference_row_dims = new IntType[] { (IntType) GType.get(BType.INT, 1) };
		    else
			throw new UndefinedTranslationException(ErrorMessage.USER_MATRIX_DIMENSION_INCOMPATIBLE, node,
				node);
		    // one-dimensional array are interpreted as row or column
		    // vector?
		    one_dimensional_as_row[i] = false;
		    if (reference_row_dims.length == 0 || (reference_row_dims.length == 1
			    && reference_row_dims[0].hasIntValue() && reference_row_dims[0].valueAsInt() == 0)) {
			// skip empty matrices
			subMatrices.remove(subMatrices.size() - 1);
			continue;
		    } else if ((reference_row_dims.length > 1 && reference_row_dims[0].hasValue()
			    && reference_row_dims[0].valueAsInt() == 1) || (reference_row_dims.length == 1)) {
			// check all the others vector_col_symbols_expr
			// if all of them do have the first dimension as 1, then interpret as row,
			// otherwise as col
			one_dimensional_as_row[i] = true;
			Iterator<GType> it = vector_col_symbols_expr.iterator();
			it.next(); // skip first as already checked
			while (it.hasNext() && one_dimensional_as_row[i]) {
			    GType vector_other_col_type = it.next();
			    IntType[] other_row_dims;
			    if (vector_other_col_type.isCastableToMatrix()) {
				DimensionType rowmtype = ((DimensionType) vector_other_col_type);
				other_row_dims = (IntType[]) rowmtype.dims();
			    } else if (vector_other_col_type.isCastableToScalar())
				// 1d matrix of 1 element -> ok row
				continue;
			    else
				throw new UndefinedTranslationException(ErrorMessage.USER_MATRIX_DIMENSION_INCOMPATIBLE,
					node, node);

			    one_dimensional_as_row[i] &= other_row_dims.length > 1 && other_row_dims[0].hasValue()
				    && other_row_dims[0].valueAsInt() == 1;
			}
			if (one_dimensional_as_row[i])
			    n_rows.add((IntType) GType.get(BType.INT, 1));
			else
			    n_rows.add(TypeUtils.realDims(reference_row_dims)[0]);
		    } else if (reference_row_dims.length > 0) {
			// name was already set in vector
			n_rows.add(reference_row_dims[0]);
		    } else {
			AASTNode curNode = vector_col_symbols.get(0);
			UndefinedTranslationException ex = new UndefinedTranslationException(
				ErrorMessage.USER_MATRIX_DIMENSION_INCOMPATIBLE, node, curNode);
			node.error(new MatrixError(ex.stringify(), Thread.currentThread().getStackTrace()));
			throw ex;
		    }

		    // columns counted on this row
		    for (int j = 0; j < vector_col_symbols.size(); ++j) {
			// matrices concatenated here are concatenated side
			// by
			// side [A,B]
			AASTNode colnode = vector_col_symbols.get(j);
			IntType[] tmp_vector_dim = null;
			DimensionType colmtype = null;
			if (vector_col_symbols_expr.get(j).isCastableToMatrix()) {
			    colmtype = ((DimensionType) vector_col_symbols_expr.get(j));
			    tmp_vector_dim = (IntType[]) colmtype.dims();
			} else if (vector_col_symbols_expr.get(j).isCastableToScalar())
			    // 1d matrix of 1 element
			    tmp_vector_dim = new IntType[] { (IntType) GType.get(BType.INT, 1) };
			else
			    throw new UndefinedTranslationException(ErrorMessage.USER_MATRIX_DIMENSION_INCOMPATIBLE,
				    node, node);

			if (tmp_vector_dim == null || tmp_vector_dim.length == 0) {
			    UndefinedTranslationException ex = new UndefinedTranslationException(
				    ErrorMessage.USER_MATRIX_DIMENSION_INCOMPATIBLE, node, colnode);
			    node.error(new MatrixError(ex.stringify(), Thread.currentThread().getStackTrace()));
			    throw ex;
			}
			if (tmp_vector_dim.length >= 2) {
			    // here number of cols can differ element by
			    // element
			    vector_rows = tmp_vector_dim[0];
			    vector_cols = tmp_vector_dim[1];
			    if (n_extra_dims == null && tmp_vector_dim.length > 2) {
				n_extra_dims = new LinkedList<IntType>();
				for (int w = 2; w < tmp_vector_dim.length; ++w)
				    n_extra_dims.add(tmp_vector_dim[w]);
			    }
			} else if (tmp_vector_dim.length == 1) {
			    if (!one_dimensional_as_row[i]) {
				// column vector
				vector_cols = (IntType) GType.get(BType.INT, 1);
				vector_rows = tmp_vector_dim[0];
				if (colmtype != null && !vector_rows.hasValue())
				    vector_rows.name(TypeUtils.matrixDimName(colmtype.name(), 1));
			    } else {
				// row vector
				vector_cols = (IntType) tmp_vector_dim[0];
				if (colmtype != null && !vector_cols.hasValue())
				    vector_cols.name(TypeUtils.matrixDimName(colmtype.name(), 2));
				vector_rows = (IntType) GType.get(BType.INT, 1);
			    }

			    // tmp_vector_dim = new IntType[] { vector_rows, vector_cols };
			    // update symbol dimension once and for all after
			    // heuristic decision
			    // on 1D matrix interpretation (removed for difficulties in C @see
			    // ensureMatrixMemory)
			    // colnode.expr(GType.get(BType.MATRIX, colmtype.name(), colmtype.of(),
			    // tmp_vector_dim));
			}

			if (firstIteration)
			    // needed for output matrix dimension
			    n_cols.add(vector_cols);
			else
			    row_cols.add(vector_cols);
			// add subMatrices col
			subMatricesCols.add(colnode);
			// CBIND join matrices by columns, add vector_cols
			// columns
			// deferred: do it when we know how many cols in
			// total
		    }
		} else {
		    int tmp_n_cols = vector_col_symbols.size();
		    one_dimensional_as_row[i] = true;
		    vector_cols = (IntType) GType.get(BType.INT, tmp_n_cols);
		    if (firstIteration)
			// needed for output matrix dimension
			n_cols.add(vector_cols);
		    List<AASTNode> vector_values = vector_col_symbols;
		    matrix_values.add(vector_values);
		}
		// first iteration completed
		firstIteration = false;
	    }

	    if (!someVectorsContainMatrices) {
		// if not matrices of matrices, n_rows is vectors.size and
		// n_cols is vector_col_symbols.length set at the first
		// iteration
		n_rows.add((IntType) GType.get(BType.INT, vectors.size()));
		// create matrix of scalars
		// optimizer.translateMatrix(newSymbol, mVectorType, dims,
		// matrix_values);
		node.expr(
			GType.get(BType.MATRIX, "_tmp_", mVectorType, new IntType[] { n_rows.get(0), n_cols.get(0) }));
		String nodename = node.symbol();
		node.expr().name(nodename);
		if (!n_rows.get(0).hasValue())
		    n_rows.get(0).name(TypeUtils.matrixDimName(nodename, 1));
		if (!n_cols.get(0).hasValue())
		    n_cols.get(0).name(TypeUtils.matrixDimName(nodename, 2));
		node.attr(NodeAttr.MATRIX_VALUES, matrix_values);
	    } else {
		// create big matrix that will contain everything after
		// cbind/rbind
		// optimizer.sumSymbols(sumRow, n_rows, ExprType.get(Expr.INT));
		IntType row_dim = null;
		int rsum = 0;
		for (IntType row : n_rows)
		    if (row.hasValue())
			rsum += row.valueAsInt();
		    else {
			// only symbolic
			row_dim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 1));
			break;
		    }
		if (row_dim == null)
		    row_dim = (IntType) GType.get(BType.INT, rsum);
		// optimizer.sumSymbols(sumCol, n_cols, ExprType.get(Expr.INT));
		IntType col_dim = null;
		int csum = 0;
		for (IntType col : n_cols)
		    if (col.hasValue())
			csum += col.valueAsInt();
		    else {
			// only symbolic
			col_dim = (IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(node, 2));
			break;
		    }
		if (col_dim == null)
		    col_dim = (IntType) GType.get(BType.INT, csum);
		// flatten matrix of matrix in a big matrix
		IntType[] mVectorDims = null;
		if (n_extra_dims != null) {
		    mVectorDims = new IntType[2 + n_extra_dims.size()];
		    mVectorDims[0] = row_dim;
		    mVectorDims[1] = col_dim;
		    int md = 2;
		    for (IntType dim : n_extra_dims)
			mVectorDims[md++] = (IntType) GType.get(dim);
		} else {
		    mVectorDims = new IntType[2];
		    mVectorDims[0] = row_dim;
		    mVectorDims[1] = col_dim;
		}
		// set tmp name
		// TODO think a better way
		node.expr(GType.get(BType.MATRIX, "_tmp_", mVectorType, mVectorDims));
		// now that type is set we can call matrixName (that uses the type information)
		// and reset matrix name and dimensions name.
		String nodename = TypeUtils.matrixName(node);
		node.expr().name(nodename);
		if (!row_dim.hasValue())
		    row_dim.name(TypeUtils.matrixDimName(nodename, 1));
		if (!col_dim.hasValue())
		    col_dim.name(TypeUtils.matrixDimName(nodename, 2));
		// update dims
		((MatrixType) node.expr()).updateDims(mVectorDims);
		if (n_extra_dims != null)
		    for (int md = 0; md < n_extra_dims.size(); ++md)
			if (!mVectorDims[2 + md].hasValue())
			    mVectorDims[2 + md].name(TypeUtils.matrixDimName(nodename, 3 + md));

		node.attr(NodeAttr.MATRIX_VALUES, subMatrices);

		// optimizer.createUninitializedMatrix(newSymbol,
		// SymbolType.SCALAR, dims);
		// fill big matrix
		// optimizer.fillBlockMatrix(newSymbol, dims, subMatrices);
		// from matrix of matrix to matrix of scalar
	    }

	    // should be translated only if toplevel AND ON RHS
	    // case [[1 2 3]; [4 5 6]] shouldn't be translated
	    // case [2*[1 2 3]; [4 5 6]] the first should be translated, the last shouldn't
	    node.attr(NodeAttr.FIXED_MATRIX, !someVectorsContainMatrices);
	    translateMatrix(node, one_dimensional_as_row);
	    return node;

	case ELEMENTWISE_CCT:
	case TRANSPOSE: {
	    // ---- TRANSPOSITION OF MATRIX
	    // load first element from stack (the one to be transposed)
	    leftOperand = popTranslation();
	    String symbol = leftOperand.symbol();
	    GType type = leftOperand.expr();

	    if (leftOperand.hasAttr(NodeAttr.DYNAMIC_ID)
		    // and transpose is the only operation ( a = a' )
		    && node.parentExists(NodeType.RHS, 2))
		// transposition is a false positive, the values were not changed
		node.parent(NodeType.STATEMENT_LIST).propagateAttribute(NodeAttr.DYNAMIC_ID, null,
			leftOperand.symbol());

	    if ((leftOperand.exprs() != null && GType.get(BType.UNDEFINED).equals(leftOperand.exprs()))) {
		// operation with undefined is undefined
		node.expr(GType.get(BType.UNDEFINED));
		return node;
	    } else if (GType.get(BType.UNKNOWN).equals(type)) {
		node.expr(GType.get(BType.UNKNOWN));
		return node;
	    } else if (GType.get(BType.MATRIX_ACCESS_SLICE).equals(type)) {
		// transpose of slice equals itself
		node.expr(GType.get(type));
		node.symbol(leftOperand.symbol());
		return node;
	    } else if (type == null || !(type instanceof MatrixType))
		throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_TRANSPOSITION, node, type);

	    MatrixType mtype = (MatrixType) type;

	    dims = mtype.dims();
	    if (dims == null || dims.length == 0) {
		logger.debug("Missing dimensions for matrix '" + leftOperand + "'");
		// copy type
		node.expr(GType.get(mtype));
		return node;
	    } else if (dims.length > 2) {
		TypeException ex = new TypeException(ErrorMessage.TRANSPOSITION_N_DIMENSIONAL_MATRIX, node, type,
			leftOperand.code());
		String message = ex.stringify();
		node.error(new TypeError(message, Thread.currentThread().getStackTrace()));
		logger.debug(message, ex);
		node.expr(GType.get(mtype));
		throw ex;
	    }

	    IntType col = (IntType) dims[0];
	    IntType row = null;
	    if (dims.length > 1)
		row = (IntType) dims[1];

	    if (row == null) {
		// if 1D vector -> COLUMN VECTOR
		row = (IntType) new IntType(BType.INT, 1);

		// matrix with 1 column (a.k.a. column vector)
		dims = new IntType[] { col, row };
		leftOperand.expr(GType.get(BType.MATRIX, mtype.name(), mtype.of(), dims));
	    }
	    // if 2D matrix
	    IntType rowType = (IntType) GType.get(row);
	    if (!rowType.hasValue() && !rowType.hasName())
		rowType.name(TypeUtils.matrixDimName(symbol, 2));
	    IntType colType = (IntType) GType.get(col);
	    if (!colType.hasValue() && !rowType.hasName())
		colType.name(TypeUtils.matrixDimName(symbol, 1));
	    IntType[] transposeDim = new IntType[] { rowType, colType };
	    // translate an initialization and assignment of new matrix with
	    // values given
	    // from the values of symbol transposed
	    // optimizer.createTransposedMatrix(transposeSymbol,
	    // SymbolType.SCALAR, transposeDim, symbol);
	    // outputExpr = new Tuple<ExprType,
	    // String>(ExprType.get(Expr.MATRIX), transposeSymbol);
	    // update env with new symbol
	    // updateSymbols(outputExpr, transposeSymbol, SymbolType.SCALAR,
	    // transposeDim);
	    node.expr(GType.get(BType.MATRIX, TypeUtils.matrixName(node), mtype.of(), transposeDim, mtype.isSparse()));
	    return node;
	}
	// ----------- BASE EXPRESSIONS
	// ---------------------------------------------------
	case EXP: {
	    // pop two translated elements for operator
	    rightOperand = popTranslation();
	    leftOperand = popTranslation();
	    scalar = false;
	    // since an element can have multiple types..
	    // we should evaluate all of them
	    List<GType> rexprs = rightOperand.exprs();
	    Iterator<GType> rexprsIter = rexprs.iterator();
	    List<GType> lexprs = leftOperand.exprs();
	    Iterator<GType> lexprsIter = lexprs.iterator();

	    if ((lexprs != null && GType.get(BType.UNDEFINED).equals(lexprs))
		    || (rexprs != null && GType.get(BType.UNDEFINED).equals(rexprs))) {
		// operation with undefined is undefined
		node.expr(GType.get(BType.UNDEFINED));
		return node;
	    }

	    int expr_number = TypeUtils.pairExpressionsForOperator(leftOperand, rightOperand);
	    GType rexpr = null, lexpr = null;
	    for (int i = 0; i < expr_number; i++) {
		rexpr = (!rexprsIter.hasNext()) ? ((LinkedList<GType>) rexprs).peekLast() : rexprsIter.next();
		lexpr = (!lexprsIter.hasNext()) ? ((LinkedList<GType>) lexprs).peekLast() : lexprsIter.next();
		if (GType.get(BType.STRING).equals(rexpr) || GType.get(BType.STRING).equals(lexpr)) {
		    // throw new TypeException(getTypeExceptionMessage(node,
		    // leftOperand, rightOperand));
		    GType nonString = null;
		    if (GType.get(BType.STRING).equals(rexpr))
			nonString = lexpr;
		    else
			nonString = rexpr;

		    if (nonString instanceof ValuedType)
			node.expr(GType.get(BType.STRING, ((ValuedType) nonString).valueAsString()));
		    else
			node.expr(GType.get(BType.STRING));
		    return node;
		} else if (rexpr.equals(BType.UNKNOWN) || lexpr.equals(BType.UNKNOWN)) {
		    node.expr(GType.get(BType.UNKNOWN));
		    return node;
		    // type inference
		} else if (rexpr.equals(BType.SCALAR)) {
		    // this will substitute unknown with scalar
		    if (GType.get(BType.UNKNOWN).equals(lexpr)) {
			lexpr = GType.get(BType.SCALAR);
			leftOperand.expr(lexpr);
		    }
		    scalar = true;
		} else if (lexpr.equals(BType.SCALAR)) {
		    if (GType.get(BType.UNKNOWN).equals(rexpr)) {
			// this will substitute unknown with scalar
			rexpr = GType.get(BType.SCALAR);
			rightOperand.expr(rexpr);
		    }
		    scalar = true;
		} else if (lexpr.isCastableToScalar() && rexpr.isCastableToScalar()) {
		    scalar = true;
		}

		if (scalar) {
		    // optimizer.translatePower(powSymbol, leftOperand.second(),
		    // rightOperand.second());
		    // from math.h
		    GType etype = TypeUtils.resultingTypeScalarOp(lexpr, rexpr, node);
		    node.expr(etype);
		    return node;
		    // return new Tuple<ExprType,
		    // String>(ExprType.get(Expr.SCALAR),
		    // powSymbol);
		} else if (lexpr.isCastableToScalar() && rexpr.isCastableToMatrix()) {
		    // generate a new matrix
		    DimensionType expType = (DimensionType) GType.get(rexpr);
		    MatrixType nodeType = (MatrixType) GType.get(BType.MATRIX, "_tmp_", expType.of(), expType.dims());
		    node.expr(nodeType);
		    // change names
		    nodeType.name(TypeUtils.matrixName(node));
		    int n = 1;
		    for (IntType dim : nodeType.dims())
			// name just
			if (!dim.hasValue())
			    dim.name(TypeUtils.matrixDimName(node, n++));

		    return node;
		} else
		    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_OPERATOR_TYPES, node, "^",
			    leftOperand.expr(), rightOperand.expr());
	    }
	}
	// POINT-WISE OPERATORS
	case PLUS:
	case MINUS:
	case TIMES:
	case LEFTDIV:
	case RIGHTDIV:
	case ELEMENTWISE_TIMES:
	case ELEMENTWISE_LEFTDIV:
	case ELEMENTWISE_RIGHTDIV:
	case ELEMENTWISE_EXP: {
	    // pop two translated elements for operator
	    rightOperand = popTranslation();
	    // single operator if stack contains only 1 element
	    boolean singleOperator = node.childs().size() == 1;

	    List<GType> rexprs = null;
	    List<GType> lexprs = null;
	    GType rexpr = null, lexpr = null;
	    int expr_number = -1;

	    if (!singleOperator) {
		leftOperand = popTranslation();
		// expr_number =
		// TypeUtils.pairExpressionsForOperator(leftOperand,
		// rightOperand);
		lexprs = leftOperand.exprs();
		rexprs = rightOperand.exprs();
		expr_number = (lexprs.size() > rexprs.size()) ? lexprs.size() : rexprs.size();
	    } else {
		leftOperand = null;
		rexprs = rightOperand.exprs();
		expr_number = rexprs.size();
	    }

	    if ((lexprs != null && lexprs.equals(BType.UNDEFINED)) || rexprs.equals(BType.UNDEFINED)) {
		// operation with undefined is undefined
		node.expr(GType.get(BType.UNDEFINED));
		return node;
	    }

	    // list of right exprs to be removed because unfeasible
	    Set<Integer> rrids = new HashSet<Integer>();
	    // list of left exprs to be removed because unfeasible
	    Set<Integer> rlids = new HashSet<Integer>();
	    // since an element can have multiple types..
	    // we should evaluate all of them
	    Iterator<GType> lexprsIt = null;
	    if (lexprs != null)
		lexprsIt = lexprs.iterator();
	    Iterator<GType> rexprsIt = rexprs.iterator();
	    while ((lexprsIt != null && lexprsIt.hasNext()) || rexprsIt.hasNext()) {
		if (rexprsIt.hasNext())
		    rexpr = rexprsIt.next();
		// else keep the prev value

		if (lexprsIt != null && lexprsIt.hasNext())
		    lexpr = lexprsIt.next();
		// else keep the prev value

		if (GType.get(BType.STRING).equals(rexpr)
			|| (!singleOperator && GType.get(BType.STRING).equals(lexpr))) {
		    // throw new TypeException(getTypeExceptionMessage(node,
		    // leftOperand, rightOperand));
		    GType nonString = null;
		    if (!singleOperator) {
			if (GType.get(BType.STRING).equals(rexpr))
			    nonString = lexpr;
			else
			    nonString = rexpr;

			if (nonString instanceof ValuedType)
			    node.expr(GType.get(BType.STRING, ((ValuedType) nonString).valueAsString()));
			else
			    node.expr(GType.get(BType.STRING));
		    } else
			// copy type
			node.expr(GType.get(rexpr.type()));

		    continue;
		}

		if ((leftOperand != null && GType.get(BType.UNKNOWN).equals(lexpr))
			&& GType.get(BType.UNKNOWN).equals(rexpr)) {
		    if (GType.get(BType.UNKNOWN).equals(rexpr) || GType.get(BType.UNKNOWN).equals(lexpr)) {
			// type inference failed
			node.expr(GType.get(BType.UNKNOWN));
			continue;
		    }
		} else if (leftOperand == null && GType.get(BType.UNKNOWN).equals(rexpr)) {
		    if (GType.get(BType.UNKNOWN).equals(rexpr)) {
			// type inference failed
			node.expr(GType.get(BType.UNKNOWN));
			continue;
		    }
		} else if (leftOperand != null && GType.get(BType.UNKNOWN).equals(rexpr)
			&& !GType.get(BType.UNKNOWN).equals(lexpr)) {
		    // type inference
		    if (GType.get(BType.UNKNOWN).equals(rexpr) || GType.get(BType.UNKNOWN).equals(lexpr)) {
			// type inference failed
			node.expr(GType.get(BType.UNKNOWN));
			continue;
		    }
		} else if (leftOperand != null && GType.get(BType.UNKNOWN).equals(lexpr)
			&& !GType.get(BType.UNKNOWN).equals(rexpr)) {
		    // type inference
		    if (GType.get(BType.UNKNOWN).equals(rexpr) || GType.get(BType.UNKNOWN).equals(lexpr)) {
			// type inference failed
			node.expr(GType.get(BType.UNKNOWN));
			continue;
		    }
		}

		if (singleOperator) {
		    if (rexpr.isCastableToScalar()) {

			// things like -1
			ValuedType vrexpr = (ValuedType) rexpr;
			if (vrexpr.hasValue()) {
			    if (rexpr.equals(BType.SCALAR))
				node.expr(GType.get(BType.SCALAR, -1 * ((ScalarType) rexpr).value()));
			    else if (rexpr.isCastableToBool())
				node.expr(GType.get(BType.INT, -1 * ((IntType) rexpr).value()));
			    else
				throw new UndefinedTranslationException(ErrorMessage.INTERNAL_UNARY_UNSUPPORTED_TYPE,
					node, rexpr);
			} else
			    node.expr(GType.get(rexpr.type()));
		    } else if (rexpr.isCastableToMatrix()) {
			// values of matrices are not stored, so type unchanged
			node.expr(GType.get(rexpr));
		    } else
			throw new UndefinedTranslationException(ErrorMessage.INTERNAL_UNARY_UNSUPPORTED_TYPE, node,
				rexpr);
		    // optimizer.translateUnaryScalarExpression(exprSymbol,
		    // node.type(), rightOperand.second());
		    continue;
		} else
		// scalar times scalar
		if (lexpr.isCastableToScalar() && rexpr.isCastableToScalar()) {
		    GType etype;
		    etype = TypeUtils.resultingTypeScalarOp(lexpr, rexpr, node);

		    node.expr(etype);
		    // Tuple<ExprType, String> tp = new Tuple<ExprType,
		    // String>(etype, exprSymbol);
		    // optimizer.translateScalarExpression(tp, node.type(),
		    // leftOperand, rightOperand);
		    // statementEnv.updateSingleSymbol(null, etype, exprSymbol,
		    // null);
		    continue;
		    // matrix times scalar
		} else if (lexpr.isCastableToMatrix() && rexpr.isCastableToScalar()) {
		    GType expr = matrixScalarOp(node, lexpr, rexpr);
		    node.expr(expr);

		    // optimizer.translateMatrixScalarExpression(exprSymbol,
		    // node.type(), leftOperand.second(), dims,
		    // rightOperand.second());
		    // statementEnv.updateSingleSymbol(SymbolType.SCALAR,
		    // ExprType.get(Expr.MATRIX), exprSymbol, dims);
		    // return new Tuple<ExprType,
		    // String>(ExprType.get(Expr.MATRIX),
		    // exprSymbol);
		    continue;
		} else if (rexpr.isCastableToMatrix() && lexpr.isCastableToScalar()) {
		    GType expr = matrixScalarOp(node, rexpr, lexpr);
		    node.expr(expr);

		    continue;
		    // matrix times matrix
		} else if (rexpr.isCastableToMatrix() && lexpr.isCastableToMatrix()) {
		    DimensionType dlexpr = (DimensionType) lexpr;
		    DimensionType drexpr = (DimensionType) rexpr;
		    boolean pointwise_op = node.type().equals(NodeType.ELEMENTWISE_TIMES)
			    || node.type().equals(NodeType.ELEMENTWISE_LEFTDIV)
			    || node.type().equals(NodeType.ELEMENTWISE_RIGHTDIV)
			    || node.type().equals(NodeType.ELEMENTWISE_EXP) || node.type().equals(NodeType.PLUS)
			    || node.type().equals(NodeType.MINUS);
		    boolean matrix_op = node.type().equals(NodeType.TIMES) || node.type().equals(NodeType.LEFTDIV)
			    || node.type().equals(NodeType.RIGHTDIV);
		    if (pointwise_op || (matrix_op && TypeUtils.isDegeneratedMatrix(dlexpr)
			    && TypeUtils.isDegeneratedMatrix(drexpr))) {
			GType outType = TypeUtils.resultingTypeMatrixMatrixPointwiseOp(node, dlexpr, drexpr);
			node.expr(outType);
			node.symbol(outType.name());
			continue;
		    } else if (matrix_op) {
			GType outType = TypeUtils.resultingTypeMatrixMatrixOp(node, dlexpr, drexpr);
			node.expr(outType);
			node.symbol(outType.name());
			// optimizer.translateMatrixMatrixExpression(exprSymbol,
			// node.type(), leftOperand.second(), dimsLeft,
			// rightOperand.second(), dimsRight);
			// statementEnv.updateSingleSymbol(out.second(),
			// out.first(), exprSymbol, out.third());
			continue;
		    } else {
			throw new TypeException(
				ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node,
				lexpr, rexpr);
			// rrids.add(ridx);
			// rlids.add(lidx);
		    }

		} else
		    throw new TypeException(
			    ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node, lexpr,
			    rexpr);

	    }

	    // if (!rrids.isEmpty()) {
	    // logger.debug("Removing types from right operand '" +
	    // rightOperand.name() + "'.");
	    // rightOperand.removeExprs(rrids);
	    // }
	    // if (!rlids.isEmpty()) {
	    // logger.debug("Removing types from left operand '" +
	    // leftOperand.name() + "'.");
	    // leftOperand.removeExprs(rlids);
	    // }

	    return node;
	}

	case EXPRESSION:
	case ID_NODE:
	case PARENS:
	    // copy expr from subnode
	    // if (!node.childs().isEmpty()) {
	    // AASTNode id = node.childs().get(0);
	    // if (id != null)
	    // node.expr(id.exprs(), false);
	    // }
	case DOT:
	case RHS:
	case COMMA:
	    // translate nothing
	    return null;

	// special matrix indexes
	case END:
	    node.expr(specialIndexType(node, BType.MATRIX_ACCESS_LAST));
	    return node;

	case COLON:
	    Iterator<AASTNode> iter = iterTranslation();
	    // colon can be used with two integers 1:2 or alone :
	    AASTNode firstStackElem = null;
	    AASTNode secondStackElem = null;

	    // if above in tree another colon or an expression this is of the
	    // form 1:2 / 1:2:3
	    // otherwise it is a single colon
	    if (!existsPreviousNodeType(NodeType.COLON, 1) && node.childs().isEmpty()) {
		node.expr(specialIndexType(node, BType.MATRIX_ACCESS_ALL));
		return node;
	    }

	    // GET STACK ELEMENTS in reverse order..
	    if (!iter.hasNext())
		throw new UndefinedTranslationException(ErrorMessage.INTERNAL_SLICE_WITHOUT_START_STOP, node);
	    else
		iter.next();

	    if (!iter.hasNext())
		throw new UndefinedTranslationException(ErrorMessage.INTERNAL_SLICE_WITHOUT_START_STOP, node);
	    // END GET STACK ELEMENTS

	    // here firstStackElem/secondStackElem are both scalars, case 1:2 or
	    // 1:2:3
	    // end lookahead in stack, actually pop out needed elements
	    secondStackElem = popTranslation();
	    firstStackElem = popTranslation();

	    GType step, start;
	    if (existsPreviousNodeType(NodeType.COLON, 1)) {
		start = firstStackElem.expr();
		step = secondStackElem.expr();
		// wait for real translation to close the slice operator
		GType matSlice = GType.get(BType.MATRIX_ACCESS_SLICE, node.name(), start,
			new Triple<GType, GType, GType>(start, step, null));
		node.expr(matSlice);
		node.attr(NodeAttr.SYMBOL, firstStackElem.symbol() + GType.SLICE_SEP + secondStackElem.symbol());
		return node;
	    } else {
		Triple<GType, GType, GType> slicingParams;
		GType end = secondStackElem.expr();
		// second can be either a MATRIX_ACCESS_SLICE or a normal type
		if (firstStackElem.expr() != null && firstStackElem.expr().equals(BType.MATRIX_ACCESS_SLICE)) {
		    // get original slicing operators
		    start = ((SliceType) firstStackElem.expr()).slices().first();
		    step = ((SliceType) firstStackElem.expr()).slices().second();
		} else {
		    start = firstStackElem.expr();
		    // access without step definition, set step to 1.
		    // exprtype the same as end for coherence
		    step = GType.get(BType.INT, 1);
		}

		// CASE INCOMPLETE DEFINITION
		if (start.equals(BType.UNKNOWN) || step.equals(BType.UNKNOWN) || end.equals(BType.UNKNOWN)) {
		    node.expr(GType.get(BType.UNKNOWN));
		    return node;
		} else if (start.equals(BType.UNDEFINED) || step.equals(BType.UNDEFINED)
			|| end.equals(BType.UNDEFINED)) {
		    node.expr(GType.get(BType.UNDEFINED));
		    return node;
		}

		slicingParams = new Triple<GType, GType, GType>(start, step, end);

		// make sure all types of the triple are equal
		Triple<GType, GType, GType> newSlicingParams = TypeUtils.resultingTypeSliceOp(slicingParams);
		TypeException ex = new TypeException(ErrorMessage.UNSUPPORTED_SLICE, node, newSlicingParams);

		GType stype = null;
		ValuedType outStart = null, outStep = null, outEnd = null;

		// cast every element of newslicingparams to valued type to see if we can
		// compute at compilation time the dimension
		try {
		    outStart = castToValuedType(node, newSlicingParams.first());
		    outStep = castToValuedType(node, newSlicingParams.second());
		    outEnd = castToValuedType(node, newSlicingParams.third());
		} catch (CastException e) {
		    // slice with unknown parameters and slice parts
		    stype = GType.get(BType.UNKNOWN);
		    GType etype = GType.get(BType.MATRIX_ACCESS_SLICE, TypeUtils.matrixName(node), stype,
			    newSlicingParams, null);
		    node.expr(etype);
		    return node;
		} catch (UndefinedTranslationException e) {
		    throw ex;
		}

		// decide "of" type
		if (GType.get(BType.INT).canRepresent(outStart) && GType.get(BType.INT).canRepresent(outStep)
			&& GType.get(BType.INT).canRepresent(outEnd))
		    stype = GType.get(BType.INT);
		else
		    stype = GType.get(BType.SCALAR);

		IntType dim = null;
		// a slice cannot exist without his slices start/step/end
		if (outStart.hasValue() && outStep.hasValue() && outEnd.hasValue())
		    if (stype.equals(BType.INT))
			dim = (IntType) GType.get(BType.INT,
				(int) Math.max(1, ((((IntType) outEnd).valueAsInt() - ((IntType) outStart).valueAsInt())
					/ ((IntType) outStep).valueAsInt()) + 1));
		    else {
			double dend = outEnd.valueAsDouble();
			double dstart = outStart.valueAsDouble();
			double dstep = outStep.valueAsDouble();
			dim = (IntType) GType.get(BType.INT, (int) Math.max(1, ((dend - dstart) / dstep) + 1));
		    }
		else
		    dim = (IntType) GType.get(BType.INT);

		GType etype = GType.get(BType.MATRIX_ACCESS_SLICE, "_slice_tmp_", stype, newSlicingParams, dim);
		node.expr(etype);
		// matrixName uses the type information
		String sliceName = TypeUtils.matrixName(node);
		node.symbol(sliceName);
		node.expr().name(sliceName);
		// just name dimensions not known at compile time
		if (!dim.hasValue())
		    dim.name(TypeUtils.matrixDimName(sliceName, 1));
		// name also start/step/end if they don't have value known at compile time
		if (!outStart.hasValue())
		    outEnd.name(TypeUtils.slicePartName(sliceName, "start"));
		if (!outStep.hasValue())
		    outEnd.name(TypeUtils.slicePartName(sliceName, "step"));
		if (!outEnd.hasValue())
		    outEnd.name(TypeUtils.slicePartName(sliceName, "end"));
		// optimizer.translateSliceOperator(outSlice, newSlicingParams);
		// statementEnv.updateSingleSymbol(stype, etype, newSymbol,
		// slicedim);
		// outputExpr = new Tuple<ExprType, String>(etype, newSymbol);
		return node;
	    }

	    // ----- FUNCTION CALLS
	    // -------------------------------------------------------------------------
	case FUNCTION_PARAMETER_LIST: {
	    List<AASTNode> params = new ArrayList<AASTNode>();
	    AASTNode param;
	    for (int n = 0; n < lastParameterList().size(); n++) {
		param = popTranslation();
		params.add(0, param);
	    }
	    node.attr(NodeAttr.PARAMS, params);
	    return node;
	}
	case APPLY: {
	    AASTNode paramsNode = popTranslation();
	    AASTNode funIdNode = null;
	    if (!paramsNode.type().equals(NodeType.FUNCTION_PARAMETER_LIST) && aast.functionSymbolExists(paramsNode)) {
		// function without arguments, got directly function id -> f
		AASTNode realParamNode = aast.functionSymbolNode(paramsNode).child(NodeType.PARAMETER_LIST);
		List<AASTNode> params = realParamNode.childs();
		if (params.size() == 1 && params.get(0).type().equals(NodeType.VOID)) {
		    funIdNode = paramsNode;
		    paramsNode = realParamNode;
		    paramsNode.attr(NodeAttr.PARAMS, new ArrayList<AASTNode>());
		}
	    } else if (paramsNode.type().equals(NodeType.VOID)) {
		// function without arguments, got VOID node -> f()
		paramsNode.attr(NodeAttr.PARAMS, new ArrayList<AASTNode>());
	    }
	    if (funIdNode == null)
		funIdNode = popTranslation();

	    // if not an external symbol, nor an internal function/matrix
	    // symbol, or encoded params are not params
	    if (!paramsNode.hasAttr(NodeAttr.PARAMS)) {
		UnfinishedTranslationException ex = new UnfinishedTranslationException(
			ErrorMessage.INTERNAL_FUN_UNDEFINED, node);
		node.error(new SyntaxError(ex.stringify(), Thread.currentThread().getStackTrace()));
		throw ex;
	    } else if (node.childExists(NodeType.ID) && node.child(NodeType.ID).hasAttr(NodeAttr.DYNAMIC_ID)
		    || node.childExists(NodeType.FIELDACCESS)
			    && node.child(NodeType.FIELDACCESS).lastChild(NodeType.ID).hasAttr(NodeAttr.DYNAMIC_ID)) {
		// set dynamic dimensions
		List<Integer> general_dims = new LinkedList<Integer>();
		List<AASTNode> params = (List<AASTNode>) paramsNode.attr(NodeAttr.PARAMS);
		for (int gd = 0; gd < params.size(); ++gd) {
		    GType ptype = TypeUtils.getExprGeneralized(params.get(gd));
		    // if scalar without value, or matrix
		    if ((ptype.isCastableToScalar() && !((ValuedType<?>) ptype).hasValue())
			    || ptype.isCastableToMatrix())
			general_dims.add(gd);
		}
		String name = null;
		if (node.childExists(NodeType.ID))
		    name = node.child(NodeType.ID).symbol();
		else if (node.childExists(NodeType.FIELDACCESS)) {
		    name = node.child(NodeType.FIELDACCESS).lastChild(NodeType.ID).symbol();
		}
		node.parent(NodeType.STATEMENT_LIST).propagateAttribute(NodeAttr.DYNAMIC_ID, general_dims, name);
	    }

	    GType functionType = funIdNode.expr();

	    if ((functionType != null && functionType.equals(BType.FUNCTION)) || aast.functionSymbolExists(funIdNode)) {
		// here we have a function call to a user-defined function
		// that's the perfect point to infer the types of this
		// user-defined parameters
		TypeUtils.resolveFunctionTypes(node, funIdNode, paramsNode);
		// unknown return value.. go on until we can infer also this
	    } else if (GType.get(BType.UNKNOWN).equals(functionType)) {
		String functionName = funIdNode.symbol();
		// unknown function call. maybe backend will know
		node.expr(GType.get(BType.UNKNOWN));
		node.attr(NodeAttr.FUNCALL, functionName);
	    } else if (functionType != null && functionType.isCastableToMatrix()) {
		// remove funcall if in a previous step we identified this as a
		// funcall
		node.removeAttr(NodeAttr.FUNCALL);
		// parameters are for a matrix
		paramsNode.attr(NodeAttr.MATRIX_PARAMS, funIdNode);
		List<AASTNode> params = (List<AASTNode>) paramsNode.attr(NodeAttr.PARAMS);
		// get matrix dimension
		DimensionType mtype = (DimensionType) functionType;
		IntType[] mdims = mtype.dims();
		GType ofType = mtype.of();
		if (// define symbolic dimensions based on parameter size
		mdims == null
			// we have more params than dimensions in the matrix.
			// enlarge matrix with symbolic params size
			|| mdims.length < params.size()) {
		    if (mtype instanceof MatrixType) {
			((MatrixType) mtype).addDims(params.size());
			mdims = mtype.dims();
		    } else {
			AASTNode originalUnknownNode = (AASTNode) node.findAttr(NodeAttr.IMPLICITLY_DEFINED);
			if (originalUnknownNode != null)
			    throw new TypeException(ErrorMessage.INTERNAL_WRONG_IMPLICIT_TYPE, node, mtype,
				    funIdNode.code(), originalUnknownNode);
			else
			    throw new TypeException(ErrorMessage.INTERNAL_MATRIX_SLICE_WITH_UNSUPPORTED_TYPE, node,
				    funIdNode, params);
		    }
		}
		// type check and parameters list building
		List<GType> matAccessParams = new ArrayList<GType>();
		List<IntType> accessDims = new ArrayList<IntType>();

		boolean ismatrix = false;
		boolean isKnown = true;
		for (AASTNode param : params) {
		    if (param.expr() != null) {
			if ((param.expr().equals(BType.MATRIX) || param.expr().equals(BType.MATRIX_ACCESS_ALL)
				|| param.expr().equals(BType.MATRIX_ACCESS_SLICE))) {
			    ismatrix = true;
			    break;
			} else if (param.expr().equals(BType.UNKNOWN)) {
			    isKnown = false;
			    break;
			}
		    } else {
			isKnown = false;
			break;
		    }
		}

		if (!isKnown)
		    node.expr(GType.get(BType.UNKNOWN));
		else {
		    // choose a name
		    if (!node.hasAttr(NodeAttr.SYMBOL))
			if (funIdNode.type().equals(NodeType.FIELDACCESS))
			    // use funIdNode.symbol() that will use the access fields as a name
			    node.symbol(Symbols.getSymbolFromType(
				    funIdNode.symbol().replaceAll("\\" + GType.STRUCT_SEP, "__"), BType.MATRIX));
			else if (ismatrix)
			    node.symbol(Symbols.getSymbolFromType(funIdNode.name(), BType.MATRIX));
			else
			    node.symbol(Symbols.getSymbolFromType(funIdNode.name(), BType.SCALAR));
		    boolean iscellaccess = true;
		    for (AASTNode param : params) {

			if (param.expr() != null && param.expr().equals(BType.MATRIX)) {
			    // if matrix add dimension
			    MatrixType pmtype = ((MatrixType) param.expr());
			    String nodeSymbol = node.symbol();
			    GType pmOfType = pmtype.of();
			    IntType[] dim = TypeUtils.realDims(pmtype.dims());
			    if (pmtype.dims() == null || dim.length == 0) {
				AASTNode originalUnknownNode = (AASTNode) node.findAttr(NodeAttr.IMPLICITLY_DEFINED);
				if (originalUnknownNode != null)
				    throw new TypeException(ErrorMessage.INTERNAL_WRONG_IMPLICIT_TYPE, node, pmtype,
					    param.code(), originalUnknownNode);
				else
				    throw new TypeException(ErrorMessage.INTERNAL_MATRIX_SLICE_WITH_UNSUPPORTED_TYPE,
					    node, funIdNode, params);
			    } else if (dim.length > 0) {
				if (pmOfType.equals(BType.BOOL))
				    // a boolean matrix, even when single is always interpreted as as 1d
				    // the apply of a boolean matrix should
				    // have dynamic dimensions (apply of boolean could results in less elements than
				    // the len of the parameter matrix)
				    // and a matrix of boolean is always reduced to a list of matching indexes
				    accessDims.add((IntType) GType.get(BType.INT)
					    .name(TypeUtils.matrixDimName(nodeSymbol, accessDims.size() + 1)));
				else if (params.size() == 1) {
				    // add all of them, in this case the output matrix takes the dimension of the
				    // param matrix
				    for (IntType tmpdim : dim) {
					IntType newdim = (IntType) GType.get(tmpdim);
					if (!newdim.hasValue() && !newdim.hasName())
					    newdim.name(TypeUtils.matrixDimName(nodeSymbol, accessDims.size() + 1));
					accessDims.add(newdim);
				    }
				} else {
				    // in this case treat the matrix as a flat array
				    Integer totDim = 1;
				    for (IntType tmpdim : dim)
					if (tmpdim.hasValue())
					    totDim *= tmpdim.valueAsInt();
					else {
					    totDim = null;
					    break;
					}
				    accessDims.add((IntType) GType.get(BType.INT, totDim)
					    .name(TypeUtils.matrixDimName(nodeSymbol, accessDims.size() + 1)));
				}
			    }
			    matAccessParams.add(param.expr());
			    iscellaccess = false;
			} else if (param.expr() != null && param.expr().equals(BType.MATRIX_ACCESS_SLICE)) {
			    IntType sdim = ((SliceType) param.expr()).dim();
			    accessDims.add(sdim);
			    matAccessParams.add(param.expr());
			    iscellaccess = false;
			} else {
			    if (param.expr() != null && param.expr().equals(BType.MATRIX_ACCESS_ALL)) {
				// ith dimension should be copied
				int i = params.indexOf(param);
				accessDims.add(mdims[i]);
				iscellaccess = false;
			    } else
				// this is an end or a number. the resulting matrix
				// has the corresponding dimension equal to 1.
				accessDims.add((IntType) GType.get(BType.INT, 1));
			    matAccessParams.add(param.expr());
			}
		    }

		    if (iscellaccess) {
			// if node type is an element of the matrix
			exprType = GType.get(ofType.type());
			accessDims = null;
		    } else {
			String nodeSymbol = node.symbol();
			List<IntType> newDims = new LinkedList<IntType>();
			// remove first n ones
			// IntType[] realDims = TypeUtils.realFirstDim(mdims);
			// copy first n ones
			int i;
			for (i = 0; i < mdims.length; ++i)
			    if (mdims[i].hasValue() && mdims[i].valueAsInt() == 1)
				newDims.add((IntType) GType.get(mdims[i]));
			    else
				break;
			for (int j = 0; j < accessDims.size(); ++j, ++i) {
			    IntType newdim = accessDims.get(j);
			    // do not override an already existing symbol
			    if (!newdim.hasValue() && !newdim.hasName())
				newdim.name(TypeUtils.matrixDimName(nodeSymbol, newDims.size() + 1));
			    newDims.add(newdim);
			}
			// copy last dimensions? NO!
			// in MATLAB if you specify less dimensions than the ones actually
			// present in the matrix, this means that
			// 1) if the last param is MATRIX_ACCESS_ALL then
			// all the next dimensions should be
			// squeezed in the last one
			// (similar to MATRIX_ACCESS_ALL for all)
			// 2) if the last param is MATRIX_ACCESS_SLICE then
			// all the next dimensions should be ignored
			// (equivalent to set 1 for all)
			// 3) if the last param is a MATRIX then
			// the matrix elements are interpreted as
			// a flat array that can account for all the remaining dimensions
			IntType lastDim = newDims.get(newDims.size() - 1);
			AASTNode lastParam = params.get(params.size() - 1);
			if (lastParam.expr().equals(BType.MATRIX_ACCESS_ALL)) {
			    newDims.remove(newDims.size() - 1);
			    Integer dim = 1;
			    int startcollapsedims = --i;
			    for (; i < mdims.length; ++i)
				if (dim != null)
				    dim = (mdims[i].hasValue()) ? dim * mdims[i].value() : null;
				else
				    break;
			    // last dimension
			    IntType veryLastDim = (IntType) GType.get(BType.INT, dim);
			    if (dim == null)
				// if all doesn't join multiple dimensions, just reuse the previous dim
				if (mdims.length - startcollapsedims == 1 && lastDim.hasName())
				    veryLastDim.name(lastDim.name());
				else
				    veryLastDim.name(TypeUtils.matrixDimName(nodeSymbol, newDims.size() + 1));
			    newDims.add(veryLastDim);
			    if (newDims.size() == 1) {
				// this is a column vector
				newDims.add((IntType) GType.get(BType.INT, 1));
			    }
			} else {
			    // add eventually trailing ones
			    for (i = newDims.size(); i < mdims.length; ++i)
				if (mdims[i].hasValue() && mdims[i].valueAsInt() == 1)
				    newDims.add((IntType) GType.get(mdims[i]));
				else
				    break;
			}
			// apply of sparse is sparse
			boolean sparse = (mtype instanceof MatrixType) && ((MatrixType) mtype).isSparse();
			DimensionType dtype = (DimensionType) GType.get(BType.MATRIX, nodeSymbol, ofType,
				newDims.toArray(new IntType[newDims.size()]), sparse);
			exprType = dtype;
		    }
		    // update current node values
		    node.expr(exprType);
		}
		node.attr(NodeAttr.MATACCESS, funIdNode.symbol());

	    } else if (functionType.equals(BType.STRUCT)) {
		String fullStructAccess = funIdNode.symbol();
		// { a : { b: { c } } }
		// ExprType baseStructDefinition = functionType;
		// 'a.b.c'
		throw new TypeException(ErrorMessage.UNSUPPORTED_STRUCT_ACCESS_BY_INDEX, node);
		// logger.warn("Access to structure '" + node.code() + "' at " + node.location()
		// + " of "
		// + node.compilationUnit().sourcePath() + " not supported. Skipping, STRUCT
		// type will be used.");
		// node.expr(functionType);

	    } else if (functionType instanceof ValuedType) {
		// if base value type (string/double..) applied to something
		// the type is the same as functionType.
		node.expr(functionType);
		// TODO this is wrong
		// a = 1; a([1 1; 1 1]) = 1 1; 1 1
		// in case this valued type is used as a matrix
		paramsNode.attr(NodeAttr.MATRIX_PARAMS, funIdNode);
		node.attr(NodeAttr.MATACCESS, funIdNode.symbol());
	    } else if (GType.get(BType.UNDEFINED).equals(functionType)) {
		// undefined function call
		UnboundException ex = new UnboundException(ErrorMessage.USER_UNDEFINED_SYMBOL_ENV, node,
			funIdNode.code());
		node.error(new UnboundError(ex.stringify(), Thread.currentThread().getStackTrace()));
		throw ex;
	    } else {
		TypeException ex = new TypeException(ErrorMessage.USER_UNDEFINED_SYMBOL, node, funIdNode.code());
		node.error(new TypeError(ex.stringify(), Thread.currentThread().getStackTrace()));
		throw ex;
	    }

	    if (node.expr() != null && node.expr() instanceof DimensionType)
		// keep aligned matrix name with node symbol
		node.attr(NodeAttr.SYMBOL, node.expr().name());

	    return node;
	}
	default:
	    TypeException ex = new TypeException(ErrorMessage.INTERNAL_UNSUPPORTED_OPERATION, statement);
	    node.error(new TypeError(ex.stringify(), Thread.currentThread().getStackTrace()));
	    throw ex;
	}

    }

    /**
     * add translate flag if needed
     * 
     * @param node
     */
    private void translateMatrix(AASTNode node, Boolean[] one_dimensional_as_row) {
	node.attr(NodeAttr.ONE_DIMENSIONAL_VECTORS_INTERPRETATION, one_dimensional_as_row);
	boolean childMatrix = node.parentExists(NodeType.MATRIX);
	int parentMatrixDepth = -1;
	if (childMatrix)
	    parentMatrixDepth = node.parentDepth(NodeType.MATRIX);
	node.attr(NodeAttr.TRANSLATE, !childMatrix || node.parentExists(TypeUtils.expressionNodes(), parentMatrixDepth)
		|| node.parentExists(NodeType.APPLY, parentMatrixDepth));
    }

    private ValuedType castToValuedType(AASTNode node, GType sliceElement)
	    throws CastException, UndefinedTranslationException {
	ValuedType outType = null;

	if (sliceElement.equals(BType.UNKNOWN)) {
	    throw new CastException(ErrorMessage.UNSUPPORTED_SLICE_PART_TYPE, node);
	} else if (sliceElement instanceof ValuedType) {
	    outType = (ValuedType) sliceElement;
	} else if (sliceElement.equals(BType.MATRIX_ACCESS_LAST)) {
	    IndexType endType = (IndexType) sliceElement;
	    if (endType.dimension() != null) {
		outType = endType.dimension();
	    } else
		throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_SLICE_PART_TYPE, node);
	} else if (TypeUtils.isDegeneratedMatrix(sliceElement)) {
	    // allowed only if it's a degenerated case of 1x1 matrix or slice of 1 element.
	    DimensionType endType = (DimensionType) sliceElement;
	    // degenerated case, use the value of this 1x1 matrix
	    outType = (ValuedType) endType.of();
	} else
	    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_SLICE_PART_TYPE, node);

	return outType;
    }

    private GType matrixScalarOp(AASTNode node, GType matrix, GType scalar) {
	DimensionType dtype = (DimensionType) matrix;
	IntType[] dims = dtype.dims();
	// GType ret = GType.get(matrix);
	if (dtype.of().canRepresent(scalar))
	    // matrix type doesn't change
	    return GType.get(matrix).name(TypeUtils.matrixName(node));
	else if (matrix instanceof MatrixType)
	    return GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(scalar.type()), dims,
		    ((MatrixType) matrix).isSparse());
	else
	    return GType.get(BType.MATRIX, TypeUtils.matrixName(node), GType.get(scalar.type()), dims);
    }

    /**
     * find nearest symbol types or unknown/undefined otherwise.
     * 
     * @param node
     * @return a copy of the symbols, or undefined/unknown
     */
    private List<GType> nearestSymbolTypes(AASTNode node, BType defaultType) {
	AASTNode parentAASTNode = null;
	List<GType> types = null;

	if (node.parentExists(NodeType.FIELDACCESS)) {
	    AASTNode fieldaccess = node.parent(NodeType.FIELDACCESS);
	    AASTNode baseEl = fieldaccess.childs().get(0);
	    // --- LOGIC TO DECIDE THE INPUTNESS (FOR RIGHT HAND SIDE) --
	    // if we don't find a symbol equal to base struct, this should be treated as
	    // either an undefined symbol
	    // or a symbol coming from load
	    boolean isInput = (baseEl.expr() != null && baseEl.expr().isInput()) || aast.nearestSymbol(baseEl) == null
		    && !(baseEl.hasAttr(NodeAttr.GLOBAL_VAR) && (Boolean) baseEl.attr(NodeAttr.GLOBAL_VAR));
	    GType undefType = null;
	    if (!isInput) {
		// if last element in structure, check base element of structure
		// if base element is input, the default type is the undefined type
		// that can be UNDEFINED or MATRIX depending on if we found a load before this
		// statement or not
		// if it's not input then it will be the defaultType
		undefType = GType.get(defaultType);
	    } else {
		undefType = TypeUtils.getUndefinedType(node, defaultType);
	    }

	    types = TypeUtils.updateStructDefinition(fieldaccess, baseEl, node, undefType);
	} else {
	    // if not fieldaccess
	    parentAASTNode = aast.nearestSymbol(node);

	    // node doesn't have an equivalent before
	    if (parentAASTNode == null) {
		types = new LinkedList<GType>();
		types.add(TypeUtils.getUndefinedType(node, defaultType));
		// node does have an equivalent before, with a type
	    } else if (parentAASTNode.exprs() != null) {
		// use the same array.. so that a change in parentAASTNode will be
		// automatically reflected on all other nodes
		types = parentAASTNode.exprs();
		// node does have an equivalent before, without a type
	    } else
		types = aast.undefinedType(node);
	}
	return types;
    }

    private List<GType> nearestSymbolTypes(AASTNode node) {
	return nearestSymbolTypes(node, BType.UNKNOWN);
    }

    @Override
    public void setBranch(AASTNode node, String tokenValue) {
    }

}
