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


def backtracking_recursiv(n, permutare : list, toate_permutarile : list):

    if n == len(permutare):
        if validate(permutare):
            toate_permutarile.append(permutare)
        return

    for index in range (1, n+1):
        if index not in permutare:
            backtracking_recursiv(n, permutare + [index], toate_permutarile)

    return toate_permutarile



