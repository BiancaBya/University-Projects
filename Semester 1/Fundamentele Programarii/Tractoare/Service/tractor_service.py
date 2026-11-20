from Domain.tractor import Tractor
from Repository.tractor_repository import TractorRepository
from datetime import date
import random
import string

class TractorService:

    def __init__(self, tractor_repository : TractorRepository):

        self.__tractor_repository = tractor_repository
        self.__lista_undo = []


    def get_toate_tractoarele(self):

        return self.__tractor_repository.get_toate_tractoarele()


    def adaugare_tractor(self, id, denumire, pret, model, data):

        lista_tractoare = self.get_toate_tractoarele()
        self.__lista_undo.append(lista_tractoare)

        zi, luna, an = data.split(":")
        data = date(int(an), int(luna), int(zi))
        tractor = Tractor(id, denumire, pret, model, data)
        self.__tractor_repository.adaugare_tractor(tractor)


    def sterge_tractoare(self, cifra):

        lista_tractoare = self.get_toate_tractoarele()
        self.__lista_undo.append(lista_tractoare)

        contor = 0
        lista_tractoare = self.get_toate_tractoarele()
        for tractor in lista_tractoare:

            pret = tractor.get_pret()
            if str(cifra) in str(pret):
                self.__tractor_repository.sterge_tractor(tractor)
                contor += 1

        return contor


    def undo(self):

        lista = self.__lista_undo.pop()
        self.__tractor_repository.write_to_file(lista)
        return lista


    def filtrare(self, sir, numar):

        if sir == "" and numar == '-1':
            lista_finala = self.get_toate_tractoarele()

        elif sir != "" and numar == '-1':
            lista = self.get_toate_tractoarele()
            lista_finala = []
            for tractor in lista:
                if sir in str(tractor.get_denumire()):
                    lista_finala.append(tractor)

        elif sir == "" and numar != '-1':
            lista = self.get_toate_tractoarele()
            lista_finala = []
            for tractor in lista:
                if int(tractor.get_pret()) < int(numar):
                    lista_finala.append(tractor)

        else:
            lista = self.get_toate_tractoarele()
            lista_finala = []
            for tractor in lista:
                if int(tractor.get_pret()) < int(numar) and sir in str(tractor.get_denumire()):
                    lista_finala.append(tractor)

        return lista_finala


    def tractoare_random(self, nr):

        lista_tractoare_random = []

        for i in range(nr):

            id = random.randint(0, 10000)
            pret = random.randint(100,5000)

            sir_litere = "aeioubcdfghjklmnpqrstvwxyz"
            lungime_model = random.randint(5, 10)
            model = list(' ' * lungime_model)
            for i in range(lungime_model):
                model [i] = random.choice(sir_litere)

            model = ''.join(model)

            # denumire cu lungime intre 5 si 20, se altereaza consoanele si vocalele si sa contina un spatiu
            vocale = "aeiou"
            consoane = "bcdfghjklmnpqrstvwxyz"

            lungime = random.randint(5, 20)
            denumire = list (' ' * lungime)
            index_spatiu = random.randint(1, lungime-1)

            for i in range(lungime):
                if i != index_spatiu:
                    if i % 2:
                        denumire[i] = random.choice(vocale)
                    else:
                        denumire[i] = random.choice(consoane)

            denumire = ''.join(denumire)

            zi = random.randint (1, 30)
            luna = random.randint (1, 12)
            an = random.randint (2000, 2050)
            data = date(an, luna, zi)

            tractor_random = Tractor(id, denumire, pret, model, data)
            lista_tractoare_random.append(tractor_random)


        return lista_tractoare_random



