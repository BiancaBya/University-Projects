/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_PARSER_TAB_H_INCLUDED
# define YY_YY_PARSER_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    KW_INT = 258,                  /* KW_INT  */
    KW_FLOAT = 259,                /* KW_FLOAT  */
    KW_STRUCT = 260,               /* KW_STRUCT  */
    KW_STD = 261,                  /* KW_STD  */
    KW_COUT = 262,                 /* KW_COUT  */
    KW_CIN = 263,                  /* KW_CIN  */
    KW_WHILE = 264,                /* KW_WHILE  */
    KW_IF = 265,                   /* KW_IF  */
    KW_ELSE = 266,                 /* KW_ELSE  */
    KW_RETURN = 267,               /* KW_RETURN  */
    KW_MAIN = 268,                 /* KW_MAIN  */
    KW_INCLUDE = 269,              /* KW_INCLUDE  */
    KW_USING = 270,                /* KW_USING  */
    KW_NAMESPACE = 271,            /* KW_NAMESPACE  */
    KW_IOSTREAM = 272,             /* KW_IOSTREAM  */
    OP_LE = 273,                   /* OP_LE  */
    OP_GE = 274,                   /* OP_GE  */
    OP_EQ = 275,                   /* OP_EQ  */
    OP_NE = 276,                   /* OP_NE  */
    OP_AND = 277,                  /* OP_AND  */
    OP_OR = 278,                   /* OP_OR  */
    OP_SHL = 279,                  /* OP_SHL  */
    OP_SHR = 280,                  /* OP_SHR  */
    OP_PLUS = 281,                 /* OP_PLUS  */
    OP_MINUS = 282,                /* OP_MINUS  */
    OP_MUL = 283,                  /* OP_MUL  */
    OP_DIV = 284,                  /* OP_DIV  */
    OP_MOD = 285,                  /* OP_MOD  */
    OP_LT = 286,                   /* OP_LT  */
    OP_GT = 287,                   /* OP_GT  */
    OP_ASSIGN = 288,               /* OP_ASSIGN  */
    OP_NOT = 289,                  /* OP_NOT  */
    LPAREN = 290,                  /* LPAREN  */
    RPAREN = 291,                  /* RPAREN  */
    LBRACE = 292,                  /* LBRACE  */
    RBRACE = 293,                  /* RBRACE  */
    LBRACK = 294,                  /* LBRACK  */
    RBRACK = 295,                  /* RBRACK  */
    COMMA = 296,                   /* COMMA  */
    SEMICOLON = 297,               /* SEMICOLON  */
    COLON = 298,                   /* COLON  */
    DOT = 299,                     /* DOT  */
    QUOTE = 300,                   /* QUOTE  */
    APOSTROPHE = 301,              /* APOSTROPHE  */
    ID = 302,                      /* ID  */
    STRING = 303,                  /* STRING  */
    INT_CONST = 304,               /* INT_CONST  */
    FLOAT_CONST = 305,             /* FLOAT_CONST  */
    UMINUS = 306,                  /* UMINUS  */
    UPLUS = 307                    /* UPLUS  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
union YYSTYPE
{
#line 18 "parser.y"

    char* str;

#line 120 "parser.tab.h"

};
typedef union YYSTYPE YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_PARSER_TAB_H_INCLUDED  */
