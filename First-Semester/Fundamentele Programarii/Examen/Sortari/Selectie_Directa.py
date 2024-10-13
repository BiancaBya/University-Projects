def selectie_directa(lista):
    for i in range(0, len(lista) - 1):
        for j in range(i+1, len(lista)):
            if lista[i] > lista[j]:
                lista[i], lista[j] = lista[j], lista[i]
    return lista



lst = [4,5,6,3,2,1,6,9,7,8,10]
print(selectie_directa(lst))



