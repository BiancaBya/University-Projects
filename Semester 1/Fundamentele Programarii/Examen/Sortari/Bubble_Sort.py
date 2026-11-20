def bubble_sort(lista):

    ok = True
    while ok:
        ok = False
        for i in range(0, len(lista) - 1):   # se pune len(lista) - 1 ca lista[i+1] sa existe
            if lista[i] > lista[i+1]:
                lista[i], lista[i+1] = lista[i+1], lista[i]
                ok = True
    return lista


lst = [10,5,3,2,1,4,5,7,6,10,9,8,3,6,7]
print(bubble_sort(lst))




