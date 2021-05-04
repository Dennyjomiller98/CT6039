<%@ page import="com.uog.miller.s1707031_ct6039.beans.SubmissionBean" %><%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 20/03/2021
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>Add Homework</title>
        <link rel="icon" type="image/gif" href="${pageContext.request.contextPath}/imgs/favicon.ico">
    </head>
    <body>
        <jsp:include page="../../required.jsp"/>
        <link rel="stylesheet" href="../../../css/main.css">

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
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=home">Home</a>
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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <% } else if(isTeacher != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/servlets/Redirects?location=class-view">My Classes</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
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
                <h1 class="myheader neat"><%="Grade Child Homework Submission"%></h1>
                <br/>
                <p class="formTextCenter myPara neat">
                    <%="From here, you can mark the Homework Submission selected, using the Traffic light System."%>
                </p>
                <br/>
                <div class="card shadow p-3 mb-5 bg-white rounded">
                    <%SubmissionBean submissionBean = (SubmissionBean) session.getAttribute("homeworkForGrading"); %>
                    <%String childEmail = (String) session.getAttribute("childForGrading"); %>
                    <form class="reg-form" action="${pageContext.request.contextPath}/servlets/homework/GradeHomework" method="POST">
                        <div class="card-body">
                            <label for="childEmail" hidden></label>
                            <input type="text" name="childEmail" id="childEmail" value="<%=childEmail%>" hidden/>
                            <label for="submissionId" hidden></label>
                            <input type="text" name="submissionId" id="submissionId" value="<%=submissionBean.getSubmissionId()%>" hidden/>
                            <label for="homeworkId" hidden></label>
                            <input type="text" name="homeworkId" id="homeworkId" value="<%=submissionBean.getEventId()%>" hidden/>
                            <br/>
                            <label for="email" class="formPara"><%="Child Email"%></label><br/>
                            <input class="formParaText form-control" type="text" name="email" id="email" value="<%=childEmail%>" disabled/>
                            <br>
                            <label for="submittedDate" class="formPara"><%="Date Submitted"%></label><br/>
                            <input class="formParaText form-control" type="text" name="submittedDate" id="submittedDate" value="<%=submissionBean.getSubmissionDate()%>" disabled/>
                            <br>
                            <label for="grade" class="form-label formPara"><%="Homework Grade"%></label>
                            <select class="form-control formParaText selectpicker" name="grade" id="grade" data-live-search="true">
                                <option class="formParaText" value="" ><%="Select Grade..."%></option>
                                <option class="formParaText gradeGreen" value="green">Green</option>
                                <option class="formParaText gradeAmber" value="amber">Amber</option>
                                <option class="formParaText gradeRed" value="red">Red</option>
                            </select>
                            <br/>
                            <label for="feedback" class="formPara"><%="Feedback"%></label><br/>
                            <input class="formParaText form-control" type="text" name="feedback" id="feedback" maxlength="255"/>
                        </div>
                        <div class="body-main-content myformbtn">
                            <br/>
                            <a class="btn btn-primary classFormBtn formParaText" href=${pageContext.request.contextPath}/servlets/Redirects?location=homework-view>&nbsp;Back&nbsp;</a>
                            <input class="btn btn-primary classFormBtn formParaText" type="submit" value="Submit Grade">
                        </div>
                        <br/>
                    </form>
                </div>
            </div>
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
        <script>
            //Title "Other" input
            let gradeSelect = $("#grade");
            gradeSelect.on('change', function(){
                let val = gradeSelect.val();
                console.log(val);
                if(val === "green")
                {
                    let selectElement = $(".btn.dropdown-toggle.btn-light");
                    selectElement.removeClass("gradeAmber");
                    selectElement.removeClass("gradeRed");
                    selectElement.addClass("gradeGreen");
                }
                else if(val === "amber")
                {
                    let selectElement = $(".btn.dropdown-toggle.btn-light");
                    selectElement.removeClass("gradeRed");
                    selectElement.removeClass("gradeGreen");
                    selectElement.addClass("gradeAmber");
                }
                else if(val === "red")
                {
                    let selectElement = $(".btn.dropdown-toggle.btn-light");
                    selectElement.removeClass("gradeAmber");
                    selectElement.removeClass("gradeGreen");
                    selectElement.addClass("gradeRed");
                }
                else
                {
                    let selectElement = $(".btn.dropdown-toggle.btn-light");
                    selectElement.removeClass("gradeAmber");
                    selectElement.removeClass("gradeGreen");
                    selectElement.removeClass("gradeRed");
                }
            });
        </script>
    </body>
</html>
