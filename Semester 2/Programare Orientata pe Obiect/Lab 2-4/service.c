//#include "service.h"
#include <string.h>
#include "oferta.h"
#include <stdlib.h>
#include "lista.h"
#include <stdbool.h>
#include "customsort.h"


void adaugare_oferta(oferta oferta_noua, my_list **lista){

    /*
        Functia primeste lista de oferte si o oferta pe care o adauga in lista
        daca nmarul de oferte existent in lista este egal cu dimensiunea listei, atunci lista este redimensionata
        si apoi elementul este adaugat
    */

    if((*lista)->lungime == (*lista)->dimensiune)
        resize(*lista);//*lista = redimensionare(*lista);

    (*lista)->lista_oferte[(*lista)->lungime] = oferta_noua;
    (*lista)->lungime ++;

}



void validare_date(char erori[100], int *ok, char tip[12], char adresa[30], float pret, float suprafata){

    /*
        Functia verifica daca datele introduse de utilizator sunt corecte
        daca nu, modifica parametrul ok si pune in sirul de erori cate un mesaj potrivit pentru fiecare eroare
    */

    if (strcmp(tip, "teren") != 0 && strcmp(tip, "casa") != 0 && strcmp(tip, "apartament") != 0)
        strcat(erori, "Tip invalid!\n"), *ok = 0;

    if (pret < 0)
        strcat(erori, "Pretul trebuie sa fie mai mare decat 0!\n"), *ok = 0;

    if (suprafata < 0)
        strcat(erori, "Suprafata trebuie sa fie mai mare decat 0!\n"), *ok = 0;

}




void validare_index(my_list *lista, int index, char erori[100], int *ok){

    /*
        Functia verifica daca indexul dat de utilizator este valid pentru lista primita ca parametru
        daca nu este modifica variabila ok si o face 0 si pune in sirul de erori un mesaj potrivit
    */

    if (index > lista->lungime || index < 1)
        strcat(erori, "Index out of range!\n"), *ok = 0;

}


void actualizeaza(my_list *lista, int index, char *tip, char *adresa, float pret, float suprafata){

    /*
        Functia primeste ca parametru o lista de oferte si actualizeaza oferta de la pozitia indicata
        de index cu noile valori primite ca parametru
    */

    index--;
    oferta oferta;
    oferta = lista->lista_oferte[index];
    set_tip(&oferta, tip);
    set_adresa(&oferta, adresa);
    set_pret(&oferta, pret);
    set_suprafata(&oferta, suprafata);
    lista->lista_oferte[index] = oferta;

}




void sterge(my_list *lista, int index){

    /*
        Functia sterge elementul de la pozitia indicata de index din lista
        prin cresterea indexului si mutarea elementelor de dupa el cu o pozitie la stanga
    */

    index--;
    for (int i = index; i < lista->lungime-1; i++)
        lista->lista_oferte[i] = lista->lista_oferte[i+1];
    lista->lungime--;

}




void swap(oferta *oferta1, oferta *oferta2){

    /*
        Functia interschimba doi parametri de tip oferta intre ei
        este folosita pentru functia de sortare
     */

    oferta aux;
    aux = *oferta1;
    *oferta1 = *oferta2;
    *oferta2 = aux;

}



void sortare_tip(my_list *lista, char crescator){

    /*
        Functia sorteaza lista primita ca parametru in functie de pret parametrul crescator sau descrescator
        -modifica lista primita ca parametru astfel incat sa fie sortata corespunzator in functie de tip
    */

    int i, j;

    for (i=0; i < lista->lungime-1; i++)
        for (j=i+1; j < lista->lungime; j++){

            if (crescator == '1'){
                if (strcmp(get_tip(get_lista_index(lista,i)), get_tip(get_lista_index(lista,j))) > 0)
                    swap(&lista->lista_oferte[i], &lista->lista_oferte[j]);
            }
            else {
                if (strcmp(get_tip(get_lista_index(lista,i)), get_tip(get_lista_index(lista,j))) < 0)
                    swap(&lista->lista_oferte[i], &lista->lista_oferte[j]);
            }
        }
}


void sortare_pret(my_list *lista, char crescator){

    /*
        Functia sorteaza lista primita ca parametru in functie de pret parametrul crescator sau descrescator
        -modifica lista primita ca parametru astfel incat sa fie sortata corespunzator in functie de pret
    */

    int i, j;

    for (i=0; i < lista->lungime-1; i++)
        for (j=i+1; j < lista->lungime; j++){

            if (crescator == '1'){
                if (get_pret(get_lista_index(lista,i)) > get_pret(get_lista_index(lista,j)))
                    swap(&lista->lista_oferte[i], &lista->lista_oferte[j]);
            }
            else {
                if (get_pret(get_lista_index(lista,i)) < get_pret(get_lista_index(lista,j)))
                    swap(&lista->lista_oferte[i], &lista->lista_oferte[j]);
            }
        }
}


bool bool_key(char key, oferta oferta1, oferta oferta2, bool crescator){

    bool ok;

    if (key == '1'){

        if (get_pret(&oferta1) > get_pret(&oferta2))
            ok = true;
        else
            ok = false;
    }
    else {

        if(strcmp(get_tip(&oferta1), get_tip(&oferta2)) > 0)
            ok = true;
        else
            ok = false;
    }

    if (crescator)
        return ok;
    return !ok;

}


void sortare(my_list *lista, bool crescator, char key){

    /*
        Functia sorteaza lista primita ca parametru in functie de key parametrul crescator sau descrescator
        -modifica lista primita ca parametru astfel incat sa fie sortata corespunzator in functie de pret
    */

    int i, j;

    for (i=0; i < lista->lungime-1; i++)
        for (j=i+1; j < lista->lungime; j++){
            if (bool_key(key, lista->lista_oferte[i], lista->lista_oferte[j], crescator))
                swap(&lista->lista_oferte[i], &lista->lista_oferte[j]);
        }
}





void filtrare_suprafata(my_list *lista, float suprafata){

    /*
        Functia creeaza o copie a listei primite ca parametru pe care o filtreaza in functie de suprafata
        -modifica lista primita ca parametru astfel incat sa ramana in ea doar elementele care indeplinesc consitia
    */

    my_list copie;
    copie = creare_lista(&copie);

    for (int i=0; i < lista->lungime; i++)
        if (get_suprafata(get_lista_index(lista,i)) >= suprafata){
            copie.lista_oferte[copie.lungime] = lista->lista_oferte[i];
            copie.lungime ++;
        }

    lista->lungime = copie.lungime;
    for (int i=0; i < lista->lungime; i++)
        lista->lista_oferte[i] = copie.lista_oferte[i];

    free_lista(&copie);

}


void filtrare_tip(my_list *lista, char *tip){

    /*
        Functia creeaza o copie a listei primite ca parametru pe care o filtreaza in functie de tip
        -modifica lista primita ca parametru astfel incat sa ramana in ea doar elementele care indeplinesc consitia
    */

    my_list copie;
    copie = creare_lista(&copie);

    for (int i=0; i < lista->lungime; i++)
        if (strcmp(get_tip(get_lista_index(lista,i)), tip) == 0){
            copie.lista_oferte[copie.lungime] = lista->lista_oferte[i];
            copie.lungime ++;
        }

    lista->lungime = copie.lungime;
    for (int i=0; i < lista->lungime; i++)
        lista->lista_oferte[i] = copie.lista_oferte[i];

    free_lista(&copie);

}


void filtrare_pret(my_list *lista, float pret){

    /*
        Functia creeaza o copie a listei primite ca parametru pe care o filtreaza in functie de pret
        -modifica lista primita ca parametru astfel incat sa ramana in ea doar elementele care indeplinesc consitia
    */

    my_list copie;
    copie = creare_lista(&copie);

    for (int i=0; i < lista->lungime; i++)
        if (get_pret(get_lista_index(lista,i)) <= pret){
            copie.lista_oferte[copie.lungime] = lista->lista_oferte[i];
            copie.lungime ++;
        }

    lista->lungime = copie.lungime;
    for (int i=0; i < lista->lungime; i++)
        lista->lista_oferte[i] = copie.lista_oferte[i];

    free_lista(&copie);

}

int cmptip(oferta * m1, oferta * m2){
    if (strcmp(m1->tip, m2->tip) > 0)
        return 1;
    else
        return 0;
}

int cmppret(oferta * m1, oferta *m2){
    if(m1->pret == m2->pret)
        return 0;
    if(m1->pret > m2->pret)
        return 1;
    else
        return 0;
}

my_list sortpret(my_list *lista){
    sort(lista, cmppret);
    return *lista;
}

my_list sorttip(my_list *lista){
    sort(lista, cmptip);
    return *lista;
}





