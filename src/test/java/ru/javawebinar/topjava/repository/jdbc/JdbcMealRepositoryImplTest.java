package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by MSI on 30.07.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class JdbcMealRepositoryImplTest {

    @Autowired
    private JdbcMealRepositoryImpl repository;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp(){
        dbPopulator.execute();
    }

    @Test
    public void save() throws Exception {

        Meal testMeal = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "test_ланч", 510);
        testMeal= repository.save(testMeal, USER_ID);
        MATCHER.assertEquals(testMeal,repository.get(testMeal.getId(), USER_ID));
    }

    @Test
    public void testFalseUpdate() throws Exception {
        Meal meal = generateMeals().get(0);
        meal.setCalories(9999);
        Assert.assertEquals(null,repository.save(meal,ADMIN_ID));
    }

    @Test
    public void testDelete() {
        List<Meal> meals = MealTestData.generateMeals();
        repository.delete(meals.remove(0).getId(), USER_ID);
        MATCHER.assertCollectionEquals(meals,repository.getAll(USER_ID));
    }
    @Test
    public void testFalseDelete() {
        List<Meal> meals = MealTestData.generateMeals();
        Assert.assertEquals(false,repository.delete(meals.remove(0).getId(), ADMIN_ID));
    }

    @Test
    public void testGet() {
        Meal meal = MEALS.get(0);
        MATCHER.assertEquals(meal,repository.get(meal.getId(), USER_ID));
    }

    @Test
    public void testFalseGet() {
        Meal meal = MEALS.get(0);
        Assert.assertEquals(null,repository.get(meal.getId(), ADMIN_ID));
    }

    @Test
    public void testGetAll() {
        MATCHER.assertCollectionEquals(MEALS,repository.getAll(USER_ID));
    }

    @Test
    public void testGetBetween() {
        List<Meal> meals = generateMeals();
        meals.remove(0);
        meals.remove(0);
        meals.remove(0);
        MATCHER.assertCollectionEquals(meals,repository.getBetween(
                LocalDateTime.of(2015,5,29,0,0,0),
                LocalDateTime.of(2015,5,30,1,0,0), USER_ID));
    }

}