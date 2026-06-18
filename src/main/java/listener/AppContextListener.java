package listener;

import dao.MatchDao;
import dao.MatchDaoImpl;
import dao.PlayerDao;
import dao.PlayerDaoImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import service.*;
import util.HibernateUtil;

@Slf4j
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MatchDaoImpl matchDao = new MatchDaoImpl();
        PlayerDaoImpl playerDao = new PlayerDaoImpl();
        NewMatchServiceImpl newMatchService = new NewMatchServiceImpl();
        OngoingMatchServiceImpl ongoingMatchService = new OngoingMatchServiceImpl();
        FinishedMatchesPersistenceServiceImpl finishedMatchesPersistenceService = new FinishedMatchesPersistenceServiceImpl(playerDao, matchDao);
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute(MatchDao.class.getName(), matchDao);
        context.setAttribute(PlayerDao.class.getName(), playerDao);
        context.setAttribute(NewMatchService.class.getName(), newMatchService);
        context.setAttribute(OngoingMatchService.class.getName(), ongoingMatchService);
        context.setAttribute(FinishedMatchesPersistenceService.class.getName(), finishedMatchesPersistenceService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.shutdown();
        log.info("Application context destroyed");
    }
}
