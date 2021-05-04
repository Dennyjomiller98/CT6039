<%@ page import="com.uog.miller.s1707031_ct6039.beans.HomeworkBean" %>
<%@ page import="com.uog.miller.s1707031_ct6039.beans.SubmissionBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
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
                <%  List<HomeworkBean> allHomework;
                    List<SubmissionBean> allSubmissions;
                    if (session.getAttribute("allHomeworks") != null)
                    {
                    	allHomework = (List<HomeworkBean>) session.getAttribute("allHomeworks");
                    }
                    else
                    {
                        allHomework = new ArrayList<>();
                    }
                    if (session.getAttribute("allSubmissions") != null)
                    {
                        allSubmissions = (List<SubmissionBean>) session.getAttribute("allSubmissions");
                    }
                    else
                    {
                        allSubmissions = new ArrayList<>();
                    }
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
                                <strong>Set Date:</strong> <%=homeworkTask.getSetDate()%> <br/>
                                <strong>Description:</strong> <%=homeworkTask.getDescription()%> <br/>
                                <strong>Submission Status:</strong> Not submitted. <br/>
                                <strong>Due Date:</strong> <%=homeworkTask.getDueDate()%> <br/>
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
                <p class="main-body-text myPara neat">
                    <%="You have no Unsubmitted homework."%>
                </p>
                <% } %>
                <br/>
                <%--List all Submitted/Completed Homework--%>
                <% if(allHomework != null && !allHomework.isEmpty()){%>
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
                                    <strong>Set Date:</strong> <%=homeworkTask.getSetDate()%> <br/>
                                    <strong>Description:</strong> <%=homeworkTask.getDescription()%> <br/>
                                    <strong>Submission Status:</strong> Submitted: #<%=matchingSubmission.getSubmissionId()%> <br/>
                                    <strong>Due Date:</strong> <%=homeworkTask.getDueDate()%> <br/>
                                    <strong>Submission Date:</strong> <%=matchingSubmission.getSubmissionDate()%> <br/>
                                    <%if(matchingSubmission.getGrade() != null){ %>
                                        <%if(matchingSubmission.getGrade().equals("green")){ %>
                                            <p class="gradeGreen gradeResult">
                                                <strong><%="Grade:"%></strong><strong><%="Superb"%></strong>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-emoji-sunglasses" viewBox="0 0 16 16">
                                                    <path d="M4.968 9.75a.5.5 0 1 0-.866.5A4.498 4.498 0 0 0 8 12.5a4.5 4.5 0 0 0 3.898-2.25.5.5 0 1 0-.866-.5A3.498 3.498 0 0 1 8 11.5a3.498 3.498 0 0 1-3.032-1.75zM7 5.116V5a1 1 0 0 0-1-1H3.28a1 1 0 0 0-.97 1.243l.311 1.242A2 2 0 0 0 4.561 8H5a2 2 0 0 0 1.994-1.839A2.99 2.99 0 0 1 8 6c.393 0 .74.064 1.006.161A2 2 0 0 0 11 8h.438a2 2 0 0 0 1.94-1.515l.311-1.242A1 1 0 0 0 12.72 4H10a1 1 0 0 0-1 1v.116A4.22 4.22 0 0 0 8 5c-.35 0-.69.04-1 .116z"></path>
                                                    <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-1 0A7 7 0 1 0 1 8a7 7 0 0 0 14 0z"></path>
                                                </svg>
                                            </p>
                                        <% } else if(matchingSubmission.getGrade().equals("amber")){ %>
                                            <p class="gradeAmber gradeResult">
                                                <strong><%="Grade:"%></strong><strong><%="Good"%></strong>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-emoji-neutral" viewBox="0 0 16 16">
                                                    <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"></path>
                                                    <path d="M4 10.5a.5.5 0 0 0 .5.5h7a.5.5 0 0 0 0-1h-7a.5.5 0 0 0-.5.5zm3-4C7 5.672 6.552 5 6 5s-1 .672-1 1.5S5.448 8 6 8s1-.672 1-1.5zm4 0c0-.828-.448-1.5-1-1.5s-1 .672-1 1.5S9.448 8 10 8s1-.672 1-1.5z"></path>
                                                </svg>
                                            </p>
                                        <% } else if(matchingSubmission.getGrade().equals("red")){ %>
                                            <p class="gradeRed gradeResult">
                                                <strong><%="Grade:"%></strong><strong><%="Keep Trying"%></strong>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-emoji-frown" viewBox="0 0 16 16">
                                                    <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"></path>
                                                    <path d="M4.285 12.433a.5.5 0 0 0 .683-.183A3.498 3.498 0 0 1 8 10.5c1.295 0 2.426.703 3.032 1.75a.5.5 0 0 0 .866-.5A4.498 4.498 0 0 0 8 9.5a4.5 4.5 0 0 0-3.898 2.25.5.5 0 0 0 .183.683zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm4 0c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5z"></path>
                                                </svg>
                                            </p>
                                        <% } %>
                                        <% if(matchingSubmission.getFeedback() != null){%>
                                            <strong>Feedback:</strong> <%=matchingSubmission.getFeedback()%> <br/>
                                        <%}%>
                                    <% } else{ %>
                                        <strong>Grade:</strong> Not Provided Yet <br/>
                                    <% } %>
                                </p>
                            </div>
                            <div class="myformbtn" style="padding-bottom: 5%">
                                <a class="btn btn-primary formParaText formBtn" href="${pageContext.request.contextPath}/servlets/homework/DownloadHomework?submissionId=<%=matchingSubmission.getSubmissionId()%>"><%="Download Submission"%></a>
                            </div>
                        </div>
                        <br/>
                        <%}
                    }%>
                </div>
               <%}else { %>
                <p class="main-body-text myPara neat">
                    <%="You have no Submissions."%>
                </p>
                <% } %>
                <% } else { %>
                <p class="main-body-text myPara neat">
                    <%="There is no Homework for you."%>
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
                    if (allHomework != null && allHomework.size() > 0)
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
                               href=${pageContext.request.contextPath}/servlets/homework/RetrieveHomework?homeworkId=<%=homeworkTask.getEventId()%>>&nbsp;View Submissions&nbsp;
                            </a>
                        </div>
                    </div>
                    <br/>
                    <%}
                    }else{ %>
                    <p class="main-body-text myPara neat">
                        <%="There is no Assigned Homework for you."%>
                    </p>
                    <% }%>
                <br/>
                <% } else { %>
                <p class="main-body-text myPara neat">
                    <%="No Homework Assignments could be found."%>
                </p>
                <br/>
                <% } %>
            </div>
            <% } %>
            <%List<SubmissionBean> allRetrievedSubmissions = (List<SubmissionBean>) session.getAttribute("retrievedSubmissions"); %>
            <%String homeworkId = (String) session.getAttribute("homeworkId"); %>
            <%if(allRetrievedSubmissions != null && allRetrievedSubmissions.size() > 0) { %>
            <%--Script to load modal (or won't popup)--%>
            <script>
                $(window).on('load', function(){
                    $('#exampleModalCenter').removeClass("hide").modal('show');
                    $("#exampleModalCenter").appendTo("body");
                });
            </script>
            <div class="modal fade calendarPopup" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
                    <div class="modal-content shadow p-3 mb-5 bg-white rounded">
                        <div class="modal-header">
                            <h5 class="modal-title formPara" id="exampleModalLongTitle">Homework #<%=homeworkId%></h5>
                            <button type="button" class="close close-btn" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <%--Content for modal popup containing homework submission information (in table)--%>
                            <table class="table table-sm table-hover thead-light">
                                <thead>
                                    <tr>
                                        <th scope="col">Child Email</th>
                                        <th scope="col">Submission Date</th>
                                        <th scope="col">Submission File</th>
                                        <th scope="col">Grade</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (SubmissionBean submission : allRetrievedSubmissions) { %>
                                        <tr>
                                            <td><%=submission.getEmail()%></td>
                                            <%--Submission date/file link--%>
                                            <%if(submission.getSubmissionDate() != null){ %>
                                                <td><%=submission.getSubmissionDate()%></td>
                                                <td><a href="${pageContext.request.contextPath}/servlets/homework/DownloadHomework?submissionId=<%=submission.getSubmissionId()%>"><%="Download"%></a></td>
                                                <%--Grade--%>
                                                <%if(submission.getGrade() != null){ %>
                                                <%if(submission.getGrade().equals("green")){ %>
                                                    <td class="gradeGreen">
                                                        <%="Superb"%>
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-emoji-sunglasses" viewBox="0 0 16 16">
                                                            <path d="M4.968 9.75a.5.5 0 1 0-.866.5A4.498 4.498 0 0 0 8 12.5a4.5 4.5 0 0 0 3.898-2.25.5.5 0 1 0-.866-.5A3.498 3.498 0 0 1 8 11.5a3.498 3.498 0 0 1-3.032-1.75zM7 5.116V5a1 1 0 0 0-1-1H3.28a1 1 0 0 0-.97 1.243l.311 1.242A2 2 0 0 0 4.561 8H5a2 2 0 0 0 1.994-1.839A2.99 2.99 0 0 1 8 6c.393 0 .74.064 1.006.161A2 2 0 0 0 11 8h.438a2 2 0 0 0 1.94-1.515l.311-1.242A1 1 0 0 0 12.72 4H10a1 1 0 0 0-1 1v.116A4.22 4.22 0 0 0 8 5c-.35 0-.69.04-1 .116z"></path>
                                                            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-1 0A7 7 0 1 0 1 8a7 7 0 0 0 14 0z"></path>
                                                        </svg>
                                                    </td>
                                                <% } else if(submission.getGrade().equals("amber")){ %>
                                                    <td class="gradeAmber">
                                                        <%="Good"%>
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-emoji-neutral" viewBox="0 0 16 16">
                                                            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"></path>
                                                            <path d="M4 10.5a.5.5 0 0 0 .5.5h7a.5.5 0 0 0 0-1h-7a.5.5 0 0 0-.5.5zm3-4C7 5.672 6.552 5 6 5s-1 .672-1 1.5S5.448 8 6 8s1-.672 1-1.5zm4 0c0-.828-.448-1.5-1-1.5s-1 .672-1 1.5S9.448 8 10 8s1-.672 1-1.5z"></path>
                                                        </svg>
                                                    </td>
                                                <% } else if(submission.getGrade().equals("red")){ %>
                                                    <td class="gradeRed">
                                                        <%="Keep Trying"%>
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-emoji-frown" viewBox="0 0 16 16">
                                                            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"></path>
                                                            <path d="M4.285 12.433a.5.5 0 0 0 .683-.183A3.498 3.498 0 0 1 8 10.5c1.295 0 2.426.703 3.032 1.75a.5.5 0 0 0 .866-.5A4.498 4.498 0 0 0 8 9.5a4.5 4.5 0 0 0-3.898 2.25.5.5 0 0 0 .183.683zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm4 0c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5z"></path>
                                                        </svg>
                                                    </td>
                                                <% } %>

                                                <% }else{ %>
                                                <td><a href=${pageContext.request.contextPath}/servlets/Redirects?location=homework-grade&homeworkId=<%=submission.getEventId()%>&child=<%=submission.getEmail()%>><%="Mark"%></a></td>
                                                <% } %>
                                            <% }else{ %>
                                                <td><%="Not Submitted"%></td>
                                                <td><%="None"%></td>
                                                <td></td>
                                            <% } %>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                            <br/>
                        </div>
                        <div class="modal-footer">
                            <a type="button" class="btn close-btn btn-secondary formParaText" data-dismiss="modal" aria-label="Close">
                                Close
                            </a>
                            <script>
                                $(".close-btn").on('click', function (){
                                    $("#exampleModalCenter").modal('hide');
                                });
                            </script>
                        </div>
                    </div>
                </div>
            </div>
            <%}%>
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
