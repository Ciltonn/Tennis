package dao;

import entity.TennisMatch;
import exception.DatabaseOperationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
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

    private static final String FIND_BY_PLAYER_HQL = """
            SELECT DISTINCT m FROM TennisMatch m
            LEFT JOIN FETCH m.firstPlayer
            LEFT JOIN FETCH m.secondPlayer
            LEFT JOIN FETCH m.winner
            WHERE m.firstPlayer.id = :playerId OR m.secondPlayer.id = :playerId
            ORDER BY m.id DESC """;

    private static final String COUNT_ALL_HQL = "SELECT COUNT(m) FROM TennisMatch m";

    private static final String COUNT_BY_PLAYER_HQL = """
            SELECT COUNT(m) FROM TennisMatch m
            WHERE m.firstPlayer.id = :playerId OR m.secondPlayer.id = :playerId""";

    @Override
    public Optional<TennisMatch> findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            TennisMatch findingMatch = session.get(TennisMatch.class, id);
            return Optional.ofNullable(findingMatch);
        } catch (Exception e) {
            log.error("Error finding match by id: {}", id, e);
            throw new DatabaseOperationException("Match not found in database");
        }
    }

    @Override
    public TennisMatch save(TennisMatch match) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.persist(match);
            return match;
        } catch (Exception e) {
            log.error("Error saving match: {}", match.getId(), e);
            throw new DatabaseOperationException("Failed to save match");
        }
    }


    public List<TennisMatch> findAllWithPagination(int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Query<TennisMatch> query = session.createQuery(
                    FIND_ALL_MATCHES_WITH_PLAYERS,
                    TennisMatch.class);
            query.setFirstResult(offset)
            .setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to retrieve matches with pagination");
        }
    }

    public List<TennisMatch> findAllByPlayerNameWithPagination(Long playerId, int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Query<TennisMatch> query = session.createQuery(
                    FIND_BY_PLAYER_HQL, TennisMatch.class);
            query.setParameter("playerId", playerId);
            query.setFirstResult(offset)
            .setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to retrieve matches for player with pagination");
        }
    }

    public Long countMatchForPagination() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Query<Long> query = session.createQuery(COUNT_ALL_HQL, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to count matches");
        }
    }

    public Long countMatchesForPlayerForPagination(Long playerId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Query<Long> query = session.createQuery(
                    COUNT_BY_PLAYER_HQL, Long.class);
            query.setParameter("playerId", playerId);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to count matches for player");
        }
    }
}

