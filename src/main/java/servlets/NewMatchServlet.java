package servlets;

import dao.PlayerImpl;
import dto.PlayerRequest;
import entity.Player;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NewMatchService;

@WebServlet ("/new-match")
public class NewMatchServlet extends HttpServlet {

    private final PlayerImpl playerImpl = new PlayerImpl();
    private final NewMatchService newMatchService = new NewMatchService();

    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) {

        String player1 = request.getParameter("name1");
        String player2 = request.getParameter("name2");

        PlayerRequest playerRequest1 = new PlayerRequest(player1);
        PlayerRequest playerRequest2 = new PlayerRequest(player2);

        Player playerOne = newMatchService.creatOrSavePlayer(playerRequest1);
        Player playerTwo = newMatchService.creatOrSavePlayer(playerRequest2);

        dto.NewMatch newMatch = newMatchService.creatMatch(playerOne, playerTwo);

        response.encodeRedirectURL("/match-score?uuid=match_id");
    }
}
