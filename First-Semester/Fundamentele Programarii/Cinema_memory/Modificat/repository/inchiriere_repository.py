from Modificat.domain.inchirieri import Inchirieri
from Modificat.domain.filme import Film
from Modificat.domain.clienti import Client
from Modificat.repository.film_repository import FilmRepositoryInMemory
from Modificat.repository.client_repository import ClientRepositoryInMemory

class InchirieriRepositoryInMemory:

    def __init__(self, film_repository: FilmRepositoryInMemory, client_repository: ClientRepositoryInMemory):
        self.__lista_inchirieri = []
        self.__film_repository = film_repository
        self.__client_repository = client_repository


    def get_toate_inchirierile(self):
        return self.__lista_inchirieri


    def add_inchiriere(self, film : Film, client : Client):
        inchiriere = Inchirieri(film, client)
        self.__lista_inchirieri.append(inchiriere)


    def delete_inchiriere(self, index):
        self.__lista_inchirieri.pop(index)


    def return_index(self, inchiriere : Inchirieri):
        return self.__lista_inchirieri.index(inchiriere)


    def find_inchiriere(self, id_client, id_film):
        for inchiriere in self.__lista_inchirieri:
            if inchiriere.get_film().get_id() == id_film and inchiriere.get_client().get_id() == id_client:
                return inchiriere
        return None



class InchirieriRepoInFile:
    """
    Repository care gestioneaza date din fisier
    """

    def __init__(self, file_name):
        self.__file_name = file_name


    def read_from_file(self):
        f = open(file = self.__file_name, mode = 'r')

        inchirieri = []
        lines = f.readlines()
        for index in range (0, len(lines), 2):
            linie_client = lines[index]
            linie_film = lines[index + 1]

            elements = linie_client.split(',')
            elements = [element.strip() for element in elements]
            id = elements[0]
            nume = elements[1]
            cnp = elements[2]
            client = Client(id, nume, cnp)

            elements = linie_film.split(',')
            elements = [element.strip() for element in elements]
            id = elements[0]
            titlu = elements[1]
            descriere = elements[2]
            gen = elements[3]
            film = Film(id, titlu, descriere, gen)

            inchiriere = Inchirieri(film, client)
            inchirieri.append(inchiriere)

        f.close()
        return inchirieri


    def write_to_file(self, lista_inchirieri):
        with open(file = self.__file_name, mode = 'w') as f:
            for inchiriere in lista_inchirieri:
                film = inchiriere.get_film()
                client = inchiriere.get_client()

                client_elements = (client.get_id(), client.get_nume(), client.get_cnp())
                client_elements = [str(element) for element in client_elements]
                line_client = ', '.join(client_elements) + '\n'

                film_elements = (film.get_id(), film.get_titlu(), film.get_descriere(), film.get_gen())
                film_elements = [str(element) for element in film_elements]
                line_film = ', '.join(film_elements) + '\n'

                f.write(line_client)
                f.write(line_film)


    def get_toate_inchirierile(self):
        return self.read_from_file()


    def add_inchiriere(self, film, client):
        inchiriere = Inchirieri(film, client)
        inchirieri = self.read_from_file()
        inchirieri.append(inchiriere)
        self.write_to_file(inchirieri)


    def delete_inchiriere(self, index):
        inchirieri = self.read_from_file()
        inchirieri.pop(index)
        self.write_to_file(inchirieri)


    def return_index(self, inchiriere : Inchirieri):
        inchirieri = self.read_from_file()
        return inchirieri.index(inchiriere)


    def find_inchiriere(self, id_client, id_film):
        inchirieri = self.read_from_file()
        for inchiriere in inchirieri:
            if inchiriere.get_film().get_id() == id_film and inchiriere.get_client().get_id() == id_client:
                return inchiriere
        return None




