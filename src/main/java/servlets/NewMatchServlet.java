package servlets;

import dao.PlayerImpl;
import dto.CurrentMatch;
import dto.PlayerRequest;
import entity.Player;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NewMatchService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private NewMatchService newMatchService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        this.newMatchService = (NewMatchService) context.getAttribute("player");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PlayerRequest playerRequest1 = new PlayerRequest(request.getParameter("playerOne"));
        PlayerRequest playerRequest2 = new PlayerRequest(request.getParameter("playerTwo"));

        CurrentMatch match = newMatchService.creatNewMatch(playerRequest1, playerRequest2);
        UUID match_id = match.getMatchId();

        response.sendRedirect("/match-score?uuid="+match_id);
    }
}
