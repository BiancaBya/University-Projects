#pragma once

#define NULL_TELEM -1
typedef int TElem;

class IteratorColectie;


class Colectie
{
	friend class IteratorColectie;

private:

    int capacitate;
    TElem* elemente;
    int* urmator; // vectorul de legaturi care contine indicele urmatorului element
    int* frecvente; // vectorul care memoreaza frecventele fiecarui nod

    int prim; // indicele primului nod al listei, lsi = vida daca prim = -1
    int prim_liber; // primul element a listei inlantuite a spatiului liber

    int aloca(); //sterge primul element al listei spatiului liber
    void dealoca(int i); // se adauga la inceputul listei spatiului liber o valoare
    void initlistaspatiuliber(); // creeaza o lsi de lungime egala cu capacitatea, toate pozitiile sunt libere
    int creeazanod(TElem el); // creeaza un nod in LSI unde este memorat elementul el

public:
		//constructorul implicit
		Colectie();

		//adauga un element in colectie
		void adauga(TElem e);

		//sterge o aparitie a unui element din colectie
		//returneaza adevarat daca s-a putut sterge
		bool sterge(TElem e);

		//verifica daca un element se afla in colectie
		bool cauta(TElem elem) const;

		//returneaza numar de aparitii ale unui element in colectie
		int nrAparitii(TElem elem) const;


		//intoarce numarul de elemente din colectie;
		int dim() const;

		//verifica daca colectia e vida;
		bool vida() const;

		//returneaza un iterator pe colectie
		IteratorColectie iterator() const;

		// destructorul colectiei
		~Colectie();

    void resize();
};

