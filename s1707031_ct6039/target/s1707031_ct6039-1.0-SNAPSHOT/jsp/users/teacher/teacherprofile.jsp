<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 19/02/2021
  Time: 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>My Account - Teacher</title>
    </head>
    <body>
        <jsp:include page="../../required.jsp"/>
        <link rel="stylesheet" href="../../../css/main.css">

        <div class="content">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="container-fluid">
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/servlets/Redirects?location=home"><%="School Site"%></a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarText">
                        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/servlets/Redirects?location=home">Home</a>
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=class-view">My Classes</a>
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
            <div class="alert alert-danger" role="alert" id="formErrors"><%=errors%></div>
            <%}%>
            <% String success = (String) session.getAttribute("formSuccess");
                if(success != null) { %>
            <div class="alert alert-success" role="alert" id="formSuccess"><%=success%></div>
            <%}%>

            <%--Title--%>
            <div class="main-body-content">
                <h1><%="Teacher Account Profile"%></h1>
                <br/>
            </div>

            <p class="main-body-text">
                <%="From here, you can edit your account and settings."%>
                <br/>
                <%="You must enter your old Password to save changes."%>
            </p>

            <%--Edit account page, new password/confirm old pword needed, plus Email settings.--%>
            <% String firstname = (String) session.getAttribute("firstname");%>
            <% String surname = (String) session.getAttribute("surname");%>
            <% String dob = (String) session.getAttribute("dob");%>
            <% String address = (String) session.getAttribute("address");%>
            <% String year = (String) session.getAttribute("year");%>
            <% String title = (String) session.getAttribute("title");%>
            <% Boolean homeworkEmail = (Boolean) session.getAttribute("homeworkEmail");%>
            <% Boolean calendarEmail = (Boolean) session.getAttribute("calendarEmail");%>
            <% Boolean profileEmail = (Boolean) session.getAttribute("profileEmail");%>

            <form class="reg-form" action="${pageContext.request.contextPath}/servlets/users/teacher/TeacherProfile" method="POST">
                <br/>
                <h3>Profile Settings</h3>
                <label for="title" class="form-label"><%="Title"%></label>
                <select  class="form-control" class="selectpicker" name="title" id="title" data-live-search="true">
                    <%--Single select, use Titles. (Mr/Mrs/Other (specified field)--%>
                    <option value=""></option>
                    <option <% if(title.equals("Mr")){ %> selected <% } %> value="Mr">Mr</option>
                    <option <% if(title.equals("Mrs")){ %> selected <% } %> value="Mrs">Mrs</option>
                    <option <% if(!title.equals("Mr") && !title.equals("Mrs") && !title.equals("")){ %> selected <% } %> value="Other">Other (Please Specify)</option>
                </select>
                <br/>
                <%--If Other selected, use textbox value. When select is changed, set value in this textbox and use this value when submitting--%>
                <label for="title-value" class="form-label titleval" hidden><%="Title (Other)"%></label>
                <input class="form-control titleval" type="text" name="title-value" id="title-value" <% if(!title.equals("Mr") && !title.equals("Mrs") && !title.equals("")){ %> value="<%=title%>" <% } else { %> hidden <% } %>/>
                <br/>
                <label for="firstname" class="form-label"><%="Firstname"%></label>
                <input type="text" name="firstname" id="firstname" class="form-control" value="<%=firstname%>" required/>
                <br/>
                <label for="surname" class="form-label"><%="Surname"%></label>
                <input type="text" name="surname" id="surname" class="form-control" value="<%=surname%>" required/>
                <br/>
                <label for="email" class="form-label"><%="Email (This cannot be changed)"%></label>
                <input type="email" name="email" id="email" class="form-control" value="<%=email%>" disabled required/>
                <br/>
                <label for="dob" class="form-label"><%="Date of Birth"%></label>
                <input type="date" name="dob" id="dob" class="form-control" value="<%=dob%>" required/>
                <br/>
                <label for="address" class="form-label"><%="Address (Start Typing to auto-fill)"%></label>
                <input type="search" id="address" class="form-control" placeholder="<%=address%>" />
                <br/>
                <label for="address-value"></label>
                <input type="text" name="address-value" id="address-value" value="<%=address%>" hidden/>
                <br/>
                <label for="tutor" class="form-label"><%="Tutor for Year"%></label>
                <select class="form-control" class="select-css" name="tutor" id="tutor" required>
                    <%--Iterate each year, Use these for options --%>
                    <option value="">None</option>
                    <% Map<String,String> allYears = (Map<String,String>) session.getAttribute("allYears");
                        if(allYears != null) {
                            for (Map.Entry<String, String> entry : allYears.entrySet())
                            {%>
                    <option <% if(entry.getKey().equals(year)){ %> selected <% } %> value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
                    <%}
                    }%>
                </select>
                <br/>
                <label for="pword" class="form-label"><%="Old Password"%></label>
                <input type="password" name="pword" id="pword" minlength="8" class="form-control" required/>
                <br/>
                <label for="newPword" class="form-label"><%="New Password"%></label>
                <input type="password" name="newPword" id="newPword" minlength="8" class="form-control"/>
                <br/>
                <label for="pwordConfirm" class="form-label"><%="Confirm New Password"%></label>
                <input type="password" name="pwordConfirm" id="pwordConfirm" minlength="8" class="form-control"/>
                <br/>
                <div class="alert alert-danger" role="alert" id="pwordErrors" style="display: none">Passwords do not match!</div>
                <br/>
                <h3>Email Settings</h3>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" name="homeworkEmail" id="homeworkEmail" <%if(homeworkEmail){ %> checked <% } %> >
                    <label class="custom-control-label" for="homeworkEmail">Email me for Homework updates</label>
                </div>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" name="calendarEmail" id="calendarEmail" <%if(calendarEmail){ %> checked <% } %> >
                    <label class="custom-control-label" for="calendarEmail">Email me for Calendar updates</label>
                </div>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" name="profileEmail" id="profileEmail" <%if(profileEmail){ %> checked <% } %> >
                    <label class="custom-control-label" for="profileEmail">Email me for Account updates</label>
                </div>
                <br/>
                <a type="button" style="flex:1" class="btn btn-secondary" href="${pageContext.request.contextPath}/servlets/users/teacher/TeacherDelete?email=<%=email%>" >Delete Account</a>
                <input class="btn btn-primary" type="submit" value="Update Changes">
            </form>
        </div>

        <footer class="footer">
            <div class="">
                <span class="text-muted">CT6039 Project by S1707031 &copy;2021</span>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=home>&nbsp;Return Home&nbsp;</a>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=child-login>&nbsp;Child Login&nbsp;</a>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=parent-login>&nbsp;Parent Login&nbsp;</a>
                <a href=${pageContext.request.contextPath}/servlets/Redirects?location=teacher-login>&nbsp;Teacher Login&nbsp;</a>
            </div>
        </footer>

        <script>
            //Address Search
            (function() {
                let placesAutocomplete = places({
                    container: document.querySelector('#address')
                });

                let $address = document.querySelector('#address-value')
                placesAutocomplete.on('change', function(e) {
                    $address.textContent = e.suggestion.value;
                    $('#address-value').val(e.suggestion.value);
                });

                placesAutocomplete.on('clear', function() {
                    $address.textContent = 'none';
                });
            })();

            //Password validation
            let pword = $("#newPword");
            let pwordConfirm = $("#pwordConfirm");
            function verifyPword() {
                let pwordErrors = $("#pwordErrors");
                if (pword.val() !== pwordConfirm.val()) {
                    pwordErrors.show();
                } else {
                    pwordErrors.hide();
                }
            }

            //Title "Other" input
            let titleSelect = $("#title");
            titleSelect.on('change', function(){
                let val = titleSelect.val();
                let titleVal = $(".titleval");
                console.log(val);
                if (val !== "Mr" && val !== "Mrs" && val !== "") {
                    titleVal.show();
                    titleVal.removeAttr("hidden");
                    titleVal.removeAttr("required");
                } else {
                    titleVal.hide();
                    titleVal.attr("hidden");
                    titleVal.attr("required");
                }
            });

            pword.keyup(function() {
                verifyPword();
            });
            pwordConfirm.keyup(function() {
                verifyPword();
            });
        </script>
    </body>
</html>
