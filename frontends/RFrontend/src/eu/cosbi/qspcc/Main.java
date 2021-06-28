package eu.cosbi.qspcc;

import java.util.ArrayList;

import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.frontend.R;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.utils.Tuple;

public class Main {

    public static void main(String[] args) throws Exception {
	R r = new R("/home/tomasoni/git/matlabTranslator/tmp/test.R", null);
	Program p = r.walk(new IFunction[] {}, new ArrayList<Tuple<GType, String>>());
    }

}
