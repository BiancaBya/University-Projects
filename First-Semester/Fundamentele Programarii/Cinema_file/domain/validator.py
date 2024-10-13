from Modificat.domain.filme import Film
from Modificat.domain.clienti import Client
from Modificat.repository.film_repository import FilmRepositoryInMemory, FilmRepoInFile
from Modificat.repository.client_repository import ClientRepositoryInMemory, ClientRepoInFile
from Modificat.repository.inchiriere_repository import InchirieriRepositoryInMemory, InchirieriRepoInFile


class FilmValidator:

    def __init__(self, film_repoditory : FilmRepoInFile):
        self.__film_repository = film_repoditory

    def validare_film(self, id : str, titlu : str, descriere : str, gen : str):
        if self.__film_repository.find_film_titlu(titlu):
            raise ValueError("Titlul deja exista!")
        if not id.isnumeric():
            raise ValueError("ID-ul trebuie sa fie un numar!")


class ClientValidator:

    def __init__(self, client_repository : ClientRepoInFile):
        self.__client_repository = client_repository

    def validare_client(self, id : str, nume : str, cnp : str):
        if self.__client_repository.find_client_id(id):
            raise ValueError("ID-ul exista deja!")
        if self.__client_repository.find_client_nume(nume):
            raise ValueError("Clientul exista deja!")
        if self.__client_repository.find_client_cnp(cnp):
            raise ValueError("CNP-ul exista deja!")
        if not id.isnumeric():
            raise ValueError("ID-ul trebuie sa fie un numar!")

        copie = str(cnp)
        if (len(copie) > 13 or len(copie) < 13):
            raise ValueError("CNP invalid!")


class IDFilmValidator:

    def __init__(self, film_repoditory : FilmRepoInFile):
        self.__film_repoditory = film_repoditory

    def validare_id(self, id):

        if not id.isnumeric():
            raise ValueError("Id-ul trebuie sa fie un numar!")


class IDClientValidator:

    def __init__(self, client_repository : ClientRepoInFile):
        self.__client_repository = client_repository

    def validare_id(self, id : str):
        if not id.isnumeric():
            raise ValueError("Id-ul trebuie sa fie un numar!")
        if not self.__client_repository.find_client_id(id):
            raise ValueError("Clientul nu exista!")


class IDFilmValidatorModificat:

    def __init__(self, film_repoditory : FilmRepoInFile):
        self.__film_repoditory = film_repoditory

    def validare_id(self, id : str, id_nou : str):

        if not id.isnumeric():
            raise ValueError("Id-ul filmului cautat trebuie sa fie un numar!")
        if not id_nou.isnumeric():
            raise ValueError("ID-ul filmului nou trebuie sa fie un numar!")


class IDClientValidatorModificat:

    def __init__(self, client_repository : ClientRepoInFile):
        self.__client_repository = client_repository

    def validare_id(self, id: str, id_nou: str, cnp_nou : str):

        if not id.isnumeric():
            raise ValueError("Id-ul clientului cautat trebuie sa fie un numar!")
        if not id_nou.isnumeric():
            raise ValueError("ID-ul clientului nou trebuie sa fie un numar!")
        copie = str(cnp_nou)
        if (len(copie) > 13 or len(copie) < 13):
            raise ValueError("CNP invalid!")

        if not self.__client_repository.find_client_id(id):
            raise ValueError("Clientul nu exista!")
        if self.__client_repository.find_client_id(id_nou) and id != id_nou:
            raise ValueError("ID-ul deja exista!")


class InchiriereValidator:

    def __init__(self, film_repoditory : FilmRepoInFile, client_repository : ClientRepoInFile, inchiriere_repository : InchirieriRepoInFile):
        self.__film_repoditory = film_repoditory
        self.__client_repository = client_repository
        self.__inchiriere_repository = inchiriere_repository

    def validare_inchiriere(self, id_client, id_film):

        filme = self.__film_repoditory.get_toate_filmele()
        film = self.__film_repoditory.find_film_id_recursiv(id_film, filme, 0)
        if not film:
            raise ValueError("Filmul nu exista!")
        client = self.__client_repository.find_client_id(id_client)
        if not client:
            raise ValueError("Clientul nu exista!")

        lista_inchirieri = self.__inchiriere_repository.get_toate_inchirierile()
        for inchiriere in lista_inchirieri:
            if inchiriere.get_client().get_id() == id_client and inchiriere.get_film().get_id() == id_film:
                raise ValueError ("Inchirierea a mai fost facuta o data!")


class ReturnareValidator:

    def __init__(self, inchiriere_repository: InchirieriRepoInFile):
        self.__inchiriere_repository = inchiriere_repository

    def validare_returnare(self, id_film, id_client):

        lista_inchirieri = self.__inchiriere_repository.get_toate_inchirierile()
        ok = False
        for inchiriere in lista_inchirieri:
            if inchiriere.get_client().get_id() == id_client and inchiriere.get_film().get_id() == id_film:
                ok = True
        if not ok:
            raise ValueError("Inchirierea nu exista!")


