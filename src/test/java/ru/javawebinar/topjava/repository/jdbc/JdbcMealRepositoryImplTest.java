package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * Created by MSI on 30.07.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class JdbcMealRepositoryImplTest {
    private final int TRUE_USER = 100001;
    private final int FALSE_USER = 100000;
    @Autowired
    private JdbcMealRepositoryImpl repository;
    @Test
    public void save() throws Exception {
        Meal testMeal = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ_ланч", 510);
        //сохранение
        repository.save(testMeal,TRUE_USER);
        //обновление записи
        testMeal.setCalories(2000);
        repository.save(testMeal,TRUE_USER);
        //пытаемся обновить запись под другим пользователем
        repository.save(testMeal, FALSE_USER);
        //проверяем можем ли мы получить запись сохнаненную под другим пользователем
        Meal returned = repository.get(testMeal.getId(),FALSE_USER);
        Assert.assertEquals(null,returned);
        //олучаем сохраненнуб еду
        returned = repository.get(testMeal.getId(),TRUE_USER);
        Assert.assertEquals(testMeal.toString(),returned.toString());
        //удаляем и проверяем что действительно удалилась
        repository.delete(testMeal.getId(),TRUE_USER);
        returned = repository.get(testMeal.getId(),TRUE_USER);
        Assert.assertEquals(null,returned);
        for (Meal meal:repository.getAll(TRUE_USER)){
            System.out.println(meal);
        }
        for (Meal meal:repository.getAll(FALSE_USER)){
            System.out.println(meal);
        }
        for (Meal meal:repository.getBetween(
                LocalDateTime.of(2015,5,29,0,0,0),
                LocalDateTime.of(2015,5,31,0,0,0),FALSE_USER)){
            System.out.println(meal);
        }

    }


}