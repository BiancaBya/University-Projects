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



list = [1,2,3,4,5,6,7,8,9]
element1 = 4
element2 = 100
print(cautare_secventiala(element1, list))
print(cautare_secventiala(element2, list))













