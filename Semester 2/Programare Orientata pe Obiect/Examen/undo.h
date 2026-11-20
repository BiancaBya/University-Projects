#ifndef EX_UNDO_H
#define EX_UNDO_H
#include "masina.h"
#include "repository.h"

class Undo{

public:

    virtual void undo() = 0;
    virtual ~Undo() = default;


};


class AdaugaUndo : public Undo{

private:

    Repository& repository;

public:

    AdaugaUndo(Repository& repo) : repository(repo){};

    void undo() override {
        repository.minus();
    }

};


class StergereUndo : public Undo{

private:

    Repository& repository;


public:

    StergereUndo(Repository& repo) : repository(repo){};

    void undo() override {
        repository.plus();

    }

};


class StergeMasinaUndo : public Undo{

private:

    Repository& repository;
    Masina masina;

public:

    StergeMasinaUndo(Repository& repo, Masina masina) : repository(repo), masina(masina){};

    void undo() override{
        repository.adauga_masina(masina);
    }

};

class Redo{

public:

    virtual void redo() = 0;
    virtual ~Redo() = default;


};


class AdaugaRedo : public Redo{

private:

    Repository& repository;

public:

    AdaugaRedo(Repository& repo) : repository(repo){};

    void redo() override {
        repository.minus();
    }

};


class StergereRedo : public Redo{

private:

    Repository& repository;

public:

    StergereRedo(Repository& repo) : repository(repo){};

    void redo() override {
        repository.plus();

    }

};

class StergeMasinaRedo : public Redo{

private:

    Repository& repository;
    Masina masina;

public:

    StergeMasinaRedo(Repository& repo, Masina masina) : repository(repo), masina(masina){};

    void redo() override{
        repository.sterge_masina(masina.get_inmatriculare());
    }

};

#endif //EX_UNDO_H
