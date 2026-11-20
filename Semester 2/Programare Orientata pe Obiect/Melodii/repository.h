#ifndef MELODII1_REPOSITORY_H
#define MELODII1_REPOSITORY_H
#include "melodie.h"
#include <vector>
#include <algorithm>
#include <fstream>

class Repository{

private:

    string filename;
    vector<Melodie> melodii;
    void Read_form_file();
    void Write_to_file();

public:

    Repository(string nume_fisier) : filename(nume_fisier) {
        Read_form_file();
    }

    void add_melodie(const Melodie& melodie){
        melodii.push_back(melodie);
        Write_to_file();
    }

    void delete_melodie(int id){

        auto it = std::remove_if(melodii.begin(), melodii.end(), [id](const Melodie& melodie){

            return melodie.get_id() == id;

        });

        melodii.erase(it);
        Write_to_file();

    }

    int get_lungime(){
        return melodii.size();
    }

    vector <Melodie>& get_all(){
        return melodii;
    }

    const Melodie* find_melodie(int id){

        auto pred = [id](const Melodie& melodie) {
            return melodie.get_id() == id;
        };

        auto it = find_if(melodii.begin(), melodii.end(), pred);

        if (it != melodii.end())
            return &(*it);

        else
            return nullptr;
    }

    void actualizare_melodii(vector <Melodie>& vector){

        melodii.clear();
        for (auto& melodie : vector){
            melodii.push_back(melodie);
        }

        Write_to_file();

    }


};


#endif //MELODII1_REPOSITORY_H
