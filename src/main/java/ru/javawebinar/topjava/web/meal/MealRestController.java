package ru.javawebinar.topjava.web.meal;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


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


    public List<MealWithExceed> getAll() {
        return MealsUtil.getFilteredWithExceeded(service.getAll(AuthorizedUser.id()), LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> getAll(String inStartDate, String inEndDate, String inStartTime, String inEndTime) {
        //парсим дату, если null или пуствая - возвращаем null;
        LocalDate startDate = parseDate(inStartDate);
        LocalDate endDate = parseDate(inEndDate);

        LocalTime startTime = parseTime(inStartTime);
        LocalTime endTime = parseTime(inEndTime);

        //если дата/время null - присваяваем min/max значения
        startTime = startTime == null ? LocalTime.MIN : startTime;
        endTime = endTime == null ? LocalTime.MAX : endTime;

        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX : endDate;

        return MealsUtil.getFilteredWithExceeded(
                service.getAll(AuthorizedUser.id(), startDate, endDate), startTime,
                endTime, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public void delete(int id) {
        service.delete(id, AuthorizedUser.id());
    }

    public Meal get(int id) {
        return service.get(id, AuthorizedUser.id());
    }

    public Meal save(Meal meal) {
        log.info("Create {}", meal);
        ValidationUtil.checkNew(meal);
        return service.save(meal, AuthorizedUser.id());
    }

    public void update(Meal meal, int id) {
        log.info("Update {}", meal);
        ValidationUtil.checkIdConsistent(meal, id);
        service.save(meal, AuthorizedUser.id());
    }

    public Meal createTamplate() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, AuthorizedUser.id());
    }

    public Meal createNewMeal(String id, String dateTime, String description, String calories) {
        return new Meal(id.isEmpty() ? null : Integer.valueOf(id), LocalDateTime.parse(dateTime),
                description, Integer.valueOf(calories), AuthorizedUser.id());
    }

    private LocalDate parseDate(String date) {
        log.info("parseDate =" + date);
        return date == null || "".equals(date) ? null : LocalDate.parse(date);
    }

    private LocalTime parseTime(String time) {
        log.info("parseTime =" + time);
        return time == null || "".equals(time) ? null : LocalTime.parse(time);
    }
}

