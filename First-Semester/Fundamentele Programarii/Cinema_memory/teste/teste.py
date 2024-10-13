from Cinema_nou.domain.film import Film
from Cinema_nou.domain.client import Client
from Cinema_nou.repository.film_repository import FilmRepository
from Cinema_nou.repository.client_repository import ClientRepository
from Cinema_nou.repository.inchiriere_repository import InchirieriRepository

class Teste:

    def __init__(self, film_repository : FilmRepository, client_repository : ClientRepository, inchirieri_repository : InchirieriRepository):
        self.__film_repository = film_repository
        self.__client_repository = client_repository
        self.__inchirieri_repository = inchirieri_repository


    def test_create_film(self):
        film = Film(123, "Harry Potter", "Film bun", "Sf")
        assert film.get_id() == 123
        assert film.get_titlu() == "Harry Potter"
        assert film.get_descriere() == "Film bun"
        assert film.get_gen() == "Sf"


    def test_create_client(self):
        client = Client(10, "Ion Ionel", 6041017330232)
        assert client.get_id() == 10
        assert client.get_nume() == "Ion Ionel"
        assert client.get_cnp() == 6041017330232


    def test_adauga_film(self):
        lista_testat = FilmRepository()
        lista_testat.add_film(1, "Harry Potter", "bun", "SF")
        lista = lista_testat.get_toate_filmele()
        assert lista[0].get_id() == 1
        assert lista[0].get_titlu() == "Harry Potter"
        assert lista[0].get_descriere() == "bun"
        assert lista[0].get_gen() == "SF"


    def test_adauga_client(self):
        lista_testat = ClientRepository()
        lista_testat.add_client(10, "Ion Ionel", 6041017330232)
        lista = lista_testat.get_toti_clientii()
        assert lista[0].get_id() == 10
        assert lista[0].get_nume() == "Ion Ionel"
        assert lista[0].get_cnp() == 6041017330232


    def test_cauta_film_id(self):
        lista_testat = FilmRepository()
        lista_testat.add_film(1, "Harry Potter", "bun", "SF")
        film = lista_testat.find_film_id(1)
        assert film.get_id() == 1
        assert film.get_titlu() == "Harry Potter"
        assert film.get_descriere() == "bun"
        assert film.get_gen() == "SF"


    def test_cauta_film_titlu(self):
        lista_testat = FilmRepository()
        lista_testat.add_film(1, "Harry Potter", "bun", "SF")
        film = lista_testat.find_film_titlu("Harry Potter")
        assert film.get_id() == 1
        assert film.get_titlu() == "Harry Potter"
        assert film.get_descriere() == "bun"
        assert film.get_gen() == "SF"


    def test_cauta_film_descriere(self):
        lista_testat = FilmRepository()
        lista_testat.add_film(1, "Harry Potter", "bun", "SF")
        film = lista_testat.find_film_descriere("bun")
        assert film.get_id() == 1
        assert film.get_titlu() == "Harry Potter"
        assert film.get_descriere() == "bun"
        assert film.get_gen() == "SF"


    def test_cauta_client_id(self):
        lista_testat = ClientRepository()
        lista_testat.add_client(10, "Ion Ionel", 6041017330232)
        client = lista_testat.find_client_id(10)
        assert client.get_id() == 10
        assert client.get_nume() == "Ion Ionel"
        assert client.get_cnp() == 6041017330232


    def test_cauta_client_nume(self):
        lista_testat = ClientRepository()
        lista_testat.add_client(10, "Ion Ionel", 6041017330232)
        client = lista_testat.find_client_nume("Ion Ionel")
        assert client.get_id() == 10
        assert client.get_nume() == "Ion Ionel"
        assert client.get_cnp() == 6041017330232


    def test_cauta_client_cnp(self):
        lista_testat = ClientRepository()
        lista_testat.add_client(10, "Ion Ionel", 6041017330232)
        client = lista_testat.find_client_cnp(6041017330232)
        assert client.get_id() == 10
        assert client.get_nume() == "Ion Ionel"
        assert client.get_cnp() == 6041017330232


    def test_sterge_film(self):
        lista_testat = FilmRepository()
        lista_testat.add_film(1, "Harry Potter", "bun", "SF")
        lista_testat.add_film(2, "Baban", "bunicel", "horror")
        lista_testat.delete_film(0)
        lista = lista_testat.get_toate_filmele()
        assert lista[0].get_id() == 2


    def test_sterge_client(self):
        lista_testat = ClientRepository()
        lista_testat.add_client(10, "Ion Ionel", 6041017330232)
        lista_testat.add_client(11, "mircea", 6041017320231)
        lista_testat.delete_client(0)
        lista = lista_testat.get_toti_clientii()
        assert lista[0].get_nume() == "mircea"


    def test_modifica_film(self):
        lista_testat = FilmRepository()
        lista_testat.add_film(1, "Harry Potter", "bun", "SF")
        lista_testat.add_film(2, "Baban", "bunicel", "horror")
        film = lista_testat.find_film_id(1)
        lista_testat.film_nou(film, 5, "The Nun", "decent", "horror")
        lista = lista_testat.get_toate_filmele()
        assert lista[0].get_id() == 5
        assert lista[0].get_titlu() == "The Nun"
        assert lista[0].get_descriere() == "decent"
        assert lista[0].get_gen() == "horror"


    def test_modifica_client(self):
        lista_testat = ClientRepository()
        lista_testat.add_client(10, "Ion Ionel", 6041017330232)
        lista_testat.add_client(11, "mircea", 6041017320231)
        client = lista_testat.find_client_id(11)
        lista_testat.client_nou(client, 100, "Gigel", 2041017330232)
        lista = lista_testat.get_toti_clientii()
        assert lista[1].get_id() == 100
        assert lista[1].get_nume() == "Gigel"
        assert lista[1].get_cnp() == 2041017330232


    def test_add_inchiriere(self):
        film = Film(123, "Harry Potter", "Film bun", "Sf")
        client = Client(10, "Ion Ionel", 6041017330232)
        self.__inchirieri_repository.add_inchiriere(film, client)
        lista = self.__inchirieri_repository.get_toate_inchirierile()
        assert lista[0].get_client().get_nume() == "Ion Ionel"
        assert lista[0].get_film().get_titlu() == "Harry Potter"


    def test_delete_inchiriere(self):
        lista = self.__inchirieri_repository.get_toate_inchirierile()
        lista.pop()
        film1 = Film(123, "Harry Potter", "Film bun", "Sf")
        client1 = Client(10, "Ion Ionel", 6041017330232)
        film2 = Film(321, "The Nun", "Film decent", "Horror")
        client2 = Client(30, "Gigel", 6041018637832)
        self.__inchirieri_repository.add_inchiriere(film1, client1)
        self.__inchirieri_repository.add_inchiriere(film2, client2)
        self.__inchirieri_repository.delete_inchiriere(0)
        lista = self.__inchirieri_repository.get_toate_inchirierile()
        assert lista[0].get_client().get_nume() == "Gigel"
        assert lista[0].get_film().get_titlu() == "The Nun"


    def test_find_inchiriere(self):
        lista = self.__inchirieri_repository.get_toate_inchirierile()
        lista.pop()
        film1 = Film(123, "Harry Potter", "Film bun", "Sf")
        client1 = Client(10, "Ion Ionel", 6041017330232)
        film2 = Film(321, "The Nun", "Film decent", "Horror")
        client2 = Client(30, "Gigel", 6041018637832)
        self.__inchirieri_repository.add_inchiriere(film1, client1)
        self.__inchirieri_repository.add_inchiriere(film2, client2)
        inchiriere = self.__inchirieri_repository.find_inchiriere(10, 123)
        assert inchiriere.get_client().get_nume() == "Ion Ionel"
        assert inchiriere.get_film().get_titlu() == "Harry Potter"


    def run_teste(self):
        self.test_create_film()
        self.test_create_client()
        self.test_adauga_film()
        self.test_adauga_client()
        self.test_sterge_film()
        self.test_sterge_client()
        self.test_modifica_film()
        self.test_modifica_client()
        self.test_cauta_film_id()
        self.test_cauta_film_titlu()
        self.test_cauta_film_descriere()
        self.test_cauta_client_id()
        self.test_cauta_client_nume()
        self.test_cauta_client_cnp()
        self.test_add_inchiriere()
        self.test_delete_inchiriere()
        self.test_find_inchiriere()


