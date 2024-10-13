#pragma once
#include <string>
#include <stdexcept>
using namespace std;
#define NULL_TVALOARE -1;

typedef int TElem;

class AB;
class IteratorAB;

class AB {
private:
    struct Nod {
        TElem elem;
        Nod* st;
        Nod* dr;

        Nod(TElem e, Nod* s = nullptr, Nod* d = nullptr) : elem(e), st(s), dr(d) {}
    };

    Nod* radacina;

    Nod* copiaza(const Nod* nod) const;
    void distruge(Nod* nod);

    // constructor arbore dintr-o radacina
    AB(Nod* nod);

public:
    friend class IteratorAB;
    friend class IteratorPreordine;
    friend class IteratorInordine;
    friend class IteratorPostordine;
    friend class IteratorLatime;

    // Constructorul implicit
    AB();

    // Constructor de copiere
    AB(const AB& ab);

    // Creeaza un arbore binar care se reduce la o frunza
    AB(TElem elem);

    // Constructor arbore binar
    AB(const AB& st, TElem elem, const AB& dr);

    // Adauga unui arbore binar subarborele stang
    // Arunca exceptie daca arborele este vid
    void adaugaSubSt(const AB& st);

    // Adauga unui arbore binar subarborele drept
    // Arunca exceptie daca arborele este vid
    void adaugaSubDr(const AB& dr);

    // Verifica daca arborele este vid
    bool vid() const;

    // Accesare element din radacina
    // Arunca exceptie daca arborele este vid
    TElem element() const;

    // Accesare subarbore stang
    // Arunca exceptie daca arborele este vid
    AB stang() const;

    // Accesare subarbore drept
    // Arunca exceptie daca arborele este vid
    AB drept() const;

    // Iteratori pe arborele binar (ordine="preordine", "postordine", "inordine", "latime")
    IteratorAB* iterator(string ordine) const;

    // Destructorul arborelui binar
    ~AB();

    int valoareMaxima() const;

};



