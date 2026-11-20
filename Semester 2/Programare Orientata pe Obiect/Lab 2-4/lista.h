#ifndef OOP_LAB2_LISTA_H
#define OOP_LAB2_LISTA_H
#include "oferta.h"

my_list creare_lista(my_list *lista);

my_list* redimensionare(my_list *lista);

oferta* get_lista_index(my_list* lista, int index);

void free_lista(my_list *lista);

void resize(my_list *lista);


#endif //OOP_LAB2_LISTA_H


