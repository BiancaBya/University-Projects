#ifndef OOP_LAB2_SERVICE_H
#define OOP_LAB2_SERVICE_H
#include "oferta.h"
#include <stdbool.h>

void adaugare_oferta(oferta oferta_noua, my_list **lista);

void validare_date(char erori[100], int *ok, char *tip, char *adresa, float pret, float suprafata);

void validare_index(my_list *lista, int index, char erori[100], int *ok);

void actualizeaza(my_list *lista, int index, char *tip, char *adresa, float pret, float suprafata);

void sterge(my_list *lista, int index);

void swap(oferta *oferta1, oferta *oferta2);

void sortare_tip(my_list *lista, char crescator);

void sortare_pret(my_list *lista, char crescator);

void sortare_pret_tip(my_list *lista);

void filtrare_suprafata(my_list *lista, float suprafata);

void filtrare_tip(my_list *lista, char *tip);

void filtrare_pret(my_list *lista, float pret);

void filtrare(my_list *lista);

bool bool_key(char key, oferta oferta1, oferta oferta2, bool crescator);

void sortare(my_list *lista, bool crescator, char key);

my_list sorttip(my_list *lista);

my_list sortpret(my_list *lista);

int cmppret(oferta * m1, oferta *m2);

int cmptip(oferta * m1, oferta * m2);


#endif //OOP_LAB2_SERVICE_H




