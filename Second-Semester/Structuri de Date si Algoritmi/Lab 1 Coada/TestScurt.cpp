#include "TestScurt.h"
#include <assert.h>
#include "Coada.h"
#include <exception>


void testAll() { //apelam fiecare functie sa vedem daca exista
    Coada c;
    assert(c.vida() == true);
    c.adauga(5);
    c.adauga(1);
    c.adauga(10);
    assert(c.vida() == false);
    assert(c.element() == 5);
    assert(c.sterge() == 5);
    assert(c.element() == 1);
    assert(c.sterge() == 1);
    assert(c.element() == 10);
    assert(c.sterge() == 10);
    assert(c.vida() == true);

    //test stergeMinim
    Coada c1;
    c.adauga(5);
    c.adauga(2);
    c.adauga(1);
    assert(c.stergeMinim() == 1);
    assert(c.stergeMinim() == 2);
    assert(c.stergeMinim() == 5);
    assert(c.vida() == true);

    try {
        c.stergeMinim();
        assert(false);
    }
    catch (exception&){
        assert(true);
    }

}




