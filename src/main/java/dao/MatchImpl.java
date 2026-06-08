package dao;

import entity.TennisMatch;
import exception.DatabaseOperationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MatchImpl implements MatchDao {
    private static final String FIND_ALL_MATCHES_WITH_PLAYERS = """
                            SELECT DISTINCT m FROM TennisMatch m
                            LEFT JOIN FETCH m.firstPlayer
                            LEFT JOIN FETCH m.secondPlayer
                            LEFT JOIN FETCH m.winner
                            ORDER BY m.id DESC
""";
    @Override
    public Optional<TennisMatch> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TennisMatch findingMatch = session.get(TennisMatch.class, id);
            return Optional.ofNullable(findingMatch);
        } catch (Exception e) {
            log.error("Error finding match by id: {}", id, e);
            throw new DatabaseOperationException("Match not found in database");
        }
    }

    @Override
    public TennisMatch save(TennisMatch match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(match);
                transaction.commit();
                return match;
            } catch (Exception e) {
                log.error("Error saving match: {}", match.getId(), e);
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DatabaseOperationException("Failed to save match");
            }
        }
    }

      public List<TennisMatch> findAllWithPagination(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int offset = (page - 1) * pageSize;
            Query<TennisMatch> query = session.createQuery(
                    FIND_ALL_MATCHES_WITH_PLAYERS,
                    TennisMatch.class);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to retrieve matches with pagination");
        }
    }

    public List<TennisMatch> findAllByPlayerNameWithPagination(Long playerId, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int offset = (page - 1) * pageSize;
            Query<TennisMatch> query = session.createQuery(
                    "SELECT DISTINCT m FROM TennisMatch m " +
                            "LEFT JOIN FETCH m.firstPlayer " +
                            "LEFT JOIN FETCH m.secondPlayer " +
                            "LEFT JOIN FETCH m.winner " +
                            "WHERE m.firstPlayer.id = :playerId OR m.secondPlayer.id = :playerId " +
                            "ORDER BY m.id DESC",
                    TennisMatch.class);
            query.setParameter("playerId", playerId);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to retrieve matches for player with pagination");
        }
    }

    public Long countMatchForPagination() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT (m) FROM TennisMatch m", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to count matches");
        }
    }

    public Long countMatchesForPlayerForPagination(Long playerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT (m) FROM TennisMatch m WHERE m.firstPlayer.id = :playerId OR m.secondPlayer.id = :playerId", Long.class);
            query.setParameter("playerId", playerId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to count matches for player");
        }
    }
}
