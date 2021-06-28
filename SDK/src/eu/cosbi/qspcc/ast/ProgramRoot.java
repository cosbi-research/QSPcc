package eu.cosbi.qspcc.ast;

import java.util.List;

public interface ProgramRoot {
    /**
     * increase the number of functions to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     */
    public void pushWalk(AAST name);

    /**
     * decrease the number of functions to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     */
    public void popWalk(AAST name);

    /**
     * increase the number of statements to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     */
    public void pushStatementWalk();

    /**
     * decrease the number of statements to be reparsed in a future walk. When
     * the number of push equals the number of pop the compilation unit won't be
     * parsed anymore.
     */
    public void popStatementWalk();

    /**
     * returns true if pushWalk called more times than popWalk by ProgramNode
     * childs.
     * 
     * @return
     */
    public boolean walkNeeded();

    /**
     * returns true if pushStatementWalk called more times than popStatementWalk
     * by ProgramNode childs.
     * 
     * @return
     */
    public boolean statementWalkNeeded();

    /**
     * set of incomplete functions, in topological order of dependency (first the one without dependencies or with less dependencies)
     * @return
     */
    public List<AAST> incompleteFunctions();
}
