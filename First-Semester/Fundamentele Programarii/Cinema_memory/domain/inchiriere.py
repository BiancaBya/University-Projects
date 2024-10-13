from Cinema_nou.domain.film import Film
from Cinema_nou.domain.client import Client

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




