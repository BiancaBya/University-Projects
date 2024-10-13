#ifndef EX_SERVICE_H
#define EX_SERVICE_H
#include "repository.h"
#include "undo.h"
#include "unistd.h"
#include <map>
#include <functional>
#include <memory>

class Service{

private:

    Repository& service;
    vector<unique_ptr<Undo>> undo;
    vector<unique_ptr<Redo>> redo;

public:

    Service(Repository& service) : service(service){};

    void adauga(const Masina& masina){
        /// adauga masina primita ca argument in lista de masini
        service.adauga_masina(masina);
    }

    void stergere(string nr_inmatriculare){
        /// nr_inmatriculare este nr masinii care trebuie stearsa
        /// sterge masina cu nr de inmatriculare primit ca argument

        undo.push_back(make_unique<StergeMasinaUndo>(service, *service.find_masina(nr_inmatriculare)));
        redo.push_back(make_unique<StergeMasinaRedo>(service, *service.find_masina(nr_inmatriculare)));
        service.sterge_masina(nr_inmatriculare);
    }

    void plus(){
        /// incrementeaza anul cu 1 la fiecare masina
        service.plus();
        undo.push_back(make_unique<AdaugaUndo>(service));
        redo.push_back(make_unique<StergereRedo>(service));

    }

    void minus(){
        /// decrementeaza anul cu 1 la fiecare masina
        service.minus();
        undo.push_back(make_unique<StergereUndo>(service));
        redo.push_back(make_unique<AdaugaRedo>(service));
    };


    const Masina* find_masina(string nr_inm){
        /// gaseste o masina dupa nr de inmatriculare
        return service.find_masina(nr_inm);
    }

    vector <Masina>& get_all(){
        /// returneaza lista de masini
        return service.get_all();
    }

    int Undo() {
        /// functia face undo din lista de undo si daca nu se mai poate face undo returneaza -1

        if (undo.empty())
            return -1;

        undo.back()->undo();
        undo.pop_back();
        return 0;

    }

    int Redo() {
        /// functia face redo din lista de redo si daca nu se mai poate face redo se returneaza -1

        if (redo.empty())
            return -1;

        redo.back()->redo();
        redo.pop_back();
        return 0;

    }

};

void test_service();

#endif //EX_SERVICE_H
