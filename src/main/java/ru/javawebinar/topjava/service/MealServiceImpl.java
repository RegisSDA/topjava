package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public class MealServiceImpl implements MealService {

    private static final Logger log = getLogger(MealServiceImpl.class);
    @Autowired
    private MealRepository repository;

    public void setRepository(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int userId){
        Meal mealNew = repository.save(meal,userId);
        ValidationUtil.checkNotFound(mealNew,mealNew.toString());
        return meal;
    }

    public boolean delete(int id,int userId) throws NotFoundException {
        ValidationUtil.checkNotFoundWithId(repository.delete(id,userId),id);
        return true;
    }

    public Meal get(int id, int userId) throws NotFoundException {
        Meal meal = repository.get(id,userId);
        ValidationUtil.checkNotFoundWithId (meal,id);
        return meal;
    }

    public Collection<Meal> getAll(int userId, LocalDate start,LocalDate end){
        Collection<Meal> meals = repository.getAll(userId,start,end);
        log.info("getAll result :"+meals);
  //      Iterator<Meal> it = meals.iterator();
  //      log.info(""+it.hasNext());
        return meals;
    }
}