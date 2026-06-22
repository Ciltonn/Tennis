package util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public final class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException e) {
            log.error("hibernate.cfg.xml could not found or a session could not be created ");
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", e);
        }
    }

    public HibernateUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            log.info("Hibernate SessionFactory closed");
        }
    }
}

