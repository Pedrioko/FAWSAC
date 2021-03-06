package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.lang.Time;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The Class FechaUtil.
 */
public class FechaUtil {

    /**
     * The Constant YEAR.
     */
    private static final int YEAR = Calendar.YEAR;

    /**
     * The Constant MONTH.
     */
    private static final int MONTH = Calendar.MONTH;

    /**
     * The Constant DATE.
     */
    private static final int DATE = Calendar.DATE;

    /**
     * Gets the calendar.
     *
     * @param date the date
     * @return the calendar
     */
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    /**
     * Years between.
     *
     * @param first the first
     * @param last  the last
     * @return the long
     */
    public static long yearsBetween(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) || a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE)) {
            diff--;
        }
        return diff;
    }

    /**
     * Time between.
     *
     * @param first the first
     * @param last  the last
     * @return the time
     */
    public static Time timeBetween(Date first, Date last) {
        long diff = last.getTime() - first.getTime();
        Time time = new Time();
        time.setSeconds(diff / 1000 % 60);
        time.setMinutes(diff / (60 * 1000) % 60);
        time.setHours(diff / (60 * 60 * 1000) % 24);
        time.setDays(diff / (24 * 60 * 60 * 1000));
        return time;
    }

    public static String formatSeconds(Object seconds) {
        Float aFloat = Float.parseFloat(seconds.toString());
        return format(Duration.ofSeconds(aFloat.longValue()));
    }

    protected static String format(Duration duration) {
        long hours = duration.toHours();
        long mins = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(mins).getSeconds();
        return String.format("%02d:%02d:%02d", hours, mins, seconds);
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = getSimpleDateFormat(pattern);
        return format.format(date);
    }

    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

}