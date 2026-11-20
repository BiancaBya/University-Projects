%{
#include <iostream>
#include <cstdio>
#include <cstdlib>

using namespace std;

int line = 1;

void print_token(const char* type) {
    cout << type << "\t" << yytext << "\t(line " << line << ")\n";
}

#ifdef BUILD_PARSER
#include "parser.tab.h"
extern YYSTYPE yylval;
    #define RET(tok) return tok;
#else
    #define RET(tok)
#endif

%}

%option noyywrap

LETTER      [a-zA-Z]
DIGIT       [0-9]
NZDIGIT     [1-9]
ID          {LETTER}({LETTER}|{DIGIT}|_)*
INTCONST    [+-]?(0|{NZDIGIT}{DIGIT}*)
FLOATCONST  [+-]?{DIGIT}*\.{DIGIT}*{NZDIGIT}
WS          [ \t\r]+

%%

\n              { line++; }
{WS}            { }


"int"           { print_token("KW_INT");       RET(KW_INT) }
"float"         { print_token("KW_FLOAT");     RET(KW_FLOAT) }
"struct"        { print_token("KW_STRUCT");    RET(KW_STRUCT) }
"std"           { print_token("KW_STD");       RET(KW_STD) }
"cout"          { print_token("KW_COUT");      RET(KW_COUT) }
"cin"           { print_token("KW_CIN");       RET(KW_CIN) }
"while"         { print_token("KW_WHILE");     RET(KW_WHILE) }
"if"            { print_token("KW_IF");        RET(KW_IF) }
"else"          { print_token("KW_ELSE");      RET(KW_ELSE) }
"return"        { print_token("KW_RETURN");    RET(KW_RETURN) }
"main"          { print_token("KW_MAIN");      RET(KW_MAIN) }
"#include"      { print_token("KW_INCLUDE");   RET(KW_INCLUDE) }
"using"         { print_token("KW_USING");     RET(KW_USING) }
"namespace"     { print_token("KW_NAMESPACE"); RET(KW_NAMESPACE) }
"iostream"      { print_token("KW_IOSTREAM");  RET(KW_IOSTREAM) }


"<="            { print_token("OP_LE");    RET(OP_LE) }
">="            { print_token("OP_GE");    RET(OP_GE) }
"=="            { print_token("OP_EQ");    RET(OP_EQ) }
"!="            { print_token("OP_NE");    RET(OP_NE) }
"&&"            { print_token("OP_AND");   RET(OP_AND) }
"||"            { print_token("OP_OR");    RET(OP_OR) }
"<<"            { print_token("OP_SHL");   RET(OP_SHL) }
">>"            { print_token("OP_SHR");   RET(OP_SHR) }

"+"             { print_token("OP_PLUS");   RET(OP_PLUS) }
"-"             { print_token("OP_MINUS");  RET(OP_MINUS) }
"*"             { print_token("OP_MUL");    RET(OP_MUL) }
"/"             { print_token("OP_DIV");    RET(OP_DIV) }
"%"             { print_token("OP_MOD");    RET(OP_MOD) }
"<"             { print_token("OP_LT");     RET(OP_LT) }
">"             { print_token("OP_GT");     RET(OP_GT) }
"="             { print_token("OP_ASSIGN"); RET(OP_ASSIGN) }
"!"             { print_token("OP_NOT");    RET(OP_NOT) }


"("             { print_token("LPAREN");    RET(LPAREN) }
")"             { print_token("RPAREN");    RET(RPAREN) }
"{"             { print_token("LBRACE");    RET(LBRACE) }
"}"             { print_token("RBRACE");    RET(RBRACE) }
"["             { print_token("LBRACK");    RET(LBRACK) }
"]"             { print_token("RBRACK");    RET(RBRACK) }
","             { print_token("COMMA");     RET(COMMA) }
";"             { print_token("SEMICOLON"); RET(SEMICOLON) }
":"             { print_token("COLON");     RET(COLON) }
"."             { print_token("DOT");       RET(DOT) }

"\""            { print_token("QUOTE");      RET(QUOTE) }
"\'"            { print_token("APOSTROPHE"); RET(APOSTROPHE) }


\"[^\n\"]*\"    {
                    print_token("STRING");
#ifdef BUILD_PARSER
                    yylval.str = strdup(yytext);
#endif
                    RET(STRING)
                }

{FLOATCONST}    {
                    print_token("FLOAT_CONST");
#ifdef BUILD_PARSER
                    yylval.str = strdup(yytext);
#endif
                    RET(FLOAT_CONST)
                }

{INTCONST}      {
                    print_token("INT_CONST");
#ifdef BUILD_PARSER
                    yylval.str = strdup(yytext);
#endif
                    RET(INT_CONST)
                }

{ID}            {
                    print_token("ID");
#ifdef BUILD_PARSER
                    yylval.str = strdup(yytext);
#endif
                    RET(ID)
                }

.               {
                    cout << "LEXICAL_ERROR\t" << yytext
                         << "\t(line " << line << ")\n";
                    return 0;
                }

%%

#ifndef BUILD_PARSER
int main(int argc, char** argv) {
    if (argc > 1) {
        FILE* f = fopen(argv[1], "r");
        if (!f) {
            cerr << "Cannot open file " << argv[1] << "\n";
            return 1;
        }
        yyin = f;
    }
    yylex();
    return 0;
}
#endif
