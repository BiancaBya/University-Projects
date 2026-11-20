from Modificat.Ui.consola import Consola
from Modificat.repository.film_repository import FilmRepositoryInMemory, FilmRepoInFile
from Modificat.repository.client_repository import ClientRepositoryInMemory, ClientRepoInFile
from Modificat.repository.inchiriere_repository import InchirieriRepositoryInMemory, InchirieriRepoInFile
from Modificat.service.service_film import FilmService
from Modificat.service.service_client import ClientService
from Modificat.service.service_inchirieri import InchiriereService
from Modificat.teste.teste import Teste


#film_repository = FilmRepositoryInMemory()
film_repository = FilmRepoInFile("data/filme.txt")
#client_repository = ClientRepositoryInMemory()
client_repository = ClientRepoInFile("data/clienti.txt")
#inchirieri_repository = InchirieriRepositoryInMemory(film_repository, client_repository)
inchirieri_repository = InchirieriRepoInFile("data/inchirieri.txt")

film_service = FilmService(film_repository)
client_service = ClientService(client_repository)
inchirieri_service = InchiriereService(film_repository, client_repository, inchirieri_repository)

runner = Consola(film_service, client_service, inchirieri_service)

film_repository_test = FilmRepositoryInMemory()
client_repository_test = ClientRepositoryInMemory()
inchirieri_repository_test = InchirieriRepositoryInMemory(film_repository_test, client_repository_test)

test = Teste(film_repository_test, client_repository_test, inchirieri_repository_test)

test.run_teste()
runner.run()






