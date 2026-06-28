package servlets;

import dto.PlayerRequestDto;
import model.CurrentMatch;
import exception.InvalidParameterException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NewMatchService;
import service.OngoingMatchService;
import java.io.IOException;
import java.util.UUID;
import static util.ValidationUtil.validate;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private NewMatchService newMatchService;
    private OngoingMatchService ongoingMatchService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.newMatchService = (NewMatchService) context.getAttribute(NewMatchService.class.getName());
        this.ongoingMatchService = (OngoingMatchService) context.getAttribute(OngoingMatchService.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/new-match.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String firstPlayer = request.getParameter("firstPlayer");
        String secondPlayer = request.getParameter("secondPlayer");
        PlayerRequestDto playerRequest = new PlayerRequestDto(firstPlayer, secondPlayer);
        validate(playerRequest);
        try {
            CurrentMatch match = newMatchService.createNewMatch(firstPlayer, secondPlayer);
            UUID matchId = match.getMatchId();
            String contextPath = request.getContextPath();
            ongoingMatchService.saveCurrentMatch(matchId, match);
            response.sendRedirect(contextPath + "/match-score?uuid=" + matchId);
        } catch (InvalidParameterException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/new-match.jsp").forward(request, response);
        }
    }
}
