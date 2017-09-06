package ru.javawebinar.topjava.web.meal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by MSI on 06.09.2017.
 */
public class MealRestControllerTest extends AbstractControllerTest {

    private final String REST_URL = MealRestController.REST_MAPPING + "/";

    @Autowired
    private MealService mealService;

    @Override
    @Before
    public void setUp() {
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertListEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getWithExceeded(mealService.getAll(USER_ID), USER.getCaloriesPerDay())));
    }

    @Test
    public void testCreateMeal() throws Exception {
        Meal created = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))).andExpect(status().isCreated());

        Meal returned = MATCHER.fromJsonAction(action);
        created.setId(returned.getId());

        MATCHER.assertEquals(created, returned);
        MATCHER.assertListEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                //              .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertEquals(updated, mealService.get(updated.getId(), USER_ID));
    }


    @Test
    public void getBetween() throws Exception {
        mockMvc.perform(get(REST_URL + "filtered")
                .param("startDate", "2015-05-15")
                .param("startTime", "09:00:00")
                // .param("endDate","2015-05-30")
                .param("endTime", "23:00:00"))//"2015-05-15T09:00:00/2015-05-30T23:00:00"
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getFilteredWithExceeded(
                        mealService.getBetweenDates(LocalDate.of(2015, 5, 15), DateTimeUtil.MAX_DATE, USER_ID),
                        LocalTime.of(9, 0, 0, 0),
                        LocalTime.of(23, 0, 0, 0),
                        USER.getCaloriesPerDay())));

    }

//    @Test
//    public void getBetween() throws Exception {
//        mockMvc.perform(get(REST_URL +"filtered")
//                .param("startDateTime","2015-05-15T09:00:00")
//                .param("endDateTime","2015-05-30T23:00:00"))//"2015-05-15T09:00:00/2015-05-30T23:00:00"
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getFilteredWithExceeded(
//                        mealService.getBetweenDates(LocalDate.of(2015, 5, 15), LocalDate.of(2015, 5, 30), USER_ID),
//                        LocalTime.of(9, 0, 0, 0),
//                        LocalTime.of(23, 0, 0, 0),
//                        USER.getCaloriesPerDay())));
//
//    }

}