package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.DuplcateMealException;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insert;

    @Autowired
    public JdbcMealRepositoryImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
        this.insert = new SimpleJdbcInsert(template);
        insert.setTableName("meals");
        insert.usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        //сохранение новой еды
        if (meal.getId() == null) {
            MapSqlParameterSource map = new MapSqlParameterSource();
            map
                    .addValue("id", meal.getId())
                    .addValue("description", meal.getDescription())
                    .addValue("dateTime", meal.getDateTime())
                    .addValue("calories", meal.getCalories())
                    .addValue("user_id", userId);
            try {
                Number key = insert.executeAndReturnKey(map);
                meal.setId(key.intValue());
            } catch (DuplicateKeyException e) {
                throw new DuplcateMealException("Two meals at same time.");
            }
        } else {//обновление
            int res;
            try {
                res = jdbcTemplate.update("UPDATE meals SET description=?, date_time=?, calories=? WHERE id=? AND user_id=?",
                        meal.getDescription(), meal.getDateTime(), meal.getCalories(), meal.getId(), userId);
            } catch (DuplicateKeyException e) {
                throw new DuplcateMealException("Two meals at same time.");
            }
            //если строка не обновлена (нет еды с таким id+user_id)
            if (res == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) == 1;
    }

    @Override
    public Meal get(int id, int userId) {

        List<Meal> results = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id =?", ROW_MAPPER, id, userId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE  user_id =?  ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {

        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().plusDays(1).atStartOfDay();

        return jdbcTemplate.query("SELECT * FROM meals WHERE  user_id =? AND date_time>=? AND date_time<=? ORDER BY date_time DESC", ROW_MAPPER, userId, startDate, endDate);
    }
}
