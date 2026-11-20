%{
#include "activitate_lab.tab.h"
%}

%%

[0-9]+      { return NUM; }
"*"         { return MUL; }
"("         { return LPAREN; }
")"         { return RPAREN; }

[ \t\r\n]+  ;

.           ;

%%

int yywrap() { return 1; }
