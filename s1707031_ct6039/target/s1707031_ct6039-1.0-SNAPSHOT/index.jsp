<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--Landing Index page for all users--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Homepage</title>
</head>
    <body class="d-flex flex-column">
        <jsp:include page="jsp/required.jsp"/>
        <link rel="stylesheet" href="css/main.css">

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
                                if(email != null) { %>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=calendar">Calendar</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=progress-view">Progress</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <%} else { %>
                            <li class="nav-item">
                                <a class="nav-link">You must be signed in to access site features</a>
                            </li>
                            <% } %>
                        </ul>
                        <%--Login/Register Side of navbar. if logged in, show logout/account links--%>
                        <span class="navbar-text">
                        <% if(email != null) { %>
                            <% String isChild = (String) session.getAttribute("isChild");
                                String isTeacher = (String) session.getAttribute("isTeacher");
                                String isParent = (String) session.getAttribute("isParent");
                                if(isChild != null) { %>
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

            <% String errors = (String) session.getAttribute("formErrors");
                if(errors != null) { %>
            <div class="alert alert-danger" role="alert" id="formErrors"><%=errors%></div>
            <%}%>
            <% String success = (String) session.getAttribute("formSuccess");
                if(success != null) { %>
            <div class="alert alert-success" role="alert" id="formSuccess"><%=success%></div>
            <%}%>

            <%--Title--%>
            <div class="main-body-content">
                <h1><%="Welcome! Index Homepage"%></h1>
                <br/>
            </div>

            <% if(email != null) { %>
                <div class="main-body-content">
                    <p>
                        <%="You are already logged in! Please use the menu at the top of the page to go to a page you want."%>
                    </p>
                </div>
            <%} else { %>
                <div class="row card-index" style="width: 100%">
                    <div style="width: 12.5%">
                    </div>
                    <div class="d-flex align-items-stretch" style="width:25%; display: inline-block">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">For Parents/Guardians</h5>
                                <p class="card-text-center">Using this Portal allows you to check your child's progress, including homework submission dates.</p>
                            </div>
                            <div class="" style="margin:auto; padding-bottom: 10%">
                                <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-login>&nbsp;Parent Login&nbsp;</a>
                                &nbsp;
                                <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-register>&nbsp;Register Parent&nbsp;</a>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex align-items-stretch" style="width:25%; display: inline-block">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">For Children</h5>
                                <p class="card-text-center">If you are a child accessing this site, please use the following links:</p>
                            </div>
                            <div class="" style="margin:auto; padding-bottom: 10%">
                                <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a>
                                &nbsp;
                                <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=child-register>&nbsp;Child Register&nbsp;</a>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex align-items-stretch" style="width:25%; display: inline-block">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">For Teachers</h5>
                                <p class="card-text-center">Teachers accessing this site can update Homework and Lesson Files from their account.</p>
                            </div>
                            <div class="" style="margin:auto; padding-bottom: 10%">
                                <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-login>&nbsp;Teacher Login&nbsp;</a>
                                &nbsp;
                                <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-register>&nbsp;Teacher Register&nbsp;</a>
                            </div>
                        </div>
                    </div>
                    <div style="width: 12.5%">
                    </div>
                </div>
                <br/>
            <% } %>

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