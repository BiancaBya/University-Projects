from manage_student import *
from Functionalitati import *

def run():

#optiunea 1


    lista_testat = [{'scor': 10, 'index': 1}, {'scor': 20, 'index': 2}, {'scor': 3, 'index': 3}, {'scor': 4, 'index': 4},{'scor': 5, 'index': 5}, {'scor': 50, 'index': 6}, {'scor': 60, 'index': 7}, {'scor': 70, 'index': 8},{'scor': 8, 'index': 9}]
    participant = creare_participant(9,10)
    adaugare_student(lista_testat,participant)
    participant_adaugat = lista_testat.pop()
    assert (get_scor(participant_adaugat) == 9)
    assert (get_index(participant_adaugat) == 10)

#optiunea 2

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4}]
    lista_testat = sterge_scor_participant(2, lista_testat)
    scor = get_scor(lista_testat[1])
    index = get_index(lista_testat[1])
    assert (scor == 3)
    assert (index == 2)

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    lista_testat = sterge_scor_interval(2, 5, lista_testat)
    scor = get_scor(lista_testat[1])
    index = get_index(lista_testat[1])
    assert (scor == 50)
    assert (index == 2)

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    lista_testat = inlocuieste_scor_participant(1, 20, lista_testat)
    scor = get_scor(lista_testat[0])
    index = get_index(lista_testat[0])
    assert (scor == 20)
    assert (index == 1)

# optiunea 3

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':70,'index':3},{'scor':60,'index':4},{'scor':51,'index':5}]
    lista_testat = afisare_participanti_scor_mic(50, lista_testat)
    scor1 = get_scor(lista_testat[0])
    scor2 = get_scor(lista_testat[1])
    index1 = get_index(lista_testat[0])
    index2 = get_index(lista_testat[1])
    assert [scor1,index1,scor2,index2] == [10,1,20,2]

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3}]
    lista_testat = afisare_participanti_ordonati(lista_testat)
    scor1 = get_scor(lista_testat[0])
    index1 = get_index(lista_testat[0])
    scor2 = get_scor(lista_testat[1])
    index2 = get_index(lista_testat[1])
    scor3 = get_scor(lista_testat[2])
    index3 = get_index(lista_testat[2])
    assert [scor1,scor2,scor3,index1,index2,index3] == [3,10,20,3,1,2]

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    lista_testat = afisare_participanti_scor_mai_mare(50, lista_testat)
    scor1 = get_scor(lista_testat[0])
    index1 = get_index(lista_testat[0])
    scor2 = get_scor(lista_testat[1])
    index2 = get_index(lista_testat[1])
    assert [scor1,scor2,index1,index2] == [60,70,7,8]


# optiunea 4

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    ok = medie_scoruri_interval(1, 3, lista_testat)
    assert (ok == 9.0)

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    ok = scor_minim_interval(1, 5, lista_testat)
    assert (ok == 3)

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    ok = participanti_cu_scor_multiplu(1, 5, lista_testat)
    scor1 = get_scor(ok[0])
    index1 = get_index(ok[0])
    scor2 = get_scor(ok[1])
    index2 = get_index(ok[1])
    assert [scor1,scor2,index1,index2] == [20,50,2,6]

#optiunea 5

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5}]
    lista_testat = filtrare_scor_multiplu(10, lista_testat)
    scor1 = get_scor(lista_testat[0])
    index1 = get_index(lista_testat[0])
    scor2 = get_scor(lista_testat[1])
    index2 = get_index(lista_testat[1])
    assert [scor1,index1,scor2,index2] == [10,1,20,2]

    lista_testat = [{'scor':10,'index':1},{'scor':20,'index':2},{'scor':3,'index':3},{'scor':4,'index':4},{'scor':5,'index':5},{'scor':50,'index':6},{'scor':60,'index':7},{'scor':70,'index':8},{'scor':8,'index':9},{'scor':9,'index':10}]
    lista_testat = filtrare_scor_mai_mic(60, lista_testat)
    scor1 = get_scor(lista_testat[0])
    index1 = get_index(lista_testat[0])
    scor2 = get_scor(lista_testat[1])
    index2 = get_index(lista_testat[1])
    assert [scor1,index1,scor2,index2] == [60,1,70,2]


