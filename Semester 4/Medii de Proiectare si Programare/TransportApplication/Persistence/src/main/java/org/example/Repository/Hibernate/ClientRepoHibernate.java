package org.example.Repository.Hibernate;

import org.example.Domain.Client;
import org.example.Repository.ClientRepoInterface;
import org.example.Repository.utils.HibernateUtils;
import org.hibernate.Session;

import java.util.Objects;
import java.util.Optional;

public class ClientRepoHibernate implements ClientRepoInterface {

    @Override
    public Optional<Client> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery("from Client where id=:idC ", Client.class)
                    .setParameter("idC", id)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Client> findAll() {
        try( Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Client ", Client.class).getResultList();
        }
    }

    @Override
    public Optional<Client> save(Client entity) {
        return HibernateUtils.getSessionFactory().fromTransaction(session -> {
            if(entity.getId() != null) {
                Client client = session.get(Client.class, entity.getId());
                if (client != null) {
                    return Optional.of(entity);
                }
            }
            session.persist(entity);
            return Optional.empty();
        });
    }

    @Override
    public Optional<Client> delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Client client=session.createQuery("from Client where id=?1",Client.class).
                    setParameter(1,id).uniqueResult();
            System.out.println("In delete am gasit clientul "+client);
            if (client!=null) {
                session.remove(client);
                session.flush();
            }
        });
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (!Objects.isNull(session.find(Client.class, entity.getId()))) {
                System.out.println("In update, am gasit clientul cu id-ul "+entity.getId());
                session.merge(entity);
                session.flush();
            }
        });
        return Optional.of(entity);
    }
}
