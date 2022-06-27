package com.example.tasked;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
     * Util function that is used to convert event objects into JSONObject for easy storage and transfer.
     *
     * @param event Event that is being converted to JSONObject
     * @return The JSONObject
     * @throws JSONException
     */
    public static JSONObject eventToJSON(Event event) throws JSONException {
        JSONObject result = new JSONObject();
        result.putOpt("name", event.name);
        result.putOpt("description", event.description);
        result.putOpt("date", event.eventDate);
        result.putOpt("startTime", event.startEventTime);
        result.putOpt("endTime", event.endEventTime);
        return result;
    }


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
}
