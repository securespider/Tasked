package com.example.tasked;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getEventDate().equals(date))
                events.add(event);
        }

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
     * Utility function to convert EventList into map for easy addition into
     *
     * Child will be
     * @return
     */
    public static Map<String, Map<String, String>> eventListToMap() {
        Map<String, Map<String, String>> result = new HashMap<>();
        for (Event event: Event.eventsList) {
            result.put(Integer.toString(event.hashCode()), event.eventToMap());
        }
        return result;
    }


    /**
     * Utility function to convert individual events to map.
     *
     * @return
     */
    public Map<String, String> eventToMap() {
        Map<String, String> mapEvent = new HashMap<>();
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


    private String name, description;
    private LocalDate eventDate;
    private LocalTime startEventTime, endEventTime;

    public Event(String name, LocalDate eventDate, LocalTime startEventTime, LocalTime endEventTime, String description) {
        this.name = name;
        this.eventDate = eventDate;
        this.startEventTime = startEventTime;
        this.endEventTime = endEventTime;
        this.description = description;
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
}
