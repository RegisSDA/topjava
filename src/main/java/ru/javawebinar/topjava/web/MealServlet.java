package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

/**
 * Created by MSI on 14.07.2017.
 */
public class MealServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("mealList", MealsUtil.getFilteredWithExceeded(MealsUtil.getMeal(),LocalTime.of(0,0),LocalTime.MAX,2000));



        req.getRequestDispatcher("meals.jsp").forward(req,resp);
    }
}
