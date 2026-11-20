#ifndef QT1_UNDO_H
#define QT1_UNDO_H

#include "carte.h"
#include "repository.h"
#include <vector>
#include <algorithm>

class Undo{

public:

    virtual void undo() = 0;
    virtual ~Undo() = default;


};


class AdaugaUndo : public Undo{

private:

    RepoAbstract& repository;
    Carte carte;

public:

    AdaugaUndo(RepoAbstract& repo, const Carte& carte) : repository(repo), carte(carte){};

    void undo() override {
        repository.sterge_carte(carte);
    }

};


class StergereUndo : public Undo{

private:

    RepoAbstract& repository;
    Carte carte_stearsa;

public:

    StergereUndo(RepoAbstract& repo, const Carte& carte) : repository(repo), carte_stearsa(carte) {};

    void undo() override {

        repository.add_carte(carte_stearsa);

    }

};


class ModificareUndo : public Undo{

private:

    RepoAbstract& repository;
    Carte carte_stearsa;
    Carte carte_adaugata;

public:

    ModificareUndo(RepoAbstract& repo, const Carte& carte_stearsa, const Carte& carte_adaugata) : repository(repo), carte_stearsa(carte_stearsa), carte_adaugata(carte_adaugata){};

    void undo() override {

        repository.modificare_carte(carte_adaugata, carte_stearsa);

    }


};

#endif //QT1_UNDO_H


