from random import randint
from matplotlib.pyplot import plot, grid, title, show, yticks, xticks
from numpy import arange


def simulari(n, nr_simulari):
    contor = 0
    for _ in range(nr_simulari):
        nr = []
        for i in range(n):
            numar = randint(1, 365)
            nr.append(numar)

        nr_egale = 0

        for i in range(n-1):
            for j in range(i + 1, n):
                if nr[i] == nr[j]:
                    nr_egale = 1
                    break

        contor += nr_egale

    prob = float(contor/nr_simulari)
    return prob



n = int(input("n = "))
simulari(n, 1000)


title('Plot test')
xs = range(2, 50)
ys = [simulari(x, 1000) for x in xs]
plot(xs,ys,'r*')
grid()
yticks(arange(0, 1, step=0.1))
xticks(range(0, 55, 5))
show()




