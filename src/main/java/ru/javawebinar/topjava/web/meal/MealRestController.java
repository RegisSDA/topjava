package ru.javawebinar.topjava.web.meal;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class MealRestController {
    private final static Logger log = getLogger(MealRestController.class);
    @Autowired
    private MealService service;

    /*
    public void setService(MealService service) {
        this.service = service;
    }
    */


    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")),AuthorizedUser.id());

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        service.save(meal,AuthorizedUser.id());
        //отображаем список после сохранения
        getAllWithExceeded(request,response);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                delete(request,response);
                break;
            case "create":
                create(request,response);
                break;
            case "update":
                update(request,response);
                break;
            case "all":
            default:
                getAllWithExceeded(request,response);
                break;
        }

    }


    private void create (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        log.info("Create template Meal");
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, AuthorizedUser.id());
        request.setAttribute("meal", meal);
        //устанавливаем дату/время из параметров запроса в качестве атрибутов для генерации новых параметров на jsp
        setDateTimeAtributes(request);
        request.getRequestDispatcher("/meal.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Meal meal = service.get(getId(request),AuthorizedUser.id());
        log.info("Get meal",meal);
        request.setAttribute("meal", meal);
        setDateTimeAtributes(request);
        request.getRequestDispatcher("/meal.jsp").forward(request, response);
    }

    private void delete (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        int id = getId(request);
        log.info("Delete {}", id);
        service.delete(id,AuthorizedUser.id());
        getAllWithExceeded(request,response);
    }

    //отображает еду с фильтрацией по дате/времени, при нулевых/пустых значениях выставляем мин/макс значения
    private void getAllWithExceeded(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        log.info("getAll");


        //получаем дату и время из строк запроса, если строка null или пустая - получаем null
        LocalDate startDate = parseDate(request.getParameter("startDate"));
        LocalDate endDate = parseDate(request.getParameter("endDate"));

        LocalTime startTime = parseTime(request.getParameter("startTime"));
        LocalTime endTime = parseTime(request.getParameter("endTime"));

        //если дата/время null - присваяваем min/max значения
        startTime = startTime==null?LocalTime.MIN:startTime;
        endTime = endTime==null?LocalTime.MAX:endTime;

        startDate = startDate==null?LocalDate.MIN:startDate;
        endDate = endDate==null?LocalDate.MAX:endDate;

        //установка параметров времени/даты для отображения поумолчанию
        setDateTimeAtributes(request);
        request.setAttribute("meals",MealsUtil.getFilteredWithExceeded(
                service.getAll(AuthorizedUser.id(), startDate,endDate),startTime,
                endTime,MealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.getRequestDispatcher("meals.jsp").forward(request,response);
    }


    private LocalDate parseDate (String date ){
        log.info("parseDate ="+date);
        return date==null||"".equals(date)?null:LocalDate.parse(date);
    }
    private LocalTime parseTime (String time ){
        log.info("parseTime ="+time);
        return time==null||"".equals(time)?null:LocalTime.parse(time);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private void setDateTimeAtributes(HttpServletRequest request){
        request.setAttribute("startDate",request.getParameter("startDate"));
        request.setAttribute("endDate",request.getParameter("endDate"));
        request.setAttribute("startTime",request.getParameter("startTime"));
        request.setAttribute("endTime",request.getParameter("endTime"));
    }
}