<%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 19/02/2021
  Time: 14:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Login Index</title>
        <link rel="icon" type="image/gif" href="${pageContext.request.contextPath}/imgs/favicon.ico">
    </head>
    <body>
        <jsp:include page="../required.jsp"/>
        <link rel="stylesheet" href="../../css/main.css">

        <div class="content">
            <nav class="navbar navbar-expand-lg navbar-light bg-light mynav">
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
                            <%if(isChild != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <% } else if(isTeacher != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=class-view">My Classes</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
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

            <% String errors = (String) session.getAttribute("formErrors");
                if(errors != null) { %>
            <div class="alert alert-danger myalert" role="alert" id="formErrors"><%=errors%></div>
            <%}%>
            <% String success = (String) session.getAttribute("formSuccess");
                if(success != null) { %>
            <div class="alert alert-success mysuccess" role="alert" id="formSuccess"><%=success%></div>
            <%}%>

            <%--Title--%>
            <div class="main-body-content myheader neat">
                <h1 class="myheader neat"><%="Login Index"%></h1>
                <br/>
            </div>

            <p class="main-body-text myPara neat">
                <%="Please select how you want to log in."%>
            </p>
            <br/>

            <div class="row card-index myheader neat" style="width: 100%">
                <div style="width: 12.5%">
                </div>
                <div class="d-flex align-items-stretch" style="width:25%; display: inline-block">
                    <div class="card shadow p-3 mb-5 bg-white rounded">
                        <form class="" action="${pageContext.request.contextPath}/servlets/users/parent/ParentLogin" method="GET">
                            <div class="card-body formParaText">
                                <h5 class="card-title formPara">For Parents</h5>
                                <label for="parentEmail" class="form-label formPara"><%="Email"%></label>
                                <input class="form-control formParaText" type="email" name="parentEmail" id="parentEmail" required/>
                                <br/>
                                <label for="parentPword" class="form-label formPara"><%="Password"%></label>
                                <input class="form-control formParaText" type="password" name="parentPword" id="parentPword" minlength="8" required/>
                                <br/>
                            </div>
                            <div class="myformbtn" style="margin:auto; padding-bottom: 10%">
                                <input class="btn btn-primary classFormBtn formParaText" type="reset" value="Clear">
                                <input class="btn btn-primary classFormBtn formParaText" type="submit" value="Submit">
                            </div>
                        </form>
                    </div>
                </div>
                <div class="d-flex align-items-stretch" style="width:25%; display: inline-block">
                    <div class="card shadow p-3 mb-5 bg-white rounded">
                        <form class="" action="${pageContext.request.contextPath}/servlets/users/child/ChildLogin" method="GET">
                            <div class="card-body formParaText">
                                <h5 class="card-title formPara">For Children</h5>
                                <label for="childEmail" class="form-label formPara"><%="Email"%></label>
                                <input class="form-control formParaText" type="email" name="childEmail" id="childEmail" required/>
                                <br/>
                                <label for="childPword" class="form-label formPara"><%="Password"%></label>
                                <input class="form-control formParaText" type="password" name="childPword" id="childPword" minlength="8" required/>
                                <br/>
                            </div>
                            <div class="myformbtn" style="margin:auto; padding-bottom: 10%">
                                <input class="btn btn-primary classFormBtn formParaText" type="reset" value="Clear">
                                <input class="btn btn-primary classFormBtn formParaText" type="submit" value="Submit">
                            </div>
                        </form>
                    </div>
                </div>
                <div class="d-flex align-items-stretch" style="width:25%; display: inline-block">
                    <div class="card shadow p-3 mb-5 bg-white rounded">
                        <form class="" action="${pageContext.request.contextPath}/servlets/users/teacher/TeacherLogin" method="GET">
                            <div class="card-body formParaText">
                                <h5 class="card-title formPara">For Teachers</h5>
                                <label for="teacherEmail" class="form-label formPara"><%="Email"%></label>
                                <input class="form-control formParaText" type="email" name="teacherEmail" id="teacherEmail" required/>
                                <br/>
                                <label for="teacherPword" class="form-label formPara"><%="Password"%></label>
                                <input class="form-control formParaText" type="password" name="teacherPword" id="teacherPword" minlength="8" required/>
                                <br/>
                            </div>
                            <div class="myformbtn" style="margin:auto; padding-bottom: 10%">
                                <input class="btn btn-primary classFormBtn formParaText" type="reset" value="Clear">
                                <input class="btn btn-primary classFormBtn formParaText" type="submit" value="Submit">
                            </div>
                        </form>
                    </div>
                </div>
                <div style="width: 12.5%">
                </div>
            </div>
            <br/>
        </div>
        <div id="background"></div>

        <footer class="footer formPara">
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
