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
            if(event.getDate().equals(date))
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
            if(event.getDate().equals(date) && eventHour == cellHour)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return eventDate;
    }

    public void setDate(LocalDate date)
    {
        this.eventDate = date;
    }

    public LocalTime getTime()
    {
        return startEventTime;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }
}
