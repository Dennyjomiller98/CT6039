<%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 19/02/2021
  Time: 12:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>Register Child</title>
    </head>
    <body>
        <jsp:include page="../../required.jsp"/>

        <div class="navbar">

        </div>

        <h1><%="Register Child Account" %></h1>
        <br/>

        <p>
            <%= "From here, you can register a child account and assign them to a year."%>
            <%="Once the account is created and assigned to a year, the child can be added to a Class. If a child is added to a class, they can join class lessons, access class materials and submit homework."%>
        </p>

        <form action="${pageContext.request.contextPath}/servlets/users/child/ChildRegistration" method="POST">
            <label for="firstname"><%="Firstname:"%></label>
            <input type="text" name="firstname" id="firstname" required/>
            <br/>
            <label for="surname"><%="Surname:"%></label>
            <input type="text" name="surname" id="surname" required/>
            <br/>
            <label for="email"><%="Email:"%></label>
            <input type="email" name="email" id="email" required/>
            <br/>
            <label for="dob"><%="Date of Birth:"%></label>
            <input type="date" name="dob" id="dob" required/>
            <br/>
            <label for="address"><%="Address (Start Typing to auto-fill):"%></label>
            <input type="search" id="address" class="form-control" placeholder="Begin Entering your address..." />
            <strong id="address-value" hidden></strong>
            <br/>
            <label for="year"><%="Year:"%></label>
            <select class="select-css" style="width: 50%; display:inline-block" name="year" id="year" required>
                <%--Iterate each year, in DB reception has ID 0, and name "reception". Use these for options --%>
            </select>
            <br/>
            <label for="pword"><%="Password:"%></label>
            <input type="password" name="pword" id="pword" minlength="8" required/>
            <br/>
            <label for="pwordConfirm"><%="Confirm Password:"%></label>
            <input type="password" name="pwordConfirm" id="pwordConfirm" minlength="8" required/>
            <br/>
            <input type="reset" value="Clear">
            <input type="submit" value="Submit">
        </form>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=home>&nbsp;Return Home&nbsp;</a>
        <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a>

        <div class="footer">

        </div>

        <script>
            (function() {
                var placesAutocomplete = places({
                    container: document.querySelector('#address')
                });

                var $address = document.querySelector('#address-value')
                placesAutocomplete.on('change', function(e) {
                    $address.textContent = e.suggestion.value
                });

                placesAutocomplete.on('clear', function() {
                    $address.textContent = 'none';
                });

            })();
        </script>
    </body>
</html>
