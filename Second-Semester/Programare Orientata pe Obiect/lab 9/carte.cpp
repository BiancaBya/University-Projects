#include "carte.h"
#include <string>
#include <iostream>
using namespace std;


Carte::Carte(const string& titlu, const string& autor, const string& gen, const int& an) {

    this->titlu = titlu;
    this->autor = autor;
    this->gen = gen;
    this->an = an;

}

Carte::Carte(const Carte& other) {
    cout << "Copie\n";
    *this = other;
}

string Carte::get_titlu() const{
    return this->titlu;
}

string Carte::get_autor() const{
    return this->autor;
}

string Carte::get_gen() const{
    return this->gen;
}

int Carte::get_an() const {
    return this->an;
}

void Carte::set_titlu(const string& titlul){
    this->titlu = titlul;
}


void Carte::set_autor(const string& autorul){
    this->autor = autorul;
}

void Carte::set_gen(const string& genul){
    this->gen = genul;
}

void Carte::set_an(int anul){
    this->an = anul;
}



