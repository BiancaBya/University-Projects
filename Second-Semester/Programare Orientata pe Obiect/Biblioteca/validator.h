#ifndef QT1_VALIDATOR_H
#define QT1_VALIDATOR_H
#include <string>
#include "exceptions.h"
#include "vector_dinamic.h"
#include "repository.h"

class Validator{

public:

    Validator() = default;

    static void validare_date(const std::string& titlu, const std::string& autor, const std::string& gen, int an){

        if (titlu.size() < 2)
            throw Dateinvalide();

        if (autor.size() < 2)
            throw Dateinvalide();

        if (gen.size() < 2)
            throw Dateinvalide();

        if (an < 1500)
            throw Dateinvalide();
    }

    static void validare_index(int index, int lungime){

        if (index < 0 or index >= lungime)
            throw IndexInvalid();

    }

    static void validare_carte(const std::string& titlu, const std::string& autor, const std::string& gen, int an, vector<Carte> lista){

        for (const auto & i : lista)
            if (i.get_titlu() == titlu and i.get_autor() == autor and i.get_gen() == gen and i.get_an() == an)
                throw CarteExistenta();

    }

    static void validare_carte_nu_exista(const std::string& titlu, const std::string& autor, const std::string& gen, int an, vector<Carte> lista){

        int ok = 0;
        for (const auto & i : lista)
            if (i.get_titlu() == titlu and i.get_autor() == autor and i.get_gen() == gen and i.get_an() == an)
                ok = 1;

        if(!ok)
            throw CarteaNuExista();
    }

    ~Validator() = default;

};




#endif //QT1_VALIDATOR_H


