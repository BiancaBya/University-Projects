#ifndef MELODII1_MELODIE_H
#define MELODII1_MELODIE_H
#include <string>
using namespace std;


class Melodie{

private:

    int id;
    string titlu;
    string artist;
    int rank;

public:

    Melodie(int id, string titlu, string artist, int rank) : id(id), titlu(titlu), artist(artist), rank(rank) {};

    Melodie() = default;

    int get_id() const{
        return id;
    }

    string get_titlu() const{
        return titlu;
    }

    string get_artist() const{
        return artist;
    }

    int get_rank() const{
        return rank;
    }

    void set_id(int id1){
        id = id1;
    }

    void set_titlu(string titlu1){
        titlu = titlu1;
    }

    void set_artist(string artist1){
        artist = artist1;
    }

    void set_rank(int rank1){
        rank = rank1;
    }


};


#endif //MELODII1_MELODIE_H
