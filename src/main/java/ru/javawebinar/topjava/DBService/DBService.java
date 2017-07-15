package ru.javawebinar.topjava.DBService;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by MSI on 15.07.2017.
 */
public interface DBService {

    void save(Meal meal);
    void delete(Meal meal);
    void update(Meal meal);
    List<Meal> getMealsByUser(String login);

}
