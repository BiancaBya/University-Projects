package drinkshop.ui;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.Reteta;
import drinkshop.domain.TipBautura;
import drinkshop.service.DrinkShopService;
import drinkshop.exception.BusinessException;
import drinkshop.exception.NotFoundException;
import drinkshop.exception.RepositoryException;
import drinkshop.validator.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class DrinkShopController {

    private DrinkShopService service;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, CategorieBautura> colProdCategorie;
    @FXML private TableColumn<Product, TipBautura> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<CategorieBautura> comboProdCategorie;
    @FXML private ComboBox<TipBautura> comboProdTip;

    @FXML private TableView<Reteta> retetaTable;
    @FXML private TableColumn<Reteta, Integer> colRetetaId;
    @FXML private TableColumn<Reteta, String> colRetetaDesc;

    @FXML private TableView<IngredientReteta> newRetetaTable;
    @FXML private TableColumn<IngredientReteta, String> colNewIngredName;
    @FXML private TableColumn<IngredientReteta, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant;

    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;

    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;
    @FXML private Label lblTotalRevenue;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Reteta> retetaList = FXCollections.observableArrayList();
    private final ObservableList<IngredientReteta> newRetetaList = FXCollections.observableArrayList();
    private final ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();

    private Order currentOrder = new Order(1);

    public void setService(DrinkShopService service) {
        this.service = service;
        initData();
    }

    @FXML
    private void initialize() {
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colProdCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colProdTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        productTable.setItems(productList);

        comboProdCategorie.getItems().setAll(CategorieBautura.values());
        comboProdTip.getItems().setAll(TipBautura.values());
        comboProdCategorie.setValue(CategorieBautura.ALL);
        comboProdTip.setValue(TipBautura.ALL);

        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Reteta reteta = data.getValue();
            String desc = reteta.getIngrediente().stream()
                    .map(i -> i.getDenumire() + " (" + i.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(retetaList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        newRetetaTable.setItems(newRetetaList);

        colOrderProdName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getNume()));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);

        comboQty.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    private void initData() {
        productList.setAll(service.getAllProducts());
        retetaList.setAll(service.getAllRetete());
        productList.sort(Comparator.comparingInt(Product::getId));
        retetaList.sort(Comparator.comparingInt(Reteta::getProductId));
        lblTotalRevenue.setText(String.format("Daily Revenue: %.2f", service.getDailyRevenue()));
        updateOrderTotal();
    }

    @FXML
    private void onAddProduct() {
        handleUiAction(() -> {
            Reteta selectedReteta = retetaTable.getSelectionModel().getSelectedItem();
            if (selectedReteta == null) {
                throw new BusinessException("Selecteaza o reteta pentru produs.");
            }
            if (service.getAllProducts().stream().anyMatch(product -> product.getId() == selectedReteta.getProductId())) {
                throw new BusinessException("Exista deja un produs pentru reteta selectata.");
            }

            Product product = new Product(
                    selectedReteta.getProductId(),
                    requireText(txtProdName, "Numele produsului"),
                    parsePositiveDouble(txtProdPrice, "Pret"),
                    requireCategorie(comboProdCategorie),
                    requireTip(comboProdTip)
            );
            service.addProduct(product);
            clearProductInputs();
            initData();
        });
    }

    @FXML
    private void onUpdateProduct() {
        handleUiAction(() -> {
            Product selected = requireSelectedProduct();
            service.updateProduct(selected.getId(),
                    requireText(txtProdName, "Numele produsului"),
                    parsePositiveDouble(txtProdPrice, "Pret"),
                    requireCategorie(comboProdCategorie),
                    requireTip(comboProdTip));
            clearProductInputs();
            initData();
        });
    }

    @FXML
    private void onDeleteProduct() {
        handleUiAction(() -> {
            Product selected = requireSelectedProduct();
            service.deleteProduct(selected.getId());
            clearProductInputs();
            initData();
        });
    }

    @FXML
    private void onFilterCategorie() {
        productList.setAll(service.filtreazaDupaCategorie(comboProdCategorie.getValue()));
    }

    @FXML
    private void onFilterTip() {
        productList.setAll(service.filtreazaDupaTip(comboProdTip.getValue()));
    }

    @FXML
    private void onAddNewIngred() {
        handleUiAction(() -> {
            String ingredientName = requireText(txtNewIngredName, "Ingredient");
            double quantity = parsePositiveDouble(txtNewIngredCant, "Cantitate ingredient");
            newRetetaList.add(new IngredientReteta(ingredientName, quantity));
            txtNewIngredName.clear();
            txtNewIngredCant.clear();
        });
    }

    @FXML
    private void onDeleteNewIngred() {
        IngredientReteta selected = newRetetaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            newRetetaList.remove(selected);
        }
    }

    @FXML
    private void onAddNewReteta() {
        handleUiAction(() -> {
            if (newRetetaList.isEmpty()) {
                throw new BusinessException("Adauga cel putin un ingredient in reteta.");
            }
            int nextId = Optional.of(service.getAllRetete().stream()
                    .map(Reteta::getProductId)
                    .max(Integer::compareTo)
                    .orElse(0)).orElse(0) + 1;
            Reteta reteta = new Reteta(nextId, new ArrayList<>(newRetetaList));
            service.addReteta(reteta);
            newRetetaList.clear();
            txtNewIngredName.clear();
            txtNewIngredCant.clear();
            initData();
        });
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaList.clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    @FXML
    private void onAddOrderItem() {
        handleUiAction(() -> {
            Product selected = requireSelectedProduct();
            Integer qty = comboQty.getValue();
            if (qty == null || qty <= 0) {
                throw new BusinessException("Selecteaza o cantitate valida.");
            }
            currentOrderItems.add(new OrderItem(selected, qty));
            updateOrderTotal();
        });
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem selected = currentOrderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            currentOrderItems.remove(selected);
            updateOrderTotal();
        }
    }

    @FXML
    private void onFinalizeOrder() {
        handleUiAction(() -> {
            if (currentOrderItems.isEmpty()) {
                throw new BusinessException("Adauga cel putin un produs in comanda.");
            }

            currentOrder.getItems().clear();
            currentOrder.getItems().addAll(currentOrderItems);
            currentOrder.setTotalPrice(service.computeTotal(currentOrder));

            service.addOrder(currentOrder);
            txtReceipt.setText(service.generateReceipt(currentOrder));

            currentOrderItems.clear();
            currentOrder = new Order(currentOrder.getId() + 1);
            updateOrderTotal();
            initData();
        });
    }

    private void updateOrderTotal() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        double total = service.computeTotal(currentOrder);
        currentOrder.setTotalPrice(total);
        lblOrderTotal.setText(String.format("Total: %.2f", total));
    }

    @FXML
    private void onExportOrdersCsv() {
        handleUiAction(() -> {
            service.exportCsv("orders.csv");
            showInfo("Fisierul orders.csv a fost generat cu succes.");
        });
    }

    @FXML
    private void onDailyRevenue() {
        lblTotalRevenue.setText(String.format("Daily Revenue: %.2f", service.getDailyRevenue()));
    }

    private Product requireSelectedProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            throw new BusinessException("Selecteaza un produs din lista.");
        }
        return selected;
    }

    private String requireText(TextField textField, String fieldName) {
        String value = textField.getText() == null ? "" : textField.getText().trim();
        if (value.isBlank()) {
            throw new BusinessException(fieldName + " nu poate fi gol.");
        }
        return value;
    }

    private double parsePositiveDouble(TextField textField, String fieldName) {
        String value = requireText(textField, fieldName);
        try {
            double parsed = Double.parseDouble(value);
            if (parsed <= 0) {
                throw new BusinessException(fieldName + " trebuie sa fie pozitiv.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new BusinessException(fieldName + " trebuie sa fie numeric.");
        }
    }

    private CategorieBautura requireCategorie(ComboBox<CategorieBautura> comboBox) {
        CategorieBautura value = comboBox.getValue();
        if (value == null || value == CategorieBautura.ALL) {
            throw new BusinessException("Selecteaza o categorie valida pentru produs.");
        }
        return value;
    }

    private TipBautura requireTip(ComboBox<TipBautura> comboBox) {
        TipBautura value = comboBox.getValue();
        if (value == null || value == TipBautura.ALL) {
            throw new BusinessException("Selecteaza un tip valid pentru produs.");
        }
        return value;
    }

    private void clearProductInputs() {
        txtProdName.clear();
        txtProdPrice.clear();
        comboProdCategorie.setValue(CategorieBautura.ALL);
        comboProdTip.setValue(TipBautura.ALL);
    }

    private void handleUiAction(Runnable runnable) {
        try {
            runnable.run();
        } catch (ValidationException | BusinessException | NotFoundException | RepositoryException exception) {
            showError(exception.getMessage());
        } catch (Exception exception) {
            showError("A aparut o eroare neasteptata: " + exception.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText("Operatia nu a putut fi finalizata");
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Information");
        alert.setHeaderText("Operatia a fost finalizata");
        alert.showAndWait();
    }
}
