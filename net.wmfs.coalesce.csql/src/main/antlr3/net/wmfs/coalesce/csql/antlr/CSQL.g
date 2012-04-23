grammar CSQL;

options {
	output=AST;
}

@header {
	package net.wmfs.coalesce.csql.antlr;
}
@lexer::header {
	package net.wmfs.coalesce.csql.antlr;
}
@rulecatch {
	catch (RecognitionException e) {
		throw(e);
	}
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

condition
	:	logical_term (OR^ logical_term )*;

logical_term 
	:	logical_factor (AND^ logical_factor )*;

logical_factor
	:	expression (
		 	null_operator^ | 
		 	comparison_operator^ expression | 
		 	set_operator^ (set | function) | 
		 	BETWEEN^ expression AND! expression
		 )?
	;

expression
	:	term ((MINUS|PLUS)^ term)*;

term 
	:	factor ((MULT|DIV)^ factor)*;

factor
	: 	function
	|	custom_item
	|	sql_literal
	|	LBRACKET! condition RBRACKET!
	; 

number 
	:	((PLUS|MINUS)^)? NUMBER;

sql_literal
	:	number | QUOTED_STRING | TRUE | FALSE;
	
null_operator
	:	ISNULL | ISNOTNULL;

comparison_operator
	:	EQ | NE | LT | LE | GT | GE; 
	
set_operator
	:	IN | NOTIN;

function
	:	((STD_FUNCTION|CUSTOM_ITEM)^) LBRACKET! expression (COMMA! expression)* RBRACKET!; 
	
custom_item
	:	CUSTOM_ITEM;

set	options {backtrack=true;}
	:	(LBRACKET! set RBRACKET!)
	|	LBRACKET expression (COMMA expression)* RBRACKET -> ^(SET expression+)
	;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

SET	:	'SET';
STD_FUNCTION
	:	'NVL' | 'TO_NUMBER';

NOT	:	'NOT';
AND	:	'AND';
OR	:	'OR';
EQ	:	'=';
NE	:	'<>' | '!=' | '^=';
LT	:	'<';
LE	:	'<=';
GT	:	'>';
GE	:	'>=';
IN	:	'IN';
NOTIN	:	'NOT IN';
ISNULL	:	'IS NULL';
ISNOTNULL
	:	'IS NOT NULL';
BETWEEN	:	'BETWEEN';

TRUE	:	'TRUE';
FALSE	:	'FALSE';
 
PLUS	:	'+';
MINUS	:	 '-';
MULT	:	 '*';
DIV	:	 '/';

LBRACKET:	'(';
RBRACKET:	')';

COMMA	:	',';

CUSTOM_ITEM
	:	':' IDENTIFIER_CHAR (IDENTIFIER_CHAR | '.' | DIGIT)* { setText(getText().substring(1)); };

fragment IDENTIFIER_CHAR
	:	'a'..'z'|'A'..'Z'|'_'|'$';

NUMBER	: (DIGIT+ '.')? DIGIT+;

QUOTED_STRING:	'\'' ('\'\'' | ~'\'')* '\'' ;

WS : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ {$channel = HIDDEN; };

fragment DIGIT	: '0'..'9' ;
