#ifndef TRACTOR_SERVICE_H
#define TRACTOR_SERVICE_H
#include "repository.h"
#include <utility>

class Service{

private:

    Repository& service;

public:

    Service(Repository& repo) : service(repo) {};

    void adauga(const Tractor& tractor){
        service.add_tractor(tractor);
    }

    void modifica(int id, const Tractor& tractor){
        service.modifica_tractor(id, tractor);
    }

    vector <Tractor>& get_all(){
        return service.get_all();
    }

    const Tractor* find_tractor(int id){
        return service.find_tractor(id);
    }

    vector<pair<int,string>> get_tipuri(){

        vector<pair<int,string>> tipuri;

        for (const auto& tractor : get_all()){

            int nr = 0;
            for (auto& pereche : tipuri){
                if (pereche.second == tractor.get_tip()) {
                    nr = pereche.first;
                    pereche.first++;
                }
            }

            if (nr == 0)
                tipuri.emplace_back(1,tractor.get_tip());

        }

        return tipuri;
    }


};


#endif //TRACTOR_SERVICE_H
