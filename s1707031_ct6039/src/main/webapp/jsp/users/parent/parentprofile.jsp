<%@ page import="com.uog.miller.s1707031_ct6039.beans.LinkBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%--
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
        <title>My Account - Parent</title>
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
                <h1 class="myheader neat"><%="Parent Account Profile"%></h1>
                <br/>
            </div>

            <p class="main-body-text myPara neat">
                <%="From here, you can edit your account and settings."%>
            </p>
            <br/>

            <% String firstname = (String) session.getAttribute("firstname");%>
            <% String surname = (String) session.getAttribute("surname");%>
            <% String dob = (String) session.getAttribute("dob");%>
            <% String address = (String) session.getAttribute("address");%>
            <% List<LinkBean> linkBeans = (List<LinkBean>) session.getAttribute("allLinkBeans");
                StringBuilder linkBeansArray = new StringBuilder();
                for (int i = 0; i < linkBeans.size(); i++)
                {
                    if (i == 0)
                    {
                    	linkBeansArray = new StringBuilder(linkBeans.get(i).getChildEmail().trim());
                    }
                    else
                    {
                        linkBeansArray.append(",").append(linkBeans.get(i).getChildEmail().trim());
                    }
                }%>
            <% Boolean homeworkEmail = (Boolean) session.getAttribute("homeworkEmail");%>
            <% Boolean calendarEmail = (Boolean) session.getAttribute("calendarEmail");%>
            <% Boolean profileEmail = (Boolean) session.getAttribute("profileEmail");%>
            <div class="card myform shadow p-3 mb-5 bg-white rounded">
                <form class="reg-form" action="${pageContext.request.contextPath}/servlets/users/parent/ParentProfile" method="POST">
                    <br/>
                    <div class="card-body">
                        <h3 class="formPara formHeader">Profile Settings</h3>
                        <br/>
                        <label for="child-value" hidden></label>
                        <input type="text" name="child-value" id="child-value" value="<%=linkBeansArray.toString()%>" hidden/>
                        <br/>
                        <label for="firstname" class="form-label formPara"><%="Firstname"%></label>
                        <input type="text" name="firstname" id="firstname" class="form-control formParaText" value="<%=firstname%>" required/>
                        <br/>
                        <label for="surname" class="form-label formPara"><%="Surname"%></label>
                        <input type="text" name="surname" id="surname" class="form-control formParaText" value="<%=surname%>" required/>
                        <br/>
                        <label for="email" class="form-label formPara"><%="Email (This cannot be changed)"%></label>
                        <input type="email" name="email" id="email" class="form-control formParaText" value="<%=email%>" disabled required/>
                        <br/>
                        <label for="dob" class="form-label formPara"><%="Date of Birth"%></label>
                        <input type="date" name="dob" id="dob" class="form-control formParaText" value="<%=dob%>" required/>
                        <br/>
                        <label for="address" class="form-label formPara"><%="Address (Start Typing to auto-fill)"%></label>
                        <input type="search" id="address" class="form-control formParaText" placeholder="<%=address%>" />
                        <br/>
                        <label for="address-value"></label>
                        <input type="text" name="address-value" id="address-value" value="<%=address%>" hidden/>
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
                        <br/>
                        <label for="pword" class="form-label formPara"><%="Old Password"%></label>
                        <input type="password" name="pword" id="pword" minlength="8" class="form-control formParaText" required/>
                        <br/>
                        <label for="newPword" class="form-label formPara"><%="New Password"%></label>
                        <input type="password" name="newPword" id="newPword" minlength="8" class="form-control formParaText"/>
                        <br/>
                        <label for="pwordConfirm" class="form-label formPara"><%="Confirm New Password"%></label>
                        <input type="password" name="pwordConfirm" id="pwordConfirm" minlength="8" class="form-control formParaText"/>
                        <div class="alert alert-danger myalert formParaText" role="alert" id="pwordErrors" style="display: none">Passwords do not match!</div>
                        <br/>
                        <h3 class="formPara formHeader">Email Settings</h3>
                        <br/>
                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input" name="homeworkEmail" id="homeworkEmail" <%if(homeworkEmail){ %> checked <% } %> >
                            <label class="custom-control-label formParaText" for="homeworkEmail">Email me for Homework updates</label>
                        </div>
                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input" name="calendarEmail" id="calendarEmail" <%if(calendarEmail){ %> checked <% } %> >
                            <label class="custom-control-label formParaText" for="calendarEmail">Email me for Calendar updates</label>
                        </div>
                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input" name="profileEmail" id="profileEmail" <%if(profileEmail){ %> checked <% } %> >
                            <label class="custom-control-label formParaText" for="profileEmail">Email me for Account updates</label>
                        </div>
                        <br/>
                    </div>
                    <div class="myformbtn">
                        <a type="button" style="flex:1" class="btn btn-secondary formBtn" href="${pageContext.request.contextPath}/servlets/users/parent/ParentDelete?email=<%=email%>" >Delete Account</a>
                        <input class="btn btn-primary formBtn" type="submit" value="Update Changes">
                    </div>
                </form>
                <br/>
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

            pword.keyup(function() {
                verifyPword();
            });
            pwordConfirm.keyup(function() {
                verifyPword();
            });

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
