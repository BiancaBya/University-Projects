#include "repository.h"
#include "fstream"
#include <algorithm>

void Repository::Read_form_file() {

    ifstream file(filename);

    while (!file.eof()){

        int id, rank;
        string titlu, autor;

        file >> id >> titlu >> autor >> rank;

        if (titlu.empty())
            break;

        Melodie melodie(id, titlu, autor, rank);

        add_melodie(melodie);

        sort(melodii.begin(), melodii.end(), [](const Melodie& m1, const Melodie& m2){
            return m1.get_rank() < m2.get_rank();
        });

    }

    file.close();

}

void Repository::Write_to_file() {

    ofstream file(filename);
    vector <Melodie> vector = get_all();

    sort(vector.begin(), vector.end(), [](const Melodie& m1, const Melodie& m2){
       return m1.get_rank() < m2.get_rank();
    });

    for (auto& melodie : vector){

        file << melodie.get_id() << " ";
        file << melodie.get_titlu().c_str() << " ";
        file << melodie.get_artist().c_str() << " ";
        file << melodie.get_rank() << '\n';

    }

    file.close();

}

