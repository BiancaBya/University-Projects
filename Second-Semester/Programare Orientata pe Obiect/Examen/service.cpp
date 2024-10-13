#include "service.h"
#include <iostream>
using namespace std;

void test_service(){

    Repository repo ("C:/Users/Next/CLionProjects/ex/test.txt");
    Service service(repo);
    vector<Masina> masini = service.get_all();
    assert(masini.size() == 7);
    service.adauga(Masina("a", "a", 1, "a"));
    masini = service.get_all();
    assert(masini.size() == 8);
    service.stergere("a");
    masini = service.get_all();
    assert(masini.size() == 7);

    repo.minus();
    repo.plus();
    masini = service.get_all();
    service.Undo();
    service.Redo();
    assert(masini[7].get_an() == 172);
}
