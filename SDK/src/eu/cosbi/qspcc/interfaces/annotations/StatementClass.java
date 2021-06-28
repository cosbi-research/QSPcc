package eu.cosbi.qspcc.interfaces.annotations;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.cosbi.qspcc.ast.NodeType;

@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface StatementClass {
    public enum Type {
	ASSIGN(NodeType.ASSIGN),
	IF(NodeType.IF),
	WHILE(NodeType.WHILE),
	FOR(NodeType.FOR),
	EXPRESSION(NodeType.EXPR_STMT),
	CLEAR(
		NodeType.CLEAR,
		NodeType.CLOSE,
		NodeType.CLEARVARS,
		NodeType.HOLD,
		NodeType.GRID,
		NodeType.FORMAT,
		NodeType.GLOBAL),
	SWITCH(NodeType.SWITCH),
	RETURN(NodeType.RETURNS),
	PERSISTENT(NodeType.PERSISTENT);

	private String stringified;
	private NodeType[] types;

	Type(NodeType... ts) {
	    this.types = ts;
	    String[] tss = new String[ts.length];
	    for (int i = 0; i < ts.length; ++i)
		tss[i] = ts[i].toString();
	    stringified = String.join(", ", tss);
	}

	public NodeType[] handledNodes() {
	    return types;
	}

	@Override
	public String toString() {
	    return stringified;
	}
    }

    Type category();
}
