from Cinema_nou.domain.film import Film
from Cinema_nou.repository.film_repository import FilmRepository
from Cinema_nou.domain.validator import FilmValidator, IDFilmValidator, IDFilmValidatorModificat

class FilmService:

    def __init__(self, film_repository : FilmRepository):
        self.__film_repository = film_repository


    def get_toate_filmele(self):
        return self.__film_repository.get_toate_filmele()


    def adaugare_film(self, id, titlu, descriere, gen):
        validator = FilmValidator(self.__film_repository)
        validator.validare_film(id, titlu, descriere, gen)
        self.__film_repository.add_film(id, titlu, descriere, gen)


    def sterge_film(self, id):
        validator = IDFilmValidator(self.__film_repository)
        validator.validare_id(id)
        film = self.__film_repository.find_film_id(id)
        index = self.__film_repository.return_index(film)
        self.__film_repository.delete_film(index)


    def find_film_id(self, id):
        film = self.__film_repository.find_film_id(id)
        return film


    def find_film_titlu(self, titlu):
        film = self.__film_repository.find_film_titlu(titlu)
        return film


    def find_film_descriere(self, descriere):
        film = self.__film_repository.find_film_descriere(descriere)
        return film


    def modifica_film(self, id, id_nou, titlu_nou, descriere_noua, gen_nou):
        validator = IDFilmValidatorModificat(self.__film_repository)
        validator.validare_id(id, id_nou)
        film = self.__film_repository.find_film_id(id)
        self.__film_repository.film_nou(film, id_nou, titlu_nou, descriere_noua, gen_nou)


    def return_index(self, film : Film):
        return self.__film_repository.return_index(film)








