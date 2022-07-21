package com.example.tasked;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private EditText etNusmodsUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initWidgets();
    }

    public void initWidgets() {
        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(User.getEmail());
        this.etNusmodsUrl = findViewById(R.id.etNusmodUrl);
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

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
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    public void deleteProfileAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

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
        startActivity(new Intent(ProfileActivity.this, ForgetPasswordActivity.class));
    }

    public void logoutAction(View view) {
        logoutDialog();
    }


    private boolean isInputValid(String url) {
        String start = "^https://nusmods.com/timetable/";
        String mid = "sem-[12]/share\\?";
        String moduleFormat = "[A-Z]{2,5}\\d{4}=";

        if (!Pattern.compile(start + ".*").matcher(url).matches()) {
            this.etNusmodsUrl.setError("Url is invalid");
            this.etNusmodsUrl.requestFocus();
            return false;
        }

        if (!Pattern.compile(start + mid + ".*").matcher(url).matches()) {
            this.etNusmodsUrl.setError("Only timetables in the semester is supported right now");
            this.etNusmodsUrl.requestFocus();
            return false;
        }

        if (!Pattern.compile(start + mid + moduleFormat + ".*").matcher(url).matches()) {
            this.etNusmodsUrl.setError("Url is invalid or there are no mods to import");
            this.etNusmodsUrl.requestFocus();
            return false;
        }
        return true;
    }


    public void importTimetable(View view) {
        closeKeyboard(view);
        final String url = etNusmodsUrl.getText().toString().trim();
        final int semester = semFromTimetable(url);
        if (!isInputValid(url)) {
            return;
        }

        if (!isOnline()) {
            this.etNusmodsUrl.setError("There is no internet connection. Service not available now.");
            return;
        }

        // TODO: Hardcoded constant for ay start dates
        final LocalDate start;
        if (semester == 2) {
            start = LocalDate.of(2023, 1, 9);
        } else {
            start = LocalDate.of(2022, 8, 8);
        }
        ProgressBar progressBar = findViewById(R.id.pbImportTimetable);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> User.importTimetable(url, start, semester, ProfileActivity.this));
        future.thenRunAsync(() -> {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ProfileActivity.this, "Timetable has been imported", Toast.LENGTH_SHORT).show();
        });
        progressBar.setVisibility(View.VISIBLE);
        future.join();
    }

    private int semFromTimetable(String truncUrl) {
        Pattern startingRegex = Pattern.compile("sem-([12])");
        Matcher startingMatch = startingRegex.matcher(truncUrl);
        if (!startingMatch.find() || startingMatch.group(1) == null) {
            return 1;
        }
        return Integer.parseInt(startingMatch.group(1));
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void closeKeyboard(View view) {
        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}
