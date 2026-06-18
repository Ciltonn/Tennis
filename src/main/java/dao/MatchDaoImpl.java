package dao;

import entity.TennisMatch;
import exception.DatabaseOperationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MatchDaoImpl implements MatchDao {
    private static final String FIND_ALL_MATCHES_WITH_PLAYERS = """
                                        SELECT DISTINCT m FROM TennisMatch m
                                        LEFT JOIN FETCH m.firstPlayer
                                        LEFT JOIN FETCH m.secondPlayer
                                        LEFT JOIN FETCH m.winner
                                        ORDER BY m.id DESC
            """;

    private static final String FIND_BY_PLAYER_NAME_HQL = """
            SELECT DISTINCT m FROM TennisMatch m
            LEFT JOIN FETCH m.firstPlayer
            LEFT JOIN FETCH m.secondPlayer
            LEFT JOIN FETCH m.winner
            WHERE m.firstPlayer.name = :playerName OR m.secondPlayer.name = :playerName
            ORDER BY m.id DESC""";

    private static final String COUNT_ALL_HQL = "SELECT COUNT(m) FROM TennisMatch m";

    private static final String COUNT_BY_PLAYER_NAME_HQL = """
            SELECT COUNT(m) FROM TennisMatch m
            WHERE m.firstPlayer.name = :playerName OR m.secondPlayer.name = :playerName""";

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

    @Override
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

    @Override
    public Long countMatchForPagination() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Query<Long> query = session.createQuery(COUNT_ALL_HQL, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to count matches");
        }
    }

    @Override
    public List<TennisMatch> findByPlayerName(String playerName, int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            return session.createQuery(FIND_BY_PLAYER_NAME_HQL, TennisMatch.class)
                    .setParameter("playerName", playerName)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
        } catch (HibernateException e) {
            log.error("Failed to retrieve matches for player: {}", playerName, e);
            throw new DatabaseOperationException(
                    String.format("Failed to retrieve matches for player: %s", playerName),
                    e
            );
        }
    }

    @Override
    public Long countByPlayerName(String playerName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Long count = session.createQuery(COUNT_BY_PLAYER_NAME_HQL, Long.class)
                    .setParameter("playerName", playerName)
                    .uniqueResult();
            return count != null ? count : 0L;
        } catch (HibernateException e) {
            log.error("Failed to count matches for player: {}", playerName, e);
            throw new DatabaseOperationException(
                    String.format("Failed to count matches for player: %s", playerName),
                    e
            );
        }
    }
}

