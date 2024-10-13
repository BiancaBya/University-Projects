from Ui.Consola import Consola
from Repository.tractor_repository import TractorRepository
from Service.tractor_service import TractorService
from Domain.validator import Validator

tractor_repository = TractorRepository("Data/tractoare.txt")
tractor_service = TractorService(tractor_repository)
validator = Validator(tractor_repository)
runner = Consola(tractor_service, validator)

runner.run()




