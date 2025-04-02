package Repository;

import Domain.Driver;
import Domain.Order;
import Domain.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderDBRepository implements Repository<Integer, Order> {


    private String url;
    private String username;
    private String password;


    public OrderDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    public List<Order> findOrdersDriver(int driverID) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM Orders WHERE driverID = ? AND status = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, driverID);
            statement.setString(2, "IN_PROGRESS");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    int orderID = resultSet.getInt("orderID");
                    Status status = Status.valueOf(resultSet.getString("status"));
                    LocalDateTime startDate = resultSet.getDate("startDate").toLocalDate().atStartOfDay();
                    LocalDateTime endDate = resultSet.getDate("endDate").toLocalDate().atStartOfDay();
                    String address = resultSet.getString("address");
                    String destination = resultSet.getString("destination");
                    String clientName = resultSet.getString("clientName");

                    Order order = new Order(orderID, driverID, status, startDate, endDate, address, destination, clientName);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }



    @Override
    public Order findOne(Integer orderID) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders " +
                     "WHERE orderId = ?");
        ) {

            statement.setInt(1, orderID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                int driverID = resultSet.getInt("driverID");
                Status status = Status.valueOf(resultSet.getString("status"));
                LocalDateTime startDate = resultSet.getDate("startDate").toLocalDate().atStartOfDay();
                LocalDateTime endDate = resultSet.getDate("endDate").toLocalDate().atStartOfDay();
                String address = resultSet.getString("address");
                String destination = resultSet.getString("destination");
                String clientName = resultSet.getString("clientName");

                Order order = new Order(orderID, driverID, status, startDate, endDate, address, destination, clientName);
                return order;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Iterable<Order> findAll() {

        Set<Order> orders = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {

                int orderID = resultSet.getInt("orderID");
                int driverID = resultSet.getInt("driverID");
                Status status = Status.valueOf(resultSet.getString("status"));
                LocalDateTime startDate = resultSet.getDate("startDate").toLocalDate().atStartOfDay();
                LocalDateTime endDate = resultSet.getDate("endDate").toLocalDate().atStartOfDay();
                String address = resultSet.getString("address");
                String destination = resultSet.getString("destination");
                String clientName = resultSet.getString("clientName");
                orders.add(new Order(orderID, driverID, status, startDate, endDate, address, destination, clientName));

            }
            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Order save(Order entity) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO orders (driverid, status, startdate, enddate, address, destination, clientname) VALUES (?, ?, ?, ?, ?, ?, ?)");
        ) {
            statement.setInt(1, entity.getDriverID());
            statement.setString(2, entity.getStatus().toString());

            Timestamp startDateTimestamp = Timestamp.valueOf(entity.getStartDate());
            statement.setTimestamp(3, startDateTimestamp);
            Timestamp endDateTimestamp = Timestamp.valueOf(entity.getEndDate());
            statement.setTimestamp(4, endDateTimestamp);

            statement.setString(5, entity.getAddress());
            statement.setString(6, entity.getDestination());
            statement.setString(7, entity.getClientName());

            statement.executeUpdate();
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Order delete(Integer integer) {
        return null;
    }

    @Override
    public Order update(Order entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE orders SET status = ?, endDate = ? WHERE orderID = ?");
        ) {
            statement.setString(1, entity.getStatus().toString());
            Timestamp endDateTimestamp = Timestamp.valueOf(entity.getEndDate());
            statement.setTimestamp(2, endDateTimestamp);
            statement.setFloat(3, entity.getId());
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
