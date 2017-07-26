package ru.javawebinar.topjava.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public static <T extends Comparable<T>> boolean isBetween(T lt, T start, T end) {
        return lt.compareTo(start) >= 0 && lt.compareTo(end) <= 0;
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }

    public static DateTimeFormatter getDateFormatter() {
        return DATE_FORMATTER;
    }

    public static DateTimeFormatter getTimeFormatter() {
        return TIME_FORMATTER;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
