#include "TestScurt.h"
#include <assert.h>
#include "Multime.h"
#include "IteratorMultime.h"

void testAll() { //apelam fiecare functie sa vedem daca exista
	Multime m;
	assert(m.vida() == true);
	assert(m.dim() == 0); //adaug niste elemente
	assert(m.adauga(5)==true);
	assert(m.adauga(1)==true);
	assert(m.adauga(10)==true);
	assert(m.adauga(7)==true);
	assert(m.adauga(1)==false);
	assert(m.adauga(10)==false);
	assert(m.adauga(-3)==true);
	assert(m.dim() == 5);
	assert(m.cauta(10) == true);
	assert(m.cauta(16) == false);
	assert(m.sterge(1) == true);
	assert(m.sterge(6) == false);
	assert(m.dim() == 4);

    Multime a;
    Multime b;

    a.adauga(4);
    a.adauga(1);
    a.adauga(6);
    a.adauga(3);
    a.adauga(5);

    b.adauga(1);
    b.adauga(2);
    b.adauga(3);

    a.intersectie(b);
    assert(a.dim() == 2);

    a.adauga(4);
    a.sterge(1);
    a.sterge(3);
    a.intersectie(b);
    assert(a.dim() == 0);


	IteratorMultime im = m.iterator();
	im.prim();
	int s = 0;
	while (im.valid()) {
	TElem e = im.element();
		s += e;
		im.urmator();
	}
	assert(s == 19);

}


