from unittest import TestCase
from Modificat.domain.filme import Film
from Modificat.repository.film_repository import FilmRepositoryInMemory

class TestFilm(TestCase):

    def setUp(self):
        """
        runs before all test methods
        :return:
        """
        self.__film = Film(123, "Harry Potter", "Film bun", "Sf")
        self.__film_repository = FilmRepositoryInMemory()


    def test_create_film(self):

        self.assertEqual(self.__film.get_id(), 123)
        self.assertEqual(self.__film.get_titlu(), "Harry Potter")
        self.assertEqual(self.__film.get_descriere(), "Film bun")
        self.assertEqual(self.__film.get_gen(), "Sf")


    def test_add_film(self):

        self.__film_repository.add_film(1, "Harry Potter", "bun", "SF")
        lista = self.__film_repository.get_toate_filmele()
        self.assertEqual(lista[0].get_id(), 1)
        self.assertEqual(lista[0].get_titlu(), "Harry Potter")
        self.assertEqual(lista[0].get_descriere(), "bun")
        self.assertEqual(lista[0].get_gen(), "SF")


    def test_find_film_id(self):

        self.__film_repository.add_film(1, "Harry Potter", "bun", "SF")
        film = self.__film_repository.find_film_id(1)
        self.assertEqual(film.get_id(), 1)
        self.assertEqual(film.get_titlu(), "Harry Potter")
        self.assertEqual(film.get_descriere(), "bun")
        self.assertEqual(film.get_gen(), "SF")


    def test_find_film_titlu(self):

        self.__film_repository.add_film(1, "Harry Potter", "bun", "SF")
        film = self.__film_repository.find_film_titlu("Harry Potter")
        self.assertEqual(film.get_id(), 1)
        self.assertEqual(film.get_titlu(), "Harry Potter")
        self.assertEqual(film.get_descriere(), "bun")
        self.assertEqual(film.get_gen(), "SF")


    def test_find_film_descriere(self):

        self.__film_repository.add_film(1, "Harry Potter", "bun", "SF")
        film = self.__film_repository.find_film_descriere("bun")
        self.assertEqual(film.get_id(), 1)
        self.assertEqual(film.get_titlu(), "Harry Potter")
        self.assertEqual(film.get_descriere(), "bun")
        self.assertEqual(film.get_gen(), "SF")


    def test_delete_film(self):

        self.__film_repository.add_film(1, "Harry Potter", "bun", "SF")
        film = self.__film_repository.find_film_id(1)
        index = self.__film_repository.return_index(film)
        self.__film_repository.delete_film(index)
        film = self.__film_repository.find_film_id(1)
        self.assertIsNone(film)


    def test_modifica_film(self):
        self.__film_repository.add_film(1, "Harry Potter", "bun", "SF")
        film = self.__film_repository.find_film_id(1)
        self.__film_repository.film_nou(film, 5, "The Nun", "decent", "horror")
        self.assertEqual(film.get_id(), 5)
        self.assertEqual(film.get_titlu(), "The Nun")
        self.assertEqual(film.get_descriere(), "decent")
        self.assertEqual(film.get_gen(), "horror")







