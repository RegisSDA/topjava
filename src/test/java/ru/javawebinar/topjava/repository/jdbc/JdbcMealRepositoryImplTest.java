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
    public void setUp() {
        dbPopulator.execute();
    }

    @Test
    public void save() throws Exception {

        Meal testMeal = generateNewMeal();
        repository.save(testMeal, USER_ID);
        MATCHER.assertEquals(testMeal, repository.get(testMeal.getId(), USER_ID));
    }

    @Test
    public void testFalseUpdate() throws Exception {
        Meal meal = repository.save(generateNewMeal(), USER_ID);
        Assert.assertEquals(null, repository.save(meal, ADMIN_ID));
    }

    @Test
    public void testDelete() {
        repository.delete(MEAL_1.getId(), USER_ID);
        MATCHER.assertCollectionEquals(generateMealsList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2), repository.getAll(USER_ID));
    }

    @Test
    public void testFalseDelete() {
        Assert.assertEquals(false, repository.delete(MEAL_6.getId(), ADMIN_ID));
    }

    @Test
    public void testGet() {
        MATCHER.assertEquals(MEAL_1, repository.get(MEAL_1.getId(), USER_ID));
    }

    @Test
    public void testFalseGet() {
        Assert.assertEquals(null, repository.get(MEAL_1.getId(), ADMIN_ID));
    }

    @Test
    public void testGetAll() {
        MATCHER.assertCollectionEquals(generateMealsList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1), repository.getAll(USER_ID));
    }

    @Test
    public void testGetBetween() {
        MATCHER.assertCollectionEquals(generateMealsList(MEAL_3, MEAL_2, MEAL_1), repository.getBetween(
                LocalDateTime.of(2015, 5, 29, 0, 0, 0),
                LocalDateTime.of(2015, 5, 30, 23, 0, 0), USER_ID));
    }

}