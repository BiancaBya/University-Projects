%{
#include <iostream>
#include <cstdio>

using namespace std;

extern FILE *yyin;
int line = 1;

void print_token(const char* type) {
    cout << type << "\t" << yytext << "\t(line " << line << ")\n";
}
%}

%option noyywrap

LETTER      [a-zA-Z]
DIGIT       [0-9]
NZDIGIT     [1-9]
ID          {LETTER}({LETTER}|{DIGIT})*
INTCONST    [+-]?{NZDIGIT}{DIGIT}*
FLOATCONST  [+-]?{DIGIT}*\.{DIGIT}*{NZDIGIT}

WS          [ \t\r]+

%%

\n              { line++; }

{WS}            { /* ignoră */ }

"int"           { print_token("KW_INT"); }
"float"         { print_token("KW_FLOAT"); }
"struct"        { print_token("KW_STRUCT"); }
"std"           { print_token("KW_STD"); }
"cout"          { print_token("KW_COUT"); }
"cin"           { print_token("KW_CIN"); }
"while"         { print_token("KW_WHILE"); }
"do"            { print_token("KW_DO"); }
"if"            { print_token("KW_IF"); }
"else"          { print_token("KW_ELSE"); }
"return"        { print_token("KW_RETURN"); }
"main"          { print_token("KW_MAIN"); }
"#include"      { print_token("KW_INCLUDE"); }
"using"         { print_token("KW_USING"); }
"namespace"     { print_token("KW_NAMESPACE"); }
"iostream"      { print_token("KW_IOSTREAM"); }

"<="            { print_token("OP_LE"); }
">="            { print_token("OP_GE"); }
"=="            { print_token("OP_EQ"); }
"!="            { print_token("OP_NE"); }
"&&"            { print_token("OP_AND"); }
"||"            { print_token("OP_OR"); }
"<<"            { print_token("OP_SHL"); }
">>"            { print_token("OP_SHR"); }

"+"             { print_token("OP_PLUS"); }
"-"             { print_token("OP_MINUS"); }
"*"             { print_token("OP_MUL"); }
"/"             { print_token("OP_DIV"); }
"%"             { print_token("OP_MOD"); }
"<"             { print_token("OP_LT"); }
">"             { print_token("OP_GT"); }
"="             { print_token("OP_ASSIGN"); }
"!"             { print_token("OP_NOT"); }

"("             { print_token("LPAREN"); }
")"             { print_token("RPAREN"); }
"{"             { print_token("LBRACE"); }
"}"             { print_token("RBRACE"); }
"["             { print_token("LBRACK"); }
"]"             { print_token("RBRACK"); }
","             { print_token("COMMA"); }
";"             { print_token("SEMICOLON"); }
":"             { print_token("COLON"); }
"."             { print_token("DOT"); }
"\""            { print_token("QUOTE"); }
"\'"            { print_token("APOSTROPHE"); }

\"[^\n"]*\"     { print_token("STRING"); }

{FLOATCONST}    { print_token("FLOAT_CONST"); }
{INTCONST}      { print_token("INT_CONST"); }

{ID}            { print_token("ID"); }

.               {
                    cout << "LEXICAL_ERROR\t" << yytext
                         << "\t(line " << line << ")\n";
                }

%%

int main(int argc, char** argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            cerr << "Cannot open file " << argv[1] << "\n";
            return 1;
        }
    }
    yylex();
    return 0;
}


