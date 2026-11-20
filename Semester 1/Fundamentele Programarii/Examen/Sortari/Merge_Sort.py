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




lst = [10,5,3,2,1,4,5,7,6,10,9,8,3,6,7]
print(merge_sort(lst))


