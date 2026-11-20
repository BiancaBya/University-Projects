from Bun.BKT_recursiv import backtracking_recursiv
from Bun.BKT_iterativ import backtracking_iterativ


def testat():
    n = 3
    permutare = []
    liste = []
    liste = backtracking_recursiv(n, permutare, liste)
    assert (liste == [[1, 2, 3], [2, 1, 3], [2, 3, 1], [3, 2, 1]])

    permutare = []
    liste = []
    for i in range(0, n + 1):
        permutare.append(0)
    liste = backtracking_iterativ(n, permutare, liste)
    assert (liste == [[1, 2, 3], [2, 1, 3], [2, 3, 1], [3, 2, 1]])



