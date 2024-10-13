from manage_student import make_list_copy, get_index, get_scor, set_scor, set_index


def validare_nr_participant(nr, student_list):
    if nr.isnumeric() == False:
        raise ValueError ("Date invalide!")
    else:
        nr = int(nr)
        if (nr != len(student_list) + 1):
            raise ValueError ("Date invalide!")


def validare_element(element):
    if element.isnumeric() == False:
        raise ValueError("Date invalide!")


def validare_index(index : str,list):
    if index.isnumeric() == False:
        raise ValueError("Index invalid!")
    index = int(index)
    index = index - 1
    if index < 0 or index >= len(list):
        raise ValueError("Index Invalid!")

def validare_participant(index, list):
    if int(index) <= 0 or int(index) > len(list):
        raise ValueError("Participant invalid!")

def validare_scor(scor):
    if int(scor) <= 0 or int(scor) > 100:
        raise ValueError("Scor invalid!")

def validare_numar(numar : str):
    if (numar.isnumeric() == False):
        raise ValueError("Date invalide!")


def validare_optiune(optiune: list, student_list):
    copie = optiune[:]
    copie_lista = make_list_copy(student_list)
    for i in copie:
        i = i.strip()
        if "Adauga" == i :
            copie_lista.append({'scor':1, 'index':1})
        elif "StergeP" in i:
            j = i.split(' ')
            if len(j) > 2:
                raise ValueError ("Date invalide!")
            validare_index(j[1], copie_lista)
        elif "StergeI" in i:
            j = i.split(' ')
            if len(j) > 3:
                raise ValueError("Date invalide!")
            validare_index(j[1],copie_lista)
            validare_index(j[2],copie_lista)
        elif "InlocuiesteP" in i:
            j = i.split(' ')
            if len(j) > 3:
                raise ValueError("Date invalide!")
            validare_index(j[1],copie_lista)
            validare_scor(j[2])
        elif "TiparesteSmic" in i or "TiparesteSmare" in i:
            j = i.split(' ')
            if len(j) > 2:
                raise ValueError("Date invalide!")
            validare_scor(j[1])
        elif "TiparesteOrdonat" == i :
            pass
        elif "MedieInterval" in i or "Scorminim" in i or "Scormultiplu" in i:
            j = i.split(' ')
            if len(j) > 3:
                raise ValueError("Date invalide!")
            validare_index(j[1],copie_lista)
            validare_index(j[2],copie_lista)
        elif "Filtraremultiplu" in i:
            j = i.split(' ')
            if len(j) > 2:
                raise ValueError("Date invalide!")
            validare_numar(j[1])
        elif "Filtrarescor" in i:
            j = i.split(' ')
            if len(j) > 2:
                raise ValueError("Date invalide!")
            validare_scor(j[1])
        elif "Break" in i or "Undo" in i :
            pass
        else:
            raise ValueError("Date invalide!")

