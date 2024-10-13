#ifndef OOP_LAB2_OFERTA_H
#define OOP_LAB2_OFERTA_H


typedef struct {
    char *tip;
    char *adresa;
    float pret, suprafata;
} oferta;



typedef struct {
    oferta *lista_oferte;
    int lungime, dimensiune;
} my_list;


char* get_tip(oferta* oferta);

char* get_adresa(oferta* oferta);

float get_pret(oferta* oferta);

float get_suprafata(oferta* oferta);

void set_tip(oferta* oferta, char *tip);

void set_adresa(oferta* oferta, char *adresa);

void set_pret(oferta* oferta, float pret);

void set_suprafata(oferta* oferta, float suprafata);

void print_menu();

oferta creare_oferta(char *tip, char *adresa, float pret, float suprafata);



#endif //OOP_LAB2_OFERTA_H




