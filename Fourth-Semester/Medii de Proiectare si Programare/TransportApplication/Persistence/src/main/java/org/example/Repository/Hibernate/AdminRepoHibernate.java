package org.example.Repository.Hibernate;

import org.example.Domain.Admin;
import org.example.Domain.Client;
import org.example.Repository.AdminRepoInterface;
import org.example.Repository.utils.HibernateUtils;
import org.hibernate.Session;

import java.util.Objects;
import java.util.Optional;

public class AdminRepoHibernate implements AdminRepoInterface {
    @Override
    public Optional<Admin> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery("from Admin where id=:idA ", Admin.class)
                    .setParameter("idA", id)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Admin> findAll() {
        try( Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Admin ", Admin.class).getResultList();
        }
    }

    @Override
    public Optional<Admin> save(Admin entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return Optional.of(entity);
    }

    @Override
    public Optional<Admin> delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Admin admin=session.createQuery("from Admin where id=?1",Admin.class).
                    setParameter(1,id).uniqueResult();
            System.out.println("In delete am gasit adminul "+admin);
            if (admin!=null) {
                session.remove(admin);
                session.flush();
            }
        });
        return Optional.empty();
    }

    @Override
    public Optional<Admin> update(Admin entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (!Objects.isNull(session.find(Admin.class, entity.getId()))) {
                System.out.println("In update, am gasit adminul cu id-ul "+entity.getId());
                session.merge(entity);
                session.flush();
            }
        });
        return Optional.of(entity);
    }
}
