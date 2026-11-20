package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.exception.NotFoundException;

import java.util.List;
import java.util.Locale;

public class ReceiptGenerator {
    private ReceiptGenerator() {}
    public static String generate(Order order, List<Product> products) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===== BON FISCAL =====\n")
                .append("Comanda #")
                .append(order.getId())
                .append("\n");

        for (OrderItem item : order.getItems()) {
            Product product = products.stream()
                    .filter(existingProduct -> item.getProduct().getId() == existingProduct.getId())
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Produsul cu ID-ul " + item.getProduct().getId() + " nu exista."));
            stringBuilder.append(String.format(Locale.US,
                    "%s: %.2f x %d = %.2f RON%n",
                    product.getNume(),
                    product.getPret(),
                    item.getQuantity(),
                    item.getTotal()));
        }

        stringBuilder.append("---------------------\nTOTAL: ")
                .append(String.format(Locale.US, "%.2f", order.getTotalPrice()))
                .append(" RON\n=====================\n");
        return stringBuilder.toString();
    }
}
