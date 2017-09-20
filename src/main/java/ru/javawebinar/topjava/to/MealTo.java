package ru.javawebinar.topjava.to;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by MSI on 19.09.2017.
 */
public class MealTo extends BaseTo {

    @NotEmpty
    private String description;

    @NotNull
    @Min(10)
    @Max(5000)
    private Integer calories;

    private LocalDateTime dateTime;

    public MealTo() {
    }

    public MealTo(Integer id, String description, Integer calories, LocalDateTime dateTime) {
        super(id);
        this.description = description;
        this.calories = calories;
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
