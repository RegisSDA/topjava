package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MealTestData {
    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100001;

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(100002, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(100003, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(100004, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(100005, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500),
            new Meal(100006, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000),
            new Meal(100007, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
            //     new Meal(100008,LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак Админ", 500),
            //     new Meal(100009,LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед Админ", 1000),
            //     new Meal(100010,LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин Админ", 500)
    ).stream().sorted((a, b) -> (b.getDateTime().compareTo(a.getDateTime()))).collect(Collectors.toList());

    public static final List<Meal> MEALS_FOR_INMEMORY = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
            //     new Meal(100008,LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак Админ", 500),
            //     new Meal(100009,LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед Админ", 1000),
            //     new Meal(100010,LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин Админ", 500)
    ).stream().sorted((a, b) -> (b.getDateTime().compareTo(a.getDateTime()))).collect(Collectors.toList());

    public static List<Meal> generateMeals() {
        return new ArrayList<>(MEALS);
    }

    public static List<Meal> generateMealsForInMemory() {
        return new ArrayList<>(MEALS_FOR_INMEMORY);
    }

    public static final Meal generateNewMeal() {
        return new Meal(LocalDateTime.of(2015, Month.MAY, 10, 13, 0), "Обед", 10000);
    }

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>(
            ((expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()) &&
                            Objects.equals(expected.getCalories(), actual.getCalories())
                            && Objects.equals(expected.getDateTime(), actual.getDateTime())
                            && Objects.equals(expected.getDescription(), actual.getDescription())
                    ))

    );

    public static final BeanMatcher<MealWithExceed> MATCHER_EXCEEDED = new BeanMatcher<>(
            ((expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()) &&
                            Objects.equals(expected.getCalories(), actual.getCalories())
                            && Objects.equals(expected.getDateTime(), actual.getDateTime())
                            && Objects.equals(expected.getDescription(), actual.getDescription())
                            && Objects.equals(expected.isExceed(), actual.isExceed())
                    ))

    );
}
