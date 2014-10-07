package hw02;

/**
 *  Graph.lex
 *  
 *  Generates a lexer for tokenizing graph calculator input.
 * 
 *  @author Emma Strubell
 *  COS397 - Chawathe
 *  February 17, 2011
 */

import java_cup.runtime.*;
import java.io.IOException;
import hw02.GraphSym;
import static hw02.GraphSym.*;

%%

%class GraphLex
%unicode
%line
%column
%final
%cupsym hw02.GraphSym
%cup

%{
	private Symbol sym(int type){
		return sym(type, yytext());
	}

	private Symbol sym(int type, Object value){
		return new Symbol(type, yyline, yycolumn, value);
	}

	private void error() throws IOException{
		throw new IOException(
		"illegal text at line = "+yyline+", column = "+yycolumn+", text = '"+yytext()+"'");
	}
%}

lineterminator = \r|\n|\r\n
whitespace     = {lineterminator}|[ \t\f]

/* A valid Java identifier:
 * Character.isJavaIdentifierStart(jletter) == true
 * Character.isJavaIdentifierPart(jletterdigit) == true
 */
identifier	   = [:jletter:][:jletterdigit:]*

%%
/* identifiers */
{identifier}		{return sym(GraphSym.IDENTIFIER);}

/* digits (a named graph order) */
[:digit:]			{return sym(GraphSym.ORDER);}

/* named graphs */
".K"				{return sym(GraphSym.COMPLETE);}
".P"				{return sym(GraphSym.PATH);}
".C"				{return sym(GraphSym.CYCLE);}
".W"				{return sym(GraphSym.WHEEL);}
".S"				{return sym(GraphSym.STAR);}

/* operators */
"--"				{return sym(GraphSym.EDGE);}

"-"					{return sym(GraphSym.COMPLEMENT);}
"(X)"				{return sym(GraphSym.DIRECT);}
"[ ]"				{return sym(GraphSym.CARTESIAN);}
"[X]"				{return sym(GraphSym.STRONG);}
"(^)"				{return sym(GraphSym.ODD);}
"(o)"				{return sym(GraphSym.LEXICOGRAPHIC);}
"(+)"				{return sym(GraphSym.SUM);}
"---"				{return sym(GraphSym.JOIN);}

/* various punctuation */
"print "			{return sym(GraphSym.PRINT);}
"="					{return sym(GraphSym.EQUALS);}
"("					{return sym(GraphSym.LPAREN);}
")"					{return sym(GraphSym.RPAREN);}
"["					{return sym(GraphSym.LBR);}
"]"					{return sym(GraphSym.RBR);}
";"					{return sym(GraphSym.SEMI);}
","					{return sym(GraphSym.COMMA);}

/* whitespace */
{whitespace}		{/* ignore whitespace */}

/* error */
.|\n				{error();}