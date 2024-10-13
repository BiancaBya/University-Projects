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


list = [1,2,3,4,5,6,7,8,9]
element1 = 4
element2 = 100
print(cautare_succesiva(element1, list))
print(cautare_succesiva(element2, list))








