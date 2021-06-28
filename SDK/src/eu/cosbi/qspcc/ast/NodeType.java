package eu.cosbi.qspcc.ast;

/**
 * TODO: Some strings are matlab-centric
 *       ex. FLOAT("Scalar")
 *       Implement a mechanism to parametrize strings based on currently used frontend/backend
 * @author tomasoni
 *
 */
public enum NodeType {
    EOF("Eof"),
    APPLY("Apply"),
    // local assignment
    // LHS variable will be visible in this block
    ASSIGN("Assignment"),
    // global assignment
    // LHS variable will be visible everywhere
    GASSIGN("Global assignment"),
    // others
    AT("Anonymous function"),
    BIN_AND("&"),
    BIN_OR("|"),
    LOG_AND("&&"),
    LOG_OR("||"),
    BREAK("Break"),
    CASE("Case"),
    CATCH("Catch"),
    TRANSPOSE("Transpose"),
    CELL("Cell definition {}"),
    CELLACCESS("Cell access {}"),
    CLEAR("Clear"),
    CLEARVARS("Clear vars"),
    CLOSE("Close"),
    COLON(":"),
    COMMA(","),
    CONTINUE("Continue"),
    // . in a.b
    DOT("."),
    DOUBLE_EQ("=="),
    DYNFIELDACCESS("Dynamic field access"),
    ELSE("Else"),
    ELSEIF("Else if"),
    ELEMENTWISE_CCT("Element-wise transposition (.')"),
    ELEMENTWISE_EXP("Element-wise exponentiation (.^)"),
    // a / b
    ELEMENTWISE_LEFTDIV("Element-wise division (./)"),
    // b \ a
    ELEMENTWISE_RIGHTDIV("Element-wise division (.\\)"),
    ELEMENTWISE_TIMES("Element-wise multiplication (.*)"),
    END("Matrix last element access"),
    EQ("="),
    EXP("^"),
    EXPONENT("e"),
    EXPRESSION("Expression"),
    EXPR_STMT("Statement"),
    FALSE("False"),
    // a.b
    FIELDACCESS("Struct access"),
    FLOAT("Scalar"),
    FOR("For"),
    FORMAT("Format"),
    FUNCTION("Function"),
    FUNCTION_PARAMETER_LIST("Apply parameters"),
    FUNCTION_RETURN("Function return values"),
    GLOBAL("Global"),
    GRID("Grid"),
    GRT(">"),
    GRTE(">="),
    HOLD("Hold"),
    ID("Id"),
    ID_NODE("Id Node"),
    ID_VALUE("Id value"),
    IF("If"),
    INT("Int"),
    LBRACE("{"),
    RBRACE("}"),
    LSBRACE("["),
    RSBRACE("]"),
    RPAREN(")"),
    LEFTDIV("/"),
    RIGHTDIV("\\"),
    LHS("Assigned variable"),
    RHS("Assigned value"),
    LINECOMMENT("Comment"),
    LPAREN("If"),
    LST("<"),
    LSTE("<="),
    MATRIX("Matrix"),
    MINUS("-"),
    NEG("Not"),
    NEQ("Not equal"),
    NL("New line"),
    NULL_STMT("Empty statement"),
    OTHERWISE("Otherwise"),
    PARAMETER_LIST("Function parameters"),
    PARENS("Parenthesis ()"),
    PERSISTENT("Persistent"),
    PLUS("+"),
    PROGRAM("Program"),
    RETURNS("Returns"),
    SEMI(";"),
    STATEMENT_LIST("Statement list"),
    STRING("String"),
    SWITCH("Switch"),
    THREEDOTS("..."),
    TIMES("*"),
    TRUE("True"),
    TRY("Try"),
    VARARGIN("Variable arguments"),
    VECTOR("Vector"),
    VOID("Void"),
    WHILE("While"),
    WS("White space"),
    DEFAULT_VALUE("Named parameter"),
    PARFOR("Parallel for");

    private String strrep;

    NodeType(String strrep) {
	this.strrep = strrep;
    }

    @Override
    public String toString() {
	return strrep;
    }
}
