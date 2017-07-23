package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class MealRestController {
    private final static Logger log = getLogger(MealRestController.class);
    @Autowired
    private MealService service;

    public void setService(MealService service) {
        this.service = service;
    }

    public Meal get(int id){
        return service.get(id,AuthorizedUser.id());
    }

    public void delete(int id){
        service.delete(id,AuthorizedUser.id());
    }

    public void save(Meal meal){
        service.save(meal,AuthorizedUser.id());
    }

    public Collection<Meal> getAll(){
        return  service.getAll(AuthorizedUser.id());
    }

    public Meal createNew(){
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, AuthorizedUser.id());
    }
    public Meal createNew(Integer id, LocalDateTime dateTime, String description, int calories){
        return new Meal(id,dateTime,description,calories,AuthorizedUser.id());
    }
    public List<MealWithExceed> getWithExceeded(String startTime, String endTime, String startDate, String endDate){
        LocalDate localStartDate = parseDate(startDate);
        LocalDate localEndDate = parseDate(endDate);
        LocalTime localStartTime = parseTime(startTime);
        LocalTime localEndTime = parseTime(endTime);


        return  MealsUtil.getFilteredWithExceeded(getAll(),localStartTime,localEndTime,localStartDate,localEndDate,MealsUtil.DEFAULT_CALORIES_PER_DAY);

    }

    private LocalDate parseDate (String date ){
        log.info("parseDate ="+date);
        return date==null||"".equals(date)?null:LocalDate.parse(date);
    }
    private LocalTime parseTime (String time ){
        log.info("parseTime ="+time);
        return time==null||"".equals(time)?null:LocalTime.parse(time);
    }

}