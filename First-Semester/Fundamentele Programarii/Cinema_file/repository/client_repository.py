from Modificat.domain.clienti import Client


class ClientRepositoryInMemory:

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


    def find_client_cnp_recursiv(self, cnp, clienti, index):
        """
        Returneaza clientul cu cnp-ul dat sau None in caz ca nu exista
        :param cnp: cnp-ul cautat
        :return: clientul
        """
        if index == len(clienti):
            return None
        client = clienti[index]
        if client.get_cnp() == cnp:
            return client
        else:
            return self.find_client_cnp_recursiv(cnp, clienti, index + 1)


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



class ClientRepoInFile(ClientRepositoryInMemory):

    def __init__(self, file_name):
        ClientRepositoryInMemory.__init__(self)
        self.__file_name = file_name
        self.load_from_file()


    def load_from_file(self):
        """
        Incarca datele despre persoane in fisier
        """
        with open(file = self.__file_name, mode = 'r') as client_file:
            lines = client_file.readlines()
            lines = [line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id, nume, cnp = line.split(',')
                id = id.strip()
                nume = nume.strip()
                cnp = cnp.strip()
                ClientRepositoryInMemory.add_client(self, id, nume, cnp)


    def write_to_file(self):
        """
        Scrie datele clientilor in fisier
        """
        clienti = ClientRepositoryInMemory.get_toti_clientii(self)
        clienti = [client.get_id() + ', ' + client.get_nume() + ', ' + client.get_cnp() for client in clienti]
        with open(file = self.__file_name, mode = 'w') as client_file:
            text_to_write = '\n'.join(clienti)
            client_file.write(text_to_write)


    def add_client(self, id, nume, cnp):
        """
        Adauga un client
        :return: se modifica fisierul de clienti
        """
        ClientRepositoryInMemory.add_client(self, id, nume, cnp)
        self.write_to_file()


    def delete_client(self, index):
        """
        Sterge un client din lista de clienti
        :param index: index-ul clientului de sters
        :return: se modifica fisierul de clienti
        """
        ClientRepositoryInMemory.delete_client(self, index)
        self.write_to_file()


    def client_nou(self, client, id, nume, cnp):
        """
        Modifica un client din lista de clienti
        :param client: clientul de modificat
        :param id: id-ul
        :param nume: numele
        :param cnp: cnp-ul
        :return: se modifica lista de clienti
        """
        ClientRepositoryInMemory.client_nou(self, client, id, nume, cnp)
        self.write_to_file()






