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


def make_list_copy(list_of_dict):
    new_list = []
    for element in list_of_dict:
        new_element = {}
        for key, value in element.items():
            new_element[key] = value

        new_list.append(new_element)
    return new_list

