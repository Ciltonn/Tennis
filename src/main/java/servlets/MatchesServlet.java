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
import java.util.Optional;


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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = Optional.ofNullable(request.getParameter("page")).map(Integer::parseInt).orElse(1) ;
        String filterByPlayerName = request.getParameter("filter_by_player_name");

        List<Match> matches = matchesService.getMatches(filterByPlayerName,page, PAGE_SIZE);
        Long totalMatches = matchesService.getTotalMatchesCount();
        int totalPages =1;
        totalPages = (int) Math.ceil((double) totalMatches/PAGE_SIZE);
        request.setAttribute("matches", matches);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("totalMatches", totalMatches);
        request.setAttribute("filter_by_player_name", filterByPlayerName);

        request.getRequestDispatcher("/matches.jsp").forward(request, response);


    }
}
