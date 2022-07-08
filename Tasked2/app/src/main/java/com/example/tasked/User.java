package com.example.tasked;

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

import java.util.Map;



public class User {

    // Constant
    public final static DatabaseReference REF =
            FirebaseDatabase
                    .getInstance("https://tasked-44a12-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Users");
    // There should only be one instance of a user
    public static User user;

    // With these various attributes
    public FirebaseUser currentUser;
    public String uid;
    public Map<String, Map<String, String>> events = Event.eventListToMap();

    private User(@NonNull FirebaseUser user) {
        this.currentUser = user;
        this.uid = user.getUid();
    }

    public static User of(FirebaseUser user) {
        User.user = new User(user);
        return User.user;
    }

    public static boolean retrieveData() {
        // TODO: retrieve events from the database and populate eventlist in arraylist format
        return true;
    }

    public void addEvent(Event event) {
        Event.eventsList.add(event);
        Map<String, String> map = event.eventToMap();
        REF.child(uid).child(Integer.toString(event.hashCode())).setValue(event);
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
