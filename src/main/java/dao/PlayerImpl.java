package dao;

import entity.Player;
import exception.DatabaseOperationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerImpl implements PlayerDao {
    private static final String FIND_BY_NAME_HQL = """
            FROM Player p WHERE p.name = :name""";

    @Override
    public Optional<Player> findByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            return session.createQuery(FIND_BY_NAME_HQL, Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (HibernateException e) {
            log.error("Error finding player by name: {}", name, e);
            throw new DatabaseOperationException("Failed to retrieve player from database");
        }
    }

    @Override
    public Optional<Player> findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Player player = session.get(Player.class, id);
            return Optional.ofNullable(player);
        } catch (HibernateException e) {
            log.error("Error finding player by ID: {}", id, e);
            throw new DatabaseOperationException("Failed to retrieve player from database");
        }
    }

    @Override
    public Player save(Player player) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.persist(player);
            return player;
        } catch (HibernateException e) {
            log.error("Error saving player: {}", player.getName(), e);
            throw new DatabaseOperationException("Failed to save player in database");
        }
    }
    @Override
    public List<Player> findAll(int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            return session.createQuery("FROM Player", Player.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
        } catch (HibernateException e) {
            log.error("Database error while finding all players", e);
            throw new DatabaseOperationException("Failed to retrieve players from database");
        }
    }
    @Override
    public long count() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            return session.createQuery("SELECT COUNT(p) FROM Player p", Long.class)
                    .uniqueResult();
        } catch (HibernateException e) {
            log.error("Database error while counting players", e);
            throw new DatabaseOperationException("Failed to count players in database");
        }
    }
}

