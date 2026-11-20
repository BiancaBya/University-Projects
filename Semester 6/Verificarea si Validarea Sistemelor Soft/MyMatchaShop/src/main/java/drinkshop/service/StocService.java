package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.interfaces.IStocService;
import drinkshop.exception.BusinessException;
import drinkshop.exception.NotFoundException;
import drinkshop.validator.StocValidator;
import drinkshop.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class StocService implements IStocService {

    private final Repository<Integer, Stoc> stocRepo;
    private final Validator<Stoc> validator;

    public StocService(Repository<Integer, Stoc> stocRepo) {
        this(stocRepo, new StocValidator());
    }

    public StocService(Repository<Integer, Stoc> stocRepo, Validator<Stoc> validator) {
        this.stocRepo = stocRepo;
        this.validator = validator;
    }

    @Override
    public List<Stoc> getAll() {
        return stocRepo.findAll();
    }

    @Override
    public void add(Stoc stoc) {
        validator.validate(stoc);
        if (stocRepo.findOne(stoc.getId()) != null) {
            throw new BusinessException("Exista deja un stoc cu ID-ul " + stoc.getId() + ".");
        }
        stocRepo.save(stoc);
    }

    @Override
    public void update(Stoc stoc) {
        if (stocRepo.findOne(stoc.getId()) == null) {
            throw new NotFoundException("Stocul cu ID-ul " + stoc.getId() + " nu exista.");
        }
        validator.validate(stoc);
        stocRepo.update(stoc);
    }

    @Override
    public void delete(int id) {
        if (stocRepo.findOne(id) == null) {
            throw new NotFoundException("Stocul cu ID-ul " + id + " nu exista.");
        }
        stocRepo.delete(id);
    }

    @Override
    public boolean areSuficient(Reteta reteta) {
        if (reteta == null || reteta.getIngrediente() == null || reteta.getIngrediente().isEmpty()) {
            return false;
        }

        for (IngredientReteta ingredientReteta : reteta.getIngrediente()) {
            String ingredient = ingredientReteta.getDenumire();
            double necesar = ingredientReteta.getCantitate();

            double disponibil = stocRepo.findAll().stream()
                    .filter(stoc -> stoc.getIngredient().equalsIgnoreCase(ingredient))
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            if (disponibil < necesar) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void consuma(Reteta reteta) {
        if (reteta == null) {
            throw new BusinessException("Reteta nu poate fi null."); }
        if (!areSuficient(reteta)) {
            throw new BusinessException("Stoc insuficient pentru reteta selectata."); }

        for (IngredientReteta ingredientReteta : reteta.getIngrediente()) {
            String ingredient = ingredientReteta.getDenumire();
            double ramas = ingredientReteta.getCantitate();

            List<Stoc> ingredienteStoc = stocRepo.findAll().stream().filter(stoc -> stoc.getIngredient().equalsIgnoreCase(ingredient)).collect(Collectors.toList());

            if (ingredienteStoc.isEmpty()) {
                throw new NotFoundException("Ingredientul " + ingredient + " nu exista in stoc."); }

            for (Stoc stoc : ingredienteStoc) {
                if (ramas <= 0) {
                    break; }

                double deScazut = Math.min(stoc.getCantitate(), ramas);
                stoc.setCantitate(stoc.getCantitate() - deScazut);
                ramas -= deScazut;
                validator.validate(stoc);
                stocRepo.update(stoc); } }
    }
}
