from Service.tractor_service import TractorService
from Domain.validator import Validator
from datetime import date

class Consola:

    def __init__(self, tractor_service : TractorService, validator : Validator):

        self.__tractor_service = tractor_service
        self.__validator = validator


    def print_menu(self):

        print("1. Afisare Tractoare")
        print("2. Adaugare Tractor")
        print("3. Sterge Tractor")
        print("4. Undo")
        print("5. Filtrare")
        print("6. Tractoare Random")
        print("7. Break")


    def afisare_tractoare(self):

        tractoare = self.__tractor_service.get_toate_tractoarele()
        for tractor in tractoare:
            print(tractor)


    def adaugare_tractor(self):

        id = input("id = ")
        denumire = input("denumire = ")
        pret = input("pret = ")
        model = input("model = ")
        data = input("data = ")
        erori = self.__validator.validare_tractor(id, denumire, pret, model, data)
        if erori == []:
            self.__tractor_service.adaugare_tractor(id, denumire, pret, model, data)
        else:
            for eroare in erori:
                print(eroare)


    def sterge_tractor(self):

        cifra = input("cifra = ")
        contor = self.__tractor_service.sterge_tractoare(cifra)
        print("Au fost sterse " + str(contor) + " tractoare")


    def undo(self):

        lista = self.__tractor_service.undo()
        print("lista de tractoare este acum:")
        for tractor in lista:
            print(tractor)


    def filtrare(self):

        sir = input("sir = ")
        numar = input("numar = ")
        lista = self.__tractor_service.filtrare(sir, numar)
        print("Lista filtrata este: \n")
        for tractor in lista:
            if tractor.get_data() < date.today():
                print("*")
            print(tractor)


    def tractoare_random(self):

        nr = input("Numarul de tractoare de generat: ")
        lista_tractoare_random = self.__tractor_service.tractoare_random(int(nr))
        for tractor in lista_tractoare_random:
            print(tractor)


    def run(self):

        while True:

            self.print_menu()
            option = input(">")

            if option == '1':
                self.afisare_tractoare()
            elif option == '2':
                self.adaugare_tractor()
            elif option == '3':
                self.sterge_tractor()
            elif option == '4':
                self.undo()
            elif option == '5':
                self.filtrare()
            elif option == '6':
                self.tractoare_random()
            elif option == '7':
                break
            else:
                print("Optiune invalida!")










