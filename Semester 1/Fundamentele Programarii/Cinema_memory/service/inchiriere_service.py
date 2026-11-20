from Cinema_nou.repository.inchiriere_repository import InchirieriRepository
from Cinema_nou.repository.film_repository import FilmRepository
from Cinema_nou.repository.client_repository import ClientRepository
from Cinema_nou.domain.inchiriere import Inchirieri
from Cinema_nou.domain.validator import InchiriereValidator, ReturnareValidator

class InchiriereService :

    def __init__(self, film_repository : FilmRepository, client_repository : ClientRepository, inchirieri_repository : InchirieriRepository):
        self.__film_repository = film_repository
        self.__client_repository = client_repository
        self.__inchirieri_repository = inchirieri_repository


    def get_toate_inchirierile(self):
        return self.__inchirieri_repository.get_toate_inchirierile()


    def inchiriere_film(self, id_client, id_film):
        validator = InchiriereValidator(self.__film_repository, self.__client_repository, self.__inchirieri_repository)
        validator.validare_inchiriere(id_client, id_film)
        film = self.__film_repository.find_film_id(id_film)
        client = self.__client_repository.find_client_id(id_client)
        self.__inchirieri_repository.add_inchiriere(film, client)


    def returnare_film(self, id_client, id_film):
        validator = ReturnareValidator(self.__inchirieri_repository)
        validator.validare_returnare(id_film, id_client)
        film = self.__film_repository.find_film_id(id_film)
        client = self.__client_repository.find_client_id(id_client)
        inchiriere = self.__inchirieri_repository.find_inchiriere(id_client, id_film)
        index = self.__inchirieri_repository.return_index(inchiriere)
        self.__inchirieri_repository.delete_inchiriere(index)


    def clienti_ordonati_nume(self):
        lista_inchirieri = self.__inchirieri_repository.get_toate_inchirierile()
        def nume(inchiriere : Inchirieri):
            return inchiriere.get_client().get_nume()
        lista_inchirieri.sort(key = nume)

        copie_lista = lista_inchirieri[:]
        for i in range(0, len(copie_lista)):
            copie_lista[i] = copie_lista[i].get_client()

        lista_copie = []
        for aux in copie_lista:
            if aux not in lista_copie:
                lista_copie.append(aux)

        return lista_copie


    def clienti_ordonati_filme(self):
        lista_inchirieri = self.__inchirieri_repository.get_toate_inchirierile()
        lista_clienti = []
        for inchiriere in lista_inchirieri:
            lista_clienti.append(inchiriere.get_client())
        lista_tuple = []
        for client in lista_clienti:
            nr = 0
            for aux in lista_clienti:
                if aux == client:
                    nr += 1
            client_tuple = (client, nr)
            lista_tuple.append(client_tuple)

        def numar(client_tuple):
            return client_tuple[1]

        lista_tuple.sort(key = numar)

        lista_tuple_copie = []
        for aux in lista_tuple:
            if aux not in lista_tuple_copie:
                lista_tuple_copie.append(aux)
        lista_tuple = lista_tuple_copie[:]

        return lista_tuple


    def filme_ordonate_inchirieri(self):
        lista_inchirieri = self.__inchirieri_repository.get_toate_inchirierile()
        lista_filme = []
        for inchiriere in lista_inchirieri:
            lista_filme.append(inchiriere.get_film())
        lista_tuple = []
        for film in lista_filme:
            nr = 0
            for aux in lista_filme:
                if aux == film:
                    nr += 1
            film_tuple = (film, nr)
            lista_tuple.append(film_tuple)

        def numar(film_tuple):
            return film_tuple[1]

        lista_tuple.sort(key = numar)

        lista_tuple_copie = []
        for aux in lista_tuple:
            if aux not in lista_tuple_copie:
                lista_tuple_copie.append(aux)
        lista_tuple = lista_tuple_copie[:]

        return lista_tuple


    def primii_clienti_30(self):
        lista_inchirieri = self.__inchirieri_repository.get_toate_inchirierile()
        lista_clienti = []
        for inchiriere in lista_inchirieri:
            lista_clienti.append(inchiriere.get_client())
        lista_tuple = []
        for client in lista_clienti:
            nr = 0
            for aux in lista_clienti:
                if aux == client:
                    nr += 1
            client_tuple = (client, nr)
            lista_tuple.append(client_tuple)

        def numar(client_tuple):
            return client_tuple[1]

        lista_tuple.sort(key = numar, reverse = True)

        lista_tuple_copie = []
        for aux in lista_tuple:
            if aux not in lista_tuple_copie:
                lista_tuple_copie.append(aux)
        lista_tuple = lista_tuple_copie[:]
        lungime = len(lista_tuple)
        lungime = 0.3 * lungime
        lungime = int(lungime)

        return lista_tuple, lungime









