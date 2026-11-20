package drinkshop.repository.file;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.exception.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileRetetaRepository extends FileAbstractRepository<Integer, Reteta> {

    public FileRetetaRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Reteta entity) {
        return entity.getProductId();
    }

    @Override
    protected Reteta extractEntity(String line) {
        String[] elems = line.split(",");
        if (elems.length < 2) {
            throw new RepositoryException("Linie invalida in retete.txt: " + line);
        }

        int productId = Integer.parseInt(elems[0].trim());
        List<IngredientReteta> ingrediente = new ArrayList<>();
        int index = 1;
        while (index < elems.length) {
            String ingredientTotal = elems[index++].trim();
            String[] ingredientSeparat = ingredientTotal.split(":");
            if (ingredientSeparat.length != 2) {
                throw new RepositoryException("Ingredient invalid in reteta: " + line);
            }
            String ingredientName = ingredientSeparat[0].trim();
            Double ingredientQuantity = Double.parseDouble(ingredientSeparat[1].trim());
            ingrediente.add(new IngredientReteta(ingredientName, ingredientQuantity));
        }
        return new Reteta(productId, ingrediente);
    }

    @Override
    protected String createEntityAsString(Reteta entity) {
        String ingrediente = entity.getIngrediente().stream()
                .map(entry -> entry.getDenumire() + ":" + entry.getCantitate())
                .collect(Collectors.joining(","));
        return entity.getProductId() + "," + ingrediente;
    }
}
