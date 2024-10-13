#ifndef BUGET_BUN_BUGET_H
#define BUGET_BUN_BUGET_H

#pragma once

typedef struct {
    char type[30];
    int zi;
    float suma;
} Buget;

Buget *createBuget(char* type, int zi, float suma);

void testBuget();

void destroyBuget(Buget* p);

Buget *copyBuget(Buget* buget);

void test_deep_copy();


#endif //BUGET_BUN_BUGET_H
