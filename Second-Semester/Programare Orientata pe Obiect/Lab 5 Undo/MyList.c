#include "MyList.h"
#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>

ElemType get(MyList *l, int poz){
    return l->elems[poz];
}

MyList *createEmpty(DestroyFct fct) {
    MyList *rez = malloc(sizeof(MyList));
    rez->cp = 50;
    rez->elems = malloc(sizeof(ElemType)*rez->cp);
    rez->lg = 0;
    rez->dfnc = fct;
    return rez;
}

void testCreateEmpty()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    assert(size(l) == 0);
    destroyList(l);
}

void destroyList(MyList* l) {
    int i;
    for(i=0;i<l->lg;i++)
    {
        ElemType el = l->elems[i];
        destroyBuget(el);
    }
    l->lg = 0;
    free(l->elems);
    free(l);
}

void test_destroy()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    add(l, createBuget("altele",2,2));
    destroyList(l);
}

void resize(MyList* l)
{
    l->cp *= 2;
    ElemType *aux = (ElemType *)realloc(l->elems, sizeof(ElemType) * l->cp);
    l->elems = aux;
}

void test_resize()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    resize(l);
    add(l, createBuget("altele", 1, 2));
    add(l, createBuget("altele", 2, 2));
    add(l, createBuget("altele", 3, 2));
    add(l, createBuget("altele", 4, 2));
    add(l, createBuget("altele", 5, 2));
    add(l, createBuget("altele", 6, 2));
    add(l, createBuget("altele", 7, 2));
    add(l, createBuget("altele", 8, 2));
    add(l, createBuget("altele", 9, 2));
    add(l, createBuget("altele", 10, 2));
    add(l, createBuget("altele", 11, 2));
    add(l, createBuget("altele", 12, 2));
    add(l, createBuget("altele", 13, 2));
    add(l, createBuget("altele", 14, 2));
    add(l, createBuget("altele", 15, 2));
    add(l, createBuget("altele", 16, 2));
    add(l, createBuget("altele", 17, 2));
    add(l, createBuget("altele", 18, 2));
    add(l, createBuget("altele", 19, 2));
    add(l, createBuget("altele", 20, 2));
    add(l, createBuget("altele", 21, 2));
    add(l, createBuget("altele", 22, 2));
    add(l, createBuget("altele", 23, 2));
    add(l, createBuget("altele", 24, 2));
    add(l, createBuget("altele", 25, 2));
    add(l, createBuget("altele", 26, 2));
    add(l, createBuget("altele", 27, 2));
    add(l, createBuget("altele", 28, 2));
    add(l, createBuget("altele", 29, 2));
    add(l, createBuget("altele", 30, 2));
    add(l, createBuget("altele", 31, 2));
    add(l, createBuget("imbracaminte", 1, 2));
    add(l, createBuget("imbracaminte", 2, 2));
    add(l, createBuget("imbracaminte", 3, 2));
    add(l, createBuget("imbracaminte", 4, 2));
    add(l, createBuget("imbracaminte", 5, 2));
    add(l, createBuget("imbracaminte", 6, 2));
    add(l, createBuget("imbracaminte", 7, 2));
    add(l, createBuget("imbracaminte", 8, 2));
    add(l, createBuget("imbracaminte", 9, 2));
    add(l, createBuget("imbracaminte", 10, 2));
    add(l, createBuget("imbracaminte", 11, 2));
    add(l, createBuget("imbracaminte", 12, 2));
    add(l, createBuget("imbracaminte", 13, 2));
    add(l, createBuget("imbracaminte", 14, 2));
    add(l, createBuget("imbracaminte", 15, 2));
    add(l, createBuget("imbracaminte", 16, 2));
    add(l, createBuget("imbracaminte", 17, 2));
    add(l, createBuget("imbracaminte", 18, 2));
    add(l, createBuget("imbracaminte", 19, 2));
    add(l, createBuget("imbracaminte", 20, 2));
    destroyList(l);
}


ElemType set(MyList* l,int poz, ElemType el)
{
    ElemType replaced = l->elems[poz];
    l->elems[poz] = el;
    return replaced;
    /*
    strcpy_s(l->elems[poz]->type, 30, type);
    l->elems[poz]->zi = day;
    l->elems[poz]->suma = sum;
    return l->elems[poz];
     */
}

void test_set()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("transport", 17, 3);
    add(l, p);
    set(l, 0, createBuget("telefon&internet", 7, 8));
    Buget *b = get(l, 0);
    assert(b->zi == 7);
    assert(b->suma == 8);
    assert(strcmp(b->type,"telefon&internet") == 0);

    destroyBuget(p);
    destroyList(l);
}

int size(MyList* l) {
    return l->lg;
}



void add(MyList* v, ElemType el) {
    if (v->lg >= v->cp) {

        int newCapacity = v->cp * 2;
        ElemType* aux = malloc(sizeof(ElemType)*newCapacity);
        for (int i = 0; i < v->lg; i++) {
            aux[i] = v->elems[i];
        }
        free(v->elems);
        v->elems = aux;
        v->cp = newCapacity;
    }
    v->elems[v->lg] = el;
    v->lg++;
}

MyList* copyList(MyList* l, CopyFct copyFct) {
    MyList* rez = createEmpty(l->dfnc);
    for (int i = 0; i < size(l); i++) {
        ElemType el = get(l,i);
        add(rez, copyFct(el));
    }
    return rez;
}

void testCopyList() {
    MyList* l = createEmpty((DestroyFct) destroyBuget);
    Buget* p = createBuget("a", 10, 10);
    add(l, p);
    Buget* t = createBuget("a2", 12, 20);
    add(l, t);
    MyList* l2 = copyList(l, (CopyFct) copyBuget);
    assert(size(l2) == 2);
    Buget* s = l2->elems[0];
    assert(strcmp(s->type, "a") == 0);
    destroyList(l2);
    destroyList(l);
}


int FindBuget(MyList *l, char* tip, int zi, float suma){

    int poz = -1;
    for (int i=0; i < l->lg; i++){
        Buget *p = get(l, i);
        if (strcmp(p->type, tip) == 0 && p->zi == zi && p->suma == suma){
            poz = i;
            break;
        }
    }
    return poz;

}


void test_find(){

    MyList *l = createEmpty((DestroyFct) destroyBuget);
    char tip[30] = "mancare";
    int zi = 1;
    float suma = 1;
    Buget *b = createBuget(tip, zi, suma);
    add(l,b);
    int poz = FindBuget(l, tip, zi, suma);
    assert(poz == 0);

    destroyList(l);

}


void delete(MyList* l, ElemType el)
{
    for(int i = 0;i < size(l);i++)
    {
        if(get(l, i) == el) {
            for (int j = i + 1; j < size(l); j++) {
                l->elems[j - 1] = l->elems[j];
            }
            i--;
            l->lg--;
        }
    }
}

void test_delete()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("altele", 29, 3);
    add(l, p);
    delete(l, p);
    destroyBuget(p);
    assert(size(l) == 0);
    destroyList(l);
}

int cmptype(Buget* a, Buget* b) {
    return  strcmp(a->type,b->type);
}

int cmpzi(Buget* a, Buget* b) {
    return a->zi - b->zi;
}

int cmpsuma(Buget* a, Buget* b) {
    return (int)(a->suma - b->suma);
}
void sort_list(MyList* l, comparare cmptype, int sortare)
{
    Buget *aux;
    int i, j;
    if(sortare == 1)
        for(i=0;i<size(l)-1;i++) {
            for (j = i + 1; j < size(l); j++) {
                if (cmptype(l->elems[i], l->elems[j])>=0) {
                    aux = l->elems[i];
                    l->elems[i] = l->elems[j];
                    l->elems[j] = aux;
                }
            }
        }
    if(sortare == -1)
        for (i = 0;i < size(l) - 1;i++) {
            for (j = i + 1; j < size(l); j++) {
                if (cmptype(l->elems[i], l->elems[j]) < 0) {
                    aux = l->elems[i];
                    l->elems[i] = l->elems[j];
                    l->elems[j] = aux;
                }
            }
        }
}

void test_sort_list()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("telefon&internet", 2, 3);
    add(l, p);
    Buget *t = createBuget("altele", 4, 5);
    add(l, t);
    sort_list(l, cmptype, 1);

    ElemType element = get(l,0);
    assert(element == t);
    element = get(l, 1);
    assert(element == p);

    sort_list(l, cmptype, -1);

    element = get(l, 1);
    assert(element == t);
    element = get(l, 0);
    assert(element == p);
    destroyList(l);

    MyList *l2 = createEmpty((DestroyFct) destroyBuget);
    Buget *p2 = createBuget("telefon&internet", 4, 3);
    add(l2, p2);
    Buget *t2 = createBuget("altele", 2, 5);
    add(l2, t2);
    sort_list(l2, cmpzi, 1);

    element = get(l2, 1);
    assert(element == p2);
    element = get(l2, 0);
    assert(element == t2);

    sort_list(l2,cmpzi,  -1);

    element = get(l2, 1);
    assert(element == t2);
    element = get(l2, 0);
    assert(element == p2);
    destroyList(l2);

    MyList *l3 = createEmpty((DestroyFct) destroyBuget);
    Buget *p3 = createBuget("telefon&internet", 4, 7);
    add(l3, p3);
    Buget *t3 = createBuget("altele", 2, 5);
    add(l3, t3);
    sort_list(l3,cmpsuma,  1);

    element = get(l3, 1);
    assert(element == p3);
    element = get(l3, 0);
    assert(element == t3);

    sort_list(l3,cmpsuma,  -1);
    element = get(l3, 1);
    assert(element == t3);
    element = get(l3, 0);
    assert(element == p3);
    destroyList(l3);
}

void filtrare_type(MyList *l, char criteriu[30])
{
    int i;
    for(i=0;i<size(l);i++) {

        Buget *b = get(l, i);

        if (strcmp(b->type, criteriu) != 0) {
            delete(l, l->elems[i]);
            i--;
        }
    }
}

void test_filtrare_type()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("telefon&internet", 4, 3);
    add(l, p);
    Buget *t = createBuget("altele", 2, 5);
    add(l, t);
    filtrare_type(l, "altele");
    destroyBuget(p);
    Buget *b = get(l, 0);
    assert(b->zi == 2);
    assert(b->suma == 5);
    assert(strcmp(b->type,"altele") == 0);
    assert(size(l) == 1);
    destroyList(l);
}

void filtrare_zi(MyList *l, int numar)
{
    int i;
    for(i=0;i<size(l);i++) {

        Buget *b = get(l, i);

        if (b->zi != numar) {
            delete(l, l->elems[i]);
            i--;
        }
    }
}

void test_filtrare_zi()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("telefon&internet", 4, 3);
    add(l, p);
    Buget *t = createBuget("altele", 2, 5);
    add(l, t);
    filtrare_zi(l, 2);
    destroyBuget(p);
    Buget *b = get(l, 0);
    assert(b->zi == 2);
    assert(b->suma == 5);
    assert(strcmp(b->type,"altele") == 0);
    assert(size(l) == 1);
    destroyList(l);
}

void filtrare_suma(MyList *l, float sum)
{
    int i;
    for(i=0;i<size(l);i++) {

        Buget *b = get(l, i);

        if (b->suma != sum) {
            delete(l, l->elems[i]);
            i--;
        }
    }
}

void test_filtrare_suma()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("telefon&internet", 4, 3);
    add(l, p);
    Buget *t = createBuget("altele", 2, 5);
    add(l, t);
    filtrare_suma(l, 5);
    destroyBuget(p);
    Buget *b = get(l, 0);
    assert(b->zi == 2);
    assert((int)b->suma == 5);
    assert(strcmp(b->type,"altele") == 0);
    assert(size(l) == 1);
    destroyList(l);
}

void filtrare_suma_mai_mare(MyList *l, float sum)
{
    int i;
    for(i=0;i<size(l);i++) {

        Buget *b = get(l, i);

        if (b->suma < sum) {
            delete(l, l->elems[i]);
            i--;
        }
    }
}


void test_filtrare_suma_mai_mare() {

    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("telefon&internet", 4, 3);
    add(l, p);
    Buget *t = createBuget("altele", 2, 5);
    add(l, t);
    filtrare_suma_mai_mare(l, 5);
    destroyBuget(p);
    Buget *b = get(l, 0);
    assert(b->zi == 2);
    assert((int)b->suma == 5);
    assert(strcmp(b->type,"altele") == 0);
    assert(size(l) == 1);
    destroyList(l);

}


