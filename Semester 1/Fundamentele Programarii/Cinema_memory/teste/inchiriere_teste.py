from unittest import TestCase
from Cinema_nou.domain.inchiriere import Inchirieri
from Cinema_nou.domain.film import Film
from Cinema_nou.domain.client import Client
from Cinema_nou.repository.inchiriere_repository import InchirieriRepository
from Cinema_nou.repository.film_repository import FilmRepository
from Cinema_nou.repository.client_repository import ClientRepository


class TestInchiriere(TestCase):

    def setUp(self):
        """
        runs before all test methods
        :return:
        """
        self.__film = Film(123, "Harry Potter", "Film bun", "Sf")
        self.__client = Client(10, "Ion Ionel", 6041017330232)
        self.__inchiriere = Inchirieri(self.__film, self.__client)
        self.__film_repository = FilmRepository()
        self.__client_repository = ClientRepository()
        self.__inchiriere_repository = InchirieriRepository(self.__film_repository, self.__client_repository)


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



