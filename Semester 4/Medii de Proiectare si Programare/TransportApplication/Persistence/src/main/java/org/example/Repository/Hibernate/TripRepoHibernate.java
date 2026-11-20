package org.example.Repository.Hibernate;

import org.example.Domain.Admin;
import org.example.Domain.Trip;
import org.example.Repository.TripRepoInterface;
import org.example.Repository.utils.HibernateUtils;
import org.hibernate.Session;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class TripRepoHibernate implements TripRepoInterface {
    @Override
    public Optional<Trip> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery("from Trip where id=:idT ", Trip.class)
                    .setParameter("idT", id)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Trip> findAll() {
        try( Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Trip ", Trip.class).getResultList();
        }
    }

    @Override
    public Optional<Trip> save(Trip entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();

            String dateStr = entity.getDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            session.createNativeQuery(
                            "INSERT INTO Trips(destination, date, nrSeats) VALUES(:dest, :dt, :nr)"
                    )
                    .setParameter("dest", entity.getDestination())
                    .setParameter("dt",   dateStr)
                    .setParameter("nr",   entity.getNrSeats())
                    .executeUpdate();

            Number lastId = (Number) session
                    .createNativeQuery("SELECT last_insert_rowid()")
                    .getSingleResult();
            entity.setId(lastId.intValue());

            session.getTransaction().commit();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Trip> delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Trip trip=session.createQuery("from Trip where id=?1",Trip.class).
                    setParameter(1,id).uniqueResult();
            System.out.println("In delete am gasit tripul "+trip);
            if (trip!=null) {
                session.remove(trip);
                session.flush();
            }
        });
        return Optional.empty();
    }

    @Override
    public Optional<Trip> update(Trip entity) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();

            String dateStr = entity.getDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            int updated = session.createNativeQuery(
                            "UPDATE Trips " +
                                    "SET destination = :dest, date = :dt, nrSeats = :nr " +
                                    "WHERE id = :id")
                    .setParameter("dest", entity.getDestination())
                    .setParameter("dt",   dateStr)
                    .setParameter("nr",   entity.getNrSeats())
                    .setParameter("id",   entity.getId())
                    .executeUpdate();

            session.getTransaction().commit();

            if (updated == 0) {
                return Optional.empty();
            }
        }
        return Optional.of(entity);
    }

}

