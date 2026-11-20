def validate(lista):
    """
    functia valideaza o solutie posibila, verificand daca exista doua numere in lista care sa indeplineasca proprietatea
    :param lista: lista care trebuie verificata
    :return: True/False daca solutia este corecta sau nu
    """
    for i in range(1, len(lista)):
        ok = False
        for j in range(0, i + 1):
            if abs(lista[i] - lista[j]) == 1:
                ok = True
        if not ok:
            return False
    return True


def permutare_verificare(lista):

    for i in range(0, len(lista)-1):
        for j in range(i+1, len(lista)):
            if lista[i] == lista[j]:
                return False
    return True


def backtracking_iterativ(n, permutare : list, toate_permutarile : list):

    index = 1
    while index > 0:
        if permutare[index] < n:
            permutare[index] += 1
            if True:
                if index == n:
                    if validate(permutare[1:]) and permutare_verificare(permutare[1:]):
                        toate_permutarile.append(permutare[1:])
                else:
                    index += 1
        else:
            permutare[index] = 0
            index -= 1

    return toate_permutarile



