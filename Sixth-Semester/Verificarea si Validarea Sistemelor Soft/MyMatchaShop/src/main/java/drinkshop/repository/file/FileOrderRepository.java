package drinkshop.repository.file;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.exception.RepositoryException;

import java.util.ArrayList;
import java.util.List;

public class FileOrderRepository extends FileAbstractRepository<Integer, Order> {

    private final Repository<Integer, Product> productRepository;

    public FileOrderRepository(String fileName, Repository<Integer, Product> productRepository) {
        super(fileName);
        this.productRepository = productRepository;
        loadFromFile();
    }

    @Override
    protected Integer getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected Order extractEntity(String line) {
        String[] parts = line.split(",", 3);
        if (parts.length < 3) {
            throw new RepositoryException("Linie invalida in orders.txt: " + line);
        }

        int id = Integer.parseInt(parts[0].trim());
        List<OrderItem> items = new ArrayList<>();

        if (!parts[1].isBlank()) {
            String[] products = parts[1].split("\\|");
            for (String product : products) {
                String[] prodParts = product.split(":");
                if (prodParts.length != 2) {
                    throw new RepositoryException("Format invalid pentru produs in comanda: " + line);
                }
                int productId = Integer.parseInt(prodParts[0].trim());
                int quantity = Integer.parseInt(prodParts[1].trim());
                Product loadedProduct = productRepository.findOne(productId);
                if (loadedProduct == null) {
                    throw new RepositoryException("Produsul cu ID-ul " + productId + " lipseste din products.txt.");
                }
                items.add(new OrderItem(loadedProduct, quantity));
            }
        }

        double totalPrice = Double.parseDouble(parts[2].trim());
        return new Order(id, items, totalPrice);
    }

    @Override
    protected String createEntityAsString(Order entity) {
        StringBuilder stringBuilder = new StringBuilder();
        for (OrderItem item : entity.getItems()) {
            if (!stringBuilder.equals("")) {
                stringBuilder.append("|");
            }
            stringBuilder.append(item.getProduct().getId())
                    .append(":")
                    .append(item.getQuantity());
        }

        return entity.getId() + "," + stringBuilder + "," + entity.getTotalPrice();
    }
}
