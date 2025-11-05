package util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException e) {
            log.error("hibernate.cfg.xml could not found or a session could not be created ");
            throw new RuntimeException(e); //TODO
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
