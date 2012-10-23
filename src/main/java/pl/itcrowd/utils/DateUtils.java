package pl.itcrowd.utils;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
// -------------------------- STATIC METHODS --------------------------

    public static Date dayEnd(Date date)
    {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.add(Calendar.DATE, 1);
        instance.add(Calendar.MILLISECOND, -1);
        return instance.getTime();
    }

    public static String dayOfWeekName(Date date)
    {
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] daysOfWeek = symbols.getShortWeekdays();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return daysOfWeek[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public static Date dayStart(Date date)
    {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        date = instance.getTime();
        return date;
    }

    public static List<String> daysOfWeek()
    {
        DateFormatSymbols symbols = new DateFormatSymbols();
        return Arrays.asList(symbols.getShortWeekdays());
    }

    public static Date hourStart(Date date)
    {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        date = instance.getTime();
        return date;
    }

    public static Date monthEnd(Date date)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthStart(date));
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date monthStart(Date date)
    {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.DATE, 1);
        date = instance.getTime();
        return date;
    }

    public static Date nextHourStart(Date date)
    {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.HOUR_OF_DAY, 1);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        date = instance.getTime();
        return date;
    }

    public static Date now()
    {
        return new Date();
    }
}
