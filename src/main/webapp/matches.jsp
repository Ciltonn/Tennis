<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Matches</h1>

        <form action="${pageContext.request.contextPath}/matches" method="get" class="input-container">
            <input class="input-filter"
                   placeholder="Filter by player name"
                   type="text"
                   name="filter_by_player_name"
                   value="${param.filter_by_player_name}" />
            <button type="submit" class="btn-filter">Apply</button>

        </form>

        <table class="table-matches">
            <tr>
                <th>Player One</th>
                <th>Player Two</th>
                <th>Winner</th>
            </tr>

            <c:choose>
                <c:when test="${not empty matches}">
                    <c:forEach var="match" items="${matches}">
                        <tr>
                            <td>${match.playerFirst.name}</td>
                            <td>${match.playerSecond.name}</td>
                            <td><span class="winner-name-td">${match.winner.name}</span></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="3" style="text-align: center; padding: 20px;">
                            No matches found
                            <c:if test="${not empty param.filter_by_player_name}">
                                for player "${param.filter_by_player_name}"
                            </c:if>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>

        <c:if test="${not empty totalPages and totalPages > 1}">
            <div class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == page}">
                            <span class="num-page current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <c:url var="pageUrl" value="/matches">
                                <c:param name="page" value="${i}"/>
                                <c:if test="${not empty param.filter_by_player_name}">
                                    <c:param name="filter_by_player_name" value="${param.filter_by_player_name}"/>
                                </c:if>
                            </c:url>
                            <a class="num-page" href="${pageUrl}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from
            <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a> roadmap.
        </p>
    </div>
</footer>
</body>
</html>