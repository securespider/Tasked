package com.example.tasked;

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
            int eventHour = event.startEventTime.getHour();
            int cellHour = time.getHour();
            int startHour = event.startEventTime.getHour();
            int endHour = event.endEventTime.getHour();
            // time is between start and end time of 
            if(event.getEventDate().equals(date) && startHour <= cellHour && endHour > cellHour)
                events.add(event);
        }

        return events;
    }


    private String name;
    private LocalDate eventDate;
    private LocalTime startEventTime, endEventTime;

    public Event(String name, LocalDate eventDate, LocalTime startEventTime, LocalTime endEventTime) {
        this.name = name;
        this.eventDate = eventDate;
        this.startEventTime = startEventTime;
        this.endEventTime = endEventTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
