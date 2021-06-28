package eu.cosbi.qspcc.frontend;

import java.util.List;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend;
import eu.cosbi.qspcc.interfaces.annotations.FrontendModule;
import eu.cosbi.utils.Tuple;

@FrontendModule(name = "dummyr")
public class DummyRFrontend extends CompilerFrontend {
    Program rprogram;

    public DummyRFrontend(String programPath) {
	super(programPath);
    }

    @Override
    public Program walk(IFunction[] coreFunctions, List<Tuple<GType, String>> gvars)
	    throws SyntaxException, ListException {
	// construct syntetic tree
	rprogram = new Program(coreFunctions, gvars);
	AAST main = new AAST(rprogram);
	main.setSourcePath("/home/tomasoni/git/matlabTranslator/tmp/fake.R");
	rprogram.mainCompilationUnit(main);
	AASTNode id, expr, assign, apply, function_param_list, stmt;

	AASTNode root = new AASTNode(main, NodeType.PROGRAM, "");
	main.rootNode(root);

	AASTNode global_stmt = new AASTNode(main, NodeType.STATEMENT_LIST,
		"c <- 2+3\ntest <- function(a) {1+c}\ntest(1)", 0, 0, root);
	root.addChild(global_stmt);
	// a <- 3+2
	assign = new AASTNode(main, NodeType.ASSIGN, "c <- 2+3", 0, 0, global_stmt);
	global_stmt.addChild(assign);
	AASTNode lhs = new AASTNode(main, NodeType.LHS, "c", 0, 0, assign);
	assign.addChild(lhs);
	AASTNode id_node = new AASTNode(main, NodeType.ID_NODE, "c", 0, 0, lhs);
	lhs.addChild(id_node);
	id = new AASTNode(main, NodeType.ID, "c", 0, 0, id_node);
	id_node.addChild(id);

	AASTNode rhs = new AASTNode(main, NodeType.RHS, "2+3", 0, 0, assign);
	assign.addChild(rhs);
	expr = new AASTNode(main, NodeType.EXPRESSION, "2+3", 0, 0, rhs);
	rhs.addChild(expr);
	AASTNode plus = new AASTNode(main, NodeType.PLUS, "2+3", 0, 0, expr);
	expr.addChild(plus);
	AASTNode v = new AASTNode(main, NodeType.INT, "2", 0, 0, plus);
	plus.addChild(v);
	v = new AASTNode(main, NodeType.INT, "3", 0, 0, plus);
	plus.addChild(v);

	assign.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, assign));

	// b <- function(a) {1+c}
	AASTNode fundef = new AASTNode(main, NodeType.FUNCTION, "test <- function(a) {1+c}", 0, 0, global_stmt);
	global_stmt.addChild(fundef);
	id = new AASTNode(main, NodeType.ID, "test", 0, 0, fundef);
	fundef.addChild(id);
	AASTNode funret = new AASTNode(main, NodeType.FUNCTION_RETURN, "test_out", 0, 0, fundef);
	fundef.addChild(funret);
	id = new AASTNode(main, NodeType.ID, "test_out", 0, 0, funret);
	funret.addChild(id);
	AASTNode funpara = new AASTNode(main, NodeType.PARAMETER_LIST, "(a)", 0, 0, fundef);
	fundef.addChild(funpara);
	id = new AASTNode(main, NodeType.ID, "a", 0, 0, funpara);
	funpara.addChild(id);

	stmt = new AASTNode(main, NodeType.STATEMENT_LIST, "1+c", 0, 0, fundef);
	fundef.addChild(stmt);

	AASTNode exprstmt = new AASTNode(main, NodeType.EXPR_STMT, " 1+c", 0, 0, stmt);
	stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "1+c", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	plus = new AASTNode(main, NodeType.PLUS, "1+c", 0, 0, expr);
	expr.addChild(plus);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, plus);
	plus.addChild(v);
	v = new AASTNode(main, NodeType.ID, "c", 0, 0, plus);
	plus.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// call to function test(1)
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "test(1)", 0, 0, global_stmt);
	global_stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "test(1)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "test(1)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "test", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "1", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "1", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, expr);
	expr.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// c <- apply(a, 1, function(x) {x})
	assign = new AASTNode(main, NodeType.ASSIGN, "c <- apply(a, 1, function(x) {x})", 0, 0, global_stmt);
	global_stmt.addChild(assign);
	lhs = new AASTNode(main, NodeType.LHS, "c", 0, 0, assign);
	assign.addChild(lhs);
	id_node = new AASTNode(main, NodeType.ID_NODE, "c", 0, 0, lhs);
	lhs.addChild(id_node);
	id = new AASTNode(main, NodeType.ID, "c", 0, 0, id_node);
	id_node.addChild(id);

	rhs = new AASTNode(main, NodeType.RHS, "apply(a, 1, function(x) {x})", 0, 0, assign);
	assign.addChild(rhs);
	expr = new AASTNode(main, NodeType.EXPRESSION, "apply(a, 1, function(x) {x})", 0, 0, rhs);
	rhs.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "apply(a, 1, function(x) {x})", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "apply", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "a, 1, function(x) {x}", 0, 0,
		apply);
	apply.addChild(function_param_list);

	expr = new AASTNode(main, NodeType.EXPRESSION, "a", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.ID, "a", 0, 0, expr);
	expr.addChild(v);

	expr = new AASTNode(main, NodeType.EXPRESSION, "1", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, expr);
	expr.addChild(v);

	// FUNCTION AS PARAM
	fundef = new AASTNode(main, NodeType.FUNCTION, "function(x) {x}", 0, 0, function_param_list);
	function_param_list.addChild(fundef);
	id = new AASTNode(main, NodeType.ID, "", 0, 0, fundef);
	fundef.addChild(id);
	funret = new AASTNode(main, NodeType.FUNCTION_RETURN, "_out", 0, 0, fundef);
	fundef.addChild(funret);
	id = new AASTNode(main, NodeType.ID, "_out", 0, 0, funret);
	funret.addChild(id);
	funpara = new AASTNode(main, NodeType.PARAMETER_LIST, "(x)", 0, 0, fundef);
	fundef.addChild(funpara);
	id = new AASTNode(main, NodeType.ID, "x", 0, 0, funpara);
	funpara.addChild(id);

	stmt = new AASTNode(main, NodeType.STATEMENT_LIST, "x", 0, 0, fundef);
	fundef.addChild(stmt);

	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, " x", 0, 0, stmt);
	stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "x", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	id_node = new AASTNode(main, NodeType.ID_NODE, "x", 0, 0, expr);
	expr.addChild(id_node);
	id = new AASTNode(main, NodeType.ID, "x", 0, 0, id_node);
	id_node.addChild(id);
	// close function statement
	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));
	// close assign
	assign.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, assign));

	// load library
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT,
		"library(MASS) # this makes visible in ENV scope the variable 'stack.loss' and the function 'rlm'", 0,
		0, global_stmt);
	global_stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "library(MASS)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "library(MASS)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "library", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "MASS", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "MASS", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	id_node = new AASTNode(main, NodeType.ID_NODE, "MASS", 0, 0, expr);
	expr.addChild(id_node);
	id = new AASTNode(main, NodeType.ID, "MASS", 0, 0, id_node);
	id_node.addChild(id);

	exprstmt.addChild(new AASTNode(main, NodeType.LINECOMMENT,
		"# this makes visible in ENV scope the variable 'stack.loss' and the function 'rlm'", 0, 0, exprstmt));

	//@formatter:off
	// a <- function(a,b=1,c) {
	//  if (missing(c)) {
	//	    return(a+b)
	//	  } else {
	//	    return(a+b+c)
	//	  }
	//	}
	//@formatter:on
	fundef = new AASTNode(main, NodeType.FUNCTION,
		"default <- function(a,b=1,c) {\n  if (missing(c)) {\n	    return(a+b)\n	  } else {\n	    return(a+b+c)\n	  }\n	}\n",
		0, 0, global_stmt);
	global_stmt.addChild(fundef);
	id = new AASTNode(main, NodeType.ID, "default", 0, 0, fundef);
	fundef.addChild(id);
	funret = new AASTNode(main, NodeType.FUNCTION_RETURN, "default_out", 0, 0, fundef);
	fundef.addChild(funret);
	id = new AASTNode(main, NodeType.ID, "default_out", 0, 0, funret);
	funret.addChild(id);
	funpara = new AASTNode(main, NodeType.PARAMETER_LIST, "(a,b=1,c)", 0, 0, fundef);
	fundef.addChild(funpara);
	id = new AASTNode(main, NodeType.ID, "a", 0, 0, funpara);
	funpara.addChild(id);
	id = new AASTNode(main, NodeType.ID, "b", 0, 0, funpara);
	funpara.addChild(id);
	AASTNode defaultValue = new AASTNode(main, NodeType.DEFAULT_VALUE, "=1", 0, 0, id);
	id.addChild(defaultValue);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, defaultValue);
	defaultValue.addChild(v);
	id = new AASTNode(main, NodeType.ID, "c", 0, 0, funpara);
	funpara.addChild(id);

	AASTNode ifstmt_list = new AASTNode(main, NodeType.STATEMENT_LIST,
		"if (missing(c)) {\n	    return(a+b)\n	  } else {\n	    return(a+b+c)\n	  }\n	}", 0,
		0, fundef);
	fundef.addChild(ifstmt_list);

	AASTNode ifstmt = new AASTNode(main, NodeType.IF, "if (missing(c))", 0, 0, ifstmt_list);
	ifstmt_list.addChild(ifstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "missing(c)", 0, 0, ifstmt);
	ifstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "missing(c)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "missing", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "c", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "c", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.ID, "c", 0, 0, expr);
	expr.addChild(v);
	// then
	stmt = new AASTNode(main, NodeType.STATEMENT_LIST, "\n	    return(a+b)", 0, 0, ifstmt);
	ifstmt.addChild(stmt);
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "\n	    return(a+b)", 0, 0, stmt);
	stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "\n	    return(a+b)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "\n	    return(a+b)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "return", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "a+b", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "a+b", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	plus = new AASTNode(main, NodeType.PLUS, "a+b", 0, 0, expr);
	expr.addChild(plus);
	v = new AASTNode(main, NodeType.ID, "a", 0, 0, plus);
	plus.addChild(v);
	v = new AASTNode(main, NodeType.ID, "b", 0, 0, plus);
	plus.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// else
	AASTNode elsestmt = new AASTNode(main, NodeType.ELSE, "else {\n	    return(a+b+c)\n}", 0, 0, ifstmt);
	ifstmt.addChild(elsestmt);

	// then
	stmt = new AASTNode(main, NodeType.STATEMENT_LIST, "\n	    return(a+b+c)", 0, 0, elsestmt);
	elsestmt.addChild(stmt);
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "\n	    return(a+b+c)", 0, 0, stmt);
	stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "\n	    return(a+b+c)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "\n	    return(a+b+c)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "return", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "a+b+c", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "a+b+c", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	plus = new AASTNode(main, NodeType.PLUS, "a+b+c", 0, 0, expr);
	expr.addChild(plus);
	v = new AASTNode(main, NodeType.ID, "a", 0, 0, plus);
	plus.addChild(v);
	AASTNode plus2 = new AASTNode(main, NodeType.PLUS, "b+c", 0, 0, plus);
	plus.addChild(plus2);
	v = new AASTNode(main, NodeType.ID, "b", 0, 0, plus2);
	plus2.addChild(v);
	v = new AASTNode(main, NodeType.ID, "c", 0, 0, plus2);
	plus2.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// call to function default(1)
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "default(1)", 0, 0, global_stmt);
	global_stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "default(1)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "default(1)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "default", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "1", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "1", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, expr);
	expr.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// call to function default(1,3)
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "default(1,3)", 0, 0, global_stmt);
	global_stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "default(1,3)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "default(1,3)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "default", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "1,3", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "1", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, expr);
	expr.addChild(v);
	expr = new AASTNode(main, NodeType.EXPRESSION, "3", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.INT, "3", 0, 0, expr);
	expr.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// call to function default(1,c=3)
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "default(1,c=3)", 0, 0, global_stmt);
	global_stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "default(1,c=3)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "default(1,c=3)", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "default", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "1,c=3", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "1", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.INT, "1", 0, 0, expr);
	expr.addChild(v);
	id = new AASTNode(main, NodeType.ID, "c", 0, 0, function_param_list);
	function_param_list.addChild(id);
	defaultValue = new AASTNode(main, NodeType.DEFAULT_VALUE, "=3", 0, 0, id);
	id.addChild(defaultValue);
	v = new AASTNode(main, NodeType.INT, "3", 0, 0, defaultValue);
	defaultValue.addChild(v);
	// expr = new AASTNode(main, NodeType.EXPRESSION, "2", 0, 0,
	// function_param_list);
	// function_param_list.addChild(expr);
	// v = new AASTNode(main, NodeType.INT, "2", 0, 0, expr);
	// expr.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	// call to function default('a')
	exprstmt = new AASTNode(main, NodeType.EXPR_STMT, "default('a')", 0, 0, global_stmt);
	global_stmt.addChild(exprstmt);
	expr = new AASTNode(main, NodeType.EXPRESSION, "default('a')", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	apply = new AASTNode(main, NodeType.APPLY, "default('a')", 0, 0, expr);
	expr.addChild(apply);
	id = new AASTNode(main, NodeType.ID, "default", 0, 0, apply);
	apply.addChild(id);
	function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "'a'", 0, 0, apply);
	apply.addChild(function_param_list);
	expr = new AASTNode(main, NodeType.EXPRESSION, "'a'", 0, 0, function_param_list);
	function_param_list.addChild(expr);
	v = new AASTNode(main, NodeType.STRING, "'a'", 0, 0, expr);
	expr.addChild(v);

	exprstmt.addChild(new AASTNode(main, NodeType.NL, "", 0, 0, exprstmt));

	return rprogram;
    }

    @Override
    public IFunction[] globalFunctionsFor(String backendClassName) {
	return new IFunction[0];
    }

}
