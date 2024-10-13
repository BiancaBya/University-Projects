from Service.service_melodie import MelodieService
from Repository.repository_melodie import MelodieRepository
from Domain.melodie import Melodie

class Teste:

    def __init__(self, melodie_service : MelodieService):

        self.__melodie_service = melodie_service

    def modificare_testat(self):
        """
        Verifica daca modificarile se fac bine
        """
        titlu = "Gaga"
        artist = "Maria"
        gen = "Jazz"
        data = "10.11.2005"
        self.__melodie_service.modifica_melodie(titlu, artist, gen, data)
        lista = self.__melodie_service.get_melodii()
        assert lista[0].get_gen() == "Jazz"


    def random_testat(self):
        """
        Verificam daca fucntia de generat melodii random functioneaza
        """
        nr = 1
        lista_titluri = ["Jajaja", "GAgagag"]
        lista_artisti = ["Nanan", "KALALA"]
        self.__melodie_service.melodii_random(nr, lista_titluri, lista_artisti)
        lista = self.__melodie_service.get_melodii()
        assert lista[len(lista) - 1].get_titlu() in lista_titluri
        assert lista[len(lista) - 1].get_artist() in lista_artisti


    def testat_sortare(self):

        """
        Functia care valideaza daca fucntia de sortare functioneaza corect
        """

        melodie1 = Melodie("abc","gaga", "Pop", "01.01.1999")
        melodie2 = Melodie("abcd", "hsahs", "Rock", "12.12.2000")
        lista = []
        lista.append(melodie1)
        lista.append(melodie2)
        self.__melodie_service.merge_sort(lista)
        assert lista[0].get_gen() == "Pop"


    def run(self):

        self.modificare_testat()
        self.random_testat()
        self.testat_sortare()




