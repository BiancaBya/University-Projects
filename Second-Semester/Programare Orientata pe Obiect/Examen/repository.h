#ifndef EX_REPOSITORY_H
#define EX_REPOSITORY_H
#include "masina.h"
#include <vector>
#include <algorithm>
#include <assert.h>

class Repository{

private:

    vector<Masina> masini;
    string filename;
    void Write_to_file();
    void Read_from_file();

public:

    Repository(string file_name) : filename(file_name) {
        /// constructorul repository ului care primeste fisierul de intrare si care citeste din el
        Read_from_file();
    }

    void adauga_masina(const Masina& masina){
        /// masina este de tip Masina
        /// adauga masina primita ca argument in lista de masini
        masini.push_back(masina);
        Write_to_file();
    }

    void sterge_masina(string nr_inmatriculare){
        /// nr_inmatriculare este nr masinii care trebuie stearsa
        /// sterge masina cu nr de inmatriculare primit ca argument

        auto it = std::remove_if(masini.begin(), masini.end(), [nr_inmatriculare](const Masina& masina){

            return masina.get_inmatriculare() == nr_inmatriculare;

        });

        masini.erase(it);
        Write_to_file();

    }

    void minus(){
        /// decrementeaza anul cu 1 la fiecare masina

        for (auto& masina : masini){
            masina.set_an(masina.get_an() - 1);
        }
        Write_to_file();

    }

    void plus(){
        /// incrementeaza anul cu 1 la fiecare masina

        for (auto& masina : masini){
            masina.set_an(masina.get_an() + 1);
        }
        Write_to_file();

    }

    const Masina* find_masina(string nr_inm){
        /// gaseste o masina dupa nr de inmatriculare

        auto pred = [nr_inm](const Masina& masina) {
            return masina.get_inmatriculare() == nr_inm;
        };

        auto it = find_if(masini.begin(),masini.end(), pred);

        if (it != masini.end())
            return &(*it);

        else
            return nullptr;

    }

    vector<Masina>& get_all(){
        /// returneaza lista de masini
        return masini;
    }

};

void test_repo();


#endif //EX_REPOSITORY_H
