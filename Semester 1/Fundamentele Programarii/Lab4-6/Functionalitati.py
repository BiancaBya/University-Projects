from manage_student import get_scor, get_index, set_index, set_scor
from Console import optiune_invalida, print_list, afisare_lista, print_medie, scor_minim, date_invalide

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
        set_index(lista[index], index + 1)
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
    for index in range(indice1 - 1 , len(lista)):
        set_index(lista[index], index + 1)
    #for-ul actualizeaza indicii participantilor de dupa cei care au fost stersi
    return lista

def inlocuieste_scor_participant(indice, scorul, lista):
    '''
    :param indice: indicele participantului la care se inlocuieste scorul
    :param scorul: scorul nou
    :param lista: lista de participanti
    :return: lista modifiacta
    '''
    #lista[indice-1].update({'scor':scorul})
    set_scor(lista[indice-1], scorul)
    return lista


def optiunea2(list, alegerea2, participant, index_inceput, index_sfarsit, scor):
    if alegerea2 == 1:
        list = sterge_scor_participant(participant, list)
        print_list(list)
    elif alegerea2 == 2:
        list = sterge_scor_interval(index_inceput, index_sfarsit, list)
        print_list(list)
    elif alegerea2 == 3:
        list = inlocuieste_scor_participant(participant, scor, list)
        print_list(list)


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


def optiunea3(list, alegerea3, scor):
    if alegerea3 == 1:
        copie_afisat = afisare_participanti_scor_mic(scor, list)
        afisare_lista(copie_afisat)
    elif alegerea3 == 2:
        copie_afisat = afisare_participanti_ordonati(list)
        afisare_lista(copie_afisat)
    elif alegerea3 == 3:
        copie_afisat = afisare_participanti_scor_mai_mare(scor, list)
        afisare_lista(copie_afisat)


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


def optiunea4(list, alegerea4, index_inceput, index_sfarsit):
    if alegerea4 == 1:
        rezultat = medie_scoruri_interval(index_inceput, index_sfarsit, list)
        print_medie(rezultat)
    elif alegerea4 == 2:
        rezultat = medie_scoruri_interval(index_inceput, index_sfarsit, list)
        rezultat = scor_minim_interval(index_inceput, index_sfarsit, list)
        scor_minim(rezultat)
    elif alegerea4 == 3:
        copie_afisat = participanti_cu_scor_multiplu(index_inceput, index_sfarsit, list)
        afisare_lista(copie_afisat)


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


def optiunea5(list, alegerea5, numar, scor):
    if alegerea5 == 1:
        list = filtrare_scor_multiplu(numar, list)
        print_list(list)
    elif alegerea5 == 2:
        list = filtrare_scor_mai_mic(scor, list)
        print_list(list)


