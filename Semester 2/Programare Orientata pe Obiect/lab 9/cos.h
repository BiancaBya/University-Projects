#ifndef LAB9_COS_H
#define LAB9_COS_H
#include "carte.h"
#include <vector>
#include <random>
#include <chrono>
#include <fstream>

class Cos{

private:

    vector<Carte> cos;

public:

    Cos()=default;

    const vector<Carte>& get_cos(){
        return this->cos;
    }



    void adaugare_in_cos(const Carte& carte){

        this->cos.push_back(carte);

    }

    void golire_cos(){

        this->cos.clear();

    }

    void random_carti(vector<Carte> lista_carti, int nr) {
        shuffle(lista_carti.begin(), lista_carti.end(), std::default_random_engine(std::random_device{}())); //amesteca vectorul v
        while (cos.size() < nr && lista_carti.size() > 0) {
            cos.push_back(lista_carti.back());
            lista_carti.pop_back();
        }
    }

    void export_html(const string& file_name){

        std::ofstream file(file_name);
        file << "<html>\n";
        file << "<head><title>Export cos</title></head>\n";
        file << "<style>\n";
        file << "body {";
        file << "    background-color: #c8fab8;";
        file << "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;";
        file << "}";
        file << "h1 {";
        file << "    color: #058322;";
        file << "}";
        file << "table {";
        file << "    border-collapse: collapse;";
        file << "    width: 100%;";
        file << "    border-radius: 8px;";
        file << "}";
        file << "th, td {";
        file << "    padding: 12px;";
        file << "    text-align: left;";
        file << "}";
        file << "th {";
        file << "    background-color: #30ef88;";
        file << "    color: #08803b;";
        file << "}";
        file << "tr:nth-child(even) {";
        file << "    background-color: #30ef88;";
        file << "}";
        file << "tr:hover {";
        file << "    background-color: #30ef88;";
        file << "}";
        file << "</style>\n";
        file << "<body>\n";
        file << "<h1>Shopping cart:</h1>\n";
        file << "<table>\n";
        file << "<tr><th>Titlu</th><th>Autor</th><th>Gen</th><th>An</th><tr>\n";

        const vector<Carte>& cos_carti = get_cos();

        for(const Carte& carte : cos_carti){
            file << "<tr><td> " << carte.get_titlu() << " </td><td> " << carte.get_autor() << " </td><td> " << carte.get_gen() << " </td><td> " << carte.get_an() <<" </td><tr>\n";
        }

        file << "</table>\n";
        file << "</body>\n";
        file << "<html>\n";

        file.close();

    }

};
#endif //LAB9_COS_H
