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



lst = [3,2,1,4,5,6,8,7,9,10,4,6,8]
print(insertion_sort(lst))

