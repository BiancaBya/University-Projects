#include "repository.h"
#include <fstream>
#include <algorithm>

void Repository::Read_from_file() {

    ifstream file(filename);

    while (!file.eof()){

        string inmatriculare, denumire, tip;
        int an;

        file >> inmatriculare >> denumire >> an >> tip;

        if (inmatriculare.empty())
            break;

        Masina masina(inmatriculare, denumire, an, tip);

        masini.push_back(masina);

        sort(masini.begin(), masini.end(), [](const Masina& m1, const Masina& m2){
            return m1.get_inmatriculare() < m2.get_inmatriculare();
        });

    }

    file.close();

}

void Repository::Write_to_file() {

    ofstream file(filename);
    vector <Masina> vector = get_all();


    for (auto& masina : vector){

        file << masina.get_inmatriculare().c_str() << " ";
        file << masina.get_denumire().c_str() << " ";
        file << masina.get_an() << " ";
        file << masina.get_tip() << '\n';

    }

    sort(masini.begin(), masini.end(), [](const Masina& m1, const Masina& m2){
        return m1.get_inmatriculare() < m2.get_inmatriculare();
    });

    file.close();

}

void test_repo(){

    Repository repo ("C:/Users/Next/CLionProjects/ex/test.txt");
    vector<Masina> masini = repo.get_all();
    assert(masini.size() == 7);
    repo.adauga_masina(Masina("a", "a", 1, "a"));
    masini = repo.get_all();
    assert(masini.size() == 8);
    repo.sterge_masina("a");
    masini = repo.get_all();
    assert(masini.size() == 7);

    Masina masina("a", "a", 1, "a");
    assert(masina.get_inmatriculare() == "a");
    assert(masina.get_an() == 1);
    assert(masina.get_tip() == "a");
    assert(masina.get_denumire() == "a");

}
