package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.validator.ProductValidator;
import drinkshop.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceIntegration3Test {

    private ProductService service;
    private InMemoryProductRepository repo;
    private ProductValidator validator;

    @BeforeEach
    void setUp() {
        repo = new InMemoryProductRepository();
        validator = new ProductValidator();
        service = new ProductService(repo, validator);
    }

    @Test
    @DisplayName("Step 3 - produs valid -> se adauga in repository")
    void addProduct_validProduct_isStoredInRepository() {
        Product product = new Product(
                1,
                "Strawberry Matcha",
                22.0,
                CategorieBautura.SMOOTHIE,
                TipBautura.DAIRY
        );

        assertDoesNotThrow(() -> service.addProduct(product));

        assertEquals(1, service.getAllProducts().size());

        Product saved = service.findById(1);
        assertNotNull(saved);
        assertEquals("Strawberry Matcha", saved.getNume());
        assertEquals(22.0, saved.getPret());
        assertEquals(CategorieBautura.SMOOTHIE, saved.getCategorie());
        assertEquals(TipBautura.DAIRY, saved.getTip());
    }

    @Test
    @DisplayName("Step 3 - produs invalid -> ValidationException si repository ramane gol")
    void addProduct_invalidProduct_repositoryRemainsEmpty() {
        Product product = new Product(
                2,
                "",
                -3.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));

        assertEquals(0, service.getAllProducts().size());
    }

    private static class InMemoryProductRepository implements Repository<Integer, Product> {
        private final Map<Integer, Product> data = new LinkedHashMap<>();

        @Override
        public Product findOne(Integer id) {
            return data.get(id);
        }

        @Override
        public List<Product> findAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public Product save(Product entity) {
            data.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Product update(Product entity) {
            data.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Product delete(Integer id) {
            return data.remove(id);
        }
    }
}