package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by MSI on 31.07.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceImplTest {


    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        MATCHER.assertEquals(MEAL_1, service.get(MEAL_1.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testGetByOtherUser() throws Exception {
        MATCHER.assertEquals(MEAL_1, service.get(MEAL_1.getId(), ADMIN_ID));
    }


    @Test
    public void testDelete() throws Exception {
        service.delete(MEAL_1.getId(), USER_ID);
        MATCHER.assertCollectionEquals(generateMealsList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteByOtherUser() throws Exception {
        service.delete(MEAL_1.getId(), ADMIN_ID);

    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        MATCHER.assertCollectionEquals(generateMealsList(MEAL_3, MEAL_2, MEAL_1), service.getBetweenDateTimes(LocalDateTime.MIN, LocalDateTime.of(2015, 5, 30, 22, 0), USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(generateMealsList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1), service.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal meal = service.save(generateNewMeal(), USER_ID);
        meal.setCalories(3333);
        service.update(meal, USER_ID);
        MATCHER.assertEquals(meal, service.get(meal.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateByOtherUser() throws Exception {
        service.update(MEAL_1, ADMIN_ID);
    }

    @Test
    public void testSave() throws Exception {
        Meal mealOrigin = generateNewMeal();
        Meal mealAfterSave = service.save(mealOrigin, USER_ID);
        mealOrigin.setId(mealAfterSave.getId());
        MATCHER.assertEquals(mealOrigin, service.get(mealOrigin.getId(), USER_ID));
    }

}