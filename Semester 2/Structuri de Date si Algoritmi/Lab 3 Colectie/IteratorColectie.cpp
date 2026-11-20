#include "IteratorColectie.h"
#include "Colectie.h"
#include <exception>
#include <iostream>

using namespace std;


IteratorColectie::IteratorColectie(const Colectie& c): colectie(c) {

    // Theta(1)
	curent = c.prim;
}

void IteratorColectie::prim() {

    // Theta(1)
    curent = colectie.prim;
}


void IteratorColectie::urmator() {

    // Theta(1)

    if (!valid()){
        throw runtime_error("Iterator invalid!");
    }

    if(iterator == colectie.frecvente[curent]-1) {
        curent = colectie.urmator[curent];
        iterator = 0;
    }
    else{
        iterator++;
    }

}


bool IteratorColectie::valid() const {

    // Theta(1)

	return curent != -1;
}



TElem IteratorColectie::element() const {

    // Theta(1)

    if (!valid()){
        throw runtime_error("Iterator invalid!");
    }

	return colectie.elemente[curent];
}

void IteratorColectie::anterior(){

    // BC = Theta(1)
    // WC = Theta(n)
    // Complexitate = O(n)

    if (!valid()){
        throw runtime_error("Iterator invalid!");
    }

    if (curent == colectie.prim and iterator == 0)
        curent = -1;

    else{
        int aux = colectie.prim;
        while (aux != -1 and colectie.urmator[aux] != curent)
            aux = colectie.urmator[aux];


        if (iterator == 0){
            curent = aux;
            iterator = colectie.frecvente[curent]-1;
        }
        else {
            iterator--;
        }
    }

}

/*
Subalgoritm anterior(curent, colectie)
    @arunca exceptie daca iteratorul este invalid
    daca curent = colectie.prim
        curent <- -1
    altfel
        aux <- colectie.prim
        cat timp aux != -1 si colectie.urmator[aux] != curent
            aux <- colectie.urmator[aux]
        if (iterator == 0)
            curent <- aux;
            iterator <- colectie.frecventa[curent]-1
        altfel
            iterator <- iterator-1

*/


