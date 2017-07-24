package ru.javawebinar.topjava.web;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.meal.MealRestController;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.*;

public class MealServlet extends HttpServlet {

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
        controller.doPost(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        controller.doGet(request,response);
    }

    @Override
    public void destroy(){
        appCon.close();
    }

}
