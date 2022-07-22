package com.example.tasked;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class CalendarUtils {
    public static LocalDate selectedDate;
    public static FirebaseDatabase DB;


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



    public static Calendar localDateTimeToCalendar(LocalDate date, LocalTime time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(
                date.getYear(),
                date.getMonthValue() - 1,
                date.getDayOfMonth(),
                time.getHour(),
                time.getMinute(),
                time.getSecond()
        );
        return calendar;
    }

    // NUSMODS compatibility - naming scheme as per schema
    public static ArrayList<Event> gettingTimetable(String nusmodsUrl, LocalDate ayStart, int semester) {
        ArrayList<Event> result = new ArrayList<>();
        String year = "2022-2023";
        String APICALL = "https://api.nusmods.com/v2/" + year + "/modules/";
        HashMap<String, HashMap<String, String>> modules = modulesFromTimetable(nusmodsUrl);


        for (Map.Entry<String, HashMap<String, String>> entry: modules.entrySet()) {
            try {
                URL url = new URL(APICALL + entry.getKey() + ".json");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                if (connection.getResponseCode() == 404) {
                    continue;
                }

                // object is returned and processed into a JSONObject
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject moduleInfo = new JSONObject(response.toString());

                // Reading and interpreting json data
                HashMap<String, String> module = entry.getValue();
                JSONArray semesterData = moduleInfo.getJSONArray("semesterData");
                for (int i = 0; i < semesterData.length(); i++) {
                    JSONObject SemesterData = semesterData.getJSONObject(i); // semester data
                    if (SemesterData.getInt("semester") != semester) {
                        continue;
                    }
                    JSONArray timetable = SemesterData.optJSONArray("timetable");
                    if (timetable == null) {
                        continue;
                    }
                    for (int j = 0; j < timetable.length() ; j++) {
                        JSONObject Lesson = timetable.getJSONObject(j);
                        String classNo = Lesson.getString("classNo");
                        String lessonType = Lesson.getString("lessonType");
                        if (module.get(lessonType) != null && module.get(lessonType).equals(classNo)) {
                            String startTime = Lesson.getString("startTime");
                            String endTime = Lesson.getString("endTime");
                            String day = Lesson.optString("day");
                            if (day == null) {
                                day = LocalDate.now().toString();
                            }
                            // TODO: recursive event for weeks of lessons
                            String name = entry.getKey() + " " + lessonType;
                            String description = Lesson.getString("venue");
                            Object weeks = Lesson.get("weeks");
                            List<Integer> weeksList = new ArrayList<>();
                            if (weeks instanceof JSONObject) {
                                weeks =  ((JSONObject) weeks).optJSONArray("weeks");
                            }
                            if (weeks instanceof JSONArray) {
                                for (int counter = 0; counter < ((JSONArray) weeks).length(); counter++) {
                                    weeksList.add(((JSONArray) weeks).getInt(counter));
                                }
                            }
                            LocalDate startDate = nearestDayOfWeek(ayStart, day);
                            for (Integer x: weeksList) {
                                result.add(new Event(name,
                                        startDate.plusWeeks(x-1).toString(),
                                        startTime,
                                        endTime,
                                        description,
                                        "false",
                                        "red"));
                            }
                        }
                    }
                }
                connection.disconnect();

            } catch (MalformedURLException malformedURLException) {
                // TODO: handle invalid urls
            } catch (IOException ioException) {
                // TODO: handle no internet connection
            } catch (JSONException jsonException) {
                // TODO
            }
        }
        return result;
    }

    private static LocalDate nearestDayOfWeek(LocalDate date, String strDay) {
        DayOfWeek day = DayOfWeek.MONDAY;
        switch (strDay) { // Monday omitted as it is already assigned to monday by default
            case "Tuesday":
                day = DayOfWeek.TUESDAY;
                break;
            case "Wednesday":
                day = DayOfWeek.WEDNESDAY;
                break;
            case "Thursday":
                day = DayOfWeek.THURSDAY;
                break;
            case "Friday":
                day = DayOfWeek.FRIDAY;
                break;
            case "Saturday":
                day = DayOfWeek.SATURDAY;
                break;
            case "Sunday":
                day = DayOfWeek.SUNDAY;
                break;
        }
        while (date.getDayOfWeek() != day) {
            date = date.plusDays(1);
        }
        return date;
    }

    private static HashMap<String, HashMap<String, String>> modulesFromTimetable(String truncUrl) {
        HashMap<String, HashMap<String, String>> result = new HashMap<>();
        Pattern startingRegex = Pattern.compile("(?<=share\\?).*");
        Matcher startingMatch = startingRegex.matcher(truncUrl);
        if (!startingMatch.find()) {
            return result;
        }
        String starting = startingMatch.group();
        String[] modulesDetails  = starting.split("&");
        for (String moduleDetail: modulesDetails) {
            HashMap<String, String> lessons = new HashMap<>();
            String[] details =  moduleDetail.split("=");
            String moduleName = details[0];
            if (details.length == 1) { // module does not have any lessons
                continue;
            }
            String[] classes = details[1].split(",");
            for (String lesson: classes) {
                String[] lessonDetail = lesson.split(":");
                switch (lessonDetail[0]) {
                    case "REC":
                        lessonDetail[0] = "Recitation";
                        break;
                    case "LAB":
                        lessonDetail[0] = "Laboratory";
                        break;
                    case "TUT":
                        lessonDetail[0] = "Tutorial";
                        break;
                    case "LEC":
                        lessonDetail[0] = "Lecture";
                        break;
                    default:
                        lessonDetail[0] = "Extra";
                }

                lessons.put(lessonDetail[0], lessonDetail[1]);
            }
            result.put(moduleName, lessons);
        }
        return result;
    }

    public static LocalDate stringToLocalDate(String strEventDate) {
        String[] strDates = strEventDate.split("-");
        int[] intDates = new int[3];

        for (int counter = 0; counter < 3; counter++) {
            int intDate = Integer.parseInt(strDates[counter]);
            intDates[counter] = intDate;
        }
        return LocalDate.of(intDates[0], intDates[1], intDates[2]);
    }

    public static LocalTime stringToLocalTime(String strLocalTime) {
        String[] strTime;
        if (strLocalTime.length() == 4) { // time in format HHMM
            strTime = new String[] {
                strLocalTime.substring(0,2),
                strLocalTime.substring(2,4)
            };
        } else { // time in format HH:MM
            strTime = strLocalTime.split(":");
        }
        int[] intTimes = new int[2];

        for (int counter = 0; counter < 2; counter++) {
            int intTime = Integer.parseInt(strTime[counter]);
            intTimes[counter] = intTime;
        }
        return LocalTime.of(intTimes[0], intTimes[1]);
    }
}
