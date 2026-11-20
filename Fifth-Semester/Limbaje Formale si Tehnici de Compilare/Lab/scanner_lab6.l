%{
#include "parser_lab6.tab.h"
#include <string.h>
%}

%option noyywrap

WS      [ \t\r\n]+
ID      [a-zA-Z_][a-zA-Z0-9_]*
NUM     [0-9]+

%%

{WS}                { /* skip */ }

"int"               { return KW_INT; }
"read"              { return KW_READ; }
"write"             { return KW_WRITE; }

"<-"                { return OP_ARROW; }

"+"                 { return OP_PLUS; }
"-"                 { return OP_MINUS; }
"*"                 { return OP_MUL; }

"("                 { return LPAREN; }
")"                 { return RPAREN; }
","                 { return COMMA; }
";"                 { return SEMICOLON; }

{NUM}               { yylval.str = strdup(yytext); return INT_CONST; }
{ID}                { yylval.str = strdup(yytext); return ID; }

.                   { return INVALID; }

%%
