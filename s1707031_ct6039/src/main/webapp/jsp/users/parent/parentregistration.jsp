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
        <title>Parent Account Registration</title>
    </head>
    <body>
    <jsp:include page="../../required.jsp"/>
        <div class="navbar">

        </div>

        <h1><%="Register Parent Account" %></h1>
        <br/>

        <p>
            <%= "From here, you can register an account for parents and guardians."%>
            <%="By linking created children accounts the parent can then view their progress with the ability to check homework submissions and deadlines."%>
        </p>

        <form action="${pageContext.request.contextPath}/servlets/users/parent/ParentRegistration" method="POST">
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
            <label for="childSelect"></label>
            <select class="selectpicker" name="childSelect" id="childSelect" multiple data-live-search="true">
                <%--Get all children, allow multi select--%>
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
                let placesAutocomplete = places({
                    container: document.querySelector('#address')
                });

                let $address = document.querySelector('#address-value')
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
