//#include "oferta.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>



typedef struct {
    char *tip;
    char *adresa;
    float pret, suprafata;
} oferta;



typedef struct {
    oferta *lista_oferte;
    int lungime, dimensiune;
} my_list;



char* get_tip(oferta* oferta){

    return oferta->tip;

}

char* get_adresa(oferta* oferta){

    return oferta->adresa;

}

float get_pret(oferta* oferta){

    return oferta->pret;

}

float get_suprafata(oferta* oferta){

    return oferta->suprafata;

}

void set_tip(oferta* oferta, const char *tip){

    /*
      Functia primeste o oferta si un sir pentru tip, elibereaza memoria alocata pentru fostul tip, aloca memorie pentru noul tip si il seteaza
     */

    free(oferta->tip);
    oferta->tip = (char*) malloc(sizeof(char) * (strlen(tip) + 1));
    strcpy(oferta->tip, tip);

}

void set_adresa(oferta* oferta, const char *adresa){

    /*
      Functia primeste o oferta si un sir pentru adresa, elibereaza memoria alocata pentru fosta adresa, aloca memorie pentru noua adresa si o seteaza
     */

    free(oferta->adresa);
    oferta->adresa = (char*) malloc(sizeof(char) * (strlen(adresa) + 1));
    strcpy(oferta->adresa, adresa);

}

void set_pret(oferta* oferta, float pret){

    oferta->pret = pret;

}

void set_suprafata(oferta* oferta, float suprafata){

    oferta->suprafata = suprafata;

}



oferta creare_oferta(char *tip, char *adresa, float pret, float suprafata){

    /*
        Functia primeste valorile unei oferte si creeaza o oferta noua pe care o returneaza
    */

    oferta oferta_noua;

    oferta_noua.tip = (char*) malloc(sizeof(char) * (strlen(tip) + 1));
    oferta_noua.adresa = (char*) malloc(sizeof(char) * (strlen(adresa) + 1));


    set_tip(&oferta_noua, tip);
    set_adresa(&oferta_noua, adresa);
    set_pret(&oferta_noua, pret);
    set_suprafata(&oferta_noua, suprafata);


    return oferta_noua;

}






