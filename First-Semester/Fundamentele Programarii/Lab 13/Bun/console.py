from Bun.BKT_recursiv import backtracking_recursiv
from Bun.BKT_iterativ import backtracking_iterativ

def print_optiuni():
    print("1. Backtracking iterativ")
    print("2. Backtracking recursiv")
    print("3. Break")


def run():

    while True:

        print_optiuni()
        try:
            option = int(input("Optiune: "))
            if option == 1:

                n = int(input("n = "))
                permutari = []
                for i in range(0, n + 1):
                    permutari.append(0)
                toate_permutarile = []
                lista_permutari = backtracking_iterativ(n, permutari, toate_permutarile)
                for permutare in lista_permutari:
                    print(permutare)

            elif option == 2:

                n = int(input("n = "))
                permutari = []
                toate_permutarile = []
                lista_permutari = backtracking_recursiv(n, permutari, toate_permutarile)
                for permutare in lista_permutari:
                    print(permutare)

            elif option == 3:
                break

            else:
                print("Optiune invalida!")

        except ValueError:
            print("Optiune invalida!")



