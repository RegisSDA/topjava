package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

/**
 * Created by MSI on 23.08.2017.
 */
@Controller
public class MealController {
    @Autowired
    private MealService service;

    @RequestMapping(value = "/createTemplate",method = RequestMethod.GET)
    public String createTemplate(Model model){
        model.addAttribute("meal",new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) );
        model.addAttribute("action","create");
        return "mealForm";
    }
    @RequestMapping(value = "/updateTemplate",method = RequestMethod.GET)
    public String updateTemplate(Model model, HttpServletRequest request){
        model.addAttribute("meal",service.get(getId(request), AuthorizedUser.id()));
        model.addAttribute("action","update");
        return "mealForm";
    }
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request){
        service.delete(getId(request),AuthorizedUser.id());
        return "redirect:meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String getAll(Model model){
        model.addAttribute("meals",MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.id()),AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(HttpServletRequest request){
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.create(meal,AuthorizedUser.id());
        return "redirect:meals";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(HttpServletRequest request){
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        assureIdConsistent(meal, getId(request));
        service.update(meal, AuthorizedUser.id());
        return "redirect: meals";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String filter(Model model, HttpServletRequest request){
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }





    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
    public List<MealWithExceed> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = AuthorizedUser.id();

        return MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
    }
}
