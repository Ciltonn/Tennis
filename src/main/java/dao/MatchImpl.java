package dao;

import entity.Match;
import exception.DatabaseOperationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MatchImpl implements MatchCrud {
    @Override
    public Optional<Match> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Match findingMatch = session.get(Match.class, id);
            return Optional.ofNullable(findingMatch);
        } catch (Exception e) {
            log.error("Error finding match by id: {}", id, e);
            throw new DatabaseOperationException("Match not found in database");
        }
    }

    @Override
    public Match save(Match match) {
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

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Match deletedMatch = session.get(Match.class, id);
                if (deletedMatch != null) {
                    session.remove(deletedMatch);
                    transaction.commit();
                } else {
                    transaction.rollback();
                    log.warn("Match not found for deletion. ID: {}", id);
                    throw new DatabaseOperationException("Match not found for deletion");
                }
            } catch (Exception e) {
                log.error("Error deleting match with ID: {}", id, e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
            throw new DatabaseOperationException("Failed to delete match");
        }
    }

    @Override
    public Optional<Match> update(Match match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(match);
                transaction.commit();
                return Optional.ofNullable(match);
            } catch (Exception e) {
                log.error("Error updating match with ID: {}", match.getId(), e);
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DatabaseOperationException("Failed to update match from database");
            }
        }
    }

    @Override
    public List<Match> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Match> matches;
            matches = session.createQuery("FROM Match", Match.class).list();
            return matches;
        } catch (Exception e) {
            log.error("Error retrieving all matches", e);
            throw new DatabaseOperationException("Failed to retrieve list of matches from database");
        }
    }

    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Match existing = session.get(Match.class, id);
            return existing != null;
        } catch (Exception e) {
            log.warn("Error checking if player exists. ID: {}", id, e);
            return false;
        }
    }
}
