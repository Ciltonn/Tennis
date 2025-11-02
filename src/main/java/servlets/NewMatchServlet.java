package servlets;

import dto.MatchRequestDto;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewMatchServlet extends HttpServlet {

private MatchRequestDto matchRequestDto = new MatchRequestDto();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        String name1 = request.getParameter("name1");
        String name2 = request.getParameter("name2");

        MatchRequestDto matchRequestDto1 = new MatchRequestDto(name1, name2);
    }
}
