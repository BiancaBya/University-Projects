import random

N = 10000
M = 10

matrix = [[random.randint(0, 255) for _ in range(M)] for _ in range(N)]

filename = "matrix_10000_10.txt"

with open(filename, "w") as f:
    for row in matrix:
        line = " ".join(str(x) for x in row)
        f.write(line + "\n")



