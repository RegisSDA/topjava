package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository crudRepository;
    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(crudUserRepository.getOne(userId));
            crudRepository.save(meal);
        } else {
            Meal mealOld = crudRepository.findOne(meal.getId());
            if (mealOld.getUser().getId() == userId) {
                meal.setUser(mealOld.getUser());
                if (crudRepository.update(meal.getDateTime(), meal.getCalories(), meal.getDescription(), userId, meal.getId()) == 1) {
                    return meal;
                }
            }
            return null;
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal meal = crudRepository.findOne(id);
        if (meal != null && meal.getUser().getId() == userId) {
            crudRepository.delete(id, userId);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.getById(id);
        if (meal != null && meal.getUser().getId() == userId) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserOrderByDateTimeDesc(crudUserRepository.findOne(userId));
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findAllByDateTimeBetweenAndUserOrderByDateTimeDesc(startDate, endDate, crudUserRepository.findOne(userId));
    }
}
