#include "Dictionar.h"
#include <iostream>
#include "IteratorDictionar.h"

class
{
public:
    QuadProbHash operator [](int dispers)
    {
        return ([=](int c, int i)
        {
            int m = 1 << dispers;
            m--;
            return ((c & m) + i * (i + 1) / 2) & m;
        });
    }
} functie_generator;


bool Dictionar::add_elem(TElem* table, int cap, QuadProbHash d, TElem e)
{

    // BC Theta(1)
    // WC Theta(n)

    for (int i = 0; i < cap; i++)
    {
        int pos = d(e.elem_key, i);
        if (is_free(table[pos]))
        {
            table[pos] = e;
            return true;
        }
    }
    return false;
}


Dictionar::Dictionar() {

    // Complexitate Theta(1)

    dispersie = 4;
    functie_hash = functie_generator[dispersie];
    Tabela_dispersie = new TElem[1 << dispersie]; // 2 la puterea dispersie
    for (int i = 0, cap = (1 << dispersie); i < cap; i++)
    {
        Tabela_dispersie[i] = NONE;
    }
    nr_elem_dictionar = 0;
}

Dictionar::~Dictionar() {
    delete[] Tabela_dispersie;
}


TValoare Dictionar::adauga(TCheie c, TValoare v){

    // BC Theta(1)
    // WC O(n)

    int pos = get_pos_of_key(c);

    if (pos >= 0)
    {
        int old_val = Tabela_dispersie[pos].elem_value;
        Tabela_dispersie[pos].elem_value = v;
        return old_val;
    }


    if (!add_elem(Tabela_dispersie, 1 << dispersie, functie_hash, {c, v }))
    {
        // resize si rehash
        int capacitate_veche = 1 << dispersie;
        dispersie++;
        functie_hash = functie_generator[dispersie];
        auto* tabela_noua = new TElem[1 << dispersie];

        for (int i = 0, cap = (1 << dispersie); i < cap; i++)
            tabela_noua[i] = NONE;

        for (int i = 0; i < capacitate_veche; i++)
        {
            if (Tabela_dispersie[i].elem_key != NONE.elem_key)
            {
                add_elem(tabela_noua, 1 << dispersie, functie_hash, Tabela_dispersie[i]);
            }
        }
        delete[] Tabela_dispersie;
        Tabela_dispersie = tabela_noua;

        add_elem(Tabela_dispersie, 1 << dispersie, functie_hash, {c, v });
    }
    nr_elem_dictionar++;
    return NULL_TVALOARE;
}


int Dictionar::get_pos_of_key(TCheie elem_key) const
{

    // BC Theta(1)
    // WC O(n)

    for (int i = 0, cap = (1 << dispersie); i < cap; i++)
    {
        int pos = functie_hash(elem_key, i);
        if (Tabela_dispersie[pos].elem_key == elem_key)
            return pos;
    }
    return -1; // Tabela_dispersie full
}



TValoare Dictionar::cauta(TCheie c) const{

    // BC Theta(1)
    // WC O(n)

    int pos = get_pos_of_key(c);
    return pos >= 0 ? Tabela_dispersie[pos].elem_value : NULL_TVALOARE;
}


TValoare Dictionar::sterge(TCheie c)
{

    // Complexitate Theta(n)

    int free_count = 0;
    for (int i = 0, cap = (1 << dispersie); i < cap; i++)
    {
        int pos = functie_hash(c, i);
        if (is_free(Tabela_dispersie[pos]))
            free_count++;
        if (Tabela_dispersie[pos].elem_key == NONE.elem_key)
        {
            return NULL_TVALOARE;
        }
        if (Tabela_dispersie[pos].elem_key == c)
        {
            int val = Tabela_dispersie[pos].elem_value;

            Tabela_dispersie[pos] = DELETED;
            nr_elem_dictionar--;

            return val;
        }
    }
    if (free_count == (1 << dispersie)) // daca tabela e goala, se reseteaza toate pozitiile la NONE
    {
        for (int i = 0, cap = (1 << dispersie); i < cap; i++)
            Tabela_dispersie[i] = NONE;

    }
    return NULL_TVALOARE;
}


int Dictionar::dim() const {

    // Complexitate Theta(1)

    return nr_elem_dictionar;
}


bool Dictionar::vid() const{

    // Complexitate Theta(1)

    return nr_elem_dictionar == 0;
}


IteratorDictionar Dictionar::iterator() const {

    // Complexitate Theta(1)

    return  IteratorDictionar(*this);
}


// pentru o pozitie in tabela verificam daca nu este ocupata de elemente
bool Dictionar::is_free(TElem elem)
{
    // Complexitate Theta(1)
    return elem.elem_key >= INT_MAX - 1;
}

int Dictionar::adaugaInexistente(Dictionar& dict){

    // O(n)

    int nr_perechi = 0;
    IteratorDictionar it = dict.iterator();

    while(it.valid()){

        TElem element = it.element();
        if(cauta(element.first) == NULL_TVALOARE){
            adauga(element.first, element.second);
            nr_perechi++;
        }

        it.urmator();
    }

    return nr_perechi;
}


/*
Algoritm adaugaInexistente(dict)
    nr_perechi <- 0
    it <- dict.iterator()

    cat timp valid(it)
        element = it.element();
        daca dict.cauta(element.first == NULL_TVALOARE)
            dict.adauga()
            nr_perechi++;
        it.urmator()

    @returneaza nr_perechi
*/


