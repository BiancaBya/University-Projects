package org.example.Repository.RepositoryDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Admin;
import org.example.Repository.AdminRepoInterface;
import org.example.Repository.JdbcUtils;
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
public class AdminRepoDB implements AdminRepoInterface {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public AdminRepoDB(Properties props) {
        logger.info("Initializing AdminDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    @Override
    public Optional<Admin> findOne(Integer integer) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Admins where id = ?")){
            preSmt.setInt(1, integer);

            try(ResultSet result = preSmt.executeQuery()){
                String email = result.getString("email");
                String password = result.getString("password");
                Admin admin = new Admin(email,password);
                admin.setId(integer);
                return Optional.of(admin);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting admins "+e);
        }

        logger.traceExit(Optional.empty());
        return Optional.empty();
    }

    @Override
    public Iterable<Admin> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Admin> admins = new ArrayList<Admin>();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Admins")){
            try(ResultSet result = preSmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("id");
                    String email = result.getString("email");
                    String password = result.getString("password");

                    Admin admin = new Admin(email, password);
                    admin.setId(id);
                    admins.add(admin);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting admins "+e);
        }

        logger.traceExit(admins);
        return admins;
    }

    @Override
    public Optional<Admin> save(Admin entity) {
        logger.traceEntry("saving admin {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("insert into Admins (email, password) values (?,?)")){

            preSmt.setString(1, entity.getEmail());
            preSmt.setString(2, entity.getPassword());
            int result = preSmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error saving admin "+e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Admin> delete(Integer integer) {
        logger.traceEntry("deleting admin {}", integer);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("delete from Admins where id = ?")){

            preSmt.setInt(1, integer);
            int result = preSmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error deleting admin "+e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Admin> update(Admin entity) {
        logger.traceEntry("updating admin {} with id {}", entity, entity.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("update Admins set email=?, password=?  where id=?")){

            preSmt.setString(1, entity.getEmail());
            preSmt.setString(2, entity.getPassword());
            preSmt.setInt(3, entity.getId());
            int result = preSmt.executeUpdate();
            logger.trace("Updated {} instances", result);
            return Optional.of(entity);

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error updating admin "+e);
        }
        return Optional.empty();
    }
}


