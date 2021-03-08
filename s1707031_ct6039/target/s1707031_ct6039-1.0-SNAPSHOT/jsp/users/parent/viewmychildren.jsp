<%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 06/03/2021
  Time: 12:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>My Children</title>
    </head>

    <body>
        <jsp:include page="../../required.jsp"/>
        <link rel="stylesheet" href="../../../css/main.css">

        <div class="content">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="container-fluid">
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/servlets/Redirects?location=home"><%="School Site"%></a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarText">
                        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/servlets/Redirects?location=home">Home</a>
                            </li>
                            <%--If logged in, show nav links, else just have home & account signup/login visible--%>
                            <% String email = (String) session.getAttribute("email");
                                String isChild = (String) session.getAttribute("isChild");
                                String isTeacher = (String) session.getAttribute("isTeacher");
                                String isParent = (String) session.getAttribute("isParent");
                                if(email != null) { %>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=calendar">Calendar</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=progress-view">Progress</a>
                            </li>
                            <%if(isChild != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <% } else if(isTeacher != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=class-view">My Classes</a>
                            </li>
                            <% } else if(isParent != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=view-child">My Children</a>
                            </li>
                            <%}%>

                            <%} else { %>
                            <li class="nav-item">
                                <a class="nav-link">You must be signed in to access site features</a>
                            </li>
                            <% } %>
                        </ul>
                        <%--Login/Register Side of navbar. if logged in, show logout/account links--%>
                        <span class="navbar-text">
                                <% if(email != null) { %>
                                    <%if(isChild != null) { %>
                                            <a class="nav-link navbar-login-info"><%="Logged in as: "%><%=email%></a>
                                            <button class="btn btn-sm btn-outline-secondary" type="button">
                                                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-profile>&nbsp;My account&nbsp;</a>
                                            </button>
                                            &nbsp;
                                            <button class="btn btn-sm btn-outline-secondary" type="button">
                                                <a href=${pageContext.request.contextPath}/servlets/users/child/ChildLogout>&nbsp;Logout&nbsp;</a>
                                            </button>
                                        <% } else if(isParent != null) { %>
                                            <a class="nav-link navbar-login-info"><%="Logged in as: "%><%=email%></a>
                                            <button class="btn btn-sm btn-outline-secondary" type="button">
                                                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-profile>&nbsp;My account&nbsp;</a>
                                            </button>
                                            &nbsp;
                                            <button class="btn btn-sm btn-outline-secondary" type="button">
                                                <a href=${pageContext.request.contextPath}/servlets/users/parent/ParentLogout>&nbsp;Logout&nbsp;</a>
                                            </button>
                                        <% } else if(isTeacher != null) { %>
                                            <a class="nav-link navbar-login-info"><%="Logged in as: "%><%=email%></a>
                                            <button class="btn btn-sm btn-outline-secondary" type="button">
                                                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-profile>&nbsp;My account&nbsp;</a>
                                            </button>
                                            &nbsp;
                                            <button class="btn btn-sm btn-outline-secondary" type="button">
                                                <a href=${pageContext.request.contextPath}/servlets/users/teacher/TeacherLogout>&nbsp;Logout&nbsp;</a>
                                            </button>
                                        <% } %>
                                <%} else { %>
                                    <a class="nav-link navbar-login-info"><%="You are not logged in"%></a>
                                    <button class="btn btn-sm btn-outline-secondary" type="button">
                                    <a href=${pageContext.request.contextPath}/servlets/Redirects?location=login>&nbsp;Login&nbsp;</a>
                                    </button>
                                    &nbsp;
                                    <button class="btn btn-sm btn-outline-secondary" type="button">
                                        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=register>&nbsp;Register&nbsp;</a>
                                    </button>
                                <% } %>
                            </span>
                    </div>
                </div>
            </nav>

            <%--Title--%>
            <div class="main-body-content">
                <h1><%="My Children"%></h1>
                <br/>
            </div>

            <p class="main-body-text">
                <%="As a Parent, any linked children will appear below, along with child information regarding homework submissions and class progress."%>
            </p>

            <%--Create cards for each linked child?--%>
        </div>
        <footer class="footer">
            <div class="">
                <span class="text-muted">CT6039 Project by S1707031 &copy;2021</span>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=home>&nbsp;Return Home&nbsp;</a>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-login>&nbsp;Parent Login&nbsp;</a>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-login>&nbsp;Teacher Login&nbsp;</a>
            </div>
        </footer>
    </body>
</html>