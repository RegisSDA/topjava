package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;


public class MealTestData {
    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100001;


    public static final Meal MEAL_1 = new Meal(100002, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_2 = new Meal(100003, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(100004, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_4 = new Meal(100005, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_5 = new Meal(100006, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000);
    public static final Meal MEAL_6 = new Meal(100007, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);


    public static List<Meal> generateMealsList(Meal... meals) {
        return Arrays.asList(meals);
    }

    public static Meal generateNewMeal() {
        return new Meal(LocalDateTime.of(2015, Month.MAY, 10, 13, 0), "Обед", 10000);
    }

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>();

    public static final BeanMatcher<MealWithExceed> MATCHER_EXCEEDED = new BeanMatcher<>();
}
