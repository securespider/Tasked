package com.example.tasked;

import static com.example.tasked.CalendarUtils.stringToLocalDate;
import static com.example.tasked.CalendarUtils.stringToLocalTime;

import android.content.Context;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Event implements Comparable<Event> {
    public static List<Event> eventsList = new ArrayList<>();

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

    /**
     * Return the set of event colors happening on that date
     *
     * Used for Weekly view calendar
     *
     * @param date Date to check
     * @return Set of event colors happening on the date
     */
    public static Set<String> eventCatForDate(LocalDate date)
    {
        Set<String> events = new HashSet<>();

        for(Event event : eventsList) {
            if(event.getEventDate().equals(date))
                events.add(event.color);
        }
        return events;
    }

    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList) {
            int cellHour = time.getHour();
            int startHour = event.startEventTime.getHour();
            // time is between start and end time of 
            if (event.getEventDate().equals(date) && startHour <= cellHour && event.endEventTime.isAfter(time))
                events.add(event);
        }

        return events;
    }


    // Used to pass Events to be modified across activities
    public static boolean isModify = false;
    public static Event modifyEvent;


    private static final String[] FIELDS = new String[]{
            "name",
            "date",
            "startTime",
            "endTime",
            "description",
            "notif",
            "color"
    };

    private boolean notif;
    private final String name, description;
    private final LocalDate eventDate;
    private final LocalTime startEventTime, endEventTime;
    private final String color;

    public Event(Map<String, String> fields) {
        this(   fields.get(FIELDS[0]),
                fields.get(FIELDS[1]),
                fields.get(FIELDS[2]),
                fields.get(FIELDS[3]),
                fields.get(FIELDS[4]),
                fields.get(FIELDS[5]) == null ? "" : fields.get(FIELDS[5]),
                fields.get(FIELDS[6]));
    }

    // Constructor for when parameters are in String instead (eg. During event retrieval from firebase)
    public Event(String name, String strEventDate, String strStartEventTime, String strEndEventTime, String description, String notif, String color) {
        this(name, stringToLocalDate(strEventDate), stringToLocalTime(strStartEventTime), stringToLocalTime(strEndEventTime), description, notif.equals("true"), color);
    }

    // Default constructor given appropriate classes
    public Event(String name, LocalDate eventDate, LocalTime startEventTime, LocalTime endEventTime, String description, boolean notif, String color) {
        this.name = name;
        this.eventDate = eventDate;
        this.startEventTime = startEventTime;
        this.endEventTime = endEventTime;
        this.description = description;
        this.notif = notif;
        this.color = color;
    }

    /**
     * Utility function to convert individual events to map.
     *
     */
    public Map<String, Object> toMap() {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put(FIELDS[0], this.name);
        mapEvent.put(FIELDS[1], this.eventDate.toString());
        mapEvent.put(FIELDS[2], this.startEventTime.toString());
        mapEvent.put(FIELDS[3], this.endEventTime.toString());
        mapEvent.put(FIELDS[4], this.description);
        mapEvent.put(FIELDS[5], Boolean.toString(this.notif));
        mapEvent.put(FIELDS[6], color);
        return mapEvent;
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

    public String getColor() {
        return color;
    }

    public boolean isNotif() {
        return notif;
    }

    public void setReminder(Context context, boolean isDelete) {
        if (this.notif) {
            NotificationUtils notif = new NotificationUtils(context);
            long notifTime = CalendarUtils.localDateTimeToCalendar(this.eventDate, this.startEventTime)
                    .getTimeInMillis();
            notif.setReminder(notifTime, this.name, "Your event is starting!", this.hashCode(), isDelete);
        }
    }

    @Override
    public int compareTo(Event o) {
        return this.startEventTime.compareTo(o.startEventTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return notif == event.notif && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(eventDate, event.eventDate) && Objects.equals(startEventTime, event.startEventTime) && Objects.equals(endEventTime, event.endEventTime) && Objects.equals(color, event.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, eventDate, startEventTime, endEventTime, color);
    }

    // Testing purposes
    @Override
    public String toString() {
        return "Event{" +
                "notif=" + notif +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", startEventTime=" + startEventTime +
                ", endEventTime=" + endEventTime +
                ", color='" + color + '\'' +
                '}';
    }
}
