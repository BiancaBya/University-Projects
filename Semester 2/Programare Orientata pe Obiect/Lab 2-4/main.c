#include "oferta.h"
#include "service.h"
#include "lista.h"
#include "teste.h"
#include "stdio.h"
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#include <stdbool.h>
#include "customsort.h"



void print_menu(){

    printf("Alegeti optiunea:\n");
    printf("1. Adauga o oferta noua.\n");
    printf("2. Actualizare oferte.\n");
    printf("3. Sterge oferta.\n");
    printf("4. Oferte ordonate.\n");
    printf("5. Filtrare oferte.\n");
    printf("6. Break.\n");

}


void afisare_lista_oferte(my_list *lista){

    oferta oferta;
    int index = lista->lungime;
    printf("\nLista de oferte este:\n");

    for (int i = 0; i < index; i++){
        oferta = lista->lista_oferte[i];
        printf("\nTipul = %s\nAdresa = %s\nPretul = %f\nSuprafata = %f\n\n", get_tip(&oferta), get_adresa(&oferta), get_pret(&oferta), get_suprafata(&oferta));
    }

}


void add_oferta(my_list **lista){

    char *tip = (char*) malloc(sizeof(char) * 30);
    char *adresa = (char*) malloc(sizeof(char) * 30);
    float pret, suprafata;

    printf("Introduceti datele ofertei:\n");
    printf("tip =");
    scanf("%s", tip);
    printf("adresa =");
    scanf("%s", adresa);
    printf("pret =");
    scanf("%f", &pret);
    printf("suprafata =");
    scanf("%f", &suprafata);

    oferta oferta_noua;

    oferta_noua = creare_oferta(tip, adresa, pret, suprafata);

    char erori[100];
    int ok = 1;

    validare_date(erori, &ok, tip, adresa, pret, suprafata);

    if (ok == 0)
        printf("%s",erori);

    else {
        adaugare_oferta(oferta_noua, lista);
        afisare_lista_oferte(*lista);
    }

    free(tip);
    free(adresa);

}


void actualizare_oferta(my_list *lista){

    /*
      Functia primeste un ponter la o lista de tipul my_list si schimba o oferta din ea cu datele citite
     */

    int index;
    char *tip = (char*) malloc(sizeof(char) * 30);
    char *adresa = (char*) malloc(sizeof(char) * 30);
    float pret, suprafata;

    printf("Indexul ofertei de actualizat =");
    scanf("%d", &index);
    printf("Introduceti datele noi ale ofertei:\n");
    printf("tip nou =");
    scanf("%s", tip);
    printf("adresa noua =");
    scanf("%s", adresa);
    printf("pret nou =");
    scanf("%f", &pret);
    printf("suprafata noua =");
    scanf("%f", &suprafata);

    char erori[200];
    int ok = 1;

    validare_index(lista, index, erori, &ok);
    validare_date(erori, &ok, tip, adresa, pret, suprafata);

    if (ok == 0)
        printf("%s",erori);

    else {
        actualizeaza(lista, index, tip, adresa, pret, suprafata);
        afisare_lista_oferte(lista);
    }

    free(tip);
    free(adresa);

}



void stergere_oferta(my_list *lista){

    /*
      Functia primeste un pointer la o lista de tipul my_list si sterge din ea oferta de la pozitia citita
     */

    int index;
    printf("Indexul ofertei de sters =");
    scanf("%d", &index);

    char erori[100];
    int ok = 1;
    validare_index(lista, index, erori, &ok);

    if (ok == 1) {

        sterge(lista, index);
        if (lista->lungime != 0)
            afisare_lista_oferte(lista);
        else
            printf("Lista este vida.\n");
    }
    else
        printf("%s", erori);

}



void sortare_pret_tip(my_list *lista){

    /*
        Functia primeste un pointer la o lista de oferte si ii face o copie pe care o sorteaza crescator dupa pret su tip
        pre: lista trebuie sa fie de tipul my_list
        post: lista initiala ramane neschimbata, si este afisata copia sortata
    */

    int key;
    my_list copie;

    copie.lungime = lista->lungime;
    copie.dimensiune = lista->dimensiune;
    copie.lista_oferte = (oferta*)malloc(sizeof(oferta) * copie.dimensiune);

    for (int i=0; i < lista->lungime; i++)
        copie.lista_oferte[i] = lista->lista_oferte[i];


    printf("Sortare dupa:\n1.Pret\n2.Tip\n");
    scanf(" %d", &key);
    if (key == 1)
        copie = sortpret(&copie);
    else if (key == 2)
        copie = sorttip(&copie);
    else
        printf("Optiune invalida!");

    afisare_lista_oferte(&copie);


    free(copie.lista_oferte);

}



void filtrare(my_list *lista){

    /*
      Functia primeste lista si ii face o copie pe care o filtreaza corespuzator si dupa o afiseaza
     */

    char criteriu;
    my_list copie;

    copie.lungime = lista->lungime;
    copie.dimensiune = lista->dimensiune;
    copie.lista_oferte = (oferta*)malloc(sizeof(oferta) * copie.dimensiune);

    for (int i=0; i < lista->lungime; i++)
        copie.lista_oferte[i] = lista->lista_oferte[i];

    printf("Filtrare dupa:\n1.Suprafata\n2.Tip\n3.Pret\n");
    scanf(" %c", &criteriu);

    if (criteriu == '1') {

        float suprafata;
        printf("Afisare oferte cu suprafata mai mare decat:");
        scanf("%f", &suprafata);
        filtrare_suprafata(&copie, suprafata);

    }
    else if (criteriu == '2') {

        char *tip = (char*) malloc(sizeof(char) * 30);
        printf("Afisare oferte cu tipul:");
        scanf("%s", tip);
        filtrare_tip(&copie, tip);
        free(tip);

    }
    else if (criteriu == '3') {

        float pret;
        printf("Afisare oferte cu pretul mai mic decat:");
        scanf("%f", &pret);
        filtrare_pret(&copie, pret);

    }
    else
        printf("Optiune invalida!\n");

    if (criteriu == '1' || criteriu == '2' || criteriu == '3')
        afisare_lista_oferte(&copie);

    free(copie.lista_oferte);

}





void run(){

    int optiune;

    my_list* lista = (my_list*)malloc(sizeof(my_list));
    *lista = creare_lista(lista);

    while (1){
        print_menu();
        printf(">");
        scanf("%d", &optiune);
        if (optiune == 1)
            add_oferta(&lista);
        else if (optiune == 2)
            actualizare_oferta(lista);
        else if (optiune == 3)
            stergere_oferta(lista);
        else if (optiune == 4)
            sortare_pret_tip(lista);
        else if (optiune == 5)
            filtrare(lista);
        else if (optiune == 6) {
            for (int i=0; i< lista->lungime; i++)
                free(lista->lista_oferte[i].tip), free(lista->lista_oferte[i].adresa);
            free(lista->lista_oferte);
            free(lista);
            _CrtDumpMemoryLeaks();
            break;
        }
        else
            printf("Optiune invalida!\n");
    }

}






int main(){


    testat_all();
    _CrtDumpMemoryLeaks();

    run();
    _CrtDumpMemoryLeaks();

    return 0;
}









