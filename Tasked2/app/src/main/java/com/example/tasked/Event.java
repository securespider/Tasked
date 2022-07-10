package com.example.tasked;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event implements Comparable<Event> {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
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
     * @return
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
     * @return
     */
    public Map<String, Object> toMap() {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("name", this.name);
        mapEvent.put("description", this.description);
        mapEvent.put("date", this.eventDate.toString());
        mapEvent.put("startTime", this.startEventTime.toString());
        mapEvent.put("endTime", this.endEventTime.toString());
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

    private String name, description;
    private LocalDate eventDate;
    private LocalTime startEventTime, endEventTime;

    // Constructor for when parameters are in String instead (eg. During event retrieval from firebase)
    public Event(String name, String strEventDate, String strStartEventTime, String strEndEventTime, String description) {
        this(name, stringToLocalDate(strEventDate), stringToLocalTime(strStartEventTime), stringToLocalTime(strEndEventTime), description);
    }

    // Default constructor given appropriate classes
    public Event(String name, LocalDate eventDate, LocalTime startEventTime, LocalTime endEventTime, String description) {
        this.name = name;
        this.eventDate = eventDate;
        this.startEventTime = startEventTime;
        this.endEventTime = endEventTime;
        this.description = description;
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

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStartEventTime() {
        return startEventTime;
    }

    public void setStartEventTime(LocalTime startEventTime) {
        this.startEventTime = startEventTime;
    }

    public LocalTime getEndEventTime() {
        return endEventTime;
    }

    public void setEndEventTime(LocalTime endEventTime) {
        this.endEventTime = endEventTime;
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
