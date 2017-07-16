package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by MSI on 15.07.2017.
 */
public interface MealDAO {

    void save(Meal meal);
    void delete(int id);
    void update(Meal meal,int id);
    List<Meal> getMealsByUser(String login);

}
