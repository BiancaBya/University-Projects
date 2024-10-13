#include "teste.h"
#include "exceptions.h"
#include <cassert>
#include "vector_dinamic.h"
#include "cos.h"


void Test::test_create_carte() {

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    assert(carte.get_titlu() == "Harry Potter");
    assert(carte.get_autor() == "JK");
    assert(carte.get_gen() == "SF");
    assert(carte.get_an() == 2000);

}

void Test::test_get_titlu() {

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    assert(carte.get_titlu() == "Harry Potter");

}

void Test::test_get_autor() {

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    assert(carte.get_autor() == "JK");

}

void Test::test_get_gen() {

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    assert(carte.get_gen() == "SF");

}

void Test::test_get_an() {

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    assert(carte.get_an() == 2000);

}

void Test::test_set_titlu(){

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    carte.set_titlu("blala");
    assert(carte.get_titlu() == "blala");

}

void Test::test_set_autor(){

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    carte.set_autor("haha");
    assert(carte.get_autor() == "haha");

}

void Test::test_set_gen(){

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    carte.set_gen("horror");
    assert(carte.get_gen() == "horror");

}

void Test::test_set_an(){

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    carte.set_an(3000);
    assert(carte.get_an() == 3000);

}





void Test::test_adaugare_carte() {

    ListaCarti lista;

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    lista.add_carte(carte);

    assert(lista.get_all()[lista.get_lungime() - 1].get_titlu() == "Harry Potter");
    assert(lista.get_lungime() == 1);

    carte = Carte("Blala", "JK", "SF", 2000);
    lista.add_carte(carte);

    assert(lista.get_lungime() == 2);
    assert(lista.get_all()[lista.get_lungime() - 1].get_titlu() == "Blala");

}

void Test::test_modificare_carte() {

    ListaCarti lista;

    Carte carte1 = Carte("Harry Potter", "JK", "SF", 2000);
    lista.add_carte(carte1);
    Carte carte2 = Carte("Blala", "JK", "SF", 2000);
    lista.modificare_carte(carte1, carte2);

    assert(lista.get_all()[lista.get_lungime() - 1].get_titlu() == "Blala");

}

void Test::test_stergere_carte() {

    ListaCarti lista;

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    Carte carte1 = Carte("Harry Potter", "JK", "SF", 2000);
    lista.add_carte(carte);
    lista.add_carte(carte1);
    carte = Carte("Blala", "JK", "SF", 2000);
    lista.add_carte(carte);

    lista.sterge_carte(carte);
    assert(lista.get_lungime() == 2);
    assert(lista.get_all()[lista.get_lungime() - 1].get_titlu() == "Harry Potter");

}

void Test::test_find_carte() {

    ListaCarti lista;

    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    lista.add_carte(carte);

    Carte carte1 = *lista.find_carte("Harry Potter", "JK", "SF", 2000);
    assert(carte1.get_titlu() == "Harry Potter");

    const Carte* carte_cautata_ptr = lista.find_carte("aaa", "aaaa", "aaaa", 10000);
    assert(carte_cautata_ptr == nullptr);

}


void Test::test_add_file(){

    RepoInFile lista("C:/Users/Next/CLionProjects/qt1/fisiertest.txt");
    Carte carte = Carte("Harry", "JK", "SF", 2000);

    lista.add_carte(carte);
    carte = lista.get_all()[lista.get_lungime()-1];
    assert(carte.get_titlu() == "Harry");

}


void Test::test_modifica_file(){

    RepoInFile lista("C:/Users/Next/CLionProjects/qt1/fisiertest.txt");
    Carte carte1 = Carte("haha", "haha", "haha", 2000);
    Carte carte2 = Carte("Blala", "JK", "SF", 2000);
    lista.add_carte(carte1);
    lista.add_carte(carte2);

    lista.modificare_carte(carte2, carte1);

}


void Test::test_sterge_file(){

    RepoInFile lista("C:/Users/Next/CLionProjects/qt1/fisiertest.txt");
    Carte carte = Carte("blala", "hihi", "bubu", 2000);
    lista.add_carte(carte);

    int lungime = lista.get_lungime();
    lista.sterge_carte(carte);
    assert(lungime == lista.get_lungime()+1);

}


void Test::test_add_dictionar(){

    RepoDictionar dictionar;
    dictionar.set_probabilitate(1);
    Carte carte = Carte("blala", "hihi", "bubu", 2000);
    dictionar.add_carte(carte);

    assert(dictionar.get_lungime() == 1);

}


void Test::test_modificare_dictionar(){

    RepoDictionar dictionar;
    dictionar.set_probabilitate(1);
    Carte carte1 = Carte("blala", "hihi", "bubu", 2000);
    Carte carte2 = Carte("haha", "hihi", "bubu", 2000);
    dictionar.add_carte(carte1);
    dictionar.modificare_carte(carte1, carte2);

    assert(dictionar.get_lungime() == 1);

}


void Test::test_sterge_dictionar() {

    RepoDictionar dictionar;
    dictionar.set_probabilitate(1);
    Carte carte = Carte("blala", "hihi", "bubu", 2000);
    dictionar.add_carte(carte);

    dictionar.sterge_carte(carte);
    assert(dictionar.get_lungime() == 0);

}


void Test::test_find_dictionar() {

    RepoDictionar dictionar;
    dictionar.set_probabilitate(1);
    Carte carte = Carte("blala", "hihi", "bubu", 2000);
    dictionar.add_carte(carte);
    carte = *dictionar.find_carte("blala", "hihi", "bubu", 2000);

    assert(carte.get_titlu() == "blala");

    const Carte *carte_prt = dictionar.find_carte("haha", "haha", "haha", 0);

    assert(carte_prt == nullptr);

}


void Test::test_all_dictionar() {

    RepoDictionar dictionar;
    dictionar.set_probabilitate(1);
    Carte carte = Carte("blala", "hihi", "bubu", 2000);
    dictionar.add_carte(carte);

    vector <Carte>& lista = dictionar.get_all();

    assert(lista.size() == dictionar.get_lungime());

}


void Test::test_exceptie_repo() {

    RepoDictionar dictionar;
    dictionar.set_probabilitate(-1);
    Carte carte = Carte("blala", "hihi", "bubu", 2000);
    try {
        dictionar.add_carte(carte);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Nu se poate efectua operatia!");
    }

}





void Test::test_service_add() {

    ListaCarti carti;
    CarteService service{carti};
    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    service.adaugare(carte);

    assert(service.get_all()[service.get_lung() - 1].get_titlu() == "Harry Potter");

}

void Test::test_service_modificare() {

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("Harry Potter", "JK", "SF", 2000);
    Carte carte2 = Carte("Blala", "JK", "SF", 2000);
    service.adaugare(carte1);

    service.modificare(carte1, carte2);

    assert(service.get_all()[service.get_lung() - 1].get_titlu() == "Blala");

}

void Test::test_service_stergere() {

    ListaCarti carti;
    CarteService service{carti};
    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    service.adaugare(carte);

    service.stergere(carte);
    assert(service.get_lung() == 0);

}

void Test::test_service_find() {

    ListaCarti carti;
    CarteService service{carti};
    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    service.adaugare(carte);

    Carte carte1 = *service.gasire_carte("Harry Potter", "JK", "SF", 2000);
    assert(carte1.get_titlu() == "Harry Potter");

}


void Test::test_filtrare_titlu() {

    vector<Carte> copie;
    int lungime=0;

    ListaCarti carti;
    CarteService service{carti};
    Carte carte = Carte("Harry Potter", "JK", "SF", 2000);
    service.adaugare(carte);
    carte = Carte("Blala", "JK", "SF", 2000);
    service.adaugare(carte);

    service.filtrare_titlu("Harry Potter", copie, lungime);
    assert(copie[0].get_titlu() == "Harry Potter");
    assert(lungime == 1);
    assert(copie.size() == 1);

}

void Test::test_filtrare_an() {

    vector<Carte> copie;
    int lungime=0;

    ListaCarti carti;
    CarteService service{carti};
    Carte carte = Carte("Harry Potter", "JK", "SF", 10000);
    service.adaugare(carte);
    carte = Carte("Blala", "JK", "SF", 20000);
    service.adaugare(carte);

    service.filtrare_an(10000, copie, lungime);
    assert(copie[0].get_titlu() == "Harry Potter");
    assert(lungime == 1);
    assert(copie.size() == 1);

}

void Test::test_comparare() {

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("aaa", "bbb", "aaa", 2000);
    Carte carte2 = Carte("bbb", "aaa", "bbb", 2000);

    assert(service.cmp_titlu(carte1, carte2) == 1);
    assert(service.cmp_autor(carte1, carte2) == 0);
    assert(service.cmp_gen(carte1, carte2) == 1);
    assert(service.cmp_an(carte1, carte2) == 1);

    carte2 = Carte("bbb", "aaa", "bbb", 3000);
    assert(service.cmp_an(carte1, carte2) == 1);

}

void Test::test_sortare() {

    vector<Carte> copie;
    int lungime=0;

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    service.adaugare(carte1);
    service.adaugare(carte2);

    service.sortare(CarteService::cmp_titlu, copie, lungime);
    assert(copie[0].get_titlu() == "aaa");

}


void Test::test_get_all(){

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    service.adaugare(carte1);
    service.adaugare(carte2);

    const vector<Carte>& elemente = service.get_all();
    assert(elemente[0].get_an() == 2000);

}


void Test::test_find_carte_titlu(){

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    service.adaugare(carte1);
    service.adaugare(carte2);

    carte1 = service.find_carte_titlu("aaa");
    assert(carte1.get_titlu() == "aaa");
    carte1 = service.find_carte_titlu("ccc");
    assert(carte1.get_titlu() == " ");

}



void Test::test_adauga_cos_cos(){

    Cos cos;
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    cos.adaugare_in_cos(carte1);
    cos.adaugare_in_cos(carte2);

    const vector<Carte>& elemente = cos.get_cos();
    assert(elemente.size() == 2);

}




void Test::test_golire_cos_cos(){

    Cos cos;
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    cos.adaugare_in_cos(carte1);
    cos.adaugare_in_cos(carte2);
    cos.golire_cos();

    const vector<Carte>& elemente = cos.get_cos();
    assert(elemente.size() == 0);

}

void Test::test_carti_random_cos(){

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    service.adaugare(carte1);
    service.adaugare(carte2);

    Cos cos;

    cos.random_carti(service.get_all(),2);
    assert(cos.get_cos().size() == 2);

}


void Test::test_export_cos_cos(){

    Cos cos;
    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    cos.adaugare_in_cos(carte1);
    cos.adaugare_in_cos(carte2);

    cos.export_html("C:/Users/Next/CLionProjects/qt1/ex_testat.html");

}



void Test::test_raport(){

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("bbb", "aaa", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    Carte carte3 = Carte("aaa", "bbb", "bbbb", 2000);
    service.adaugare(carte1);
    service.adaugare(carte2);
    service.adaugare(carte3);

    map <string, Autorcount> raport = service.report();
    assert(raport.size() == 2);
    for (auto& pereche : raport){
        int lungime = pereche.second.get_nr();
    }

}


void Test::test_undo(){

    ListaCarti carti;
    CarteService service{carti};
    Carte carte1 = Carte("bbb", "aaa", "aaaa", 2000);
    Carte carte2 = Carte("aaa", "aaa", "bbbb", 2000);
    Carte carte3 = Carte("aaa", "bbb", "bbbb", 2000);
    service.adaugare(carte1);
    service.adaugare(carte2);
    service.adaugare(carte3);

    service.Undo();
    assert(service.get_lung() == 2);

    service.modificare(carte1,carte3);
    service.Undo();

    service.stergere(carte1);
    service.Undo();
    assert(service.get_lung() == 2);

    service.Undo();
    service.Undo();

    assert(service.Undo() == -1);

}




void Test::test_validari(){

    ListaCarti carti;
    CarteService service{carti};
    Carte carte = Carte("b", "b", "a", 1);
    Carte carte_exceptie = Carte(" ", " ", " ", 0);

    try {
        service.adaugare(carte);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Date invalide!");
    }

    Carte carte3 = Carte("bb", "b", "a", 1);

    try {
        service.adaugare(carte3);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Date invalide!");
    }

    Carte carte4 = Carte("bb", "bb", "a", 1);

    try {
        service.adaugare(carte4);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Date invalide!");
    }

    Carte carte5 = Carte("bb", "bb", "aa", 1);

    try {
        service.adaugare(carte5);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Date invalide!");
    }

    Carte carte1 = Carte("bbb", "bbbb", "aaaa", 2000);
    Carte carte2 = Carte("bbb", "bbbb", "aaaa", 2000);
    service.adaugare(carte1);

    try {
        service.adaugare(carte2);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Cartea exista deja!");
    }

    try {
        service.modificare(carte1, carte);
    }catch (const Exceptions& exceptie){
        assert(exceptie.get_message() == "Date invalide!");
    }

    try{
        service.modificare(carte, carte1);
    }catch (const Exceptions& execptie){
        assert(execptie.get_message() == "Cartea nu exista!");
    }

}


void Test::test_mesaj() {

    IndexInvalid exceptie1;
    assert(exceptie1.get_message() == "Pozitie invalida!");

    CarteExistenta exceptie2;
    assert(exceptie2.get_message() == "Cartea exista deja!");

    Dateinvalide exceptie3;
    assert(exceptie3.get_message() == "Date invalide!");

    CarteaNuExista exceptie4;
    assert(exceptie4.get_message() == "Cartea nu exista!");


}



void Test::test_all() {

    test_create_carte();
    test_get_titlu();
    test_get_autor();
    test_get_gen();
    test_get_an();
    test_set_titlu();
    test_set_autor();
    test_set_gen();
    test_set_an();

    test_adaugare_carte();
    test_modificare_carte();
    test_stergere_carte();
    test_find_carte();
    test_add_file();
    test_modifica_file();
    test_sterge_file();
    test_add_dictionar();
    test_modificare_dictionar();
    test_sterge_dictionar();
    test_find_dictionar();
    test_all_dictionar();
    test_exceptie_repo();

    test_service_add();
    test_service_modificare();
    test_service_stergere();
    test_service_find();
    test_filtrare_titlu();
    test_filtrare_an();
    test_comparare();
    test_sortare();
    test_get_all();
    test_raport();
    test_find_carte_titlu();
    test_undo();

    test_adauga_cos_cos();
    test_golire_cos_cos();
    test_carti_random_cos();
    test_export_cos_cos();

    test_validari();
    test_mesaj();

}




















