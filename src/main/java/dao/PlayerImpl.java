package dao;

import entity.Player;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerImpl implements PlayerCrud {


    @Override
    public Optional<Player> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Player> query = session.createQuery("FROM Player WHERE name = :name", Player.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Player player = session.get(Player.class, id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    @Override
    public Player save(Player player) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(player);
                transaction.commit();
                return player;

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Player player = session.get(Player.class, id);

                if (player != null) {
                    session.remove(player);
                }
                transaction.commit();

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<Player> update(Player player) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Player existing = session.get(Player.class, player.getId());
                if (existing == null) {
                    return Optional.empty();
                }
                Player managedPlayer = session.merge(player);
                transaction.commit();
                return Optional.of(managedPlayer);
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Player> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Player", Player.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Player player = session.get(Player.class, id);
            return player != null;
        } catch (Exception e) {
            return false;
        }
    }
}
