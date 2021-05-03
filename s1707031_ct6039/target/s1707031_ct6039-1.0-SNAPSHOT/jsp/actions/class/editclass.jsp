<%@ page import="java.util.Map" %>
<%@ page import="com.uog.miller.s1707031_ct6039.beans.ClassLinkBean" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 06/03/2021
  Time: 12:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Edit Class</title>
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
                <h1 class="myheader neat"><%="Edit Class"%></h1>
                <br/>
                <p class="formTextCenter myPara neat">
                    <%="From here, you can edit your Classes."%>
                </p>
                <br/>

                <% String classId = (String) session.getAttribute("classId");%>
                <% String className = (String) session.getAttribute("className");%>
                <% String classYear = (String) session.getAttribute("classYear");%>
                <% String classTutor = (String) session.getAttribute("classTutor");%>
                <% List<ClassLinkBean> linkBeans = (List<ClassLinkBean>) session.getAttribute("allLinkChildren");
                    StringBuilder linkBeansArray = new StringBuilder();
                    for (int i = 0; i < linkBeans.size(); i++)
                    {
                        if (i == 0)
                        {
                            linkBeansArray = new StringBuilder(linkBeans.get(i).getEmail().trim());
                        }
                        else
                        {
                            linkBeansArray.append(",").append(linkBeans.get(i).getEmail().trim());
                        }
                    }%>
                <div class="card shadow p-3 mb-5 bg-white rounded">
                    <form class="reg-form" action="${pageContext.request.contextPath}/servlets/childclass/UpdateClass" method="POST">
                        <div class="card-body">
                            <label for="child-value" hidden></label>
                            <input type="text" name="child-value" id="child-value" value="<%=linkBeansArray.toString()%>" hidden/>
                            <br/>
                            <h3 class="formPara formHeader">Class Details for Class #<%=classId%></h3>
                            <br/>
                            <label for="className" class="form-label formPara"><%="Name of Class"%></label>
                            <input type="text" name="className" id="className" class="form-control formParaText" value="<%=className%>" required/>
                            <br/>
                            <label for="email" class="form-label formPara"><%="Tutor"%></label>
                            <input type="email" name="email" id="email" class="form-control formParaText" value="<%=classTutor%>" disabled required/>
                            <br/>
                            <label for="tutor" class="form-label formPara"><%="Year"%></label>
                            <select class="form-control formParaText" class="select-css" name="tutor" id="tutor" required>
                                <%--Iterate each year, Use these for options --%>
                                <option class="formParaText" value="">None</option>
                                <% Map<String,String> allYears = (Map<String,String>) session.getAttribute("allYears");
                                    if(allYears != null) {
                                        for (Map.Entry<String, String> entry : allYears.entrySet())
                                        {%>
                                <option class="formParaText" <% if(entry.getKey().equals(classYear)){ %> selected <% } %> value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
                                <%}
                                }%>
                            </select>
                            <br/>
                            <label for="childSelect[]" class="form-label formPara"><%="Children"%></label>
                            <select class="form-control select-css selectpicker childSelect formParaText" name="childSelect[]" id="childSelect[]" multiple data-live-search="true">
                                <%--Get all children, allow multi select--%>
                                <option class="formParaText" value=""></option>
                                <% Map<String,String> allChildren = (Map<String,String>) session.getAttribute("allChildren");
                                    if(allChildren != null) {
                                        for (Map.Entry<String, String> entry : allChildren.entrySet())
                                        {%>
                                <option class="formParaText" value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
                                <%}
                                }%>
                            </select>
                            <br/>
                        </div>
                        <div class="body-main-content myformbtn">
                            <br>
                            <a class="btn btn-primary formParaText formBtn" href=${pageContext.request.contextPath}/servlets/Redirects?location=class-view>&nbsp;Back&nbsp;</a>
                            <a class="btn btn-primary formParaText formBtn" href=${pageContext.request.contextPath}/servlets/childclass/DeleteClass?classId=<%=classId%>>&nbsp;Delete Class&nbsp;</a>
                            <input class="btn btn-primary formParaText formBtn" type="submit" value="Update Changes">
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
                    let linkBeans = $("#child-value").val();
                    let linkBeansArr =linkBeans.split(",");
                    let selPicker = $('.selectpicker');
                    let selPicker2 = $(".childSelect");
                    selPicker2.val(linkBeansArr);
                    selPicker.selectpicker('refresh');
                });
        </script>
    </body>
</html>
