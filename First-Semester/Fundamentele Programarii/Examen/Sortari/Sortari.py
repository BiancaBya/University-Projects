def quick_sort(lista : list):

    if len(lista) <= 1:
        return lista

    pivot = lista.pop()
    lesser = quick_sort([x for x in lista if x <= pivot])
    greater = quick_sort([x for x in lista if x >= pivot])
    return lesser + [pivot] + greater


def partition(lista, st, dr):
    pivot = lista[st]
    i = st
    j = dr

    while i != j:
        while lista[j] >= pivot and i < j:
            j -= 1
        lista[i] = lista[j]
        while lista[i] <= pivot and i < j:
            i += 1
        lista[j] = lista[i]

    lista[i] = pivot
    return i  # pozitia finala a pivotului


def quick(lista, st, dr):
    if st > dr:
        return
    poz = partition(lista, st, dr)

    if poz - 1 > left:
        quick(lista, st, poz - 1)
    if poz + 1 < dr:
        quick(lista, poz + 1, right)




def intercalsare(lst1, lst2):
    i = 0
    j = 0
    lista = []
    while i < len(lst1) and j < len(lst2):
        if lst1[i] <= lst2[j]:
            lista.append(lst1[i])
            i += 1
        else:
            lista.append(lst2[j])
            j += 1
    lista.extend(lst1[i : ])
    lista.extend(lst2[j : ])
    return lista


def merge_sort(lista):
    if len(lista) <= 1:
        return lista
    mij = len(lista) // 2
    low_part = merge_sort(lista[ : mij])
    high_part = merge_sort(lista[mij : ])
    return intercalsare(low_part, high_part)





def bubble_sort(lista):

    ok = True
    while ok:
        ok = False
        for i in range(0, len(lista) - 1):   # se pune len(lista) - 1 ca lista[i+1] sa existe
            if lista[i] > lista[i+1]:
                lista[i], lista[i+1] = lista[i+1], lista[i]
                ok = True
    return lista




def insertion_sort(lista):
    """
    la fiecare pas lista din spatele elementului i este sortata crescator
    daca elementul curent este mai mic decat cel din spatele sau atunci elementele sunt
    permutate la dreapta pana cand elementul curent salvat in aux ajunge sa fie mai mare sau egal
    decat elementul din spatele sau si atunci este plasat acolo, deoarece acela este locul sau bun
    """
    for i in range(0, len(lista)):
        aux = lista[i]    # elementul lista[i] este pus la locul sau corect
        index = i-1       # se compara cu toate elementele din spatele sau
        while index >=0 and aux < lista[index]:
            lista[index + 1] = lista[index]
            index -= 1      # trecem la urmatorul element din spatele lui aux
        lista[index + 1] = aux  # il punem pe aux la locul potrivit
                # facem index + 1 deoarece cand s-a iesit din while index era scazut cu 1 fata de cat trebuia sa fie

    return lista




def selectie_directa(lista):
    for i in range(0, len(lista) - 1):
        for j in range(i+1, len(lista)):
            if lista[i] > lista[j]:
                lista[i], lista[j] = lista[j], lista[i]
    return lista



def selection_sort(lista):
    """
    ia fiecare pozitie din sir si pune pe acea pozitie elementul care va ocupa acea pozitie in final
    adica cel mai mic element din intervalul [i, n]
    """
    for i in range(0, len(lista) -  1):
        index = i
        for j in range(i, len(lista)):
            if lista[j] < lista[index]:
                index = j
        if index > i:
            lista[i], lista[index] = lista[index], lista[i]

    return lista




