from Cinema_nou.domain.film import Film

class FilmRepository:

    def __init__(self):
        self.__lista_filme = []


    def find_film_titlu(self,titlu):
        """
        Returneaza filmul cu titlul dat, None in caz ca nu exista
        :param titlu: titlul filmului
        :return: filmul
        """
        for film in self.__lista_filme:
            if film.get_titlu() == titlu:
                return film
        return None


    def find_film_id(self,id):
        """
        Returneaza filmul cu id-ul dat, None in caz ca nu exista
        :param id: id-ul filmului
        :return: filmul
        """
        for film in self.__lista_filme:
            if film.get_id() == id:
                return film
        return None


    def find_film_descriere(self, descriere):
        """
        Returneaza filmul cu descrierea data sau None in caz ca nu exista
        :param descriere: descrierea cautata
        :return: filmul
        """
        for film in self.__lista_filme:
            if film.get_descriere() == descriere:
                return film
        return None


    def add_film(self, id, titlu, descriere, gen) -> None:
        """
        Modifica lista de filme adaugand la final noul film
        :param id: id-ul filmului
        :param titlu: titlul filmului
        :param descriere: descrierea filmului
        :param gen: genul filmului
        :return: modifica lista de filme prin adaugarea la sfarsit a noului film
        """
        film = Film(id, titlu, descriere, gen)
        self.__lista_filme.append(film)


    def get_toate_filmele(self) -> list:
        return self.__lista_filme


    def film_nou(self, film, id, titlu, descriere, gen):
        film.set_id(id)
        film.set_titlu(titlu)
        film.set_descriere(descriere)
        film.set_gen(gen)


    def return_index(self, film:Film):
        return self.__lista_filme.index(film)


    def delete_film(self, index):
        self.__lista_filme.pop(index)





