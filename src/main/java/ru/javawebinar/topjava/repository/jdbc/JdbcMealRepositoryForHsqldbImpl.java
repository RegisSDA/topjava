package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;


import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by MSI on 16.08.2017.
 */
@Repository
@Profile("hsqldb")
public class JdbcMealRepositoryForHsqldbImpl extends JdbcMealRepositoryImpl implements MealRepository {
    @Autowired
    public JdbcMealRepositoryForHsqldbImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(dataSource, jdbcTemplate, namedParameterJdbcTemplate);
    }

    private static Logger log = LoggerFactory.getLogger(JdbcMealRepositoryForHsqldbImpl.class);
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", convertLocalDateTimeToTimeStamp(meal.getDateTime()))
                .addValue("user_id", userId);
        if (meal.isNew()) {
            Number newId = insertMeal.executeAndReturnKey(map);
            meal.setId(newId.intValue());
        } else {
            if (jdbcTemplate.update("" +
                            "UPDATE meals " +
                            "   SET description=?, calories=?, date_time=?" +
                            " WHERE id=? AND user_id=?"
                    , meal.getDescription(), meal.getCalories(), convertLocalDateTimeToTimeStamp(meal.getDateTime()), meal.getId(), userId) == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("JdbcMealRepositoryForHsqldbImpl");
        return super.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("JdbcMealRepositoryForHsqldbImpl");
        return super.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("JdbcMealRepositoryForHsqldbImpl");
        return super.getAll(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, convertLocalDateTimeToTimeStamp(startDate), convertLocalDateTimeToTimeStamp(endDate));
    }

    private String convertLocalDateTimeToTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }
}
