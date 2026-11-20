from Repository.tractor_repository import TractorRepository
from Domain.tractor import Tractor
from datetime import date

class Validator:

    def __init__(self, tractor_repository : TractorRepository):

        self.__tractor_repository = tractor_repository


    def validare_tractor(self, id, denumire, pret, model, data):

        erori = []

        lista_tractoare = self.__tractor_repository.get_toate_tractoarele()
        for tractor in lista_tractoare:
            if str(tractor.get_id()) == str(id):
                eroare = "ID-ul exista deja"
                if eroare not in erori:
                    erori.append(eroare)
            if str(tractor.get_denumire()) == str(denumire):
                eroare = "Denumirea exista deja"
                if eroare not in erori:
                    erori.append(eroare)

        try:

            pret_int = int(pret)

        except ValueError as eroare:
            erori.append(eroare)

        zi, luna, an = data.split(":")
        try:

            date(int(an), int(luna), int(zi))

        except ValueError as eroare:
            erori.append(eroare)

        return erori













