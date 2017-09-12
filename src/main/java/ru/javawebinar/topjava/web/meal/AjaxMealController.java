package ru.javawebinar.topjava.web.meal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.meal.AjaxMealController.BASE_URL;

/**
 * Created by MSI on 11.09.2017.
 */

@RestController
@RequestMapping(value = BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AjaxMealController extends AbstractMealController {
    static final String BASE_URL = "/ajax/meals";


    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @GetMapping
    public List<MealWithExceed> getAll() {

        System.out.println("getAll");
        return super.getAll();
    }


    @PostMapping
    public void createWithLocation(
            @RequestParam("id") Integer id,
            @RequestParam("datetime") LocalDateTime dateTime,
            @RequestParam("description") String description,
            @RequestParam("calories") int calories
    ) {

        Meal meal = new Meal(id, dateTime, description, calories);
        System.out.println("Attempt");
        if (id == null) {
            super.create(meal);
        }

    }

    @Override
    @GetMapping(value = "filter")
    public List<MealWithExceed> getBetween(
            @RequestParam(value = "startDate", required = false) LocalDate startDate, @RequestParam(value = "startTime", required = false) LocalTime startTime,
            @RequestParam(value = "endDate", required = false) LocalDate endDate, @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
