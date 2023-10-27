package eu.cosbi.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeError;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.FunctionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.symbols.Symbols;

public class TypeUtils {
    private static Logger logger = LogManager.getLogger(TypeUtils.class);

    public static String matrixName(AASTNode node) {
	// TODO Auto-generated method stub
	return lhsIdNode(node);
    }

    public static GType getExprGeneralized(AASTNode node) {
	GType exprResult = node.expr();
	if (exprResult == null && node.childs().size() == 1) {
	    return getExprGeneralized(node.childs().get(0));
	} else {
	    return exprResult;
	}
    }

    public static AASTNode getSubNode(AASTNode node, NodeType type) {
	List<NodeType> allowed = new ArrayList<NodeType>();
	allowed.add(NodeType.PARENS);
	allowed.add(NodeType.EXPRESSION);
	allowed.add(NodeType.ID_NODE);

	if (!allowed.contains(node.type()))
	    return node;
	if (node.childs().size() == 1 && !node.type().equals(type)) {
	    return getIDNode(node.childs().get(0));
	} else {
	    return node;
	}
    }

    /**
     * get bottom-level ID that contains the variable expression
     * 
     * @param node
     * @return
     */
    public static AASTNode getIDNode(AASTNode node) {
	return getSubNode(node, NodeType.ID);
    }

    /**
     * name of inttypes that are dimension of matrices
     * 
     * @param symbol
     * @param i
     * @return
     * @throws UndefinedTranslationException
     */
    public static String matrixDimName(AASTNode node, int i) {
	return matrixDimName(lhsIdNode(node), i);
    }

    public static String matrixDimName(String nodeSymbol, int i) {
	return nodeSymbol + GType.STRUCT_SEP + "dim" + Integer.toString(i);
    }

    public static String slicePartName(String nodeSymbol, String structSuffix) {
	return nodeSymbol + GType.STRUCT_SEP + structSuffix;
    }

    public static ArrayList<NodeType> baseExpressionNodes() {
	ArrayList<NodeType> skip_translation = new ArrayList<NodeType>(3);
	skip_translation.add(NodeType.INT);
	skip_translation.add(NodeType.FLOAT);
	skip_translation.add(NodeType.ID);
	return skip_translation;
    }

    public static ArrayList<NodeType> unaryExpressionNodes() {
	ArrayList<NodeType> skip_translation = new ArrayList<NodeType>(3);
	skip_translation.add(NodeType.MINUS);
	skip_translation.add(NodeType.ELEMENTWISE_CCT);
	skip_translation.add(NodeType.TRANSPOSE);
	return skip_translation;
    }

    public static ArrayList<NodeType> requireDoubleNodes() {
	ArrayList<NodeType> skip_translation = new ArrayList<NodeType>(17);
	skip_translation.add(NodeType.LEFTDIV);
	skip_translation.add(NodeType.ELEMENTWISE_LEFTDIV);
	skip_translation.add(NodeType.RIGHTDIV);
	skip_translation.add(NodeType.ELEMENTWISE_RIGHTDIV);
	return skip_translation;
    }

    public static ArrayList<NodeType> expressionNodes() {
	ArrayList<NodeType> skip_translation = new ArrayList<NodeType>(17);
	skip_translation.add(NodeType.PARENS);
	skip_translation.add(NodeType.PLUS);
	skip_translation.add(NodeType.MINUS);
	skip_translation.add(NodeType.TIMES);
	skip_translation.add(NodeType.ELEMENTWISE_TIMES);
	skip_translation.add(NodeType.LEFTDIV);
	skip_translation.add(NodeType.ELEMENTWISE_LEFTDIV);
	skip_translation.add(NodeType.RIGHTDIV);
	skip_translation.add(NodeType.ELEMENTWISE_RIGHTDIV);
	skip_translation.add(NodeType.EXP);
	skip_translation.add(NodeType.ELEMENTWISE_EXP);
	skip_translation.add(NodeType.DOUBLE_EQ);
	skip_translation.add(NodeType.GRT);
	skip_translation.add(NodeType.GRTE);
	skip_translation.add(NodeType.BIN_AND);
	skip_translation.add(NodeType.LOG_AND);
	skip_translation.add(NodeType.BIN_OR);
	skip_translation.add(NodeType.LOG_OR);
	skip_translation.add(NodeType.LST);
	skip_translation.add(NodeType.LSTE);
	skip_translation.add(NodeType.NEQ);
	return skip_translation;
    }

    public static ArrayList<NodeType> pointwiseExpressionNodes() {
	ArrayList<NodeType> skip_translation = new ArrayList<NodeType>(17);
	skip_translation.add(NodeType.PARENS);
	skip_translation.add(NodeType.PLUS);
	skip_translation.add(NodeType.ELEMENTWISE_TIMES);
	skip_translation.add(NodeType.ELEMENTWISE_LEFTDIV);
	skip_translation.add(NodeType.ELEMENTWISE_RIGHTDIV);
	skip_translation.add(NodeType.ELEMENTWISE_EXP);
	skip_translation.add(NodeType.DOUBLE_EQ);
	skip_translation.add(NodeType.GRT);
	skip_translation.add(NodeType.GRTE);
	skip_translation.add(NodeType.BIN_AND);
	skip_translation.add(NodeType.LOG_AND);
	skip_translation.add(NodeType.BIN_OR);
	skip_translation.add(NodeType.LOG_OR);
	skip_translation.add(NodeType.LST);
	skip_translation.add(NodeType.LSTE);
	skip_translation.add(NodeType.NEQ);
	return skip_translation;
    }

    public static List<NodeType> expressionLimitNodes() {
	ArrayList<NodeType> limit = new ArrayList<NodeType>(16);
	limit.add(NodeType.APPLY);
	limit.add(NodeType.VECTOR);
	limit.add(NodeType.ASSIGN);
	limit.add(NodeType.GASSIGN);
	limit.add(NodeType.EXPR_STMT);
	limit.add(NodeType.WHILE);
	limit.add(NodeType.FOR);
	limit.add(NodeType.PARFOR);
	limit.add(NodeType.ELSEIF);
	limit.add(NodeType.IF);
	limit.add(NodeType.CLEAR);
	limit.add(NodeType.CLOSE);
	limit.add(NodeType.CLEARVARS);
	limit.add(NodeType.HOLD);
	limit.add(NodeType.GRID);
	limit.add(NodeType.FORMAT);

	return limit;
    }

    /**
     * 
     * @param node
     * @return the limit for this pure-expression subtree (ex. 1 + 2 / 3 -> 2,
     *         f(1+2) / 3 -> 1)
     */
    public static int expressionTreeLimit(AASTNode node) {
	int limit = 0;
	if (node.parentExists(NodeType.APPLY))
	    limit = node.parentDepth(NodeType.APPLY);
	else if (node.parentExists(NodeType.VECTOR))
	    limit = node.parentDepth(NodeType.VECTOR);
	else if (node.parentExists(NodeType.ASSIGN))
	    limit = node.parentDepth(NodeType.ASSIGN);
	else if (node.parentExists(NodeType.GASSIGN))
	    limit = node.parentDepth(NodeType.GASSIGN);
	else if (node.parentExists(NodeType.EXPR_STMT))
	    limit = node.parentDepth(NodeType.EXPR_STMT);
	else if (node.parentExists(NodeType.WHILE))
	    limit = node.parentDepth(NodeType.WHILE);
	else if (node.parentExists(new NodeType[] { NodeType.FOR, NodeType.PARFOR }))
	    limit = node.parentDepth(new NodeType[] { NodeType.FOR, NodeType.PARFOR });
	else if (node.parentExists(NodeType.ELSEIF))
	    limit = node.parentDepth(NodeType.ELSEIF);
	else if (node.parentExists(NodeType.IF))
	    limit = node.parentDepth(NodeType.IF);
	else {
	    logger.warn("Unknown context for " + node);
	    limit = 2;
	}
	return limit;
    }

    /**
     * return lhs meaningful symbol for this rhs expression, or node itself if not
     * found.
     * 
     * @param node
     * @return
     * @throws UndefinedTranslationException
     */
    private static String lhsIdNode(AASTNode node) {
	List<NodeType> exprTypes = expressionNodes();

	int limit = expressionTreeLimit(node);
	boolean parentExprType = false;
	if (node.parentExists(exprTypes, limit)) {
	    // reduce further the limit if before the apply we have an expression
	    limit = node.parentDepth(exprTypes.toArray(new NodeType[exprTypes.size()]));
	    parentExprType = true;
	}

	if (node.parent().type().equals(NodeType.FIELDACCESS)) {
	    // in case of fieldaccess, just use node symbol
	    return node.symbol();
	} else if (node.parentExists(NodeType.RHS, limit) || node.type().equals(NodeType.COLON)) {
	    return doLhsIdNode(node, limit).symbol();
	} else if (node.type().equals(NodeType.APPLY) && !parentExprType) {
	    // apply at toplevel without expressions
	    return doLhsIdNode(node, limit).symbol();
	} else if (node.type().equals(NodeType.APPLY) && parentExprType) {
	    // apply not at toplevel with expressions -> generate intermediate symbol
	    if (!node.hasAttr(NodeAttr.SYMBOL))
		node.attr(NodeAttr.SYMBOL, Symbols.getSymbolFromType(node.child(NodeType.ID).name(), BType.MATRIX));
	    return node.symbol();
	} else if (exprTypes.contains(node.type()) || parentExprType) {
	    // if inside expression use child nodes
	    List<AASTNode> exprparts = node.childs();
	    if (exprparts.isEmpty())
		// id node. return itself
		return node.symbol();

	    AASTNode lhsNode = exprparts.get(0);
	    while (lhsNode.type().equals(NodeType.ID_NODE) || lhsNode.type().equals(NodeType.EXPRESSION)
		    || lhsNode.type().equals(NodeType.PARENS))
		lhsNode = lhsNode.childs().get(0);

	    if (exprparts.size() == 1)
		if (lhsNode.expr() != null && lhsNode.expr().isCastableToMatrix())
		    // unary operator is like a' or -a where a is a matrix.
		    if (node.type().equals(NodeType.TRANSPOSE) || node.type().equals(NodeType.ELEMENTWISE_CCT))
			return node.symbol(Symbols.getSymbolFromType(
				lhsNode.symbol().replaceAll("_vector_\\d+", "_transpose"), BType.MATRIX));
		    else
			// case -a or +a
			return lhsNode.symbol();
		else
		    return lhsNode.symbol();

	    // if more than 1
	    AASTNode rhsNode = exprparts.get(1);
	    String selSymbol = null;

	    // skip id_node/expression nodes
	    while (rhsNode.type().equals(NodeType.ID_NODE) || rhsNode.type().equals(NodeType.EXPRESSION)
		    || rhsNode.type().equals(NodeType.PARENS))
		rhsNode = rhsNode.childs().get(0);
	    GType lhs = lhsNode.expr();
	    GType rhs = rhsNode.expr();
	    if (lhs != null && lhs.isCastableToMatrix() && rhs != null && rhs.isCastableToMatrix()) {
		// if not dot product, take one of the two matrix (pointwise operation)
		// it's not important which one
		selSymbol = Symbols.getSymbolFromType(lhs.name(), BType.MATRIX);
	    } else if (lhs != null && lhs.isCastableToMatrix()) {
		selSymbol = Symbols.getSymbolFromType(lhs.name(), BType.MATRIX);
	    } else if (rhs != null && rhs.isCastableToMatrix()) {
		selSymbol = Symbols.getSymbolFromType(rhs.name(), BType.MATRIX);
	    } else
		return lhsNode.symbol();

	    return doLhsIdNode(node, limit).symbol(selSymbol);
	}
	return node.symbol();
    }

    private static AASTNode doLhsIdNode(AASTNode node, int limit) {
	AASTNode assignNode = null;
	if (node.parentExists(NodeType.ASSIGN, limit))
	    assignNode = node.parent(NodeType.ASSIGN);
	else if (node.parentExists(NodeType.GASSIGN, limit))
	    assignNode = node.parent(NodeType.GASSIGN);
	else
	    return node;

	AASTNode lhsNode = assignNode.child(NodeType.LHS);
	AASTNode idNode = lhsNode.childs().get(0);
	// skip id_node/expression nodes
	while (idNode.type().equals(NodeType.ID_NODE) || idNode.type().equals(NodeType.EXPRESSION))
	    idNode = idNode.childs().get(0);

	return idNode;
    }

    /**
     * plus [INT,STRING] , [INT] --> plus [INT,STRING] , [INT, UNKNOWN]
     * 
     * @param left
     * @param right
     * @return the number of expressions per node
     */
    public static int pairExpressionsForOperator(AASTNode left, AASTNode right) {
	List<GType> leftExprs = left.exprs();
	List<GType> rightExprs = right.exprs();
	// lower
	int l_types = -1;
	// upper
	int u_types = -1;
	// to be filled with unknown if left/right have different dimensions
	AASTNode toBeFilled = null;
	if (leftExprs.size() > rightExprs.size()) {
	    toBeFilled = right;
	    l_types = rightExprs.size();
	    u_types = leftExprs.size();
	} else {
	    toBeFilled = left;
	    l_types = leftExprs.size();
	    u_types = rightExprs.size();
	}

	// fill toBeFilled with missing unknown
	// for (int i = l_types; i < u_types; ++i)
	// toBeFilled.expr(GType.get(BType.UNKNOWN), false);
	return u_types;
    }

    public static List<GType> resultingTypeBoolOp(AASTNode op, AASTNode node) throws UndefinedTranslationException {
	List<GType> nodeExprs = node.exprs();
	Iterator<GType> nodeExprsIt = nodeExprs.iterator();

	List<GType> results = new LinkedList<GType>();
	while (nodeExprsIt.hasNext()) {
	    GType expr = nodeExprsIt.next();
	    if (expr.isCastableToMatrix()) {
		IntType[] dims = ((DimensionType) expr).dims();
		IntType[] newDims = new IntType[dims.length];
		for (int k = 0; k < dims.length; ++k)
		    newDims[k] = (IntType) GType.get(dims[k]);
		expr = GType.get(BType.MATRIX, op.symbol(), GType.get(BType.BOOL), newDims);
	    } else if (expr.isCastableToScalar())
		expr = GType.get(BType.BOOL, performBoolOperation(op, op.type(), (ValuedType) expr));
	    else
		// not matrix, not scalar
		expr = GType.get(BType.UNKNOWN);
	    results.add(expr);
	}
	return results;
    }

    public static List<GType> resultingTypeBoolOp(AASTNode op, AASTNode left, AASTNode right)
	    throws UndefinedTranslationException {
	// we can have multiple exprs per node
	List<GType> leftExprs = left.exprs();
	Iterator<GType> leftIter = leftExprs.iterator();
	List<GType> rightExprs = right.exprs();
	Iterator<GType> rightIter = rightExprs.iterator();

	int u_types = pairExpressionsForOperator(left, right);

	List<GType> results = new LinkedList<GType>();
	for (int i = 0; i < u_types; ++i) {
	    GType leftExpr = (!leftIter.hasNext()) ? ((LinkedList<GType>) leftExprs).peekLast() : leftIter.next();
	    GType rightExpr = (!rightIter.hasNext()) ? ((LinkedList<GType>) rightExprs).peekLast() : rightIter.next();

	    GType expr = null;
	    IntType[] dims = null;
	    IntType[] leftDims = null;
	    IntType[] rightDims = null;

	    if (leftExpr.isCastableToMatrix() || rightExpr.isCastableToMatrix()) {
		if (leftExpr.isCastableToMatrix() && rightExpr.isCastableToMatrix()) {
		    Tuple<IntType[], IntType[]> tp = TypeUtils.realDims(left, right);
		    IntType[] leftRealDims = tp.first();
		    IntType[] rightRealDims = tp.second();
		    DimensionType lmtype = (DimensionType) left.expr();
		    leftDims = lmtype.dims();
		    rightDims = ((DimensionType) right.expr()).dims();

		    if (leftRealDims.length != rightRealDims.length)
			if (TypeUtils.isDegeneratedMatrix(left.expr()))
			    dims = rightDims;
			else if (TypeUtils.isDegeneratedMatrix(right.expr()))
			    dims = leftDims;
			else
			    throw new UndefinedTranslationException(
				    ErrorMessage.UNSUPPORTED_BOOL_EXPR_BETWEEN_INCOMPATIBLE_MATRICES, op,
				    leftRealDims.length, rightRealDims.length);
		    else
			dims = leftDims;
		} else if (leftExpr.isCastableToMatrix()) {
		    DimensionType mtype = (DimensionType) left.expr();
		    dims = mtype.dims();
		} else if (rightExpr.isCastableToMatrix()) {
		    DimensionType mtype = (DimensionType) right.expr();
		    dims = ((DimensionType) right.expr()).dims();
		}
		// matrix of booleans with dims dimensions
		expr = GType.get(BType.MATRIX, "_tmp_", GType.get(BType.BOOL), dims);
	    } else if (leftExpr.isCastableToScalar() && rightExpr.isCastableToScalar())
		// both left and right scalars
		expr = GType.get(BType.BOOL,
			performBoolOperation(op, op.type(), (ValuedType) leftExpr, (ValuedType) rightExpr));
	    else {
		// not matrix, not scalar
		expr = GType.get(BType.UNKNOWN);
	    }
	    results.add(expr);
	}
	return results;
    }

    private static <T> Boolean performBoolOperation(AASTNode node, NodeType op, ValuedType<T> vnode)
	    throws UndefinedTranslationException {
	switch (op) {
	case NEG:
	    if (!vnode.hasBoolValue())
		return null;
	    else
		return !vnode.valueAsBool();
	default:
	    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_BOOL_EXPR, node, op);
	}
    }

    private static <T> Boolean performBoolOperation(AASTNode node, NodeType op, ValuedType<T> vleft,
	    ValuedType<T> vright) throws UndefinedTranslationException {
	switch (op) {
	case GRTE:
	case GRT:
	case LST:
	case LSTE:
	case DOUBLE_EQ:
	case NEQ:

	    if (!vleft.hasDoubleValue() || !vright.hasDoubleValue())
		return null;
	    break;
	case BIN_AND:
	case BIN_OR:
	case LOG_AND:
	case LOG_OR:
	    if (!vleft.hasBoolValue() || !vright.hasBoolValue())
		return null;
	    break;
	default:
	    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_BOOL_EXPR, node, op);
	}

	double tol = 10e-10;

	switch (op) {
	case GRTE:
	    return vleft.valueAsDouble() >= vright.valueAsDouble();
	case GRT:
	    return vleft.valueAsDouble() > vright.valueAsDouble();
	case LSTE:
	    return vleft.valueAsDouble() <= vright.valueAsDouble();
	case LST:
	    return vleft.valueAsDouble() < vright.valueAsDouble();
	case DOUBLE_EQ:
	    return Math.abs(vleft.valueAsDouble() - vright.valueAsDouble()) < tol;
	case NEQ:
	    return Math.abs(vleft.valueAsDouble() - vright.valueAsDouble()) >= tol;
	case LOG_AND:
	case BIN_AND:
	    return vleft.valueAsBool() && vright.valueAsBool();
	case LOG_OR:
	case BIN_OR:
	    return vleft.valueAsBool() || vright.valueAsBool();
	default:
	    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_BOOL_EXPR, node, op);
	}
    }

    public static GType updateScalarVectorType(GType vectorType, BType newtype) {
	if (vectorType == null)
	    // bool can be converted to whatever type
	    return GType.get(newtype);
	else if (vectorType.equals(BType.STRING)) {
	    // string cannot be casted to anything
	    return vectorType;
	} else if (vectorType.equals(BType.BOOL)) {
	    // bool type can be casted to anything (even string)
	    return GType.get(newtype);
	} else if (vectorType.isCastableToAccessIndex() && (newtype.equals(BType.BOOL)))
	    // int cannot be converted to bool
	    return vectorType;
	else if (vectorType.equals(BType.SCALAR) && (newtype.equals(BType.BOOL) || newtype.equals(BType.INT)))
	    // scalar cannot be converted to bool nor to int
	    return vectorType;
	else
	    return GType.get(newtype);

    }

    public static GType resultingTypeMatrixMatrixOp(AASTNode node, DimensionType lexpr, DimensionType rexpr)
	    throws UndefinedTranslationException, TypeException {
	IntType[] matrixLeftDims = lexpr.dims();
	IntType[] matrixRightDims = rexpr.dims();

	GType expr = null;
	BType rexprtype = null;
	boolean outSparse = false;
	if(lexpr.equals(BType.MATRIX_ACCESS_SLICE) && rexpr.equals(BType.MATRIX_ACCESS_SLICE)) {
		throw new TypeException(ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES,
				node, lexpr, rexpr);
	}else if (matrixLeftDims.length == 1 && matrixRightDims.length == 1) {
	    // and thanks to prev checks also right
	    // has same dimension
	    if (node.type().equals(NodeType.TIMES))
		// multiplication of vectors -> scalar
		expr = updateScalarVectorType(lexpr.of(), rexpr.of().type());
	    else
		throw new TypeException(ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES,
			node, lexpr, rexpr);

	} else if (matrixLeftDims.length == 2 && matrixRightDims.length == 2) {
	    IntType ldim = (IntType) GType.get(matrixLeftDims[0]);
	    IntType lrdim = (IntType) GType.get(matrixLeftDims[1]);
	    IntType rldim = (IntType) GType.get(matrixRightDims[0]);
	    IntType rdim = (IntType) GType.get(matrixRightDims[1]);
	    // mxn times nxk -> mxk
	    if (node.type().equals(NodeType.TIMES)  
	    	&& ldim.hasValue() && ldim.valueAsInt() == 1 && rdim.hasValue()
		    && rdim.valueAsInt() == 1)
		// 2d but in fact they are 1D (mx1 or 1xn)
		expr = updateScalarVectorType(lexpr.of(), rexpr.of().type());
	    else {
		IntType[] dims = null;
		rexprtype = rexpr.of().type();
		if (node.type().equals(NodeType.TIMES)) {
		    // matrix multiplication
		    dims = new IntType[] { (IntType) GType.get(ldim), (IntType) GType.get(rdim) };
		} else if (node.type().equals(NodeType.RIGHTDIV))
		    // solve linear system lexpr = A \ rexpr = b
		    // the result is the dimension of b
		    if ((!ldim.hasValue() || ldim.valueAsInt() > 1) && (!lrdim.hasValue() || lrdim.valueAsInt() > 1)) {
			dims = new IntType[] { (IntType) GType.get(rldim), (IntType) GType.get(rdim) };
			rexprtype = BType.SCALAR;
		    } else
			// A is a vector! raise error
			throw new TypeException(
				ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node,
				lexpr, rexpr);

		else if (node.type().equals(NodeType.LEFTDIV))
		    // solve linear system lexpr = b / rexpr = A
		    // the result is the dimension of b
		    if ((!rldim.hasValue() || rldim.valueAsInt() > 1) && (!rdim.hasValue() || rdim.valueAsInt() > 1)) {
			dims = new IntType[] { (IntType) GType.get(ldim), (IntType) GType.get(lrdim) };
			rexprtype = BType.SCALAR;
		    } else if ((ldim.hasValue() && ldim.valueAsInt() == 1)
			    || (lrdim.hasValue() && lrdim.valueAsInt() == 1)) {
			// if also left expression is vector this is a pointwise operation like ./
			return resultingTypeMatrixMatrixPointwiseOp(node, (DimensionType) lexpr, (DimensionType) rexpr);
		    } else
			// A is a vector! raise error
			throw new TypeException(
				ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node,
				lexpr, rexpr);
		else
		    // if sum, substract, out sparse if both sparse
		    outSparse = lexpr.equals(BType.MATRIX) && ((MatrixType) lexpr).isSparse()
			    && rexpr.equals(BType.MATRIX) && ((MatrixType) rexpr).isSparse();

		String exprname;
		if (node.hasAttr(NodeAttr.SYMBOL))
		    exprname = TypeUtils.matrixName(node);
		else
		    exprname = Symbols.getSymbolFromType("", BType.MATRIX);
		expr = GType.get(BType.MATRIX, exprname, updateScalarVectorType(lexpr.of(), rexprtype), dims,
			outSparse);
	    }
	} else if (matrixLeftDims.length == 2 && matrixRightDims.length == 1) {
	    IntType ldim = (IntType) GType.get(matrixLeftDims[0]);
	    IntType lrdim = (IntType) GType.get(matrixLeftDims[1]);
	    IntType rldim = (IntType) GType.get(matrixRightDims[0]);
	    IntType rdim = (IntType) GType.get(BType.INT, 1);
	    IntType[] dims = null;
	    // the only possible case here is A\b
	    if ((ldim.hasValue() && ldim.valueAsInt() == 1) || (lrdim.hasValue() && lrdim.valueAsInt() == 1)) {
		// if also left expression is vector this is a pointwise operation like ./
		return resultingTypeMatrixMatrixPointwiseOp(node, (DimensionType) lexpr, (DimensionType) rexpr);
	    } else if (node.type().equals(NodeType.TIMES) && ldim.hasValue() && ldim.valueAsInt() == 1)
		expr = updateScalarVectorType(lexpr.of(), rexpr.of().type());
	    else if (node.type().equals(NodeType.TIMES)) {
		rexprtype = rexpr.of().type();
		if (node.type().equals(NodeType.TIMES))
		    // matrix multiplication
		    dims = new IntType[] { (IntType) GType.get(ldim), (IntType) GType.get(rdim) };
	    } else if (node.type().equals(NodeType.RIGHTDIV)) {
		// solve linear system lexpr = A \ rexpr = b
		// the result is the dimension of b
		if ((!ldim.hasValue() || ldim.valueAsInt() > 1) && (!lrdim.hasValue() || lrdim.valueAsInt() > 1)) {
		    dims = new IntType[] { (IntType) GType.get(rldim), (IntType) GType.get(rdim) };
		    rexprtype = BType.SCALAR;
		} else
		    // A is a vector! raise error
		    throw new TypeException(
			    ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node, lexpr,
			    rexpr);

	    } else
		throw new UndefinedTranslationException(
			ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node, lexpr,
			rexpr);

	    String exprname;
	    if (node.hasAttr(NodeAttr.SYMBOL))
		exprname = node.symbol();
	    else
		exprname = Symbols.getSymbolFromType("", BType.MATRIX);

	    expr = GType.get(BType.MATRIX, exprname, updateScalarVectorType(lexpr.of(), rexprtype), dims, outSparse);
	} else if (matrixLeftDims.length == 1 && matrixRightDims.length == 2) {
	    IntType ldim = (IntType) GType.get(matrixLeftDims[0]);
	    IntType lrdim = (IntType) GType.get(BType.INT, 1);
	    IntType rldim = (IntType) GType.get(matrixRightDims[0]);
	    IntType rdim = (IntType) GType.get(matrixRightDims[1]);

	    if ((rldim.hasValue() && rldim.valueAsInt() == 1) || (rdim.hasValue() && rdim.valueAsInt() == 1))
		// if also left expression is vector this is a pointwise operation like ./
		return resultingTypeMatrixMatrixPointwiseOp(node, (DimensionType) lexpr, (DimensionType) rexpr);
	    else
		throw new UndefinedTranslationException(
			ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node, lexpr,
			rexpr);
	} else
	    throw new UndefinedTranslationException(
		    ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, node, lexpr, rexpr);

	return expr;
    }

    public static Triple<GType, GType, GType> resultingTypeSliceOp(Triple<GType, GType, GType> slice)
	    throws UndefinedTranslationException {

	GType start = slice.first();
	GType step = slice.second();
	GType end = slice.third();

	Tuple<GType, GType> tp;
	if (!start.canRepresent(step) || !step.canRepresent(start)) {
	    tp = temporaryCast(start, step);
	    start = tp.first();
	    step = tp.second();
	}
	if (!step.canRepresent(end) || !end.canRepresent(step)) {
	    tp = temporaryCast(end, step);
	    step = tp.second();
	    end = tp.first();
	}
	// redo
	if (!start.canRepresent(step) || !step.canRepresent(start)) {
	    tp = temporaryCast(start, step);
	    start = tp.first();
	    step = tp.second();
	}
	if (!step.canRepresent(end) || !end.canRepresent(step)) {
	    tp = temporaryCast(end, step);
	    step = tp.second();
	    end = tp.first();
	}

	return new Triple<GType, GType, GType>(start, step, end);
    }

    private static Tuple<GType, GType> temporaryCast(GType start, GType step) throws UndefinedTranslationException {
	if (start.equals(BType.UNKNOWN))
	    step = GType.get(BType.UNKNOWN);
	else if (step.equals(BType.UNKNOWN))
	    start = GType.get(BType.UNKNOWN);
	else if (start.equals(BType.INT) && step.equals(BType.SCALAR))
	    start = GType.get(BType.SCALAR,
		    (((IntType) start).hasValue()) ? ((IntType) start).valueAsInt().doubleValue() : null);
	else if (step.equals(BType.INT) && start.equals(BType.SCALAR))
	    step = GType.get(BType.SCALAR,
		    (((IntType) step).hasValue()) ? ((IntType) step).valueAsInt().doubleValue() : null);
	else if (TypeUtils.isDegeneratedMatrix(start)) {
	    // start is a matrix but it contains just one element, we can think of it like a
	    // scalar/int
	    DimensionType dtype = (DimensionType) start;
	    if (step.equals(BType.INT) && dtype.of().equals(BType.SCALAR))
		step = GType.get(BType.SCALAR,
			(((IntType) step).hasValue()) ? ((IntType) step).valueAsInt().doubleValue() : null);
	    else if (step.equals(BType.SCALAR) && dtype.of().equals(BType.INT))
		// temporary cast it to matrix of scalar
		start = GType.get(dtype.type(), dtype.name(), GType.get(BType.SCALAR), dtype.dims());

	} else if (TypeUtils.isDegeneratedMatrix(step)) {
	    // step is a matrix but it contains just one element, we can think of it like a
	    // scalar/int
	    DimensionType dtype = (DimensionType) step;
	    if (start.equals(BType.INT) && dtype.of().equals(BType.SCALAR))
		start = GType.get(BType.SCALAR,
			(((IntType) step).hasValue()) ? ((IntType) step).valueAsInt().doubleValue() : null);
	    else if (start.equals(BType.SCALAR) && dtype.of().equals(BType.INT))
		// temporary cast it to matrix of scalar
		step = GType.get(dtype.type(), dtype.name(), GType.get(BType.SCALAR), dtype.dims());

	} else
	    logger.debug("Cannot translate a slice for a matrix with slice values of different types: '" + start + "' '"
		    + step + "'");
	return new Tuple<GType, GType>(start, step);
    }

    public static Tuple<IntType[], IntType[]> realDims(AASTNode left, AASTNode right) {
	IntType[] leftDims = ((DimensionType) left.expr()).dims();
	IntType[] rightDims = ((DimensionType) right.expr()).dims();
	return new Tuple<IntType[], IntType[]>(realDims(leftDims), realDims(rightDims));
    }

    public static IntType[] realDims(AASTNode node, int reduce_to) {
	return realDims(((DimensionType) node.expr()).dims(), reduce_to);
    }

    public static IntType[] realDims(List<IntType> leftDims) {
	return realDims(leftDims.toArray(new IntType[leftDims.size()]));
    }

    public static boolean isScalarMatrix(IntType[] dims) {
	IntType[] realdims = realDims(dims);
	if (realdims.length == 1 && realdims[0].hasIntValue() && realdims[0].valueAsInt() == 1) {
	    return true;
	} else {
	    return false;
	}
    }

    public static Tuple<IntType[], List<Integer>> realDimsWithPos(IntType[] leftDims) {
	List<IntType> leftRealDims = new ArrayList<IntType>();
	List<Integer> nonones = new ArrayList<Integer>();
	if (leftDims != null && leftDims.length > 0)
	    if (leftDims.length == 1 || allOnes(leftDims)) {
		leftRealDims.add(leftDims[0]);
		nonones.add(0);
	    } else
		for (int lpos = 0; lpos < leftDims.length; ++lpos) {
		    IntType ldim = leftDims[lpos];
		    if (!ldim.hasValue() || ldim.hasValue() && ldim.valueAsInt() > 1) {
			leftRealDims.add(ldim);
			nonones.add(lpos);
		    }
		}
	return new Tuple<IntType[], List<Integer>>(leftRealDims.toArray(new IntType[leftRealDims.size()]), nonones);
    }

    public static IntType[] realDims(IntType[] leftDims) {
	return realDims(leftDims, 0);
    }

    public static IntType[] realDims(IntType[] leftDims, int reduce_to) {
	List<IntType> leftRealDims = new ArrayList<IntType>();
	if (leftDims != null && leftDims.length > 0)
	    if (leftDims.length == 1 || allOnes(leftDims))
		leftRealDims.add(leftDims[0]);
	    else {
		for (IntType ldim : leftDims)
		    if (!ldim.hasValue() || ldim.hasValue() && ldim.valueAsInt() > 1)
			leftRealDims.add(ldim);
		if (reduce_to > 0 && leftRealDims.size() < reduce_to) {
		    // we removed too much, add missing ones
		    for (int i = 0; i < leftDims.length; ++i)
			if (leftRealDims.size() >= reduce_to)
			    break;
			else if (leftDims[i].hasValue() && leftDims[i].valueAsInt() == 1)
			    leftRealDims.add(i, leftDims[i]);
		}
	    }
	return leftRealDims.toArray(new IntType[leftRealDims.size()]);
    }

    public static Tuple<IntType, Integer> firstNonEmptyDim(IntType[] leftDims, int startFrom) {
	if (leftDims == null)
	    return null;
	else if (leftDims.length == 1 || allOnes(leftDims))
	    return new Tuple<IntType, Integer>(leftDims[0], 0);
	else
	    for (int i = startFrom; i < leftDims.length; ++i) {
		IntType ldim = leftDims[i];
		if (!ldim.hasValue() || ldim.hasValue() && ldim.valueAsInt() > 1)
		    return new Tuple<IntType, Integer>(ldim, i);
	    }
	// return last one
	return new Tuple<IntType, Integer>(leftDims[leftDims.length - 1], leftDims.length - 1);
    }

    public static boolean allOnes(IntType[] leftDims) {
	for (IntType dim : leftDims)
	    if (!dim.hasIntValue() || dim.valueAsInt() > 1)
		return false;
	return true;
    }

    public static GType resultingTypeScalarOp(GType lexpr, GType rexpr, AASTNode node)
	    throws UndefinedTranslationException {
	GType etype = null;
	// if at least one is scalar, convert to scalar
	if (lexpr.equals(BType.SCALAR) || rexpr.equals(BType.SCALAR)) {
	    etype = (ValuedType) GType.get(BType.SCALAR);
	} else {
	    IntType vlexpr = (IntType) lexpr;
	    IntType vrexpr = (IntType) rexpr;
	    switch (node.type()) {
	    case PLUS:
	    case MINUS:
	    case EXP:
	    case ELEMENTWISE_EXP:
	    case ELEMENTWISE_TIMES:
	    case TIMES:
		// if types are int or bool, keep int (BoolType extends IntType)
		if (vlexpr.hasValue() && vrexpr.hasValue())
		    switch (node.type()) {
		    case PLUS:
			etype = GType.get(BType.INT, vlexpr.valueAsInt() + vrexpr.valueAsInt());
			break;
		    case MINUS:
			etype = GType.get(BType.INT, vlexpr.valueAsInt() - vrexpr.valueAsInt());
			break;
		    case EXP:
		    case ELEMENTWISE_EXP:
			if (vrexpr.hasValue() && vrexpr.valueAsInt() < 0)
			    // case (10^-5) both are int, but result is scalar
			    etype = GType.get(BType.SCALAR, Math.pow(vlexpr.valueAsInt(), vrexpr.valueAsInt()));
			else
			    etype = GType.get(BType.INT, (int) Math.pow(vlexpr.valueAsInt(), vrexpr.valueAsInt()));
			break;
		    case TIMES:
		    case ELEMENTWISE_TIMES:
			etype = GType.get(BType.INT, vlexpr.valueAsInt() * vrexpr.valueAsInt());
			break;
		    default:
			throw new UndefinedTranslationException(ErrorMessage.INTERNAL_SCALAR_OP_RESULT_TYPE, node,
				lexpr, rexpr);
		    }
		else
		    etype = GType.get(BType.INT);
		break;
	    case LEFTDIV:
	    case ELEMENTWISE_LEFTDIV:
		// if types are int or bool, keep int (BoolType extends IntType)
		if (vlexpr.hasValue() && vrexpr.hasValue())
		    etype = GType.get(BType.SCALAR, vlexpr.valueAsInt() / (double) vrexpr.valueAsInt());
		else
		    etype = GType.get(BType.SCALAR);
		break;
	    case RIGHTDIV:
	    case ELEMENTWISE_RIGHTDIV:
		// if types are int or bool, keep int (BoolType extends IntType)
		if (vlexpr.hasValue() && vrexpr.hasValue())
		    etype = GType.get(BType.SCALAR, vrexpr.valueAsInt() / (double) vlexpr.valueAsInt());
		else
		    etype = GType.get(BType.SCALAR);
		break;
	    default:
		throw new UndefinedTranslationException(ErrorMessage.INTERNAL_SCALAR_OP_RESULT_TYPE, node, lexpr,
			rexpr);
	    }
	}
	return etype;
    }

    public static IntType[] mostGeneralDimension(IntType[] dimsLeft, IntType[] dimsRight) {
	if ((dimsLeft == null || dimsLeft.length == 0) && (dimsRight == null || dimsRight.length == 0))
	    return null;
	else if (dimsLeft == null || dimsLeft.length == 0)
	    return dimsRight;
	else if (dimsRight == null || dimsRight.length == 0)
	    return dimsLeft;

	if (dimsLeft.length > dimsRight.length)
	    return mostGeneralDim(dimsLeft, dimsRight);
	else
	    return mostGeneralDim(dimsRight, dimsLeft);
    }

    private static IntType[] mostGeneralDim(IntType[] higherD, IntType[] lowerD) {
	// if higher dimensional have all non-1 dimension
	Tuple<IntType[], List<Integer>> tp1 = realDimsWithPos(higherD);
	IntType[] hNonTrivialDim = tp1.first();
	List<Integer> hNonOnesDims = tp1.second();
	IntType[] lNonTrivialDim = realDims(lowerD);
	// all nontrivial
	if (hNonTrivialDim.length < lNonTrivialDim.length)
	    return lowerD;
	else if (hNonTrivialDim.length > lNonTrivialDim.length)
	    return higherD;
	// if they are equal in dimension compare values
	List<IntType> res = new LinkedList<IntType>();

	for (int i = 0; i < hNonTrivialDim.length; ++i)
	    if (hNonTrivialDim[i].hasValue() && lNonTrivialDim[i].hasValue())
		res.add((hNonTrivialDim[i].valueAsInt() > lNonTrivialDim[i].valueAsInt())
			? (IntType) GType.get(hNonTrivialDim[i])
			: (IntType) GType.get(lNonTrivialDim[i]));
	    // null means we should add a general dimension
	    else
		res.add((IntType) GType.get(hNonTrivialDim[i]));

	if (higherD.length > hNonTrivialDim.length)
	    // add missing ones, to preserve matrix orientation
	    for (int i = 0; i < higherD.length; ++i)
		if (!hNonOnesDims.contains(i))
		    res.add(i, (IntType) GType.get(higherD[i]));

	return res.toArray(new IntType[res.size()]);
    }

    /**
     * update type name
     * 
     * @param gType the type that will be updated
     * @param name
     * @return
     */
    public static GType updateNameType(GType gType, AASTNode lhsNode) {
	GType newType = GType.get(gType);
	switch (newType.type()) {
	case MATRIX:
	case MATRIX_ACCESS_SLICE:
	    DimensionType dtype = (DimensionType) newType;
	    dtype.name(lhsNode.fullname());
	    if (dtype != null && dtype.dims() != null)
		for (int i = 0; i < dtype.dims().length; ++i) {
		    if (!dtype.dims()[i].hasValue() || dtype.dims()[i].hasName())
			if (newType.equals(BType.MATRIX_ACCESS_SLICE) && i > 0)
			    // slices do have only 1 named dimension
			    dtype.dims()[i].name(TypeUtils.matrixDimName(lhsNode.fullname(), 1));
			else
			    dtype.dims()[i].name(TypeUtils.matrixDimName(lhsNode.fullname(), i + 1));
		}
	    return dtype;
	default:
	    return newType;
	}
    }

    /**
     * helper method to check for type consistency of parameters and unzip them from
     * a single list to three distinct lists: list of types, list of dimensions,
     * list of values
     * 
     * @param params
     * @param coreFun: the core function to check the formal parameters against
     * @return a triple: list of types, list of dimensions, list of values
     * @throws TypeException
     * @throws UnboundException
     * @throws SyntaxException
     */
    private static Tuple<List<GType>, List<GType>> checkAndUpdateFunctionParameters(AASTNode applyNode,
	    AASTNode funNode, List<AASTNode> actualParams) throws TypeException, UnboundException, SyntaxException {
	AAST aast = applyNode.compilationUnit();
	List<AASTNode> formalParams = new ArrayList<AASTNode>();
	AASTNode stmtListNode = funNode.child(NodeType.STATEMENT_LIST);
	AASTNode paramListNode = funNode.child(NodeType.PARAMETER_LIST);
	List<AASTNode> paramNodes = paramListNode.childs();
	List<AASTNode> outSymbols = funNode.childs(NodeType.FUNCTION_RETURN);
	IFunction coreFun = null;
	BiFunction<AASTNode, List<GType>, List<GType>> paramTypeUpdater = null;
	List<GType> formalParamTypes = null;
	Iterator<GType> formalParamTypesIt = null;
	List<GType> actualParamTypes = new ArrayList<GType>(actualParams.size());
	GType curParam;
	GType undefined = GType.get(BType.UNDEFINED);
	for (AASTNode actualParam : actualParams) {
	    curParam = actualParam.expr();
	    if (undefined.equals(curParam))
		// error
		throw new TypeException(ErrorMessage.FUN_PARAMETER_UNDEFINED, applyNode, actualParam.name(),
			funNode.child(NodeType.ID).name());
	    actualParamTypes.add(curParam);
	}
	// case core function
	if (funNode.coreFunction() != null) {
	    // reset to actual parameter type for core functions
	    coreFun = funNode.coreFunction();
	    paramTypeUpdater = coreFun.paramTypesUpdater();
	    // get formal parameters based on actual parameters
	    if (paramTypeUpdater != null) {
		formalParamTypes = paramTypeUpdater.apply(applyNode, actualParamTypes);
		if (formalParamTypes.size() == 1 && formalParamTypes.get(0).equals(BType.UNDEFINED)) {
		    throw new TypeException(ErrorMessage.UNSUPPORTED_CORE_FUNCTION_TYPE_UPDATER, applyNode,
			    applyNode.name(), actualParamTypes, applyNode.child(NodeType.ID).name(),
			    formalParamTypes.get(0).name());
		}
		if (formalParamTypes != null)
		    formalParamTypesIt = formalParamTypes.iterator();
	    }
	    // add missing param nodes
	    for (int i = 0; i < actualParams.size(); ++i) {
		AASTNode formalParam;
		boolean oldParamNode = i < paramNodes.size();
		if (oldParamNode)
		    formalParam = paramNodes.get(i);
		else
		    // case only 1 formal param of type unknown. create
		    // extra
		    // formal params on the fly
		    formalParam = new AASTNode(aast, NodeType.ID, "p" + i, 0, 0, paramListNode);

		if (formalParamTypesIt != null) {
		    GType formalParamType = formalParamTypesIt.next();
		    if (formalParamType.equals(BType.UNDEFINED))
			applyNode.compilationUnit()
				.addWarning(new TypeException(ErrorMessage.UNSUPPORTED_CORE_FUNCTION_TYPE_UPDATER,
					applyNode, applyNode.name(), actualParamTypes,
					applyNode.child(NodeType.ID).name(),
					"Error was encountered on parameter '" + formalParam.name() + "'."));
		    List<GType> l = new LinkedList<GType>();
		    l.add(formalParamType);
		    formalParam.exprs(l, false);
		} else {
		    List<GType> l = new LinkedList<GType>();
		    l.add(((oldParamNode) ? coreFun.getParamTypes().get(i) : GType.get(BType.UNKNOWN)));
		    formalParam.exprs(l, false);
		}
		formalParams.add(formalParam);
	    }
	} else
	    // case user-defined function
	    for (int i = 0; i < paramNodes.size(); ++i) {
		AASTNode formalParam = paramNodes.get(i);
		if (!formalParam.type().equals(NodeType.VOID))
		    formalParams.add(formalParam);
	    }

	if (actualParams.size() > formalParams.size()) {
	    TypeException ex = new TypeException(ErrorMessage.FUN_FORMAL_PARAMS_DONT_MATCH, applyNode,
		    funNode.child(NodeType.ID).name(), actualParams, formalParams);
	    String message = ex.stringify();
	    applyNode.error(new TypeError(message, Thread.currentThread().getStackTrace()));
	    logger.debug(message, ex);
	    throw ex;
	}
	boolean only_named = false;
	// the last index among the possible input parameter types, that match this
	// call. needed to choose the right return type in case of function with
	// multiple return types
	int ptype_idx = -1;
	// the complete list of ptype_idx
	List<Integer> ptype_idxs = new ArrayList<Integer>();
	// true if the function parameter types can have multiple types
	boolean polymorphicFunction = false;
	// search for a function handle in the parameters...
	for (int i = 0; i < actualParams.size(); ++i) {
	    AASTNode actualParam = actualParams.get(i);
	    Tuple<AASTNode, Boolean> tp = matchFormalParam(applyNode, funNode, actualParam, i, formalParams,
		    only_named);
	    AASTNode formalParam = tp.first();
	    only_named |= tp.second();

	    // type resolution
	    if (unknownFunction(actualParam.expr())
		    && (formalParam.expr() != null && formalParam.expr().equals(BType.FUNCTION))) {
		// copy function definition
		List<GType> actualParamExprs = new LinkedList<GType>();
		Iterator<GType> it = formalParam.exprs().iterator();
		while (it.hasNext())
		    actualParamExprs.add(GType.get(it.next()));
		actualParam.exprs(actualParamExprs, false);
	    } else if ((formalParam.expr() == null || formalParam.expr().equals(BType.UNKNOWN))
		    && (actualParam.expr() != null && !actualParam.expr().equals(BType.UNKNOWN))) {
		GType newType = TypeUtils.updateNameType(actualParam.expr(), formalParam);
		formalParam.expr(newType);

		// formalParametersUpdated = true;
	    } else if (formalParam.expr() == null)
		formalParam.expr(GType.get(BType.UNKNOWN));

	    // both KNOWN
	    // actual param type
	    GType actualptype = actualParam.expr();
	    if (actualptype != null
	    	// if core function, it's types doesn't need to be resolved
		    && !actualParam.hasAttr(NodeAttr.REF_CORE_FUNCTION) 
		    && (actualptype.equals(BType.FUNCTION) || actualParam.hasAttr(NodeAttr.REF_FUNCTION))) {
		if (   !actualParam.type().equals(NodeType.FUNCTION) && !actualParam.hasAttr(NodeAttr.REF_FUNCTION)) {
		    TypeException ex = new TypeException(ErrorMessage.INTERNAL_FUN_FUNCTIONAL_PARAM_DONT_MATCH,
			    applyNode, funNode.child(NodeType.ID).name(), actualParam);
		    String msg = ex.stringify();
		    actualParam.error(new TypeError(msg, Thread.currentThread().getStackTrace()));
		    throw ex;
		}
		// infer parameter types of this functional parameter
		// from the firm of the function that is using it
		// infer this parameter types
		AASTNode actualFunNode = actualParam;
		FunctionType formalFunType = (FunctionType) formalParam.expr();
		FunctionType actualFunType = (FunctionType) actualFunNode.expr();
		if (actualFunNode.hasAttr(NodeAttr.REF_FUNCTION)) {
		    List<GType> types = actualFunNode.exprs();
		    actualFunNode = (AASTNode) actualFunNode.attr(NodeAttr.REF_FUNCTION);
		    // propagate type
		    actualFunNode.exprs(types, false);
		}
		
		List<AASTNode> returnChilds = actualFunNode.childs(NodeType.FUNCTION_RETURN);
		if (returnChilds.size() != actualFunType.outputs().size()) {
		    TypeException ex = new TypeException(ErrorMessage.FUN_FUNCTIONAL_PARAM_N_RETURN_VALUES_DONT_MATCH,
			    applyNode, funNode.child(NodeType.ID).name(), actualFunNode.child(NodeType.ID).name(),
			    returnChilds.size(), actualFunType.outputs().size());
		    String message = ex.stringify();
		    actualParam.error(new TypeError(message, Thread.currentThread().getStackTrace()));
		    throw ex;
		}
		// propagate types to return nodes
		Iterator<GType> it = actualFunType.outputs().iterator();
		for (int j = 0; j < actualFunType.outputs().size(); j++) {
		    GType outputType = it.next();
		    GType rtype = returnChilds.get(j).expr();
		    if (rtype.equals(BType.UNKNOWN) || !rtype.castableTo(outputType)) {
			// add type only if previous type unknown or not
			// castable to the new one (in this case it will be
			// added)
			GType newType = TypeUtils.updateNameType(outputType, returnChilds.get(j));
			returnChilds.get(j).expr(newType, false);
		    }
		}
		List<AASTNode> actualParamChilds = actualFunNode.childs(NodeType.PARAMETER_LIST);
		List<String> actualFunParamsWithEnv = (List<String>) actualFunNode.attr(NodeAttr.FUNCTION_PARAMS);
		List<AASTNode> actualFunEnvParamNodes = (List<AASTNode>) actualFunNode
			.attr(NodeAttr.FUNCTION_ENV_PARAMS);
		List<String> actualFunEnvParams;
		if (actualFunEnvParamNodes != null) {
		    actualFunEnvParams = new ArrayList<>(actualFunEnvParamNodes.size());
		    for (AASTNode p : actualFunEnvParamNodes)
			actualFunEnvParams.add(p.name());
		} else
		    actualFunEnvParams = new ArrayList<>(0);
		List<String> actualFunParams = new ArrayList<>(
			actualFunParamsWithEnv.size() - actualFunEnvParams.size());
		// store only explicit parameters, excluding environment ones
		for (String p : actualFunParamsWithEnv)
		    if (!actualFunEnvParams.contains(p))
			actualFunParams.add(p);

		if (formalFunType.inputs().size() != actualFunParams.size()) {
		    TypeException ex = new TypeException(ErrorMessage.FUN_FUNCTIONAL_PARAM_N_PARAM_VALUES_DONT_MATCH,
			    applyNode, funNode.child(NodeType.ID).name(), actualFunNode.child(NodeType.ID).name(),
			    actualFunParams.size(), formalFunType.inputs().size());
		    String message = ex.stringify();
		    actualParam.error(new TypeError(message, Thread.currentThread().getStackTrace()));
		    throw ex;
		}
		// propagate types to param nodes
		it = actualFunType.inputs().iterator();
		for (int j = 0; j < actualFunType.inputs().size(); j++) {
		    GType inputType = it.next();
		    GType ptype = actualParamChilds.get(j).expr();
		    if (ptype.equals(BType.UNKNOWN) || !ptype.castableTo(inputType)) {
			// add type only if previous type unknown or not
			// castable to the new one (in this case it will be
			// added)
			GType newType = TypeUtils.updateNameType(inputType, actualParamChilds.get(j));
			actualParamChilds.get(j).expr(newType, false);
		    }
		}
		// mark this function to be parsed again in a future compilation
		// unit walk
		actualFunNode.attr(NodeAttr.FUNCTION_INPUTS_RESOLVED, true);
	    }

	    GType formalptype = null;
	    // apply this only to user-defined functions
	    if (funNode.coreFunction() == null) {
		// select apply node return type based on actual type
		if (ptype_idx == -2 || actualptype == null || actualptype.equals(BType.UNKNOWN)
			|| actualptype.equals(BType.UNDEFINED))
		    ptype_idx = -2; // can't be determined
		else {
		    polymorphicFunction |= formalParam.exprs().size() > 1;
		    boolean done = false;

		    Iterator<GType> it = formalParam.exprs().iterator();
		    for (int k = 0; !done && k < formalParam.exprs().size(); ++k) {
			GType fptype = it.next();
			if (fptype.deeplyEqual(actualptype)) {
			    formalptype = fptype;
			    ptype_idx = k;
			    done = true;
			} else if (fptype.lessGenericThan(actualptype)) {
			    // case fptype=INT actualptype=SCALAR
			    // take new
			    formalptype = TypeUtils.updateNameType(actualptype, formalParam);
			    if (actualptype instanceof ValuedType && !fptype.equalValue(actualptype))
				((ValuedType<?>) formalptype).deleteValue();
			    // update ids
			    formalParam.expr(formalptype);
			    ptype_idx = k;
			    done = true;
			} else if (fptype.canBeForcedToRepresent(actualptype)) {
			    // case fptype=SCALAR actualptype=INT
			    // keep old
			    formalptype = TypeUtils.updateNameType(fptype, formalParam);
			    if (fptype.hasValue() && !fptype.equalValue(actualptype))
				((ValuedType<?>) formalptype).deleteValue();
			    // update ids
			    formalParam.expr(formalptype);
			    ptype_idx = k;
			    done = true;
			} else if (!done && k == formalParam.exprs().size() - 1) {
			    // if last loop and still we haven't found any match for this param
			    if (fptype.equals(BType.STRUCT) && actualptype.equals(BType.STRUCT)) {
				// function call with two different structs -> join structs
				StructType formalStruct = ((StructType) fptype);
				StructType actualStruct = ((StructType) actualptype);
				if (actualStruct.isSubSetOf(formalStruct)) {
				    // use formal struct, update actual param
				    actualParam.expr(GType.get(formalStruct).setInput(actualptype.inputName()), true);
				} else if (formalStruct.isSubSetOf(actualStruct)) {
				    // use actual Struct, update formal param
				    formalParam.expr(GType.get(actualStruct).setInput(fptype.inputName()), true);
				} else {
				    // create a new struct
				    StructType newStruct = TypeUtils.joinStructs(formalStruct, actualStruct);
				    // delete & substitute original formal and actual structs
				    actualParam.expr(newStruct.setInput(actualptype.inputName()), true);
				    formalParam.expr(GType.get(newStruct).setInput(fptype.inputName()), true);
				}
			    } else if (fptype.equals(BType.STRUCT)) {
				StructType formalStruct = ((StructType) fptype);
				// use formal struct, update actual param
				actualParam.expr(GType.get(formalStruct).setInput(fptype.inputName()), true);
			    } else if (actualptype.equals(BType.STRUCT)) {
				StructType actualStruct = ((StructType) actualptype);
				// use actual Struct, update formal param
				formalParam.expr(GType.get(actualStruct).setInput(actualptype.inputName()), true);
			    } else {
				throw new TypeException(ErrorMessage.WARN_FUN_FORMAL_PARAM_DONT_MATCH, applyNode,
					funNode.child(NodeType.ID).name(), actualParam.name(), actualptype,
					formalParam.name(), fptype);
			    }
			}
		    }
		}

		// propagate function call return value to this node
		if (formalptype == null && ptype_idx != -2) {
		    logger.debug("Function '" + funNode.child(NodeType.ID).name()
			    + "' formal and actual parameters doesn't match: new parameter '" + actualptype
			    + "' old type '" + formalptype + "' adding new polimorphic type.");
		    formalptype = TypeUtils.updateNameType(actualptype, formalParam);
		    formalParam.expr(formalptype, false);
		    // formalParametersUpdated = true;

		    for (AASTNode out : outSymbols) {
			if (out.expr() == null)
			    out.expr(GType.get(BType.UNKNOWN), false);
			if (ptype_idx == -1 || out.exprs().size() < ptype_idx)
			    // minimum
			    ptype_idx = out.exprs().size() - 1;
		    }
		}
	    }
	}

	if (outSymbols.size() == 1) {
	    List<GType> exprs = outSymbols.get(0).exprs();
	    if (coreFun != null) {
		BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater = coreFun.returnTypeUpdater();
		if (returnTypeUpdater != null)
		    // don't alter functionNode return type (use copy
		    // constructor)
		    // change return type based on updater output
		    exprs = returnTypeUpdater.apply(applyNode, actualParamTypes);
		for (GType expr : exprs)
		    if (expr.equals(BType.UNDEFINED))
			throw new TypeException(ErrorMessage.UNSUPPORTED_CORE_FUNCTION_TYPE_UPDATER, applyNode,
				applyNode.name(), actualParamTypes, applyNode.child(NodeType.ID).name(), expr.name());
	    }

	    if (exprs == null) {
		outSymbols.get(0).expr(GType.get(BType.UNKNOWN), false);
		exprs = outSymbols.get(0).exprs();
	    }
	    if (ptype_idx != -2) {
		// formal param types now match with actual param types
		if (exprs.size() <= ptype_idx || ptype_idx == -1)
		    // this means the out types are always unknown
		    ptype_idx = exprs.size() - 1;
		GType oTypeCandidate = null;
		if (polymorphicFunction)
		    oTypeCandidate = exprs.get(ptype_idx);
		else {
		    // choose the most general output type
		    for (GType expr : exprs)
			if (oTypeCandidate == null || !oTypeCandidate.canRepresent(expr))
			    oTypeCandidate = expr;
		}

		if (!oTypeCandidate.equals(BType.STRUCT)) {
		    String newSymbol = Symbols.getSymbolFromType(applyNode.child(NodeType.ID).name(),
			    oTypeCandidate.type());
		    applyNode.expr(GType.get(oTypeCandidate).nameall(newSymbol), false);
		} else
		    applyNode.expr(GType.get(oTypeCandidate), false);

	    } else {
		if (exprs.size() > 1)
		    // if cannot determine input, cannot determine output
		    applyNode.expr(GType.get(BType.UNKNOWN), false);
		else {
		    // no ambiguity
		    List<GType> applyNodeExprs = new LinkedList<>();
		    for (GType e : exprs)
			applyNodeExprs.add(GType.get(e));
		    applyNode.exprs(applyNodeExprs, false);
		}
	    }
	} else {
	    // we need a struct
	    StructType complexType = createComplexType(funNode.child(NodeType.ID).name(), ptype_idx, outSymbols);
	    applyNode.expr(complexType);
	}

	// check if all parameters set
	List<GType> formalParameterTypes = new ArrayList<GType>();
	for (AASTNode formalParam : formalParams)
	    formalParameterTypes.add(formalParam.expr());

	return new Tuple<List<GType>, List<GType>>(formalParameterTypes, actualParamTypes);
    }

    public static StructType joinStructs(StructType formalStruct, StructType actualStruct) {
	StructType joined = (StructType) GType.get(formalStruct);
	// join by name with actualstruct
	joined.name(actualStruct.name());
	if (actualStruct.isInput())
	    joined.setInput(actualStruct.inputName());
	return joined;
    }

    private static boolean unknownFunction(GType expr) {
	// base cases
	if (expr == null || expr.equals(BType.UNKNOWN))
	    return true;
	if (!expr.equals(BType.FUNCTION))
	    return false;
	// recursion
	FunctionType ftype = (FunctionType) expr;
	boolean anyUnknown = false;
	for (GType output : ftype.outputs())
	    anyUnknown |= unknownFunction(output);
	for (GType input : ftype.inputs())
	    anyUnknown |= unknownFunction(input);

	return anyUnknown;
    }

    public static StructType createComplexType(String name, List<AASTNode> outSymbols) {
	return createComplexType(name, -1, outSymbols);
    }

    public static StructType createComplexType(String name, int ptype_idx, List<AASTNode> outSymbols) {
	StructType complexType = (StructType) GType.get(BType.STRUCT).name(name);
	complexType.fullname(name);
	for (AASTNode outSymbol : outSymbols) {
	    List<GType> exprs = outSymbol.exprs();
	    if (exprs == null) {
		outSymbol.expr(GType.get(BType.UNKNOWN), false);
		complexType.updateField(outSymbol);
	    } else if (ptype_idx < 0)
		complexType.updateField(outSymbol);
	    else
		complexType.updateField(outSymbol, ptype_idx);
	}
	return complexType;
    }

    public static Tuple<AASTNode, Boolean> matchFormalParam(AASTNode applyNode, AASTNode funNode, AASTNode actualParam,
	    int actual_param_position, List<AASTNode> formalParams, boolean only_named)
	    throws UnboundException, SyntaxException {
	AASTNode formalParam = null;

	if (actualParam.childExists(NodeType.DEFAULT_VALUE)) {
	    // from now on only named parameters will be accepted
	    only_named = true;
	    // user is setting a specific formal parameter value
	    // search and match by name
	    boolean found = false;
	    for (AASTNode fp : formalParams)
		if (fp.name().equals(actualParam.name())) {
		    formalParam = fp;
		    found = true;
		    break;
		}
	    // error handling
	    if (!found) {
		throw new UnboundException(ErrorMessage.FUN_PARAMETER_MISSING, applyNode,
			funNode.child(NodeType.ID).name(), actualParam.name());
	    }

	} else if (!only_named)
	    // if (i < formalParams.size())
	    formalParam = formalParams.get(actual_param_position);
	// else {
	// }
	else
	    throw new SyntaxException(ErrorMessage.FUN_POSITIONAL_ARGUMENT_FOLLOWS_NAMED_ARGUMENT, applyNode,
		    funNode.child(NodeType.ID).name());

	return new Tuple<AASTNode, Boolean>(formalParam, only_named);
    }

    public static GType getUndefinedType(AASTNode actualNode, BType defaultType) {
	GType type = null;
	if (actualNode.compilationUnit().hasCustomUndefinedType() || defaultType == null) {
	    List<GType> types = actualNode.compilationUnit().undefinedType(actualNode);
	    if (types != null && !types.isEmpty())
		type = types.get(0);
	} else {
	    type = GType.get(defaultType);
	}
	return type;
    }

    public static List<GType> updateStructDefinition(AASTNode fieldaccess, AASTNode baseEl, AASTNode node,
	    GType defaultType) {
	List<GType> types = null;
	StructType curType = null;
	int i = 0;
	String fullName = null;
	int end = fieldaccess.childs().size();
	String inputName = defaultType.inputName();
	AASTNode c = null;
	// find the first sub-structure that we still don't know about
	while (i < end) {
	    c = fieldaccess.childs().get(i);
	    if (c.type().equals(NodeType.DOT)) {
		i = i + 1;
		continue;
	    }
	    if (types == null) {
		// get base expr
		types = c.exprs();
		fullName = c.name();
		if (types != null && types.get(0).equals(BType.STRUCT)) {
		    curType = (StructType) types.get(0);
		    if (curType.isInput()) {
			inputName = curType.inputName();
		    }
		} else {
		    // set first structure
		    curType = (StructType) GType.get(BType.STRUCT).name(fullName).setInput(inputName);
		    if (i == 0)
			curType.baseStruct(baseEl);

		    types = new LinkedList<>();
		    types.add(curType);
		}
	    } else {
		types = curType.getField(c.name());
		fullName += StructType.NESTED_STRUCT_NAME_SEP + c.name();
		if (i == end - 1) {
		    // last element
		    if (types == null) {
			types = new LinkedList<GType>();
			GType defType = defaultType;
			types.add(defType);
		    }
		    curType.addField(types, c.name());
		} else {
		    if (types != null && types.get(0).equals(BType.STRUCT)) {
			curType = (StructType) types.get(0);
			if (curType.isInput()) {
			    inputName = curType.inputName();
			}
		    } else {
			// middle elements
			StructType newStruct = (StructType) GType.get(BType.STRUCT).name(fullName).setInput(inputName);
			types = new LinkedList<>();
			types.add(newStruct);
			// add newly found structure to curType
			curType.addField(types, c.name());
			// override curtype for next element
			curType = newStruct;
		    }
		}
	    }

	    if (c.equals(node)) {
		// skip the rest we found the node we were looking for
		i = end;
		// don't update last node type, type of last node will be returned
		// and the caller will work on it
	    } else {
		i = i + 1;
		if (i < end)
		    // update inner node type
		    // don't update last node type, type of last node will be returned
		    // and the caller will work on it
		    c.exprs(types, true);
	    }
	}

	return types;
    }

    /**
     * 
     * @param statement
     * @param force_reparse_stmt: force statement to be reparsed even if inside an
     *                            already defined function. it doesn't have effect
     *                            of function still to be resolved.
     */
    public static void markStatementToBeReparsed(AASTNode statement, boolean force_reparse_stmt) {
	AAST compilationUnit = statement.compilationUnit();
	// inside a function we rely on the FUNCTION_INPUTS_RESOLVED attribute
	if (!statement.parentExists(NodeType.FUNCTION))
	    statement.attr(NodeAttr.REPARSE_STATEMENT, new AASTNode(null, null, null));
	else {
	    AASTNode funNode = statement.parent(NodeType.FUNCTION);
	    AASTNode origFunNode = funNode;
	    boolean function_already_resolved = false;
	    while (funNode != null) {
		if (funNode.hasAttr(NodeAttr.FUNCTION_RESOLVED) && funNode.hasAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED)) {
		    function_already_resolved = true;
		    // readd inputs_resolved flag (so that it will be re-marked to be translated)
		    funNode.removeAttr(NodeAttr.FUNCTION_RESOLVED);
		    funNode.removeAttr(NodeAttr.FUNCTION_INPUTS_RESOLVED);
		    funNode.attr(NodeAttr.FUNCTION_INPUTS_RESOLVED, true);

		} else if (funNode.hasAttr(NodeAttr.FUNCTION_RESOLVED))
		    // behave like this function wasn't resolved, even if the full firm of the
		    // function is known
		    funNode.removeAttr(NodeAttr.FUNCTION_RESOLVED);

		// inhibits resolution even if return type is set (useful for FunctionsWalker)
		funNode.attr(NodeAttr.INHIBITS_RESOLUTION, true);
		// also check for env params without type: if they exist reparse also parent
		// function/script
		List<AASTNode> envParams = (List<AASTNode>) funNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);
		boolean parsePreviousFunction = false;
		if (envParams != null && !envParams.isEmpty())
		    for (AASTNode envParam : envParams)
			parsePreviousFunction |= envParam.expr() == null || envParam.expr().equals(BType.UNKNOWN);

		if (parsePreviousFunction)
		    funNode = funNode.parent().parent(NodeType.FUNCTION);
		else
		    funNode = null;
	    }
	    if (force_reparse_stmt && function_already_resolved) {
		statement.attr(NodeAttr.REPARSE_STATEMENT, origFunNode);
		funNode = null;
	    } else if (!force_reparse_stmt) {
		// this mark to be reparsed should come directly from statement
		statement.attr(NodeAttr.UNKNOWN_STATEMENT_IN_UNRESOLVED_FUNCTION, origFunNode);
	    }
	}
    }

    public static void markAffectedStatementsToBeReparsed(AASTNode node) {
	for (StatementClass.Type statementType : StatementClass.Type.values())
	    if (node.parentExists(statementType.handledNodes())) {
		AASTNode statement = node.parent(statementType.handledNodes());
		TypeUtils.markStatementToBeReparsed(statement, true);
	    }
    }

    public static void resolveFunctionTypes(AASTNode applyNode, AASTNode funIdNode, AASTNode paramsNode)
	    throws TypeException, UnboundException, SyntaxException {
	AASTNode functionNode = funIdNode.compilationUnit().functionSymbolNode(funIdNode);
	AASTNode paramListNode = functionNode.child(NodeType.PARAMETER_LIST);
	// parameters explicitly listed in function definition
	List<AASTNode> ffunctionFormalParams = paramListNode.childs();
	List<AASTNode> functionFormalParams = new LinkedList<>();
	for (AASTNode f : ffunctionFormalParams)
	    if (!f.type().equals(NodeType.VOID))
		functionFormalParams.add(f);
	// environment parameters, not explicitly listed in function definition but used
	// and not defined in the function
	List<AASTNode> functionEnvParams = (List<AASTNode>) functionNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);
	// actual parameters, parameters explicitly passed as input to this function
	List<AASTNode> functionActualParams = (List<AASTNode>) paramsNode.attr(NodeAttr.PARAMS);
	// update function parameters and node type
	Tuple<List<GType>, List<GType>> parameterTypes = checkAndUpdateFunctionParameters(applyNode, functionNode,
		functionActualParams);
	List<GType> formalParameterTypes = parameterTypes.first();
	List<GType> actualParameterTypes = parameterTypes.second();

	// remains true if all parameters set
	List<AASTNode> formalParametersUnknown = new ArrayList<>(formalParameterTypes.size());
	List<AASTNode> actualParametersUnknown = new ArrayList<>(formalParameterTypes.size());
	List<AASTNode> actualEnvParametersUnknown = new ArrayList<>(formalParameterTypes.size());
	AASTNode actualParamUnknown = null;
	for (int i = 0; i < formalParameterTypes.size(); ++i) {
	    GType formalParam = formalParameterTypes.get(i);

	    if (formalParam == null || formalParam.equals(BType.UNKNOWN)) {
		if (i < functionActualParams.size()) {
		    AASTNode actualParam = functionActualParams.get(i);

		    //if (!actualParam.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM))
			// don't print that unknown function param is unknown
			// because it's misleading for the user. The problem is somewhere else
			//continue;

		    actualParametersUnknown.add(actualParam);
		    actualEnvParametersUnknown.add(null);
		} else if (functionEnvParams != null && i < functionActualParams.size() + functionEnvParams.size()) {
		    // env parameter, will have a value only in core functions with a single parameter
		    actualParametersUnknown.add(null);
		    actualEnvParametersUnknown.add(functionEnvParams.get(i - functionActualParams.size()));
		}

		if (i < functionFormalParams.size())
		    formalParametersUnknown.add(TypeUtils.getIDNode(functionFormalParams.get(i)));
		else
		    // core functions often only have 1 formal parameter
		    formalParametersUnknown
			    .add(TypeUtils.getIDNode(functionFormalParams.get(functionFormalParams.size() - 1)));
	    }else {
	    	// check if actual parameter is unknown
	    	if (i < functionActualParams.size()) {
	    		GType actualType = actualParameterTypes.get(i);
				AASTNode actualParam = functionActualParams.get(i);
			    if ((actualType == null || actualType.equals(BType.UNKNOWN)) && actualParamUnknown == null) {
			    	actualParamUnknown = actualParam;
			    }
	    	}
	    }
	}
	
	applyNode.clearDeferrableError(ErrorMessage.FUN_PARAMETER_UNDEFINED);
	if(actualParamUnknown!=null) {
		// record that this function, has an actual parameter undefined (even if the function itself is fully-defined)
		TypeException ex = new TypeException(ErrorMessage.FUN_PARAMETER_UNDEFINED, applyNode, actualParamUnknown.name(),
				functionNode.child(NodeType.ID).name());
		applyNode.deferrableError(ErrorMessage.FUN_PARAMETER_UNDEFINED, ex);
	}

	if (formalParametersUnknown.isEmpty()) {
	    // set function types as resolved.
	    // a second pass can now completely resolve the types of
	    // this function
	    functionNode.attr(NodeAttr.FUNCTION_INPUTS_RESOLVED, true);
	    applyNode.clearDeferrableError(ErrorMessage.FUN_FORMAL_PARAMS_UNKNOWN);

	    if (functionNode.coreFunction() != null) {
		// update funIdNode return type based on parameter types
		BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater = functionNode.coreFunction()
			.returnTypeUpdater();
		if (returnTypeUpdater != null) {
		    // don't alter functionNode return type (use copy
		    // constructor)
		    // change return type based on updater output
		    FunctionType functionTypes = (FunctionType) GType.get(BType.FUNCTION, formalParameterTypes,
			    returnTypeUpdater.apply(applyNode, formalParameterTypes));
		    funIdNode.expr(functionTypes, false);
		    applyNode.exprs(returnTypeUpdater.apply(applyNode, actualParameterTypes), false);
		}
	    } else if (!functionNode.hasAttr(NodeAttr.FUNCTION_RESOLVED)
		    || !((Boolean) functionNode.attr(NodeAttr.FUNCTION_RESOLVED)))
		// mark this assignment to be reparsed until this function call will be
		// completely resolved
		markAffectedStatementsToBeReparsed(applyNode);
	} else {
	    TypeException ex = new TypeException(ErrorMessage.FUN_FORMAL_PARAMS_UNKNOWN, functionNode,
		    formalParametersUnknown, actualParametersUnknown, actualEnvParametersUnknown,
		    functionNode.coreFunction(), functionNode.child(NodeType.ID).name());
	    applyNode.deferrableError(ErrorMessage.FUN_FORMAL_PARAMS_UNKNOWN, ex);
	}

	applyNode.attr(NodeAttr.FUNCALL, funIdNode.name());
	paramsNode.attr(NodeAttr.FUNCALL_PARAMS, true);
    }

    /**
     * @param expr
     * @return the total size in cells of the matrix (2x2 = 4) or -1 if unknown.
     */
    public static int matrixTotalDimensions(DimensionType expr) {
	IntType[] elDims = expr.dims();
	int mdim = 1;
	int vdim = 0;
	if (elDims != null && elDims.length > 0) {
	    for (IntType elDim : elDims)
		if (elDim.hasValue())
		    mdim *= elDim.valueAsInt();
		else
		    return -1;
	    vdim += mdim;
	} else
	    return -1;

	return vdim;
    }

    /**
     * if a matrix is assigned twice with two different matrix dimensions, choose
     * the biggest one
     * 
     * @param lhsnode
     * @param n_dims
     * @param p_dims
     * @param expected_elements number of expected dimension of the output matrix
     * @return
     * @throws TypeException
     */
    public static IntType[] newMatrixDimensions(AASTNode lhsnode, IntType[] n_dims, IntType[] p_dims,
	    int expected_elements) {
	try {
	    return newMatrixDimensions(lhsIdNode(lhsnode), n_dims, p_dims, expected_elements);
	} catch (Exception e) {
	    lhsnode.compilationUnit()
		    .addWarning(new TypeException(ErrorMessage.WARN_FLAT_INDEX_OUT_OF_BOUNDS, lhsnode));
	    return p_dims;
	}
    }

    public static IntType[] newMatrixDimensions(String matrixName, IntType[] n_dims, IntType[] p_dims,
	    int expected_elements) throws Exception {
	IntType prevd, newd;
	List<IntType> ret = null;
	IntType[] large, small;
	IntType[] new_dims = TypeUtils.realDims(n_dims, expected_elements);
	IntType[] prev_dims = TypeUtils.realDims(p_dims, expected_elements);

	if (prev_dims == null || prev_dims.length == 0 || allOnes(prev_dims))
	    return n_dims;

	if (new_dims == null || new_dims.length == 0 || allOnes(new_dims))
	    return p_dims;

	int totalValue = 1;
	for (int d = new_dims.length - 1; d < prev_dims.length; ++d)
	    if (prev_dims[d].hasValue())
		totalValue *= prev_dims[d].valueAsInt();
	    else {
		totalValue = -1;
		break;
	    }

	boolean largeNew;
	if (new_dims.length >= prev_dims.length) {
	    small = prev_dims;
	    large = new_dims;
	    largeNew = true;
	} else {
	    // check if access with flat index?
	    IntType flatIdx = new_dims[new_dims.length - 1];
	    if (flatIdx.hasValue() && totalValue > 0) {
		if (flatIdx.valueAsInt().compareTo(totalValue) > 0)
		    throw new Exception();
		// this is a valid flat index, keep previous dimensions
		return p_dims;
	    }

	    small = new_dims;
	    large = prev_dims;
	    largeNew = false;
	}

	// we are adding new dimensions
	ret = new LinkedList<>();
	// iterate on prev dims to see if they should be enlarged
	int i;
	for (i = 0; i < small.length; ++i) {
	    newd = new_dims[i];
	    prevd = prev_dims[i];

	    if (prevd.hasValue() && newd.hasValue())
		if (prevd.valueAsInt() < newd.valueAsInt())
		    ret.add((IntType) newd);
		else
		    ret.add((IntType) prevd);
	    else {
		// if value not available at compile-time
		// keep the unbound one, prefer old value against new
		// also to avoid changing dimension name
		if (!prevd.hasValue())
		    ret.add((IntType) prevd);
		else
		    ret.add((IntType) newd);
	    }
	}

	int k = i;
	// add new dimensions
	for (; k < large.length; ++k) {
	    ret.add(large[k]);
	}

	if (ret.size() < p_dims.length && p_dims[0].hasIntValue() && p_dims[0].valueAsInt() == 1)
	    ret.add(0, (IntType) GType.get(BType.INT, 1));
	else if (ret.size() < n_dims.length && n_dims[0].hasIntValue() && n_dims[0].valueAsInt() == 1)
	    ret.add(0, (IntType) GType.get(BType.INT, 1));

	if (ret.size() < p_dims.length && p_dims[p_dims.length - 1].hasIntValue()
		&& p_dims[p_dims.length - 1].valueAsInt() == 1)
	    ret.add((IntType) GType.get(BType.INT, 1));
	else if (ret.size() < n_dims.length && n_dims[n_dims.length - 1].hasIntValue()
		&& n_dims[n_dims.length - 1].valueAsInt() == 1)
	    ret.add((IntType) GType.get(BType.INT, 1));

	for (i = 1; i <= ret.size(); ++i)
	    if (!ret.get(i - 1).hasValue() && !ret.get(i - 1).hasName())
		ret.get(i - 1).name(TypeUtils.matrixDimName(matrixName, i));
	return ret.toArray(new IntType[ret.size()]);
    }

    /**
     * returns true if the matrix is degenere: contains only a single value.
     * 
     * @param matrix
     * @return
     */
    public static boolean isDegeneratedMatrix(GType matrix) {
	if (!matrix.isCastableToMatrix())
	    return false;
	DimensionType endType = (DimensionType) matrix;
	for (IntType edim : endType.dims())
	    if (!edim.hasValue() || !edim.valueAsInt().equals(1))
		return false;
	return true;
    }

    public static GType resultingTypeMatrixMatrixPointwiseOp(AASTNode node, DimensionType lexpr, DimensionType rexpr) {
	String outName = TypeUtils.matrixName(node);
	IntType[] copyDims = TypeUtils.mostGeneralDimension(((DimensionType) lexpr).dims(),
		((DimensionType) rexpr).dims());
	IntType[] outDims = new IntType[copyDims.length];
	for (int w = 0; w < copyDims.length; ++w) {
	    outDims[w] = (IntType) GType.get(copyDims[w]);
	}

	GType lofType = ((DimensionType) lexpr).of();
	GType rofType = ((DimensionType) rexpr).of();
	GType outType;
	boolean sparse = false;
	// resulting matrix sparse only if both inputs are sparse
	if (lexpr.equals(BType.MATRIX) && rexpr.equals(BType.MATRIX))
	    sparse = ((MatrixType) lexpr).isSparse() && ((MatrixType) rexpr).isSparse();

	if (requireDoubleNodes().contains(node.type()))
	    // force scalar in case of operations that may arise a double
	    outType = GType.get(BType.MATRIX, outName, GType.get(BType.SCALAR), outDims, sparse);
	else if (lofType.canRepresent(rofType))
	    outType = GType.get(BType.MATRIX, outName, lofType, outDims, sparse);
	else if (rofType.canRepresent(lofType))
	    outType = GType.get(BType.MATRIX, outName, rofType, outDims, sparse);
	else
	    outType = GType.get(BType.MATRIX, outName, GType.get(BType.SCALAR), outDims, sparse);
	return outType;
    }

}
