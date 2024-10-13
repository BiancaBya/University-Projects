#include "IteratorMultime.h"
#include "Multime.h"
#include <exception>
#include <iostream>

using namespace std;


IteratorMultime::IteratorMultime(const Multime& m) : multime(m), curent(m.primul){}


void IteratorMultime::prim() {

    // Complexitate = theta(1)
    curent = multime.primul;

}


void IteratorMultime::urmator() {

    // Complexitate = theta(1)
    if (!valid()){
        throw runtime_error("Iterator invalid!");
    }
    curent = curent->dupa;

}


TElem IteratorMultime::element() const {

    // Complexitate = theta(1)
    if (!valid()){
        throw runtime_error("Iterator invalid!");
    }
	return curent->element;
}

bool IteratorMultime::valid() const {

    // Complexitate = theta(1)
    if (curent == nullptr)
	    return false;

    return true;
}


