#include "AB.h"
#include "IteratorAB.h"

//Theta(1)
AB::AB() : radacina(nullptr) {}

//O(n)
AB::AB(const AB& ab) : radacina(copiaza(ab.radacina)) {}

//Theta(1)
AB::AB(TElem elem) : radacina(new Nod(elem)) {}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
AB::AB(const AB& st, TElem elem, const AB& dr) : radacina(new Nod(elem, copiaza(st.radacina), copiaza(dr.radacina))) {}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
AB::AB(Nod* nod) : radacina(copiaza(nod)) {}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
AB::Nod* AB::copiaza(const Nod* nod) const {
    if (nod == nullptr) return nullptr;
    return new Nod(nod->elem, copiaza(nod->st), copiaza(nod->dr));
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void AB::distruge(Nod* nod) {
    if (nod != nullptr) {
        distruge(nod->st);
        distruge(nod->dr);
        delete nod;
    }
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void AB::adaugaSubSt(const AB& st) {
    if (vid()) throw runtime_error("Arborele este vid.");
    if (radacina->st != nullptr) delete radacina->st;
    radacina->st = copiaza(st.radacina);
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
void AB::adaugaSubDr(const AB& dr) {
    if (vid()) throw runtime_error("Arborele este vid.");
    if (radacina->dr != nullptr) delete radacina->dr;
    radacina->dr = copiaza(dr.radacina);
}

//Theta (1)
bool AB::vid() const {
    return radacina == nullptr;
}

//Theta (1)
TElem AB::element() const {
    if (vid()) throw runtime_error("Arborele este vid.");
    return radacina->elem;
}

//Theta (1)
AB AB::stang() const {
    if (vid()) throw runtime_error("Arborele este vid.");
    return AB(radacina->st);
}

//Theta (1)
AB AB::drept() const {
    if (vid()) throw runtime_error("Arborele este vid.");
    return AB(radacina->dr);
}

//Theta(1) favorabil
//Theta(n) defavorabil
//O(n)
AB::~AB() {
    distruge(radacina);
}

//Theta(1) favorabil
IteratorAB* AB::iterator(string ordine) const {
    if (ordine == "preordine") return new IteratorPreordine(*this);
    if (ordine == "inordine") return new IteratorInordine(*this);
    if (ordine == "postordine") return new IteratorPostordine(*this);
    if (ordine == "latime") return new IteratorLatime(*this);
    throw runtime_error("Ordine necunoscuta.");
}

// Theta(n) , verifica viecare valoare din arbore
int AB::valoareMaxima() const {

    if (vid())
        return NULL_TVALOARE;

    AB ab(radacina);
    IteratorAB* iterator = ab.iterator("latime");
    iterator->prim();

    TElem maxim = iterator->element();

    while (iterator->valid()) {
        TElem e = iterator->element();
        if (e > maxim)
            maxim = e;
        iterator->urmator();
    }

    return maxim;

}

/*
Subalgoritm valoareMaxima

    daca vid()
        @returneaza NULL_TVALOARE

    creeazaArbore(ab,radacina)
    creeazaIterator(ab,iterator)
    prim(iterator)

    maxim <- element(iterator)

    cat timp valid(iterator)
        e <- element(iterator)
        daca e > maxim
            maxim <- e
        urmator(iterator)
    sfarsitCattimp

    @returneaza maxim

*/


