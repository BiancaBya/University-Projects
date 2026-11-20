package drinkshop.service;

import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.service.interfaces.IRetetaService;
import drinkshop.exception.BusinessException;
import drinkshop.exception.NotFoundException;
import drinkshop.validator.RetetaValidator;
import drinkshop.validator.Validator;

import java.util.List;

public class RetetaService implements IRetetaService {

    private final Repository<Integer, Reteta> retetaRepo;
    private final Validator<Reteta> validator;

    public RetetaService(Repository<Integer, Reteta> retetaRepo) {
        this(retetaRepo, new RetetaValidator());
    }

    public RetetaService(Repository<Integer, Reteta> retetaRepo, Validator<Reteta> validator) {
        this.retetaRepo = retetaRepo;
        this.validator = validator;
    }

    @Override
    public void addReteta(Reteta reteta) {
        validator.validate(reteta);
        if (retetaRepo.findOne(reteta.getProductId()) != null) {
            throw new BusinessException("Exista deja o reteta pentru produsul cu ID-ul " + reteta.getProductId() + ".");
        }
        retetaRepo.save(reteta);
    }

    @Override
    public void updateReteta(Reteta reteta) {
        if (retetaRepo.findOne(reteta.getProductId()) == null) {
            throw new NotFoundException("Reteta cu ID-ul " + reteta.getProductId() + " nu exista.");
        }
        validator.validate(reteta);
        retetaRepo.update(reteta);
    }

    @Override
    public void deleteReteta(int id) {
        if (retetaRepo.findOne(id) == null) {
            throw new NotFoundException("Reteta cu ID-ul " + id + " nu exista.");
        }
        retetaRepo.delete(id);
    }

    @Override
    public Reteta findById(int id) {
        Reteta reteta = retetaRepo.findOne(id);
        if (reteta == null) {
            throw new NotFoundException("Reteta pentru produsul cu ID-ul " + id + " nu exista.");
        }
        return reteta;
    }

    @Override
    public List<Reteta> getAll() {
        return retetaRepo.findAll();
    }
}
