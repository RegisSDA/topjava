package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

/**
 * Created by MSI on 19.09.2017.
 */
public class MealToUtil {

    public static Meal createNewMeal(MealTo mealTo) {
        return new Meal(null, mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
    }

    public static Meal updateMeal(MealTo mealTo) {
        return new Meal(mealTo.getId(), mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
    }

    public static MealTo asTo(Meal meal) {
        return new MealTo(meal.getId(), meal.getDescription(), meal.getCalories(), meal.getDateTime());
    }
}
