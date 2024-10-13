#pragma warning(disable: 4996)
#include "buget.h"
#include <string.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

Buget *createBuget(char* type, int zi, float suma)
{
    Buget *rez = malloc(sizeof(Buget));
    strcpy(rez->type, type);
    rez->zi = zi;
    rez->suma = suma;
    return rez;
}

void testBuget()
{
    Buget *p = createBuget("telefon", 12, 10);
    assert(strcmp(p->type, "telefon") == 0);
    assert(p->zi == 12);
    assert(p->suma == 10);
    destroyBuget(p);
}

void destroyBuget(Buget* p) {
    free(p);
}

// create deep copy
Buget *copyBuget(Buget* buget){
    return createBuget(buget->type, buget->zi, buget->suma);
}

void test_deep_copy(){

    Buget *p = createBuget("telefon", 12, 10);
    Buget *buget = copyBuget(p);
    assert(strcmp(buget->type, "telefon") == 0);
    assert(buget->zi == 12);
    assert(buget->suma == 10);
    destroyBuget(p);
    destroyBuget(buget);
}

