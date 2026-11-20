#include "lista.h"
#ifndef OOP_LAB2_CUSTOMSORT_H
#define OOP_LAB2_CUSTOMSORT_H
#include "oferta.h"

typedef int (*CompareFct)(oferta * m1,oferta * m2);
void sort(my_list *l, CompareFct cmpF);




#endif //OOP_LAB2_CUSTOMSORT_H
