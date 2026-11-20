#include "Multime.h"
#include "IteratorMultime.h"

#include <iostream>

Multime::Multime() {

    primul = nullptr;

}


bool Multime::adauga(TElem elem) {

    // Complexitate = O(n)

    if (not cauta(elem)){

        Node* nod_nou = new Node();  // facem un nod care are valoarea elementului de adaugat
        nod_nou->element = elem;
        nod_nou->inainte = nullptr;
        nod_nou->dupa = nullptr;

        if (primul == nullptr)
            primul = nod_nou;

        else{

            Node* curent = primul;
            while (curent->dupa != nullptr)
                curent = curent->dupa;

            curent->dupa = nod_nou;
            nod_nou->inainte = curent;

        }

        return true;
    }

    return false;
}


bool Multime::sterge(TElem elem) {

    // Theta(1)  =  best case
    // Theta(n)  =  worst case
    // Complexitate = O(n)

    if (not cauta(elem))
	    return false;

    else {

        Node* curent = primul;
        if (primul->element == elem){
            primul = primul->dupa;
            return true;
        }

        else{
            while (curent->dupa->element != elem)
                curent = curent->dupa;
            curent->dupa = curent->dupa->dupa;
            return true;
        }

    }

}


bool Multime::cauta(TElem elem) const {

    // Theta(1)  =  best case
    // Theta(n)  =  worst case
    // Complexitate = O(n)

    Node* curent = primul;

    while (curent != nullptr){
        if (curent->element == elem)
            return true;

        curent = curent->dupa;
    }
	return false;
}


int Multime::dim() const {

    // Complexitate = theta(n)

    int dim = 0;
    Node* curent = primul;

    while (curent != nullptr){
        curent = curent->dupa;
        dim ++;
    }

	return dim;
}

bool Multime::vida() const {

    // Complexitate = theta(1)
	if(primul == nullptr)
	    return true;

    return false;
}


Multime::~Multime() {

    // Complexitate = theta(1)
	delete primul;
}

void Multime::intersectie(const Multime &b) {

    // n = nr elemente din a, m = nr de elemente din b
    // Caz favorabil = Theta(n)
    // Caz defavorabil = Theta(n*m)
    // Complexitate = O(n*m)

    Node* curent = primul;
    while (curent != nullptr){
        if (!b.cauta(curent->element))
            sterge(curent->element);
        curent = curent->dupa;
    }

}

/*

Subalgoritm intersectie(Multime b)
    aloca curent
    curent <- primul
    cat timp curent != NIL executa
        daca !b.cauta([curent].element)
            sterge([curent].element)
        curent <- [curent].dupa
    sfwhile
SfSubalgoritm


*/


IteratorMultime Multime::iterator() const {

    // Complexitate = Theta(1)
	return IteratorMultime(*this);
}




