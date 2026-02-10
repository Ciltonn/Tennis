package dao;

import entity.Player;
import exception.DatabaseOperationException;
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
            Query<Player> query = session.createQuery("FROM Player p WHERE p.name = :name", Player.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            log.error("Error finding player by name: {}", name, e);
            throw new DatabaseOperationException("Player not found by name in database");
        }
    }

    @Override
    public Optional<Player> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Player player = session.get(Player.class, id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            log.error("Error finding player by ID: {}", id, e);
            throw new DatabaseOperationException("Player not found by id in database");
        }
    }

    @Override
    public Player save(Player player) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(player);
                transaction.commit();
                return player;
            } catch (Exception e) {
                log.error("Error saving player: {}", player.getName(), e);
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DatabaseOperationException("Failed to save player in database");
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
                } else {
                    log.warn("Player not found for deletion. ID: {}", id);
                    throw new DatabaseOperationException("Player not found for deletion");
                }
                transaction.commit();
            } catch (Exception e) {
                log.error("Error deleting player with ID: {}", id, e);
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DatabaseOperationException("Failed to delete match");
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
                    log.warn("Player not found for update. ID: {}", player.getId());
                    return Optional.empty();
                }
                Player managedPlayer = session.merge(player);
                transaction.commit();
                log.info("Player updated successfully. ID: {}", player.getId());
                return Optional.of(managedPlayer);
            } catch (Exception e) {
                log.error("Error updating player with ID: {}", player.getId(), e);
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DatabaseOperationException("Failed to update player from database");
            }
        }
    }

    @Override
    public List<Player> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Player", Player.class).list();
        } catch (Exception e) {
            log.error("Error retrieving all players", e);
            throw new DatabaseOperationException("Failed to retrieve list from database");
        }
    }
}
