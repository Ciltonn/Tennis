package dao;

import entity.Match;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class MatchImpl implements MatchCrud {

    @Override
    public Optional<Match> findById(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Match findingMatch = session.get(Match.class, id);
            return Optional.ofNullable(findingMatch);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Match save(Match match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(match);
                transaction.commit();

            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return match;
    }

    @Override
    public void delete(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Match deletedMatch = session.get(Match.class, id);
                session.remove(deletedMatch);
                transaction.commit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
                if (transaction != null) {
                    transaction.rollback();
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Match> findAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Match> matches;
            matches = session.createQuery("FROM Match", Match.class).list();
            return matches;
        } catch (Exception e) {
            return List.of();
        }

    }

    @Override
    public boolean existsById(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Match existing = session.get(Match.class, id);
            return existing != null;

        } catch (Exception e) {
            return false;
        }

    }
}
