from Functionalitati import *
from manage_student import *
from Console import lista_anterioara, date_invalide, optiune_invalida, print_menu, citire_optiune, citire_participant
from Validari import validare_optiune

def start():
    student_list = []
    lista_copii = []
    alegere2 = 0
    alegere3 = 0
    alegere4 = 0
    alegere5 = 0
    participant = 0
    index_inceput = 0
    index_sfarsit = 0
    scor = 0
    scor_mic = 0
    scor_mare = 0
    numar = 0
    optiune = 0
    while True:
        print_menu()
        option = citire_optiune()
        option = option.split(';')
        validare_optiune(option,student_list)
        while len(option) != 0:
            option[0] = option[0].strip()
            if "Adauga" in option[0]:
                optiune = 1
            elif "Sterge" in option[0] or "Inlocuieste" in option[0]:
                optiune = 2
                date = option[0].split(' ')
                if date[0] == ' ' or date[0] == '':
                    date.pop(0)
                if date[0] == "StergeP":
                    alegere2 = 1
                    participant = int(date [1])
                elif date[0] == "StergeI":
                    alegere2 = 2
                    index_inceput = int(date[1])
                    index_sfarsit = int(date[2])
                elif date[0] == "InlocuiesteP":
                    alegere2 = 3
                    participant = int(date[1])
                    scor = int(date[2])
            elif "Tipareste" in option[0]:
                optiune = 3
                date = option[0].split(' ')
                if date[0] == ' ' or date[0] == '':
                    date.pop(0)
                if date[0] == "TiparesteSmic":
                    alegere3 = 1
                    scor = int(date[1])
                elif date [0] == "TiparesteSmare":
                    alegere3 = 3
                    scor = int(date[1])
                else:
                    alegere3 = 2
            elif "MedieInterval" in option[0] or "Scorminim" in option[0] or "Scormultiplu" in option[0]:
                optiune = 4
                date = option[0].split(' ')
                if date[0] == ' ' or date[0] == '':
                    date.pop(0)
                index_inceput = int(date[1]) - 1
                index_sfarsit = int(date[2]) - 1
                if date[0] == "MedieInterval":
                    alegere4 = 1
                elif date[0] == "Scorminim":
                    alegere4 = 2
                else :
                    alegere4 = 3
            elif "Filtrare" in option[0]:
                optiune = 5
                date = option[0].split(' ')
                if date[0] == ' ' or date[0] == '':
                    date.pop(0)
                if date[0] == "Filtraremultiplu":
                    numar = int(date[1])
                    alegere5 = 1
                else :
                    scor = int(date[1])
                    alegere5 = 2
            elif "Undo" in option[0]:
                optiune = 6
            elif "Break" in option[0]:
                optiune = 7
            if optiune == 1:
                citire_participant(student_list,lista_copii)
            elif optiune == 2:
                copie_lista = make_list_copy(student_list)
                lista_copii.append(copie_lista)
                optiunea2(student_list, alegere2, participant, index_inceput, index_sfarsit, scor)
            elif optiune == 3:
                optiunea3(student_list, alegere3, scor)
            elif optiune == 4:
                optiunea4(student_list, alegere4, index_inceput, index_sfarsit)
            elif optiune == 5:
                copie_lista = make_list_copy(student_list)
                lista_copii.append(copie_lista)
                optiunea5(student_list, alegere5, numar, scor)
            elif optiune == 6:
                student_list = lista_copii[len(lista_copii) - 1]
                lista_anterioara(student_list)
                lista_copii.pop()
            elif optiune == 7:
                break
            else:
                raise ValueError("Optiune invalida!")
            option.pop(0)
        if optiune == 7:
            break

