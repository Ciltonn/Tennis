package listener;

import dao.MatchImpl;
import dao.PlayerImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized (ServletContextEvent servletContextEvent) {
        MatchImpl match = new MatchImpl();
        PlayerImpl player = new PlayerImpl();
        ServletContext context = servletContextEvent.getServletContext();
        context.setAttribute("match", match);
        context.setAttribute("player", player);
    }
}
