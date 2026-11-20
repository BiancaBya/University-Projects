#include "service.h"
#include "validator.h"
#include <cstring>
#include <random>
#include <fstream>
#include <algorithm>
#include <map>


void CarteService::adaugare(const Carte& carte) {

    Validator::validare_date(carte.get_titlu(), carte.get_autor(), carte.get_gen(), carte.get_an());
    Validator::validare_carte(carte.get_titlu(), carte.get_autor(), carte.get_gen(), carte.get_an(), get_all());
    lista_service.add_carte(carte);

    undo.push_back(make_unique<AdaugaUndo>(lista_service, carte));

}

void CarteService::modificare(const Carte& carte_stearsa, const Carte& carte_noua) {

    Validator::validare_date(carte_noua.get_titlu(), carte_noua.get_autor(), carte_noua.get_gen(), carte_noua.get_an());
    Validator::validare_carte_nu_exista(carte_stearsa.get_titlu(), carte_stearsa.get_autor(), carte_stearsa.get_gen(), carte_stearsa.get_an(), get_all());
    Validator::validare_carte(carte_noua.get_titlu(), carte_noua.get_autor(), carte_noua.get_gen(), carte_noua.get_an(), get_all());

    lista_service.modificare_carte(carte_stearsa, carte_noua);

    undo.push_back(make_unique<ModificareUndo>(lista_service, carte_stearsa, carte_noua));

}

void CarteService::stergere(const Carte& carte) {

    Validator::validare_carte_nu_exista(carte.get_titlu(), carte.get_autor(), carte.get_gen(), carte.get_an(), get_all());
    lista_service.sterge_carte(carte);

    undo.push_back(make_unique<StergereUndo>(lista_service, carte));

}


int CarteService::get_lung() const{

    return lista_service.get_lungime();

}


const Carte* CarteService::gasire_carte(const string& titlu, const string& autor, const string& gen, int an) const{

    return lista_service.find_carte(titlu, autor, gen, an);

}



void CarteService::filtrare_titlu(const string& titlu, vector<Carte>& copie, int& lungime) const{

    const vector<Carte>& elemente = lista_service.get_all();
    copie.resize(elemente.size());

    auto it = std::copy_if(elemente.begin(), elemente.end(), copie.begin(),[titlu](const Carte& carte){

        return (carte.get_titlu() == titlu);

    });

    lungime = std::distance(copie.begin(),it);
    copie.resize(lungime); // ca sa aiba lungimea reala

}


void CarteService::filtrare_an(int an, vector<Carte>& copie, int& lungime) const{

    const vector<Carte>& elemente = lista_service.get_all();
    copie.resize(elemente.size());

    auto it = std::copy_if(elemente.begin(), elemente.end(), copie.begin(),[an](const Carte& carte){

        return (carte.get_an() == an);

    });

    lungime = std::distance(copie.begin(),it);
    copie.resize(lungime); // ca sa aiba lungimea reala

}



int CarteService::cmp_titlu(const Carte& x, const Carte& y) {

    return x.get_titlu() <= y.get_titlu();

}

int CarteService::cmp_autor(const Carte& x, const Carte& y) {

    return x.get_autor() <= y.get_autor();

}

int CarteService::cmp_gen(const Carte& x, const Carte& y) {

    return x.get_gen() < y.get_gen();

}

int CarteService::cmp_an(const Carte& x, const Carte& y) {

    if (x.get_an() == y.get_an())
        return cmp_gen(x,y);

    return x.get_an() < y.get_an();
}


void CarteService::sortare(CompareFct cmpfct, vector<Carte>& copie, int& lungime) const{

    const vector<Carte>& elemente = lista_service.get_all();

    for (const auto& carte : elemente){
        copie.push_back(carte);
        lungime++;
    }

    std::sort(copie.begin(),copie.end(),cmpfct);
}


Carte CarteService::find_carte_titlu(const string& titlu){

    const vector<Carte> carti = lista_service.get_all();

    auto it = std::find_if(carti.begin(), carti.end(),[titlu](const Carte& carte){

        return titlu == carte.get_titlu();

    });

    if (it != carti.end()) {
        Carte carte = *it;
        return carte;
    }

    Carte carte = Carte(" "," "," ",0);
    return carte;

}



map <string, Autorcount> CarteService::report() const{

    map <string, Autorcount> rep;

    for (auto& carte : lista_service.get_all()){

        auto it = rep.find(carte.get_autor());
        if (it != rep.end())
            it->second.Incrementcout();
        else{
            rep.insert(std::make_pair(carte.get_autor(), Autorcount()));
            rep.emplace(carte.get_autor(), Autorcount());
        }

    }

    return rep;

}


int CarteService::Undo() {

    if (undo.empty())
        return -1;

    undo.back()->undo();
    undo.pop_back();
    return 0;

}



/*
void generare_carte_random(Carte& carte){

    string caractere = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    random_device rd;
    mt19937 gen(rd()); //  generatorul de numere folosit de dis
    uniform_int_distribution<> dis(0, caractere.size()-1); // genereaza numere aleatoare
    uniform_int_distribution<> numere(1600, 2024);

    string titlu, autor, genul;
    int an;

    for (int i=0 ; i < 10; i++)
        titlu += caractere[dis(gen)];
    for (int i=0; i < 5; i++)
        autor += caractere[dis(gen)];
    autor += " ";
    for (int i=0; i < 5; i++)
        autor += caractere[dis(gen)];
    for (int i=0; i < 5; i++)
        genul += caractere[dis(gen)];
    an = numere(gen);

    carte.set_titlu(titlu);
    carte.set_autor(autor);
    carte.set_gen(genul);
    carte.set_an(an);

}
*/




