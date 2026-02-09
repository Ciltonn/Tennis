package servlets;

import dto.CurrentMatch;
import dto.PlayerRequest;
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
        this.newMatchService = (NewMatchService) context.getAttribute("newMatchService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PlayerRequest playerRequest1 = new PlayerRequest();
        playerRequest1.setName(request.getParameter("playerOne"));
        PlayerRequest playerRequest2 = new PlayerRequest();
        playerRequest2.setName(request.getParameter("playerTwo"));

        CurrentMatch match = newMatchService.createNewMatch(playerRequest1, playerRequest2);
        UUID match_id = match.getMatchId();

        String contextPath = request.getContextPath();

        response.sendRedirect(contextPath+"/match-score?uuid=" + match_id);
    }
}
