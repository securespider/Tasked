package com.example.tasked;

import android.content.Context;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event implements Comparable<Event> {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    /**
     * Returns a boolean value if there is an event on this date.
     *
     * Used in Calendars to show busy dates
     *
     * @param date Date to check
     * @return Boolean value if there is an event on this date.
     */
    public static boolean isEventOnDate(LocalDate date) {
        for (Event event : eventsList) {
            if (event.getEventDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a list of events happening on that date
     *
     * Used for Weekly view calendar
     *
     * @param date Date to check
     * @return list of events happening on the date
     */
    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList) {
            if(event.getEventDate().equals(date))
                events.add(event);
        }
        Collections.sort(events);
        return events;
    }

    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            int cellHour = time.getHour();
            int startHour = event.startEventTime.getHour();
            int endHour = event.endEventTime.getHour();
            // time is between start and end time of 
            if(event.getEventDate().equals(date) && startHour <= cellHour && endHour > cellHour)
                events.add(event);
        }

        return events;
    }

    /**
     * Utility function to convert EventList into map for easy addition into database
     *
     */
    public static Map<String, Map<String, Object>> listToMap() {
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Event event: Event.eventsList) {
            result.put(Integer.toString(event.hashCode()), event.toMap());
        }
        return result;
    }


    /**
     * Utility function to convert individual events to map.
     *
     */
    public Map<String, Object> toMap() {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("name", this.name);
        mapEvent.put("description", this.description);
        mapEvent.put("date", this.eventDate.toString());
        mapEvent.put("startTime", this.startEventTime.toString());
        mapEvent.put("endTime", this.endEventTime.toString());
        mapEvent.put("notif", Boolean.toString(this.notif));
        return mapEvent;
    }

//    /**
//     * Util function that is used to convert map back to ArrayList for events.
//     *
//     * @param event Event that is being converted to JSONObject
//     * @return The JSONObject
//     * @throws JSONException
//     */
//    public static ArrayList<Event> mapToEventList() {
//        JSONObject result = new JSONObject();
//        result.putOpt("name", event.name);
//        result.putOpt("description", event.description);
//        result.putOpt("date", event.eventDate);
//        result.putOpt("startTime", event.startEventTime);
//        result.putOpt("endTime", event.endEventTime);
//        return result;
//    }

    // Used to pass Events to be modified across activities
    public static boolean isModify = false;
    public static Event modifyEvent;

    private boolean notif;
    private final String name, description;
    private final LocalDate eventDate;
    private final LocalTime startEventTime, endEventTime;

    // Constructor for when parameters are in String instead (eg. During event retrieval from firebase)
    public Event(String name, String strEventDate, String strStartEventTime, String strEndEventTime, String description, String notif) {
        this(name, stringToLocalDate(strEventDate), stringToLocalTime(strStartEventTime), stringToLocalTime(strEndEventTime), description, notif.equals("true"));
    }

    // Default constructor given appropriate classes
    public Event(String name, LocalDate eventDate, LocalTime startEventTime, LocalTime endEventTime, String description, boolean notif) {
        this.name = name;
        this.eventDate = eventDate;
        this.startEventTime = startEventTime;
        this.endEventTime = endEventTime;
        this.description = description;
        this.notif = notif;
    }

    private static LocalDate stringToLocalDate(String strEventDate) {
        String[] strDates = strEventDate.split("-");
        int[] intDates = new int[3];

        for (int counter = 0; counter < 3; counter++) {
            int intDate = Integer.parseInt(strDates[counter]);
            intDates[counter] = intDate;
        }
        return LocalDate.of(intDates[0], intDates[1], intDates[2]);
    }

    private static LocalTime stringToLocalTime(String strLocalTime) {
        String[] strTime = strLocalTime.split(":");
        int[] intTimes = new int[2];

        for (int counter = 0; counter < 2; counter++) {
            int intTime = Integer.parseInt(strTime[counter]);
            intTimes[counter] = intTime;
        }
        return LocalTime.of(intTimes[0], intTimes[1]);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }


    public LocalTime getStartEventTime() {
        return startEventTime;
    }


    public LocalTime getEndEventTime() {
        return endEventTime;
    }

    public boolean isNotif() {
        return notif;
    }

    public void setReminder(Context context, boolean isDelete) {
        if (this.notif) {
            NotificationUtils notif = new NotificationUtils(context);
            long notifTime = CalendarUtils.localDateTimeToCalendar(this.eventDate, this.startEventTime)
                    .getTimeInMillis();
            notif.setReminder(notifTime, this.name, "Your event is starting soon!", this.hashCode(), isDelete);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(eventDate, event.eventDate) && Objects.equals(startEventTime, event.startEventTime) && Objects.equals(endEventTime, event.endEventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventDate, startEventTime, endEventTime);
    }

    @Override
    public int compareTo(Event o) {
        return this.startEventTime.compareTo(o.startEventTime);
    }
}
