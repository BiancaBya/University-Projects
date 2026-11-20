package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Order;
import drinkshop.domain.Product;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.domain.TipBautura;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.repository.Repository;
import drinkshop.service.interfaces.IOrderService;
import drinkshop.service.interfaces.IProductService;
import drinkshop.service.interfaces.IRetetaService;
import drinkshop.service.interfaces.IStocService;
import drinkshop.exception.BusinessException;

import java.util.List;

public class DrinkShopService {

    private final IProductService productService;
    private final IOrderService orderService;
    private final IRetetaService retetaService;
    private final IStocService stocService;
    private final DailyReportService report;

    public DrinkShopService(
            Repository<Integer, Product> productRepo,
            Repository<Integer, Order> orderRepo,
            Repository<Integer, Reteta> retetaRepo,
            Repository<Integer, Stoc> stocRepo
    ) {
        this.productService = new ProductService(productRepo);
        this.orderService = new OrderService(orderRepo, productRepo);
        this.retetaService = new RetetaService(retetaRepo);
        this.stocService = new StocService(stocRepo);
        this.report = new DailyReportService(orderRepo);
    }

    public void addProduct(Product product) {
        productService.addProduct(product);
    }

    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        productService.updateProduct(id, name, price, categorie, tip);
    }

    public void deleteProduct(int id) {
        productService.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> filtreazaDupaCategorie(CategorieBautura categorie) {
        return productService.filterByCategorie(categorie);
    }

    public List<Product> filtreazaDupaTip(TipBautura tip) {
        return productService.filterByTip(tip);
    }

    public void addOrder(Order order) {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            throw new BusinessException("Comanda nu poate fi goala.");
        }

        for (var item : order.getItems()) {
            Product product = productService.findById(item.getProduct().getId());
            Reteta reteta = retetaService.findById(product.getId());
            for (int index = 0; index < item.getQuantity(); index++) {
                stocService.consuma(reteta);
            }
            item.setProduct(product);
        }

        order.setTotalPrice(orderService.computeTotal(order));
        orderService.addOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public double computeTotal(Order order) {
        return orderService.computeTotal(order);
    }

    public String generateReceipt(Order order) {
        return ReceiptGenerator.generate(order, productService.getAllProducts());
    }

    public double getDailyRevenue() {
        return report.getTotalRevenue();
    }

    public void exportCsv(String path) {
        CsvExporter.exportOrders(productService.getAllProducts(), orderService.getAllOrders(), path);
    }

    public void comandaProdus(Product produs) {
        Reteta reteta = retetaService.findById(produs.getId());
        stocService.consuma(reteta);
    }

    public List<Reteta> getAllRetete() {
        return retetaService.getAll();
    }

    public void addReteta(Reteta reteta) {
        retetaService.addReteta(reteta);
    }

    public void updateReteta(Reteta reteta) {
        retetaService.updateReteta(reteta);
    }

    public void deleteReteta(int id) {
        retetaService.deleteReteta(id);
    }
}
