package eu.cosbi.qspcc.frontend;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.RewriteEmptyStreamException;
import org.antlr.runtime.tree.Tree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.matlab.antlr.matlabLexer;
import eu.cosbi.matlab.antlr.matlabParser;
import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.frontend.errors.MatlabParsingErrorMessage;

/**
 * Contains ANTLR parser/tokenizer to get the language-specific AST, generalizes
 * it to Abstract-AST and register a CodeBlock instance as a listener for the
 * tree walk.
 * 
 * The CodeBlock instance owns a backend instance to ask for translations
 * 
 * @author tomasoni
 *
 */
public class MatlabCompilationUnit {
    private Logger logger = LogManager.getLogger(MatlabCompilationUnit.class);

    private CommonTree tree;
    private GenericMatlabTree root;
    private TokenRewriteStream tokens;

    private Set<String> fundefs = new HashSet<String>();
    private Set<String> vardefs = new HashSet<String>();
    private Set<String> funcalls = new HashSet<String>();
    private String tmpvar = "";
    // true to remember that before ( in a parent) I've found an apply
    boolean apply = false;
    // true to remember that before ( in a parent) I've found a function
    boolean function = false;
    // true to remember that before ( in a parent) I've found a variable
    boolean LHS = false;
    // true to remember that before ( in a parent) I've found function
    // parameters
    boolean funparams = false;

    private ANTLRFileStream matlabProgram;
    protected Map<String, Set<AASTNode>> externalRequirements;

    private Path basePath;
    private List<Path> basePaths;
    private String mainScript;

    private Program program;
    private matlabParser parser;
    private String error;

    public MatlabCompilationUnit(Program p, String matlabProgramPath) {
	this.program = p;
	externalRequirements = new LinkedHashMap<String, Set<AASTNode>>();
	Path spath = Paths.get(matlabProgramPath);
	this.basePath = spath.getParent();
	if (basePath == null)
	    basePath = Paths.get(System.getProperty("user.dir"));
	basePaths = new LinkedList<Path>();
	basePaths.add(basePath);
	this.mainScript = matlabProgramPath;
	prepare(basePath);
	matlabLexer lex = new matlabLexer(matlabProgram);
	tokens = new TokenRewriteStream(lex);

	parser = new matlabParser(tokens);
	matlabParser.program_return treeGetter = null;

	error = null;
	try {
	    treeGetter = parser.program();
	    tree = (CommonTree) treeGetter.getTree();
	} catch (RecognitionException | RewriteEmptyStreamException e) {
	    error = e.getMessage();
	}
    }

    public boolean hasUnrecoverableSyntaxError() {
	return error != null;
    }

    public String syntaxError() {
	if (error != null || !parser.getErrors().isEmpty()) {
	    StringBuffer sb = new StringBuffer();
	    if (error != null || parser.getErrors().size() > 1)
		sb.append("Multiple errors parsing " + mainScript + ":\r\n\r\n");
	    if (error != null)
		sb.append(error + "\r\n");
	    for (String err : parser.getErrors())
		sb.append(err + "\r\n");

	    return sb.toString();
	}
	return error;
    }

    public Map<String, Set<AASTNode>> subFiles() {
	return externalRequirements;
    }

    /**
     * prepare folder and input streams for frontend
     * 
     * @param mainPath
     */
    private void prepare(Path mainPath) {
	// ---- PREPARE DATA FOR FRONTEND --------
	// create folders if does not exist
	try {
	    if (mainPath != null)
		Files.createDirectories(mainPath);

	    matlabProgram = new ANTLRFileStream(mainScript);
	} catch (IOException e) {
	    logger.fatal("Error loading source file: " + e.getMessage());
	    logger.debug("Error loading source file: " + e.getMessage(), e);
	    System.exit(1);
	}
    }

    public AAST walk(AAST parent) throws SyntaxException, ListException {
	MatlabAAST gtree = new MatlabAAST(program, parent);
	gtree.setSourcePath(mainScript);
	String nodeStatement = getCurrentChildText(tree, null);
	root = new GenericMatlabTree(gtree, toGenericType(gtree, null, tree), nodeStatement);
	walk(parent, tree, gtree, root, 1);
	gtree.rootNode(root);
	logger.debug("----------------- VARIABLES DEFINED --------------------------");
	for (String funcall : vardefs)
	    logger.debug(funcall);
	logger.debug("----------------- FUNCTIONS DEFINED --------------------------");
	for (String funcall : fundefs)
	    logger.debug(funcall);
	logger.debug("----------------- UNBOUND FUNCTION CALLS --------------------------");
	funcalls.removeAll(vardefs);
	funcalls.removeAll(fundefs);
	for (String funcall : funcalls)
	    logger.debug(funcall);
	return gtree;
    }

    public void walk(AAST parent, CommonTree mt, MatlabAAST gtree, GenericMatlabTree t, int indent)
	    throws SyntaxException, ListException {
	logger.debug(t.toString());
	walkHelper(parent, mt, gtree, t, indent);
    }

    private void walkHelper(AAST aastparent, CommonTree mt, MatlabAAST gtree, AASTNode parent, int indent)
	    throws SyntaxException, ListException {
	if (mt != null) {
	    StringBuffer sb = new StringBuffer(indent);
	    for (int i = 0; i < indent; i++)
		sb = sb.append("   ");
	    for (int i = 0; i < mt.getChildCount(); i++) {
		CommonTree child = (CommonTree) mt.getChild(i);
		Integer line = child.getLine();
		Integer charPosition = child.getCharPositionInLine();
		GenericMatlabTree genericTree = null;
		String tokenValue = null;
		// skip null statements
		if (child.getType() == matlabParser.NULL_STMT)
		    continue;
		else if (child.getType() == matlabParser.BREAK) {
		    tokenValue = child.toString();
		    genericTree = new GenericMatlabTree(gtree, NodeType.EXPR_STMT, tokenValue, line, charPosition,
			    parent);
		    GenericMatlabTree idNode = new GenericMatlabTree(gtree, NodeType.ID, tokenValue, line, charPosition,
			    genericTree);
		    genericTree.addChild(idNode);
		} else {

		    if (child.getType() == matlabParser.APPLY) {
			String applyId = getCurrentChildText(child.getChild(0), parent);
			String applyParam = getCurrentChildText(child.getChild(1), parent).replace("'", "")
				.replace("\"", "");
			if ("addpath".equals(applyId)) {
			    basePaths.add(basePath.resolve(new File(applyParam).getPath()).normalize());
			    continue;
			}
		    }
		    // the string that raised this node
		    tokenValue = getCurrentChildText(child, parent);
		    genericTree = new GenericMatlabTree(gtree, toGenericType(gtree, parent, child), tokenValue, line,
			    charPosition, parent);
		}

		if (genericTree.type().equals(NodeType.ID) && !parent.type().equals(NodeType.FUNCTION)) {
		    // search for subfiles with this name
		    String p = getMatchingFileName(genericTree.name());
		    boolean irrelevant = parent.type().equals(NodeType.CLEAR) || parent.type().equals(NodeType.CLOSE)
			    || parent.type().equals(NodeType.CLEARVARS) || parent.type().equals(NodeType.HOLD)
			    || parent.type().equals(NodeType.GRID) || parent.type().equals(NodeType.FORMAT);
		    if (p != null && parent.type().equals(NodeType.APPLY)) {
			addExternalRequirement(p, genericTree);
			parent.addChild(genericTree);
		    } else if (p != null && !irrelevant) {
			// subscript! go on parsing this so that the resulting
			// tree will be an union of the main and the subscript
			MatlabCompilationUnit subParser;
			MatlabAAST subAAST;
			subParser = new MatlabCompilationUnit(program, p);
			subAAST = (MatlabAAST) subParser.walk(aastparent);
			// subAAST.rootNode().childs(NodeType.STATEMENT_LIST).get(0).type().equals(NodeType.FUNCTION)
			List<AASTNode> stmts = subAAST.rootNode().childs(NodeType.STATEMENT_LIST);
			if (stmts.size() != 1 || !stmts.get(0).type().equals(NodeType.FUNCTION))
			    // join as a subscript
			    gtree.join(subAAST, genericTree);
			else {
			    // function call with void parameters
			    gtree.addParens(genericTree);
			    addExternalRequirement(p, genericTree);
			}
			// add sub-files
			for (Entry<String, Set<AASTNode>> entry : subParser.subFiles().entrySet())
			    for (AASTNode node : entry.getValue())
				addExternalRequirement(entry.getKey(), node);

		    } else
			parent.addChild(genericTree);
		} else if (genericTree.type().equals(NodeType.ID_NODE) && child.getChildCount() == 0) {
		    // add missing ID (be sure every ID_NODE have an ID as child
		    AASTNode id = new AASTNode(gtree, NodeType.ID, tokenValue, genericTree.lineNumber(),
			    genericTree.colNumber(), genericTree);
		    genericTree.addChild(id);
		    parent.addChild(genericTree);
		} else
		    parent.addChild(genericTree);

		// for (ASTListener l : listeners)
		// l.onEnter(genericTree, tokenValue);

		fillStructures(child, parent);
		debug(sb.toString(), child);
		// recursion
		walkHelper(aastparent, (CommonTree) mt.getChild(i), gtree, genericTree, indent + 1);

		// we leave this node, the whole sub-tree was visited
		// for (ASTListener l : listeners)
		// l.onExit(genericTree, tokenValue);
	    }

	}
    }

    private void addExternalRequirement(String p, AASTNode genericTree) {
	if (!externalRequirements.containsKey(p))
	    externalRequirements.put(p, new HashSet<AASTNode>());
	externalRequirements.get(p).add(genericTree);
    }

    // helper
    private String getMatchingFileName(String filename) {
	for (Path bpath : basePaths) {
	    // your directory
	    File basePathf = bpath.toFile();
	    File[] matchingFunctionFiles = basePathf.listFiles(new FilenameFilter() {
		public boolean accept(File dir, String name) {
		    return name.equals(filename + ".m");
		}
	    });
	    if (matchingFunctionFiles != null && matchingFunctionFiles.length > 0)
		return matchingFunctionFiles[0].getAbsolutePath();
	}
	return null;
    }

    private void fillStructures(Tree child, AASTNode node) throws SyntaxException {
	if ("APPLY".equals(matlabParser.tokenNames[child.getType()]))
	    apply = true;
	if ("FUNCTION".equals(matlabParser.tokenNames[child.getType()]))
	    function = true;
	if ("PARAMETER_LIST".equals(matlabParser.tokenNames[child.getType()]))
	    funparams = true;
	if ("LHS".equals(matlabParser.tokenNames[child.getType()]))
	    LHS = true;

	if ("ID".equals(matlabParser.tokenNames[child.getType()])) {
	    tmpvar = getCurrentChildText(child, node);
	}

	if (!"".equals(tmpvar)) {
	    if (apply)
		funcalls.add(tmpvar);
	    if (function)
		fundefs.add(tmpvar);
	    if (funparams)
		vardefs.add(tmpvar);
	    if (LHS)
		vardefs.add(tmpvar);

	    tmpvar = "";
	    apply = false;
	    function = false;
	    LHS = false;
	}

	// keep adding ids to vardefs until we get a token that is not an ID
	if (!"PARAMETER_LIST".equals(matlabParser.tokenNames[child.getType()])
		&& !"ID".equals(matlabParser.tokenNames[child.getType()])) {
	    funparams = false;
	}

    }

    private void debug(String pad, Tree child) {
	logger.debug(pad + tokens.toString(child.getTokenStartIndex(), child.getTokenStopIndex()).trim() + " ["
		+ matlabParser.tokenNames[child.getType()] + "]");
    }

    private String getCurrentChildText(Tree child, AASTNode node) throws SyntaxException {
	try {
	    return tokens.toString(child.getTokenStartIndex(), child.getTokenStopIndex()).trim();
	} catch (Exception e) {
	    throw new SyntaxException(MatlabParsingErrorMessage.UNEXPECTED_TOKEN, node);
	}
    }

    private NodeType toGenericType(MatlabAAST gtree, AASTNode parent, CommonTree node)
	    throws SyntaxException, ListException {
	switch (node.getType()) {
	case matlabParser.EOF:
	    return NodeType.EOF;
	case matlabParser.APPLY:
	    return NodeType.APPLY;
	case matlabParser.ASSIGN:
	    return NodeType.ASSIGN;
	case matlabParser.AT:
	    return NodeType.AT;
	case matlabParser.BIN_AND:
	    return NodeType.BIN_AND;
	case matlabParser.BIN_OR:
	    return NodeType.BIN_OR;
	case matlabParser.BREAK:
	    return NodeType.BREAK;
	case matlabParser.CASE:
	    return NodeType.CASE;
	case matlabParser.CATCH:
	    return NodeType.CATCH;
	case matlabParser.CCT:
	    return NodeType.TRANSPOSE;
	case matlabParser.CELL:
	    return NodeType.CELL;
	case matlabParser.CELLACCESS:
	    return NodeType.CELLACCESS;
	case matlabParser.CLEAR:
	case matlabParser.CLC:
	    return NodeType.CLEAR;
	case matlabParser.CLEARVARS:
	    return NodeType.CLEARVARS;
	case matlabParser.CLOSE:
	    return NodeType.CLOSE;
	case matlabParser.COLON:
	    return NodeType.COLON;
	case matlabParser.COMMA:
	    return NodeType.COMMA;
	case matlabParser.CONTINUE:
	    return NodeType.CONTINUE;
	case matlabParser.DOT:
	    return NodeType.DOT;
	case matlabParser.DOUBLE_EQ:
	    return NodeType.DOUBLE_EQ;
	case matlabParser.DYNFIELDACCESS:
	    return NodeType.DYNFIELDACCESS;
	case matlabParser.ELSE:
	    return NodeType.ELSE;
	case matlabParser.ELSEIF:
	    return NodeType.ELSEIF;
	case matlabParser.EL_CCT:
	    return NodeType.ELEMENTWISE_CCT;
	case matlabParser.EL_EXP:
	    return NodeType.ELEMENTWISE_EXP;
	case matlabParser.EL_LEFTDIV:
	    return NodeType.ELEMENTWISE_LEFTDIV;
	case matlabParser.EL_RIGHTDIV:
	    return NodeType.ELEMENTWISE_RIGHTDIV;
	case matlabParser.EL_TIMES:
	    return NodeType.ELEMENTWISE_TIMES;
	case matlabParser.END:
	    return NodeType.END;
	case matlabParser.EQ:
	    return NodeType.EQ;
	case matlabParser.EXP:
	    return NodeType.EXP;
	case matlabParser.EXPRESSION:
	    return NodeType.EXPRESSION;
	case matlabParser.EXPR_STMT:
	    return NodeType.EXPR_STMT;
	case matlabParser.FIELDACCESS:
	    return NodeType.FIELDACCESS;
	case matlabParser.FLOAT:
	    return NodeType.FLOAT;
	case matlabParser.FOR:
	    return NodeType.FOR;
	case matlabParser.PARFOR:
	    return NodeType.PARFOR;
	case matlabParser.FORMAT:
	    return NodeType.FORMAT;
	case matlabParser.FUNCTION:
	    return NodeType.FUNCTION;
	case matlabParser.FUNCTION_PARAMETER_LIST:
	    return NodeType.FUNCTION_PARAMETER_LIST;
	case matlabParser.FUNCTION_RETURN:
	    return NodeType.FUNCTION_RETURN;
	case matlabParser.GLOBAL:
	    return NodeType.GLOBAL;
	case matlabParser.GRID:
	    return NodeType.GRID;
	case matlabParser.GRT:
	    return NodeType.GRT;
	case matlabParser.GRTE:
	    return NodeType.GRTE;
	case matlabParser.HOLD:
	    return NodeType.HOLD;
	case matlabParser.ID:
	    return NodeType.ID;
	case matlabParser.ID_NODE:
	    return NodeType.ID_NODE;
	case matlabParser.ID_VALUE:
	    return NodeType.ID_VALUE;
	case matlabParser.IF:
	    return NodeType.IF;
	case matlabParser.INT:
	    return NodeType.INT;
	case matlabParser.LBRACE:
	    return NodeType.LBRACE;
	case matlabParser.LEFTDIV:
	    return NodeType.LEFTDIV;
	case matlabParser.LHS:
	    return NodeType.LHS;
	case matlabParser.LINECOMMENT:
	    return NodeType.LINECOMMENT;
	case matlabParser.LOG_AND:
	    return NodeType.LOG_AND;
	case matlabParser.LOG_OR:
	    return NodeType.LOG_OR;
	case matlabParser.LPAREN:
	    return NodeType.LPAREN;
	case matlabParser.LSBRACE:
	    return NodeType.LSBRACE;
	case matlabParser.LST:
	    return NodeType.LST;
	case matlabParser.LSTE:
	    return NodeType.LSTE;
	case matlabParser.MATRIX:
	    return NodeType.MATRIX;
	case matlabParser.MINUS:
	    return NodeType.MINUS;
	case matlabParser.NEG:
	    if (parent != null && parent.parentExists(NodeType.LHS))
		return NodeType.VOID;
	    else
		return NodeType.NEG;
	case matlabParser.NEQ:
	    return NodeType.NEQ;
	case matlabParser.NL:
	    return NodeType.NL;
	case matlabParser.NULL_STMT:
	    return NodeType.NULL_STMT;
	case matlabParser.OTHERWISE:
	    return NodeType.OTHERWISE;
	case matlabParser.PARAMETER_LIST:
	    return NodeType.PARAMETER_LIST;
	case matlabParser.PARENS:
	    return NodeType.PARENS;
	case matlabParser.PERSISTENT:
	    return NodeType.PERSISTENT;
	case matlabParser.PLUS:
	    return NodeType.PLUS;
	case matlabParser.PROGRAM:
	    return NodeType.PROGRAM;
	case matlabParser.RBRACE:
	    return NodeType.RBRACE;
	case matlabParser.RETURNS:
	    return NodeType.RETURNS;
	case matlabParser.RHS:
	    return NodeType.RHS;
	case matlabParser.RIGHTDIV:
	    return NodeType.RIGHTDIV;
	case matlabParser.RPAREN:
	    return NodeType.RPAREN;
	case matlabParser.RSBRACE:
	    return NodeType.RSBRACE;
	case matlabParser.SEMI:
	    return NodeType.SEMI;
	case matlabParser.STATEMENT_LIST:
	    return NodeType.STATEMENT_LIST;
	case matlabParser.STRING:
	    return NodeType.STRING;
	case matlabParser.SWITCH:
	    return NodeType.SWITCH;
	case matlabParser.THREEDOTS:
	    return NodeType.THREEDOTS;
	case matlabParser.TIMES:
	    return NodeType.TIMES;
	case matlabParser.TRY:
	    return NodeType.TRY;
	case matlabParser.VARARGIN:
	    return NodeType.VARARGIN;
	case matlabParser.VECTOR:
	    return NodeType.VECTOR;
	case matlabParser.VOID:
	    return NodeType.VOID;
	case matlabParser.WHILE:
	    return NodeType.WHILE;
	case matlabParser.WS:
	    return NodeType.WS;
	default:
	    if (parser.getErrors().isEmpty()) {
		try {
		    node.setUnknownTokenBoundaries();
		} catch (Exception e) {
		    //amen
		}
		int line = 0;
		int charPos = 0;
		CommonTree prevNode = node;
		while (node != null && (line == 0 || node.getLine() == line)) {
		    if (node.getLine() != 0)
			line = node.getLine();
		    // keep just the first
		    if (charPos == 0)
			charPos = node.getCharPositionInLine();
		    prevNode = node;
		    node = node.parent;
		}
		// re-construct string walking down from node
		String text = getCurrentChildText(prevNode, null);

		throw new SyntaxException(ErrorMessage.USER_SYNTAX_ERROR,
			new AASTNode(gtree, null, text, line, charPos, null));
	    } else {
		// use errors collected by parser and throw them alltogether
		ListException errors = new ListException(ErrorMessage.LIST_OF_TREE_ERRORS);
		for (String message : parser.getErrors())
		    errors.add(new SyntaxException(ErrorMessage.USER_SYNTAX_ERROR, null, message));

		throw errors;
	    }
	}
    }

    @Override
    public String toString() {
	return mainScript;
    }
}
