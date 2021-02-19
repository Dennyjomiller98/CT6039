<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--Landing Index page for all users--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Homepage</title>
</head>
    <body>
    <div class="navbar">

    </div>

    <h1><%= "Welcome!" %></h1>
    <br/>

    <a href=${pageContext.request.contextPath}/servlets/Redirects?location=home>&nbsp;Return Home&nbsp;</a>
    <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a>

    <div class="footer">

    </div>
    </body>
</html>