from unittest import TestCase
from Modificat.domain.inchirieri import Inchirieri
from Modificat.domain.filme import Film
from Modificat.domain.clienti import Client
from Modificat.repository.inchiriere_repository import InchirieriRepositoryInMemory
from Modificat.repository.film_repository import FilmRepositoryInMemory
from Modificat.repository.client_repository import ClientRepositoryInMemory

class TestInchiriere(TestCase):

    def setUp(self):
        """
        runs before all test methods
        :return:
        """
        self.__film = Film(123, "Harry Potter", "Film bun", "Sf")
        self.__client = Client(10, "Ion Ionel", 6041017330232)
        self.__inchiriere = Inchirieri(self.__film, self.__client)
        self.__film_repository = FilmRepositoryInMemory()
        self.__client_repository = ClientRepositoryInMemory()
        self.__inchiriere_repository = InchirieriRepositoryInMemory(self.__film_repository, self.__client_repository)


    def test_create_inchiriere(self):

        self.assertEqual(self.__inchiriere.get_film(), self.__film)
        self.assertEqual(self.__inchiriere.get_client(), self.__client)


    def test_add_inchiriere(self):

        film = Film(123, "Harry Potter", "Film bun", "Sf")
        client = Client(10, "Ion Ionel", 6041017330232)
        self.__inchiriere_repository.add_inchiriere(film, client)
        lista = self.__inchiriere_repository.get_toate_inchirierile()
        self.assertEqual(lista[0].get_client().get_nume(), "Ion Ionel")
        self.assertEqual(lista[0].get_film().get_titlu(), "Harry Potter")


    def test_delete_inchiriere(self):

        film = Film(123, "Harry Potter", "Film bun", "Sf")
        client = Client(10, "Ion Ionel", 6041017330232)
        self.__inchiriere_repository.add_inchiriere(film, client)
        lista = self.__inchiriere_repository.get_toate_inchirierile()
        self.assertEqual(lista[0].get_client().get_nume(), "Ion Ionel")
        self.assertEqual(lista[0].get_film().get_titlu(), "Harry Potter")
        self.__inchiriere_repository.delete_inchiriere(0)
        lista = self.__inchiriere_repository.get_toate_inchirierile()
        self.assertEqual(lista, [])


    def test_find_inchiriere(self):

        film = Film(123, "Harry Potter", "Film bun", "Sf")
        client = Client(10, "Ion Ionel", 6041017330232)
        self.__inchiriere_repository.add_inchiriere(film, client)
        inchiriere = self.__inchiriere_repository.find_inchiriere(10, 123)
        self.assertEqual(inchiriere.get_client().get_nume(), "Ion Ionel")
        self.assertEqual(inchiriere.get_film().get_titlu(), "Harry Potter")



