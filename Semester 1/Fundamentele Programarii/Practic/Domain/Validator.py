from Repository.repository_melodie import MelodieRepository
from datetime import date

class Validator:

    def __init__(self, melodie_repository : MelodieRepository):

        self.__melodie_repository = melodie_repository

    def validare_melodie(self, titlu, artist, gen, data):

        """
        Functia valideaza datele de intrare pentru o melodie si aduna erorile ce pot aparea
        :param titlu: titlul
        :param artist: artistul
        :param gen: genul
        :param data: data
        :return: lista de erori
        """

        erori = []
        ok = 0

        lista_melodii = self.__melodie_repository.get_toate_melodiile()
        for melodie in lista_melodii:
            if melodie.get_titlu() == titlu and melodie.get_artist() == artist:
                ok = 1

        if ok == 0:

            eroare = "Melodia nu exista"
            erori.append(eroare)

        if titlu == '':

            eroare = "Titlul este vid"
            erori.append(eroare)

        if artist == '':

            eroare = "Artistul este vid"
            erori.append(eroare)

        lista_genuri = ["Rock", "Jazz", "Pop"]
        if gen not in lista_genuri:

            eroare = "Genul nu este valid"
            erori.append(eroare)

        zi, luna, an = data.split('.')
        try:

            data = date(int(an), int(luna), int(zi))

        except ValueError as eroare:

            erori.append(eroare)

        return erori





