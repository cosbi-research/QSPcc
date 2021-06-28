package eu.cosbi.qspcc.ast;

import java.util.List;
import java.util.Map;

import eu.cosbi.utils.Tuple;

public interface ProgramCompilationUnit {
    /**
     * increase the number of functions to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     */
    public void pushWalk(AASTNode name);

    /**
     * decrease the number of functions to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     */
    public void popWalk(AASTNode name);

    /**
     * set of incomplete function definition and implementation, in topological order of dependency (first the one without dependencies or with less dependencies)
     * @return
     */
    public List<AASTNode> incompleteFunctionStatements();

    /** 
     * set of incomplete statements, in topological order of dependency (first the one without dependencies or with less dependencies), divided by containing function.
     * The first list is the list of statements that are not contained in a function.
     * @return
     */
    public Tuple<List<AASTNode>, Map<AASTNode, List<AASTNode>>> incompleteStatements();

    public boolean walkNeeded();

    /**
     * returns true if pushStatementWalk called more times than popStatementWalk
     * by ProgramNode childs.
     * 
     * @return
     */
    public boolean statementWalkNeeded();

    /**
     * increase the number of statements to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     * @param inFunction: default false, this statement should be reparsed alone. if true, this means that also the top-level function should
     *                    be reparsed so this statement will be automatically reparsed upon function walk
     */
    public void pushStatementWalk(AASTNode name, AASTNode inFunction);

    /**
     * decrease the number of statements to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     * @param inFunction: default false, this statement should be reparsed alone. if true, this means that also the top-level function should
     *                    be reparsed so this statement will be automatically reparsed upon function walk
     */
    public void popStatementWalk(AASTNode name, AASTNode inFunction);

}
