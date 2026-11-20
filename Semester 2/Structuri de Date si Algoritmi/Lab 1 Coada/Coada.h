#pragma once
using namespace std;

typedef int TElem;

class Coada
{
private:

    TElem* elements;
    int capacity;
    int size;
    int index_actual;
    int index_primul;

public:

    //constructorul implicit
    Coada();

    //adauga un element in coada
    void adauga(TElem e);


    void redim();


    //acceseaza elementul cel mai devreme introdus in coada
    //arunca exceptie daca coada e vida
    TElem element() const;


    //sterge elementul cel mai devreme introdus in coada si returneaza elementul sters (principiul FIFO)
    //arunca exceptie daca coada e vida
    TElem sterge();

    //elimina si returneaza elementul minim din coada
    //arunca exceptie daca coada este goala
    TElem stergeMinim();


    //verifica daca coada e vida;
    bool vida() const;


    // destructorul cozii
    ~Coada();
};




