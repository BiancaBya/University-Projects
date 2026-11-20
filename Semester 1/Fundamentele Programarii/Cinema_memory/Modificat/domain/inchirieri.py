from Modificat.domain.filme import Film
from Modificat.domain.clienti import Client

class Inchirieri:
    def __init__(self, film : Film, client : Client):
        self.__film = film
        self.__client = client

    def get_film(self):
        return self.__film

    def get_client(self):
        return self.__client

    def __str__(self):
        return "\nClientul:\n" + self.__client.__str__() + "\n\ncu filmul:\n" + self.__film.__str__() + "\n"


    def __eq__(self, other):
        return self.__film.get_id() == other.get_film().get_id() and self.__client.get_id() == other.get_client().get_id()

