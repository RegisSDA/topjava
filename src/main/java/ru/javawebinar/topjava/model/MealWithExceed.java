package ru.javawebinar.topjava.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MealWithExceed implements Serializable{
    private  LocalDateTime dateTime;

    private  String description;

    private  int calories;

    private  boolean exceed;

    private int id;


    public MealWithExceed(int id, LocalDateTime dateTime, String description, int calories, boolean exceed) {
        this.id=id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate(){return dateTime.toLocalDate();}
    public LocalTime getTime(){return dateTime.toLocalTime();}

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExceed() {
        return exceed;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserMealWithExceed{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", exceed=" + exceed +
                '}';
    }
}