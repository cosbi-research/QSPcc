package eu.cosbi.qspcc.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.FunctionType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.utils.Tuple;

public class ExceptionGenerator {

    public enum FakeFunction implements IFunction {
	;

	@Override
	public String getName() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public List<GType> getParamTypes() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public Map<BType, GType> getOutType() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public FunctionType type() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public BiFunction<AASTNode, List<GType>, List<GType>> paramTypesUpdater() {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

    public static void main(String[] args) {
	Program fakeprogram = new Program(new FakeFunction[] {}, new ArrayList<Tuple<GType, String>>());
	AAST main = new AAST(fakeprogram);
	main.setSourcePath("/home/tomasoni/git/matlabTranslator/tmp/fake.R");
	fakeprogram.mainCompilationUnit(main);

	AASTNode root = new AASTNode(main, NodeType.PROGRAM, "");
	main.rootNode(root);

	AASTNode global_stmt = new AASTNode(main, NodeType.STATEMENT_LIST, "f(a,....,z)", 0, 0, root);
	root.addChild(global_stmt);
	AASTNode exprstmt = new AASTNode(main, NodeType.EXPR_STMT, " f(a,....,z)", 0, 0, global_stmt);
	global_stmt.addChild(exprstmt);
	AASTNode expr = new AASTNode(main, NodeType.EXPRESSION, "f(a,....,z)", 0, 0, exprstmt);
	exprstmt.addChild(expr);
	AASTNode apply = new AASTNode(main, NodeType.APPLY, "f(a,....,z)", 0, 0, expr);
	expr.addChild(apply);
	AASTNode id = new AASTNode(main, NodeType.ID, "f", 0, 0, apply);
	apply.addChild(id);
	AASTNode function_param_list = new AASTNode(main, NodeType.FUNCTION_PARAMETER_LIST, "a,....,z", 0, 0, apply);
	apply.addChild(function_param_list);

	List<GType> actualParamTypes = new ArrayList<GType>();
	actualParamTypes.add(GType.get(BType.INT, 1));
	actualParamTypes.add(GType.get(BType.INT, 2));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.SCALAR));
	actualParamTypes.add(GType.get(BType.STRING));

	String error = new TypeException(ErrorMessage.FUN_ENV_PARAM_NEVER_DEFINED, apply, "ode32", "f",
		"line xx, char yy", new String[] { "z" }).stringify();
	System.out.println(error);
    }

}
