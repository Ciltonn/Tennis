package servlets;

import dao.MatchImpl;
import dao.PlayerImpl;
import entity.Match;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MatchesService;
import java.io.IOException;
import java.util.List;


@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private static final int PAGE_SIZE = 5;
    private MatchesService matchesService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        MatchImpl match = (MatchImpl) context.getAttribute("match");
        PlayerImpl player = (PlayerImpl) context.getAttribute("player");
        this.matchesService = new MatchesService(match, player);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
    List<Match> matches = matchesService.getMatches();
    request.setAttribute("matches", matches);
    request.getRequestDispatcher("/matches.jsp").forward(request, response);


    }
}
