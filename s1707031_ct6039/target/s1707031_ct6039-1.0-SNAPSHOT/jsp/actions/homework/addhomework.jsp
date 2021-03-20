<%@ page import="com.uog.miller.s1707031_ct6039.beans.ClassBean" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 09/03/2021
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>Add Homework</title>
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
                <h1 class="myheader neat"><%="Assign Homework To Class"%></h1>
                <br/>
                <p class="formTextCenter myPara neat">
                    <%="From here, you can assign Homework by selecting a Class to assign. All Children in the Class will be notified and given a new Calendar event for the Homework deadline."%>
                </p>
                <br/>
                <div class="card shadow p-3 mb-5 bg-white rounded">
                    <%--Assign homework--%>
                    <% List<ClassBean> linkBeans = (List<ClassBean>) session.getAttribute("allClasses");
                        String selectedClass = (String) session.getAttribute("selectedClass");
                        StringBuilder linkBeansArray = new StringBuilder();
                        for (int i = 0; i < linkBeans.size(); i++)
                        {
                            if (i == 0)
                            {
                                linkBeansArray = new StringBuilder(linkBeans.get(i).getEventId().trim());
                            }
                            else
                            {
                                linkBeansArray.append(",").append(linkBeans.get(i).getEventId().trim());
                            }
                        }%>
                    <form class="reg-form" action="${pageContext.request.contextPath}/servlets/homework/AddHomework" method="POST">
                        <div class="card-body">
                            <label for="class-value" hidden></label>
                            <input type="text" name="class-value" id="class-value" value="<%=linkBeansArray.toString()%>" hidden/>
                            <label for="setDate" hidden></label>
                            <input type="text" name="setDate" id="setDate" hidden/>
                            <label for="fullDate" hidden></label>
                            <input type="text" name="fullDate" id="fullDate" hidden/>
                            <br/>
                            <label for="name" class="form-label formPara"><%="Homework Title"%></label>
                            <input type="text" name="name" id="name" class="form-control formParaText" placeholder="E.g. 'Science Poster Homework'" required/>
                            <br/>
                            <label for="description" class="form-label formPara"><%="Homework Description"%></label>
                            <textarea type="text" name="description" id="description" class="form-control formParaText" aria-label="With textarea" placeholder="Further describe the Homework task here..." required></textarea>
                            <br/>
                            <label for="dueDate" class="form-label formPara"><%="Due Date"%></label>
                            <input type="date" name="dueDate" id="dueDate" class="form-control formParaText" required/>
                            <br/>
                            <label for="classSelect" class="form-label formPara"><%="Class to Assign Homework for"%></label>
                            <select class="form-control select-css selectpicker classSelect formParaText" name="classSelect" id="classSelect" data-live-search="true">
                                <%--Get all children, allow multi select--%>
                                <option class="formParaText" value=""></option>
                                <% List<ClassBean> allClasses = (List<ClassBean>) session.getAttribute("allClasses");
                                    if(allClasses != null) {
                                        for (ClassBean allClass : allClasses)
                                        {%>
                                <option class="formParaText" <% if(allClass.getEventId().equals(selectedClass)){ %> selected <% } %> value="<%=allClass.getEventId()%>"><%=allClass.getName()%></option>
                                <%}
                                }%>
                            </select>
                            <br/>
                        </div>
                        <div class="body-main-content myformbtn">
                            <br/>
                            <a class="btn btn-primary classFormBtn formParaText" href=${pageContext.request.contextPath}/servlets/Redirects?location=class-view>&nbsp;Back to Classes&nbsp;</a>
                            <input class="btn btn-primary classFormBtn formParaText" type="submit" value="Assign Homework">
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
            //Child Selection
            $(document).ready(
                function () {
                    let linkBeans = $("#class-value").val();
                    let linkBeansArr =linkBeans.split(",");
                    let selPicker = $('.selectpicker');
                    let selPicker2 = $(".childSelect");
                    selPicker2.val(linkBeansArr);
                    selPicker.selectpicker('refresh');

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
