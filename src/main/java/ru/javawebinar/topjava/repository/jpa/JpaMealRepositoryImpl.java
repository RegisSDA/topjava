package ru.javawebinar.topjava.repository.jpa;


import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            meal.setUser(entityManager.getReference(User.class, userId));
            entityManager.persist(meal);
        } else {
            if (entityManager.createNamedQuery(Meal.UPDATE)
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("description", meal.getDescription())
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .executeUpdate() == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {

        return entityManager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() == 1;
    }

    @Override
    public Meal get(int id, int userId) {

 /*
        Meal meal = entityManager.find(Meal.class,id);
        if (meal.getUser().getId()==userId){
            return meal;
        }else{
            return null;
        }
*/
        List<Meal> result = entityManager.createNamedQuery("getQuery", Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId).getResultList();
        return DataAccessUtils.singleResult(result);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery("getAll", Meal.class)
                .setParameter("user_id", userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return entityManager.createNamedQuery(Meal.GETBETWEEN, Meal.class)
                .setParameter("user_id", userId)
                .setParameter("startdate", startDate)
                .setParameter("enddate", endDate)
                .getResultList();
    }
}