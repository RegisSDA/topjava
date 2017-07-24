<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meal list</h2>
    <a href="meals?action=create&startTime=${startTime}&endTime=${endTime}&startDate=${startDate}&endDate=${endDate}">Add Meal</a>
    <hr/>

    <h2>Select Date and Time</h2>
    <form method="get" action="meals">
    <table border>
        <thead>
        <tr>
            <th>Beginning</th>
            <th>End</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <input type="time" name="startTime" value="${startTime}"/>
            </td>
            <td>
                <input type="time" name="endTime" value="${endTime}"/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="date" name="startDate" value="${startDate}"/>
            </td>
            <td>
                <input type="date" name="endDate" value="${endDate}"/>
            </td>
        </tr>

        </tbody>
    </table>
        <input type="submit"/>
    </form>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}&startTime=${startTime}&endTime=${endTime}&startDate=${startDate}&endDate=${endDate}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}&startTime=${startTime}&endTime=${endTime}&startDate=${startDate}&endDate=${endDate}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>