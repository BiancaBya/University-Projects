from Service.service_melodie import MelodieService
from Domain.Validator import Validator

class Consola:

    def __init__(self, melodie_service : MelodieService, validator : Validator):

        self.__melodie_service = melodie_service
        self.__validator = validator


    def print_menu(self):
        print("1. Modificare melodie")
        print("2. Melodii Random")
        print("3. Exporta sortat dupa data aplicatiei")
        print("4. Break")


    def modificare_melodie(self):
        """
        Functia citeste de la tastatura datele ce trebuie cautate sau modificate
        :return: -
        """

        titlu = input("titlu = ")
        artist = input("artist = ")
        gen_nou = input("gen_nou = ")
        data_noua = input("data_noua = ")
        erori = self.__validator.validare_melodie(titlu, artist, gen_nou, data_noua)
        if erori == []:
            self.__melodie_service.modifica_melodie(titlu, artist, gen_nou, data_noua)
        else:
            for eroare in erori:
                print(eroare)


    def melodii_random(self):
        """
        Functia citeste de la tastatura nr de melodii de generat si titlurile si artistii din care se poate alege
        :return: -
        """

        nr = int(input("nr de melodii random: "))

        titluri = input("Titluri: ")
        lista_titluri = titluri.split(',')
        lista_titluri = [titlu.strip() for titlu in lista_titluri]

        artisti = input("Artisti: ")
        lista_artisti = artisti.split(',')
        lista_artisti = [artist.strip() for artist in artisti]

        self.__melodie_service.melodii_random(nr, lista_titluri, lista_artisti)
        print("S-au generat " , nr, " melodii random")


    def export(self):

        nume_fisier = input("Nume fisier: ")
        self.__melodie_service.export(nume_fisier)


    def run(self):

        self.print_menu()

        while True:
            optiune = input('>')
            if optiune == '1':
                self.modificare_melodie()
            elif optiune == '2':
                self.melodii_random()
            elif optiune == '3':
                self.export()
            elif optiune == '4':
                break
            else:
                print("Optiune invalida")










