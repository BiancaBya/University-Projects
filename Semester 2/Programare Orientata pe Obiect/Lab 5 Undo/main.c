//#include "buget.h"
//#include "MyList.h"
#include "service.h"
#include <stdio.h>
#include <assert.h>
#include <string.h>
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>

void getAllBuget(MyList* l)
{
    int i;
    for(i=0;i<size(l);i++)
    {
        Buget *b = get(l, i);
        printf("Type: %s\n Zi: %d\n Suma: %.2f\n\n", b->type, b->zi, b->suma);
    }
}


void afisare_lista_liste(MyList* l){

    for (int i=0; i < l->lg ; i++){
        MyList *lista = l->elems[i];
        for (int j=0; j < lista->lg; j++){

            Buget *b = get(lista, j);
            printf("Type: %s\n Zi: %d\n Suma: %.2f\n\n", b->type, b->zi, b->suma);

        }
    }

}


int addBuget(ManagerBuget* buget, char* type, int zi, float suma)
{
    Buget *p = createBuget(type, zi, suma);
    int succ = valideaza(p, buget->allbugets);
    if (succ != 0)
    {
        if(succ == 4) printf("Exista deja aceasta cheltuiala in ziua respectiva!\n");
        if(succ == 1) printf("Nu exista ziua respectiva!\n");
        if(succ == 2) printf("Nu exista tipul respectiv!\n");
        if(succ == 3) printf("Suma trebuie sa fie pozitiva!\n");
        return succ;
    }

    MyList  *toUndo = copyList(buget->allbugets, (CopyFct) copyBuget);
    add(buget->undolist, toUndo);
    add(buget->allbugets, p);

    return 0;
}



void test_getAllBuget()
{
    MyList *l = createEmpty((DestroyFct) destroyBuget);
    Buget *p = createBuget("altele", 2, 2);
    add(l, p);
    assert(size(l) == 1);
    Buget *b = get(l, 0);
    assert(strcmp(b->type,"altele") == 0);
    assert(b->zi == 2);
    assert(b->suma == 2);
    destroyList(l);
}

void undo_ui(ManagerBuget* buget){

    int ok = undo(buget);
    if (ok)
        printf("Undo realizat cu succes.\n");
    else
        printf("Nu se poate face undo.\n");

}

void test_all()
{
    testBuget();
    testValidare();
    test_destroy();
    test_resize();
    test_getAllBuget();
    test_modificare();
    testCreateEmpty();
    test_set();
    test_delete();
    test_sort_list();
    test_filtrare_type();
    test_filtrare_zi();
    test_filtrare_suma();
    test_filtrare_suma_mai_mare();
    testCopyList();
    test_deep_copy();
    test_delete_cmd();
    test_find();
    test_delete_undo();
    test_undo();
}

void start()
{
    //MyList *Bugete = createEmpty((DestroyFct) destroyBuget);
    ManagerBuget buget = createManagerBuget();
    int ok = 1;
    while(ok)
    {
        printf("1. Add\n2. Modify\n3. Delete\n4. Afisare dupa o proprietate(tip,zi,suma)\n5. Afisare ordonate dupa o proprietate(suma,tip,zi)\n6.Undo\n0. Iesire\n");
        int cmd = 0;
        scanf_s("%ld", &cmd);
        if(cmd == 1)
        {
            printf("Tip:");
            char type[30];
            scanf_s("%s", type, 30);
            printf("Zi:");
            int zi;
            scanf_s("%ld", &zi);
            printf("Suma:");
            float suma;
            scanf_s("%f", &suma);
            addBuget(&buget, type, zi, suma);
        }
        else if(cmd == 2)
        {
            printf("Tip:");
            char tip[30];
            scanf_s("%s", tip, 30);
            printf("Zi:");
            int zi;
            scanf_s("%ld", &zi);
            printf("Suma:");
            float suma;
            scanf_s("%f", &suma);
            printf("Tipul pe care vrei sa-l pui:");
            char type[30];
            scanf_s("%s", type, 30);
            printf("Ziua pe care vrei sa o pui:");
            int day;
            scanf_s("%ld", &day);
            printf("Suma pe care vrei sa o pui:");
            float sum;
            scanf_s("%f", &sum);

            MyList  *toUndo = copyList(buget.allbugets, (CopyFct) copyBuget);
            add(buget.undolist, toUndo);

            modificare(&buget,tip,zi,suma,type,day,sum);
        }
        else if(cmd == 3)
        {
            printf("Tip:");
            char tip[30];
            scanf_s("%s", tip, 30);
            printf("Zi:");
            int zi;
            scanf_s("%ld", &zi);
            printf("Suma:");
            float suma;
            scanf_s("%f", &suma);
            int poz = FindBuget(buget.allbugets, tip, zi, suma);
            Buget *p = get(buget.allbugets, poz);

            MyList  *toUndo = copyList(buget.allbugets, (CopyFct) copyBuget);
            add(buget.undolist, toUndo);

            delete_cmd(&buget, p);
        }
        else if(cmd == 4)
        {
            char cuvant[30], criteriu[30];
            int numar;
            float sum;
            printf("Criteriu de filtrare:");
            scanf_s("%s",cuvant,30);
            MyList *rez = copyList(buget.allbugets, (CopyFct) copyBuget);
            if(strcmp(cuvant,"type") == 0)
            {
                printf("Alege tipul:");
                scanf_s("%s", criteriu, 30);
                filtrare_type(rez, criteriu);
            }
            if(strcmp(cuvant,"zi") == 0)
            {
                printf("Alege ziua:");
                scanf_s("%ld", &numar);
                filtrare_zi(rez, numar);
            }
            if(strcmp(cuvant,"suma") == 0)
            {
                printf("Alege suma:");
                scanf_s("%f", &sum);
                filtrare_suma(rez, sum);
            }
            if(strcmp(cuvant,"suma_mare") == 0){
                printf("Alege suma:");
                scanf_s("%f", &sum);
                filtrare_suma_mai_mare(rez, sum);
            }
            getAllBuget(rez);
            printf("\n");
        }
        else if(cmd == 5)
        {
            char cuvant[30];
            int sortare;
            printf("Criteriu de sortare:");
            scanf_s("%s", cuvant, 30);
            MyList *rez = copyList(buget.allbugets, (CopyFct) copyBuget);
            if (strcmp(cuvant, "type") == 0) {
                printf("Alegere sortare: ");
                scanf_s("%ld", &sortare);
                sort_list(rez, cmptype, sortare);
            }
            if (strcmp(cuvant, "zi") == 0)
            {
                printf("Alegere sortare: ");
                scanf_s("%ld", &sortare);
                sort_list(rez, cmpzi, sortare);
            }
            if (strcmp(cuvant, "suma") == 0) {
                printf("Alegere sortare: ");
                scanf_s("%ld", &sortare);
                sort_list(rez, cmpsuma, sortare);
            }
            getAllBuget(rez);
            printf("\n");
        }
        else if(cmd == 6){
            undo_ui(&buget);
        }
        else if(cmd == 0) break;
        else printf("Comanda invalida!");
    }
    destroyManagerBuget(&buget);
}

int main(){
    test_all();
    _CrtDumpMemoryLeaks();
    start();
    _CrtDumpMemoryLeaks();
    return 0;
}


/*
3 Buget de familie

Creati o aplicatie care permite gestiunea bugetului pentru o familie. Aplicatia trebuie sa
stocheze cheltuielile pe o luna. Fiecare cheltuiala are: zi (ziua in care s-a efectuat), suma,
tip (mancare, transport, telefon&internet,inbracaminte,altele)
Aplicatia permite:
a) Adaugarea de cheltuieli noi (cand familia cumpara ceva sau plateste o factura)
b) Modificarea unei cheltuieli existente (ziua, tipul, suma)
c) Stergere cheltuiala
d) Vizualizare lista de cheltuieli filtrat dupa o proprietate (suma, ziua, tipul)
e) Vizualizare lista de cheltuieli ordonat dupa suma sau tip (crescator/descrescator)
 */


