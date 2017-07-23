package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;

@Service
public class MealServiceImpl implements MealService {
    @Autowired
    private MealRepository repository;

    public void setRepository(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int userId){
        return repository.save(meal, userId);
    }

    public boolean delete(int id,int userId) throws NotFoundException {
        return repository.delete(id,userId);
    }

    public Meal get(int id, int userId) throws NotFoundException {
        return repository.get(id,userId);
    }

    public Collection<Meal> getAll(int userId){
        return repository.getAll(userId);
    }
}