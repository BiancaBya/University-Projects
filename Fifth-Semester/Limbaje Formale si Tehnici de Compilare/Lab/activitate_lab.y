%{
    #include <iostream>
    #include <cstdio>

    using namespace std;

    extern FILE* yyin;
    int yylex();
    void yyerror(const char* s) { }
%}

%token LPAREN RPAREN
%token NUM
%token MUL

%start input

%%

input
    : seq           { cout << "Toppp\n"; }
    ;

seq
    :
    | seq elem
    ;

elem
    : NUM
    | MUL
    | LPAREN seq RPAREN
    ;

%%

int main(int argc, char** argv) {
    if (argc > 1)
        yyin = fopen(argv[1], "r");

    int rc = yyparse();
    if (rc != 0)
        cout << "Nope\n";
    return 0;
}
