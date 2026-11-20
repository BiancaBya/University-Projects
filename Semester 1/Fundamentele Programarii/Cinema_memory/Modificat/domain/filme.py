class Film:

    def __init__(self, id, titlu, descriere, gen):
        self.__id = id
        self.__titlu = titlu
        self.__descriere = descriere
        self.__gen = gen

    def __eq__(self, other):
        return self.__id == other.get_id() and self.__gen == other.get_gen() and \
            self.__titlu == other.get_titlu() and self.__descriere == other.get_descriere()

    def __str__(self):
        return "ID: " + str(self.get_id()) + "\nTitlu: " + str(self.get_titlu()) + "\nDescriere: " + str(self.get_descriere()) + "\nGen: " + str(self.get_gen())

    def get_id(self):
        return self.__id

    def get_titlu(self):
        return self.__titlu

    def get_descriere(self):
        return self.__descriere

    def get_gen(self):
        return self.__gen

    def set_id(self, id_nou):
        self.__id = id_nou

    def set_titlu(self, titlu_nou):
        self.__titlu = titlu_nou

    def set_descriere(self, descriere_noua):
        self.__descriere = descriere_noua

    def set_gen(self, gen_nou):
        self.__gen = gen_nou


