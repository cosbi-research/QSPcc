package eu.cosbi.qspcc.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.exceptions.UnfinishedTranslationException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.ScalarType;
import eu.cosbi.qspcc.expressions.type.SliceType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.interfaces.annotations.StatementClass;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.qspcc.type.StatementType;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

@StatementClass(category = StatementClass.Type.ASSIGN)
public class AssignStatement extends Statement {
    private Logger logger = LogManager.getLogger(AssignStatement.class);
    // list of elements in assignment
    List<AASTNode> lhs = null;
    Map<AASTNode, List<AASTNode>> lhs_params = null;
    GType rhs_type = null;
    // right hand side expression statement
    ExpressionStatement rhs = null;
    // we are evaluating left hand side
    boolean isLhs = false;
    // we are evaluating right hand side
    boolean isRhs = false;
    private AASTNode lhsNode;
    private AASTNode rhsNode;
    private Map<AASTNode, ExpressionStatement> lhsParamsExprs;

    public AssignStatement(AAST aast, AASTNode fullStatement) {
	super(aast, fullStatement);
	lhs = new ArrayList<AASTNode>();
	lhs_params = new HashMap<AASTNode, List<AASTNode>>();
    }

    @Override
    public StatementType type() {
	return StatementType.FUNCTIONAL;
    }

    // called on the onEnter of level ASSIGN+1
    @Override
    public void setBranch(AASTNode node, String tokenValue) throws UnboundException {
	switch (node.type()) {
	// --- CONTEXT INFORMATION ---
	case LHS:
	    lhsNode = node;
	    isRhs = false;
	    isLhs = true;
	    // map of expression statements for each parameter on lhs
	    lhsParamsExprs = new HashMap<AASTNode, ExpressionStatement>();
	    if (lhsNode.childExists(NodeType.APPLY)
		    && lhsNode.child(NodeType.APPLY).childExists(NodeType.FUNCTION_PARAMETER_LIST)) {
		AASTNode paramlist = lhsNode.child(NodeType.APPLY).child(NodeType.FUNCTION_PARAMETER_LIST);
		for (AASTNode param : paramlist.childs())
		    lhsParamsExprs.put(param, new ExpressionStatement(aast, nodesStack, param));
	    } else if (lhsNode.childExists(NodeType.MATRIX)) {

	    }
	    return;
	case RHS:
	    rhsNode = node;
	    isLhs = false;
	    isRhs = true;
	    // child mode, share nodesStack with this class
	    rhs = new ExpressionStatement(aast, nodesStack, node);
	    return;
	default:
	    throw new UnboundException(ErrorMessage.INTERNAL_ASSIGN_UNRECOGNIZED_BRANCH, node);
	}
    }

    @Override
    public void pushTranslation(AASTNode tp) {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(tp);
	    if (!lhsParamsExprs.containsKey(tp) && lhsStmt != null)
		lhsStmt.pushTranslation(tp);
	} else if (isRhs)
	    rhs.pushTranslation(tp);
    }

    @Override
    public void pushParameterList(AASTNode node) {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.pushParameterList(node);
	    else
		super.pushParameterList(node);
	} else if (isRhs)
	    rhs.pushParameterList(node);
	else
	    super.pushParameterList(node);
    }

    @Override
    public void popParameterList(AASTNode node) {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.popParameterList(node);
	    else
		super.popParameterList(node);
	} else if (isRhs)
	    rhs.popParameterList(node);
	else
	    super.popParameterList(node);
    }

    @Override
    public void updateLastParameterList(AASTNode node) {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    // if this is exactly the top-level node, update super
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.updateLastParameterList(node);
	    else
		super.updateLastParameterList(node);

	} else if (isRhs)
	    rhs.updateLastParameterList(node);
	else
	    super.updateLastParameterList(node);
    }

    @Override
    public void resetStructList(AASTNode node) {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.resetStructList(node);
	    else
		super.resetStructList(node);
	} else if (isRhs)
	    rhs.resetStructList(node);
	else
	    super.resetStructList(node);
    }

    @Override
    public void updateStructList(AASTNode node) {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.updateStructList(node);
	    else
		super.updateStructList(node);
	} else if (isRhs)
	    rhs.updateStructList(node);
	else
	    super.updateStructList(node);
    }

    @Override
    public void initVectorList(AASTNode node) throws GException {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.initVectorList(node);
	    else
		super.initVectorList(node);
	} else if (isRhs)
	    rhs.initVectorList(node);
	else
	    super.initVectorList(node);
    }

    @Override
    public void updateVectorList(AASTNode node) throws GException {
	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (!lhsParamsExprs.containsKey(node) && lhsStmt != null)
		lhsStmt.updateVectorList(node);
	    else
		super.updateVectorList(node);
	} else if (isRhs)
	    rhs.updateVectorList(node);
	else
	    super.updateVectorList(node);
    }

    /**
     * called only on a closed, translatable sub-tree. Meant to be directly called
     * only by Translator objects (matlabToC) or from other parent statements that
     * owns this object in child mode
     * 
     * @throws UnboundException
     */
    @Override
    public void translateImpl(AAST aast, AASTNode node, String value) throws GException {

	if (isLhs) {
	    ExpressionStatement lhsStmt = getExpression(node);
	    if (lhsStmt != null)
		lhsStmt.translateImpl(aast, node, value);
	    else
		evalLhs(node, value);
	} else if (isRhs)
	    evalRhs(node, value);
	else
	    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_ASSIGN_MISSING_BRANCH, node);
    }

    private ExpressionStatement getExpression(AASTNode node) {
	for (Map.Entry<AASTNode, ExpressionStatement> entry : lhsParamsExprs.entrySet())
	    if (entry.getKey().parentOf(node)) {
		return entry.getValue();
	    }
	return null;
    }

    /**
     * final assignment
     * 
     * @throws UnfinishedTranslationException
     * @throws UndefinedTranslationException
     * @throws TypeException
     * @throws UnboundException
     * @throws SyntaxException
     */
    @Override
    public void completeStatement() throws UnfinishedTranslationException, UndefinedTranslationException, TypeException,
	    UnboundException, SyntaxException {

	AASTNode rhsTp = rhs.getExpressionIdentifier();
	if (rhsTp == null)
	    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_ASSIGN_UNDEFINED_RHS_TYPE, statement, lhs,
		    rhs.statement);

	// remember type of assignment
	rhs_type = rhsTp.expr();
	IntType[] rhs_dim = null;
	GType stype = null;
	if (rhs_type != null && rhs_type.equals(BType.MATRIX)) {
	    rhs_dim = ((MatrixType) rhs_type).dims();
	    stype = ((MatrixType) rhs_type).of();
	}

	boolean no_params = true;
	for (List<AASTNode> params : lhs_params.values())
	    no_params &= params.isEmpty();

	// add missing lhs_params entries
	for (AASTNode lhsElem : lhs)
	    if (!lhs_params.containsKey(lhsElem))
		lhs_params.put(lhsElem, new ArrayList<AASTNode>());

	if (!no_params && lhs.size() > 1)
	    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_MULTIASSIGN_WITH_LHS_PARAMS, statement);

	// update type for scalars that are assigned inside a loop
	// if (lhs_params.isEmpty())
	// updateLoopVariable(lhs, rhsTp);
	// define and assign all rhs symbols to lhs symbols
	translateAssignment(lhs, lhs_params, rhsTp);

	// remember this is a multiple assign
	AASTNode assignNode = lhsNode.parent(statement.type());
	assignNode.attr(NodeAttr.LHS, lhs);
	assignNode.attr(NodeAttr.RHS, rhsTp);
	assignNode.attr(NodeAttr.LHS_PARAMS, lhs_params);
	if (lhs.size() > 1)
	    assignNode.attr(NodeAttr.MULTIPLE_ASSIGN, true);
    }

    private void updateLoopVariable(List<AASTNode> lhs, AASTNode rhsTp) {
	for (AASTNode l : lhs) {
	    AASTNode loop = l.parent(new NodeType[] { NodeType.FOR, NodeType.PARFOR, NodeType.WHILE });
	    // if(l)
	}
    }

    private void evalLhs(AASTNode node, String value) throws GException {
	switch (node.type()) {
	case EXPRESSION:
	    // copy type from child
	    node.exprs(node.childs().get(0).exprs(), false);
	    break;
	case VOID:
	    // skip this return value!
	    lhs.add(node);
	    break;
	case LHS:
	case MATRIX:
	case VECTOR:
	case ID_NODE:
	    break;
	case APPLY:
	    node.child(NodeType.FUNCTION_PARAMETER_LIST).attr(NodeAttr.MATRIX_PARAMS, node.child(NodeType.ID));
	    break;
	case FUNCTION_PARAMETER_LIST:
	    List<AASTNode> curParams = new ArrayList<AASTNode>();
	    for (AASTNode t : lastParameterList()) {
		AASTNode n = lhsParamsExprs.get(t).getExpressionIdentifier();
		curParams.add(n);
	    }
	    lhs_params.put(lhs.get(lhs.size() - 1), curParams);
	    break;
	case ID:
	    if (!node.parentExists(NodeType.FIELDACCESS))
		lhs.add(node);
	    break;
	case DOT:
	    // this means this is a FIELDACCESS node.
	    // the last id added to the list is the base struct name.
	    break;
	case FIELDACCESS:
	    // this means this is a FIELDACCESS node.
	    // lhs list contains the sequence of struct access
	    // iterate until lhs.size()-1 and set type to struct
	    // reset lhs to last one
	    AASTNode last = TypeUtils.getIDNode(node.childs().get(node.childs().size() - 1));
	    lhs.add(last);
	    break;
	default:
	    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_ASSIGN_UNKNOWN_NODE_IN_LHS, node.parent());
	}
    }

    private void evalRhs(AASTNode node, String value) throws GException {
	// right-hand side is an expression! use it's translation engine
	// NOTE: translateImpl can be called directly only in child mode
	rhs.translateImpl(aast, node, value);
    }

    public void translateAssignment(List<AASTNode> lhs, Map<AASTNode, List<AASTNode>> lhs_params, AASTNode rhsTp)
	    throws TypeException, UndefinedTranslationException, UnboundException, SyntaxException {

	List<AASTNode> lhsDef = new ArrayList<AASTNode>();
	GType rhs_type = rhsTp.expr();

	if (rhs_type == null)
	    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_ASSIGN_UNDEFINED_RHS_TYPE, statement, lhs,
		    rhsTp);

	AASTNode implicitParentNode = null;
	if (rhsTp.type().equals(NodeType.ID) && rhsTp.hasAttr(NodeAttr.IMPLICITLY_DEFINED))
	    implicitParentNode = (AASTNode) rhsTp.attr(NodeAttr.IMPLICITLY_DEFINED);
	else if (rhsTp.childExists(NodeType.ID) && rhsTp.child(NodeType.ID).hasAttr(NodeAttr.IMPLICITLY_DEFINED))
	    implicitParentNode = (AASTNode) rhsTp.child(NodeType.ID).attr(NodeAttr.IMPLICITLY_DEFINED);

	if (implicitParentNode != null)
	    // propagate implicitly defined to LHS
	    for (AASTNode lhsIDNode : lhs)
		if (!lhsIDNode.type().equals(NodeType.VOID)) {
		    lhsIDNode.parent(NodeType.STATEMENT_LIST).propagateAttribute(NodeAttr.IMPLICITLY_DEFINED,
			    implicitParentNode, lhsIDNode.name());
		}

	GType stype = null;
	IntType[] rhs_dim = null;
	if (rhs_type instanceof DimensionType) {
	    stype = ((DimensionType) rhs_type).of();
	    rhs_dim = ((DimensionType) rhs_type).dims();
	}

	// if struct
	if (rhs_type.equals(BType.STRUCT) && (lhs.size() > 1 || ((StructType) rhs_type).explodeOnAssignment())) {
	    // number of real field
	    int j = 0;
	    Iterator<Tuple<List<GType>, String>> it = ((StructType) rhs_type).iterFields();
	    while (it.hasNext()) {
		Tuple<List<GType>, String> ith_rhs_tp = it.next();
		if (j >= lhs.size()) {
		    if (!statement.hasAttr(NodeAttr.USER_WARNING)) {
			statement.compilationUnit()
				.addWarning(new SyntaxException(ErrorMessage.WARN_ASSIGN_MORE_RHS_THAN_LHS, statement,
					ith_rhs_tp.second(), lhs.size() - j));
			statement.attr(NodeAttr.USER_WARNING, true);
		    }
		} else {
		    if (!lhs.get(j).type().equals(NodeType.VOID)) {
			List<AASTNode> params = lhs_params.get(lhs.get(j));
			// define it if not defined or unknown:
			Tuple<AASTNode, String> new_lhs = assignDefineIfNotDefined(lhs, j, rhsTp, ith_rhs_tp, params,
				true);

			lhsDef.add(new_lhs.first());
		    }
		    j++;
		}
	    }
	} else if (rhs_type.equals(BType.STRUCT) && !lhs.get(0).type().equals(NodeType.VOID)) {
	    // ASSIGNMENT OF struct of type input

	    // direct struct assignment
	    // define it if not defined or unknown:
	    int i = 0;
	    List<AASTNode> params = lhs_params.get(lhs.get(i));
	    Tuple<AASTNode, String> new_lhs = assignDefineIfNotDefined(lhs, i, rhsTp,
		    new Tuple<List<GType>, String>(rhsTp.exprs(), rhsTp.symbol()), params, true);
	    lhsDef.add(new_lhs.first());
	    // optimizer.translateAssignment(new_lhs.first(), params,
	    // new_lhs.second());
	} else {
	    // if not struct
	    Tuple<List<GType>, String> ithRhs = null;
	    for (int i = 0; i < lhs.size(); i++) {
		if (lhs.get(i).type().equals(NodeType.VOID))
		    continue;

		List<AASTNode> params = lhs_params.get(lhs.get(i));
		// define it if not defined or unknown:

		if (rhs_type.equals(BType.MATRIX) && lhs.size() > 1) {
		    // case [c,d] = b
		    // TODO: this is not supported by MATLAB
		    //       find a way to avoid handling code that the frontend
		    //       doesn't support
		    IntType rhs_rows = null;
		    IntType rhs_cols = null;
		    MatrixType mtype = (MatrixType) rhsTp.expr();
		    IntType[] rhsDims = mtype.dims();
		    List<GType> sliceParams = null;
		    GType rowSliceExpr = null;
		    SliceType colSliceExpr = null;

		    if (rhsDims == null) {
			// if rhs is matrix but we don't have his dimension
			// assume 2 dimension here
			mtype.defaultDims(2);
			rhsDims = mtype.dims();
		    }

		    if (rhsDims.length != 2)
			throw new UndefinedTranslationException(
				ErrorMessage.INTERNAL_MULTIASSIGN_WITH_NDIMENSIONAL_RHS_UNSUPPORTED, rhsTp,
				rhsDims.length);

		    rhs_rows = rhsDims[0];
		    rhs_cols = rhsDims[1];
		    rowSliceExpr = GType.get(BType.MATRIX_ACCESS_ALL);

		    // slice in equally sized columns keep rows unalterated i
		    // lhs.size > 2
		    // if size == 2 first is the first column, second all the
		    // rest
		    Triple<GType, GType, GType> slices;
		    IntType start = getIthSliceColStart(i, rhs_cols, lhs.size());
		    IntType step = (IntType) GType.get(BType.INT, 1);
		    IntType end = getIthSliceColEnd(i, rhs_cols, lhs.size());
		    GType dim = GType.get(BType.INT,
			    (int) Math.max(1, ((end.valueAsInt() - start.valueAsInt()) / step.valueAsInt()) + 1));

		    slices = new Triple<GType, GType, GType>(start, step, end);
		    String slice_name = rhs_type.name() + "_slicing";
		    // the of value will be set below
		    colSliceExpr = (SliceType) GType.get(BType.MATRIX_ACCESS_SLICE, slice_name, null, slices, dim)
			    .setVirtual();

		    sliceParams = new ArrayList<GType>();
		    sliceParams.add(rowSliceExpr);
		    sliceParams.add(colSliceExpr);

		    // output dimension: all the rows, only some columns
		    List<IntType> outDims = new ArrayList<IntType>();
		    outDims.add(rhs_rows);
		    if (end.valueAsInt() == null || start.valueAsInt() == null) {
			String strval = end.valueAsString() + "-" + start.valueAsString() + "+1";
			outDims.add((IntType) GType.get(BType.INT).name(strval));
		    } else
			outDims.add((IntType) GType.get(BType.INT, end.valueAsInt() - start.valueAsInt() + 1));

		    GType lhsType = GType.get(BType.MATRIX, lhs.get(i).name(), stype,
			    outDims.toArray(new IntType[outDims.size()]));
		    lhs.get(i).expr(lhsType);
		    colSliceExpr.of(lhsType);

		    lhs.get(i).symbol(Symbols.getSymbolFromType(lhs.get(i).name(), BType.SCALAR));
		    lhs.get(i).attr(NodeAttr.MATRIX_SLICES, sliceParams);
		    // slicedMatrix = new Quadruple<ExprType, SymbolType,
		    // String, List<String>>(ExprType.get(Expr.MATRIX),
		    // SymbolType.SCALAR, Symbols.getNewScalarVectorSymbol(),
		    // outDims);
		    // do the slicing
		    // TODO add slicedMatrix as output
		    // optimizer.translateMatrixAccess(slicedMatrix,
		    // rhsTp.third(), rhsTp.fourth(), rhsTp.second(),
		    // sliceParams);
		    // statementEnv.updateSingleSymbol(slicedMatrix.second(),
		    // slicedMatrix.first(), slicedMatrix.third(),
		    // slicedMatrix.fourth());
		    ithRhs = new Tuple<List<GType>, String>(lhs.get(i).exprs(), lhs.get(i).symbol());
		} else if (lhs.size() == 1 || rhs_type.equals(BType.UNKNOWN)) {
		    // if on lhs we have just 1 element, or we have multiple elements but rhs is
		    // unknown (set unknown to all of them)
		    ithRhs = new Tuple<List<GType>, String>(rhsTp.exprs(), rhsTp.symbol());
		} else
		    throw new UndefinedTranslationException(ErrorMessage.INTERNAL_ASSIGN_UNDEFINED_RHS_TYPE, statement,
			    lhs, rhsTp);

		Tuple<AASTNode, String> new_lhs = assignDefineIfNotDefined(lhs, i, rhsTp, ithRhs, params, true);
		// ------ here statementEnv.symbolExists returns true! ---------
		// now it is type-safe to translateAssign
		lhsDef.add(new_lhs.first());
		new_lhs.first().attr(NodeAttr.PARAMS, params);
		// optimizer.translateAssignment(new_lhs.first(), params,
		// new_lhs.second());
	    }
	}
	boolean allset = rhs_type != null && !rhs_type.equals(BType.UNKNOWN) && innerTypesDefined(rhs_type);
	for (AASTNode lhsNode : lhsDef) {
	    if (!allset)
		break;
	    allset &= lhsNode.expr() != null && !lhsNode.expr().equals(BType.UNKNOWN)
		    && innerTypesDefined(lhsNode.expr());
	}
	if (!allset || rhs.shouldBeReparsed())
	    // mark this statement (and his dependencies) to be reparsed
	    markToBeReparsed();
	else
	    // assign statement completed
	    markAsCompleted();
    }

    private boolean innerTypesDefined(GType expr) {
	if (expr.equals(BType.STRUCT)) {
	    Iterator<Tuple<List<GType>, String>> it = ((StructType) expr).iterFields();
	    while (it.hasNext()) {
		for (GType ftype : it.next().first())
		    if (ftype.equals(BType.UNKNOWN))
			return false;
	    }
	}
	return true;
    }

    /**
     * actually do the assignment
     * 
     * @param lhs
     * @param lhs_idx
     * @param rhsTp
     * @param ith_rhs_tp
     * @param params
     * @param updateSymbolsBackward
     * @return
     * @throws UndefinedTranslationException
     * @throws TypeException
     * @throws UnboundException
     * @throws SyntaxException
     */
    private Tuple<AASTNode, String> assignDefineIfNotDefined(List<AASTNode> lhs, int lhs_idx, AASTNode rhsTp,
	    Tuple<List<GType>, String> ith_rhs_tp, List<AASTNode> params, boolean updateSymbolsBackward)
	    throws UndefinedTranslationException, TypeException, UnboundException, SyntaxException {

	// needed for matrix splitting feature
	// [a,b] = A will split A matrix in two sub-matrices
	IntType[] ith_rhs_dim = null;
	String ith_rhsId = null;
	GType ith_rhs_type = null;
	IntType[] lhs_dim = null;

	ith_rhsId = (String) ith_rhs_tp.second();
	AASTNode new_lhs = null;
	AASTNode curLhs = lhs.get(lhs_idx);
	// if assignment to myself or delete, this matrix should be necessarily dynamic
	boolean dyn_id = lhs.get(lhs_idx).hasAttr(NodeAttr.DYNAMIC_ID);
	boolean assign_to_myself = false;
	List<GType> rhsTypes = ith_rhs_tp.first();
	boolean lhs_assigned = false;
	Iterator<GType> rhsTypesIt = rhsTypes.iterator();
	while (rhsTypesIt.hasNext()) {
	    ith_rhs_type = rhsTypesIt.next();
	    GType lhsstype = null;
	    GType lhsExpr = lhs.get(lhs_idx).expr();
	    boolean lhsUnknown = lhsExpr == null || GType.get(BType.UNKNOWN).equals(lhsExpr);
	    String lhsId = (String) lhs.get(lhs_idx).symbol();
	    if (ith_rhs_type instanceof DimensionType) {
		// assign_to_myself modify the flow to handle case of matrix assigned to itself
		// a=a(..)
		// a = [a; x] a = [a, x]
		// but we need to discriminate the case where one of the conditions
		// before are true but also it happens (before or after) that
		// a = w(1:m,:)
		// in the latter case assign_to_myself should be false
		assign_to_myself = dyn_id && !lhsUnknown && ith_rhs_type.name().equals(lhsExpr.name());
		ith_rhs_dim = ((DimensionType) ith_rhs_type).dims();
		lhsstype = ((DimensionType) ith_rhs_type).of();
	    } else
		assign_to_myself = false;
	    // useful for delete case a(:,1)=[]
	    boolean is_delete = ith_rhs_dim != null && (ith_rhs_dim.length == 0 || (ith_rhs_dim.length == 1
		    && ith_rhs_dim[0].hasIntValue() && ith_rhs_dim[0].valueAsInt().equals(0)));

	    // in this specific case the updateSymbols at the end of this
	    // function will override the unknown type
	    if (lhsUnknown) {
		// if symbol does not exist..
		if (params == null || params.isEmpty()) {
		    lhs_dim = assignType(ith_rhs_type, lhsstype, lhsId, ith_rhs_dim);
		    if (ith_rhs_type.isCastableToMatrix())
			// copy to avoid changing rhs names
			ith_rhs_type = TypeUtils.updateNameType(ith_rhs_type, curLhs);
		} else {
		    // assignment to missing matrix symbol.
		    // or side-effect of function call (true especially for R)
		    lhs_dim = getDimsBasedOnLHSParams(curLhs, params, ith_rhs_type);
		    if (is_delete) {
			IntType[] new_lhs_dim = new IntType[lhs_dim.length];
			int dn = 0;
			// only the rhs dimensions should be dynamic
			// a[4x4] = a(1:2) --> a[nx4]
			for (; dn < ith_rhs_dim.length && dn < lhs_dim.length; ++dn)
			    new_lhs_dim[dn] = (IntType) GType.get(BType.INT)
				    .name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
			for (; dn < lhs_dim.length; ++dn)
			    new_lhs_dim[dn] = lhs_dim[dn];
			lhs_dim = new_lhs_dim;
		    }
		    boolean matrixAssignment = false;
		    for (AASTNode param : params) {
			GType paramType = TypeUtils.getExprGeneralized(param);
			if (paramType.isCastableToMatrix() || paramType.equals(BType.MATRIX_ACCESS_ALL)) {
			    matrixAssignment = true;
			    break;
			}
		    }

		    if (aast.functionSymbolExists(curLhs)) {
			AASTNode funIdNode = curLhs;
			// here we have a function call to a user-defined function
			// that's the perfect point to infer the types of this
			// user-defined parameters
			AASTNode paramsNode = params.get(0).parent(NodeType.FUNCTION_PARAMETER_LIST);
			paramsNode.attr(NodeAttr.PARAMS, params);
			TypeUtils.resolveFunctionTypes(funIdNode.parent(NodeType.APPLY), funIdNode, paramsNode);
			return new Tuple<AASTNode, String>(curLhs, ith_rhsId);
		    } else if (!matrixAssignment) {
			if (!ith_rhs_type.equals(BType.UNKNOWN) && !ith_rhs_type.equals(BType.UNDEFINED))
			    ith_rhs_type = GType.get(BType.MATRIX, curLhs.fullname(), ith_rhs_type, lhs_dim);
		    } else if (ith_rhs_type.isCastableToMatrix()) {
			DimensionType dtype = (DimensionType) ith_rhs_type;
			ith_rhs_type = GType.get(BType.MATRIX, curLhs.fullname(), dtype.of(), lhs_dim);
		    } else if (ith_rhs_type.isCastableToScalar()) {
			GType ofType = GType.get(ith_rhs_type.type());
			ith_rhs_type = GType.get(BType.MATRIX, curLhs.fullname(), ofType, lhs_dim);
		    } else if (ith_rhs_type.equals(BType.UNKNOWN)) {
			// let's try to go on, maybe the next try we will have more information
		    } else
			throw new TypeException(ErrorMessage.UNSUPPORTED_ASSIGN_ALL_OF_NONMATRIX, statement,
				ith_rhs_type);
		    // parameters are for a matrix
		    params.get(0).parent(NodeType.FUNCTION_PARAMETER_LIST).attr(NodeAttr.MATRIX_PARAMS, curLhs);
		}

		if (curLhs.parentExists(NodeType.APPLY))
		    curLhs.parent(NodeType.APPLY).attr(NodeAttr.MATACCESS, true);

		// update definition of structure that lhs belongs to if in structure
		updateLhsType(curLhs, rhsTp, ith_rhs_type);
		// statementEnv.updateSingleSymbol(lhsstype, ith_rhs_type,
		// lhsId,
		// lhs_dim);
		new_lhs = curLhs;
	    } else if (lhsExpr.equals(BType.STRING)) {
		// update definition of structure that lhs belongs to if in structure
		updateLhsType(curLhs, rhsTp, ith_rhs_type);

		new_lhs = curLhs;
	    } else {
		List<GType> real_lhs_types = curLhs.exprs();
		GType real_lhs_type = null;
		IntType[] real_lhs_dim = null;
		GType real_lhs_stype = null;
		GType lhsApplyType = null;
		GType lhsApplyOfType = null;
		if (params == null || params.isEmpty()) {
		    // check for compatibility with RHS,
		    // if not compatible change lhsApplyType
		    Iterator<GType> rltIt = ((LinkedList<GType>) real_lhs_types).descendingIterator();
		    while (lhsApplyType == null && rltIt.hasNext()) {
			real_lhs_type = rltIt.next();
			real_lhs_stype = null;
			real_lhs_dim = null;
			if (real_lhs_type instanceof DimensionType) {
			    real_lhs_stype = ((DimensionType) real_lhs_type).of();
			    real_lhs_dim = ((DimensionType) real_lhs_type).dims();
			}
			lhsApplyType = checkLHSRHSTypeCompatibility(ith_rhs_type, rhsTp, real_lhs_type, false, curLhs);
		    }
		    if (lhsApplyType == null) {
			TypeException ex = new TypeException(ErrorMessage.UNSUPPORTED_ASSIGN_INCOMPATIBLE_TYPES,
				statement, rhsTp.fullname(), ith_rhs_type, curLhs.fullname(), real_lhs_type);
			if (lhs_assigned || rhsTypesIt.hasNext()) {
			    // if we have other types to check, just discard
			    // this one as impossible with a warning
			    if (!curLhs.hasAttr(NodeAttr.USER_WARNING)) {
				statement.compilationUnit().addWarning(ex);
				curLhs.attr(NodeAttr.USER_WARNING, true);
			    }
			    continue;
			} else
			    throw ex;
		    }

		    lhsApplyOfType = getLHSOfType(lhsApplyType);

		    if (lhsApplyType instanceof DimensionType) {
			lhs_dim = ((DimensionType) lhsApplyType).dims();
			// update name to be coherent with lhs
			for (int dn = 0; dn < lhs_dim.length; ++dn)
			    if (!lhs_dim[dn].hasValue())
				lhs_dim[dn].name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
			if (assign_to_myself && !ith_rhs_type.equals(BType.UNKNOWN)) {
			    if (ith_rhs_dim == null)
				throw new UndefinedTranslationException(ErrorMessage.INTERNAL_ASSIGN_UNDEFINED_RHS_TYPE,
					statement, lhs, rhsTp);
			    List<Integer> general_dims;
			    Object dynid = curLhs.attr(NodeAttr.DYNAMIC_ID);
			    if (dynid instanceof List)
				// case a = [a; b] or a = [a, b]
				general_dims = (List<Integer>) dynid;
			    else {
				logger.debug(
					"dynamic ID miss dynamic dimension information, assuming all dynamic up to RHS dimension");
				// case a = a(1:k, 2)
				general_dims = new LinkedList<Integer>();
				for (int wk = 0; wk < ith_rhs_dim.length; ++wk)
				    if (!ith_rhs_dim[wk].hasValue() || ith_rhs_dim[wk].valueAsInt() > 1)
					general_dims.add(wk);
				// if empty -> allones -> assume the first dimension
				if (general_dims.isEmpty())
				    general_dims.add(0);
			    }
			    // this should be necessarily dynamic
			    // only the rhs dimensions should be dynamic
			    // a[4x4] = a(1:2) --> a[nx4]
			    IntType[] new_lhs_dim = new IntType[Math.max(lhs_dim.length, ith_rhs_dim.length)];
			    for (Integer dn : general_dims) {
				new_lhs_dim[dn] = (IntType) GType.get(BType.INT)
					.name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
			    }
			    int dn = 0;
			    // first rhs_dim because this is an assign_to_myself case,
			    // so rhs_dim names are coherent with lhs_dim names
			    for (; dn < ith_rhs_dim.length; ++dn)
				if (!general_dims.contains(dn)) {
				    new_lhs_dim[dn] = (IntType) GType.get(ith_rhs_dim[dn]);
				    if (!new_lhs_dim[dn].hasValue())
					new_lhs_dim[dn].name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
				}
			    for (; dn < lhs_dim.length; ++dn)
				if (!general_dims.contains(dn)) {
				    new_lhs_dim[dn] = (IntType) GType.get(lhs_dim[dn]);
				    if (!new_lhs_dim[dn].hasValue())
					new_lhs_dim[dn].name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
				}
			    lhs_dim = new_lhs_dim;

			}
		    }
		    // update definition of structure that lhs belongs to if in structure
		    updateLhsType(curLhs, rhsTp,
			    typeOf(curLhs, rhsTp, curLhs.fullname(), lhsApplyType, lhsApplyOfType, lhs_dim), true);

		} else {
		    // use last one as first guess
		    real_lhs_type = ((LinkedList<GType>) real_lhs_types).peekLast();
		    real_lhs_stype = null;
		    real_lhs_dim = null;
		    if (real_lhs_type instanceof DimensionType) {
			real_lhs_stype = ((DimensionType) real_lhs_type).of();
			real_lhs_dim = ((DimensionType) real_lhs_type).dims();
		    }
		    // if assignment to part of already existing variable
		    IntType[] prev_dims = null;
		    if (curLhs.expr() instanceof DimensionType)
			prev_dims = (IntType[]) ((DimensionType) curLhs.expr()).dims();
		    List<IntType> new_dims = new LinkedList<IntType>();
		    // type of the lhs after the apply
		    // ex a(1:4) is MATRIX[INT[4]] of SCALAR if a MATRIX[INT] of
		    // SCALAR
		    List<IntType> lhs_apply_type_dims = new LinkedList<IntType>();
		    Integer delete_access_position = null;
		    Integer access_all_position = null;
		    boolean unknownLhsApply = false;
		    for (int j = 0; j < params.size(); ++j) {
			AASTNode param = params.get(j);
			if (real_lhs_dim == null || real_lhs_dim.length < j)
			    logger.debug("Matrix " + curLhs.fullname() + ":" + real_lhs_type + " have less than "
				    + (j + 1) + " dimensions adding dimension " + (j + 1));

			if (param.expr().isCastableToBool())
			    new_dims.add(((IntType) GType.get(param.expr())));
			else if (param.expr().equals(BType.SCALAR)) {
			    // cast is needed output a warning
			    statement.compilationUnit().addWarning(new TypeException(
				    ErrorMessage.WARN_ASSIGN_TO_MATRIX_PART_UNSUPPORTED,
				    curLhs.parent(new NodeType[] { NodeType.APPLY, NodeType.ASSIGN, NodeType.GASSIGN }),
				    param.name(), param.expr()));
			    ScalarType stype = (ScalarType) param.expr();
			    new_dims.add(((IntType) stype.castTo(BType.INT)));
			} else if (param.expr().equals(BType.MATRIX_ACCESS_LAST)
				|| param.expr().equals(BType.MATRIX_ACCESS_ALL)) {
			    if (real_lhs_dim == null || real_lhs_dim.length < j)
				new_dims.add((IntType) GType.get(BType.INT)
					.name(TypeUtils.matrixDimName(curLhs.fullname(), j + 1)));
			    else
				new_dims.add((IntType) GType.get(real_lhs_dim[j]));

			    if (param.expr().equals(BType.MATRIX_ACCESS_ALL)) {
				// if all copy it to the dimensions of the lhs
				// after apply
				lhs_apply_type_dims.add(new_dims.get(new_dims.size() - 1));
				access_all_position = j;
			    }
			} else if (param.expr().equals(BType.MATRIX_ACCESS_SLICE)) {
			    // keep the maximum value of the slice as access
			    // dimension
			    SliceType slice = (SliceType) param.expr();
			    Integer size = slice.sequenceSize();
			    if (size != null)
				lhs_apply_type_dims.add((IntType) GType.get(BType.INT, size)
					.name(TypeUtils.matrixDimName(curLhs.fullname(), j + 1)));
			    else
				lhs_apply_type_dims.add((IntType) GType.get(BType.INT)
					.name(TypeUtils.matrixDimName(curLhs.fullname(), j + 1)));

			    new_dims.add(lhs_apply_type_dims.get(lhs_apply_type_dims.size() - 1));
			} else if (param.expr().equals(BType.MATRIX)) {
			    IntType[] dims = ((MatrixType) param.expr()).dims();
			    if (dims.length == 1) {
				IntType cdim = (IntType) GType.get(dims[0]);
				new_dims.add(cdim);
				lhs_apply_type_dims.add(cdim);
			    } else {
				for (IntType dim : TypeUtils.realDims(dims)) {
				    IntType cdim = (IntType) GType.get(dim);
				    new_dims.add(cdim);
				    lhs_apply_type_dims.add(cdim);
				}
			    }
			} else if (param.expr().equals(BType.UNKNOWN)) {
			    // lhsApplyType -> UNKNOWN
			    // lhs type -> the same as before
			    unknownLhsApply = true;
			} else
			    throw new TypeException(ErrorMessage.UNSUPPORTED_ASSIGN_TO_MATRIX_PART,
				    curLhs.parent(new NodeType[] { NodeType.APPLY, NodeType.ASSIGN, NodeType.GASSIGN }),
				    param.name(), param.expr());

			if (!param.expr().equals(BType.MATRIX_ACCESS_ALL))
			    if (delete_access_position == null)
				delete_access_position = j;
			    else if (is_delete)
				throw new UndefinedTranslationException(ErrorMessage.USER_NULL_ASSIGNMENT_ALL_MISSING,
					curLhs.parent(NodeType.APPLY));
		    }

		    if (!unknownLhsApply)
			lhs_dim = TypeUtils.newMatrixDimensions(curLhs, new_dims.toArray(new IntType[new_dims.size()]),
				prev_dims, params.size());
		    else
			// keep the same, nothing can be inferred
			lhs_dim = prev_dims;

		    if (ith_rhs_type.isCastableToMatrix() && (assign_to_myself || is_delete)) {
			List<Integer> general_dims = null;

			if (is_delete) {
			    statement.attr(NodeAttr.DELETE_ASSIGN_MATRIX, true);
			    // delete of a row/column
			    if (access_all_position == null)
				throw new UndefinedTranslationException(ErrorMessage.USER_NULL_ASSIGNMENT_ALL_MISSING,
					curLhs.parent(NodeType.APPLY));
			    // only the rhs dimensions should be dynamic
			    // a[4x4] = a(1:2) --> a[nx4]
			    general_dims = new LinkedList<Integer>();
			    for (int wk = 0; wk < ith_rhs_dim.length; ++wk)
				general_dims.add(wk);
			} else {
			    Object dynid = curLhs.attr(NodeAttr.DYNAMIC_ID);
			    if (dynid instanceof List)
				general_dims = (List<Integer>) dynid;
			    else {
				// case a = a(1:k, 2)
				logger.debug(
					"dynamic ID miss dynamic dimension information, assuming all dynamic up to RHS dimension");
				general_dims = new LinkedList<Integer>();
				for (int wk = 0; wk < ith_rhs_dim.length; ++wk)
				    if (!ith_rhs_dim[wk].hasValue() || ith_rhs_dim[wk].valueAsInt() > 1)
					general_dims.add(wk);
				// if empty -> allones -> assume the first dimension
				if (general_dims.isEmpty())
				    general_dims.add(0);
			    }
			}
			// here if prev_dims contains valued intTypes we can in theory know the full
			// dimension of
			// the new variable. the problem is that in typed languages like C a variable
			// can have a single
			// dimension that never changes.
			// The only possibility is to alter the dimensions deleting the dimension
			// values,
			// This is the standard way to say "hey this matrix is dynamic don't expect
			// fixed dimensions)
			// this should be necessarily dynamic
			IntType[] new_lhs_dim = new IntType[Math.max(lhs_dim.length, ith_rhs_dim.length)];
			for (Integer dn : general_dims) {
			    new_lhs_dim[dn] = (IntType) GType.get(BType.INT)
				    .name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
			}
			int dn = 0;
			// first rhs_dim because this is an assign_to_myself case,
			// so rhs_dim names are coherent with lhs_dim names
			for (; dn < ith_rhs_dim.length; ++dn)
			    if (!general_dims.contains(dn)) {
				new_lhs_dim[dn] = (IntType) GType.get(ith_rhs_dim[dn]);
				if (!new_lhs_dim[dn].hasValue())
				    new_lhs_dim[dn].name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
			    }
			for (; dn < lhs_dim.length; ++dn)
			    if (!general_dims.contains(dn)) {
				new_lhs_dim[dn] = (IntType) GType.get(lhs_dim[dn]);
				if (!new_lhs_dim[dn].hasValue())
				    new_lhs_dim[dn].name(TypeUtils.matrixDimName(curLhs.fullname(), dn + 1));
			    }
			lhs_dim = new_lhs_dim;
		    }

		    // lhsId, lhs_dim);
		    // decide between orginal of type
		    // real_lhs_stype
		    // and new of type
		    // ith_rhs_type
		    GType lhsNewApplyType = null;
		    // type of left-hand side after apply
		    if (unknownLhsApply)
			lhsApplyType = GType.get(BType.UNKNOWN);
		    else if (!lhs_apply_type_dims.isEmpty()) {
			if (real_lhs_stype == null && real_lhs_type.isCastableToScalar())
			    real_lhs_stype = real_lhs_type;
			lhsApplyType = GType.get(BType.MATRIX, curLhs.fullname(), real_lhs_stype,
				lhs_apply_type_dims.toArray(new IntType[lhs_apply_type_dims.size()]));
		    } else if (real_lhs_stype != null)
			lhsApplyType = real_lhs_stype;
		    else
			lhsApplyType = ith_rhs_type;
		    // check for compatibility with RHS,
		    // if not compatible change lhsApplyType
		    if (!unknownLhsApply)
			lhsNewApplyType = checkLHSRHSTypeCompatibility(ith_rhs_type, rhsTp, lhsApplyType,
				!lhs_apply_type_dims.isEmpty(), curLhs);
		    else
			lhsNewApplyType = lhsApplyType;

		    if (lhsNewApplyType == null) {
			TypeException ex = new TypeException(ErrorMessage.UNSUPPORTED_ASSIGN_INCOMPATIBLE_TYPES,
				statement, rhsTp.name(), ith_rhs_type, curLhs.name(), real_lhs_type);
			if (lhs_assigned || rhsTypesIt.hasNext()) {
			    // if we have other types to check, just discard
			    // this one as impossible with a warning
			    if (!curLhs.hasAttr(NodeAttr.USER_WARNING)) {
				statement.compilationUnit().addWarning(ex);
				curLhs.attr(NodeAttr.USER_WARNING, true);
			    }
			    continue;
			} else
			    throw ex;
		    }
		    lhsApplyOfType = getLHSOfType(lhsNewApplyType);
		    // real_lhs_type is a matrix of something for sure
		    // (otherwise params would have been null)
		    if (lhsApplyOfType == null)
			lhsApplyOfType = lhsNewApplyType;
		    // parameters are for a matrix
		    params.get(0).parent(NodeType.FUNCTION_PARAMETER_LIST).attr(NodeAttr.MATRIX_PARAMS, curLhs);
		    // update definition of structure that lhs belongs to if in structure
		    updateLhsType(curLhs, rhsTp,
			    typeOf(curLhs, rhsTp, curLhs.fullname(), real_lhs_type, lhsApplyOfType, lhs_dim), true);

		}
		if (curLhs.parentExists(NodeType.APPLY)) {
		    AASTNode applyNode = curLhs.parent(NodeType.APPLY);
		    applyNode.expr(lhsApplyType, false);
		    applyNode.attr(NodeAttr.MATACCESS, true);
		}

		new_lhs = curLhs;
	    }
	    lhs_assigned = true;
	}
	return new Tuple<AASTNode, String>(new_lhs, ith_rhsId);
    }

    private void updateLhsType(AASTNode curLhs, AASTNode rhs, GType newType) {
	updateLhsType(curLhs, rhs, newType, null);
    }

    private void updateLhsType(AASTNode curLhs, AASTNode rhs, GType newType, Boolean updateall) {

	// handle case a.b.c = xx
	boolean fieldaccess = curLhs.parent().type().equals(NodeType.FIELDACCESS);
	if (fieldaccess) {
	    AASTNode fieldaccessEl = curLhs.parent();
	    AASTNode baseEl = fieldaccessEl.childs().get(0);
	    // --- LOGIC TO DECIDE THE INPUTNESS (FOR LEFT HAND SIDE) --
	    boolean isInput = (baseEl.expr() != null && baseEl.expr().isInput())
		    || (rhs.expr() != null && rhs.expr().isInput());
	    GType undefType = null;
	    if (!isInput) {
		// if last element in structure, check base element of structure
		// if base element is input, the default type is the undefined type
		// that can be UNDEFINED or MATRIX depending on if we found a load before this
		// statement or not
		// if it's not input then it will be the defaultType
		undefType = GType.get(BType.UNKNOWN);
	    } else {
		undefType = TypeUtils.getUndefinedType(curLhs, BType.UNKNOWN);
	    }

	    List<GType> types = TypeUtils.updateStructDefinition(fieldaccessEl, baseEl, curLhs, undefType);
	    if (curLhs.expr() == null)
		// set old type
		curLhs.expr(types.get(0), false);
	    // set new type
	    curLhs.expr(newType, false);
	    // now curLhs.expr() is the correct type we should keep, update structure
	    List<AASTNode> fieldElements = curLhs.parent().childs();
	    // -1 is the curLhs, -2 is a dot, -3 is the structure just before
	    AASTNode prevStruct = fieldElements.get(fieldElements.size() - 3);
	    StructType prevStructType = prevStruct.sexpr();
	    prevStructType.updateField(curLhs.exprs(), curLhs.name());
	} else {
	    if (updateall == null)
		curLhs.expr(newType);
	    else
		curLhs.expr(newType, updateall);
	}

    }

    private void recursivelyUpdateStructFields(StructType parentStruct, List<GType> curLhsExpr, String curLhsName,
	    StructType newType) {
	// it exists because curLhs.expr(..) was called in the calling method
	List<GType> newFieldType = newType.getField(curLhsName);
	// update curLhsName with new type
	parentStruct.updateField(newFieldType, curLhsName);
	GType newFieldTypeV = newFieldType.get(0);
	if (newFieldTypeV.equals(BType.STRUCT)) {
	    // sub-structs from level 1 to level n
	    StructType newStrType = (StructType) newFieldTypeV;
	    // update subfields of curLhsName
	    StructType s = (StructType) parentStruct.getField(curLhsName).get(0);
	    Iterator<Tuple<List<GType>, String>> it = s.iterFields();
	    while (it.hasNext()) {
		Tuple<List<GType>, String> field = it.next();
		// recursively update subtype
		recursivelyUpdateStructFields(s, field.first(), field.second(), newStrType);
	    }
	}
    }

    private GType getLHSOfType(GType lhsNewApplyType) {
	if (lhsNewApplyType.isCastableToMatrix())
	    return ((DimensionType) lhsNewApplyType).of();
	// else if (lhsNewApplyType.isCastableToScalar())
	return null;
    }

    private GType checkLHSRHSTypeCompatibility(GType ith_rhs_type, AASTNode ith_rhs, GType lhsApplyType,
	    // true if we are in this form a(x) = ..., false if we are in this form a = ...
	    boolean lhsApplyDifferentFromLhs,
	    // name of lhs id (a)
	    AASTNode lhsApply) {
	GType lhsNewType = null;

	if (ith_rhs_type.equals(BType.UNKNOWN) || lhsApplyType.canRepresent(ith_rhs_type)) {
	    lhsNewType = lhsApplyType;
	} else if (ith_rhs_type.canRepresent(lhsApplyType)) {
	    lhsNewType = GType.get(ith_rhs_type);
	} else if (lhsApplyType.canBeForcedToRepresent(ith_rhs_type)) {
	    try {
		lhsNewType = lhsApplyType.forceTo(ith_rhs_type);
	    } catch (Exception e) {
		// should never happen, probably a bug in forceTo implementation
		logger.error("ERROR in type assignment");
	    }
	} else if (ith_rhs_type.canBeForcedToRepresent(lhsApplyType)) {
	    try {
		lhsNewType = ith_rhs_type.forceTo(lhsApplyType);
	    } catch (Exception e) {
		// should never happen, probably a bug in forceTo implementation
		logger.error("ERROR in type assignment");
	    }
	} else if (lhsApplyDifferentFromLhs) {
	    // handle cases like a(x) = 1; where x is a matrix, lhs is matrix, rhs is scalar
	    // this will be interpreted as 1 will be set for every cell indexed by matrix x
	    if (lhsApplyType.isCastableToMatrix()) {
		DimensionType dlhsApply = (DimensionType) lhsApplyType;
		GType lhsApplyOf = dlhsApply.of();
		GType newLhsApplyOf = null;
		// check oftype compatibility
		if (lhsApplyOf.canRepresent(ith_rhs_type))
		    newLhsApplyOf = GType.get(lhsApplyOf);
		else if (ith_rhs_type.canRepresent(lhsApplyOf)) {
		    newLhsApplyOf = GType.get(ith_rhs_type);
		} else if (lhsApplyOf.canBeForcedToRepresent(ith_rhs_type)) {
		    try {
			newLhsApplyOf = lhsApplyOf.forceTo(ith_rhs_type);
		    } catch (Exception e) {
			// should never happen, probably a bug in forceTo implementation
		    }
		} else if (ith_rhs_type.canBeForcedToRepresent(lhsApplyOf)) {
		    try {
			newLhsApplyOf = ith_rhs_type.forceTo(lhsApplyOf);
		    } catch (Exception e) {
			// should never happen, probably a bug in forceTo implementation
		    }
		}

		if (newLhsApplyOf != null) {
		    // if compatibility found
		    lhsNewType = GType.get(BType.MATRIX, lhsApply.name(), newLhsApplyOf, dlhsApply.dims());
		}

	    }
	}

	if (lhsNewType != null && ith_rhs_type.equals(BType.STRUCT) && lhsApplyType.equals(BType.STRUCT)) {
	    // update subtypes
	    StructType strType = (StructType) lhsNewType;
	    StructType newType = (StructType) ith_rhs_type;
	    // iterate over all sub-structures and update their type
	    Iterator<Tuple<List<GType>, String>> it = strType.iterFields();
	    while (it.hasNext()) {
		Tuple<List<GType>, String> field = it.next();
		if (field.first().get(0).equals(BType.UNKNOWN))
		    // recursively update type
		    recursivelyUpdateStructFields(strType, field.first(), field.second(), (StructType) newType);
	    }
	}

	// scalars should be generic when they are dynamic
	if (lhsApply.hasAttr(NodeAttr.DYNAMIC_ID) && lhsNewType != null && lhsNewType.isCastableToScalar())
	    ((ValuedType<?>) lhsNewType).deleteValue();

	return lhsNewType;
    }

    /**
     * build a type from the parameters
     * 
     * @param ith_rhs_type
     * @param lhsstype
     * @param lhs_dim
     * @return
     * @throws TypeException
     */
    private GType typeOf(AASTNode lhsIDNode, AASTNode rhsNode, String lhsId, GType ith_rhs_type, GType lhsstype,
	    IntType[] lhs_dim) throws TypeException {
	if (lhsstype == null || lhsstype.equals(BType.UNDEFINED) || lhsstype.equals(BType.UNKNOWN))
	    return GType.get(ith_rhs_type);
	else if (ith_rhs_type instanceof ValuedType) {
	    if (lhs_dim == null)
		return GType.get(ith_rhs_type);
	    else {
		AASTNode originalUnknownNode = (AASTNode) rhsNode.findAttr(NodeAttr.IMPLICITLY_DEFINED);
		if (originalUnknownNode != null)
		    throw new TypeException(ErrorMessage.INTERNAL_WRONG_IMPLICIT_TYPE, rhsNode, ith_rhs_type,
			    rhsNode.code(), originalUnknownNode);
		else
		    throw new TypeException(ErrorMessage.UNSUPPORTED_VALUED_TYPE_WITH_SUB_TYPE, statement, ith_rhs_type,
			    lhsstype);
	    }
	}

	GType mtype;
	switch (ith_rhs_type.type()) {
	case BOOL:
	case SCALAR:
	case INT:
	case VOID:
	case VECTOR:
	case STRUCT:
	case STRING:
	case UNDEFINED:
	case UNKNOWN:
	case MATRIX_ACCESS_ALL:
	case MATRIX_ACCESS_LAST:
	case FUNCTION:
	case MATRIX_ACCESS_SLICE:
	    mtype = GType.get(ith_rhs_type);
	    break;
	case MATRIX:
	    String name = lhsId;
	    if (name == null)
		name = ith_rhs_type.name();
	    mtype = GType.get(BType.MATRIX, name, lhsstype, lhs_dim, ((MatrixType) ith_rhs_type).isSparse());
	    break;
	default:
	    throw new TypeException(ErrorMessage.UNSUPPORTED_VALUED_TYPE_WITH_SUB_TYPE, statement, ith_rhs_type,
		    lhsstype);
	}
	if (ith_rhs_type.isInput())
	    mtype.setInput(ith_rhs_type.inputName());
	return mtype;
    }

    // ith_rhs_dim can be null if it comes from a call to a core function
    // only backend can add the right values
    private IntType[] assignType(GType ith_rhs_type, GType lhsstype, String lhsId, IntType[] ith_rhs_dim)
	    throws UndefinedTranslationException {
	IntType[] lhs_dim = null;
	// remember symbol type only for matrix case
	if (ith_rhs_type != null && ith_rhs_type.equals(BType.MATRIX)) {
	    if (lhsstype == null) {
		throw new UndefinedTranslationException(ErrorMessage.INTERNAL_MATRIX_TYPE_SHOULD_HAVE_DERIVATE_TYPE,
			statement, ith_rhs_type);
	    } else if (lhsstype.equals(BType.SCALAR)) {
		lhs_dim = (ith_rhs_dim == null) ? null : Arrays.copyOf(ith_rhs_dim, ith_rhs_dim.length);
		// optimizer.createMatrixRef(lhsId, lhs_dim);
	    } else if (lhsstype.equals(BType.INT)) {
		lhs_dim = (ith_rhs_dim == null) ? null : Arrays.copyOf(ith_rhs_dim, ith_rhs_dim.length);
		// optimizer.createIndexMatrixRef(lhsId, lhs_dim);
	    } else if (lhsstype.equals(BType.BOOL)) {
		lhs_dim = (ith_rhs_dim == null) ? null : Arrays.copyOf(ith_rhs_dim, ith_rhs_dim.length);
		// optimizer.createBoolMatrixRef(lhsId, lhs_dim);
	    } else
		throw new UndefinedTranslationException(ErrorMessage.INTERNAL_MATRIX_DERIVATE_TYPE_UNKNOWN, statement,
			lhsstype);
	} else if (ith_rhs_type != null && ith_rhs_type.equals(BType.SCALAR)) {
	    lhsstype = GType.get(BType.SCALAR);
	    // optimizer.createScalar(lhsId);
	} else if (ith_rhs_type != null && ith_rhs_type.equals(BType.INT)) {
	    lhsstype = GType.get(BType.INT);
	    // IMPORTANT: also for int create a scalar variable because
	    // later on the same variable can be assigned to a scalar.
	    // (see Gaucher bug!)
	    // optimizer.createIntScalar(lhsId);
	} else if (ith_rhs_type != null && ith_rhs_type.equals(BType.BOOL)) {
	    lhsstype = GType.get(BType.BOOL);
	    // optimizer.createBoolScalar(lhsId);
	} else if (ith_rhs_type != null && ith_rhs_type.equals(BType.STRING)) {
	    lhsstype = GType.get(BType.STRING);
	    // optimizer.createBoolScalar(lhsId);
	} else if (ith_rhs_type != null && ith_rhs_type.equals(BType.STRUCT)) {
	    // optimizer.createStruct(lhsId, ith_rhs_type, lhsstype);
	} else if (ith_rhs_type != null && ith_rhs_type.equals(BType.MATRIX_ACCESS_SLICE)) {
	    // optimizer.createSlice(lhsId, lhsstype);
	    lhs_dim = ith_rhs_dim;
	} else if (GType.get(BType.UNKNOWN).equals(ith_rhs_type) || GType.get(BType.UNDEFINED).equals(ith_rhs_type)) {
	    // both LHS and RHS unknown
	    logger.debug("Assigning unknown type to '" + lhsId + "'");
	} else
	    throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED_ASSIGN_RHS_TYPES, statement, ith_rhs_type);

	return lhs_dim;
    }

    private IntType[] getDimsBasedOnLHSParams(AASTNode matrix, List<AASTNode> params, GType ith_rhs_type)
	    throws TypeException, SyntaxException {
	List<IntType> dims = new ArrayList<IntType>();
	int allp = 0;
	for (int i = 0; i < params.size(); ++i) {
	    AASTNode lhs_param = params.get(i);
	    GType lhs_param_expr = TypeUtils.getExprGeneralized(lhs_param);
	    if (lhs_param_expr.equals(BType.INT)) {
		IntType newParamType = (IntType) GType.get(lhs_param_expr);
		// give name only if doesn't have value
		if (!newParamType.hasValue())
		    newParamType.name(TypeUtils.matrixDimName(matrix, i + 1));
		dims.add(newParamType);
	    } else if (lhs_param_expr.equals(BType.SCALAR)) {
		// cast is needed output a warning
		statement.compilationUnit()
			.addWarning(new TypeException(ErrorMessage.WARN_ASSIGN_TO_MATRIX_PART_UNSUPPORTED,
				matrix.parent(new NodeType[] { NodeType.APPLY, NodeType.ASSIGN, NodeType.GASSIGN }),
				lhs_param.name(), lhs_param_expr));
		IntType newParamType;
		if (!lhs_param_expr.hasValue())
		    newParamType = (IntType) GType.get(BType.INT);
		else
		    newParamType = (IntType) GType.get(BType.INT, ((ScalarType) lhs_param_expr).valueAsInt());
		if (!newParamType.hasValue())
		    newParamType.name(TypeUtils.matrixDimName(matrix, i + 1));
		dims.add(newParamType);
	    } else if (lhs_param_expr.isCastableToMatrix()) {
		DimensionType dtype = (DimensionType) lhs_param_expr;
		IntType[] ddims = TypeUtils.realDims(dtype.dims());
		if (ddims.length > allp) {
		    IntType newParamType = (IntType) GType.get(ddims[allp++]);
		    // give name only if doesn't have value
		    if (!newParamType.hasValue())
			newParamType.name(TypeUtils.matrixDimName(matrix, i + 1));
		    dims.add(newParamType);
		} else
		    throw new SyntaxException(ErrorMessage.UNSUPPORTED, statement);
	    } else if (lhs_param_expr.equals(BType.MATRIX_ACCESS_ALL)) {
		if (ith_rhs_type.isCastableToMatrix()) {
		    DimensionType dtype = (DimensionType) ith_rhs_type;
		    IntType newParamType = (IntType) GType.get(TypeUtils.realDims(dtype.dims())[allp++]);
		    // give name only if doesn't have value
		    if (!newParamType.hasValue())
			newParamType.name(TypeUtils.matrixDimName(matrix, i + 1));
		    dims.add(newParamType);
		} else if (ith_rhs_type.equals(BType.UNKNOWN)) {
		    logger.debug("Unknown right-hand-side, we can postpone lhs dimension inference");
		    dims.add((IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(matrix, i + 1)));
		} else
		    throw new TypeException(ErrorMessage.UNSUPPORTED_ASSIGN_ALL_OF_NONMATRIX,
			    matrix.parent(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN }), ith_rhs_type);
	    } else if (lhs_param_expr.equals(BType.UNDEFINED) || lhs_param_expr.equals(BType.UNKNOWN)) {
		logger.debug("Accessing to undefined symbol with UNKNOWN param " + lhs_param.name() + ":"
			+ lhs_param.expr() + " can't deduce dimension size.");
		dims.add((IntType) GType.get(BType.INT).name(TypeUtils.matrixDimName(matrix, i + 1)));
	    } else
		throw new TypeException(ErrorMessage.UNSUPPORTED_ASSIGN_TO_MATRIX_PART,
			matrix.parent(new NodeType[] { NodeType.APPLY, NodeType.ASSIGN, NodeType.GASSIGN }),
			lhs_param.name(), lhs_param.expr());
	}
	return dims.toArray(new IntType[dims.size()]);
    }

    private IntType getIthSliceColEnd(int i, IntType rhs_cols, int lhs_size) {
	Integer intval = null;
	String strval = null;
	if (lhs_size != 2) {
	    if (rhs_cols.hasValue())
		intval = (i != lhs_size - 1) ? i + 1 : rhs_cols.valueAsInt();

	    strval = (i != lhs_size - 1) ? String.valueOf(i + 1) : rhs_cols.valueAsString();
	} else {
	    if (i != lhs_size - 1)
		intval = 1;
	    else if (rhs_cols.hasValue())
		intval = rhs_cols.valueAsInt();

	    strval = (i != lhs_size - 1) ? "1" : rhs_cols.valueAsString();
	}

	if (intval != null)
	    return (IntType) GType.get(BType.INT, intval);
	else
	    return (IntType) GType.get(BType.INT).name(strval);
    }

    private IntType getIthSliceColStart(int i, IntType rhs_cols, int lhs_size) {
	int intval = 0;
	String strval = null;
	if (lhs_size != 2) {
	    // divide in equally-spaced columns
	    intval = (i == 0) ? 1 : (i * rhs_cols.valueAsInt() / lhs_size) + 1;
	    strval = (i == 0) ? "1" : "(" + i + "*" + rhs_cols.name() + "/" + lhs_size + ")+1";
	} else
	    // if size == 2 divide in two columns
	    intval = (i == 0) ? 1 : 2;
	return (IntType) GType.get(BType.INT, intval).name(strval);
    }

}
