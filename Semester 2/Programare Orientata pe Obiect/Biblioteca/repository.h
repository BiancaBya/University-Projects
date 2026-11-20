#ifndef QT1_REPOSITORY_H
#define QT1_REPOSITORY_H

#include "carte.h"
#include "exceptions.h"
#include <string>
#include <vector>
#include <unordered_map>
#include <random>
#include <algorithm>

using namespace std;

class RepoAbstract{

public:

    virtual void add_carte(const Carte& carte) = 0;
    virtual void modificare_carte(const Carte& carte_cautata, const Carte& carte_modificat) = 0;
    virtual void sterge_carte(const Carte& carte) = 0;
    virtual const Carte* find_carte(const string& titlu, const string& autor, const string& gen, int an) const = 0;
    virtual vector<Carte>& get_all() = 0;
    virtual int get_lungime() const = 0;

    virtual ~RepoAbstract() = default;
};



class ListaCarti : public RepoAbstract
{

private:

    vector<Carte> lista_carti;

public:

    ListaCarti(const ListaCarti& ot) = default;

    ListaCarti() = default;
    /*
        constructorul listei
        lista_carti -> lista de carti de tip vector<Carte>
    */

    int get_lungime() const override;
    /*
        Returneaza lungimea listei de carti
    */

    void add_carte(const Carte& carte) override;
    /*
        adauga o carte la lista de carti
        carte -> cartea adaugata, de tip Carte
    */

    void modificare_carte(const Carte& carte_stearsa, const Carte& carte_noua) override;
    /*
        modifica cartea de la pozitia poz cu cartea primita ca parametru
        poz -> pozitia cartii de modificat
        carte -> cartea de adaugat
    */

    void sterge_carte(const Carte& carte) override;
    /*
        sterge cartea de la pozitia poz din lista de carti
        poz -> pozitia cartii de sters
    */

    [[nodiscard]] const Carte* find_carte(const string& titlu, const string& autor, const string& gen, int an) const override;
    /*
        Functia de gasire a unei carti dupa titlu, autor, gen si an
    */


    vector<Carte>& get_all() override {
        return lista_carti;
    }

};


class RepoInFile : public ListaCarti{

private:

    string file_name;
    void ReadFromFile();
    void WriteToFile();

public:

    explicit RepoInFile(string filename) : ListaCarti(), file_name(filename){
        ReadFromFile();
    }

    void add_carte(const Carte& carte) override{

        ListaCarti::add_carte(carte);
        WriteToFile();

    }

    void modificare_carte(const Carte& carte_stearsa, const Carte& carte_noua) override{

        ListaCarti::modificare_carte(carte_stearsa, carte_noua);
        WriteToFile();

    }

    void sterge_carte(const Carte& carte) override{

        ListaCarti::sterge_carte(carte);
        WriteToFile();

    }

};





class RepoDictionar : public RepoAbstract{

private:

    unordered_map<string, Carte> dictionar; // cheia este titlul cartii si valoarea e cartea
    double probabilitate;

public:

    RepoDictionar() {

        probabilitate = 0.5;

    };

    void set_probabilitate(double prob){

        probabilitate = prob;
    }

    void add_carte(const Carte& carte) override{

        random_prob();
        dictionar.insert(make_pair(carte.get_titlu(), carte));
    }

    void modificare_carte(const Carte& carte_cautata, const Carte& carte_modificat) override{

        random_prob();
        dictionar.erase(carte_cautata.get_titlu());
        dictionar.insert(make_pair(carte_modificat.get_titlu(), carte_modificat));
    }

    void sterge_carte(const Carte& carte) override{

        random_prob();
        dictionar.erase(carte.get_titlu());
    }

    int get_lungime() const override {
        return dictionar.size();
    }

    const Carte* find_carte(const string& titlu, const string& autor, const string& gen, int an) const override{

        random_prob();
        for (const auto& pereche : dictionar)
            if (pereche.second.get_titlu() == titlu and pereche.second.get_autor() == autor and pereche.second.get_gen() == gen and pereche.second.get_an() == an)
                return &pereche.second;

        return nullptr;
    }

    vector<Carte> carti;

    vector<Carte>& get_all() override{

        carti.clear();

        for (const auto& pereche : dictionar)
            carti.push_back(pereche.second);

        return carti;
    }

    void random_prob() const{

        random_device rd;
        mt19937 gen(rd());
        uniform_real_distribution<double> dis(0.0, 1.0);
        double prob = dis(gen);
        if (prob > probabilitate){
            throw Probabilitate();
        }

    }

};

#endif //QT1_REPOSITORY_H


