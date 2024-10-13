#include "customsort.h"

void sort(my_list *l, CompareFct cmpF){
    int i, j;
    for (i = 0; i < l->lungime-1; i++) {
        for (j = i + 1; j < l->lungime; j++) {
            if (cmpF(&l->lista_oferte[i], &l->lista_oferte[j])){
                oferta aux;
                aux = l->lista_oferte[i];
                l->lista_oferte[i] = l->lista_oferte[j];
                l->lista_oferte[j] = aux;
            }
        }
    }
}

