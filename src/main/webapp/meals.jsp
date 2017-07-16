<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 14.07.2017
  Time: 18:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>

    <title>meals</title>
    <style>

        .red{
            color: red;
        }
        .green {
            color: green; /* Цвет текста */
        }
        
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<h1>User Meals</h1>
<table border>
    <thead>
       Meals List
    </thead>
    <tbody>
        <tr>
            <th>
                Name
            </th>
            <th>
                Date
            </th>
            <th>
                Time
            </th>
            <th>
                Calloris
            </th>
            <th>
                Delete
            </th>
            <th>
                Edit
            </th>
        </tr>

        <c:forEach var="meal" items="${mealList}">

                    <tr  bgcolor="${meal.exceed?"red":"green"}">
                        <td >${meal.description}</td> <td>${meal.date}</td><td>${meal.time}</td><td>${meal.calories}</td>
                        <td><form method="get" action="edit"><input type="hidden" name="mealid" value="${meal.id}"><input type="submit" value="Delete"></form></td>
                        <td><form method="post" action="edit.jsp"><input type="hidden" name="mealid" value="${meal.id}"><input type="submit" value="Edit"></form></td>

                    </tr>

        </c:forEach>
    </tbody>
</table>
<h2>Add Meal</h2>
<form id="first" method="post" action="meals"accept-charset="UTF-8">
<table border>
    <tbody>
    <tr>
        <th>
            Name
        </th>
        <th>
            Date and Time
        </th>
        <th>
            Calloris
        </th>
    </tr>

    <tr>
        <th>
            <input form="first" type="text" name = "description">
        </th>
        <th>
            <div>
                <input form="first" type="datetime-local" name="datetime-local">
            </div>
        </th>
        <th>
            <div>
                <input form="first" type="number" name="number">
            </div>
        </th>
    </tr>
    </tbody>
</table>
    <p><input form="first" type="submit" value="Add Meal"></p>
</form>
</body>
</html>
