package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.interfaces.IOrderService;
import drinkshop.exception.BusinessException;
import drinkshop.exception.NotFoundException;
import drinkshop.validator.OrderValidator;
import drinkshop.validator.Validator;

import java.util.List;

public class OrderService implements IOrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;
    private final Validator<Order> validator;

    public OrderService(Repository<Integer, Order> orderRepo, Repository<Integer, Product> productRepo) {
        this(orderRepo, productRepo, new OrderValidator());
    }

    public OrderService(Repository<Integer, Order> orderRepo,
                        Repository<Integer, Product> productRepo,
                        Validator<Order> validator) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.validator = validator;
    }

    @Override
    public void addOrder(Order order) {
        order.computeTotalPrice();
        validator.validate(order);
        if (orderRepo.findOne(order.getId()) != null) {
            throw new BusinessException("Exista deja o comanda cu ID-ul " + order.getId() + ".");
        }
        orderRepo.save(order);
    }

    @Override
    public void updateOrder(Order order) {
        if (orderRepo.findOne(order.getId()) == null) {
            throw new NotFoundException("Comanda cu ID-ul " + order.getId() + " nu exista.");
        }
        order.computeTotalPrice();
        validator.validate(order);
        orderRepo.update(order);
    }

    @Override
    public void deleteOrder(int id) {
        if (orderRepo.findOne(id) == null) {
            throw new NotFoundException("Comanda cu ID-ul " + id + " nu exista.");
        }
        orderRepo.delete(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Order findById(int id) {
        Order order = orderRepo.findOne(id);
        if (order == null) {
            throw new NotFoundException("Comanda cu ID-ul " + id + " nu exista.");
        }
        return order;
    }

    @Override
    public double computeTotal(Order order) {
        if (order == null || order.getItems() == null) {
            return 0.0;
        }
        return order.getItems().stream()
                .mapToDouble(item -> {
                    Product product = productRepo.findOne(item.getProduct().getId());
                    if (product == null) {
                        throw new NotFoundException("Produsul cu ID-ul " + item.getProduct().getId() + " nu exista.");
                    }
                    return product.getPret() * item.getQuantity();
                })
                .sum();
    }

    @Override
    public void addItem(Order order, OrderItem item) {
        if (order == null) {
            throw new BusinessException("Comanda nu poate fi null.");
        }
        order.getItems().add(item);
        order.computeTotalPrice();
        validator.validate(order);
        orderRepo.update(order);
    }

    @Override
    public void removeItem(Order order, OrderItem item) {
        if (order == null) {
            throw new BusinessException("Comanda nu poate fi null.");
        }
        order.getItems().remove(item);
        order.computeTotalPrice();
        validator.validate(order);
        orderRepo.update(order);
    }
}
