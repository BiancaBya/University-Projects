from Domain.melodie import Melodie
from Repository.repository_melodie import MelodieRepository
from datetime import date
import random

class MelodieService:

    def __init__(self, melodie_repository : MelodieRepository):

        self.__melodie_repository = melodie_repository


    def modifica_melodie(self, titlu, artist, gen_nou, data_noua): # data = dd.mm.yy

        """
        Functia care modifica o melodie cu un titlu si un artist dat
        :param titlu: titlul cautat
        :param artist: artistul cautat
        :param gen_nou: genul nou de modificat
        :param data_noua: data noua de modificat
        :return: - (doar scrie in fisier lista de melodii cu melodia noua modificata)
        """

        lista_melodii = self.__melodie_repository.get_toate_melodiile()
        for melodie in lista_melodii:

            if melodie.get_titlu() == titlu and melodie.get_artist() == artist:
                melodie.set_gen(gen_nou)
                zi, luna, an = data_noua.split('.')
                data = date(int(an), int(luna), int(zi))
                melodie.set_data(data)

        self.__melodie_repository.write_to_file(lista_melodii)


    def melodii_random(self, nr_melodii, lista_titluri, lista_artisti):

        """
        Functia care genereaza un numar de  melodii random
        :param nr_melodii: numarul de melodii de generat
        :param lista_titluri: lista de titluri din care se poate alege
        :param lista_artisti: lista de artisti din care se poate alege
        :return: - (scrie in fisier lista actualizata cu noile melodii)
        """

        lista_melodii = self.__melodie_repository.get_toate_melodiile()
        melodii_noi = []

        for i in range(nr_melodii):

            titlu = random.choice(lista_titluri)
            artist = random.choice(lista_artisti)
            gen = random.choice(["Rock", "Jazz", "Pop"])
            zi, luna, an = 12, 12, 2002
            data = date(an, luna, zi)

            melodie = Melodie(titlu, artist, gen, data)
            if melodie not in melodii_noi:
                melodii_noi.append(melodie)
            else:
                i -= 1

        lista_melodii.extend(melodii_noi)
        self.__melodie_repository.write_to_file(lista_melodii)


    def intercalsare(self, lst1, lst2) :
        """
        Fucntia de interclasare folosita de merge sort
        :param lst1: lista 1 care se interclaseaza cu lista 2
        :param lst2: lista 2 care se interclaseaza cu lista 1
        :return: lista compusa din interclasarea celor doua
        """

        i = 0
        j = 0
        lista = []
        while i < len(lst1) and j < len(lst2):
            if lst1[i].get_data() <= lst2[j].get_data():
                lista.append(lst1[i])
                i += 1
            else:
                lista.append(lst2[j])
                j += 1
        lista.extend(lst1[i:])
        lista.extend(lst2[j:])
        return lista


    def merge_sort(self, lista):
        """
        Functia care sorteaza lista in functie de data
        :param lista: lista ce trebuie sortata
        :return: lista sortata dupa data
        """
        if len(lista) <= 1:
            return lista
        mij = len(lista) // 2
        low_part = self.merge_sort(lista[: mij])
        high_part = self.merge_sort(lista[mij:])
        return self.intercalsare(low_part, high_part)




    def export(self, nume_fisier):

        """
        Fucntia exporta lista sortata in alt fisier
        :param nume_fisier: numele fisierului
        :return: -
        """

        repository_export = MelodieRepository(nume_fisier)

        lista_melodii = self.__melodie_repository.get_toate_melodiile()
        lista_afisat = self.merge_sort(lista_melodii)
        repository_export.write_to_file(lista_afisat)


    def get_melodii(self):
        return self.__melodie_repository.get_toate_melodiile()





