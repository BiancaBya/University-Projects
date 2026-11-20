def cautare_binara_recursiv(element, lista, left, right):

    if left > right:
        return right
    mij = (left + right) // 2   # trebuie // ca sa faca impartirea intreaga
    if lista[mij] == element:
        return mij
    elif element < lista[mij]:
        return cautare_binara_recursiv(element, lista, left, mij - 1)
    else:
        return cautare_binara_recursiv(element, lista, mij + 1, right)



def cautare_binara_iterativ(element, lista):

    left = 0
    right = len(lista) - 1
    while left <= right:
        mij = (left + right) // 2   # trebuie // ca sa faca impartirea intreaga
        if element == lista[mij]:
            return mij
        elif element < lista[mij]:
            right = mij - 1
        else:
            left = mij + 1

    return right    #!!! daca se termina while-ul trebuie returnat right-ul




def cautare_succesiva(element, lista):
    """
    Cauta un element intr-o lista
    :param element: elementul cautat
    :param lista: lista in care se cauta
    :return: pozitia elementului sau -1 daca elementul nu se afla in lista
    """
    i = 0
    while i < len(lista) and element != lista[i]:
        i = i+1
    if i < len(lista):
        return i
    return -1



def cautare_secventiala(lst, value):
    """
    Cauta un element intr-o lista
    element = elementul cautat
    lista = lista in care este cautat
    :return: pozitia elementului din lista sau -1 daca acesta nu apare in lista
    """
    poz = -1
    for i in range(0, len(lst)):
        if lst[i] == value:
            poz = i
    return poz



