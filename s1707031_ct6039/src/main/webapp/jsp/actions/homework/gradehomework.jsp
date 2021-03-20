<%@ page import="com.uog.miller.s1707031_ct6039.beans.HomeworkBean" %>
<%@ page import="com.uog.miller.s1707031_ct6039.beans.SubmissionBean" %><%--
  Created by IntelliJ IDEA.
  User: Denny-Jo
  Date: 20/03/2021
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mark Homework</title>
</head>
<body>
<%SubmissionBean submissionBean = (SubmissionBean) session.getAttribute("homeworkForGrading"); %>
<%String childEmail = (String) session.getAttribute("childForGrading"); %>
Test mark HW
<a href="${pageContext.request.contextPath}/servlets/homework/GradeHomework?homeworkId=<%=submissionBean.getEventId()%>"><%="Mark"%></a>

</body>
</html>
