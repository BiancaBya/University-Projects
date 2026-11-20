package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.exception.BusinessException;
import drinkshop.repository.Repository;
import drinkshop.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceMockitoTest {

     Repository<Integer, Product> repo;
     Validator<Product> validator;
     ProductService service;

    @BeforeEach
    void setUp() {
        repo = mock(Repository.class);
        validator = mock(Validator.class);
        service = new ProductService(repo, validator);
    }

    @Test
    @DisplayName("addProduct - produs valid -> save este apelat")
    void addProduct_validProduct_callsSave() {
        Product product = new Product(
                1,
                "Cappuccino",
                15.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        when(repo.findOne(1)).thenReturn(null);

        assertDoesNotThrow(() -> service.addProduct(product));

        verify(validator, times(1)).validate(product);
        verify(repo, times(1)).findOne(1);
        verify(repo, times(1)).save(product);
    }

    @Test
    @DisplayName("addProduct - ID existent -> BusinessException si save nu este apelat")
    void addProduct_existingId_throwsBusinessException() {
        Product product = new Product(
                1,
                "Latte",
                18.0,
                CategorieBautura.MILK_COFFEE,
                TipBautura.DAIRY
        );

        when(repo.findOne(1)).thenReturn(product);

        assertThrows(BusinessException.class, () -> service.addProduct(product));

        verify(validator, times(1)).validate(product);
        verify(repo, times(1)).findOne(1);
        verify(repo, never()).save(product);
    }
}