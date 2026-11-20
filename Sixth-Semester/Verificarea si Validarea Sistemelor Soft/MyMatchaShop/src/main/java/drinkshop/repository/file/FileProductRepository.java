package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.exception.RepositoryException;

public class FileProductRepository extends FileAbstractRepository<Integer, Product> {

    public FileProductRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }

    @Override
    protected Product extractEntity(String line) {
        String[] elems = line.split(",", 5);
        if (elems.length < 5) {
            throw new RepositoryException("Linie invalida in products.txt: " + line);
        }

        int id = Integer.parseInt(elems[0].trim());
        String name = elems[1].trim();
        double price = Double.parseDouble(elems[2].trim());
        CategorieBautura categorie = CategorieBautura.valueOf(elems[3].trim());
        TipBautura tip = TipBautura.valueOf(elems[4].trim());

        return new Product(id, name, price, categorie, tip);
    }

    @Override
    protected String createEntityAsString(Product entity) {
        return entity.getId() + "," +
                entity.getNume() + "," +
                entity.getPret() + "," +
                entity.getCategorie() + "," +
                entity.getTip();
    }
}
