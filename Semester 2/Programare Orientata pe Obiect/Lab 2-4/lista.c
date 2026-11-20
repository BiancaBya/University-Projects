//#include "lista.h"
#include "oferta.h"
#include "stdlib.h"
#include "stdio.h"


my_list creare_lista(my_list *lista){

    /*
        Functia primeste o lista ca parametru si initializeaza valorile sale, alocand spatiu pentru lista de oferte
    */

    lista->lungime = 0;
    lista->dimensiune = 100;
    lista->lista_oferte = (oferta*)malloc(sizeof(oferta)*lista->dimensiune);
    return *lista;

}


/*
my_list* redimensionare(my_list *lista){

    my_list *lista_noua = (my_list*)malloc(sizeof(my_list));
    lista_noua->dimensiune = lista->dimensiune * 2;
    lista_noua->lungime = lista->lungime;
    lista_noua->lista_oferte = (oferta*) malloc(sizeof(oferta)*lista_noua->dimensiune);
    for (int i=0; i<lista_noua->lungime; i++)
        lista_noua->lista_oferte[i] = lista->lista_oferte[i];

    return lista_noua;

}
*/


oferta* get_lista_index(my_list* lista, int index){

    /*
        Functia returneaza oferta de la indexul primit ca parametru din lista de oferte
    */

    return &lista->lista_oferte[index];

}


void free_lista(my_list *lista){

    /*
        Functia care elibereaza memoria pentru o variabila de tip lista
    */

    free(lista->lista_oferte);

}

void resize(my_list *lista){

    /*
        Functia redimensioneaza lista primita ca parametru, alocand memorie dublra fata de capacitatea sa initiala
    */

    lista->dimensiune = 2 * lista->dimensiune;
    int new_capacity = lista->dimensiune;
    lista->lista_oferte = (oferta *)realloc(lista->lista_oferte, sizeof(oferta ) * new_capacity);

}




