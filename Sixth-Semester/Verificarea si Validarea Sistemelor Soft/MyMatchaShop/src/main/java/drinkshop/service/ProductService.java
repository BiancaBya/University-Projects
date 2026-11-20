package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.interfaces.IProductService;
import drinkshop.exception.BusinessException;
import drinkshop.exception.NotFoundException;
import drinkshop.validator.ProductValidator;
import drinkshop.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements IProductService {

    private final Repository<Integer, Product> productRepo;
    private final Validator<Product> validator;

    public ProductService(Repository<Integer, Product> productRepo) {
        this(productRepo, new ProductValidator());
    }

    public ProductService(Repository<Integer, Product> productRepo, Validator<Product> validator) {
        this.productRepo = productRepo;
        this.validator = validator;
    }

    @Override
    public void addProduct(Product product) {
        validator.validate(product);
        if (productRepo.findOne(product.getId()) != null) {
            throw new BusinessException("Exista deja un produs cu ID-ul " + product.getId() + ".");
        }
        productRepo.save(product);
    }

    @Override
    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        Product existing = productRepo.findOne(id);
        if (existing == null) {
            throw new NotFoundException("Produsul cu ID-ul " + id + " nu exista.");
        }
        Product updated = new Product(id, name, price, categorie, tip);
        validator.validate(updated);
        productRepo.update(updated);
    }

    @Override
    public void deleteProduct(int id) {
        Product existing = productRepo.findOne(id);
        if (existing == null) {
            throw new NotFoundException("Produsul cu ID-ul " + id + " nu exista.");
        }
        productRepo.delete(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product findById(int id) {
        Product product = productRepo.findOne(id);
        if (product == null) {
            throw new NotFoundException("Produsul cu ID-ul " + id + " nu exista.");
        }
        return product;
    }

    @Override
    public List<Product> filterByCategorie(CategorieBautura categorie) {
        if (categorie == null || categorie == CategorieBautura.ALL) {
            return getAllProducts();
        }
        return getAllProducts().stream()
                .filter(p -> p.getCategorie() == categorie)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByTip(TipBautura tip) {
        if (tip == null || tip == TipBautura.ALL) {
            return getAllProducts();
        }
        return getAllProducts().stream()
                .filter(p -> p.getTip() == tip)
                .collect(Collectors.toList());
    }
}
