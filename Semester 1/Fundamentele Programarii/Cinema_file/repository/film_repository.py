from Modificat.domain.filme import Film


class FilmRepositoryInMemory:

    def __init__(self):
        self.__lista_filme = []


    def find_film_titlu(self, titlu):
        """
        Returneaza filmul cu titlul dat, None in caz ca nu exista
        :param titlu: titlul filmului
        :return: filmul
        """
        for film in self.__lista_filme:
            if film.get_titlu() == titlu:
                return film
        return None


    def find_film_id_recursiv(self, id, filme : list, index):
        """
        Returneaza filmul cu id-ul dat, None in caz ca nu exista
        :param id: id-ul filmului
        :return: filmul
        """
        if (index == len(filme)):
            return None
        film = filme[index]
        if film.get_id() == id:
            return film
        else:
            return self.find_film_id_recursiv(id, filme, index + 1)


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



class FilmRepoInFile:
    """
    Repository care gestioneaza date fin fisier
    """

    def __init__(self, file_name):
        self.__file_name = file_name


    def read_from_file(self):
        f = open(file = self.__file_name, mode = 'r')

        filme = []
        lines = f.readlines()
        for line in lines:
            elements = line.split(',')
            elements = [element.strip() for element in elements]
            id = elements[0]
            titlu = elements[1]
            descriere = elements[2]
            gen = elements[3]
            film = Film(id, titlu, descriere, gen)
            filme.append(film)

        f.close()
        return filme


    def write_to_file(self, lista_filme):
        with open(file = self.__file_name, mode = 'w') as f:
            for film in lista_filme:
                film_elements = (film.get_id(), film.get_titlu(), film.get_descriere(), film.get_gen())
                film_elements = [str(element) for element in film_elements]
                line = ', '.join(film_elements) + '\n'
                f.write(line)


    def add_film(self, id, titlu, descriere, gen):
        """
        Citeste lista de filme care se afla in fisier, adauga la acea lista noul film si dupa o scrie in fisier
        :param id: id
        :param titlu: titlu
        :param descriere: descriere
        :param gen: gen
        :return: None
        """
        film = Film(id, titlu, descriere, gen)
        filme = self.read_from_file()
        filme.append(film)
        self.write_to_file(filme)


    def find_film_titlu(self, titlu):
        """
        Gaseste filmul cu titlul dat
        :param titlu: titlul
        :return: filmul
        """
        filme = self.read_from_file()
        for film in filme:
            if film.get_titlu() == titlu:
                return film
        return None


    def find_film_descriere(self, descriere):
        """
        Gaseste filmul cu descrierea data
        :param descriere: descrierea
        :return: filmul
        """
        filme = self.read_from_file()
        for film in filme:
            if film.get_descriere() == descriere:
                return film
        return None


    def find_film_id_recursiv(self, id, filme, index):
        """
        Gaseste filmul cu id-ul dat
        :param id: id-ul
        :return: filmul
        """
        if index == len(filme):
            return None
        film = filme[index]
        if film.get_id() == id:
            return film
        else:
            return self.find_film_id_recursiv(id, filme, index + 1)



    def delete_film(self, index):
        """
        Sterge filmul cu index-ul dat
        :param index: index-ul
        :return: None
        """
        filme = self.read_from_file()
        filme.pop(index)
        self.write_to_file(filme)


    def get_toate_filmele(self):

        return self.read_from_file()


    def film_nou(self, film, id, titlu, descriere, gen):

        filme = self.read_from_file()
        index = filme.index(film)
        film.set_id(id)
        film.set_titlu(titlu)
        film.set_descriere(descriere)
        film.set_gen(gen)
        filme[index] = film
        self.write_to_file(filme)


    def return_index(self, film):

        filme = self.read_from_file()
        index = filme.index(film)
        return index







