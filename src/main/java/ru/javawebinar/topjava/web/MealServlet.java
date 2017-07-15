package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.DBService.DBService;
import ru.javawebinar.topjava.DBService.DataBaseFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by MSI on 14.07.2017.
 */
public class MealServlet extends HttpServlet{

    private DBService dataBase = DataBaseFactory.getDataBase();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("mealList", MealsUtil.getFilteredWithExceeded(dataBase.getMealsByUser("template"),LocalTime.of(0,0),LocalTime.MAX,2000));
        req.getRequestDispatcher("meals.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name= req.getParameter("description");
        int calloris = Integer.parseInt(req.getParameter("number"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("datetime-local"));
        dataBase.save(new Meal(dateTime,name,calloris));
        resp.sendRedirect("meals");

    }
}
