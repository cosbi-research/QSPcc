package eu.cosbi.qspcc.frontend;

import java.util.List;
import java.util.Map;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;

public class MatlabAAST extends AAST {

    public MatlabAAST(Program p, AAST parent) {
	super(p, parent);
    }

    public MatlabAAST(Program p) {
	super(p);
    }

    // node that holds all statements
    private AASTNode program_stmt_list;

    /**
     * join the given ID (that represents a subScript call) to the current
     * statement list. remove the given ID.
     * 
     * @param subAAST
     * @param subNode 
     */
    public void join(MatlabAAST subAAST, GenericMatlabTree subNode) {
	AASTNode subAASTRoot = subAAST.rootNode();
	// change tree!
	subAASTRoot.root(this);
	// substitute this node with root of sub-tree
	AASTNode stmt_list = subAASTRoot.childs().get(0);
	// remove statement that calls this subscript
	AASTNode toremove = null;
	for (AASTNode stmt : program_stmt_list.childs())
	    if (stmt.parentOf(subNode))
		toremove = stmt;
	program_stmt_list.deleteChild(toremove);
	for (AASTNode stmt : stmt_list.childs()) {
	    program_stmt_list.addChild(stmt);
	    ((GenericMatlabTree) stmt).changeParent(program_stmt_list);
	}
	// copy variables from subtree
	for (String variable : subAAST.staticVariables.keySet())
	    for (AASTNode node : subAAST.staticVariables.get(variable))
		newVariable(node, variable);
	for (Map<String, List<AASTNode>> localFunctionVariables : subAAST.localVariables.values())
	    for (String variable : localFunctionVariables.keySet())
		for (AASTNode node : localFunctionVariables.get(variable))
		    newVariable(node, variable);

    }

    public void statementList(GenericMatlabTree genericMatlabTree) {
	program_stmt_list = genericMatlabTree;
    }

    /**
     * add to the given ID (that represents a function call without parameters)
     * the apply nodes needed to make it recognized as a function call.
     * 
     * @param id
     */
    public void addParens(GenericMatlabTree id) {
	AASTNode apply = new AASTNode(id.compilationUnit(), NodeType.APPLY, id.name() + "()", id.lineNumber(),
		id.colNumber(), id.parent());
	id.parent().addChild(apply);
	id.parent().deleteChild(id);
	id.parent(apply);
	apply.addChild(id);
	AASTNode function_param_list = new AASTNode(id.compilationUnit(), NodeType.FUNCTION_PARAMETER_LIST, "",
		id.lineNumber(), id.colNumber(), apply);
	apply.addChild(function_param_list);
	// AASTNode nu = new AASTNode(id.compilationUnit(), NodeType.VOID, "",
	// id.lineNumber(), id.colNumber(),
	// function_param_list);
	// function_param_list.addChild(nu);
    }

}
