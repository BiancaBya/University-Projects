%{
    #include <iostream>
    #include <cstdio>
    #include <cstdlib>

    using namespace std;

    extern int yylex();
    extern FILE *yyin;

    extern int line;

    void yyerror(const char* s) {
        cerr << "Syntax error at line " << line << ": " << s << "\n";
    }
%}

%union {
    char* str;
}

%token KW_INT KW_FLOAT KW_STRUCT KW_STD KW_COUT KW_CIN KW_WHILE KW_IF KW_ELSE KW_RETURN KW_MAIN
%token KW_INCLUDE KW_USING KW_NAMESPACE KW_IOSTREAM
%token OP_LE OP_GE OP_EQ OP_NE OP_AND OP_OR OP_SHL OP_SHR
%token OP_PLUS OP_MINUS OP_MUL OP_DIV OP_MOD OP_LT OP_GT OP_ASSIGN OP_NOT
%token LPAREN RPAREN LBRACE RBRACE LBRACK RBRACK COMMA SEMICOLON COLON DOT QUOTE APOSTROPHE
%token <str> ID
%token <str> STRING
%token <str> INT_CONST
%token <str> FLOAT_CONST

/* priority for shift/reduce */

%left OP_OR
%left OP_AND
%left OP_EQ OP_NE
%left OP_LT OP_LE OP_GT OP_GE
%left OP_PLUS OP_MINUS
%left OP_MUL OP_DIV OP_MOD
%right OP_NOT
%right UMINUS UPLUS

%start program

%%


program
    : libraries KW_INT KW_MAIN LPAREN RPAREN LBRACE program_body return_stmt RBRACE
    ;

libraries
    : KW_INCLUDE OP_LT KW_IOSTREAM OP_GT KW_USING KW_NAMESPACE KW_STD SEMICOLON
    ;

program_body
    : decl_list instr_list
    ;


decl_list
    : decl_list decl
    |
    ;

decl
    : type id_list SEMICOLON
    | struct_def
    ;

id_list
    : ID
    | ID COMMA id_list
    ;

type
    : simple_type
    | struct_type
    ;

simple_type
    : KW_INT
    | KW_FLOAT
    ;

struct_def
    : KW_STRUCT ID LBRACE decl_list RBRACE SEMICOLON
    ;

struct_type
    : KW_STRUCT ID
    ;


instr_list
    : instr_list instr
    |
    ;

instr
    : assign_stmt SEMICOLON
    | read_stmt   SEMICOLON
    | write_stmt  SEMICOLON
    | cond_stmt
    | while_stmt
    ;

assign_stmt
    : ID OP_ASSIGN expr
    ;

expr
    : expr OP_PLUS term
    | expr OP_MINUS term
    | term
    ;

term
    : term OP_MUL factor
    | term OP_DIV factor
    | term OP_MOD factor
    | factor
    ;

factor
    : ID
    | const_value
    | LPAREN expr RPAREN
    | OP_MINUS factor %prec UMINUS
    | OP_PLUS factor  %prec UPLUS
    ;

const_value
    : INT_CONST
    | FLOAT_CONST
    ;


read_stmt
    : KW_CIN OP_SHR ID read_tail
    ;

read_tail
    : OP_SHR ID read_tail
    |
    ;

write_stmt
    : KW_COUT OP_SHL write_expr write_tail
    ;

write_tail
    : OP_SHL write_expr write_tail
    |
    ;

write_expr
    : expr
    | STRING
    ;


cond_stmt
    : KW_IF LPAREN bool_expr RPAREN LBRACE instr_list RBRACE else_part
    ;

else_part
    : KW_ELSE LBRACE instr_list RBRACE
    |
    ;

bool_expr
    : bool_expr OP_OR bool_term
    | bool_term
    ;

bool_term
    : bool_term OP_AND bool_factor
    | bool_factor
    ;

bool_factor
    : comparison
    | OP_NOT bool_factor
    | LPAREN bool_expr RPAREN
    ;

comparison
    : operand comp_op operand
    | OP_NOT operand
    ;

operand
    : ID
    | const_value
    ;

comp_op
    : OP_LT
    | OP_LE
    | OP_GT
    | OP_GE
    | OP_EQ
    | OP_NE
    ;


while_stmt
    : KW_WHILE LPAREN bool_expr RPAREN LBRACE instr_list RBRACE
    ;

return_stmt
    : KW_RETURN INT_CONST SEMICOLON
    ;

%%


int main(int argc, char** argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            cerr << "Cannot open file " << argv[1] << "\n";
            return 1;
        }
    }

    int rc = yyparse();
    if (rc == 0) {
        cout << "\nTOP\n";
    } else {
        cout << "\nSyntactic error!!\n";
    }
    return rc;
}


