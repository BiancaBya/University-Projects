package drinkshop.service.interfaces;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;

import java.util.List;

public interface IOrderService {
    void addOrder(Order order);
    void updateOrder(Order order);
    void deleteOrder(int id);
    List<Order> getAllOrders();
    Order findById(int id);
    double computeTotal(Order order);
    void addItem(Order order, OrderItem item);
    void removeItem(Order order, OrderItem item);
}
