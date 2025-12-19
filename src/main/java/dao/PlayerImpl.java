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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try  {
            Query<Player> query = session.createQuery("FROM Player WHERE name = :name", Player.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Player> findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Player player = session.get(Player.class, id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            session.close();
        }
    }


    @Override
    public Player save(Player player) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(player);
            transaction.commit();
            return player;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Player player = session.get(Player.class, id);

            if (player != null) {
                session.delete(player);
            }
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Player> update(Player player) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Player existing = session.get(Player.class, player.getId());
            if(existing == null) {
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
        } finally {
            session.close();
        }
    }

    @Override
    public List<Player> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List players;
        try {
            players = session.createQuery("FROM Player", Player.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return players;
    }

    @Override
    public boolean existsById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Player player = session.get(Player.class, id);
            return player != null;
        } catch (Exception e) {
            return false;
        } finally {
            session.close();
        }
    }
}
