package eu.cosbi.qspcc.backend.utils;

import eu.cosbi.qspcc.ast.AASTNode;

public enum VarVisibility {
    GLOBAL,
    PERSISTENT,
    LOCAL_TO_FUNCTION,
    LOCAL_TO_BLOCK;

    private AASTNode context = null;

    public VarVisibility context(AASTNode ctx) {
	this.context = ctx;
	return this;
    }

    public AASTNode context() {
	if (this.equals(LOCAL_TO_BLOCK))
	    return context;
	else
	    return null;
    }

    public boolean isPersistent() {
	return this.equals(VarVisibility.PERSISTENT);
    }

    public boolean isGlobal() {
	return this.equals(VarVisibility.GLOBAL);
    }

    public boolean isLocalToFunction() {
	return this.equals(VarVisibility.LOCAL_TO_FUNCTION);
    }

    public boolean isLocalToBlock() {
	return this.equals(VarVisibility.LOCAL_TO_BLOCK);
    }

}
