#ifndef EX_MASINA_H
#define EX_MASINA_H
#include <string>
using namespace std;

class Masina{

private:

    string inmatriculare; // unic
    string denumire;
    int an;
    string tip;

public:

    Masina(string inm, string den, int an, string t) : inmatriculare(inm), denumire(den), an(an), tip(t) {};

    Masina() = default;

    string get_inmatriculare() const{
        return inmatriculare;
    }

    string get_denumire() const{
        return denumire;
    }

    int get_an() const{
        return an;
    }

    string get_tip() const{
        return tip;
    }

    void set_an(int anul){
        an = anul;
    }


};


#endif //EX_MASINA_H
