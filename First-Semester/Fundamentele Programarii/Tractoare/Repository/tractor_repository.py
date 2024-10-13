from Domain.tractor import Tractor
from datetime import date

class TractorRepository:

    """
    Repository in finiser
    """

    def __init__(self, file_name):
        self.__file_name = file_name


    def read_from_file(self):

        f = open(self.__file_name, "r")

        tractoare = []
        lines = f.readlines()
        for line in lines:

            elements = line.split(",")                        # sa facem lista de elemente
            elements = [element.strip() for element in elements] # sa nu avem spatii
            id = elements[0]
            denumire = elements[1]
            pret = elements[2]
            model = elements[3]
            zi, luna, an = elements[4].split(":")
            data = date(int(an), int(luna), int(zi))

            tractor = Tractor(id, denumire, pret, model, data)
            tractoare.append(tractor)

        f.close()
        return tractoare


    def write_to_file(self, tractoare : list):

        with open(self.__file_name, "w") as f:   # ca sa nu mai punem fclose
            for tractor in tractoare:
                data = str(tractor.get_data())
                an, luna, zi = data.split("-")
                data = zi + ":" + luna + ":" + an
                elemente_tractor = (tractor.get_id(), tractor.get_denumire(), tractor.get_pret(), tractor.get_model(), data)
                elemente_tractor = [str(element) for element in elemente_tractor]
                line = ', '.join(elemente_tractor) + '\n'
                f.write(line)



    def get_toate_tractoarele(self):

        return self.read_from_file()


    def adaugare_tractor(self, tractor : Tractor):

        lista_tractoare = self.get_toate_tractoarele()
        lista_tractoare.append(tractor)
        self.write_to_file(lista_tractoare)


    def sterge_tractor(self, tractor):

        lista_tractoare = self.get_toate_tractoarele()
        lista_tractoare.remove(tractor)
        self.write_to_file(lista_tractoare)









