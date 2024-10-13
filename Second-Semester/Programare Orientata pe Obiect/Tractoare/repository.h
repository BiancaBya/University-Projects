#ifndef TRACTOR_REPOSITORY_H
#define TRACTOR_REPOSITORY_H
#include "tractor.h"
#include <algorithm>
#include <vector>

class Repository{

private:

    string filename;
    vector<Tractor> tractoare;
    void Read_from_file();
    void Write_to_file();


public:

    Repository(string file_name) : filename(file_name) {
        Read_from_file();
    }

    void add_tractor(const Tractor& tractor){
        tractoare.push_back(tractor);

        sort(tractoare.begin(), tractoare.end(), [](const Tractor& tr1, const Tractor& tr2){
            return tr1.get_denumire() < tr2.get_denumire();
        });

        Write_to_file();
    }

    void modifica_tractor(int id, const Tractor& tractor_nou){

        for (auto& tractor : tractoare){
            if (tractor.get_id() == id)
                tractor = tractor_nou;
        }
        Write_to_file();

    }


    const Tractor* find_tractor(int id){

        auto pred = [id](const Tractor& tractor) {
            return tractor.get_id() == id;
        };

        auto it = find_if(tractoare.begin(),tractoare.end(), pred);

        if (it != tractoare.end())
            return &(*it);

        else
            return nullptr;

    }


    vector <Tractor>& get_all(){
        return tractoare;
    }

};



#endif //TRACTOR_REPOSITORY_H
