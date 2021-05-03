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
        <link rel="icon" type="image/gif" href="${pageContext.request.contextPath}/imgs/favicon.ico">
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
                });

                /* initialize the calendar
                -----------------------------------------------------------------*/
                let calElement = $('#calendar');
                let calendar =  calElement.fullCalendar({
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
                        let eventName = prompt('Add Event - Event Name:');
                        let dateForUpdate = start.getFullYear() + "-" + start.getMonth() + "-" + start.getDate();
                        let currentUser = $("#currentUser").val();
                        let myData = {"currentUser":currentUser, "eventDate":start, "eventName":eventName, "dateForUpdate":dateForUpdate};
                        if (eventName) {
                            $.ajax({
                                type: "POST",
                                url: "${pageContext.request.contextPath}/servlets/calendar/AddCalendarEvent",
                                data: myData,
                                dataType: "text",
                                async: false,
                                success : function(data){
                                    console.log("success", data);
                                },
                                error : function(result) {
                                    console.log("Failed to add event: ", result);
                                },
                                always : function(result){
                                    console.log(result);
                                    if ("${!empty sessionScope['newlyAddedEvent']}") {
                                        calendar.fullCalendar('renderEvent',
                                            {
                                                title: eventName,
                                                start: start,
                                                end: end,
                                                className: 'info',
                                                url: '${pageContext.request.contextPath}/servlets/calendar/CalendarActions?eventId='+"${sessionScope['newlyAddedEvent']}"
                                            },
                                            true // make the event "stick"
                                        );
                                    }
                                }
                            });
                            location.reload();
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
                        calElement.fullCalendar('renderEvent', copiedEventObject, true);

                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    },
                    events: [],
                });

                let allUserEvents = getAllEventsForUser();
                addAllEvents(allUserEvents);

            function getAllEventsForUser(){
                let eventsForUser = {events: []};
                let currentUser = $("#currentUser").val();
                let myData = {"currentUser":currentUser};
                $.ajax({
                    async : false,
                    type: "GET",
                    url: "${pageContext.request.contextPath}/servlets/calendar/RetrieveCalendarEvent",
                    data: myData,
                    contentType: "json",
                    success : function(result) {
                        let parsedJSONEvents = $.parseJSON(result);
                        for(let i = 0; i < parsedJSONEvents.length; i++) {
                            if (i !== parsedJSONEvents.length - 1) {
                                let customDate = parsedJSONEvents[i].fDateForUpdate;
                                let split = customDate.split("-");
                                let year = split[0];
                                let month = split[1];
                                let day = split[2];
                                let event = {title: parsedJSONEvents[i].fEventName,
                                    start: new Date(year, month, day),
                                    end: new Date(year, month, day),
                                    url: "${pageContext.request.contextPath}/servlets/calendar/CalendarActions?eventId="+parsedJSONEvents[i].fEventId,
                                    className: 'info'};
                                if (event !== {}) {
                                    if (i === 0) {
                                        eventsForUser.events = [event];
                                    } else {
                                        eventsForUser.events.push(event);
                                    }
                                }
                            } else {
                                let customDate = parsedJSONEvents[i].fDateForUpdate;
                                let split = customDate.split("-");
                                let year = split[0];
                                let month = split[1];
                                let day = split[2];
                                let event = {title: parsedJSONEvents[i].fEventName,
                                    start: new Date(year, month, day),
                                    end: new Date(year, month, day),
                                    url: "${pageContext.request.contextPath}/servlets/calendar/CalendarActions?eventId="+parsedJSONEvents[i].fEventId,
                                    className: 'info'};
                                if (event !== {}) {
                                    if (i === 0) {
                                        eventsForUser.events = [event];
                                    } else {
                                        eventsForUser.events.push(event);
                                    }
                                }
                            }
                        }
                    },
                    error : function(result) {
                        console.log("Failed to retrieve event: ", result);
                    }
                });
                return eventsForUser.events;
            }

            function addAllEvents(allUserEvents){
                for(let i = 0; i < allUserEvents.length; i++)
                {
                    let eventToAdd = {
                        title: allUserEvents[i].title,
                        start: allUserEvents[i].start,
                        end: allUserEvents[i].end,
                        className: allUserEvents[i].className,
                        url: allUserEvents[i].url
                    }
                    calElement.fullCalendar('renderEvent', eventToAdd, true);
                }
            }
            });
        </script>
    </head>

    <body class="d-flex flex-column">
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
                                <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/servlets/Redirects?location=calendar">Calendar</a>
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
            <% String shouldNotify = (String) session.getAttribute("shouldNotify");
                if(shouldNotify != null) { %>
            <div class="alert alert-success mysuccess" role="alert" id="shouldNotify"><%=shouldNotify%></div>
            <%}%>
            <%}%>

            <%--Title--%>
            <div class="main-body-content myheader neat">
                <h1 class="myheader neat"><%="My Calendar"%></h1>
                <br/>

                <p class="myPara neat">
                    <%="To add an event, please click on the desired day."%>
                    <br/>
                    <%="To edit or delete an event, click the event on the Calendar."%>
                    <br/>
                    <label for="currentUser"></label>
                    <input type="text" name="currentUser" id="currentUser" value="<%=email%>" hidden/>
                    <br/>
                </p>
            </div>

            <%--If event selected, create dialog popup to view/edit/delete event information--%>
            <%  String eventId = (String) session.getAttribute("eventId");
                String eventName = (String) session.getAttribute("eventName");
                String eventUser = (String) session.getAttribute("eventUser");
                String eventDate = (String) session.getAttribute("eventDate");
                String eventUpdateDate = (String) session.getAttribute("eventUpdateDate");
                String month = null;
                String newMonth = null;
                String newDate = eventUpdateDate;
                if(eventUpdateDate != null)
                {
                	String[] split = eventUpdateDate.split("-");
                	month = split[1];
                	if(month.equals("00")){
                		newMonth = "01";
                	}
                	else if(month.equals("01")){
                		newMonth = "02";
                	}
                	else if(month.equals("02")){
                		newMonth = "03";
                	}
                	else if(month.equals("03")){
                		newMonth = "04";
                	}
                	else if(month.equals("04")){
                		newMonth = "05";
                	}
                	else if(month.equals("05")){
                		newMonth = "06";
                	}
                	else if(month.equals("06")){
                		newMonth = "07";
                	}
                	else if(month.equals("07")){
                		newMonth = "08";
                	}
                	else if(month.equals("08")){
                		newMonth = "09";
                	}
                	else if(month.equals("09")){
                		newMonth = "10";
                	}
                	else if(month.equals("10")){
                		newMonth = "11";
                	}
                	else if(month.equals("11")){
                		newMonth = "12";
                	}
                	else{
                		newMonth = month;
                	}

                	newDate = split[0] + "-" + newMonth + "-" + split[2];
                }

            if(eventId != null && eventName != null && eventUser != null && eventDate != null && eventUpdateDate != null) { %>
                <%--Script to load modal (or won't popup)--%>
                <script>
                    $(window).on('load', function(){
                        $('#exampleModalCenter').removeClass("hide").modal('show');
                        $("#exampleModalCenter").appendTo("body");
                    });
                </script>
                <div class="modal fade calendarPopup" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content shadow p-3 mb-5 bg-white rounded">
                            <div class="modal-header">
                                <h5 class="modal-title formPara" id="exampleModalLongTitle">Event #<%=eventId%></h5>
                                <button type="button" class="close close-btn" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <form class="calendar-form" action="${pageContext.request.contextPath}/servlets/calendar/UpdateCalendarEvent" method="POST">
                                <div class="modal-body">
                                    <%--Content for modal popup containing calendar event information--%>
                                    <label for="eventUpdateId"></label>
                                    <input type="text" name="eventUpdateId" id="eventUpdateId" value="<%=eventId%>" hidden/>
                                    <label for="newEventUpdateDate"></label>
                                    <input type="text" name="newEventUpdateDate" id="newEventUpdateDate" value="<%=eventDate%>" hidden/>
                                    <label for="eventUpdateName" class="form-label formPara"><%="Event Name"%></label>
                                    <input class="form-control formParaText" type="text" name="eventUpdateName" id="eventUpdateName" value="<%=eventName%>" required/>
                                    <br/>
                                    <label for="eventUpdateDate" class="form-label formPara"><%="Event Date"%></label>
                                    <input type="date" name="eventUpdateDate" id="eventUpdateDate" class="form-control formParaText" value="<%=newDate%>" required/>
                                    <br/>
                                    <label for="eventUpdateUser" class="form-label formPara"><%="Event For User"%></label>
                                    <input class="form-control formParaText" type="text" name="eventUpdateUser" id="eventUpdateUser" disabled value="<%=eventUser%>" />
                                    <br/>
                                </div>
                                <div class="modal-footer">
                                    <a type="button" class="btn btn-secondary formParaText formBtn" href="${pageContext.request.contextPath}/servlets/calendar/DeleteCalendarEvent?eventId=<%=eventId%>" >Delete Event</a>
                                    <button class="btn btn-primary formParaText formBtn" type="submit">Save Changes</button>
                                    <script>
                                        $(".close-btn").on('click', function (){
                                            $("#exampleModalCenter").modal('hide');
                                        });

                                        let eventDate = $("#eventUpdateDate");
                                        function getDate() {
                                            let dateInput = $("#newEventUpdateDate");
                                            dateInput.val(new Date(eventDate.val()));
                                        }
                                        eventDate.on('change', function() {
                                            getDate();
                                        });
                                    </script>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            <%}%>

            <%--Calendar--%>
            <div id='wrap'>
                <div id='calendar' class="shadow p-3 mb-5 bg-white rounded"></div>

                <div style='clear:both'></div>
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
    </body>
</html>
