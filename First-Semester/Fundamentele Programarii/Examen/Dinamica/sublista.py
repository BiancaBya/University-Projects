# sublista crescatoare de lungime max cu nu de 2 cifre 33 4 30 6 20 5 40 60 ->  20 40 60


def sublista_maxima(lista):

    n = len(lista)
    rezultat = []

    s = []
    for i in range(n):
        if 10 <= lista[i] <= 99:
            s.append(1)      # la care sunt bune, punem 1, pentru ca ele in sine sunt o subsecventa valida de lungime 1
        else:
            s.append(0)      # care nu sunt, 0

    p = [-1] * n

    for i in range(n):
        for j in range(i):
            if 10 <= lista[i] <= 99 and 10 <= lista[j] <= 99 and lista[i] >= lista[j] and s[j] + 1 > s[i]:
                # daca elementele de pe pozitiile i si j sunt pare, si cel de pe pozitia i este mai mare decat
                # cel de pe pozitia j, si s[j] + 1 > s[i] -> daca lungimea subsecventei obtinute e mai mare decat cea curenta
                s[i] = s[j] + 1 # lungimea subsecventei maxime pana la pozitia i
                p[i] = j # indicele unde se termina subsecventa maxima anterioara


    max = 0
    indice = 0
    for i in range(n):
        if s[i] > max:
            max = s[i]
            indice = i

    while indice != -1:
        rezultat.append(lista[indice])
        indice = p[indice]

    rezultat.reverse()

    if max == 0:
        rezultat = []

    return rezultat




lista = [22, 10, 16, 11, 4, 13, 14]
print(sublista_maxima(lista))








