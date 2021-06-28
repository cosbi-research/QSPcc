/**
  * Copyright (c) 2011 David Wingate
  * Copyright (c) 2014 Matthew Dawson
  * Copyright (c) 2021 Fondazione the Microsoft Research - University of Trento Centre for Computational and Systems Biology (COSBI)
  *
  * Permission is hereby granted, free of charge, to any person obtaining
  * a copy of this software and associated documentation files (the
  * "Software"), to deal in the Software without restriction, including
  * without limitation the rights to use, copy, modify, merge, publish,
  * distribute, sublicense, and/or sell copies of the Software, and to
  * permit persons to whom the Software is furnished to do so, subject to
  * the following conditions:
  *
  * The above copyright notice and this permission notice shall be
  * included in all copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
  * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
  * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
  * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
  * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  *
  *
  * This parser was originally written by David Wingate for the mparser project,
  * and updated to work with ANTLR4 by Matthew Dawson for the jtet project.
  * Finally, it was updated to handle many more matlab constructs 
  * (ex. nested matrices, transpose, end, comments, mixed function definition and scripts and more) 
  * Also downgraded to antlr3 to take advantage of the tree rewriting feature.
**/

grammar matlab;

/*

Things to fix:	

0) lots of warnings.  This is because of anonymous function handles; see details in appropriate XXX comment.
   if you comment out one line, all of the warnings go away.  I believe that even with the warnings,
   the grammar is doing the right thing.
1) "clear" statements can accept wildcards as part of IDs, but we can't (due to the way lexing happens)
2) control flow statements require a newline or comma to resolve an ambiguity (but matlab doesn't)
3) vector construction requires comma delimiters because of whitespace madness (but matlab doesn't)
4) block comments don't work (and I can't figure out why)

For each case, see the appropriate XXX comment below.

*/

options {
	output=AST;
}

//import pureLexer;

tokens {
	// imaginary nodes for our AST
	PROGRAM;
	FUNCTION;
	FUNCTION_RETURN;
	PARAMETER_LIST;
	FUNCTION_PARAMETER_LIST;
	STATEMENT_LIST;
	EXPRESSION;
	EXPR_STMT;
	NULL_STMT;
	ASSIGN;
	APPLY;
	FIELDACCESS;
	DYNFIELDACCESS;
	CELLACCESS;
	MATRIX;
	VECTOR;
	CELL;
	CLEAR;
	CLC;
	CLOSE;
	LHS;
	RHS;
	ID_NODE;
	PARENS;
	//STRING;
	VOID;
}

@parser::header {
 package eu.cosbi.matlab.antlr;
}

@parser::members{
// This scans backwards in the token stream looking for a hidden newline.
// The newline must occur after the last visible token and before the current token.

// C IMPLEMENTATION

/*
    int check_for_hidden_newline( pmatlabParser ctx ) {

      pANTLR3_TOKEN_STREAM ts = ctx->pParser->tstream;

      int tok_ind = INDEX(); // this is the index of LT(1)
      pANTLR3_COMMON_TOKEN cur_tok = LT(1);

      ANTLR3_UINT32 cur_tok_chan = cur_tok->getChannel( cur_tok ); // this is the current channel.

      int rval = 0;

      while ( tok_ind > 0 ) {
	tok_ind--;
	cur_tok = ts->get( ts, tok_ind );
	if ( cur_tok->getChannel( cur_tok ) == cur_tok_chan ) {
	  // uh-oh.  we found a non-hidden token further back in the stream, but no newline in between.  fail.
	  break;
	}
	if ( cur_tok->getType( cur_tok ) == NL ) {
	  // found it!
	  rval = 1;
	  break;
	}
      }

      return rval;
    }
*/



// JAVA IMPLEMENTATION
// Useful for debugging.  NOTE: UNCOMMENT THE RELEVANT LINE IN NL!

    private List<String> errors = new ArrayList<String>();
    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        errors.add(hdr + " " + msg);
    }
    public List<String> getErrors() {
        return errors;
    }


boolean check_for_hidden_newline( TokenStream input ) {
        int tok_ind = input.index(); // this is the index of LT(1)
        Token cur_tok = input.LT(1); 
        int cur_tok_chan = cur_tok.getChannel(); // this is the current channel.

        boolean rval = false;

        while ( tok_ind > 0 ) {
        	tok_ind--;
        	cur_tok = input.get( tok_ind );
        	if ( cur_tok.getChannel() == cur_tok_chan ) {
        		// uh-oh.  we found a non-hidden token further back in the stream, but no newline in between.  fail.
        		break;
        	}
        	if ( cur_tok.getType() == NL ) {
        		// found it!
        		rval = true;
        		break;
        	}
        }

        return rval;
}

} // end of parser::members


@lexer::header {
 package eu.cosbi.matlab.antlr;
}

@lexer::members{
 //keep comment in certain circumstances
 //if inside a string CCT .* CCT
 //boolean inString = false;
 
 //true when tokenizing an ID followed by an ' 
 boolean transpose = false;
  // keep whitespaces in certain circumstances
 // (vector definition)
 boolean keepws = false;
 boolean comment = false;
}

@headers{

public String getErrorMessage(RecognitionException e,
                              String[] tokenNames)
{
    List stack = getRuleInvocationStack(e, this.getClass().getName());
    String msg = null;
    String inputContext =
        input.LT(-3) == null ? "" : ((Tree)input.LT(-3)).getText()+" "+
        input.LT(-2) == null ? "" : ((Tree)input.LT(-2)).getText()+" "+
        input.LT(-1) == null ? "" : ((Tree)input.LT(-1)).getText()+" >>>"+
        ((Tree)input.LT(1)).getText()+"<<< "+
        ((Tree)input.LT(2)).getText()+" "+
        ((Tree)input.LT(3)).getText();
    if ( e instanceof NoViableAltException ) {
       NoViableAltException nvae = (NoViableAltException)e;
       msg = " no viable alt; token="+e.token+
          " (decision="+nvae.decisionNumber+
          " state "+nvae.stateNumber+")"+
          " decision=<<"+nvae.grammarDecisionDescription+">>";
    }
    else {
       msg = super.getErrorMessage(e, tokenNames);
    }
    return stack+" "+msg+" context=..."+inputContext+"...";
}

public String getTokenErrorDisplay(Token t) {
    return t.toString();
}

}
//
// ==================================================================
//
// PARSER RULES
//

// we mostly want to ignore whitespace, but every now and then
// it's significant -- as a statement delimiter, as part of
// matrix construction, etc.  This checks the hiddent channel for a newline.

//NL
	// Java
	//: NL //{ check_for_hidden_newline( input ) }? 
	// C
	//: ( { check_for_hidden_newline( ctx ) }? )
	//;

program	: func_or_statement_list -> ^(PROGRAM func_or_statement_list);

nloc	: ( NL | COMMA ); // mnemonic: newline or comma

//
// scripts and m-files
//

mfile 	: function_definition+ -> ^(PROGRAM function_definition+ ) ;

scriptfile: statement_list;

//
// functions and statements
//

function_definition
	: FUNCTION function_return? ID parameter_list? nloc
	  func_or_statement_list
	  END? -> ^(FUNCTION ID function_return? parameter_list? func_or_statement_list)
	;

function_return
	: ID EQ -> ^(FUNCTION_RETURN ID)
	| LSBRACE ID ( ( (WS* COMMA WS*) | WS+) ID )* RSBRACE EQ -> ^(FUNCTION_RETURN ID+)
	;

// the contents of a function (or a .m file) are statements and function definitions
func_or_statement: ( function_definition | statement );
func_or_statement_list: func_or_statement* -> ^(STATEMENT_LIST func_or_statement* );

// there are times when you can have a list of statements, but not function
// definitions -- for example, inside of an "if" block.
statement_list
	:  statement* -> ^(STATEMENT_LIST statement*)
	;

parameter_list
	: LPAREN RPAREN -> ^(PARAMETER_LIST VOID)
	| LPAREN ( ID COMMA? )+ RPAREN -> ^(PARAMETER_LIST ID+)
	;

// Note: there is a functional difference between terminating a statement with a
// newline vs. a semicolon, so we have to remember the appropriate token in the AST.

statement
	:( lhs EQ ) => lhs EQ rhs nlosoc -> ^(ASSIGN lhs rhs nlosoc)
	| stmt_expression nlosoc -> ^( EXPR_STMT stmt_expression nlosoc )
	| if_statement
	| for_statement
	| while_statement
	| switch_statement
	| try_statement
	| return_statement
	| break_statement
	| continue_statement
	| clearvars_statement
	| clear_statement
	| clc_statement
	| format_statement
	| close_statement
	| hold_statement
	| grid_statement
	| global_statement
	| persistent_statement
	| COMMENT LINECOMMENT -> ^(LINECOMMENT)
	| WS* (NL | SEMI) WS*  -> ^(NULL_STMT) // a null statement
	;

nlosoc	: ( NL | SEMI | COMMA )^
	| COMMENT LINECOMMENT -> ^(LINECOMMENT)
	;

lhs: id_plus_indexers -> ^(LHS id_plus_indexers)
     |  lhs_base_expression -> ^(LHS lhs_base_expression)
    ;
     
rhs: stmt_expression -> ^(RHS stmt_expression);

/* 
XXX CONTROL FLOW AMBIGUITIES:

The statement
    while a (5) end;
is parsed as
    while (a(5))
      [empty]
    end;
and not
    while a
      (5)
     end;
but I can't seem to get a non-ambiguous grammar to do that.
so, I enforce a newline or a comma after the expression to
delimit it from the body of the statements.  everywhere you
see a "nloc" below, you shouldn't really need one; this
grammar therefore parses only a subset of "true" matlab.
(Of course, *I* think that reasonable code wouldn't have
such ambiguities, don't you?  :) )

Similarly:	

The statement
    while 1 -5, end;
is parsed as
    while (1)
      -5,
    end;
while the statement
    while 1 - 5, end;
is parsed as
    while (1-5)
      [empty]
    end;

NOTE : THIS FEELS A LOT LIKE THE VECTOR AMBIGUITIES BELOW.
Perhaps solving one solves them both, since both are essentially
'space-delimited expressions' ambiguities.

*/
	
if_statement
	: IF stmt_expression nlosoc statement_list elseif_statement* else_statement? END -> ^(IF stmt_expression statement_list elseif_statement* else_statement?)
	;

elseif_statement
	: ELSEIF stmt_expression nlosoc statement_list -> ^(ELSEIF stmt_expression statement_list)
	;
	
else_statement
	: ELSE statement_list -> ^(ELSE statement_list)
	;
		
for_statement
	: FOR ID EQ stmt_expression nloc statement_list END -> ^(FOR ID stmt_expression statement_list)
	| PARFOR ID EQ stmt_expression nloc statement_list END -> ^(PARFOR ID stmt_expression statement_list)
	;
	
while_statement
	: WHILE expression nlosoc statement_list END -> ^(WHILE expression statement_list)
	;
	
switch_statement
	: SWITCH expression nlosoc case_statement* otherwise_statement? END -> ^(SWITCH expression case_statement* otherwise_statement?)
	;
	
case_statement
	: CASE stmt_expression nlosoc statement_list -> ^(CASE stmt_expression statement_list)
	;

otherwise_statement
	: OTHERWISE nlosoc statement_list -> ^(OTHERWISE statement_list)
	;
	
try_statement
	: TRY statement_list catch_statement? END -> ^(TRY statement_list catch_statement?)
	;
	
catch_statement
        : CATCH ID? nlosoc statement_list -> ^(CATCH ID? statement_list)
	;
	
return_statement
	: RETURNS^ nlosoc
	;

break_statement
	: BREAK^ nlosoc
	;
	
continue_statement
	: CONTINUE^ nlosoc
	;

global_statement
	: GLOBAL (options {greedy=false;} : ID COMMA?)+ nlosoc -> ^(GLOBAL ID+ nlosoc)
	;

persistent_statement
	: PERSISTENT (options {greedy=false;} : ID COMMA?)+ nlosoc -> ^(PERSISTENT ID+ nlosoc)
	;

/*	
XXX CLEAR STATEMENT WILDCARDS

How can we fix wildcards in clear statements?
For example, "clear foo* bob" ought to be parsed as
  clear (foo*) (bob)
but this is hard since the '*' is lexed as its own
character, and we've discarded the fact that there is
whitespace between '*' and 'bob', but not between
'foo' and '*'.  This means we can't tell that it's supposed
to be part of the foo identifier.

Options	for solving:
1) parser-context sensitive lexing?  (I don't think this is right)
2) poke around in the hidden channel to find out where the whitespace is.
   this is probably the best solution, but seems like a pain.
   
*/

format_statement
	: FORMAT (options {greedy=false;} : ID COMMA?)* nlosoc -> ^(FORMAT ID* nlosoc)
	;

clear_statement
	: CLEAR (options {greedy=false;} : ID COMMA?)* nlosoc -> ^(CLEAR ID* nlosoc)
	;

clc_statement
	: CLC (options {greedy=false;} : ID COMMA?)* nlosoc -> ^(CLEAR ID* nlosoc)
	;

clearvars_statement
	: CLEARVARS (options {greedy=false;} : TIMES? ID TIMES? COMMA?)* nlosoc -> ^(CLEARVARS ID* nlosoc)
	;

close_statement
	: CLOSE (options {greedy=false;} : ID COMMA?)* nlosoc -> ^(CLOSE ID* nlosoc)
	;

hold_statement
	: HOLD (options {greedy=false;} : ID COMMA?)* nlosoc -> ^(HOLD ID* nlosoc)
	;

grid_statement
	: GRID (options {greedy=false;} : ID COMMA?)* nlosoc -> ^(GRID ID* nlosoc)
	;
//
// ===============================
//

// a precedence hierarchy for parsing expressions

// these are groups of operators that have equivalent precedences
g1	: ( NEQ | DOUBLE_EQ | GRTE | GRT | LSTE | LST );
g2	: ( PLUS | MINUS );
g3	: ( LEFTDIV | RIGHTDIV | TIMES | EL_LEFTDIV | EL_RIGHTDIV | EL_TIMES );
g4	: ( EXP | EL_EXP );

/*

XXX MATRIX TRANSPOSE PROBLEM.

The single quote operator is problematic because of things like this:	

aa' + foo('some string here')+bb'

Right now, the operator is placed in the correct place in the grammar,
and the grammar checks out just fine, but you get lexing errors if you
try to use it.

*/

postfix_operator
	: ( CCT | EL_CCT );
prefix_operator
	: ( PLUS | MINUS | NEG );

// the hierarchy is defined from LOWEST to HIGHEST priority.
// valid only inside apply nodes
expression : e0 -> ^(EXPRESSION e0);

e0	: e1;

e1	: e2 (LOG_OR^ e2)*;
e2	: e3 (LOG_AND^ e3)*;
e3	: e4 (BIN_OR^ e4)*;
e4	: e5 (BIN_AND^ e5)*;
e5	: e6 (g1^ e6)*;
e6	: e7 (COLON^ e7)*;
// END SYNTAX END-1 
e7	:  e8 (g2^ e8)*;
//e7	: e8 (g2^ e8)*;
e8	: e9 (g3^ e9)*;

e9	: prefix_operator^ e9 | e10;
e10	: e11 (g4^ e11)*; // note: in matlab, exponentiation is left-associative
e11	: unary_expression postfix_operator^?;

unary_expression
	: base_expression
	| LPAREN expression RPAREN -> ^( PARENS expression )
	;

base_expression
	: id_plus_indexers
	| g2^ INT
	| g2^ FLOAT
	| END
	| INT
	| FLOAT
	| STRING
	| anon_func_handle
	| cell
	| matrix
	;

// subset of expressions valid for toplevel statements only
// when inside an apply, then full 'expression' path is used
stmt_expression : se0 -> ^(EXPRESSION se0);
se0	: se1;
se1	: se2 (LOG_OR^ se2)*;
se2	: se3 (LOG_AND^ se3)*;
se3	: se4 (BIN_OR^ se4)*;
se4	: se5 (BIN_AND^ se5)*;
se5	: se6 (g1^ se6)*;
se6	: se7 (COLON^ se7)*;
se7	: se8 (g2^ se8)*;
se8	: se9 (g3^ se9)*;
se9	: prefix_operator^ se9 | se10;
se10	: se11 (g4^ se11)*; // note: in matlab, exponentiation is left-associative
se11	: stmt_unary_expression postfix_operator^?;

stmt_unary_expression
	: stmt_base_expression
	| LPAREN stmt_expression RPAREN -> ^( PARENS stmt_expression )
	;

stmt_base_expression
	: id_plus_indexers
	| g2^ INT
	| g2^ FLOAT
	| INT
	| FLOAT
	| STRING
	| anon_func_handle
	| cell
	| stmt_matrix
	;

lhs_base_expression
	: id_plus_indexers
	| g2^ INT
	| g2^ FLOAT
	| INT
	| FLOAT
	| STRING
	| cell
	| lhs_matrix
	;

//string
//	: CCT str=(.)* CCT -> ^(STRING $str)
//	;

/*
XXX ANONYMOUS EXPRESSION AMBIGUITIES

This generates a ton of warnings, but it does the right thing.
At least, I think it does.

We want anonymous expressions to be "greedy".

The statement
    a+@()x+y
parses as
   a+( @()x+y )
and not as
   a+ ( @()x ) +y
but I can't seem to make the right behavior explicit.  The way that
ANTLR is disabling the alternatives seems to result in the right
behavior, though.

If you comment out the second alternative here, the grammar should
check out perfectly clean.
*/

anon_func_handle
	: AT ID -> ^(AT ID)
	| AT parameter_list (options{greedy=true;}: stmt_expression ) -> ^(AT parameter_list stmt_expression)
	;

id_plus_indexers
	: ID -> ^(ID_NODE ID)
	| i1=ID LPAREN RPAREN -> ^(APPLY $i1 VOID)
	| i2=ID LPAREN fpl1=function_parameter_list RPAREN -> ^(APPLY $i2 $fpl1)
	| i3=ID LBRACE fpl2=function_parameter_list RBRACE -> ^(CELLACCESS  $i3 $fpl2)
	| fieldaccess -> ^(FIELDACCESS fieldaccess)
	| i4=fieldaccess LPAREN RPAREN -> ^(APPLY ^(FIELDACCESS $i4) VOID) 
	| i5=fieldaccess LPAREN fpl3=function_parameter_list RPAREN -> ^(APPLY ^(FIELDACCESS $i5) $fpl3)
	| i6=fieldaccess LBRACE fpl4=function_parameter_list RBRACE -> ^(CELLACCESS  ^(FIELDACCESS $i6) $fpl4)
	;

fieldaccess
	: ID ( DOT ID )+
	;

// also permits the use of the colon as an "expression"
function_parameter_list
	: function_parameter ( COMMA function_parameter )* -> ^(FUNCTION_PARAMETER_LIST function_parameter*)
	;
	
function_parameter 
	: expression
	| COLON
	;

stmt_matrix	: LSBRACE stmt_vector ( vector_term+ stmt_vector )* RSBRACE -> ^(MATRIX stmt_vector+);
lhs_matrix	: LSBRACE lhs_vector ( vector_term+ lhs_vector )* RSBRACE -> ^(MATRIX lhs_vector+);
matrix	: LSBRACE vector ( vector_term+ vector )* RSBRACE -> ^(MATRIX vector+);

cell	: LBRACE vector ( vector_term+ vector )* RBRACE -> ^(CELL vector+);

/*

XXX

I think the rule is the following

1) if a +/- does not have any space to the right, it's interpreted
   as a unary op
2) if there's no whitespace to the left of the operator, it's
   grouped to the left.
3) if there's whitespace to the right and left, it's treated as
   a binary op, and expressions are required on the right and left.

Vectors are pretty crazy.  Because you have a list of expressions
that can be separated by nothing but whitespace, all sorts of parsing
ambiguities start happening.  Whitespace becomes syntactically
meaningful in strange ways that are not totally context free.
For example,

[ a + b ]	is parsed as 	[ (a+b) ]
[ a+b ]		is parsed as 	[ (a+b) ]
[ a +b ]	is parsed as	[ (a) (+b) ]

A longer example:	

[ 1+  2+  3 ]   parses to   6		  
[ 1 + 2+  3 ]		    6		  
[ 1  +2+  3 ]		    1     5	  
[ 1+  2 + 3 ]		    6		  
[ 1 + 2 + 3 ]		    6		  
[ 1  +2 + 3 ]		    1     5	  
[ 1+  2  +3 ]		    3     3	  
[ 1 + 2  +3 ]		    3     3	  
[ 1  +2  +3 ]		    1     2     3

So, the weird thing is that we can't just look for the absence of a 
space between '+' and the next character to determine if it's a binary
or unary operator -- we also have to know if there's a space to the
left of it.

This is mostly a problem for prefix operators which can be ambiguous
in this context, but it also shows up in things like cell arrays
of anonymous function expressions.

size( { @()a+b } )	is	[ 1 1 ]
size( { @()a +b } )	is	[ 1 2 ]
size( { @()a + b } )	is	[ 1 1 ]

NOTE	:	 this feels like the control flow ambiguities above,
since both are essentially "whitespace-delimited expression" problems.

*/

// XXX The COMMA should really have a ? after it!!!
//vector	: ( expression COMMA? )+ -> ^(VECTOR expression+);

stmt_vector 
	: /*epsilon*/ -> ^(VECTOR VOID)
	| stmt_vector_part ( vector_sep stmt_vector_part )* -> ^(VECTOR stmt_vector_part+)
	//| ( expression WS* )=>                                                     expression WS* ( ( COMMA  WS* | WS+) expression )* -> ^(VECTOR expression+)
	;	
lhs_vector 
	: /*epsilon*/ -> ^(VECTOR VOID)
	| lhs_vector_part ( vector_sep lhs_vector_part )* -> ^(VECTOR lhs_vector_part+)
	//| ( expression WS* )=>                                                     expression WS* ( ( COMMA  WS* | WS+) expression )* -> ^(VECTOR expression+)
	;	
vector	
	: /*epsilon*/ -> ^(VECTOR VOID)
	| vector_part ( vector_sep vector_part )* -> ^(VECTOR vector_part+)
	//| ( expression WS* )=>                                                     expression WS* ( ( COMMA  WS* | WS+) expression )* -> ^(VECTOR expression+)
	;

lhs_vector_part
	: NEG /*tilde allowed on the left-hand side*/
	| stmt_expression
	;
	
stmt_vector_part
	: stmt_expression
	;

vector_part
	: expression
	;
	
vector_sep
	: ( WS* COMMA WS* | WS+)
	;
	
vector_term	
	: WS* ( NL | SEMI ) WS*
	| WS* COMMENT LINECOMMENT
	;

//vector	:	expression ( WS+ expression )* -> ^(VECTOR expression+);

//
// ==================================================================
//
// LEXER RULES
//

//
// language keywords
//

//
// Other useful language snippets
//


// WS needed because in [ ] spaces are not omitted (keepws = true)
LPAREN	: '(' WS*
	;
RPAREN	: (WS*')' '\'' )=> WS* ')'  {transpose=true;}
	| WS* ')' {transpose=false;}
	;
LBRACE	: '{' WS*
	;
RBRACE	: WS* '}'
	;
LSBRACE	: '[' WS* { keepws = true; }
	;
RSBRACE	: (WS* ']' '\'' )=> WS* ']' { keepws = false; transpose=true; }
	| WS* ']'                            { keepws = false; transpose=false; }
	;

COMMA	: WS* ',' WS*
	;

AT	: '@';
DOT	: '.';

// XXX I can't seem to get block comments to work.  The problem is that
// no matter what I do, the linecomment ends up "overriding" the block
// comment, and I get a lex error.  I've tried syntactic predicates,
// but they didn't help...
// If I comment out the LINECOMMENT rule, the BLOCKCOMMENT works fine.
// So, since I can only seem to have one or the other, I'm commenting
// out BLOCKCOMMENT for now.

//BLOCKCOMMENT
//        : '%{' (options{greedy=false;} : .)*  '%}' { $channel = HIDDEN; }
//	;


//match only if we are inside a string definition (match all the tokens)
//IN_STRING_CHARS
//	: {inString}?=> ~('\'' | '\n' )*
//	;


WS 
	: {!keepws}?=> ( ' ' | '\t' ) {$channel=HIDDEN;}
	| {keepws}?=> ( ' ' | '\t' )
	;

COMMENT	: '%'+	{ comment=true; }
	;

CCT	//: ('\'' ( . )* '\'')=> '\'' { gParent.inString = !gParent.transpose; }
	: '\''  	        { transpose = false;  }
	;

BREAK	: 'break';
CASE	: 'case';
CATCH	: 'catch';
CONTINUE: 'continue';
ELSE	: 'else';
ELSEIF	: 'elseif';
END	: 'end';
FOR	: 'for';
PARFOR	: 'parfor';
FUNCTION: 'function';
GLOBAL	: 'global';
IF	: 'if';
OTHERWISE: 'otherwise';
PERSISTENT: 'persistent';
RETURNS	: 'return'; // not "RETURN" to avoid #define conflicts with readline.h
SWITCH	: 'switch';
TRY	: 'try';
VARARGIN: 'varargin';
WHILE	: 'while';
CLEAR	: 'clear';
CLC	: 'clc';
FORMAT	: 'format';
CLEARVARS	:'clearvars';
CLOSE	: 'close';
HOLD	: 'hold';
GRID	: 'grid';

//
// operators and assignments
//

DOUBLE_EQ	:   '==' ;

LOG_OR	:   '||' ;
LOG_AND	:   '&&' ;
LSTE	:   '<='  ;
GRTE	:   '>='  ;
NEQ	:   '~='  ;

EL_TIMES	:  WS* '.*'  WS*;
EL_LEFTDIV	:  WS* './'  WS*;
EL_RIGHTDIV	: WS*  '.\\'  WS*;
EL_EXP	: '.^';
EL_CCT	: '.\'';

EQ	:  '=' ;

BIN_OR	:  '|';
BIN_AND	:  '&' ;

LST	:   '<' ;
GRT	:   '>' ;

COLON	: ':';

PLUS	:  WS* '+' WS*;
MINUS	:  WS* '-' WS*;
NEG	: '~';
TIMES	:  WS* '*' WS*;

LEFTDIV	:  WS* '/'  WS*;
RIGHTDIV	:  WS* '\\'  WS*;

EXP	: '^';

//
// identifiers, strings, numbers, whitespace
//
INT	: '0'..'9'+ ;
ID	: (  ID_VALUE  '\'' )=> ID_VALUE { transpose=true; }
	| ID_VALUE { transpose=false; }
	;


fragment
ID_VALUE
	: ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
	;
	
STRING
	: { !transpose }?=> '\'' ~('\''|'\n' )* '\''
	;

// MIST SPECIFIC TEMPLATE ANNOTATIONS
//ANNOTATIONS
//	: ('%' (options {greedy=false;} : .)* '--<')
//	;

LINECOMMENT
	: { comment }?=> (options {greedy=false;} : .)* NL //{ $channel = HIDDEN; }
	;

THREEDOTS
	: ( WS* '...' COMMA? WS* NL ) { $channel=HIDDEN;}
	;

SEMI	: ';'
	;

NL	: '\r'? '\n'	{ comment=false; }
	;

FLOAT
	: ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
	| '.' ('0'..'9')+ EXPONENT?
	| ('0'..'9')+ EXPONENT
	;
fragment
EXPONENT
	: ('e'|'E') ('+'|'-')? ('0'..'9')+ ;
/*

fragment
HEX_DIGIT
	: ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
	: '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
	| UNICODE_ESC
	| OCTAL_ESC
	;

fragment
OCTAL_ESC
	: '\\' ('0'..'3') ('0'..'7') ('0'..'7')
	| '\\' ('0'..'7') ('0'..'7')
	| '\\' ('0'..'7')
	;

fragment
UNICODE_ESC
	: '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
	;

*/