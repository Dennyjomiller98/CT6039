<%@ page import="com.uog.miller.s1707031_ct6039.beans.HomeworkBean" %>
<%@ page import="com.uog.miller.s1707031_ct6039.beans.SubmissionBean" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 19/02/2021
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>Homework</title>
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <% } else if(isTeacher != null) {%>
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/servlets/Redirects?location=class-view">My Classes</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <% } else if(isParent != null) {%>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=progress-view">Progress</a>
                            </li>
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
                <h1 class="myheader neat"><%="View Homework"%></h1>
                <br/>
            </div>

            <%if(isChild != null)
            { %>
            <%--Children Homework Cards--%>
            <p class="main-body-text myPara neat">
                <%="From here, you can view Homework, both required and submitted."%>
            </p>
            <br/>

            <div class="main-body-content myPara neat">
                <%List<HomeworkBean> allHomework = (List<HomeworkBean>) session.getAttribute("allHomeworks");
                    List<SubmissionBean> allSubmissions = (List<SubmissionBean>) session.getAttribute("allSubmissions");
                    if (allHomework.size() > 0 && allSubmissions.size() > 0)
                    {%>

                <%--List all Unsubmitted/Due Homework--%>
                <% if(!allHomework.isEmpty()){%>
                <div class="shadow p-3 mb-5 bg-white rounded unsubmittedHomework">
                    <h3 class="formPara formHeader">Unsubmitted Homework</h3>
                    <% for (HomeworkBean homeworkTask : allHomework) {
                        SubmissionBean matchingSubmission = null;
                        for (SubmissionBean submission : allSubmissions) {
                            if(homeworkTask.getEventId().equals(submission.getEventId()))
                            {
                                matchingSubmission = submission;
                            }
                        }
                        if(matchingSubmission != null && matchingSubmission.getSubmissionId() == null){%>
                    <div class="card shadow p-3 mb-5 bg-white rounded">
                        <div class="card-body">
                            <h5 class="card-title formPara"><%="Homework #" + homeworkTask.getEventId() + ": " + homeworkTask.getName()%></h5>
                            <p class="card-text-center formParaText">
                                Set Date: <%=homeworkTask.getSetDate()%> <br/>
                                Description: <%=homeworkTask.getDescription()%> <br/>
                                Submission Status: Not submitted. <br/>
                                Due Date: <%=homeworkTask.getDueDate()%> <br/>
                            </p>
                        </div>
                        <div class="myformbtn" style="padding-bottom: 5%">
                            <a class="btn btn-primary formParaText formBtn" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-upload&homeworkId=<%=homeworkTask.getEventId()%>">Submit Homework</a>
                        </div>
                    </div>
                    <br/>
                    <%}
                    } %>
                </div>
                <%}else { %>
                <p class="formParaText">
                    <%="You have no Unsubmitted homework."%>
                </p>
                <% } %>
                <br/>
                <%--List all Submitted/Completed Homework--%>
                <% if(!allHomework.isEmpty()){%>
                <div class="shadow p-3 mb-5 bg-white rounded submittedHomework">
                    <h3 class="formPara formHeader">Submitted Homework</h3>
                    <%for (HomeworkBean homeworkTask : allHomework) {
                            SubmissionBean matchingSubmission = null;
                            for (SubmissionBean submission : allSubmissions) {
                                if(homeworkTask.getEventId().equals(submission.getEventId()))
                                {
                                    matchingSubmission = submission;
                                }
                            }
                            if(matchingSubmission != null && matchingSubmission.getSubmissionId() != null){%>
                               <div class="card shadow p-3 mb-5 bg-white rounded">
                            <div class="card-body">
                                <h5 class="card-title formPara"><%="Homework #" + homeworkTask.getEventId() + ": " + homeworkTask.getName()%></h5>
                                <p class="card-text-center formParaText">
                                    Set Date: <%=homeworkTask.getSetDate()%> <br/>
                                    Description: <%=homeworkTask.getDescription()%> <br/>
                                    Submission Status: Submitted: #<%=matchingSubmission.getSubmissionId()%> <br/>
                                    Due Date: <%=homeworkTask.getDueDate()%> <br/>
                                    Submission Date: <%=matchingSubmission.getSubmissionDate()%> <br/>
                                    <%if(matchingSubmission.getGrade() != null){ %>
                                    Grade: <%=matchingSubmission.getGrade()%> <br/>
                                    <% } else{ %>
                                    Grade: Not Provided Yet <br/>
                                    <% } %>
                                </p>
                            </div>
                            <div class="myformbtn" style="padding-bottom: 5%">
                                <a class="btn btn-primary formParaText formBtn"
                                   href=${pageContext.request.contextPath}/servlets/homework/DownloadHomework?homeworkId=<%=homeworkTask.getEventId()%>>&nbsp;Download Submission&nbsp;
                                </a>
                            </div>
                        </div>
                        <br/>
                        <%}
                    }%>
                </div>
               <%}else { %>
                <p class="formParaText">
                    <%="You have no Submissions."%>
                </p>
                <% } %>
                <% } else { %>
                <p class="formParaText">
                    <%="There are no Homeworks for you."%>
                </p>
                <br/>
                <% } %>
            </div>

            <% }
            else if(isTeacher != null)
            { %>
            <%--Teacher Homework Cards--%>
            <p class="main-body-text myPara neat">
                <%="From here, you can view assigned Homework and Childrens submissions."%>
            </p>
            <br/>

            <div class="main-body-content">
                <%List<HomeworkBean> allHomework = (List<HomeworkBean>) session.getAttribute("allHomeworksTeacher");
                    if (allHomework.size() > 0)
                    {%>
                    <br/>
                    <% if(!allHomework.isEmpty()){
                        for (HomeworkBean homeworkTask : allHomework){%>
                    <div class="card shadow p-3 mb-5 bg-white rounded">
                        <div class="card-body">
                            <h5 class="card-title formPara"><%="Homework #" + homeworkTask.getEventId() + ": " + homeworkTask.getName()%></h5>
                            <p class="card-text-center formParaText">
                                Set Date: <%=homeworkTask.getSetDate()%> <br/>
                                Due Date: <%=homeworkTask.getDueDate()%> <br/>
                                <%if(homeworkTask.getClassId().equals("0")){%>
                                Class Assigned: Reception <br/>
                                <%}else{%>
                                Class Assigned: <%="Year " + homeworkTask.getClassId()%> <br/>
                                <%}%>
                                Description: <%=homeworkTask.getDescription()%> <br/>
                            </p>
                        </div>
                        <div class="myformbtn" style="padding-bottom: 5%">
                            <a class="btn btn-secondary formParaText formBtn"
                               href=${pageContext.request.contextPath}/servlets/homework/DeleteHomework?homeworkId=<%=homeworkTask.getEventId()%>>&nbsp;Delete&nbsp;
                            </a>
                            <a class="btn btn-primary formParaText formBtn"
                               href=${pageContext.request.contextPath}/servlets/homework/ViewSubmissions?homeworkId=<%=homeworkTask.getEventId()%>>&nbsp;View Submissions&nbsp;
                            </a>
                        </div>
                    </div>
                    <br/>
                    <%}
                    }else{ %>
                    <p class="formParaText">
                        <%="There are no Assigned Homeworks for you."%>
                    </p>
                    <% }%>
                <br/>
                <% } else { %>
                <p class="formParaText">
                    <%="No Homework Assignments could be found."%>
                </p>
                <br/>
                <% } %>
            </div>
            <% } %>
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
            //Child Selection
            $(document).ready(
                function () {
                    //Todays date (set date) for Homework Assign
                    let curday = function(){
                        let today = new Date();
                        let dd = today.getDate();
                        let mm = today.getMonth()+1;
                        let yyyy = today.getFullYear();

                        if(dd<10) dd='0'+dd;
                        if(mm<10) mm='0'+mm;
                        return (yyyy+"-"+mm+"-"+dd);
                    };
                    $('#setDate').val(curday());
                    //Full Todays Date (for creating Calendar events for children in class)
                    $('#fullDate').val(new Date());
                });
        </script>
    </body>
</html>
