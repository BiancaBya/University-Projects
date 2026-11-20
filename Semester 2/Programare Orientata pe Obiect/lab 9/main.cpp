#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#include <iostream>
#include <string>
#include "service.h"
#include "teste.h"
#include "exceptions.h"
#include "validator.h"
#include "cos.h"

using namespace std;


void print_menu(){

    cout << "-BIBLIOTECA-\n";
    cout << "1. Adaugare carte\n";
    cout << "2. Modificare carte\n";
    cout << "3. Stergere carte\n";
    cout << "4. Afisare lista carti\n";
    cout << "5. Cautare carte\n";
    cout << "6. Filtrare carti\n";
    cout << "7. Sortare carti\n";
    cout << "8. Adauga in cos\n";
    cout << "9. Afisare cos\n";
    cout << "10. Goleste cos\n";
    cout << "11. Carti random\n";
    cout << "12. Export in fisier HTML\n";
    cout << "13. Report autori\n";
    cout << "14. Baga carti\n";
    cout << "15. Undo\n";
    cout << "0. Break\n";

}


void afisare_lista(CarteService& lista){

    const vector<Carte>& elemente = lista.get_all();

    if (elemente.empty())
        cout << "Lista este vida\n";

    else{
        cout << "Lista este:\n";
        for (const auto& element: elemente){
            cout << endl << "Titlu: " << element.get_titlu().c_str() << endl;
            cout << "Autor: " << element.get_autor().c_str() << endl;
            cout << "Gen: " << element.get_gen().c_str() << endl;
            cout << "An: " << element.get_an() << endl << endl;
        }

    }

}

void print_lista(vector<Carte>& lista, int& lg){

    for(int i=0; i < lg; i++) {

        cout << endl << "Titlu: " << lista[i].get_titlu().c_str() << endl;
        cout << "Autor: " << lista[i].get_autor().c_str() << endl;
        cout << "Gen: " << lista[i].get_gen().c_str() << endl;
        cout << "An: " << lista[i].get_an() << endl << endl;

    }

}



void UI_add(CarteService& lista){

    string titlu, autor, gen;
    int an;

    cout << "Titlu:";
    getline(cin, titlu);
    cout << "Autor:";
    getline(cin, autor);
    cout << "Gen:";
    getline(cin, gen);
    cout << "An:";
    cin >> an;

    Carte carte(titlu, autor, gen, an);

    try {
        lista.adaugare(carte);
    }catch (const Exceptions& exceptie){
        cout << exceptie.get_message() << '\n';
    }

}


void UI_modificare(CarteService& lista){

    string titlu_nou, autor_nou, gen_nou;
    string titlu_sters, autor_sters, gen_sters;
    int an_nou, an_sters;

    cout << "Titlu cautat:";
    getline(cin, titlu_sters);
    cout << "Autor cautat:";
    getline(cin, autor_sters);
    cout << "Gen cautat:";
    getline(cin, gen_sters);
    cout << "An cautat:";
    cin >> an_sters;

    cin.ignore(1000, '\n');

    cout << "Titlu nou:";
    getline(cin, titlu_nou);
    cout << "Autor nou:";
    getline(cin, autor_nou);
    cout << "Gen nou:";
    getline(cin, gen_nou);
    cout << "An nou:";
    cin >> an_nou;

    Carte carte_noua(titlu_nou, autor_nou, gen_nou, an_nou);
    Carte carte_stearsa(titlu_sters, autor_sters, gen_sters, an_sters);

    try {
        lista.modificare(carte_stearsa, carte_noua);
    }catch (const Exceptions& exceptie){
        cout << exceptie.get_message() << '\n';
    }

}


void UI_stergere(CarteService& lista){

    string titlu, autor, gen;
    int an;

    cout << "Titlu cautat:";
    getline(cin, titlu);
    cout << "Autor cautat:";
    getline(cin, autor);
    cout << "Gen cautat:";
    getline(cin, gen);
    cout << "An cautat:";
    cin >> an;

    const Carte* carte_cautata_ptr = lista.gasire_carte(titlu, autor, gen, an);
    if (carte_cautata_ptr == nullptr)
        cout << "Cartea nu exista!\n";

    else {
        Carte carte = *lista.gasire_carte(titlu, autor, gen, an);

        try {
            lista.stergere(carte);
        } catch (const Exceptions &exceptie) {
            cout << exceptie.get_message() << '\n';
        }
    }

}


void UI_cautare_carte(CarteService& lista){

    string titlu, autor, gen;
    int an;

    cout << "Titlu carte:";
    getline(cin, titlu);
    cout << "Autor carte:";
    getline(cin, autor);
    cout << "Gen carte:";
    getline(cin, gen);
    cout << "An carte:";
    cin >> an;

    const Carte* carte_cautata_ptr = lista.gasire_carte(titlu, autor, gen, an);
    if (carte_cautata_ptr == nullptr)
        cout << "Cartea nu exista!\n";
    else {
        const Carte& carte = *lista.gasire_carte(titlu, autor, gen, an);

        cout << endl << "Titlu: " << carte.get_titlu() << endl;
        cout << "Autor: " << carte.get_autor() << endl;
        cout << "Gen: " << carte.get_gen() << endl;
        cout << "An: " << carte.get_an() << endl << endl;
    }

}


void UI_filtrare(const CarteService& lista){


    vector<Carte> copie;
    int lungime=0;


    int optiune, an;
    string titlu;
    cout << "Filtrare dupa:\n1.titlu\n2.an\n";
    cin >> optiune;
    cin.ignore(1000, '\n');

    if (optiune == 1) {
        cout << "Titlu:";
        getline(cin, titlu);
        lista.filtrare_titlu(titlu, copie, lungime);
    }

    else if (optiune == 2) {
        cout << "An:";
        cin >> an;
        lista.filtrare_an(an, copie, lungime);
    }
    else{
        cout << "Optiune invalida!";
    }

    cout << "Lista filtrata este:\n";
    print_lista(copie, lungime);

}


void UI_sortare(CarteService& lista){


    vector<Carte> copie;
    int lungime=0;

    int optiune;

    cout << "Sortare dupa:\n1.titlu\n2.autor\n3.an + gen\n";
    cin >> optiune;
    cin.ignore(1000, '\n');

    if (optiune == 1){
        lista.sortare(&CarteService::cmp_titlu, copie, lungime);
    }
    else if (optiune == 2){
        lista.sortare(&CarteService::cmp_autor, copie, lungime);
    }
    else if (optiune == 3){
        lista.sortare(&CarteService::cmp_an, copie, lungime);
    }
    else
        cout << "Optiune invalida!";

    cout << "Lista sortata este:\n";
    print_lista(copie, lungime);

}


void UI_afisare_cos(Cos& cos){

    const vector<Carte> elemente = cos.get_cos();

    if (elemente.empty())
        cout << "Lista este vida\n";

    else{
        cout << "Lista este:\n";
        for (const auto& element: elemente){
            cout << endl << "Titlu: " << element.get_titlu().c_str() << endl;
            cout << "Autor: " << element.get_autor().c_str() << endl;
            cout << "Gen: " << element.get_gen().c_str() << endl;
            cout << "An: " << element.get_an() << endl << endl;
        }

    }

}


void UI_add_cos(CarteService& lista, Cos& cos){

    string titlu;
    cout << "Titlul cartii de adaugat in cos:";
    getline(cin, titlu);

    Carte carte = lista.find_carte_titlu(titlu);

    if (carte.get_titlu() != " ") {
        cos.adaugare_in_cos(carte);
        cout << "Carte adaugata cu succes";
    }
    else
        cout << "Cartea nu exista!";

}

void UI_goleste_cos(Cos& cos){

    cos.golire_cos();

}


void UI_carti_random(CarteService& lista, Cos& cos){

    int n;
    cout << "Numarul de carti de generat:";
    cin >> n;
    cin.ignore(1000, '\n');
    if(n > lista.get_lung()){
        cout << "Se pot adauga doar " << lista.get_lung() << " carti.\n";
        n = lista.get_lung();
    }
    cos.random_carti(lista.get_all(),n);

}




void UI_report(CarteService& lista){

    map <string, Autorcount> report = lista.report();
    for(auto& pereche : report){
        cout << "Autorul " << pereche.first << " are " << pereche.second.get_nr() << " carti\n";
    }

}


void UI_export_HTML(Cos& cos){

    cout << "Nume fisier:";
    string file_name;
    getline(cin, file_name);
    cos.export_html(file_name);

}


void UI_baga_carti(CarteService& lista){

    Carte carte1 = Carte("aaa", "aaa", "aaaa", 2222);
    Carte carte2 = Carte("bbb", "bbb", "bbbb", 3333);
    Carte carte3 = Carte("ccc", "ccc", "cccc", 4444);
    Carte carte4 = Carte("vvv", "vvv", "vvvv", 2222);
    Carte carte5 = Carte("bbb", "bbb", "bbbb", 4444);
    Carte carte6 = Carte("qqq", "qqq", "qqqq", 5555);

    lista.adaugare(carte1);
    lista.adaugare(carte2);
    lista.adaugare(carte3);
    lista.adaugare(carte4);
    lista.adaugare(carte5);
    lista.adaugare(carte6);

}


void UI_undo(CarteService& lista){

    if (lista.Undo() == -1)
        cout << "Nu se mai poate face undo!\n";
    else{
        cout << "Undo realizat cu succes\n";
    }

}


void run(){

    //ListaCarti carti;
    RepoInFile carti("C:/Users/Next/CLionProjects/lab9/fisier.txt");
    //RepoDictionar carti;

    CarteService lista{carti};
    Cos cos;

    while(true){

        print_menu();
        int optiune;
        cin >> optiune;
        cin.ignore(1000, '\n');

        switch (optiune) {

            case 1:
                UI_add(lista);
                break;
            case 2:
                UI_modificare(lista);
                break;
            case 3:
                UI_stergere(lista);
                break;
            case 4:
                afisare_lista(lista);
                break;
            case 5:
                UI_cautare_carte(lista);
                break;
            case 6:
                UI_filtrare(lista);
                break;
            case 7:
                UI_sortare(lista);
                break;
            case 8:
                UI_add_cos(lista, cos);
                break;
            case 9:
                UI_afisare_cos(cos);
                break;
            case 10:
                UI_goleste_cos(cos);
                break;
            case 11:
                UI_carti_random(lista, cos);
                break;
            case 12:
                UI_export_HTML(cos);
                break;
            case 13:
                UI_report(lista);
                break;
            case 14:
                UI_baga_carti(lista);
                break;
            case 15:
                UI_undo(lista);
                break;
            case 0:
                break;

            default:
                cout << "Optiune invalida!";

        }

        if (optiune == 0)
            break;

    }

}


int main() {

    Test::test_all();

    _CrtDumpMemoryLeaks();

    run();


    return 0;

}








