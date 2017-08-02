package ru.javawebinar.topjava.web.meal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by MSI on 01.08.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-test.xml"
})
@RunWith(SpringRunner.class)
public class InMemoryMealRestControllerTest {

    private List<Meal> localMeals;
    @Autowired
    private MealRestController controller;

    @Autowired
    private MealRepository repository;

    @Before
    public void setUp() throws Exception {
        //создаем локальный экземпляр тестовых данных
        localMeals = new ArrayList<>(generateMealsList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1));

        repository.getAll(USER_ID).forEach(a -> repository.delete(a.getId(), USER_ID));

        //при занесении в базу тестовые Meal автоматически получают актульные id, тк передача ссылки
        for (Meal meal : localMeals) {
            meal.setId(null);
            repository.save(meal, USER_ID);
        }
    }

    @Test
    public void get() throws Exception {
        Meal meal = localMeals.get(0);
        MATCHER.assertEquals(meal, controller.get(meal.getId()));
    }

    @Test
    public void delete() throws Exception {
        Meal meal = localMeals.get(0);
        controller.delete(meal.getId());
        localMeals.remove(meal);
        MATCHER_EXCEEDED.assertCollectionEquals(MealsUtil.getFilteredWithExceeded(localMeals, LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY), controller.getAll());
    }

    @Test
    public void getAll() throws Exception {
        MATCHER_EXCEEDED.assertCollectionEquals(MealsUtil.getFilteredWithExceeded(localMeals, LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY), controller.getAll());
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.MAY, 23, 10, 0), "Завтрак", 500);
        controller.create(newMeal);
        MATCHER.assertEquals(newMeal, controller.get(newMeal.getId()));

    }

    @Test
    public void update() throws Exception {
        Meal meal = localMeals.get(0);
        meal.setCalories(10000);
        controller.update(meal, meal.getId());
        MATCHER.assertEquals(meal, controller.get(meal.getId()));
    }

    @Test
    public void getBetween() throws Exception {
        //удалаяем еду за 31
        localMeals.remove(0);
        localMeals.remove(0);
        localMeals.remove(0);

        MATCHER_EXCEEDED.assertCollectionEquals(
                MealsUtil.getFilteredWithExceeded(localMeals, LocalTime.of(7, 0), LocalTime.of(18, 0), MealsUtil.DEFAULT_CALORIES_PER_DAY),
                controller.getBetween(LocalDate.MIN, LocalTime.of(7, 0), LocalDate.of(2015, 5, 30), LocalTime.of(18, 0)));
    }

}