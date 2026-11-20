from Cinema_nou.ui.consola import Consola
from Cinema_nou.repository.film_repository import FilmRepository
from Cinema_nou.repository.client_repository import ClientRepository
from Cinema_nou.repository.inchiriere_repository import InchirieriRepository
from Cinema_nou.service.film_service import FilmService
from Cinema_nou.service.client_service import ClientService
from Cinema_nou.service.inchiriere_service import InchiriereService
from Cinema_nou.teste.teste import Teste


film_repository = FilmRepository()
client_repository = ClientRepository()
inchirieri_repository = InchirieriRepository(film_repository,client_repository)
film_service = FilmService(film_repository)
client_service = ClientService(client_repository)
inchirieri_service = InchiriereService(film_repository, client_repository, inchirieri_repository)
runner = Consola(film_service, client_service, inchirieri_service)

film_repository_test = FilmRepository()
client_repository_test = ClientRepository()
inchirieri_repository_test = InchirieriRepository(film_repository_test, client_repository_test)
test = Teste(film_repository_test, client_repository_test, inchirieri_repository_test)

test.run_teste()
runner.run()






