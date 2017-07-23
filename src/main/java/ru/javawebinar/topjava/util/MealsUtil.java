package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class MealsUtil {
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500, AuthorizedUser.id()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000, AuthorizedUser.id()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500, AuthorizedUser.id()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000, AuthorizedUser.id()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500, AuthorizedUser.id()),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510, AuthorizedUser.id())
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static void main(String[] args) {
        List<MealWithExceed> mealsWithExceeded = getFilteredWithExceeded(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0),null,null, 2000);
        mealsWithExceeded.forEach(System.out::println);

        System.out.println(getFilteredWithExceededByCycle(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0),null,null, DEFAULT_CALORIES_PER_DAY));
    }

    public static List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX,null,null, caloriesPerDay);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, LocalTime startTime, LocalTime endTime,LocalDate startDate, LocalDate endDate, int caloriesPerDay) {
        final LocalTime fStartTime = startTime==null?LocalTime.MIN:startTime;
        final LocalTime fEndTime = endTime==null?LocalTime.MAX:endTime;

        final LocalDate fStartDate = startDate==null?LocalDate.MIN:startDate;
        final LocalDate fEndDate = endDate==null?LocalDate.MAX:endDate;

        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .filter(a->DateTimeUtil.isBetweenByDate(a.getDate(),fStartDate,fEndDate))
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(a->DateTimeUtil.isBetweenByDate(a.getDate(),fStartDate,fEndDate))
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), fStartTime, fEndTime))
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .sorted((a,b)->(-1*a.getDateTime().compareTo(b.getDateTime())))
                .collect(Collectors.toList());
    }

    public static List<MealWithExceed> getFilteredWithExceededByCycle(List<Meal> meals, LocalTime startTime, LocalTime endTime,LocalDate startDate, LocalDate endDate, int caloriesPerDay) {
        final LocalTime fStartTime = startTime==null?LocalTime.MIN:startTime;
        final LocalTime fEndTime = endTime==null?LocalTime.MAX:endTime;

        final LocalDate fStartDate = startDate==null?LocalDate.MIN:startDate;
        final LocalDate fEndDate = endDate==null?LocalDate.MAX:endDate;

        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.stream().filter(a->DateTimeUtil.isBetweenByDate(a.getDate(),fStartDate,fEndDate)).forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealWithExceed> mealsWithExceeded = new ArrayList<>();
        meals.forEach(meal -> {
            if (DateTimeUtil.isBetween(meal.getTime(), fStartTime, fEndTime)&&(DateTimeUtil.isBetweenByDate(meal.getDate(),fStartDate,fEndDate))) {
                mealsWithExceeded.add(createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return mealsWithExceeded;
    }

    /*
     *  Advanced solution in one return (listDayMeals can be inline).
     *  Streams are not multiplied, so complexity is still O(N)
     *  Execution time is increased as for every day we create 2 additional streams
     */
    public static List<MealWithExceed> getFilteredWithExceededInOneReturn(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Collection<List<Meal>> listDayMeals = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate)).values();

        return listDayMeals
                .stream().map(dayMeals -> {
                    boolean exceed = dayMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
                    return dayMeals.stream().filter(meal ->
                            DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                            .map(meal -> createWithExceed(meal, exceed));
                }).flatMap(identity())
                .collect(Collectors.toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}