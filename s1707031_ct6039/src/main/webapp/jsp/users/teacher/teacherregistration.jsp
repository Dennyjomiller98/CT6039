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
        <title>Teacher Account Registration</title>
    </head>

    <body>
        <jsp:include page="../../required.jsp"/>
        <link rel="stylesheet" href="../../../css/main.css">

        <div class="navbar">

        </div>

        <h1><%="Register Teacher Account" %></h1>
        <br/>

        <p>
            <%= "From here, you can register an account for Teachers."%>
        </p>

        <form action="${pageContext.request.contextPath}/servlets/users/teacher/TeacherRegistration" method="POST">
            <% String errors = (String) session.getAttribute("formErrors");
                if(errors != null) { %>
            <div class="alert alert-danger" role="alert" id="formErrors"><%=errors%></div>
            <%}%>
            <br/>
            <label for="title"><%="Title:"%></label>
            <select class="selectpicker" name="title" id="title" data-live-search="true">
                <%--Single select, use Titles. (Mr/Mrs/Other (specified field)--%>
                <option value="Mr">Mr</option>
                <option value="Mrs">Mrs</option>
                <option value="Other">Other (Please Specify)</option>
            </select>
            <br/>
            <%--If Other selected, use textbox value. When select is changed, set value in this textbox and use this value when submitting--%>
            <label for="title-value"><%="Title (Other):"%></label>
            <%--HIDE THIS WHEN "Other" NOT SELECTED!--%>
            <input type="text" name="title-value" id="title-value" <%--If "Other", required--%>required/>
            <br/>
            <br/>
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
            <label for="tutor"><%="Tutor for Class:"%></label>
            <select class="select-css" style="width: 50%; display:inline-block" name="tutor" id="tutor" required>
                <%--Iterate each year, in DB reception has ID 0, and name "reception". Use these for options --%>
            </select>
            <br/>
            <label for="pword"><%="Password:"%></label>
            <input type="password" name="pword" id="pword" minlength="8" required/>
            <br/>
            <label for="pwordConfirm"><%="Confirm Password:"%></label>
            <input type="password" name="pwordConfirm" id="pwordConfirm" minlength="8" required/>
            <br/>
            <div class="alert alert-warning" role="alert" id="pwordErrors" style="display: none">Passwords do not match!</div>
            <input class="btn btn-primary" type="reset" value="Clear">
            <input class="btn btn-primary" type="submit" value="Submit">
        </form>

        <div class="footer">

        </div>

        <%--Address Finder inline script--%>
        <script>
            //Address Search
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

            //Password validation
            let pword = $("#pword");
            let pwordConfirm = $("#pwordConfirm");
            function verifyPword() {
                let pwordErrors = $("#pwordErrors");
                if (pword.val() !== pwordConfirm.val()) {
                    pwordErrors.show();
                } else {
                    pwordErrors.hide();
                }
            }

            pword.keyup(function() {
                verifyPword();
            });
            pwordConfirm.keyup(function() {
                verifyPword();
            });
        </script>
    </body>
</html>
