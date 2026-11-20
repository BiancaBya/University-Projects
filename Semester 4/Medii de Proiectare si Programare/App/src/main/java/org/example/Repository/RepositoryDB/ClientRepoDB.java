package org.example.Repository.RepositoryDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Client;
import org.example.Repository.ClientRepoInterface;
import org.example.Repository.JdbcUtils;
import org.example.Repository.Repository;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class ClientRepoDB implements ClientRepoInterface {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ClientRepoDB(Properties props) {
        logger.info("Initializing ClientDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    @Override
    public Optional<Client> findOne(Integer integer) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Clients where id = ?")){
            preSmt.setInt(1, integer);

            try(ResultSet result = preSmt.executeQuery()){
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String email = result.getString("email");
                String phone = result.getString("phone");
                Client client = new Client(firstName, lastName, email, phone);
                client.setId(integer);
                return Optional.of(client);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting clients "+e);
        }

        logger.traceExit(Optional.empty());
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Client> clients = new ArrayList<Client>();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Clients")){
            try(ResultSet result = preSmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("id");
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    String email = result.getString("email");
                    String phone = result.getString("phone");

                    Client client = new Client(firstName, lastName, email, phone);
                    client.setId(id);
                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting clients "+e);
        }

        logger.traceExit(clients);
        return clients;
    }

    @Override
    public Optional<Client> save(Client entity) {
        logger.traceEntry("saving client {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("insert into Clients (firstName, lastName, email, phone) values (?,?,?,?)")){

            preSmt.setString(1, entity.getFirstName());
            preSmt.setString(2, entity.getLastName());
            preSmt.setString(3, entity.getEmail());
            preSmt.setString(4, entity.getPhone());
            int result = preSmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error saving client "+e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Client> delete(Integer integer) {
        logger.traceEntry("deleting client {}", integer);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("delete from Clients where id = ?")){

            preSmt.setInt(1, integer);
            int result = preSmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error deleting client "+e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) {
        logger.traceEntry("updating client {} with id {}", entity, entity.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("update Clients set firstName=?, lastName=?, email=?, phone=?  where id=?")){

            preSmt.setString(1, entity.getFirstName());
            preSmt.setString(2, entity.getLastName());
            preSmt.setString(3, entity.getEmail());
            preSmt.setString(4, entity.getPhone());
            preSmt.setInt(5, entity.getId());
            int result = preSmt.executeUpdate();
            logger.trace("Updated {} instances", result);
            return Optional.of(entity);

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error updating client "+e);
        }
        return Optional.empty();
    }
}



