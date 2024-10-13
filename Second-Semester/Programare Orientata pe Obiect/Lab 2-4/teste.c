#include "oferta.h"
#include "service.h"
#include "lista.h"
#include "assert.h"
#include "string.h"
#include "stdlib.h"
#include "stdio.h"
#include <stdbool.h>



void test_creare_oferta(){

    char tip[12] = "teren";
    char adresa[30] = "Cluj";
    float pret = 2000;
    float suprafata = 3000;

    oferta oferta;
    oferta = creare_oferta(tip, adresa, pret, suprafata);

    assert(strcmp(get_tip(&oferta), tip) == 0);
    assert(strcmp(get_adresa(&oferta), adresa) == 0);
    assert(get_pret(&oferta) == pret);
    assert(get_suprafata(&oferta) == suprafata);

    free(oferta.tip);
    free(oferta.adresa);

}


void test_creare_lista(){

    my_list lista;
    lista = creare_lista(&lista);

    assert(lista.lungime == 0);

    free(lista.lista_oferte);

}


void test_validare_date(){

    char tip[12] = "baban";
    char adresa[30] = "Cluj";
    float pret = -123;
    float suprafata = -123;

    char erori[100] = {"\n"};
    int ok = 1;

    validare_date(erori, &ok, tip, adresa, pret, suprafata);
    assert(strstr(erori, "Tip invalid"));
    assert(strstr(erori, "Pretul trebuie sa fie mai mare decat 0"));
    assert(strstr(erori, "Suprafata trebuie sa fie mai mare decat 0"));
    assert(ok == 0);


}


void test_validare_index(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    char erori[100] = {"\n"};
    int ok = 1;
    oferta oferta_noua;

    oferta_noua = creare_oferta("teren", "Cluj", 100, 100);

    adaugare_oferta(oferta_noua, &lista);

    adaugare_oferta(oferta_noua, &lista);
    adaugare_oferta(oferta_noua, &lista);
    adaugare_oferta(oferta_noua, &lista);

    validare_index(lista, 0, erori, &ok);
    assert(ok == 0);
    assert(strstr(erori, "Index out of range"));

    ok = 1;
    validare_index(lista, 10, erori, &ok);
    assert(ok == 0);
    assert(strstr(erori, "Index out of range"));

    free(oferta_noua.tip);
    free(oferta_noua.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_adaugare_oferta(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);


    char tip[12] = "teren";
    char adresa[30] = "Cluj";
    float pret = 2000;
    float suprafata = 3000;
    oferta oferta_noua;

    oferta_noua = creare_oferta(tip, adresa, pret, suprafata);

    adaugare_oferta(oferta_noua, &lista);

    assert(lista->lungime == 1);
    assert(strcmp(lista->lista_oferte[0].tip, "teren") == 0);
    assert(strcmp(lista->lista_oferte[0].adresa, "Cluj") == 0);
    assert(lista->lista_oferte[0].pret == (float)2000);
    assert(lista->lista_oferte[0].suprafata == (float)3000);

    free(lista->lista_oferte[0].tip);
    free(lista->lista_oferte[0].adresa);
    free(lista->lista_oferte);
    free(lista);

}

void test_actualizeaza(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua;

    oferta_noua = creare_oferta("teren", "Cluj", 1000, 10000);

    adaugare_oferta(oferta_noua, &lista);

    actualizeaza(lista, 1, "casa", "Suceava", 300, 500);

    assert(lista->lungime == 1);
    assert(strcmp(lista->lista_oferte[0].tip, "casa") == 0);
    assert(strcmp(lista->lista_oferte[0].adresa, "Suceava") == 0);
    assert(lista->lista_oferte[0].pret == (float)300);
    assert(lista->lista_oferte[0].suprafata == (float)500);

    free(lista->lista_oferte[0].tip);
    free(lista->lista_oferte[0].adresa);
    free(lista->lista_oferte);
    free(lista);

}

void test_sterge(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua;

    oferta_noua = creare_oferta("teren", "Cluj", 1000, 10000);

    adaugare_oferta(oferta_noua, &lista);
    adaugare_oferta(oferta_noua, &lista);

    sterge(lista, 1);
    assert(lista->lungime == 1);

    free(oferta_noua.tip);
    free(oferta_noua.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_sortare_tip(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua_1, oferta_noua_2;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 1000, 10000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 1000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    sortare_tip(lista, '1');
    assert(strcmp(lista->lista_oferte[0].tip, "apartament") == 0);
    sortare_tip(lista, '2');
    assert(strcmp(lista->lista_oferte[0].tip, "teren") == 0);

    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_sortare_pret(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua_1, oferta_noua_2;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 1000, 10000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 2000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    sortare_pret(lista, '2');
    assert(lista->lista_oferte[0].pret == 2000);
    sortare_pret(lista, '1');
    assert(lista->lista_oferte[0].pret == 1000);

    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_key(){

    oferta oferta1, oferta2;

    oferta1 = creare_oferta("teren", "Cluj", 100, 100);
    oferta2 = creare_oferta("apartament", "Cluj", 200, 200);
    assert(bool_key('1', oferta1, oferta2, true) == false);
    assert(bool_key('1', oferta1, oferta2, false) == true);
    assert(bool_key('2', oferta1, oferta2, true) == true);
    assert(bool_key('2', oferta1, oferta2, false) == false);

    free(oferta1.tip);
    free(oferta1.adresa);
    free(oferta2.tip);
    free(oferta2.adresa);
}


void test_sortare(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua_1, oferta_noua_2, oferta_noua_3;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 2000, 10000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 1000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    sortare(lista, true, '1');
    assert(lista->lista_oferte[0].pret == 1000);
    sortare(lista, true, '2');
    assert(lista->lista_oferte[0].pret == 1000);
    sortare(lista, false, '1');
    assert(lista->lista_oferte[0].pret == 2000);
    sortare(lista, false, '2');
    assert(lista->lista_oferte[0].pret == 2000);

    *lista = sorttip(lista);
    assert(lista->lista_oferte[0].pret == 1000);
    *lista = sortpret(lista);
    assert(lista->lista_oferte[0].pret == 1000);
    oferta_noua_3 = creare_oferta("apartament", "Cluj", 1000, 10000);
    adaugare_oferta(oferta_noua_3, &lista);
    *lista = sortpret(lista);
    assert(lista->lista_oferte[0].pret == 1000);
    *lista = sorttip(lista);
    assert(lista->lista_oferte[0].pret == 1000);


    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(oferta_noua_3.tip);
    free(oferta_noua_3.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_filtrare_suprafata(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua_1, oferta_noua_2;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 1000, 1000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 1000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    filtrare_suprafata(lista, 5000);
    assert(lista->lungime == 1);
    assert(lista->lista_oferte[0].suprafata == (float)10000);

    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_filtrare_tip(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua_1, oferta_noua_2;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 1000, 10000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 1000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    filtrare_tip(lista, "teren");
    assert(lista->lungime == 1);
    assert(strcmp(lista->lista_oferte[0].tip, "teren") == 0);

    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_filtrare_pret(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);
    oferta oferta_noua_1, oferta_noua_2;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 1000, 10000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 2000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    filtrare_pret(lista, 1500);
    assert(lista->lungime == 1);
    assert(lista->lista_oferte[0].pret == (float)1000);

    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void test_redimensionare(){

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);

    lista->dimensiune = 1;

    oferta oferta_noua_1, oferta_noua_2;

    oferta_noua_1 = creare_oferta("teren", "Cluj", 1000, 10000);
    oferta_noua_2 = creare_oferta("apartament", "Cluj", 1000, 10000);
    adaugare_oferta(oferta_noua_1, &lista);
    adaugare_oferta(oferta_noua_2, &lista);

    assert(lista->dimensiune == 2);

    free(oferta_noua_1.tip);
    free(oferta_noua_1.adresa);
    free(oferta_noua_2.tip);
    free(oferta_noua_2.adresa);
    free(lista->lista_oferte);
    free(lista);

}


void testat_all(){

    test_creare_oferta();
    test_creare_lista();
    test_validare_date();
    test_validare_index();
    test_adaugare_oferta();
    test_actualizeaza();
    test_sterge();
    test_sortare_tip();
    test_sortare_pret();
    test_filtrare_suprafata();
    test_filtrare_tip();
    test_filtrare_pret();
    test_redimensionare();
    test_key();
    test_sortare();

}





