package com.example.tasked;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static User user;

    // Constant
    public static DatabaseReference REF =
            FirebaseDatabase
                    .getInstance("https://tasked-44a12-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Users");

    // With these various attributes
    public FirebaseUser currentUser;
    public String uid;

    private User(@NonNull FirebaseUser user) {
        this.currentUser = user;
        this.uid = user.getUid();
    }

    public static User of(FirebaseUser user) {
        User.user = new User(user);
        retrieveAllData();
        return User.user;
    }

    public static void retrieveAllData() {
        REF.child(user.uid).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void addEvent(Event event) {
        Event.eventsList.add(event);
        Map<String, Object> map = event.toMap();
        REF.child(uid).child(String.valueOf(event.hashCode())).setValue(map);
    }

    public void removeEvent(Event event) {
        if (Event.eventsList.contains(event)) {
            Event.eventsList.remove(event);
            REF.child(uid).child(String.valueOf(event.hashCode())).removeValue();
            Log.v("Event", "event deleted " + event.getName());
        }
    }

//    public static boolean addEventToFirebase (Event event) {
//        boolean[] result = new boolean[1];
//        REF.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild(uid)){
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        REF.child("User")
//                .setValue(event)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (!task.isSuccessful()) {
//                            result[0] = true;
//                        }
//                    }
//                });
//        return result[0];
//    }

    // Initialise the database with new list of events
//                        User.REF.child(User.user.uid)
//            .setValue(user)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//        @Override
//        public void onComplete(@NonNull Task<Void> task) {
//            if (!task.isSuccessful()) {
//                Toast.makeText(RegisterAccount.this, "Authentication failed!",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    });
}
