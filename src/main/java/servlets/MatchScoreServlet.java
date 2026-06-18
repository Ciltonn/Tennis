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
import service.FinishedMatchesPersistenceServiceImpl;
import service.OngoingMatchService;
import service.OngoingMatchServiceImpl;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchServiceImpl ongoingMatchService;
    private FinishedMatchesPersistenceServiceImpl finishedMatchesPersistenceService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.ongoingMatchService = (OngoingMatchServiceImpl) context.getAttribute(OngoingMatchService.class.getName());
        this.finishedMatchesPersistenceService = (FinishedMatchesPersistenceServiceImpl) context.getAttribute(FinishedMatchesPersistenceService.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String matchId = request.getParameter("uuid");
        if (matchId == null) {
            throw new NotFoundException("Match ID not found");
        }
        UUID uuid = UUID.fromString(matchId);
        CurrentMatch match = ongoingMatchService.getCurrentMatch(uuid);
        setMatchAttributes(request, match);
        request.getRequestDispatcher("/match-score.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int numberPlayerParam = Integer.parseInt(request.getParameter("number"));
        String matchIdString = request.getParameter("uuid");
        UUID matchId = UUID.fromString(matchIdString);
        CurrentMatch match = ongoingMatchService.getCurrentMatch(matchId);
        match.awardPointTo(numberPlayerParam);
        if (match.isFinished()) {
            finishedMatchesPersistenceService.finishMatch(match);
            setMatchAttributes(request, match);
            request.getRequestDispatcher("/match-result.jsp").forward(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + matchId);
        }

    }

    private void setMatchAttributes(HttpServletRequest request, CurrentMatch match) {
        request.setAttribute("match", match);
           }


}



