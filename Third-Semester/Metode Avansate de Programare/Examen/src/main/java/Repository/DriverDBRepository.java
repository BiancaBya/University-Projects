package Repository;

import Domain.Driver;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DriverDBRepository implements Repository<Integer, Driver>{

    private String url;
    private String username;
    private String password;

    public DriverDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Driver findOne(Integer driverID) {
        return null;
    }

    @Override
    public Iterable<Driver> findAll() {

        Set<Driver> drivers = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Drivers");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {

                int driverID = resultSet.getInt("driverID");
                String name = resultSet.getString("name");
                Driver driver = new Driver(driverID, name);
                drivers.add(driver);

            }
            return drivers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Driver save(Driver entity) {
        return null;
    }

    @Override
    public Driver delete(Integer integer) {
        return null;
    }

    @Override
    public Driver update(Driver entity) {
        return null;
    }
}
