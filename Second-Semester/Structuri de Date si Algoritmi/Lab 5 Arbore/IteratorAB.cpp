#include "IteratorAB.h"


//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
IteratorPreordine::IteratorPreordine(const AB& ab) : arbore(ab) {
    prim();
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void IteratorPreordine::prim() {
    while (!stiva.empty()) stiva.pop();
    if (arbore.radacina != nullptr) stiva.push(arbore.radacina);
}

//Theta(1) favorabil
void IteratorPreordine::urmator() {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    const AB::Nod* curent = stiva.top();
    stiva.pop();
    if (curent->dr != nullptr) stiva.push(curent->dr);
    if (curent->st != nullptr) stiva.push(curent->st);
}

//Theta(1) favorabil
bool IteratorPreordine::valid() const {
    return !stiva.empty();
}

//Theta(1) favorabil
TElem IteratorPreordine::element() const {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    return stiva.top()->elem;
}


//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
IteratorInordine::IteratorInordine(const AB& ab) : arbore(ab) {
    prim();
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void IteratorInordine::mergiLaStanga(const AB::Nod* p) {
    while (p != nullptr) {
        stiva.push(p);
        p = p->st;
    }
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void IteratorInordine::prim() {
    while (!stiva.empty()) stiva.pop();
    mergiLaStanga(arbore.radacina);
}

//Theta(1) favorabil
void IteratorInordine::urmator() {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    const AB::Nod* curent = stiva.top();
    stiva.pop();
    if (curent->dr != nullptr) mergiLaStanga(curent->dr);
}

//Theta(1) favorabil
bool IteratorInordine::valid() const {
    return !stiva.empty();
}

//Theta(1) favorabil
TElem IteratorInordine::element() const {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    return stiva.top()->elem;
}


//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
IteratorPostordine::IteratorPostordine(const AB& ab) : arbore(ab) {
    prim();
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void IteratorPostordine::init() {
    while (!stiva1.empty()) stiva1.pop();
    while (!stiva2.empty()) stiva2.pop();
    if (arbore.radacina != nullptr) {
        stiva1.push(arbore.radacina);
        while (!stiva1.empty()) {
            const AB::Nod* nod = stiva1.top();
            stiva1.pop();
            stiva2.push(nod);
            if (nod->st != nullptr) stiva1.push(nod->st);
            if (nod->dr != nullptr) stiva1.push(nod->dr);
        }
    }
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void IteratorPostordine::prim() {
    init();
}

//Theta(1) favorabil
void IteratorPostordine::urmator() {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    stiva2.pop();
}

//Theta(1) favorabil
bool IteratorPostordine::valid() const {
    return !stiva2.empty();
}

//Theta(1) favorabil
TElem IteratorPostordine::element() const {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    return stiva2.top()->elem;
}


//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
IteratorLatime::IteratorLatime(const AB& ab) : arbore(ab) {
    prim();
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void IteratorLatime::prim() {
    while (!coada.empty()) coada.pop();
    if (arbore.radacina != nullptr) coada.push(arbore.radacina);
}

//Theta(1) favorabil
void IteratorLatime::urmator() {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    const AB::Nod* curent = coada.front();
    coada.pop();
    if (curent->st != nullptr) coada.push(curent->st);
    if (curent->dr != nullptr) coada.push(curent->dr);
}

//Theta(1) favorabil
bool IteratorLatime::valid() const {
    return !coada.empty();
}

//Theta(1) favorabil
TElem IteratorLatime::element() const {
    if (!valid()) throw std::out_of_range("Iterator invalid");
    return coada.front()->elem;
}


