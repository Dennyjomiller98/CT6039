<%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 19/02/2021
  Time: 12:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Child Login</title>
    </head>

    <body>
        <jsp:include page="../../required.jsp"/>

        <div class="navbar">

        </div>

        <h1><%="Child Account Login Portal" %></h1>
        <br/>

        <p>
            <%="From this portal, you can log in with your Child account credentials."%>
        </p>
        <form action="${pageContext.request.contextPath}/servlets/users/child/ChildLogin" method="GET">
            <label for="email"><%="Email:"%></label>
            <input type="email" name="email" id="email" required/>
            <br/>
            <label for="pword"><%="Password:"%></label>
            <input type="password" name="pword" id="pword" minlength="8" required/>
            <br/>
            <input class="btn btn-primary" type="reset" value="Clear">
            <input class="btn btn-primary" type="submit" value="Submit">
        </form>

        <div class="footer">

        </div>
    </body>
</html>
