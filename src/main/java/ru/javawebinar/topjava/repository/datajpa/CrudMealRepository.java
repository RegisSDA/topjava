package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;


import java.time.LocalDateTime;
import java.util.List;


@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    Meal save(Meal Meal);

    @Transactional
    @Modifying
    @Query("UPDATE Meal SET dateTime=:dateTime, calories=:calories, description=:description, user.id=:user WHERE id=:id")
    int update(@Param("dateTime") LocalDateTime dateTime, @Param("calories") int calories, @Param("description") String description, @Param("user") int user, @Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal WHERE id=:id AND user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    Meal getById(int id);//

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int user);

    List<Meal> findAllByDateTimeBetweenAndUserIdOrderByDateTimeDesc(LocalDateTime startDate, LocalDateTime endDate, int user);

}
