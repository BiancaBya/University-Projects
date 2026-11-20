#include "IteratorDictionar.h"
#include "Dictionar.h"

using namespace std;


IteratorDictionar::IteratorDictionar(const Dictionar& d) : dict(d){

    // Complexitate Theta(1)

    index = 0;
    prim();
}


void IteratorDictionar::prim() {

    // BC Theta(1)
    // WC O(n)

    int cap = 1 << dict.dispersie;
    for (index = 0; index<cap && Dictionar::is_free(dict.Tabela_dispersie[index]); index++);
}


void IteratorDictionar::urmator() {

    // BC Theta(1)
    // WC O(n)

    int cap = 1 << dict.dispersie;
    for (++index; index < cap && Dictionar::is_free(dict.Tabela_dispersie[index]); index++);
}


TElem IteratorDictionar::element() const{

    // Complexitate Theta(1)

    return dict.Tabela_dispersie[index];
}


bool IteratorDictionar::valid() const {

    // Complexitate Theta(1)

    return index < (1 << dict.dispersie) && !Dictionar::is_free(dict.Tabela_dispersie[index]);
}


