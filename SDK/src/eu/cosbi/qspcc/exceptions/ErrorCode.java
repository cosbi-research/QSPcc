package eu.cosbi.qspcc.exceptions;

import eu.cosbi.qspcc.ast.ProgramNode;

public interface ErrorCode {
    public String stringify(ProgramNode node, Object... params);
}
