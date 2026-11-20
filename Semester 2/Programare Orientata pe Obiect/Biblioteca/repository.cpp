#include "repository.h"
#include <algorithm>
#include <fstream>

void ListaCarti::add_carte(const Carte& carte) {

    lista_carti.push_back(carte);

}

void ListaCarti::sterge_carte(const Carte& carte){

    auto it = std::remove_if(lista_carti.begin(), lista_carti.end(), [carte](const Carte& carte_comparata){
        return carte.get_titlu() == carte_comparata.get_titlu() and carte.get_autor() == carte_comparata.get_autor() and carte.get_gen() == carte_comparata.get_gen() and carte.get_an() == carte_comparata.get_an();
    });

    lista_carti.erase(it);

}

void ListaCarti::modificare_carte(const Carte& carte_stearsa, const Carte& carte_noua) {

    for (auto& carte : ListaCarti::get_all()){
        if (carte.get_titlu() == carte_stearsa.get_titlu() and carte.get_autor() == carte_stearsa.get_autor() and carte.get_gen() == carte_stearsa.get_gen() and carte.get_an() == carte_stearsa.get_an()){
            carte.set_titlu(carte_noua.get_titlu());
            carte.set_autor(carte_noua.get_autor());
            carte.set_gen(carte_noua.get_gen());
            carte.set_an(carte_noua.get_an());
        }
    }

}


int ListaCarti::get_lungime() const {
    return lista_carti.size();
}


const Carte* ListaCarti::find_carte(const string& titlu, const string& autor, const string& gen, int an) const{

    auto pred = [titlu,autor,gen,an](const Carte& carte){
        return carte.get_titlu() == titlu and carte.get_autor() == autor and carte.get_gen() == gen and carte.get_an() == an;
    };

    auto iterator = std::find_if(lista_carti.begin(), lista_carti.end(), pred);
    if (iterator != lista_carti.end())
        return &(*iterator);

    else{
        return nullptr;
    }
}


void RepoInFile::ReadFromFile() {

    ifstream file(file_name);

    while (!file.eof()){

        string titlu, autor, gen;
        int an;

        file >> titlu >> autor >> gen >> an;

        if (titlu.empty() or autor.empty() or gen.empty())
            break;

        Carte carte(titlu, autor, gen, an);
        ListaCarti::add_carte(carte);

    }

    file.close();

}


void RepoInFile::WriteToFile() {

    ofstream file(file_name);
    vector<Carte> lista = ListaCarti::get_all();

    for (auto& carte : lista){

        file << carte.get_titlu().c_str() << '\n';
        file << carte.get_autor().c_str() << '\n';
        file << carte.get_gen().c_str() << '\n';
        file << carte.get_an() << '\n';

    }

    file.close();

}





