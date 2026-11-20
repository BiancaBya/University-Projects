from manage_student import get_scor, get_index, set_index, set_scor
from manage_student import adaugare_student, creare_participant, make_list_copy
from Validari import validare_nr_participant, validare_element, validare_numar, validare_index, validare_participant


def print_list(list):
    print("Noua lista este:")
    for i in range(0, len(list)):
        print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))

def afisare_lista(copie_afisat):
    for i in range(0, len(copie_afisat)):
        print("Participantul ", get_index(copie_afisat[i]), " cu scorul ", get_scor(copie_afisat[i]))

def print_medie(rezultat):
    print("Media este:", rezultat)

def scor_minim(rezultat):
    print("Scorul minim este", rezultat)

def lista_anterioara(list):
    print("Lista anterioara este:")
    for i in range(0, len(list)):
        print("Participantul ", get_index(list[i]), " cu scorul ", get_scor(list[i]))


def date_invalide():
    print("Date invalide")

def optiune_invalida():
    print("Optiune invalida")

def citire_optiune():
    option = input("Introduceti datele: ")
    return option


def citire_participant(student_list, lista_copii):
    nr = input("Numarul participantului: ")
    validare_nr_participant(nr, student_list)
    nr = int(nr)
    print("Scorurile la cele 10 materii sunt: ")
    suma = 0
    ok = True
    for i in range(0, 10):
        element = input()
        validare_element(element)
        element = int(element)
        suma += element
        if (element < 1 or element > 10):
            ok = False
            break
    if ok == True:
        copie_lista = make_list_copy(student_list)
        lista_copii.append(copie_lista)
        student = creare_participant(suma, nr)
        adaugare_student(student_list, student)
    else:
        raise ValueError ("Date invalide!")


def print_menu():
    print("1.Adauga")
    print("2.StergeP, StergeI, InlocuiesteP")
    print("3.TiparesteSmic, TiparesteOrdonat, TiparesteSmare")
    print("4.MedieInterval, Scorminim, Scormultiplu")
    print("5.Filtraremultiplu, Filtrarescor")
    print("6.Undo")
    print("7.Break")



