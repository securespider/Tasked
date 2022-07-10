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
    public Map<String, Map<String, Object>> events = Event.listToMap();

    private User(@NonNull FirebaseUser user) {
        this.currentUser = user;
        this.uid = user.getUid();
    }

    public static User of(FirebaseUser user) {
        User.user = new User(user);
//        retrieveAllData();
        return User.user;
    }

//    public static void retrieveAllData() {
//        REF.child(user.uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                @SuppressWarnings("unchecked")
//                Map<String, Map<String, Object>> dataSnapshot = (Map<String, Map<String, Object>>) snapshot.getValue();
//                User.user.events = dataSnapshot;
//                Event.eventsList = collectEvents(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Event.eventsList = new ArrayList<>();
//            }
//        });
//    }
//    private static ArrayList<Event> collectEvents(Map<String, Map<String, Object>> eventList) {
//        if (eventList == null) {
//            return new ArrayList<>();
//        }
//        ArrayList<Event> events = new ArrayList<>();
//        for (Map.Entry<String, Map<String,Object>> entry: eventList.entrySet()) {
//            Map<String, Object> fields = entry.getValue();
//            String name = (String) fields.get("name");
//            String date = (String) fields.get("date");
//            String startTime = (String) fields.get("startTime");
//            String endTime = (String) fields.get("endTime");
//            String description = (String) fields.get("description");
//            Event event = new Event(name, date, startTime, endTime, description);
//            events.add(event);
//        }
//        return events;
//    }

    public void addEvent(Event event) {
        Event.eventsList.add(event);
        Map<String, Object> map = event.toMap();
        Log.v("well", "reached here");
        REF.child(uid).push().setValue(map);
        Log.v("nice", "saved data");
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
