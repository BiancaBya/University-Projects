class Tractor:

    def __init__(self, id, denumire, pret, model, data):
        self.__id = id
        self.__denumire = denumire
        self.__pret = pret
        self.__model = model
        self.__data = data


    def __str__(self):
        return "ID: " + str(self.get_id()) + '\n' + "Denumire: " + str(self.get_denumire()) + '\n' + "Pret: " + str(self.get_pret()) + '\n' + "Model: " + str(self.get_model()) + '\n' + "Data: " + str(self.get_data()) + '\n'


    def __eq__(self, other):
        return self.__id == other.__id and self.__denumire == other.__denumire and self.__pret == other.__pret and self.__model == other.__model and self.__data == other.__data



    def get_id(self):
        return self.__id

    def get_denumire(self):
        return self.__denumire

    def get_pret(self):
        return self.__pret

    def get_model(self):
        return self.__model

    def get_data(self):
        return self.__data


    def set_id(self, id):
        self.__id = id

    def set_denumire(self, denumire):
        self.__denumire = denumire

    def set_pret(self, pret):
        self.__pret = pret

    def set_model(self, model):
        self.__model = model

    def set_data(self, data):
        self.__data = data



