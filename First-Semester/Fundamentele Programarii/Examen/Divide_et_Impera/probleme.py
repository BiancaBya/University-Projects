def par(lista):
    if len(lista) == 0:
        return 0
    if len(lista) == 1:
        return 1*lista[0]
    mid = len(lista) // 2
    stanga = par(lista[:mid:2])   # ca sa mearga din doi in doi
    dreapta = par(lista[mid: :2])
    return stanga * dreapta


#lista = [1,0,1,0,1,0,1,0,1]
#print(par(lista))




def nr_de_nr_negative(lista):

    if len(lista) == 0:
        return 0
    if len(lista) == 1:
        if lista[0] < 0:
            return 1
        else:
            return 0

    mij = len(lista) // 2
    stanga = nr_de_nr_negative(lista[ : mij])
    dreapta = nr_de_nr_negative(lista[mij : ])
    return stanga + dreapta


#lista = [1,-2,-3,-4,-5,6,7,-8,9,10,-11]
#print(nr_de_nr_negative(lista))





def dublare_elemente(lista):

    if len(lista) == 0:
        return

    if len(lista) == 1:
        return [lista[0] * 2]   # se pot pune [] ca sa se rezurneze o lista cu acel element

    mij = len(lista) // 2
    stanga = dublare_elemente(lista[:mij])
    dreapta = dublare_elemente(lista[mij:])
    return stanga + dreapta


#lista = [1,2,3,4,5]
#print(dublare_elemente(lista))


def sterge_nr_pare(lista):

    if len(lista) == 0:
        return
    elif len(lista) == 1:
        if lista[0]%2:
            return [lista[0]]
        else:
            return []

    mij = len(lista) // 2
    stanga = sterge_nr_pare(lista[:mij])
    dreapta = sterge_nr_pare(lista[mij:])
    return stanga+dreapta


#list = [1,2,3,4,5,6,7,8]
#print(sterge_nr_pare(list))




def inversare_lista(lista):

    if len(lista) == 0:
        return
    elif len(lista) == 1:
        return [lista[0]]

    mij = len(lista) // 2
    stanga = inversare_lista(lista[:mij])
    dreapta = inversare_lista(lista[mij:])
    return dreapta+stanga


#lista = [1,2,3,4,5,6,7,8,9,10,11,12]
#print(inversare_lista(lista))




def produs_nr_poz_pare(lista):

    if len(lista) == 0:
        return
    elif len(lista) == 1:
        return lista[0]

    mij = len(lista) // 2
    stanga = produs_nr_poz_pare(lista[:mij:2])
    dreapta = produs_nr_poz_pare(lista[mij::2])
    return stanga * dreapta



def maxim(lista):

    if len(lista) == 0:
        return
    elif len(lista) == 1:
        return lista[0]

    mij = len(lista) // 2
    stanga = maxim(lista[:mij])
    dreapta = maxim((lista[mij:]))
    return max(stanga,dreapta)


#lista = [2,8,4,1,4,5,6,7]
#print(maxim(lista))



def contine_par(lista):

    if len(lista) == 0:
        return
    elif len(lista) == 1:
        return not lista[0] % 2

    mij = len(lista) // 2
    stanga = contine_par(lista[:mij])
    dreapta = contine_par((lista[mij:]))
    return stanga or dreapta


#lista = [1,3,2,5]
#print(contine_par(lista))






