%{
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <string>
#include <unordered_set>
#include <vector>
#include <iostream>

using std::string;

extern int yylex();
extern FILE* yyin;

static void yyerror(const char* s);

static std::unordered_set<string> sym;
static std::vector<string> decl_order;

static string text_code;

static void ensure_declared(const string& id) {
    if (!sym.count(id)) {
        std::cerr << "Semantic error: variable '" << id << "' used before declaration.\n";
        exit(1);
    }
}

static void declare_id(const string& id) {
    if (!sym.count(id)) {
        sym.insert(id);
        decl_order.push_back(id);
    }
}


static string emit_expr_id(const string& id) {
    ensure_declared(id);
    return "    mov eax, [" + id + "]\n";
}

static string emit_expr_int(const string& v) {
    return "    mov eax, " + v + "\n";
}

static string combine_bin(const string& left, const string& right, const string& op) {
    string c;
    c += left;
    c += "    push eax\n";
    c += right;
    c += "    pop ecx\n";

    if (op == "+") {
        c += "    add eax, ecx\n";
    } else if (op == "-") {
        c += "    sub ecx, eax\n";
        c += "    mov eax, ecx\n";
    } else if (op == "*") {
        c += "    imul eax, ecx\n";
    }
    return c;
}


static string gen_read(const string& id) {
    ensure_declared(id);
    string c;
    c += "    push dword " + id + "\n";
    c += "    push dword format_in\n";
    c += "    call [scanf]\n";
    c += "    add esp, 4*2\n";
    return c;
}

static string gen_write(const string& id) {
    ensure_declared(id);
    string c;
    c += "    push dword [" + id + "]\n";
    c += "    push dword format_out\n";
    c += "    call [printf]\n";
    c += "    add esp, 4*2\n";
    return c;
}

static string gen_assign(const string& id, const string& expr_code) {
    ensure_declared(id);
    string c;
    c += expr_code;
    c += "    mov [" + id + "], eax\n";
    return c;
}


static void emit_program_asm() {
    std::cout
        << "bits 32\n\n"
        << "global start\n"
        << "extern exit, printf, scanf\n\n"
        << "import exit msvcrt.dll\n"
        << "import printf msvcrt.dll\n"
        << "import scanf msvcrt.dll\n\n"
        << "segment data use32 class = data\n\n"
        << "    format_in  db \"%d\", 0\n"
        << "    format_out db \"%d\", 13, 10, 0\n\n";

    for (const auto& v : decl_order) {
        std::cout << "    " << v << " dd 0\n";
    }

    std::cout
        << "\nsegment code use32 class = code\n"
        << "    start:\n"
        << text_code
        << "\n    push dword 0\n"
        << "    call [exit]\n";
}

%}

%code requires {
  #include <string>
}

%union { char* str; std::string* code; }

%token KW_INT KW_READ KW_WRITE
%token OP_ARROW OP_PLUS OP_MINUS OP_MUL
%token LPAREN RPAREN COMMA SEMICOLON
%token <str> ID
%token <str> INT_CONST
%token INVALID

%type <code> expr term factor

%start program

%%

program
    : decls stmts
      {
        emit_program_asm();
      }
    ;

decls
    : decls decl
    |
    ;

decl
    : KW_INT id_list SEMICOLON
    ;

id_list
    : ID
      {
        declare_id($1);
        free($1);
      }
    | ID COMMA id_list
      {
        declare_id($1);
        free($1);
      }
    ;

stmts
    : stmts stmt
    |
    ;

stmt
    : ID OP_ARROW expr SEMICOLON
      {
        string id = $1;
        free($1);
        text_code += gen_assign(id, *$3);
        delete $3;
      }
    | KW_READ LPAREN ID RPAREN SEMICOLON
      {
        string id = $3;
        free($3);
        text_code += gen_read(id);
      }
    | KW_WRITE LPAREN ID RPAREN SEMICOLON
      {
        string id = $3;
        free($3);
        text_code += gen_write(id);
      }
    ;

expr
    : expr OP_PLUS term
      {
        $$ = new string(combine_bin(*$1, *$3, "+"));
        delete $1; delete $3;
      }
    | expr OP_MINUS term
      {
        $$ = new string(combine_bin(*$1, *$3, "-"));
        delete $1; delete $3;
      }
    | term
      { $$ = $1; }
    ;

term
    : term OP_MUL factor
      {
        $$ = new string(combine_bin(*$1, *$3, "*"));
        delete $1; delete $3;
      }
    | factor
      { $$ = $1; }
    ;

factor
    : INT_CONST
      {
        $$ = new string(emit_expr_int($1));
        free($1);
      }
    | ID
      {
        string id = $1;
        free($1);
        $$ = new string(emit_expr_id(id));
      }
    | LPAREN expr RPAREN
      { $$ = $2; }
    ;

%%

static void yyerror(const char* s) {
    std::cerr << "Parse error: " << s << "\n";
}

int main(int argc, char** argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            std::cerr << "Cannot open file " << argv[1] << "\n";
            return 1;
        }
    }
    int rc = yyparse();
    if (rc != 0) return 1;
    return 0;
}
