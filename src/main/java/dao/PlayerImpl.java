package dao;

import entity.Player;
import jakarta.persistence.Id;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class PlayerImpl implements CrudDao<Player, Id> {

    @Override
    public Optional<Player> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Player player = session.createQuery("FROM Player WHERE name =: name", Player.class)
                    .setParameter("name", name)
                    .uniqueResult();
        return Optional.ofNullable(player);
        }
    }

    @Override
    public Player save(Player entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(entity);
        transaction.commit();
        session.close();
        return entity;
    }

    @Override
    public List<Player> findByAll(String nameFilter) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            if(nameFilter==null|| nameFilter.trim().isEmpty()) {
                return session.createQuery("FROM Player", Player.class).list();
            } else {
                return session.createQuery(
                        "FROM Player WHERE name LIKE :name", Player.class)
                        .setParameter("name", "%" + nameFilter + "%" )
                        .list();
            }
        }
    }
}
