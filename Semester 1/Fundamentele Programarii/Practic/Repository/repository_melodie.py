from Domain.melodie import Melodie
from datetime import date

class MelodieRepository:

    """
    Repository in fisier
    """

    def __init__(self, file_name):

        self.__file_name = file_name


    def read_from_file(self):

        """
        Functia care citeste melodiile din fisier
        :return: lista de melodii
        """

        f = open(file = self.__file_name, mode = "r")
        melodii = []
        lines = f.readlines()

        for line in lines:

            elements = line.split(',')
            elements = [element.strip() for element in elements]
            titlu = elements[0]
            artist = elements[1]
            gen = elements[2]
            zi, luna, an = elements[3].split('.')
            data = date(int(an), int(luna), int(zi))
            melodie = Melodie(titlu, artist, gen, data)

            melodii.append(melodie)

        f.close()
        return melodii



    def write_to_file(self, lista_melodii):

        """
        Functia care scrie o noua lista de melodii in fisier
        :param lista_melodii: lista de scris
        :return: -
        """

        with open(self.__file_name, "w") as f:
            for melodie in lista_melodii:

                data = str(melodie.get_data())
                an, luna, zi = data.split("-")
                data = zi + "." + luna + "." + an
                elemente_melodie = (melodie.get_titlu(), melodie.get_artist(), melodie.get_gen(), data)
                elemente_melodie = [str(element) for element in elemente_melodie]
                line = ', '.join(elemente_melodie) + '\n'
                f.write(line)


    def get_toate_melodiile(self):
        return self.read_from_file()



