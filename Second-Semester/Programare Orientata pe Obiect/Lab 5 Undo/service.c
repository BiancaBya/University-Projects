//#include "MyList.h"
#include "service.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <stdlib.h>


ManagerBuget createManagerBuget(){

    ManagerBuget store;
    store.allbugets = createEmpty((DestroyFct) destroyBuget);
    store.undolist = createEmpty((DestroyFct) destroyList);
    return store;

}

void destroyManagerBuget(ManagerBuget *buget){

    destroyList(buget->allbugets);
    destroyList(buget->undolist);

}

int valideaza(Buget *p, MyList* l) {
    if (p->zi<1 || p->zi>31) {
        return 1;
    }
    if (strcmp("telefon&internet",p->type) != 0 &&
        strcmp("imbracaminte",p->type) != 0 &&
        strcmp("transport",p->type) != 0 &&
        strcmp("mancare",p->type) != 0 &&
        strcmp("altele",p->type) != 0) {
        return 2;
    }
    if (p->suma < 0) {
        return 3;
    }
    int i;
    for(i=0;i<size(l);i++) {
        Buget *b = get(l, i);
        if(strcmp(b->type, p->type) == 0 && b->zi == p->zi) {
            return 4;
        }
    }
    return 0;
}

void testValidare()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("tableta", 12, 100);
    assert(valideaza(p, l) == 2);
    destroyBuget(p);
    Buget *t = createBuget("telefon&internet", 32, 100);
    assert(valideaza(t,l)==1);
    destroyBuget(t);
    Buget *x = createBuget("telefon&internet", 12, -3);
    assert(valideaza(x,l)==3);
    destroyBuget(x);
    Buget *s = createBuget("imbracaminte", 12, 10);
    assert(valideaza(s, l) == 0);
    add(l, s);
    Buget* y = createBuget("imbracaminte", 12, 10);
    assert(valideaza(y, l) == 4);
    destroyBuget(y);
    destroyList(l);
}

void modificare(ManagerBuget* buget, char* tip, int zi, float suma, char* type, int day, float sum)
{
    int i, ok = 0;
    for(i=0;i<size(buget->allbugets);i++)
    {
        Buget *b = get(buget->allbugets, i);
        if(strcmp(tip, b->type) == 0 && zi == b->zi && suma == b->suma)
        {
            Buget *t = createBuget(type, day, sum);
            int ver = valideaza(t, buget->allbugets);
            if(ver == 0) {
                set(buget->allbugets, i, createBuget(type, day, sum));
                ok = 1;
            }
            destroyBuget(t);
        }
    }
    if (ok) return;
}

void test_modificare()
{
    ManagerBuget buget = createManagerBuget();
    Buget *p = createBuget("altele", 23, 2);
    add(buget.allbugets, p);
    modificare(&buget, "altele", 23, 2,"imbracaminte", 20, 8);
    Buget *b = get(buget.allbugets, 0);
    assert(b->zi == 20);
    assert(b->suma == 8);
    assert(strcmp(b->type,"imbracaminte")==0);

    destroyBuget(p);
    destroyManagerBuget(&buget);
}

void delete_cmd(ManagerBuget *buget, ElemType el){

    for(int i = 0;i < size(buget->allbugets);i++)
    {
        if(get(buget->allbugets, i) == el) {
            for (int j = i + 1; j < size(buget->allbugets); j++) {
                buget->allbugets->elems[j - 1] = buget->allbugets->elems[j];
            }
            i--;
            buget->allbugets->lg--;
        }
    }

}

void test_delete_cmd(){

    ManagerBuget buget = createManagerBuget();
    Buget *b = createBuget("mancare", 1, 1);
    buget.allbugets->cp = 1;
    add(buget.allbugets, b);
    b = createBuget("mancare", 2, 2);
    add(buget.allbugets, b);
    delete_cmd(&buget,b);
    assert(buget.allbugets->lg == 1);

    destroyBuget(b);
    destroyManagerBuget(&buget);

}


ElemType delete_undo(MyList* v, int poz) {
    ElemType el = v->elems[poz];
    for (int i = poz; i < v->lg-1; i++) {
        v->elems[i] = v->elems[i + 1];
    }
    v->lg--;
    return el;
}


void test_delete_undo(){

    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *b = createBuget("mancare", 1, 1);

    add(l,b);
    free(b);

    b = createBuget("mancare", 2, 2);
    add(l,b);

    delete_undo(l, 0);
    assert(l->lg == 1);

    destroyList(l);

}


int undo(ManagerBuget* buget){

    if (size(buget->undolist) == 0)
        return 0;

    MyList *l = delete_undo(buget->undolist, buget->undolist->lg - 1);
    destroyList(buget->allbugets);
    buget->allbugets = l;
    return 1;

}


void test_undo(){

    ManagerBuget buget = createManagerBuget();
    Buget *b = createBuget("mancare", 1, 1);

    MyList  *toUndo = copyList(buget.allbugets, (CopyFct) copyBuget);
    add(buget.undolist, toUndo);

    add(buget.allbugets, b);
    b = createBuget("mancare", 2, 2);

    toUndo = copyList(buget.allbugets, (CopyFct) copyBuget);
    add(buget.undolist, toUndo);
    add(buget.allbugets, b);

    undo(&buget);
    assert(buget.undolist->lg == 1);
    undo(&buget);
    undo(&buget);
    assert(buget.undolist->lg == 0);

    destroyManagerBuget(&buget);

}

