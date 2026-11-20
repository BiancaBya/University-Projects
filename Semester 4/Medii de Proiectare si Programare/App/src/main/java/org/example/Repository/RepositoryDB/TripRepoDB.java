package org.example.Repository.RepositoryDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Trip;
import org.example.Repository.JdbcUtils;
import org.example.Repository.Repository;
import org.example.Repository.TripRepoInterface;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class TripRepoDB implements TripRepoInterface {


    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public TripRepoDB(Properties props) {
        logger.info("Initializing TripDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Optional<Trip> findOne(Integer integer) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Trips where id = ?")){
            preSmt.setInt(1, integer);

            try(ResultSet result = preSmt.executeQuery()){
                String destination = result.getString("destination");
                LocalDateTime dateTime = LocalDateTime.parse(result.getString("date"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                int nrSeats = result.getInt("nrSeats");
                Trip trip = new Trip(destination, dateTime, nrSeats);
                trip.setId(integer);
                return Optional.of(trip);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting trips "+e);
        }

        logger.traceExit(Optional.empty());
        return Optional.empty();
    }

    @Override
    public Iterable<Trip> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Trip> trips = new ArrayList<Trip>();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Trips")){
            try(ResultSet result = preSmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("id");
                    String destination = result.getString("destination");
                    LocalDateTime dateTime = LocalDateTime.parse(result.getString("date"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int nrSeats = result.getInt("nrSeats");
                    Trip trip = new Trip(destination, dateTime, nrSeats);
                    trip.setId(id);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting trips "+e);
        }

        logger.traceExit(trips);
        return trips;
    }

    @Override
    public Optional<Trip> save(Trip entity) {
        logger.traceEntry("saving trip {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("insert into Trips (destination, date, nrSeats) values (?,?,?)")){

            preSmt.setString(1, entity.getDestination());
            preSmt.setString(2, entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            preSmt.setInt(3, entity.getNrSeats());
            int result = preSmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error saving trip "+e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Trip> delete(Integer integer) {
        logger.traceEntry("deleting trip {}", integer);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("delete from Trips where id = ?")){

            preSmt.setInt(1, integer);
            int result = preSmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error deleting trip "+e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Trip> update(Trip entity) {
        logger.traceEntry("updating trip {} with id {}", entity, entity.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("update main.Trips set destination=?, date=?, nrSeats=?  where id=?")){

            preSmt.setString(1, entity.getDestination());
            preSmt.setString(2, entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            preSmt.setInt(3, entity.getNrSeats());
            preSmt.setInt(4, entity.getId());
            int result = preSmt.executeUpdate();
            logger.trace("Updated {} instances", result);
            return Optional.of(entity);

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error updating trip "+e);
        }
        return Optional.empty();
    }
}


