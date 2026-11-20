from Cinema_nou.domain.client import Client

class ClientRepository:

    def __init__(self):
        self.__lista_clienti = []


    def find_client_id(self, id):
        """
        Returneaza clientul cu id-ul dat, None in caz ca nu exista
        :param id: id-ul clientului
        :return: clientul
        """
        for client in self.__lista_clienti:
            if client.get_id() == id:
                return client
        return None


    def find_client_nume(self, nume):
        """
        Returneaza clientul cu numele dat sau None in caz ca nu exista
        :param nume: numele clientului
        :return: clientul
        """
        for client in self.__lista_clienti:
            if client.get_nume() == nume:
                return client
        return None


    def find_client_cnp(self, cnp):
        """
        Returneaza clientul cu cnp-ul dat sau None in caz ca nu exista
        :param cnp: cnp-ul cautat
        :return: clientul
        """
        for client in self.__lista_clienti:
            if client.get_cnp() == cnp:
                return client
        return None


    def add_client(self, id, nume, cnp) -> None:
        """
        Adauga un client in lista de clienti
        :param id: id-ul clientului
        :param nume: numele clientului
        :param cnp: cnp-ul clientului
        :return: modifica lista de clienti prin adaugarea la sfarsit a clientului nou
        """
        client = Client(id, nume, cnp)
        self.__lista_clienti.append(client)


    def get_toti_clientii(self) -> list:
        return self.__lista_clienti


    def client_nou(self, client, id, nume, cnp):
        client.set_id(id)
        client.set_nume(nume)
        client.set_cnp(cnp)


    def return_index(self, client:Client):
        return self.__lista_clienti.index(client)


    def delete_client(self, index):
        self.__lista_clienti.pop(index)



