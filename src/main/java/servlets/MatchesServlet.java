package servlets;

import dao.MatchImpl;
import dao.PlayerDaoImpl;
import entity.TennisMatch;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MatchService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private static final int PAGE_SIZE = 5;
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Инициализируем сервис
        MatchImpl matchDao = new MatchImpl();
        PlayerDaoImpl playerDao = new PlayerDaoImpl();
        this.matchService = new MatchService(matchDao, playerDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = Optional.ofNullable(request.getParameter("page")).map(Integer::parseInt).orElse(1) ;
        String filterByPlayerName = request.getParameter("filter_by_player_name");

        List<TennisMatch> matches = matchService.getMatches(filterByPlayerName,page, PAGE_SIZE);
        long totalMatches = matchService.getTotalMatchesCount(filterByPlayerName);
        int totalPages = (int) Math.ceil((double) totalMatches/PAGE_SIZE);
        if (totalPages<1) {
            totalPages=1;
        }
        request.setAttribute("matches", matches);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("totalMatches", totalMatches);
        request.setAttribute("filter_by_player_name", filterByPlayerName);

        request.getRequestDispatcher("/matches.jsp").forward(request, response);


    }
}
