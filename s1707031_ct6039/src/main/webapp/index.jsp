<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--Landing Index page for all users--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Homepage</title>
</head>
    <body>
    <jsp:include page="jsp/required.jsp"/>

    <div class="navbar">

    </div>

    <h1><%="Welcome! Index Homepage"%></h1>
    <br/>

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