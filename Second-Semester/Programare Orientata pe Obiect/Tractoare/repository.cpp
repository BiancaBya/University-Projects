#include "repository.h"
#include <fstream>

void Repository::Read_from_file() {

    ifstream file(filename);

    while (!file.eof()){

        int id, nrRoti;
        string denumire, tip;

        file >> id >> denumire >> tip >> nrRoti;

        if (denumire.empty())
            break;

        Tractor tractor(id, denumire, tip, nrRoti);

        tractoare.push_back(tractor);

        sort(tractoare.begin(), tractoare.end(), [](const Tractor& tr1, const Tractor& tr2){
           return tr1.get_denumire() < tr2.get_denumire();
        });

    }

    file.close();

}

void Repository::Write_to_file() {

    ofstream file(filename);
    vector <Tractor> vector = get_all();

    for (auto& tractor : vector){

        file << tractor.get_id() << " ";
        file << tractor.get_denumire().c_str() << " ";
        file << tractor.get_tip().c_str() << " ";
        file << tractor.get_nrRoti() << '\n';

    }

    file.close();

}


