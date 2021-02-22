<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--Landing Index page for all users--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Homepage</title>
</head>
    <body>
        <jsp:include page="jsp/required.jsp"/>
        <link rel="stylesheet" href="css/main.css">

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
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=calender">Calender</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=calender">Homework, File Resources, Progress links</a>
                        </li>
                    </ul>
                    <%--Login/Register Side of navbar. if logged in, show logout/account links--%>
                    <span class="navbar-text">
                        <button class="btn btn-sm btn-outline-secondary" type="button">
                            <a href=${pageContext.request.contextPath}/servlets/Redirects?location=login>&nbsp;Login&nbsp;</a>
                        </button>
                        &nbsp;
                        <button class="btn btn-sm btn-outline-secondary" type="button">
                            <a href=${pageContext.request.contextPath}/servlets/Redirects?location=register>&nbsp;Register&nbsp;</a>
                        </button>
                    </span>
                </div>
            </div>
        </nav>

        <%--Title--%>
        <div class="main-body-content">
            <h1><%="Welcome! Index Homepage"%></h1>
            <br/>
        </div>

        <div class="row card-index">
            <div class="" style="width:30%">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">For Parents/Guardians</h5>
                        <p class="card-text-center">Using this Portal allows you to check your child's progress, including homework submission dates.</p>
                        <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-login>&nbsp;Parent Login&nbsp;</a>
                        <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-register>&nbsp;Register Parent&nbsp;</a>
                    </div>
                </div>
            </div>
            <div class="" style="width:30%">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">For Children</h5>
                        <p class="card-text-center">If you are a child accessing this site, please use the following links:</p>
                        <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a>
                        <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=child-register>&nbsp;Child Register&nbsp;</a>
                    </div>
                </div>
            </div>
            <div class="" style="width:30%">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">For Teachers</h5>
                        <p class="card-text-center">Teachers accessing this site can update Homework and Lesson Files from their account.</p>
                        <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-login>&nbsp;Teacher Login&nbsp;</a>
                        <a class="btn btn-primary" href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-register>&nbsp;Teacher Register&nbsp;</a>
                    </div>
                </div>
            </div>
        </div>

        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=home>&nbsp;Return Home&nbsp;</a> <br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-register>&nbsp;Child Reg&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-profile>&nbsp;Child Profile&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-login>&nbsp;ParentLogin&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-register>&nbsp;Parent Reg&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-profile>&nbsp;Parent Profile&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-login>&nbsp;Teacher Login&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-register>&nbsp;Teacher Reg&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-profile>&nbsp;Teacher Profile&nbsp;</a><br/>
        <br>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=calender>&nbsp;Calender&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=lessons>&nbsp;Lessons Join&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=progress-request>&nbsp;Request Progress (By parent?)&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=progress-submit>&nbsp;Submit Progress (By Teacher)&nbsp;</a><br/>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=progress-view>&nbsp;View Progress (Put on profile page of child, accessible by THAT Child/Teacher/LINKED Parent)&nbsp;</a><br/>
        <br>

        <div class="footer">

        </div>
    </body>
</html>