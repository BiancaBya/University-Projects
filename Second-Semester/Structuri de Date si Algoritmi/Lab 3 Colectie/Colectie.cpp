#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>

using namespace std;


Colectie::Colectie() {

    // Complexitate O(n)

    capacitate = 100000;
    elemente = new TElem [capacitate];
    urmator = new int [capacitate];
    frecvente = new int [capacitate];

    prim = -1; // marcam ca lista este vida la inceput

    initlistaspatiuliber();
}

int Colectie::aloca() {

    // Complexitate Theta(1)

    int i = prim_liber;
    prim_liber = urmator[prim_liber];
    return i;

}

void Colectie::dealoca(int i) {

    // Complexitate Thena(1)

    urmator[i] = prim_liber;
    prim_liber = i;

}

void Colectie::resize() {

    // Complexitate Theta(n)

    auto* elemente_nou = new TElem [2 * capacitate];
    auto* urmator_nou = new int [2 * capacitate];
    auto* frecvente_nou = new int [2 * capacitate];

    for (int i=0; i < capacitate; i++){

        elemente_nou[i] = elemente[i];
        urmator_nou[i] = urmator[i];
        frecvente_nou[i] = frecvente[i];

    }

    delete[] elemente;
    delete[] urmator;
    delete[] frecvente;

    elemente = elemente_nou;
    urmator = urmator_nou;
    frecvente = frecvente_nou;

    capacitate *= 2;

    // BC = Theta(1)
    // WC = Theta(n)
    // Complexitate = O(n)

}

void Colectie::initlistaspatiuliber() {

    // Complexitate Theta(n)

    for (int i = 0; i < capacitate-1; i++)
        urmator[i] = i+1;
    urmator[capacitate-1] = -1;
    prim_liber = 0;

}


int Colectie::creeazanod(TElem el) {

    // BC -> Theta(1)
    // WC -> Theta(n)
    // Complexitate O(n)

    if (prim_liber == 0)
        resize();
    int i = aloca();
    if (i != -1) // daca exista spatiu in lista spatiului liber
    {
        elemente[i] = el;
        frecvente[i] = 1;
        urmator[i] = -1; // marcam ca i este ultimul nod din lista
    }
    return i;

}


void Colectie::adauga(TElem elem) {

    // BC = Theta(1)
    // WC = Theta(n)
    // Complexitate = O(n)

   if (cauta(elem)){

        int contor = prim;
        while (contor != -1){
            if (elemente[contor] == elem)
                frecvente[contor]++;
            contor = urmator[contor];
        }
   }

   else {
        int i = creeazanod(elem);
        if (i != -1) {
            urmator[i] = prim;
            prim = i; // se adauga nodul i la inceputul listei
        }
   }

}


bool Colectie::sterge(TElem elem) {

    // BC = Theta(1)
    // WC = Theta(n)
    // Complexitate = O(n)

    if (cauta(elem)){

        int contor = prim;
        int anterior = -1;

        while (contor != -1){
            if (elemente[contor] == elem){
                if (frecvente[contor] == 1){
                    if (anterior != -1) {
                        urmator[anterior] = urmator[contor];
                        dealoca(contor);
                    }
                    else{
                        prim = urmator[contor];
                        dealoca(contor);
                    }
                }
                else
                    frecvente[contor]--;
                return true;
            }
            anterior = contor;
            contor = urmator[contor];
        }
    }

	return false;
}


bool Colectie::cauta(TElem elem) const {

    // BC = Theta(1)
    // WC = Theta(n)
    // Complexitate = O(n)

    if(prim == -1)
        return false;

    int contor = prim;
    while (contor != -1) {

        if (elemente[contor] == elem)
            return true;
        contor = urmator[contor];
    }

    return false;

}

int Colectie::nrAparitii(TElem elem) const {

    // BC = Theta(1)
    // WC = Theta(n)
    // Complexitate = O(n)

    if(!cauta(elem))
        return 0;

    int contor = prim;
    while (contor != -1) {
        if (elemente[contor] == elem)
            return frecvente[contor];
        contor = urmator[contor];
    }

}


int Colectie::dim() const {

    // Complexitate = Theta(n)

	if (prim == -1)
	    return 0;

    int dim = 0;
    int contor = prim;

    while (contor != -1) {
        dim += frecvente[contor];
        contor = urmator[contor];
    }

    return dim;

}


bool Colectie::vida() const {

    // Complexitate = Theta(1)

	return prim == -1;
}

IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}


Colectie::~Colectie() {

    // Complexitate = O(n)

    delete[] elemente;
    delete[] urmator;
    delete[] frecvente;
}



