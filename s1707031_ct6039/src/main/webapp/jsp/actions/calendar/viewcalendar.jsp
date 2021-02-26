<%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 19/02/2021
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!Doctype HTML>
<html lang="en">
    <head>
        <title>My Calendar</title>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

        <jsp:include page="../../../jsp/required.jsp"/>
        <link rel="stylesheet" href="../../../css/main.css">
        <link rel="stylesheet" href="../../../css/calendar.css">
        <link href='../../../assets/css/fullcalendar.css' rel='stylesheet' />
        <link href='../../../assets/css/fullcalendar.print.css' rel='stylesheet' media='print' />
        <script src='../../../assets/js/jquery-1.10.2.js' type="text/javascript"></script>
        <script src='../../../assets/js/jquery-ui.custom.min.js' type="text/javascript"></script>
        <script src='../../../assets/js/fullcalendar.js' type="text/javascript"></script>
        <script>

            $(document).ready(function() {
                let date = new Date();
                let d = date.getDate();
                let m = date.getMonth();
                let y = date.getFullYear();

                /* initialize the external events
                -----------------------------------------------------------------*/

                $('#external-events div.external-event').each(function() {

                    // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
                    // it doesn't need to have a start or end
                    let eventObject = {
                        title: $.trim($(this).text()) // use the element's text as the event title
                    };

                    // store the Event Object in the DOM element so we can get to it later
                    $(this).data('eventObject', eventObject);

                    // make the event draggable using jQuery UI
                    /*$(this).draggable({
                        zIndex: 999,
                        revert: true,      // will cause the event to go back to its
                        revertDuration: 0  //  original position after the drag
                    });*/
                });

                /* initialize the calendar
                -----------------------------------------------------------------*/

                let calendar =  $('#calendar').fullCalendar({
                    header: {
                        left: 'title',
                        center: 'month',
                        right: 'prev,next today'
                    },
                    editable: true,
                    firstDay: 1, //  1(Monday) this can be changed to 0(Sunday) for the USA system
                    selectable: true,
                    defaultView: 'month',

                    axisFormat: 'h:mm',
                    columnFormat: {
                        month: 'ddd',    // Mon
                        week: 'ddd d', // Mon 7
                        day: 'dddd M/d',  // Monday 9/7
                        agendaDay: 'dddd d'
                    },
                    titleFormat: {
                        day: 'MMMM yyyy'
                    },
                    allDaySlot: false,
                    selectHelper: true,
                    select: function(start, end, allDay) {
                        let title = prompt('Add Event - Event Name:');
                        if (title) {
                            //TODO - Add event to DB
                            calendar.fullCalendar('renderEvent',
                                {
                                    title: title,
                                    start: start,
                                    end: end,
                                    allDay: allDay,
                                    className: 'info'
                                },
                                true // make the event "stick"
                            );
                        }
                        calendar.fullCalendar('unselect');
                    },
                    droppable: false, // this allows things to be dropped onto the calendar !!!
                    drop: function(date, allDay) { // this function is called when something is dropped

                        // retrieve the dropped element's stored Event Object
                        let originalEventObject = $(this).data('eventObject');

                        // we need to copy it, so that multiple events don't have a reference to the same object
                        let copiedEventObject = $.extend({}, originalEventObject);

                        // assign it the date that was reported
                        copiedEventObject.start = date;
                        copiedEventObject.allDay = allDay;

                        // render the event on the calendar
                        // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
                        $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    },

                    /*Populate events from DB here*/
                    events: [
                        {
                            title: 'All Day Event',
                            start: new Date(y, m, 1)
                        },
                        {
                            id: 999,
                            title: 'Repeating Event',
                            start: new Date(y, m, d-3, 16, 0),
                            allDay: false,
                            className: 'info'
                        },
                        {
                            id: 999,
                            title: 'Repeating Event',
                            start: new Date(y, m, d+4, 16, 0),
                            allDay: false,
                            className: 'info'
                        },
                        {
                            title: 'Meeting',
                            start: new Date(y, m, d, 10, 30),
                            allDay: false,
                            className: 'important'
                        },
                        {
                            title: 'Lunch',
                            start: new Date(y, m, d, 12, 0),
                            end: new Date(y, m, d, 14, 0),
                            allDay: false,
                            className: 'important'
                        },
                        {
                            title: 'Birthday Party',
                            start: new Date(y, m, d+1, 19, 0),
                            end: new Date(y, m, d+1, 22, 30),
                            allDay: false,
                        },
                        {
                            title: 'Click for Google',
                            start: new Date(y, m, 28),
                            end: new Date(y, m, 29),
                            url: 'http://google.com/',
                            className: 'success'
                        }
                    ],
                });
            });
        </script>
    </head>

    <body class="d-flex flex-column">
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
                                <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/servlets/Redirects?location=home">Home</a>
                            </li>
                            <%--If logged in, show nav links, else just have home & account signup/login visible--%>
                            <% String email = (String) session.getAttribute("email");
                                if(email != null) { %>
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/servlets/Redirects?location=calendar">Calendar</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=progress-view">Progress</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/servlets/Redirects?location=homework-view">Homework</a>
                            </li>
                            <%} else { %>
                            <li class="nav-item">
                                <a class="nav-link">You must be signed in to access site features</a>
                            </li>
                            <% } %>
                        </ul>
                        <%--Login/Register Side of navbar. if logged in, show logout/account links--%>
                        <span class="navbar-text">
                            <% if(email != null) { %>
                                <% String isChild = (String) session.getAttribute("isChild");
                                    String isTeacher = (String) session.getAttribute("isTeacher");
                                    String isParent = (String) session.getAttribute("isParent");
                                    if(isChild != null) { %>
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
                <h1><%="My Calendar"%></h1>
                <br/>
            </div>

            <div id='wrap'>
                <div id='calendar'></div>

                <div style='clear:both'></div>
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
    </body>
</html>
