package com.example.tasked;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class User {

    // There should only be one instance of a user
    private static User user;

    // Constant
    private static DatabaseReference EVENTREF;
    private static DatabaseReference CUSTOMTEMPLATEREF;
    private static DatabaseReference DEFAULTTEMPLATEREF;


    // With these various attributes
    private static FirebaseUser currentUser;
    private static String uid;
    private static String email;


    private User(@NonNull FirebaseUser user, String email) {
        User.currentUser = user;
        User.uid = user.getUid();
        User.email = email;
        EVENTREF = CalendarUtils.DB.getReference("Users").child(uid);
        CUSTOMTEMPLATEREF = CalendarUtils.DB.getReference("Templates").child("Custom").child(uid);
        DEFAULTTEMPLATEREF = CalendarUtils.DB.getReference("Templates").child("Default");
    }

    public static User of(FirebaseUser user, String email) {
        User.user = new User(user, email);
        Event.eventsList = retrieveEventData(EVENTREF);
        return User.user;
    }

    public static ArrayList<Event> retrieveEventData(DatabaseReference ref) {
        ArrayList<Event> result = new ArrayList<>();
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Map<String, String>> dataSnapshot = (Map<String, Map<String, String>>) snapshot.getValue();
                    result.addAll(collectEvents(dataSnapshot));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        ref.keepSynced(false);
        return result;
    }

    private static ArrayList<Event> collectEvents(Map<String, Map<String, String>> eventList) {
        if (eventList == null) {
            return new ArrayList<>();
        }
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Map<String,String>> entry: eventList.entrySet()) {
            Map<String, String> fields = entry.getValue();
            Event event = new Event(fields);
            events.add(event);
        }
        return events;
    }

    public static void addEvent(Event newEvent, Context context) {
        for (Event event : Event.eventsList) {
            if (event.equals(newEvent)) {
                return;
            }
        }
        Event.eventsList.add(newEvent);
        Map<String, Object> map = newEvent.toMap();
        EVENTREF.child(String.valueOf(newEvent.hashCode())).setValue(map);
        newEvent.setReminder(context, false);
    }

    public static void removeEvent(Event event, Context context) {
        if (Event.eventsList.contains(event)) {
            Event.eventsList.remove(event);
            EVENTREF.child(String.valueOf(event.hashCode())).removeValue();
            if (event.isNotif()) {
                event.setReminder(context, true);
            }
        }
    }

    public static void deleteUser(Context context, Runnable successAction) {
        currentUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Profile has been deleted", Toast.LENGTH_SHORT).show();
                EVENTREF.removeValue();
                FirebaseAuth.getInstance().signOut();
                successAction.run();
            } else {
                Toast.makeText(context, "Profile cannot be deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        currentUser = null;
        uid = null;
        email = null;
    }

    public static String getEmail() {
        return email;
    }

    public static void importTimetable(String nusmodsUrl, LocalDate ayStart, int semester, Context context) {
        ArrayList<Event> events = CalendarUtils.gettingTimetable(nusmodsUrl, ayStart, semester);
        for (Event event: events) {
            addEvent(event, context);
        }
    }



    // Template methods

    public static ArrayList<Event> retrieveCustomTemplateData() {
        return retrieveEventData(CUSTOMTEMPLATEREF);
    }
    public static ArrayList<Event> retrieveDefaultTemplateData() {
        return retrieveEventData(DEFAULTTEMPLATEREF);
    }

    public static void addTemplate(Event newEvent) {
        CUSTOMTEMPLATEREF.child(newEvent.getName()).setValue(newEvent.toMap());
    }
}
