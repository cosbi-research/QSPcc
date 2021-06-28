package eu.cosbi.qspcc.frontend;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.dag.DAGFunctionalListener;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.frontend.RParser.Binary_bin_andContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_bin_orContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_divideContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_double_eqContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_expContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_grtContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_grteContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_log_andContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_log_orContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_lstContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_lsteContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_minusContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_neqContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_plusContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_tildeContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_tilde_rightContext;
import eu.cosbi.qspcc.frontend.RParser.Binary_timesContext;
import eu.cosbi.qspcc.frontend.RParser.BlockContext;
import eu.cosbi.qspcc.frontend.RParser.BreakContext;
import eu.cosbi.qspcc.frontend.RParser.CommentsContext;
import eu.cosbi.qspcc.frontend.RParser.ComplexContext;
import eu.cosbi.qspcc.frontend.RParser.ExprContext;
import eu.cosbi.qspcc.frontend.RParser.ExprlistContext;
import eu.cosbi.qspcc.frontend.RParser.FalseContext;
import eu.cosbi.qspcc.frontend.RParser.FloatContext;
import eu.cosbi.qspcc.frontend.RParser.ForContext;
import eu.cosbi.qspcc.frontend.RParser.FormContext;
import eu.cosbi.qspcc.frontend.RParser.FormlistContext;
import eu.cosbi.qspcc.frontend.RParser.FuncallContext;
import eu.cosbi.qspcc.frontend.RParser.FundefContext;
import eu.cosbi.qspcc.frontend.RParser.GLassignContext;
import eu.cosbi.qspcc.frontend.RParser.GRassignContext;
import eu.cosbi.qspcc.frontend.RParser.HelpContext;
import eu.cosbi.qspcc.frontend.RParser.HexContext;
import eu.cosbi.qspcc.frontend.RParser.IdContext;
import eu.cosbi.qspcc.frontend.RParser.IfContext;
import eu.cosbi.qspcc.frontend.RParser.If_elseContext;
import eu.cosbi.qspcc.frontend.RParser.InfContext;
import eu.cosbi.qspcc.frontend.RParser.IntContext;
import eu.cosbi.qspcc.frontend.RParser.LassignContext;
import eu.cosbi.qspcc.frontend.RParser.Mat_accessContext;
import eu.cosbi.qspcc.frontend.RParser.Mat_access_nameContext;
import eu.cosbi.qspcc.frontend.RParser.Mat_access_singleContext;
import eu.cosbi.qspcc.frontend.RParser.NaContext;
import eu.cosbi.qspcc.frontend.RParser.NanContext;
import eu.cosbi.qspcc.frontend.RParser.NextContext;
import eu.cosbi.qspcc.frontend.RParser.NullContext;
import eu.cosbi.qspcc.frontend.RParser.ParensContext;
import eu.cosbi.qspcc.frontend.RParser.ProgContext;
import eu.cosbi.qspcc.frontend.RParser.RassignContext;
import eu.cosbi.qspcc.frontend.RParser.RepeatContext;
import eu.cosbi.qspcc.frontend.RParser.ScopeContext;
import eu.cosbi.qspcc.frontend.RParser.SequenceContext;
import eu.cosbi.qspcc.frontend.RParser.StringContext;
import eu.cosbi.qspcc.frontend.RParser.SubContext;
import eu.cosbi.qspcc.frontend.RParser.SublistContext;
import eu.cosbi.qspcc.frontend.RParser.TrueContext;
import eu.cosbi.qspcc.frontend.RParser.Unary_minusContext;
import eu.cosbi.qspcc.frontend.RParser.Unary_notContext;
import eu.cosbi.qspcc.frontend.RParser.Unary_plusContext;
import eu.cosbi.qspcc.frontend.RParser.WhileContext;
import eu.cosbi.qspcc.frontend.exceptions.ThrowableErrorListener;

public class RCompilationUnit implements RListener {
    private static final String TAB = "\t";
    private Logger logger = LogManager.getLogger(RCompilationUnit.class);
    private ANTLRInputStream rProgram;
    private RParser.ProgContext ast;
    private CommonTokenStream rtokens;
    private RParser rparser;
    private Program grProgram;
    private String programPath;
    private Path mainPath;
    private StringBuffer sb;
    private Program program;
    private AAST tree;
    private AASTNode root;
    // curparent is set to the current node on enter
    // re-set to the parent node on exit
    // it is responsibility of every "onenter" to re-set the curparent
    // to the AASTNode he is actually creating
    private AASTNode curparent;
    private List<String> subFiles;
    private int curLine;
    private int curColumn;
    private String curFun = null;
    private ArrayList<String> arrayDims = null;
    private ArrayList<AASTNode> arrayNodes = null;
    private boolean writingDims = false;
    private int atNum;
    private boolean deleteOnExit;
    private String[] ignoredFunctions = new String[] { "listToDataFrame" };
    private boolean ignoreNodes;

    public RCompilationUnit(Program p, String programPath) {
	this.program = p;
	this.ignoreNodes = false;
	this.programPath = programPath;
	Path spath = Paths.get(programPath);
	this.mainPath = spath.getParent();
	if (mainPath == null)
	    mainPath = Paths.get(System.getProperty("user.dir"));
	subFiles = new ArrayList<String>();
	atNum = 0;
	prepare();
	rparser = new RParser(rtokens);
	rparser.removeErrorListeners();
	rparser.addErrorListener(ThrowableErrorListener.INSTANCE);
	rparser.setBuildParseTree(true);
    }

    /**
     * prepare folder and input streams for frontend
     * 
     * @param mainPath
     */
    private void prepare() {
	// ---- PREPARE DATA FOR FRONTEND --------
	// create folders if does not exist
	try {
	    if (mainPath != null)
		Files.createDirectories(mainPath);

	    rProgram = new ANTLRInputStream(new FileInputStream(programPath));
	    RLexer lexer = new RLexer(rProgram);
	    rtokens = new CommonTokenStream(lexer);
	    // filter newlines
	    RFilter filter = new RFilter(rtokens);
	    filter.stream(); // call start rule: stream
	    rtokens.reset();

	} catch (IOException e) {
	    logger.fatal("Error loading source file: " + e.getMessage());
	    logger.debug("Error loading source file: " + e.getMessage(), e);
	    System.exit(1);
	}
    }

    public AAST walk(AAST parent) {
	RParser.ProgContext rootContext = rparser.prog();

	// Walk it and attach our listener
	ParseTreeWalker walker = new ParseTreeWalker();
	tree = new AAST(program, parent);
	tree.setSourcePath(programPath);
	root = new AASTNode(tree, NodeType.PROGRAM, rootContext.getText());
	tree.rootNode(root);
	curparent = root;
	walker.walk(this, rootContext);
	System.out.println("---- RESULTING AAST ----");
	try {
	    root.walkOnEnter(new DAGFunctionalListener<AAST, AASTNode>() {

		@Override
		public void on(AAST aast, AASTNode node) throws GException {
		    sb = new StringBuffer();
		    for (int i = 0; i < node.parents().size(); ++i)
			sb.append(TAB);
		    sb.append(node);
		    System.out.println(sb.toString());
		}
	    }, false);
	} catch (Exception e) {
	    logger.error(e.getMessage());
	    logger.debug(e.getMessage(), e);
	}

	return tree;
    }

    /* list of paths to files to be loaded found in this compilation unit */
    public List<String> subFiles() {
	return subFiles;
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
	sb = new StringBuffer();
	for (int i = 0; i < ctx.depth(); ++i)
	    sb.append(TAB);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
	if (curparent == null) {
	    if (!(ctx instanceof ProgContext))
		logger.debug("missing parent for node " + ctx.getText());
	} else if (ctx instanceof BlockContext) {
	    // skip block context. we use only exprlist to add statement_list node
	    return;
	} else if (deleteOnExit) {
	    AASTNode parent = curparent.parent();
	    deleteNode(curparent);
	    curparent = parent;
	    deleteOnExit = false;
	} else
	    curparent = curparent.parent();
	curLine = ctx.getStart().getLine();
	curColumn = ctx.getStart().getCharPositionInLine();
    }

    /**
     * remove the given node from the tree
     * 
     * @param node
     */
    private void deleteNode(AASTNode node) {
	AASTNode parent = node.parent();
	// remove references to curparent
	parent.deleteChild(node);
	node.parent((AASTNode) null);
    }

    @Override
    public void visitErrorNode(ErrorNode arg0) {
    }

    @Override
    public void visitTerminal(TerminalNode t) {
    }

    @Override
    public void enterNext(NextContext ctx) {
	sb.append("next: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode expr_stmt = new AASTNode(tree, NodeType.EXPR_STMT, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(expr_stmt);

	AASTNode expr = new AASTNode(tree, NodeType.EXPRESSION, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), expr_stmt);
	expr_stmt.addChild(expr);

	// next must be defined as globalvariable in R2XFunction.java
	AASTNode id = new AASTNode(tree, NodeType.ID, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), expr);
	expr.addChild(id);

	curparent = id;
    }

    @Override
    public void exitNext(NextContext ctx) {
	curparent = curparent.parent().parent();
	onExitStatement(ctx);
    }

    @Override
    public void enterProg(ProgContext ctx) {
	sb.append("Prog: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode stmt_list = new AASTNode(tree, NodeType.STATEMENT_LIST, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(stmt_list);

	curparent = stmt_list;
    }

    @Override
    public void exitProg(ProgContext ctx) {
    }

    @Override
    public void enterExprlist(ExprlistContext ctx) {
	sb.append("stmt_list: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode global_stmt = new AASTNode(tree, NodeType.STATEMENT_LIST, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(global_stmt);
	curparent = global_stmt;
    }

    @Override
    public void exitExprlist(ExprlistContext ctx) {
    }

    // assignment
    @Override
    public void enterRassign(RassignContext ctx) {
	sb.append("rassign: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode rassign = new AASTNode(tree, NodeType.ASSIGN, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(rassign);
	curparent = rassign;

	onEnterAssign(ctx);
    }

    @Override
    public void exitRassign(RassignContext ctx) {
	// swap childs so that it is equivalent to an lassign
	List<AASTNode> childs = curparent.childs();
	for (AASTNode child : childs)
	    curparent.deleteChild(child);
	Collections.reverse(childs);
	for (AASTNode child : childs)
	    curparent.addChild(child);

	onExitAssign(ctx);
	onExitStatement(ctx);
    }

    @Override
    public void enterLassign(LassignContext ctx) {
	sb.append("lassign: " + ctx.getText());
	logger.trace(sb.toString());

	// if not in a function is a true assignment
	AASTNode lassign = new AASTNode(tree, NodeType.ASSIGN, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(lassign);
	curparent = lassign;

	onEnterAssign(ctx);
    }

    @Override
    public void exitLassign(LassignContext ctx) {
	onExitAssign(ctx);
	onExitStatement(ctx);
    }

    @Override
    public void enterGRassign(GRassignContext ctx) {
	sb.append("grassign: " + ctx.getText());
	logger.trace(sb.toString());
	// if not in a function is a true assignment
	AASTNode lassign = new AASTNode(tree, NodeType.GASSIGN, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(lassign);
	curparent = lassign;

	// TODO Auto-generated method stub
	onEnterAssign(ctx);
    }

    @Override
    public void exitGRassign(GRassignContext ctx) {
	// swap childs so that it is equivalent to an lassign
	List<AASTNode> childs = curparent.childs();
	for (AASTNode child : childs)
	    curparent.deleteChild(child);
	Collections.reverse(childs);
	for (AASTNode child : childs)
	    curparent.addChild(child);

	onExitAssign(ctx);
	onExitStatement(ctx);
    }

    @Override
    public void enterGLassign(GLassignContext ctx) {
	sb.append("grassign: " + ctx.getText());
	logger.trace(sb.toString());

	// if not in a function is a true assignment
	AASTNode lassign = new AASTNode(tree, NodeType.GASSIGN, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(lassign);
	curparent = lassign;

	onEnterAssign(ctx);
    }

    @Override
    public void exitGLassign(GLassignContext ctx) {
	onExitAssign(ctx);
	onExitStatement(ctx);
    }
    // end assignment

    @Override
    public void enterInt(IntContext ctx) {
	sb.append("int: " + ctx.getText());
	logger.trace(sb.toString());
	AASTNode intn;

	intn = new AASTNode(tree, NodeType.INT, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);

	curparent.addChild(intn);
	curparent = intn;
    }

    @Override
    public void exitInt(IntContext ctx) {
	if (arrayNodes != null && curFun != null && !writingDims) {

	    arrayNodes.add(curparent.parent());
	}

    }

    @Override
    public void enterFalse(FalseContext ctx) {
	sb.append("false: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode floatn = new AASTNode(tree, NodeType.FALSE, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(floatn);
	curparent = floatn;
    }

    @Override
    public void exitFalse(FalseContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterTrue(TrueContext ctx) {
	sb.append("true: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode floatn = new AASTNode(tree, NodeType.TRUE, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(floatn);
	curparent = floatn;
    }

    @Override
    public void exitTrue(TrueContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterFloat(FloatContext ctx) {
	sb.append("float: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode floatn = new AASTNode(tree, NodeType.FLOAT, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(floatn);
	curparent = floatn;
    }

    @Override
    public void exitFloat(FloatContext ctx) {
	if (arrayNodes != null && curFun != null && !writingDims) {

	    arrayNodes.add(curparent.parent());
	}

    }

    @Override
    public void enterString(StringContext ctx) {
	sb.append("string: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode id = new AASTNode(tree, NodeType.STRING, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(id);
	curparent = id;
    }

    @Override
    public void exitString(StringContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterId(IdContext ctx) {
	sb.append("id: " + ctx.getText());
	logger.trace(sb.toString());

	boolean toplevel = checkExprStmtNeeded(ctx);

	AASTNode id = new AASTNode(tree, NodeType.ID, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(id);

	if (toplevel)
	    // add newline
	    onExitStatement(ctx);

	curparent = id;
    }

    @Override
    public void exitId(IdContext ctx) {
	onExitExpr(ctx);
    }

    @Override
    public void enterSub(SubContext ctx) {
	sb.append("subExpression: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode expr = new AASTNode(tree, NodeType.EXPRESSION, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(expr);
	curparent = expr;

	SubContext param = ctx;
	AASTNode id = null;
	if (param.ID() == null && param.expr() == null && param.STRING() == null && param.NULL() == null
		&& param.THREEDOTS() == null) {
	    // void expression means a colon
	    sb.append("subExpression: " + ctx.getText());
	    logger.trace(sb.toString());

	    AASTNode colon = new AASTNode(tree, NodeType.COLON, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), expr);
	    curparent.addChild(colon);
	} else if (param.THREEDOTS() != null) {
	    // unknown param list
	    id = new AASTNode(tree, NodeType.THREEDOTS, param.getText(), param.getStart().getLine(),
		    param.getStart().getCharPositionInLine(), curparent);
	    curparent.addChild(id);
	} else if (param.ID() != null || param.STRING() != null || param.NULL() != null) {
	    // unnamed parameter
	    NodeType type = null;
	    if (param.ID() != null)
		type = NodeType.ID;
	    else if (param.STRING() != null)
		type = NodeType.STRING;
	    else if (param.NULL() != null)
		type = NodeType.ID;
	    id = new AASTNode(tree, type, param.getText(), param.getStart().getLine(),
		    param.getStart().getCharPositionInLine(), curparent);
	    curparent.addChild(id);
	    if (param.expr() != null) {
		// named parameter
		ExprContext paramDef = param.expr();
		AASTNode defval = new AASTNode(tree, NodeType.DEFAULT_VALUE, paramDef.getText(),
			paramDef.getStart().getLine(), paramDef.getStart().getCharPositionInLine(), id);
		id.addChild(defval);
		curparent = defval;
	    }
	} else {
	    // case simple expression -> deeper nodes
	}

    }

    @Override
    public void exitSub(SubContext ctx) {
	if (writingDims) {
	    for (AASTNode child : curparent.childs())
		arrayDims.add(child.name());
	}
    }

    @Override
    public void enterParens(ParensContext ctx) {
	sb.append("parens: " + ctx.getText());
	logger.trace(sb.toString());
	AASTNode id = new AASTNode(tree, NodeType.PARENS, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(id);
	curparent = id;

    }

    @Override
    public void exitParens(ParensContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterFuncall(FuncallContext ctx) {

	// if entering fucntions not inside another function, set function name
	// as ctx text
	if (curFun == null)
	    curFun = ctx.getText();

	sb.append("funcall: " + ctx.getText());
	logger.trace(sb.toString());

	boolean toplevel = checkExprStmtNeeded(ctx);

	AASTNode apply;
	// step downward
	if (ctx.ID().getText().equals("array")) {

	    apply = new AASTNode(tree, NodeType.MATRIX, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);

	    arrayDims = new ArrayList<String>();
	    arrayNodes = new ArrayList<AASTNode>();
	} else if (ctx.ID().getText().equals("c")) {

	    if (!curparent.type().equals(NodeType.MATRIX)) {
		apply = new AASTNode(tree, NodeType.MATRIX, ctx.getText(), ctx.getStart().getLine(),
			ctx.getStart().getCharPositionInLine(), curparent);
		AASTNode vector = new AASTNode(tree, NodeType.VECTOR, ctx.getText(), ctx.getStart().getLine(),
			ctx.getStart().getCharPositionInLine(), apply);
		apply.addChild(vector);

	    } else {
		apply = new AASTNode(tree, NodeType.VECTOR, ctx.getText(), ctx.getStart().getLine(),
			ctx.getStart().getCharPositionInLine(), curparent);
	    }

	} else {
	    apply = new AASTNode(tree, NodeType.APPLY, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);
	}
	curparent.addChild(apply);

	AASTNode id = new AASTNode(tree, NodeType.ID, ctx.ID().getText(), ctx.ID().getSymbol().getLine(),
		ctx.ID().getSymbol().getCharPositionInLine(), apply);
	apply.addChild(id);

	if (toplevel)
	    // add newline
	    onExitStatement(ctx);

	curparent = apply;
    }

    @Override
    public void exitFuncall(FuncallContext ctx) {
	// if exiting from first called function, set function call to null
	if (curFun.equals(ctx.getText()))
	    curFun = null;

	AASTNode id = curparent.child(NodeType.ID);
	String functionName = id.name();
	// if we are sourcing -> add new file to be parsed to subFiles
	if ("source".equals(functionName)) {
	    for (AASTNode param : curparent.childs(NodeType.FUNCTION_PARAMETER_LIST)) {
		while (param.type().equals(NodeType.EXPRESSION))
		    param = param.childs().get(0);
		if (param.type().equals(NodeType.STRING))
		    subFiles.add(Paths.get(mainPath.toString(), param.name().substring(1, param.name().length() - 1))
			    .toString());
	    }

	    // if id is c, this is a vector
	} else if ("c".equals(functionName)) {
	    AASTNode parent = curparent.parent();

	    AASTNode matrix = new AASTNode(tree, NodeType.MATRIX, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), parent);
	    parent.addChild(matrix);
	    AASTNode vector = new AASTNode(tree, NodeType.VECTOR, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), matrix);
	    matrix.addChild(vector);
	    for (AASTNode param : curparent.childs(NodeType.FUNCTION_PARAMETER_LIST)) {
		param.parent(vector);
		vector.addChild(param);
	    }

	    // curparent = parent;

	    parent.deleteChild(id.parent());
	} else if (ctx.ID().getText().equals("array")) {

	    int[] dims = new int[arrayDims.size()];

	    for (int i = 0; i < arrayDims.size(); i++) {
		dims[i] = Integer.parseInt(arrayDims.get(i));
	    }

	    for (AASTNode child : curparent.childs()) {
		curparent.deleteChild(child);

	    }
	    int count = 0;
	    SublistContext subctx = ctx.sublist();
	    for (int i = 0; i < dims[0]; i++) {
		AASTNode forr = new AASTNode(tree, NodeType.VECTOR, subctx.getText(), subctx.getStart().getLine(),
			subctx.getStart().getCharPositionInLine(), curparent);
		for (int j = 0; j < dims[1]; j++) {
		    arrayNodes.get(i + j * dims[1]).parent(forr);
		    forr.addChild(arrayNodes.get(i + j * dims[1]));
		    count++;
		}
		curparent.addChild(forr);
	    }

	    AASTNode exp = new AASTNode(tree, NodeType.EXPRESSION, subctx.getText(), subctx.getStart().getLine(),
		    subctx.getStart().getCharPositionInLine(), curparent.parent());
	    curparent.parent().addChild(exp);
	    // AASTNode parent = curparent.parent();
	    curparent.parent(exp);
	    curparent = exp;
	    exp.addChild(curparent.parent().child(NodeType.MATRIX));
	    curparent.parent().deleteChild(curparent.parent().child(NodeType.MATRIX));

	    arrayNodes = null;

	    arrayDims = null;
	}

	onExitExpr(ctx);
    }

    @Override
    public void enterMat_access(Mat_accessContext ctx) {
	sb.append("mataccess: " + ctx.getText());
	logger.trace(sb.toString());

	// rosario told to disable this until list type is implemented
	if (!ignoreNodes)
	    rparser.notifyErrorListeners(ctx.getStart(), "list type not implemented",
		    new RecognitionException("list type not implemented", null, null, ctx));

	boolean toplevel = checkExprStmtNeeded(ctx);
	AASTNode apply = new AASTNode(tree, NodeType.APPLY, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	apply.attr(NodeAttr.MATACCESS, true);
	curparent.addChild(apply);
	if (toplevel)
	    // add newline
	    onExitStatement(ctx);
	curparent = apply;

    }

    @Override
    public void exitMat_access(Mat_accessContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterMat_access_single(Mat_access_singleContext ctx) {
	sb.append("mataccess_single: " + ctx.getText());
	logger.trace(sb.toString());

	boolean toplevel = checkExprStmtNeeded(ctx);
	AASTNode apply = new AASTNode(tree, NodeType.APPLY, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	apply.attr(NodeAttr.MATSLICE, true);
	curparent.addChild(apply);
	if (toplevel)
	    // add newline
	    onExitStatement(ctx);

	curparent = apply;
    }

    @Override
    public void exitMat_access_single(Mat_access_singleContext ctx) {
    }

    @Override
    public void enterMat_access_name(Mat_access_nameContext ctx) {
	sb.append("mataccess_name: " + ctx.getText());
	logger.trace(sb.toString());
	// this is a struct

	boolean toplevel = checkExprStmtNeeded(ctx);
	AASTNode fieldaccess = new AASTNode(tree, NodeType.FIELDACCESS, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);

	curparent.addChild(fieldaccess);
	if (toplevel)
	    // add newline
	    onExitStatement(ctx);

	curparent = fieldaccess;

    }

    @Override
    public void exitMat_access_name(Mat_access_nameContext ctx) {
	if (!curparent.type().equals(NodeType.FIELDACCESS)) {
	    if (!ignoreNodes)
		rparser.notifyErrorListeners(ctx.getStart(), "Wrong exit node",
			new RecognitionException("wrong exit node", null, null, ctx));
	    return;
	}
	// add DOT node in the middle
	curparent.addChild(new AASTNode(tree, NodeType.DOT, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent), 1);
    }

    @Override
    public void enterFor(ForContext ctx) {
	sb.append("for: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode forr = new AASTNode(tree, NodeType.FOR, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(forr);

	AASTNode id = new AASTNode(tree, NodeType.ID, ctx.ID().getText(), ctx.ID().getSymbol().getLine(),
		ctx.ID().getSymbol().getCharPositionInLine(), forr);
	forr.addChild(id);

	curparent = forr;

    }

    @Override
    public void exitFor(ForContext ctx) {
	// add EXPRESSION/STATEMENT LIST
	AASTNode id = curparent.childs().get(0);
	AASTNode expr = curparent.childs().get(1);
	AASTNode stmts = curparent.childs().get(2);
	AASTNode newExpr = null;
	AASTNode newStmts = null;

	if (!expr.type().equals(NodeType.EXPRESSION) || !stmts.type().equals(NodeType.STATEMENT_LIST)) {
	    curparent.deleteChild(expr);
	    curparent.deleteChild(stmts);
	}
	curparent.addChild(id);

	if (!expr.type().equals(NodeType.EXPRESSION)) {
	    newExpr = new AASTNode(tree, NodeType.EXPRESSION, expr.code(), expr.lineNumber(), expr.colNumber(),
		    curparent);
	    expr.parent(newExpr);
	    newExpr.addChild(expr);
	} else
	    newExpr = expr;
	curparent.addChild(newExpr);

	if (!stmts.type().equals(NodeType.STATEMENT_LIST)) {
	    newStmts = new AASTNode(tree, NodeType.STATEMENT_LIST, stmts.code(), stmts.lineNumber(), stmts.colNumber(),
		    curparent);
	    stmts.parent(newStmts);
	    newStmts.addChild(stmts);
	} else
	    newStmts = stmts;
	curparent.addChild(newStmts);
    }

    @Override
    public void enterWhile(WhileContext ctx) {
	sb.append("while: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode whi = new AASTNode(tree, NodeType.WHILE, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(whi);
	curparent = whi;

    }

    @Override
    public void exitWhile(WhileContext ctx) {
	exitConditional(ctx);
    }

    @Override
    public void enterFundef(FundefContext ctx) {
	sb.append("fundef: " + ctx.getText());
	logger.trace(sb.toString());
	AASTNode fundef = null;
	String functionName = null;
	ParserRuleContext parentCtx = ctx.getParent();
	ExprContext ectx = null;

	if (parentCtx instanceof LassignContext || parentCtx instanceof GLassignContext
		|| parentCtx instanceof RassignContext || parentCtx instanceof GRassignContext) {
	    // named function
	    if (parentCtx instanceof LassignContext) {
		ectx = ((LassignContext) parentCtx).expr(0);
		functionName = ectx.getText();
	    } else if (parentCtx instanceof GLassignContext) {
		ectx = ((GLassignContext) parentCtx).expr(0);
		functionName = ectx.getText();
	    } else if (parentCtx instanceof RassignContext) {
		ectx = ((RassignContext) parentCtx).expr(1);
		functionName = ectx.getText();
	    } else if (parentCtx instanceof GRassignContext) {
		ectx = ((GRassignContext) parentCtx).expr(1);
		functionName = ectx.getText();
	    } else if (!ignoreNodes)
		rparser.notifyErrorListeners(ctx.getStart(), "Code error",
			new RecognitionException("Code error", null, null, ctx));
	} else {
	    // unnamed function
	}
	if (functionName == null)
	    functionName = "at_" + atNum++;
	if (ectx == null)
	    ectx = ctx;

	NodeType functionType = NodeType.FUNCTION;
	for (String ignoredFunction : ignoredFunctions)
	    if (ignoredFunction.equals(functionName)) {
		// avoid that this function is saved in the list of program functions.
		// at assign level this check will throw this function away
		functionType = NodeType.AT;
		ignoreNodes = true;
	    }

	fundef = new AASTNode(tree, functionType, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(fundef);
	AASTNode id = new AASTNode(tree, NodeType.ID, functionName, ectx.getStart().getLine(),
		ectx.getStart().getCharPositionInLine(), fundef);
	fundef.addChild(id);
	AASTNode funret = new AASTNode(tree, NodeType.FUNCTION_RETURN, functionName + "_out", ctx.getStop().getLine(),
		ctx.getStop().getCharPositionInLine(), fundef);
	fundef.addChild(funret);
	id = new AASTNode(tree, NodeType.ID, functionName + "_out", ctx.getStop().getLine(),
		ctx.getStop().getCharPositionInLine(), funret);
	funret.addChild(id);

	curparent = fundef;
    }

    @Override
    public void exitFundef(FundefContext ctx) {
	if (!curparent.type().equals(NodeType.FUNCTION)) {
	    if (!ignoreNodes)
		rparser.notifyErrorListeners(ctx.getStart(), "Wrong exit node",
			new RecognitionException("wrong exit node", null, null, ctx));
	    return;
	}
    }

    @Override
    public void enterRepeat(RepeatContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitRepeat(RepeatContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterComplex(ComplexContext ctx) {

    }

    @Override
    public void exitComplex(ComplexContext ctx) {
	if (arrayNodes != null && curFun != null && !writingDims) {
	    arrayNodes.add(curparent.parent());
	}
    }

    @Override
    public void enterScope(ScopeContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitScope(ScopeContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBlock(BlockContext ctx) {
    }

    @Override
    public void exitBlock(BlockContext ctx) {

    }

    @Override
    public void enterHex(HexContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitHex(HexContext ctx) {
	if (arrayNodes != null && curFun != null && !writingDims) {
	    arrayNodes.add(curparent.parent());
	}
    }

    @Override
    public void enterNan(NanContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitNan(NanContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterIf(IfContext ctx) {
	sb.append("if: " + ctx.getText());
	logger.trace(sb.toString());
	AASTNode iff;
	if (!(ctx.getParent() instanceof If_elseContext) && !(ctx.getParent() instanceof If_elseContext)) {
	    iff = new AASTNode(tree, NodeType.IF, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);
	} else {
	    iff = new AASTNode(tree, NodeType.ELSEIF, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);
	}

	curparent.addChild(iff);
	curparent = iff;
    }

    @Override
    public void exitIf(IfContext ctx) {
	exitConditional(ctx);
    }

    @Override
    public void enterInf(InfContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitInf(InfContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterComments(CommentsContext ctx) {
	sb.append("comment: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode comment = new AASTNode(tree, NodeType.LINECOMMENT, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(comment);
	curparent = comment;
    }

    @Override
    public void exitComments(CommentsContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterFormlist(FormlistContext ctx) {
	FormlistContext paramsCtx = ctx;
	AASTNode funpara = new AASTNode(tree, NodeType.PARAMETER_LIST, paramsCtx.getText(),
		paramsCtx.getStart().getLine(), paramsCtx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(funpara);
	curparent = funpara;
    }

    @Override
    public void exitFormlist(FormlistContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBreak(BreakContext ctx) {
	sb.append("break: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode expr_stmt = new AASTNode(tree, NodeType.EXPR_STMT, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(expr_stmt);

	AASTNode expr = new AASTNode(tree, NodeType.EXPRESSION, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), expr_stmt);
	expr_stmt.addChild(expr);

	// next must be defined as globalvariable in R2XFunction.java
	AASTNode id = new AASTNode(tree, NodeType.ID, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), expr);
	expr.addChild(id);

	curparent = id;
    }

    @Override
    public void exitBreak(BreakContext ctx) {
	// TODO Auto-generated method stub
	curparent = curparent.parent().parent();
	onExitStatement(ctx);
    }

    @Override
    public void enterHelp(HelpContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitHelp(HelpContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterSequence(SequenceContext ctx) {
	sb.append("sequence: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode colon = new AASTNode(tree, NodeType.COLON, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(colon);
	curparent = colon;
    }

    @Override
    public void exitSequence(SequenceContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterNa(NaContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitNa(NaContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterNull(NullContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void exitNull(NullContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterForm(FormContext ctx) {
	FormContext param = ctx;
	AASTNode id = null;
	if (param.expr() != null) {
	    // named parameter
	    id = new AASTNode(tree, NodeType.ID, param.getText(), param.getStart().getLine(),
		    param.getStart().getCharPositionInLine(), curparent);
	    curparent.addChild(id);
	    ExprContext paramDef = param.expr();
	    AASTNode defval = new AASTNode(tree, NodeType.DEFAULT_VALUE, paramDef.getText(),
		    paramDef.getStart().getLine(), paramDef.getStart().getCharPositionInLine(), id);
	    id.addChild(defval);
	} else if (param.ID() != null) {
	    // unnamed parameter
	    id = new AASTNode(tree, NodeType.ID, param.getText(), param.getStart().getLine(),
		    param.getStart().getCharPositionInLine(), curparent);
	    curparent.addChild(id);
	} else if (param.THREEDOTS() != null) {
	    // unknown param list
	    id = new AASTNode(tree, NodeType.THREEDOTS, param.getText(), param.getStart().getLine(),
		    param.getStart().getCharPositionInLine(), curparent);
	    curparent.addChild(id);
	}
	curparent = id;
    }

    @Override
    public void exitForm(FormContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterSublist(SublistContext ctx) {
	// add function_parameter_list
	sb.append("sublist: " + ctx.getText());
	logger.trace(sb.toString());

	AASTNode id = new AASTNode(tree, NodeType.FUNCTION_PARAMETER_LIST, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(id);
	curparent = id;
    }

    @Override
    public void exitSublist(SublistContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterIf_else(If_elseContext ctx) {
	sb.append("ifelse: " + ctx.getText());
	logger.trace(sb.toString());

	List expr = ctx.expr();
	AASTNode iff;
	if (!(ctx.getParent() instanceof If_elseContext) && !(ctx.getParent() instanceof If_elseContext)) {
	    iff = new AASTNode(tree, NodeType.IF, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);
	} else {
	    iff = new AASTNode(tree, NodeType.ELSEIF, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);
	}

	curparent.addChild(iff);
	curparent = iff;
    }

    @Override
    public void exitIf_else(If_elseContext ctx) {

	AASTNode condition = curparent.childs().get(0);
	AASTNode statement = curparent.childs().get(1);
	AASTNode elseStm = curparent.childs().get(2);

	curparent.deleteChild(condition);
	curparent.deleteChild(statement);
	curparent.deleteChild(elseStm);

	AASTNode newExpr = new AASTNode(tree, NodeType.EXPRESSION, condition.code(), condition.lineNumber(),
		condition.colNumber(), curparent);
	condition.parent(newExpr);
	newExpr.addChild(condition);
	curparent.addChild(newExpr);

	AASTNode newStmts = null;
	if (!statement.type().equals(NodeType.STATEMENT_LIST)) {
	    newStmts = new AASTNode(tree, NodeType.STATEMENT_LIST, statement.code(), statement.lineNumber(),
		    statement.colNumber(), curparent);
	    statement.parent(newStmts);
	    newStmts.addChild(statement);
	} else
	    newStmts = statement;
	curparent.addChild(newStmts);

	AASTNode elseNode = null;

	if (!(ctx.expr().get(2) instanceof If_elseContext)) {

	    elseNode = new AASTNode(tree, NodeType.ELSE, ctx.expr().get(2).getText(), elseStm.lineNumber(),
		    elseStm.colNumber(), curparent);

	}

	if (elseNode != null) {
	    elseStm.parent(elseNode);
	    elseNode.addChild(elseStm);
	    curparent.addChild(elseNode);
	} else {
	    elseStm.parent(curparent);
	    curparent.addChild(elseStm);
	}

    }

    // NEWLINE is needed at the end of a statement
    // add it
    private void onExitStatement(ExprContext ctx) {
	curparent.addChild(new AASTNode(tree, NodeType.NL, "", 0, 0, curparent));
    }

    private void onEnterAssign(ExprContext ctx) {
	if (ctx.getText().contains("dim") && curFun != null) {
	    writingDims = true;
	}
    }

    private void onExitAssign(ExprContext ctx) {
	if (!curparent.type().equals(NodeType.ASSIGN) && !curparent.type().equals(NodeType.GASSIGN)) {
	    if (!ignoreNodes)
		rparser.notifyErrorListeners(ctx.getStart(), "Wrong exit node",
			new RecognitionException("wrong exit node", null, null, ctx));
	    return;
	}
	List<AASTNode> childs = curparent.childs();
	for (AASTNode child : childs)
	    curparent.deleteChild(child);

	if (childs.size() > 2)
	    logger.warn("Error on assignment: " + ctx.getText() + "\nExtra nodes skipped.");

	AASTNode lhsNode = childs.get(0);
	AASTNode rhsNode = childs.get(1);

	AASTNode parent = curparent.parent();
	if (curFun != null) {
	    lhsNode.parent(parent);
	    parent.addChild(lhsNode);
	    if (parent.name().contains("dim")) {
		writingDims = false;
	    }
	    AASTNode def = new AASTNode(tree, NodeType.DEFAULT_VALUE, lhsNode.code(), lhsNode.lineNumber(),
		    lhsNode.colNumber(), lhsNode);
	    lhsNode.addChild(def);
	    rhsNode.parent(def);
	    def.addChild(rhsNode);
	    parent.deleteChild(curparent);

	} else if (rhsNode.type().equals(NodeType.FUNCTION)) {
	    // add rhs (the full function definition) to childs
	    rhsNode.parent(parent);
	    parent.addChild(rhsNode);
	    // delete this assign on exit
	    deleteOnExit = true;
	} else if (rhsNode.type().equals(NodeType.AT)) {
	    // AT used to avoid that this function is saved in the list of program
	    // functions.
	    // at assign level this is like a flag to throw this function away
	    rhsNode.parent((AASTNode) null);
	    ignoreNodes = false;
	    // delete this assign on exit
	    deleteOnExit = true;
	} else {
	    if (lhsNode.type().equals(NodeType.ASSIGN) || lhsNode.type().equals(NodeType.GASSIGN))
		lhsNode = rightMostNonAssign(lhsNode, true);

	    AASTNode lhs = new AASTNode(tree, NodeType.LHS, lhsNode.code(), lhsNode.lineNumber(), lhsNode.colNumber(),
		    curparent);
	    // change lhs parent
	    lhsNode.parent(lhs);
	    lhs.addChild(lhsNode);
	    curparent.addChild(lhs);

	    if (rhsNode.type().equals(NodeType.ASSIGN) || rhsNode.type().equals(NodeType.GASSIGN))
		rhsNode = rightMostNonAssign(rhsNode, false);

	    AASTNode rhs = new AASTNode(tree, NodeType.RHS, rhsNode.code(), rhsNode.lineNumber(), rhsNode.colNumber(),
		    curparent);
	    // change lhs parent
	    rhsNode.parent(rhs);
	    rhs.addChild(rhsNode);
	    curparent.addChild(rhs);
	}
    }

    /**
     * resolve case a <- b <- c -> d
     * 
     * @param lhsNode
     * @return
     */
    private AASTNode rightMostNonAssign(AASTNode node, boolean rhs) {
	if (!node.type().equals(NodeType.ASSIGN) && !node.type().equals(NodeType.GASSIGN)) {
	    // a copy of the node! avoid the same node being referenced as child of two
	    // assign
	    return new AASTNode(node);
	} else {
	    AASTNode rhsNode = null;
	    if (rhs)
		rhsNode = node.childs(NodeType.RHS).get(0);
	    else
		rhsNode = node.childs(NodeType.LHS).get(0);

	    // first non-assign rhs node
	    AASTNode retNode = rightMostNonAssign(rhsNode, rhs);
	    // change parent of this assign to the statement_list
	    AASTNode stmt_list = node.parent(NodeType.STATEMENT_LIST);
	    // used to get the position where this assign should be inserted (just after
	    // this)
	    AASTNode topAssign = node.parentBefore(NodeType.STATEMENT_LIST);
	    AASTNode parent = node.parent();
	    parent.deleteChild(node);
	    node.parent(stmt_list);
	    int position = stmt_list.childs().indexOf(topAssign);
	    stmt_list.addChild(node, position + 1);
	    return retNode;
	}
    }

    @Override
    public void enterBinary_minus(Binary_minusContext ctx) {
	onEnterBinary(ctx, NodeType.MINUS, "minus");
    }

    @Override
    public void exitBinary_minus(Binary_minusContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    @Override
    public void enterBinary_lst(Binary_lstContext ctx) {
	onEnterBinary(ctx, NodeType.LST, "lst");

    }

    @Override
    public void exitBinary_lst(Binary_lstContext ctx) {
    }

    @Override
    public void enterUnary_minus(Unary_minusContext ctx) {
	onEnterBinary(ctx, NodeType.NEG, "negative");

    }

    @Override
    public void exitUnary_minus(Unary_minusContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    @Override
    public void enterBinary_tilde(Binary_tildeContext ctx) {
	if (!ignoreNodes)
	    rparser.notifyErrorListeners(ctx.start, "Formula objects not implemented yet.",
		    new RecognitionException(ctx.getText(), null, null, ctx));
    }

    @Override
    public void exitBinary_tilde(Binary_tildeContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterUnary_plus(Unary_plusContext ctx) {
	onEnterBinary(ctx, NodeType.PLUS, "unary_plus");

    }

    @Override
    public void exitUnary_plus(Unary_plusContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    @Override
    public void enterBinary_exp(Binary_expContext ctx) {
	onEnterBinary(ctx, NodeType.EXP, "exp");

    }

    @Override
    public void exitBinary_exp(Binary_expContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    @Override
    public void enterBinary_log_and(Binary_log_andContext ctx) {
	onEnterBinary(ctx, NodeType.LOG_AND, "log_and");

    }

    @Override
    public void exitBinary_log_and(Binary_log_andContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_lste(Binary_lsteContext ctx) {
	onEnterBinary(ctx, NodeType.LSTE, "lste");

    }

    @Override
    public void exitBinary_lste(Binary_lsteContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_times(Binary_timesContext ctx) {
	onEnterBinary(ctx, NodeType.TIMES, "times");

    }

    @Override
    public void exitBinary_times(Binary_timesContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    @Override
    public void enterBinary_bin_and(Binary_bin_andContext ctx) {
	onEnterBinary(ctx, NodeType.BIN_AND, "bin_and");

    }

    @Override
    public void exitBinary_bin_and(Binary_bin_andContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_double_eq(Binary_double_eqContext ctx) {
	onEnterBinary(ctx, NodeType.DOUBLE_EQ, "double_eq");
    }

    @Override
    public void exitBinary_double_eq(Binary_double_eqContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_bin_or(Binary_bin_orContext ctx) {
	onEnterBinary(ctx, NodeType.BIN_OR, "bin_or");

    }

    @Override
    public void exitBinary_bin_or(Binary_bin_orContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_tilde_right(Binary_tilde_rightContext ctx) {
	if (!ignoreNodes)
	    rparser.notifyErrorListeners(ctx.start, "Formula objects not implemented yet.",
		    new RecognitionException(ctx.getText(), null, null, ctx));
    }

    @Override
    public void exitBinary_tilde_right(Binary_tilde_rightContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_plus(Binary_plusContext ctx) {
	onEnterBinary(ctx, NodeType.PLUS, "plus");
    }

    @Override
    public void exitBinary_plus(Binary_plusContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    @Override
    public void enterBinary_grte(Binary_grteContext ctx) {
	onEnterBinary(ctx, NodeType.GRTE, "grte");
    }

    @Override
    public void exitBinary_grte(Binary_grteContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_neq(Binary_neqContext ctx) {
	onEnterBinary(ctx, NodeType.NEQ, "neq");

    }

    @Override
    public void exitBinary_neq(Binary_neqContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterUnary_not(Unary_notContext ctx) {

    }

    @Override
    public void exitUnary_not(Unary_notContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_log_or(Binary_log_orContext ctx) {
	onEnterBinary(ctx, NodeType.LOG_OR, "log_or");

    }

    @Override
    public void exitBinary_log_or(Binary_log_orContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_grt(Binary_grtContext ctx) {
	onEnterBinary(ctx, NodeType.GRT, "grt");

    }

    @Override
    public void exitBinary_grt(Binary_grtContext ctx) {
	// TODO Auto-generated method stub

    }

    @Override
    public void enterBinary_divide(Binary_divideContext ctx) {
	onEnterBinary(ctx, NodeType.LEFTDIV, "divide");

    }

    @Override
    public void exitBinary_divide(Binary_divideContext ctx) {
	onExitBinaryNumericExpr(ctx);
    }

    private void onExitBinaryNumericExpr(ExprContext ctx) {
	onExitExpr(ctx);
    }

    private void onExitExpr(ExprContext ctx) {
	if (curparent.parent().type().equals(NodeType.EXPR_STMT))
	    curparent = curparent.parent();
    }

    private void onEnterBinary(ExprContext ctx, NodeType nodeType, String typeLabel) {
	sb.append(typeLabel + ": " + ctx.getText());
	logger.trace(sb.toString());

	boolean toplevel = checkExprStmtNeeded(ctx);
	AASTNode node = new AASTNode(tree, nodeType, ctx.getText(), ctx.getStart().getLine(),
		ctx.getStart().getCharPositionInLine(), curparent);
	curparent.addChild(node);

	if (toplevel)
	    // add newline
	    onExitStatement(ctx);

	curparent = node;
    }

    private boolean checkExprStmtNeeded(ExprContext ctx) {
	boolean toplevel = curparent.type().equals(NodeType.STATEMENT_LIST);
	AASTNode expr_stmt;
	if (toplevel) {
	    // add extra node expr_statement
	    expr_stmt = new AASTNode(tree, NodeType.EXPR_STMT, ctx.getText(), ctx.getStart().getLine(),
		    ctx.getStart().getCharPositionInLine(), curparent);
	    curparent.addChild(expr_stmt);
	    curparent = expr_stmt;
	}
	return toplevel;
    }

    private void exitConditional(ExprContext ctx) {
	// add EXPRESSION/STATEMENT LIST
	AASTNode expr = curparent.childs().get(0);
	AASTNode stmts = curparent.childs().get(1);
	AASTNode newExpr = null;
	AASTNode newStmts = null;

	if (!expr.type().equals(NodeType.EXPRESSION) || !stmts.type().equals(NodeType.STATEMENT_LIST)) {
	    curparent.deleteChild(expr);
	    curparent.deleteChild(stmts);
	}

	if (!expr.type().equals(NodeType.EXPRESSION)) {
	    newExpr = new AASTNode(tree, NodeType.EXPRESSION, expr.code(), expr.lineNumber(), expr.colNumber(),
		    curparent);
	    expr.parent(newExpr);
	    newExpr.addChild(expr);
	} else
	    newExpr = expr;
	curparent.addChild(newExpr);

	if (!stmts.type().equals(NodeType.STATEMENT_LIST)) {
	    newStmts = new AASTNode(tree, NodeType.STATEMENT_LIST, stmts.code(), stmts.lineNumber(), stmts.colNumber(),
		    curparent);
	    stmts.parent(newStmts);
	    newStmts.addChild(stmts);
	} else
	    newStmts = stmts;
	curparent.addChild(newStmts);
    }

}
