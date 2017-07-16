<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.07.2017
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<table border>
    <h2>Edit Meal</h2>
    <form id="first" method="post" action="edit"accept-charset="UTF-8">
            <input type="hidden" name="mealid" value="<%=request.getParameter("mealid")%>">
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
    </form>
</table>
        <p><input form="first" type="submit" value="Edit Meal"></p>

</body>
</html>
