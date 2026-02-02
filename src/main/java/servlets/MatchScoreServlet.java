package servlets;

import dao.PlayerImpl;
import dto.CurrentMatch;
import entity.Player;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MatchScoreCalculationService;
import service.OngoingMatchService;

import java.io.IOException;
import java.util.UUID;


@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchService ongoingMatchService;
    private PlayerImpl playerImpl;
    private MatchScoreCalculationService matchScoreCalculationService;


    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.ongoingMatchService = (OngoingMatchService) context.getAttribute("ongoingMatchService");
        this.playerImpl = (PlayerImpl) context.getAttribute("player");
        this.matchScoreCalculationService = (MatchScoreCalculationService) context.getAttribute("matchScoreCalculationService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String matchId = request.getParameter("uuid");
        UUID uuid = UUID.fromString(matchId);
        CurrentMatch match = ongoingMatchService.getCurrentMatch(uuid);
        Player player1 = playerImpl.findById(match.getIdPlayer1())
                .orElseThrow();
        Player player2 = playerImpl.findById(match.getIdPlayer2())
                .orElseThrow();


        request.setAttribute("matchId", match.getMatchId());
        request.setAttribute("player1Name", player1.getName());
        request.setAttribute("player2Name", player2.getName());
        request.setAttribute("sets1", match.getSets1());
        request.setAttribute("sets2", match.getSets2());
        request.setAttribute("games1", match.getGames1());
        request.setAttribute("games2", match.getGames2());
        request.setAttribute("points1", match.getPoints1());
        request.setAttribute("points2", match.getPoints2());
        request.setAttribute("isTieBreak", match.getMatchState().isTieBreak());
        request.getRequestDispatcher("/match-score.jsp").forward(request, response);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playerName = request.getParameter("player");
        String matchIdString = request.getParameter("uuid");
        UUID matchId = UUID.fromString(matchIdString);
        CurrentMatch match = matchScoreCalculationService.saveMatch(matchId, playerName);
        Player player1 = playerImpl.findById(match.getIdPlayer1())
                .orElseThrow();
        Player player2 = playerImpl.findById(match.getIdPlayer2())
                .orElseThrow();


        request.setAttribute("matchId", match.getMatchId().toString());
        request.setAttribute("player1Name", player1.getName());
        request.setAttribute("player2Name", player2.getName());
        request.setAttribute("sets1", match.getSets1());
        request.setAttribute("sets2", match.getSets2());
        request.setAttribute("games1", match.getGames1());
        request.setAttribute("games2", match.getGames2());
        request.setAttribute("points1", match.getPoints1());
        request.setAttribute("points2", match.getPoints2());


        request.getRequestDispatcher("/match-score.jsp").forward(request, response);

    }
}
