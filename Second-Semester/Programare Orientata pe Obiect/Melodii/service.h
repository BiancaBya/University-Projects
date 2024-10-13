#ifndef MELODII1_SERVICE_H
#define MELODII1_SERVICE_H
#include "repository.h"


class Service{

private:

    Repository& service;

public:

    explicit Service(Repository& repository) : service(repository){};

    int get_lungime(){
        return service.get_lungime();
    }

    void adaugare(const Melodie& melodie){
        service.add_melodie(melodie);
    }

    void sterge(int id){
        service.delete_melodie(id);
    }

    const Melodie* find_melodie(int id){
        return service.find_melodie(id);
    }

    vector <Melodie> get_all(){
        return service.get_all();
    }

    vector <int> get_ranks(){

        vector <int> ranks;

        for (int i=0; i <= 10; i++)
            ranks.push_back(0);

        vector <Melodie> melodii = get_all();
        for (const auto& melodie : melodii){
            ranks[melodie.get_rank()] ++;
        }

        return ranks;

    }

    void actualizare_melodii(vector <Melodie>& vector){
        service.actualizare_melodii(vector);
    }

};


#endif //MELODII1_SERVICE_H
