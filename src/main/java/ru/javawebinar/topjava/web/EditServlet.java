package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.DAO.MealDAO;
import ru.javawebinar.topjava.DAO.MealDAOFactory;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by MSI on 16.07.2017.
 */
public class EditServlet extends HttpServlet {

    MealDAO dataBase = MealDAOFactory.getMealDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.valueOf(req.getParameter("mealid"));
        dataBase.delete(id);
        resp.sendRedirect("meals");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("mealid"));
        String name= req.getParameter("description");
        int calloris = Integer.parseInt(req.getParameter("number"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("datetime-local"));
        dataBase.update(new Meal(dateTime,name,calloris),id);
        resp.sendRedirect("meals");
    }
}
