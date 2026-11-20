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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceIntegrationTest {

    private Repository<Integer, Product> repo;
    private ProductValidator validator;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repo = mock(Repository.class);
        validator = new ProductValidator();
        service = new ProductService(repo, validator);
    }

    @Test
    @DisplayName("Step 2 - produs valid -> validator real accepta si save este apelat")
    void addProduct_validProduct_callsSave() {
        Product product = new Product(
                1,
                "Matcha Latte",
                20.0,
                CategorieBautura.MILK_COFFEE,
                TipBautura.DAIRY
        );

        when(repo.findOne(1)).thenReturn(null);

        assertDoesNotThrow(() -> service.addProduct(product));

        verify(repo, times(1)).findOne(1);
        verify(repo, times(1)).save(product);
    }

    @Test
    @DisplayName("Step 2 - produs invalid -> ValidationException si save nu este apelat")
    void addProduct_invalidProduct_throwsValidationException() {
        Product product = new Product(
                2,
                "",
                0.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));

        verify(repo, never()).findOne(anyInt());
        verify(repo, never()).save(any());
    }
}