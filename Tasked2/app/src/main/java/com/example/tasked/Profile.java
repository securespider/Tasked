package com.example.tasked;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initWidgets();
    }

    public void initWidgets() {
        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(User.getEmail());
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);

        //Setting message manually and performing action on button click
        builder.setMessage("You are about to logout. Do you wish to continue?")
                .setCancelable(false)
                .setPositiveButton("Cancel", (dialog, id) -> dialog.cancel())
                .setNegativeButton("Logout", (dialog, id) -> {
                    //  Logout activities
                    logout();
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();
    }

    private void logout() {
        User.logoutUser();
        startActivity(new Intent(Profile.this, MainActivity.class));
    }

    public void deleteProfileAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);

        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure you want to delete your account? This action is irreversible!")
                .setCancelable(false)
                .setPositiveButton("Cancel", (dialog, id) -> dialog.cancel())
                .setNegativeButton("Delete", (dialog, id) -> {
                    //  Logout activities
                    User.deleteUser(getApplicationContext(), this::logout);
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Delete profile");
        alert.show();
    }

    public void changePasswordAction(View view) {
        startActivity(new Intent(Profile.this, ForgetPassword.class));
    }

    public void logoutAction(View view) {
        logoutDialog();
    }
}