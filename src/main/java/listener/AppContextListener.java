package listener;

import dao.MatchImpl;
import dao.PlayerDaoImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.*;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MatchImpl match = new MatchImpl();
        PlayerDaoImpl player = new PlayerDaoImpl();
        NewMatchService newMatchService = new NewMatchService();
        OngoingMatchService ongoingMatchService = new OngoingMatchService();
        FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService(player, match);
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute("match", match);
        context.setAttribute("player", player);
        context.setAttribute("newMatchService", newMatchService);
        context.setAttribute("ongoingMatchService", ongoingMatchService);
        context.setAttribute("finishedMatchesPersistenceService", finishedMatchesPersistenceService);
    }
}
