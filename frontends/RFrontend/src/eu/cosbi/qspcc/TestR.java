package eu.cosbi.qspcc;

import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;

import eu.cosbi.qspcc.frontend.RFilter;
import eu.cosbi.qspcc.frontend.RLexer;
import eu.cosbi.qspcc.frontend.RParser;

public class TestR {
    public static void main(String[] args) throws Exception {
	String inputFile = null;
	if (args.length > 0)
	    inputFile = args[0];
	InputStream is = System.in;
	if (inputFile != null) {
	    is = new FileInputStream(inputFile);
	}
	ANTLRInputStream input = new ANTLRInputStream(is);
	RLexer lexer = new RLexer(input);
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	// Print tokens BEFORE filtering
	//		tokens.fill();
	//		for (Object tok : tokens.getTokens()) {
	//			System.out.println(tok);
	//		}
	RFilter filter = new RFilter(tokens);
	filter.stream(); // call start rule: stream
	tokens.reset();
	// Print tokens AFTER filtering
	//		for (Object tok : tokens.getTokens()) {
	//			System.out.println(tok);
	//		}
	RParser parser = new RParser(tokens);
	parser.setBuildParseTree(true);
	RuleContext tree = parser.prog();
	//tree.save(parser, "/tmp/R.ps"); // Generate postscript
	System.out.println(tree.toStringTree(parser));
    }
}
