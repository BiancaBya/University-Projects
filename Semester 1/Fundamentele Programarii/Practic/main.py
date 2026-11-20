from Ui.consola import Consola
from Repository.repository_melodie import MelodieRepository
from Service.service_melodie import MelodieService
from Domain.Validator import Validator
from Teste.teste import Teste

melodie_repository = MelodieRepository("Data/melodii.txt")
validator = Validator(melodie_repository)
melodie_service = MelodieService(melodie_repository)
runner = Consola(melodie_service, validator)

repository_test = MelodieRepository("Teste/melodii_test.txt")
service_testat = MelodieService(repository_test)
tester = Teste(service_testat)

tester.run()
runner.run()










"""
Gaga, Maria, Rock, 10.10.2004
Haha, Gigi, Pop, 02.02.2003
Baba, Marcel, Jazz, 12.12.2006
Uaua, Radu, Rock, 04.06.2020
Lala, Mihnea, Rock, 05.08.2020
Rara, Bibi, Jazz, 06.06.2000
Haha, Gigi, Pop, 07.07.2023
Juju, Mari, Pop, 10.04.2003
Kala, Zanzi, Jazz, 10.05.2006
Iuiu, Iulia,  Rock, 05.04.2005
"""

