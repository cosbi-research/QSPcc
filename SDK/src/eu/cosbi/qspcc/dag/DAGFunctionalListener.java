package eu.cosbi.qspcc.dag;

import eu.cosbi.qspcc.ast.ProgramNode;
import eu.cosbi.qspcc.exceptions.GException;

public interface DAGFunctionalListener<Root, Node extends DAG<Root, Node> & ProgramNode> {
    public void on(Root aast, Node node) throws GException;
}
