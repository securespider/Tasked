package com.example.tasked;

import java.time.LocalTime;
import java.util.ArrayList;

class  HourEvent
{
    LocalTime time;
    ArrayList<Event> events;

    public HourEvent(LocalTime time, ArrayList<Event> events)
    {
        this.time = time;
        this.events = events;
    }

}
