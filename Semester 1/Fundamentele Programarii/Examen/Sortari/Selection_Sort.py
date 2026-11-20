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



lst = [3,4,5,1,2,6,7,9,10,8]
print(selection_sort(lst))


