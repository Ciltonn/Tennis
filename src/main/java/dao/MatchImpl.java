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

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Match findingMatch = session.get(Match.class, id);
            return Optional.ofNullable(findingMatch);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    @Override
    public Match save(Match match) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(match);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return match;
    }

    @Override
    public void delete(Long id) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Match deletedMatch = session.get(Match.class, id);
            session.delete(deletedMatch);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }

    @Override
    public Optional<Match> update(Match match) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(match);
            transaction.commit();
            return Optional.ofNullable(match);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public List<Match> findAll() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Match> matches;
        try {
            matches = session.createQuery("FROM Match", Match.class).list();
            return matches;
        } catch (Exception e) {
            return List.of();
        } finally {
            session.close();
        }

    }

    @Override
    public boolean existsById(Long id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Match existing = session.get(Match.class, id);
            if (existing != null) {
                return existing !=null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
