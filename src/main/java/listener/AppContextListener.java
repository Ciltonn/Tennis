package listener;

import dao.MatchImpl;
import dao.PlayerImpl;
import dto.CurrentMatch;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.FinishedMatchesPersistenceService;
import service.MatchScoreCalculationService;
import service.NewMatchService;
import service.OngoingMatchService;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized (ServletContextEvent servletContextEvent) {
        MatchImpl match = new MatchImpl();
        PlayerImpl player = new PlayerImpl();
        NewMatchService newMatchService = new NewMatchService(player);
        OngoingMatchService ongoingMatchService = new OngoingMatchService();
        CurrentMatch currentMatch = new CurrentMatch();
        FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService(ongoingMatchService, player, match);
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService(player,finishedMatchesPersistenceService, ongoingMatchService);
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute("match", match);
        context.setAttribute("player", player);
        context.setAttribute("newMatchService", newMatchService);
        context.setAttribute("ongoingMatchService", ongoingMatchService);
        context.setAttribute("currentMatch", currentMatch);
        context.setAttribute("matchScoreCalculationService", matchScoreCalculationService);
        context.setAttribute("finishedMatchesPersistenceService", finishedMatchesPersistenceService);
    }
}
