package drinkshop.export;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.exception.NotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CsvExporter {
    private CsvExporter() {}
    public static void exportOrders(List<Product> products, List<Order> orders, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("Date,OrderId,Product,Quantity,UnitPrice,LineTotal,OrderTotal,DailyTotal\n");
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            double dailyTotal = orders.stream().mapToDouble(Order::getTotalPrice).sum();

            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    Product product = products.stream()
                            .filter(p -> item.getProduct().getId() == p.getId())
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Produsul cu ID-ul " + item.getProduct().getId() + " nu exista."));
                    writer.write(String.format(Locale.US,
                            "%s,%d,%s,%d,%.2f,%.2f,%.2f,%.2f%n",
                            date,
                            order.getId(),
                            escape(product.getNume()),
                            item.getQuantity(),
                            product.getPret(),
                            item.getTotal(),
                            order.getTotalPrice(),
                            dailyTotal));
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Nu s-a putut exporta fisierul CSV.", exception);
        }
    }

    private static String escape(String value) {
        String escaped = value.replace("\"", "\"\"");
        return '"' + escaped + '"';
    }
}
