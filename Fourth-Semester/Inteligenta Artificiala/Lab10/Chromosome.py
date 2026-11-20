import random

class Chromosome:
  def __init__(self, problParam=None):
    self.__problParam = problParam
    self.__fitness = 0.0
    self.__repres = self._initialize_representation()

  def _initialize_representation(self):
    num_nodes = self.__problParam['num_nodes']
    max_comms = self.__problParam.get('max_comms', num_nodes // 2)
    return [random.randint(0, max_comms) for _ in range(num_nodes)]

  @property
  def repres(self):
    return self.__repres

  @property
  def fitness(self):
    return self.__fitness

  @repres.setter
  def repres(self, l=[]):
    self.__repres = l

  @fitness.setter
  def fitness(self, fit=0.0):
    self.__fitness = fit

  def crossover(self, other):
    r = random.randint(0, len(self.__repres) - 1)
    newrepres = self.__repres[:r] + other.__repres[r:]
    offspring = Chromosome(self.__problParam)
    offspring.repres = newrepres
    return offspring

  def mutation(self):
    pos = random.randint(0, len(self.__repres) - 1)
    max_comms = self.__problParam.get('max_comms', len(self.__repres) // 2)
    self.__repres[pos] = random.randint(0, max_comms)

  def __str__(self):
    return f"Chromo: {self.__repres} has fit: {self.__fitness:.4f}"

  def __repr__(self):
    return self.__str__()

  def __eq__(self, other):
    return self.__repres == other.__repres and self.__fitness == other.__fitness
