from unittest import TestCase
from Cinema_nou.domain.client import Client
from Cinema_nou.repository.client_repository import ClientRepository

class TestClient(TestCase):

    def setUp(self):
        """
        runs before all test methods
        :return:
        """
        self.__client = Client(10, "Ion Ionel", 6041017330232)
        self.__client_repository = ClientRepository()


    def test_create_client(self):

        self.assertEqual(self.__client.get_id(), 10)
        self.assertEqual(self.__client.get_nume(), "Ion Ionel")
        self.assertEqual(self.__client.get_cnp(), 6041017330232)


    def test_add_client(self):

        self.__client_repository.add_client(10, "Ion Ionel", 6041017330232)
        lista = self.__client_repository.get_toti_clientii()
        self.assertEqual(lista[0].get_id(), 10)
        self.assertEqual(lista[0].get_nume(), "Ion Ionel")
        self.assertEqual(lista[0].get_cnp(), 6041017330232)


    def test_find_client_id(self):

        self.__client_repository.add_client(10, "Ion Ionel", 6041017330232)
        client = self.__client_repository.find_client_id(10)
        self.assertEqual(client.get_id(), 10)
        self.assertEqual(client.get_nume(), "Ion Ionel")
        self.assertEqual(client.get_cnp(), 6041017330232)


    def test_find_client_nume(self):

        self.__client_repository.add_client(10, "Ion Ionel", 6041017330232)
        client = self.__client_repository.find_client_nume("Ion Ionel")
        self.assertEqual(client.get_id(), 10)
        self.assertEqual(client.get_nume(), "Ion Ionel")
        self.assertEqual(client.get_cnp(), 6041017330232)


    def test_find_client_cnp(self):

        self.__client_repository.add_client(10, "Ion Ionel", 6041017330232)
        client = self.__client_repository.find_client_cnp(6041017330232)
        self.assertEqual(client.get_id(), 10)
        self.assertEqual(client.get_nume(), "Ion Ionel")
        self.assertEqual(client.get_cnp(), 6041017330232)


    def test_delete_client(self):

        self.__client_repository.add_client(10, "Ion Ionel", 6041017330232)
        client = self.__client_repository.find_client_id(10)
        index = self.__client_repository.return_index(client)
        self.__client_repository.delete_client(index)
        client = self.__client_repository.find_client_id(10)
        self.assertIsNone(client)


    def test_modifica_client(self):

        self.__client_repository.add_client(10, "Ion Ionel", 6041017330232)
        client = self.__client_repository.find_client_id(10)
        self.__client_repository.client_nou(client, 100, "Gigel", 2041017330232)
        self.assertEqual(client.get_id(), 100)
        self.assertEqual(client.get_nume(), "Gigel")
        self.assertEqual(client.get_cnp(), 2041017330232)



