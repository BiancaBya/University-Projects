package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.exception.BusinessException;
import drinkshop.exception.NotFoundException;
import drinkshop.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StocServiceTest {

    private StocService service;
    private InMemoryStocRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryStocRepository();
        service = new StocService(repo);
    }

    @Test
    @DisplayName("TC01 - reteta null -> BusinessException")
    @Tag("WBT")
    void consuma_TC01_retetaNull_throwsBusinessException() {
        assertThrows(BusinessException.class, () -> service.consuma(null));
    }

    @Test
    @DisplayName("TC02 - reteta fara ingrediente -> BusinessException")
    @Tag("WBT")
    void consuma_TC02_retetaFaraIngrediente_throwsBusinessException() {
        Reteta reteta = new Reteta(1, List.of());

        assertThrows(BusinessException.class, () -> service.consuma(reteta));
    }

    @Test
    @DisplayName("TC03 - ingredient lipsa sau stoc insuficient -> BusinessException")
    @Tag("WBT")
    void consuma_TC03_stocInsuficient_throwsBusinessException() {
        repo.save(new Stoc(1, "matcha", 2.0, 0.0));

        Reteta reteta = new Reteta(
                1,
                List.of(new IngredientReteta("matcha", 5.0))
        );

        assertThrows(BusinessException.class, () -> service.consuma(reteta));
    }

    @Test
    @DisplayName("TC04 - ingredient inexistent cu necesar 0 -> NotFoundException")
    @Tag("WBT")
    void consuma_TC04_ingredientInexistentCantitateZero_throwsNotFoundException() {
        Reteta reteta = new Reteta(
                1,
                List.of(new IngredientReteta("tapioca", 0.0))
        );

        assertThrows(NotFoundException.class, () -> service.consuma(reteta));
    }

    @Test
    @DisplayName("TC05 - ramas <= 0 -> break, stocul ramane nemodificat")
    @Tag("WBT")
    void consuma_TC05_ramasZero_break_stocNemodificat() {
        repo.save(new Stoc(1, "lapte", 10.0, 0.0));

        Reteta reteta = new Reteta(
                1,
                List.of(new IngredientReteta("lapte", 0.0))
        );

        service.consuma(reteta);

        Stoc stoc = repo.findOne(1);
        assertNotNull(stoc);
        assertEquals(10.0, stoc.getCantitate());
    }

    @Test
    @DisplayName("TC06 - consum normal dintr-un singur stoc")
    @Tag("WBT")
    void consuma_TC06_consumNormalUnSingurStoc_actualizeazaCantitatea() {
        repo.save(new Stoc(1, "lapte", 10.0, 0.0));

        Reteta reteta = new Reteta(
                1,
                List.of(new IngredientReteta("lapte", 4.0))
        );

        service.consuma(reteta);

        Stoc stoc = repo.findOne(1);
        assertNotNull(stoc);
        assertEquals(6.0, stoc.getCantitate());
    }

    @Test
    @DisplayName("TC07 - consum normal din doua stocuri")
    @Tag("WBT")
    void consuma_TC07_consumNormalDouaStocuri_actualizeazaAmbeleCantitati() {
        repo.save(new Stoc(1, "sirop", 3.0, 0.0));
        repo.save(new Stoc(2, "sirop", 5.0, 0.0));

        Reteta reteta = new Reteta(
                1,
                List.of(new IngredientReteta("sirop", 6.0))
        );

        service.consuma(reteta);

        Stoc stoc1 = repo.findOne(1);
        Stoc stoc2 = repo.findOne(2);

        assertNotNull(stoc1);
        assertNotNull(stoc2);

        assertEquals(0.0, stoc1.getCantitate());
        assertEquals(2.0, stoc2.getCantitate());
    }

    private static class InMemoryStocRepository implements Repository<Integer, Stoc> {
        private final Map<Integer, Stoc> data = new LinkedHashMap<>();

        @Override
        public Stoc findOne(Integer id) {
            return data.get(id);
        }

        @Override
        public List<Stoc> findAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public Stoc save(Stoc entity) {
            data.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Stoc delete(Integer id) {
            return data.remove(id);
        }

        @Override
        public Stoc update(Stoc entity) {
            data.put(entity.getId(), entity);
            return entity;
        }
    }
}