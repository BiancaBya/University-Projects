#ifndef QT1_CARTE_H
#define QT1_CARTE_H

#include <string>
using namespace std;

class Carte
{


private:

    string titlu;
    string autor;
    string gen;
    int an;

public:

    Carte(const string& titlu, const string& autor, const string& gen, const int& an); // Carte(string titlu, string autor, string gen, int an) : titlu{titlu}, model_tabel{model_tabel}...;
    /*
        constructorul obiectului carte
        titlu -> titlul cartii care trebuie adaugata, tip = string
        autor -> autorul cartii care trebuie adaugata, tip = string
        gen -> genul cartii care trebuie adaugata, tip = string
        an -> anul cartii de adaugat, tip = int
        return -> None
    */
    Carte() = default;

    Carte(const Carte& other);

    string get_titlu() const;
    /*
        Returneaza titlul cartii, de tip string
    */

    string get_autor() const;
    /*
        Returneaza autorul cartii, de tip string
    */

    string get_gen() const;
    /*
        Returneaza genul cartii, de tip string
    */

    int get_an() const;
    /*
        Returneaza anul cartii, de tip int
    */

    void set_titlu(const string& titlul);
    /*
        Schimba titlul cartii
    */

    void set_autor(const string& autorul);
    /*
        Schimba autorul cartii
    */

    void set_gen(const string& genul);
    /*
        Schimba genul cartii
    */

    void set_an(int anul);
    /*
        Schimba anul cartii
    */


};



#endif //QT1_CARTE_H

