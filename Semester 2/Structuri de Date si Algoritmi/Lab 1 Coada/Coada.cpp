#include "Coada.h"
#include <exception>
#include <iostream>

using namespace std;


Coada::Coada() {

    /*
        Complexitate = Theta(1)
    */

    capacity = 1;
    elements = new TElem [capacity];
    index_actual = 0;
    index_primul = 0;
    size = 0;

}


void Coada::adauga(TElem elem) {

    /*
        Caz favorabil, daca size < capacity -> Complexitatea = Theta(1)
        Caz defavorabil, daca size = capacity -> Complexitatea = Theta(n)
    */

    if (size == capacity)
        redim();

    else if (index_actual == capacity)
        index_actual = 0;


    elements[index_actual++] = elem;
    size ++;

}


void Coada::redim(){

    /*
        Complexitate = Theta(n)
    */

    // alocam spatiu de capacitate dubla
    TElem *element_nou = new TElem [2 * capacity];

    // mutam elementele in noua zona
    int j = 0;

    for (int i = index_primul; i < size ; i++)
        element_nou[j++] = elements[i];

    for (int i = 0; i < index_actual; i++)
        element_nou[j++] = elements[i];

    index_primul = 0;
    index_actual = size;


    // dublam capacitatea
    capacity = 2 * capacity;

    // eliberam zona veche
    delete[] elements;

    // vectorul de elemente care indica spre noua zona de memorie de capacitate dubla
    elements = element_nou;

}


//arunca exceptie daca coada e vida
TElem Coada::element() const {

    /*
        Complexitate = Theta(1)
    */

    if (vida()){
        throw runtime_error("Coada este vida");
    }

    return elements[index_primul];

}

TElem Coada::sterge() {

    /*
        Complexitate = Theta(1)
    */

    if (vida()){
        throw runtime_error("Coada este vida");
    }

    if (index_primul == capacity) {
        index_primul = 0;
        size --;
        return elements[capacity - 1];
    }

    else {
        index_primul++;
        size --;
        return elements[index_primul - 1];
    }

}


TElem Coada::stergeMinim(){

    /*
     Complexitate:
        Caz defavorabil cand parcurge tot vectorul = Theta(n)
     */

    if (vida()){
        throw runtime_error("Coada este vida");
    }

    if(size == 1){
        return sterge();
    }

    TElem minim = elements[0];
    int index = 0;

    for (int i=1; i < size; i++)
        if (elements[i] < minim) {
            minim = elements[i];
            index = i;
        }

    for (int i=index; i < size-1; i++)
        elements[i] = elements[i+1];


    if (index == index_primul) {
        if (index_primul == capacity)
            index_primul = 0;
    }

    size--;
    return minim;

}

/*
Pseudocod
 SubAlgoritm stergeMinim

    daca vida()
        arunca exceptie "Coada este vida"
    sfdaca

    daca size = 1
        return sterge()
    sfdaca

    minim <- elements[0]
    index <- 0

    pentru i=1, size executa
        daca elements[i] < minim executa
            minim <- elements[i]
            index <- i
        sfdaca
    sfpentru

    pentru i <- index, size-2 executa
        elements[i] <- elements[i+1]
    sfpentru


    daca index = index_primul executa
        daca index_primul == capacity
            index_primul <- 0
        sfdaca
    sfdaca

    size <- size-1
    return minim

SFSubAlgoritm

*/


bool Coada::vida() const {

    /*
        Complexitate = Theta(1)
    */

    return (size == 0);

}


Coada::~Coada() {

    /*
        Complexitate = Theta(n)
    */

    delete[] elements;

}







