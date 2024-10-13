from Cinema_nou.service.film_service import FilmService
from Cinema_nou.service.client_service import ClientService
from Cinema_nou.service.inchiriere_service import InchiriereService
from Cinema_nou.domain.client import Client
from random import randint, choice


class Consola:

    def __init__(self, film_service : FilmService, client_service : ClientService, inchirieri_service : InchiriereService):
        self.__film_service = film_service
        self.__client_service = client_service
        self.__inchirieri_service = inchirieri_service


    def print_menu(self):
        print("1. Adauga film")
        print("2. Adauga client")
        print("3. Afiseaza lista de filme")
        print("4. Afiseaza lista de clienti")
        print("5. Sterge film")
        print("6. Sterge client")
        print("7. Modifica film")
        print("8. Modifica client")
        print("9. Cauta film")
        print("10. Cauta client")
        print("11. Inchiriere film de catre un client")
        print("12. Returnare film de catre un client")
        print("13. Afisare clientii cu filme inchiriate ordonati dupa nume")
        print("14. Afisare clientii cu filme inchiriate ordonati dupa nr de filme inchiriate")
        print("15. Afisare cele mai inchiriate filme")
        print("16. Afisare primii 30% clienti cu cele mai multe filme inchiriate")
        print("17. Afisare lista inchirieri")
        print("18. Adauga clienti random")
        print("19. Break")


    def print_lista_filme(self, lista_filme):
        for film in lista_filme:
            print(film, "\n")


    def print_lista_clienti(self,lista_clienti):
        for client in lista_clienti:
            print(client, "\n")


    def adauga_film(self):
        id = input("ID: ")
        titlu = input("titlu: ")
        descriere = input("descriere: ")
        gen = input("gen: ")
        try:
            self.__film_service.adaugare_film(id, titlu, descriere, gen)
        except ValueError as ve:
            print("\nEroare:")
            print(ve, '\n')


    def adauga_client(self):
        id = input("ID: ")
        nume = input("nume: ")
        cnp = input("CNP: ")
        try:
            self.__client_service.adauga_client(id, nume, cnp)
        except ValueError as ve:
            print("\nEroare:")
            print(ve, '\n')


    def cauta_film(self):
        optiune = input("Cauta film dupa:\n 1.Id \n 2.Titlu \n 3. Descriere \n")
        try:
            optiune = int(optiune)
            if optiune == 1:
                id = input("ID: ")
                film = self.__film_service.find_film_id(id)
            elif optiune == 2:
                titlu = input("titlu: ")
                film = self.__film_service.find_film_titlu(titlu)
            elif optiune == 3:
                descriere = input("descriere: ")
                film = self.__film_service.find_film_descriere(descriere)
            else:
                print("\nOptiune invalida!\n")
                return
            if film:
                print("\nFilmul cautat este:")
                print(film, '\n')
            else:
                print("\nFilmul nu exista!\n")
        except ValueError:
            print("\nOptiune invalida!\n")


    def cauta_client(self):
        optiune = input("Cauta client dupa:\n 1.Id \n 2.Nume \n 3.CNP \n")
        try:
            optiune = int(optiune)
            if optiune == 1:
                id = input("ID: ")
                client = self.__client_service.find_client_id(id)
            elif optiune == 2:
                nume = input("nume: ")
                client = self.__client_service.find_client_nume(nume)
            elif optiune == 3:
                cnp = input("CNP: ")
                client = self.__client_service.find_client_cnp(cnp)
            else:
                print("\nOptiune invalida!\n")
                return
            if client:
                print("\nClientul cautat este:")
                print(client, '\n')
            else:
                print("\nClientul nu exista!\n")
        except ValueError:
            print("\nOptiune invalida!\n")


    def sterge_film(self):
        id = input("ID-ul filmului de sters: ")
        try:
            self.__film_service.sterge_film(id)
            print("Noua lista este:")
            self.print_lista_filme(self.__film_service.get_toate_filmele())
        except ValueError as ve:
            print("\nEroare!")
            print(ve, '\n')


    def sterge_client(self):
        id = input("ID-ul clientului de sters: ")
        try:
            self.__client_service.sterge_client(id)
            print("Noua lista este:")
            self.print_lista_clienti(self.__client_service.get_toti_clientii())
        except ValueError as ve:
            print("\nEroare!")
            print(ve, '\n')


    def modifica_film(self):
        id = input("ID-ul filmului de modificat: ")
        id_nou = input("ID nou: ")
        titlu_nou = input("titlu nou: ")
        descriere_noua = input("descriere noua: ")
        gen_nou = input("gen nou: ")
        try:
            self.__film_service.modifica_film(id, id_nou, titlu_nou, descriere_noua, gen_nou)
            print("Noua lista de filme este:")
            self.print_lista_filme(self.__film_service.get_toate_filmele())
        except ValueError as ve:
            print("\nEroare!")
            print(ve, '\n')


    def modifica_client(self):
        id = input("ID-ul clientului de modificat: ")
        id_nou = input("ID nou: ")
        nume_nou = input("nume nou: ")
        cnp_nou = input("CNP nou: ")
        try:
            self.__client_service.modifica_client(id, id_nou, nume_nou, cnp_nou)
            print("Noua lista de clienti este:")
            self.print_lista_clienti(self.__client_service.get_toti_clientii())
        except ValueError as ve:
            print("\nEroare!")
            print(ve, '\n')


    def inchiriere_film(self):
        id_client = input("ID-ul clientului care inchiriaza: ")
        id_film = input("ID-ul filmului inchiriat: ")
        try:
            self.__inchirieri_service.inchiriere_film(id_client, id_film)
        except ValueError as ve:
            print("\nEroare!")
            print(ve, '\n')


    def print_lista_inchirieri(self, lista_inchirieri):
        for inchiriere in lista_inchirieri:
            print(inchiriere)


    def returnare_film(self):
        id_client = input("ID-ul clientului care returneaza: ")
        id_film = input("ID-ul filmului returnat: ")
        try:
            self.__inchirieri_service.returnare_film(id_client, id_film)
            print("Noua lista de inchirieri este:")
            self.print_lista_inchirieri(self.__inchirieri_service.get_toate_inchirierile())
        except ValueError as ve:
            print("\nEroare!")
            print(ve, '\n')


    def afisare_clienti_ordonati_nume(self):
        lista_inchirieri = self.__inchirieri_service.clienti_ordonati_nume()

        print("Clientii cu filme inchiriate ordonati dupa nume sunt:")
        for inchiriere in lista_inchirieri:
            print(inchiriere, '\n')


    def afisare_clienti_ordonati_nr_filme(self):
        lista_tuple = self.__inchirieri_service.clienti_ordonati_filme()

        for index in lista_tuple:
            print("Clientul:")
            print(index[0])
            print("cu numarul de filme inchiriate: ", index[1], '\n')


    def afisare_filme_nr_inchirieri(self):
        lista_tuple = self.__inchirieri_service.filme_ordonate_inchirieri()

        for index in lista_tuple:
            print("Filmul:")
            print(index[0])
            print("inchiriat de: ", index[1], " ori",'\n')


    def afisare_primii_30_clienti(self):
        lista_tuple, lungime = self.__inchirieri_service.primii_clienti_30()

        for index in range(0, lungime + 1):
            print("Clientul:")
            print(lista_tuple[index][0])
            print("cu numarul de filme inchiriate: ", lista_tuple[index][1], '\n')


    def clienti_random(self):
        lista_nume = ["Gigel", "Ana", "Maria", "Ion", "Gigi", "Bubu", "Carol", "Florin", "Ioana", "Denisa", "Bianca", "Alexandru", "Andrei"]
        n = int(input("Numarul de clienti generati random: "))
        for index in range (0,n):
            id = randint(1, 100)
            nume = choice(lista_nume)
            cnp = randint(1000000000000, 9999999999999)
            client = Client(id, nume, cnp)
            print("Clientul ", index+1, " este:\n",client, '\n')


    def run(self):

        while True:
            self.print_menu()
            option = input(">")
            option = option.strip()
            if option == '1':
                self.adauga_film()
            elif option == '2':
                self.adauga_client()
            elif option == '3':
                self.print_lista_filme(self.__film_service.get_toate_filmele())
            elif option == '4':
                self.print_lista_clienti(self.__client_service.get_toti_clientii())
            elif option == '5':
                self.sterge_film()
            elif option == '6':
                self.sterge_client()
            elif option == '7':
                self.modifica_film()
            elif option == '8':
                self.modifica_client()
            elif option == '9':
                self.cauta_film()
            elif option == '10':
                self.cauta_client()
            elif option == '11':
                self.inchiriere_film()
            elif option == '12':
                self.returnare_film()
            elif option == '13':
                self.afisare_clienti_ordonati_nume()
            elif option == '14':
                self.afisare_clienti_ordonati_nr_filme()
            elif option == '15':
                self.afisare_filme_nr_inchirieri()
            elif option == '16':
                self.afisare_primii_30_clienti()
            elif option == '17':
                self.print_lista_inchirieri(self.__inchirieri_service.get_toate_inchirierile())
            elif option == '18':
                self.clienti_random()
            elif option == '19':
                break
            else:
                raise ValueError ("Optiune invalida!")









