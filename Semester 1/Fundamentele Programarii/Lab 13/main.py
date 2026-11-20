'''
Generați toate permmutările de dimensiune n (1..n), cu propritatea că pentru orice i 2<=i<=n
exista un j, 1<=j<=i astfel încât |v(i)-v(j)|=1.
'''



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


def backtracking_iterativ(n, permutare : list, toate_permutarile = []):

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


def backtracking_recursiv(n, permutare : list, toate_permutarile = []):

    if n == len(permutare):
        if validate(permutare):
            toate_permutarile.append(permutare)
        return

    for index in range (1, n+1):
        if index not in permutare:
            backtracking_recursiv(n, permutare + [index])

    return toate_permutarile



n = int(input("n = "))
permutari = []

lista_permutari = backtracking_recursiv(n, permutari)
for permutare in lista_permutari:
   print(permutare)

for i in range(0, n+1):
    permutari.append(0)

print('\n')

lista_permutari = backtracking_iterativ(n, permutari)
for permutare in lista_permutari:
   print(permutare)





