package ru.javawebinar.topjava.web;


import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import org.springframework.context.*;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final static Logger log = getLogger(MealServlet.class);
    private MealRestController controller;
    private ConfigurableApplicationContext appCon;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCon = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = (MealRestController) appCon.getBean("mealRestController");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = controller.createNewMeal(id,
                request.getParameter("dateTime"),
                request.getParameter("description"),
                request.getParameter("calories"));
        if (id.isEmpty()) {
            controller.save(meal);
        } else {
            //по заданию передаем в update контроллера id
            controller.update(meal, Integer.parseInt(id));
        }
        //отображаем список после сохранения
        showStartPageWithParameters(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                controller.delete(getId(request));
                showStartPageWithParameters(request, response);
                break;
            case "create":
                request.setAttribute("meal", controller.createTamplate());
                setDateTimeAtributes(request);
                request.getRequestDispatcher("meal.jsp").forward(request, response);
                break;
            case "update":
                request.setAttribute("meal", controller.get(getId(request)));
                setDateTimeAtributes(request);
                request.getRequestDispatcher("meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                showStartPageWithParameters(request, response);
                break;
        }
    }

    @Override
    public void destroy() {
        appCon.registerShutdownHook();
    }


    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    //проверяет есть ли параметры даты/времени, вызывает соответствующий getAll, устанавливает атрибуты даты/времени и результат и отображает его
    private void showStartPageWithParameters(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sD = request.getParameter("startDate");
        String eD = request.getParameter("endDate");
        String sT = request.getParameter("startTime");
        String eT = request.getParameter("endTime");
        log.info("{} - {} - {} - {}", sD, eD, sT, eT);
        if ((sD == null || sD.isEmpty()) &&
                (eD == null || eD.isEmpty()) &&
                (sT == null || sT.isEmpty()) &&
                (eT == null || eT.isEmpty())) {
            log.info("getAll without parameters");
            request.setAttribute("meals", controller.getAll());
        } else {
            log.info("getAll with parameters");
            request.setAttribute("meals", controller.getAll(sD, eD, sT, eT));
        }
        setDateTimeAtributes(request);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    private void setDateTimeAtributes(HttpServletRequest request) {
        request.setAttribute("startDate", request.getParameter("startDate"));
        request.setAttribute("endDate", request.getParameter("endDate"));
        request.setAttribute("startTime", request.getParameter("startTime"));
        request.setAttribute("endTime", request.getParameter("endTime"));
    }
}
