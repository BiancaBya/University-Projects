#ifndef LAB9_SERVICE_H
#define LAB9_SERVICE_H

#include "repository.h"
#include <map>
#include <functional>
#include <memory>
#include "undo.h"

typedef int (CompareFct)(const Carte& x, const Carte& y);


class Autorcount{

private:

    int nr=1;
    string autor;

public:

    Autorcount() = default;

    void Incrementcout(){
        nr++;
    }

    [[nodiscard]] int get_nr() const{
        return nr;
    }

};


class CarteService
{

private:

    RepoAbstract& lista_service;
    vector<unique_ptr<Undo>> undo;

public:

    explicit CarteService(RepoAbstract& lista_service) : lista_service{lista_service}{};
    /*
        Constructorul listei din service care apeleaza repository-ul
    */

    [[nodiscard]] int get_lung() const;
    /*
        Returneaza lungimea listei de carti
    */

    void adaugare(const Carte& carte);
    /*
        adauga cartea carte in lista de carti
    */

    void modificare(const Carte& carte_stearsa, const Carte& carte_noua);
    /*
        modifica cartea de la pozitia poz cu carte
    */

    void stergere(const Carte& carte);
    /*
        sterge din lista de carti cartea de la pozitia poz
    */

    [[nodiscard]] const Carte* gasire_carte(const string& titlu, const string& autor, const string& gen, int an) const;
    /*
        gaseste cartea dupa titlu autor an si gen
    */

    void filtrare_titlu(const string& titlu, vector<Carte>& copie, int& lungime) const;
    /*
        filtreaza lista de carti, lasand in ea doar cartile cu titlul titlu, de tip string
    */

    void filtrare_an(int an, vector<Carte>& copie, int& lungime) const;
    /*
        filtreaza lista de carti, lasand in ea doar cartile care au anul an, de tip int
    */

    static int cmp_titlu(const Carte& x, const Carte& y);
    /*
        functia de comparare folosita la sortarea dupa titlu
    */

    static int cmp_autor(const Carte& x, const Carte& y);
    /*
        functia de comparare folosita la sortarea dupa autor
    */

    static int cmp_gen(const Carte& x, const Carte& y);
    /*
        functia de comparare folosita la sortarea dupa gen
    */

    static int cmp_an(const Carte& x, const Carte& y);
    /*
        functia de comparare folosita la sortarea dupa an
    */

    void sortare(CompareFct cmpfct, vector<Carte>& copie, int& lungime) const;
    /*
        functia de sortare care primeste o functie de comparare si sorteaza lista in functie de functia de comparare
    */

    const vector<Carte>& get_all(){
        return lista_service.get_all();
    }

    Carte find_carte_titlu(const string &titlu);

    [[nodiscard]] map<string, Autorcount> report() const;

    int Undo();

    ~CarteService() = default;


};



#endif //LAB9_SERVICE_H
