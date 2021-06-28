package eu.cosbi.qspcc.backend.interfaces;

import java.util.List;
import java.util.Set;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.backend.utils.VarVisibility;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.Tuple;

public interface MainCode {
    public void clearCurrentTranslationBuffer(AASTNode node, String content);

    public void clearCurrentTranslationBuffer();

    public void removeLastTranslationBuffer();

    public List<Triple<AASTNode, StringBuffer, Set<String>>> getTranslationBufferList();

    public StringBuffer getLocalStructBuffer();

    public StringBuffer getGlobalBuffer();

    public StringBuffer getLocalFunctionsBuffer();

    public String createIntScalar(String newSymbol, boolean onlyRefs, VarVisibility v)
	    throws UndefinedTranslationException;

    public String createIntScalar(String newSymbol, String value, boolean onlyRefs, VarVisibility v)
	    throws UndefinedTranslationException;

    public String createScalar(String newSymbol, String value, boolean onlyRefs, VarVisibility v)
	    throws UndefinedTranslationException;

    public Tuple<String, String> createMatrixRef(String refName, String type, String value, IntType[] dimensions,
	    boolean sparse, boolean onlyReferences, boolean forceStatic, VarVisibility v, boolean alreadyDeclared,
	    String defaultValue, boolean extern) throws UndefinedTranslationException, TypeException;

    public String createString(String symbol, String value, boolean forceStatic, VarVisibility v, boolean pointer)
	    throws UndefinedTranslationException;

    public void translateComment(AASTNode curRoot, String multiline);

    public void translation(StringBuffer buff, String string, boolean should_indent);

}
