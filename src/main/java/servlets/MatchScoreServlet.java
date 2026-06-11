package servlets;

import model.CurrentMatch;
import exception.NotFoundException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.FinishedMatchesPersistenceService;
import service.OngoingMatchService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchService ongoingMatchService;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.ongoingMatchService = (OngoingMatchService) context.getAttribute("ongoingMatchService");
        this.finishedMatchesPersistenceService = (FinishedMatchesPersistenceService) context.getAttribute("finishedMatchesPersistenceService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String matchId = request.getParameter("uuid");
        if (matchId == null) {
            throw new NotFoundException("Match ID not found");
        }
        UUID uuid = UUID.fromString(matchId);
        CurrentMatch match = ongoingMatchService.getCurrentMatch(uuid);
        setAttribute(request, match);
        request.setAttribute("matchId", match.getMatchId());
        //request.setAttribute("isTieBreak", match.getMatchState().isTieBreak());
        request.getRequestDispatcher("/match-score.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int numberPlayerParam = Integer.parseInt(request.getParameter("number"));
        String matchIdString = request.getParameter("uuid");
        UUID matchId = UUID.fromString(matchIdString);
        CurrentMatch match = ongoingMatchService.getCurrentMatch(matchId);
        match.pointWon(numberPlayerParam);
        finishedMatchesPersistenceService.finishMatch(match);

        request.setAttribute("winner", match.getWinner());
        request.setAttribute("firstPlayer", match.getFirstPlayer());
        request.setAttribute("secondPlayer", match.getSecondPlayer());
        request.getRequestDispatcher("/match-result.jsp").forward(request, response);

        setAttribute(request, match);
        request.setAttribute("matchId", match.getMatchId());
        request.getRequestDispatcher("/match-score.jsp").forward(request, response);
    }
    public void setAttribute(HttpServletRequest request, CurrentMatch match) {
        request.setAttribute("player1Name", match.getFirstPlayer());
        request.setAttribute("player2Name", match.getSecondPlayer());
        request.setAttribute("sets", match.getSets());
        request.setAttribute("games1", match.getCurrentGame());
    }
}



