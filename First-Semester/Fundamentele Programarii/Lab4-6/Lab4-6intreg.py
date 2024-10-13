def print_menu():
    print("1.Adauga un scor la un participant")
    print("2.Modificare scor")
    print("3.Tipareste lista de participanti")
    print("4.Operatii pe un subset de participanti")
    print("5.Filtrare")
    print("6.Undo")
    print("7.Break")

def creare_participant(sum,numar) -> dict:
    '''
    Funcita creaza un nou participant
    :param sum: scorul la cele 10 materii
    :type sum: int
    :param numar: indexul participantului
    :type numar:int
    :return: participantul creat
    :rtype:dict
    '''
    return {'scor' : sum, 'index' : numar}

def adaugare_student(student_list, student):
    '''
    Functia adauga in lista de participanti noul participant creat
    :param student_list: lista de participanti
    :param student: participantul care este adaugat in lista
    :return: lista actualizata de participanti
    '''
    student_list.append(student)

def get_scor(student):
    return student['scor']

def get_index(student):
    return student['index']

def set_scor(student,scor):
    student['scor'] = scor

def set_index(student,index):
    student['index'] = index

def sterge_scor_participant(indice, lista):
    '''
    :param indice: indicele elementului care trebuie eliminat din lista
    :type indice: int
    :param lista: lista de dictionare in care sunt stocati participantii
    :type lista: list
    :return: lista modificata cu participantul dorit sters
    '''
    lista.pop(indice-1)
    for index in range (indice-1,len(lista)):
        lista[index].update({'index' : index + 1})
    #for-ul actualizeaza indicii participantilor de dupa cel care a fost sters
    return lista

def sterge_scor_interval(indice1, indice2, lista):
    '''
    :param indice1: indicele de la inceputul intervalului
    :type indice1: int
    :param indice2: indicele de la sfarsitul intervalului
    :type indice2: int
    :param lista: lista de dictionare unde sunt stocati participantii
    :return: lista modificata
    '''
    del lista[indice1 - 1 : indice2]
    for index in range(indice1 - 1, len(lista)):
        lista[index].update({'index' : index + 1})
    #for-ul actualizeaza indicii participantilor de dupa cei care au fost stersi
    return lista

def inlocuieste_scor_participant(indice, scorul, lista):
    '''
    :param indice: indicele participantului la care se inlocuieste scorul
    :param scorul: scorul nou
    :param lista: lista de participanti
    :return: lista modifiacta
    '''
    lista[indice-1].update({'scor':scorul})
    return lista


def optiunea2(list):
    '''
    Functia primeste ca parametru lista de dictionare unde sunt stocati participantii si returneaza lista modificata
    :param list: lista de participanti
    :return: lista modificata in functie de optiunea aleasa
    '''
    print("'A' pentru Șterge scorul pentru un participant dat.")
    print("'B' pentru Șterge scorul pentru un interval de participanți.")
    print("'C' pentru Înlocuiește scorul de la un participant.")
    optiune = input('>')
    optiune = optiune.upper()
    if optiune < 'A' or optiune > 'C':
        print("Optiune invalida")
    elif optiune == 'A':
        index = int(input("Participantul: "))
        list = sterge_scor_participant(index, list)
        print("Noua lista este")
        for i in range(0, len(list)):
            print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))
    elif optiune == 'B':
        index1 = int(input("Inceput interval: "))
        index2 = int(input("Sfarsit interval: "))
        list = sterge_scor_interval(index1, index2, list)
        print("Noua lista este")
        for i in range(0, len(list)):
            print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))
    elif optiune == 'C':
        index = int(input("Participantul: "))
        scor = int(input("Noul Scor: "))
        list = inlocuieste_scor_participant(index, scor, list)
        print("Noua lista este")
        for i in range(0, len(list)):
            print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))


def afisare_participanti_scor_mic(scor, lista):
    '''
    :param scor: scorul cu care se compara elementele listei
    :param lista: lista de participanti
    :return: o copie a listei cu participantii care au scorul mai mic decat cel dat
    :rtype: list
    '''
    copie = []
    for student in lista:
        if get_scor(student) < scor :
            copie.append(student)
    return copie


def afisare_participanti_ordonati(lista):
    '''
    Functia face o copie a listei si o sorteaza si o returneaza
    :param lista: lista de participanti
    :return: o copie a listei, sortata
    :rtype: list
    '''
    copie = lista[:]
    def scor(student):
        return get_scor(student)
    copie.sort(key = scor)
    return copie


def afisare_participanti_scor_mai_mare(scor_mic, lista):
    '''
    Functia face o copie in care pune doar participantii din lista care au scorul mai mare decat scorul dat
    si o sorteaza, dupa o returneaza
    :param scor_mic: scorul cu care se compara scorurile participantilor
    :param lista: lista in care sunt stocati participantii
    :return: o copie cu participantii sortati dupa scor
    '''
    copie = []
    for i in range(0,len(lista)):
        if get_scor(lista[i]) > scor_mic:
            copie.append(lista[i])
    def scor(student):
        return get_scor(student)
    copie.sort(key = scor)
    return copie


def optiunea3(list):
    print("'A' pentru Tipărește participanții care au scor mai mic decât un scor dat.")
    print("'B' pentru Tipărește participanții ordonat după scor.")
    print("'C' pentru Tipărește participanții cu scor mai mare decât un scor dat, ordonat după scor.")
    optiune = input('>')
    optiune = optiune.upper()
    if optiune < 'A' or optiune > 'C':
        print("Optiune invalida")
    elif optiune == 'A':
        Scor = int(input("Scor: "))
        copie_afisat = afisare_participanti_scor_mic(Scor, list)
        for i in range(0, len(copie_afisat)):
            print("Participantul ", get_index(copie_afisat[i]), " cu scorul ", get_scor(copie_afisat[i]))
    elif optiune == 'B':
        copie_afisat = afisare_participanti_ordonati(list)
        for i in range(0, len(copie_afisat)):
            print("Participantul ", get_index(copie_afisat[i]), " cu scorul ", get_scor(copie_afisat[i]))
    elif optiune == 'C':
        scor = int(input("Introduceti scorul: "))
        copie_afisat = afisare_participanti_scor_mai_mare(scor, list)
        for i in range(0, len(copie_afisat)):
            print("Participantul ", get_index(copie_afisat[i]), " cu scorul ", get_scor(copie_afisat[i]))


def medie_scoruri_interval(index1, index2, lista):
    '''
    :param index1: indexul de la inceputul intervalului
    :param index2: indexul de la sfarsitul intervalului
    :param lista: lista de participanti
    :return: media scorurilor din interval
    '''
    nr_participanti = index2 - index1 + 1
    suma = 0
    for i in range(index1, index2 + 1):
        suma += get_scor(lista[i])
    medie = suma / nr_participanti
    return medie


def scor_minim_interval(index1, index2, lista):
    '''
    :param index1: indexul de la inceputul intervalului
    :param index2: indexul de la finalul intervalului
    :param lista: lista de participanti
    :return: scorul minim din interval
    '''
    minim = 1000
    for i in range(index1, index2+1):
        if (get_scor(lista[i]) < minim):
            minim = get_scor(lista[i])
    return minim


def participanti_cu_scor_multiplu(index1, index2, lista):
    '''
    :param index1: index inceput
    :param index2: index sfarsit
    :param lista: lista de participanti
    :return: o copie care contine participantii cu scorul multiplu de 10
    '''
    copie = []
    for i in range(index1, index2 + 1):
        if get_scor(lista[i]) % 10 == 0:
            copie.append(lista[i])
    return copie


def optiunea4(list):
    print("'A' pentru Calculează media scorurilor pentru un interval dat.")
    print("'B' pentru Calculează scorul minim pentru un interval de participanți dat.")
    print("'C' pentru Tipărește participanții dintr-un interval dat care au scorul multiplu de 10.")
    optiune = input('>')
    optiune = optiune.upper()
    if optiune < 'A' or optiune > 'C':
        print("Optiune invalida")
    elif optiune == 'A':
        index_inceput = int(input("Index inceput:")) - 1
        index_sfarsit = int(input("Index sfarsit:")) - 1
        rezultat = medie_scoruri_interval(index_inceput, index_sfarsit, list)
        print("Media este:", rezultat)
    elif optiune == 'B':
        index_inceput = int(input("Index inceput:")) - 1
        index_sfarsit = int(input("Index sfarsit:")) - 1
        rezultat = scor_minim_interval(index_inceput, index_sfarsit, list)
        print("Scorul minim este", rezultat)
    elif optiune == 'C':
        index_inceput = int(input("Index inceput:")) - 1
        index_sfarsit = int(input("Index sfarsit:")) - 1
        copie_afisat = participanti_cu_scor_multiplu(index_inceput, index_sfarsit, list)
        for i in range(0, len(copie_afisat)):
            print("Participantul ", get_index(copie_afisat[i]), " cu scorul ", get_scor(copie_afisat[i]))


def filtrare_scor_multiplu(numar, lista):
    '''
    :param numar: numarul dupa care se cauta multiplii
    :param lista: lista de participanti
    :return: lista modificata, in care raman doar participantii cu scorul multiplu de numar
    '''
    lista_elemente = []
    for i in range(0, len(lista)):
        if get_scor(lista[i]) % numar != 0:
            lista_elemente.append(lista[i])
    for i in range(0, len(lista_elemente)):
        lista.pop(lista.index(lista_elemente[i]))
    for i in range(0,len(lista)):
        set_index(lista[i],i+1)
    return lista


def filtrare_scor_mai_mic(scor, lista):
    '''
    :param scor: scorul cu care se compara
    :param lista: lista de participanti
    :return: lista modificata in care raman doar participantii care au scorul mai mare sau egal cu cel dat
    '''
    lista_elemente = []
    for i in range(0, len(lista)):
        if get_scor(lista[i]) < scor:
            lista_elemente.append(lista[i])
    for i in range(0, len(lista_elemente)):
        lista.pop(lista.index(lista_elemente[i]))
    for i in range(0, len(lista)):
        set_index (lista[i],i + 1)
    return lista


def optiunea5(list):
    print("'A' pentru Filtrare participanți care au scorul multiplu unui număr dat.")
    print("'B' pentru Filtrare participanți care au scorul mai mic decât un scor dat.")
    optiune = input('>')
    optiune = optiune.upper()
    if optiune < 'A' or optiune > 'B':
        print("Optiune invalida")
    elif optiune == 'A':
        nr = int(input("Numar:"))
        list = filtrare_scor_multiplu(nr, list)
        print("Noua lista este:")
        for i in range(0, len(list)):
            print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))
    elif optiune == 'B':
        scorul = int(input("Scor:"))
        list = filtrare_scor_mai_mic(scorul, list)
        print("Noua lista este:")
        for i in range(0, len(list)):
            print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))


#lista unde este stocat scorul si indexul fiecarui participant
student_list = []

lista_copii = []

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

run()


def make_list_copy(list_of_dict):
    new_list = []
    for element in list_of_dict:
        new_element = {}
        for key, value in element.items():
            new_element[key] = value

        new_list.append(new_element)
    return new_list



while True:
    print_menu()
    option = int(input("Ce optiune alegeti?: "))
    if option == 1:
        copie_lista = make_list_copy(student_list)
        lista_copii.append(copie_lista)
        nr = int(input("Numarul participantului: "))
        print("Scorurile la cele 10 materii sunt: ")
        suma = 0
        ok = True
        for i in range(0, 10):
            element = int(input())
            suma += element
            if (element < 1 or element > 10):
                ok = False
                break
        if ok == True:
            student = creare_participant(suma, nr)
            adaugare_student(student_list, student)
        else:
            print("Date invalide")
    elif option == 2:
        copie_lista = make_list_copy(student_list)
        lista_copii.append(copie_lista)
        optiunea2(student_list)
    elif option == 3:
        optiunea3(student_list)
    elif option == 4:
        optiunea4(student_list)
    elif option == 5:
        copie_lista = make_list_copy(student_list)
        lista_copii.append(copie_lista)
        optiunea5(student_list)
    elif option == 6:
        student_list = lista_copii[len(lista_copii) - 1]
        print("Lista anterioara este:")
        for i in range(0, len(student_list)):
            print("Participantul ", get_index(student_list[i]), " cu scorul ", get_scor(student_list[i]))
        lista_copii.pop()
    elif option == 7:
        break
    else:
        print("Optiune invalida")

