from Modificat.repository.inchiriere_repository import InchirieriRepositoryInMemory, InchirieriRepoInFile
from Modificat.repository.film_repository import FilmRepositoryInMemory, FilmRepoInFile
from Modificat.repository.client_repository import ClientRepositoryInMemory, ClientRepoInFile
from Modificat.domain.inchirieri import Inchirieri
from Modificat.domain.validator import InchiriereValidator, ReturnareValidator

class InchiriereService :

    def __init__(self, film_repository : FilmRepoInFile, client_repository : ClientRepoInFile, inchirieri_repository : InchirieriRepoInFile):
        self.__film_repository = film_repository
        self.__client_repository = client_repository
        self.__inchirieri_repository = inchirieri_repository


    def get_toate_inchirierile(self):
        return self.__inchirieri_repository.get_toate_inchirierile()


    def inchiriere_film(self, id_client, id_film):
        validator = InchiriereValidator(self.__film_repository, self.__client_repository, self.__inchirieri_repository)
        validator.validare_inchiriere(id_client, id_film)
        filme = self.__film_repository.get_toate_filmele()
        film = self.__film_repository.find_film_id_recursiv(id_film, filme, 0)
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


    def bubble_sort(self, list, key = None):

        ok = True
        while ok == True:
            ok = False
            for index in range(0, len(list) - 1):
                if key(list[index]) > key(list[index + 1]):
                    list[index], list[index + 1] = list[index + 1], list[index]
                    ok = True

        return list


    def clienti_ordonati_nume(self):
        lista_inchirieri = self.__inchirieri_repository.get_toate_inchirierile()

        #lista_inchirieri.sort(key = lambda inchiriere : inchiriere.get_client().get_nume())
        self.bubble_sort(lista_inchirieri, key = lambda inchiriere : inchiriere.get_client().get_nume())

        copie_lista = lista_inchirieri[:]
        for i in range(0, len(copie_lista)):
            copie_lista[i] = copie_lista[i].get_client()

        lista_copie = []
        for aux in copie_lista:
            if aux not in lista_copie:
                lista_copie.append(aux)

        return lista_copie

    def shake_sort(self, list, key = None):
        n = len(list)
        ok = True
        inceput = 0
        final = n - 1

        while (ok == True):
            ok = False

            for i in range(inceput, final):
                if (key(list[i]) > key(list[i + 1])):
                    list[i], list[i + 1] = list[i + 1], list[i]
                    ok = True

            if (ok == False):
                break

            ok = False

            final = final - 1
            for i in range(final - 1, inceput - 1, -1):
                if (key(list[i]) > key(list[i + 1])):
                    list[i], list[i + 1] = list[i + 1], list[i]
                    ok = True

            inceput = inceput + 1
        return list


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

        #lista_tuple.sort(key = lambda client_tuple : client_tuple[1])
        lista_tuple = self.shake_sort(lista_tuple, key = lambda client_tuple : client_tuple[1])

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

        lista_tuple.sort(key = lambda film_tuple : film_tuple[1])

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

        lista_tuple.sort(key = lambda client_tuple : client_tuple[1], reverse = True)

        lista_tuple_copie = []
        for aux in lista_tuple:
            if aux not in lista_tuple_copie:
                lista_tuple_copie.append(aux)
        lista_tuple = lista_tuple_copie[:]
        lungime = len(lista_tuple)
        lungime = 0.3 * lungime
        lungime = int(lungime)

        return lista_tuple, lungime









