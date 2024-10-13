def quick_sort(lista : list):

    if len(lista) <= 1:    # !!! lungimea poate fi 1 sau 0 si se returneaza direct lista
        return lista

    pivot = lista.pop()
    lesser = quick_sort([x for x in lista if x <= pivot])
    greater = quick_sort([x for x in lista if x >= pivot])
    return lesser + [pivot] + greater




lst = [10,5,3,2,1,4,5,7,6,10,9,8,3,6,7]
sortat = quick_sort(lst)
print(sortat)


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

    if poz-1 > left:
        quick(lista, st, poz-1)
    if poz+1 < dr:
        quick(lista, poz+1, right)





