package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {

    Meal save(Meal Meal,int userId);

    boolean delete(int id,int userId) throws NotFoundException;

    Meal get(int id, int userId) throws  NotFoundException;

    Collection<Meal> getAll(int userId, LocalDate start,LocalDate end);
}