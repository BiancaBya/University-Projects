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



lista = [1,2,3,4,5,6,7,8,9,10]
el1 = 4
el2 = 11
print(cautare_binara_iterativ(el1, lista))
print(cautare_binara_recursiv(el1, lista, 0, len(lista) - 1))

print(cautare_binara_iterativ(el2, lista))
print(cautare_binara_recursiv(el2, lista, 0, len(lista) - 1))

