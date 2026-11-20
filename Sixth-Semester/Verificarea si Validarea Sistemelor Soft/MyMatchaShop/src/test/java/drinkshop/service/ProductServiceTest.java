package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {

    @TempDir
    Path tempDir;

    private ProductService service;
    private FileProductRepository repo;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = tempDir.resolve("products_test.txt");
        Files.createFile(tempFile);

        repo = new FileProductRepository(tempFile.toString());
        service = new ProductService(repo);
    }

    @Test
    @Order(1)
    @DisplayName("ECP1 - nume valid, pret valid -> record added")
    @Tag("ECP")
    void tc1_ECP() {
        Product product = new Product(
                1,
                "Cappuccino",
                12.50,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        service.addProduct(product);

        assertEquals(1, service.getAllProducts().size());
        Product saved = service.findById(1);
        assertNotNull(saved);
        assertEquals("Cappuccino", saved.getNume());
        assertEquals(12.50, saved.getPret());
    }

    @Test
    @Order(2)
    @DisplayName("ECP2 - nume = null -> ValidationException")
    @Tag("ECP")
    void tc2_ECP() {
        Product product = new Product(
                2,
                null,
                12.50,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(3)
    @DisplayName("ECP3 - nume = empty string -> ValidationException")
    @Tag("ECP")
    void tc3_ECP() {
        Product product = new Product(
                3,
                "",
                12.50,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(4)
    @DisplayName("ECP4 - nume = single blank -> ValidationException")
    @Tag("ECP")
    void tc4_ECP() {
        Product product = new Product(
                4,
                " ",
                12.50,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(5)
    @DisplayName("ECP5 - pret = -5.00 -> ValidationException")
    @Tag("ECP")
    void tc5_ECP() {
        Product product = new Product(
                5,
                "Latte",
                -5.00,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(6)
    @DisplayName("BVA1 - pret = -0.01, nume = C -> ValidationException")
    @Tag("BVA")
    void tc1_BVA() {
        Product product = new Product(
                1,
                "C",
                -0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(7)
    @DisplayName("BVA2 - pret = 0, nume = C -> ValidationException")
    @Tag("BVA")
    void tc2_BVA() {
        Product product = new Product(
                2,
                "C",
                0.00,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(8)
    @DisplayName("BVA3 - pret = 0.01, nume = C -> record added")
    @Tag("BVA")
    void tc3_BVA() {
        Product product = new Product(
                3,
                "C",
                0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        service.addProduct(product);

        assertEquals(1, service.getAllProducts().size());
        Product saved = service.findById(3);
        assertNotNull(saved);
        assertEquals("C", saved.getNume());
        assertEquals(0.01, saved.getPret());
    }

    @Test
    @Order(9)
    @DisplayName("BVA4 - nume = null, pret = 0.01 -> ValidationException")
    @Tag("BVA")
    void tc4_BVA() {
        Product product = new Product(
                4,
                null,
                0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(10)
    @DisplayName("BVA5 - nume != null, folosim nume = C si pret = 0.01 -> record added")
    @Tag("BVA")
    void tc5_BVA() {
        Product product = new Product(
                3,
                "C",
                0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        service.addProduct(product);

        assertEquals(1, service.getAllProducts().size());
        Product saved = service.findById(3);
        assertNotNull(saved);
        assertEquals("C", saved.getNume());
        assertEquals(0.01, saved.getPret());
    }

    @Test
    @Order(11)
    @DisplayName("BVA6 - nume = empty string, pret = 0.01 -> ValidationException")
    @Tag("BVA")
    void tc6_BVA() {
        Product product = new Product(
                5,
                "",
                0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(12)
    @DisplayName("BVA7 - nume = single blank, pret = 0.01 -> ValidationException")
    @Tag("BVA")
    void tc7_BVA() {
        Product product = new Product(
                6,
                " ",
                0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        assertThrows(ValidationException.class, () -> service.addProduct(product));
        assertEquals(0, service.getAllProducts().size());
    }

    @Test
    @Order(13)
    @DisplayName("BVA8 - nume not blank, folosim nume = C si pret = 0.01 -> record added")
    @Tag("BVA")
    void tc8_BVA() {
        Product product = new Product(
                3,
                "C",
                0.01,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        service.addProduct(product);

        assertEquals(1, service.getAllProducts().size());
        Product saved = service.findById(3);
        assertNotNull(saved);
        assertEquals("C", saved.getNume());
        assertEquals(0.01, saved.getPret());
    }
}

