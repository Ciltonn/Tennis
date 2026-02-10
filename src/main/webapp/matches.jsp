<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">

    <script src="js/app.js"></script>
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
                  <input
                          class="input-filter"
                          placeholder="Filter by name"
                          type="text"
                          name="filter_by_player_name"
                          value="${filter_by_player_name}"
                  />

                  <button type="submit" class="btn-filter">Apply</button>

                  <a href="${pageContext.request.contextPath}/matches">
                      <button type="button" class="btn-filter">Reset Filter</button>
                  </a>
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
                              <td>${match.player1.name}</td>
                              <td>${match.player2.name}</td>
                              <td>
                                  <span class="winner-name-td">${match.winner.name}</span>
                              </td>
                          </tr>
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                   <tr>
                     <td colspan="3" style="text-align: center; padding: 20px;">
                                                No matches found
                      <c:if test="${not empty errorMessage}">
                          <p class="error-message" style="color:red">${errorMessage}</p>
                      </c:if>
                        </td>
                    </tr>
                    </c:otherwise>
                    </c:choose>
                  </table>
                  <c:if test="${totalPages > 1}">
                      <div class="pagination">

                          <c:choose>
                              <c:when test="${page > 1}">
                                  <a class="prev"
                                     href="${pageContext.request.contextPath}/matches?page=${page - 1}&filter_by_player_name=${filter_by_player_name}">
                                      <
                                  </a>
                              </c:when>
                              <c:otherwise>
                                  <span class="prev disabled"><</span>
                              </c:otherwise>
                          </c:choose>

                          <c:forEach begin="1" end="${totalPages}" var="p">
                              <c:choose>
                                  <c:when test="${p == page}">
                                      <span class="num-page current">${p}</span>
                                  </c:when>
                                  <c:otherwise>
                                      <a class="num-page"
                                         href="${pageContext.request.contextPath}/matches?page=${p}&filter_by_player_name=${filter_by_player_name}">
                                              ${p}
                                      </a>
                                  </c:otherwise>
                              </c:choose>
                          </c:forEach>

                          <c:choose>
                              <c:when test="${page < totalPages}">
                                  <a class="next"
                                     href="${pageContext.request.contextPath}/matches?page=${page + 1}&filter_by_player_name=${filter_by_player_name}">
                                      >
                                  </a>
                              </c:when>
                              <c:otherwise>
                                  <span class="next disabled">></span>
                              </c:otherwise>
                          </c:choose>

                      </div>
                  </c:if>
              </div>
          </main>
          <footer>
              <div class="footer">
                  <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
                      roadmap.</p>
              </div>
          </footer>
          </body>
          </html>