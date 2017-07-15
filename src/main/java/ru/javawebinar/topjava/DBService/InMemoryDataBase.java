package ru.javawebinar.topjava.DBService;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by MSI on 15.07.2017.
 */
public class InMemoryDataBase implements DBService {

    private ConcurrentMap<Integer,Meal> dataBase = new ConcurrentHashMap<>();
    public InMemoryDataBase() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        for (Meal meal : meals) {
            dataBase.put(meal.getId(), meal);
        }
    }
    @Override
    public void save(Meal meal) {

        dataBase.putIfAbsent(meal.getId(),meal);
    }

    @Override
    public void delete(Meal meal) {
        dataBase.remove(meal.getId());
    }

    @Override
    public void update(Meal meal) {
        dataBase.put(meal.getId(),meal);
    }

    @Override
    public List<Meal> getMealsByUser(String login) {
         return dataBase.entrySet().stream().map(entry->entry.getValue()).collect(Collectors.toList());
    }
}
