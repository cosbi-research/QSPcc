package eu.cosbi.qspcc.backend.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.utils.Quadruple;

public interface Function extends MainCode {
	public List<AASTNode> inputs();

	public List<AASTNode> outputs();

	public AASTNode getFunctionNode();

	public String funOutTypeAsString();

	public boolean funOutIsPointer();

	public String functionName();

	public List<String> getFunctionParams();

	public List<String> getFunctionOutputSymbols();

	public void initializeRandomNumberGenerator();

	public void outputMainCode(StringBuffer writer, AAST program, Path path) throws IOException, GException;

	public StringBuffer getInitializationBuffer();

	/**
	 * 
	 * @return the return symbol for this function if it returns just one symbol.
	 *         null otherwise.
	 */
	public String singleReturnSymbol();

	public String fillOutputStructureCode();

	public void genCode(AAST program) throws TypeException, UnboundException, SyntaxException;

	public String getBody();

	public Quadruple<String, Boolean, Boolean, Boolean> genFunctionDefinition()
			throws TypeException, UndefinedTranslationException;

	public boolean needsOutputStructure();

	public boolean isVoid();

	public String toString();

}
