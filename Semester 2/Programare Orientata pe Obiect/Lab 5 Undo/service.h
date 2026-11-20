#ifndef BUGET_BUN_SERVICE_H
#define BUGET_BUN_SERVICE_H

#include "MyList.h"
typedef struct {

    MyList* allbugets;
    MyList* undolist;

}ManagerBuget;

int valideaza(Buget *p, MyList* l);

void testValidare();

void modificare(ManagerBuget* buget, char* tip, int zi, float suma,  char* type, int day, float sum);

void test_modificare();


ManagerBuget createManagerBuget();

void destroyManagerBuget(ManagerBuget *buget);

void delete_cmd(ManagerBuget *buget, ElemType el);

int undo(ManagerBuget* buget);

void test_delete_cmd();

void test_delete_undo();

void test_undo();


#endif //BUGET_BUN_SERVICE_H


