#ifndef BUGET_BUN_MYLIST_H
#define BUGET_BUN_MYLIST_H

#pragma once
#include "buget.h"

typedef void* ElemType;

typedef void (*DestroyFct)(ElemType);
typedef ElemType (*CopyFct)(ElemType);

typedef struct {
    ElemType* elems;
    int lg;
    int cp;
    DestroyFct dfnc;
} MyList;

typedef int(*comparare)(Buget*, Buget*);

MyList *createEmpty(DestroyFct fct);

void testCreateEmpty();

void destroyList(MyList* l);

void test_destroy();

void test_resize();

void getAllBuget(MyList *l);

void test_getAllBuget();


ElemType set(MyList* l,int poz, ElemType el);

int size(MyList* l);

void sort_list(MyList* l, comparare cmptype,  int sortare);

void filtrare_type(MyList* l, char criteriu[30]);

void filtrare_zi(MyList* l, int numar);

void filtrare_suma(MyList* l, float sum);

void add(MyList* l, ElemType el);

void delete(MyList* l, ElemType el);

void resize(MyList *l);

MyList* copyList(MyList* l, CopyFct copyFct);

void testCopyList();

void test_sort_list();

void test_set();

void test_delete();

void test_filtrare_type();

void test_filtrare_zi();

void test_filtrare_suma();

void test_filtrare_suma_mai_mare();

void filtrare_suma_mai_mare(MyList *l, float sum);

int cmptype(Buget* a, Buget* b);

int cmpzi(Buget* a, Buget* b);

int cmpsuma(Buget* a, Buget* b);

ElemType get(MyList *l, int poz);

int FindBuget(MyList *l, char* tip, int zi, float suma);

void test_find();

#endif //BUGET_BUN_MYLIST_H


