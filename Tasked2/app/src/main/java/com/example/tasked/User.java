package com.example.tasked;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;



public class User {

    // There should only be one instance of a user
    private static User user;

    // Constant
    public static DatabaseReference REF =
            FirebaseDatabase
                    .getInstance(CalendarUtils.PATH)
                    .getReference("Users");

    // With these various attributes
    private static FirebaseUser currentUser;
    private static String uid;
    private static String email;


    private User(@NonNull FirebaseUser user, String email) {
        User.currentUser = user;
        User.uid = user.getUid();
        User.email = email;
    }

    public static User of(FirebaseUser user, String email) {
        User.user = new User(user, email);
        retrieveAllData();
        return User.user;
    }

    public static void retrieveAllData() {
        REF.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Event> result = new ArrayList<>();
                if (snapshot.exists()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Map<String, String>> dataSnapshot = (Map<String, Map<String, String>>) snapshot.getValue();
                    result = collectEvents(dataSnapshot);
                }
                Event.eventsList = result;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Event.eventsList = new ArrayList<>();
            }
        });
    }
    private static ArrayList<Event> collectEvents(Map<String, Map<String, String>> eventList) {
        if (eventList == null) {
            return new ArrayList<>();
        }
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Map<String,String>> entry: eventList.entrySet()) {
            Map<String, String> fields = entry.getValue();
            String name = fields.get("name");
            String date = fields.get("date");
            String startTime = fields.get("startTime");
            String endTime = fields.get("endTime");
            String description = fields.get("description");
            Event event = new Event(name, date, startTime, endTime, description);
            events.add(event);
            Log.v("entry", name + " added");
        }
        return events;
    }

    public static void addEvent(Event event) {
        Event.eventsList.add(event);
        Map<String, Object> map = event.toMap();
        REF.child(uid).child(String.valueOf(event.hashCode())).setValue(map);
    }

    public static void removeEvent(Event event) {
        if (Event.eventsList.contains(event)) {
            Event.eventsList.remove(event);
            REF.child(uid).child(String.valueOf(event.hashCode())).removeValue();
            Log.v("Event", "event deleted " + event.getName());
        }
    }

    public static void deleteUser(Context context, Runnable successAction) {
        currentUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Profile has been deleted", Toast.LENGTH_SHORT).show();
                REF.child(uid).removeValue();
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
}
