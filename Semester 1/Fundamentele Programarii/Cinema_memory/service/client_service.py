from Cinema_nou.domain.client import Client
from Cinema_nou.repository.client_repository import ClientRepository
from Cinema_nou.domain.validator import ClientValidator, IDClientValidator, IDClientValidatorModificat

class ClientService:

    def __init__(self, client_repository : ClientRepository):
        self.__client_repository = client_repository


    def get_toti_clientii(self):
        return self.__client_repository.get_toti_clientii()


    def adauga_client(self, id, nume, cnp):
        validator = ClientValidator(self.__client_repository)
        validator.validare_client(id, nume, cnp)
        self.__client_repository.add_client(id, nume, cnp)


    def sterge_client(self, id):
        validator = IDClientValidator(self.__client_repository)
        validator.validare_id(id)
        client = self.__client_repository.find_client_id(id)
        index = self.__client_repository.return_index(client)
        self.__client_repository.delete_client(index)


    def find_client_id(self, id):
        client = self.__client_repository.find_client_id(id)
        return client


    def find_client_nume(self, nume):
        client = self.__client_repository.find_client_nume(nume)
        return client


    def find_client_cnp(self, cnp):
        client = self.__client_repository.find_client_cnp(cnp)
        return client


    def modifica_client(self, id, id_nou, nume_nou, cnp_nou):
        validator = IDClientValidatorModificat(self.__client_repository)
        validator.validare_id(id, id_nou, cnp_nou)
        client = self.__client_repository.find_client_id(id)
        self.__client_repository.client_nou(client, id_nou, nume_nou, cnp_nou)


    def return_index(self, client : Client):
        return self.__client_repository.return_index(client)




