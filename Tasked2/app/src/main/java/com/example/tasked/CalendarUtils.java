package com.example.tasked;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils
{
    public static LocalDate selectedDate;
    public static final String PATH = "https://tasked-44a12-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String formattedShortTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static String monthDayFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray()
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate prevMonth = selectedDate.minusMonths(1);
        LocalDate nextMonth = selectedDate.plusMonths(1);

        YearMonth prevYearMonth = YearMonth.from(prevMonth);
        int prevDaysInMonth = prevYearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek)
                daysInMonthArray.add(LocalDate.of(prevMonth.getYear(),prevMonth.getMonth(),prevDaysInMonth + i - dayOfWeek));
            else if(i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(LocalDate.of(nextMonth.getYear(),nextMonth.getMonth(),i - dayOfWeek - daysInMonth));
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
        return  daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }

    public static int[] selectedDateInArray(LocalDate selectedDate) {
        return new int[] {selectedDate.getYear(), selectedDate.getMonthValue(), selectedDate.getDayOfMonth()};
    }

    public static void selectDateDialog(Context instance, Runnable runnable) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            CalendarUtils.selectedDate = LocalDate.of(year, ++month, dayOfMonth);
            runnable.run();
        };

        int[] selectedDate = CalendarUtils.selectedDateInArray(CalendarUtils.selectedDate);
        new DatePickerDialog(
            instance,
            AlertDialog.THEME_HOLO_LIGHT,
            dateSetListener,
            selectedDate[0],
            selectedDate[1] - 1,
            selectedDate[2]).show();
    }



//    public static Calendar localDateTimeToCalendar(LocalDate date, LocalTime time) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//        calendar.set(
//                date.getYear(),
//                date.getMonthValue() - 1,
//                date.getDayOfMonth(),
//                time.getHour(),
//                time.getMinute(),
//                time.getSecond()
//        );
//        return calendar;
//    }




}
