package org.example.Repository.RepositoryDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Reservation;
import org.example.Repository.JdbcUtils;
import org.example.Repository.Repository;
import org.example.Repository.ReservationRepoInterface;
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
public class ReservationRepoDB implements ReservationRepoInterface {

    private JdbcUtils dbUtils;
    private ClientRepoDB clientRepoDB;
    private TripRepoDB tripRepoDB;

    private static final Logger logger= LogManager.getLogger();

    public ReservationRepoDB(Properties props, ClientRepoDB clientRepoDB, TripRepoDB tripRepoDB) {
        logger.info("Initializing ReservationDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.clientRepoDB=clientRepoDB;
        this.tripRepoDB=tripRepoDB;
    }


    @Override
    public Optional<Reservation> findOne(Integer integer) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Reservations where id = ?")){
            preSmt.setInt(1, integer);

            try(ResultSet result = preSmt.executeQuery()){
                int client_id = result.getInt("client_id");
                int trip_id = result.getInt("trip_id");
                int nrReservedSeats = result.getInt("nrReservedSeats");
                Reservation reservation = new Reservation(clientRepoDB.findOne(client_id).get(), tripRepoDB.findOne(trip_id).get(), nrReservedSeats);
                reservation.setId(integer);
                return Optional.of(reservation);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting reservations "+e);
        }

        logger.traceExit(Optional.empty());
        return Optional.empty();
    }

    @Override
    public Iterable<Reservation> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Reservation> reservations = new ArrayList<Reservation>();

        try(PreparedStatement preSmt = con.prepareStatement("select * from Reservations")){
            try(ResultSet result = preSmt.executeQuery()){
                while (result.next()) {
                    int id = result.getInt("id");
                    int client_id = result.getInt("client_id");
                    int trip_id = result.getInt("trip_id");
                    int nrReservedSeats = result.getInt("nrReservedSeats");

                    Reservation reservation = new Reservation(clientRepoDB.findOne(client_id).get(), tripRepoDB.findOne(trip_id).get(), nrReservedSeats);
                    reservation.setId(id);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting admins "+e);
        }

        logger.traceExit(reservations);
        return reservations;
    }

    @Override
    public Optional<Reservation> save(Reservation entity) {
        logger.traceEntry("saving reservation {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("insert into Reservations(client_id, trip_id, nrReservedSeats) values (?,?,?)")){

            preSmt.setInt(1, entity.getClient().getId());
            preSmt.setInt(2, entity.getTrip().getId());
            preSmt.setInt(3, entity.getNrReservedSeats());
            int result = preSmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error saving reservation "+e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Reservation> delete(Integer integer) {
        logger.traceEntry("deleting reservations {}", integer);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preSmt=con.prepareStatement("delete from Reservations where id = ?")){

            preSmt.setInt(1, integer);
            int result = preSmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error deleting reservation "+e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Reservation> update(Reservation entity) {
        logger.traceEntry("updating reservation {} with id {}", entity, entity.getId());
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("update Reservations set client_id=?, trip_id=?, nrReservedSeats=?  where id=?")){

            preSmt.setInt(1, entity.getClient().getId());
            preSmt.setInt(2, entity.getTrip().getId());
            preSmt.setInt(3, entity.getNrReservedSeats());
            preSmt.setInt(4, entity.getId());
            int result = preSmt.executeUpdate();
            logger.trace("Updated {} instances", result);
            return Optional.of(entity);

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error updating reservation "+e);
        }
        return Optional.empty();
    }
}


