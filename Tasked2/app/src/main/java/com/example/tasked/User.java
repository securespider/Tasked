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

import java.util.ArrayList;
import java.util.Map;



public class User {

    // There should only be one instance of a user
    private static User user;

    // Constant
    private static DatabaseReference REF;

    // With these various attributes
    private static FirebaseUser currentUser;
    private static String uid;
    private static String email;


    private User(@NonNull FirebaseUser user, String email) {
        User.currentUser = user;
        User.uid = user.getUid();
        User.email = email;
        REF = CalendarUtils.DB.getReference("Users").child(uid);
    }

    public static User of(FirebaseUser user, String email) {
        User.user = new User(user, email);
        retrieveAllData();
        return User.user;
    }

    public static void retrieveAllData() {
        REF.keepSynced(true);
        REF.addListenerForSingleValueEvent(new ValueEventListener() {
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
        REF.keepSynced(false);
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

    public static void addEvent(Event event, Context context) {
        Event.eventsList.add(event);
        Map<String, Object> map = event.toMap();
        REF.child(String.valueOf(event.hashCode())).setValue(map);
        event.setReminder(context, false);
    }

    public static void removeEvent(Event event, Context context) {
        if (Event.eventsList.contains(event)) {
            Event.eventsList.remove(event);
            REF.child(String.valueOf(event.hashCode())).removeValue();
            if (event.isNotif()) {
                event.setReminder(context, true);
            }
        }
    }

    public static void deleteUser(Context context, Runnable successAction) {
        currentUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Profile has been deleted", Toast.LENGTH_SHORT).show();
                REF.removeValue();
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
