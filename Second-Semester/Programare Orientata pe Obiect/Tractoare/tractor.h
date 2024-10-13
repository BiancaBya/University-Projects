#ifndef TRACTOR_TRACTOR_H
#define TRACTOR_TRACTOR_H
#include <string>
using namespace std;


class Tractor{

private:

    int id;
    string denumire;
    string tip;
    int nrRoti;

public:

    Tractor(int id, string den, string tip, int nr) : id(id), denumire(den), tip(tip), nrRoti(nr){};

    Tractor() = default;

    int get_id() const{
        return id;
    }

    string get_denumire() const{
        return denumire;
    }

    string get_tip() const{
        return tip;
    }

    int get_nrRoti() const{
        return nrRoti;
    }

    void set_id(int id_nou){
        id = id_nou;
    }

    void set_denumire(string den_noua){
        denumire = den_noua;
    }

    void set_tip(string tip_nou){
        tip = tip_nou;
    }

    void set_nrRoti(int nr){
        nrRoti = nr;
    }

};


#endif //TRACTOR_TRACTOR_H
