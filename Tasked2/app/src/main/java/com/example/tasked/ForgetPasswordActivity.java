package com.example.tasked;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private static final CharSequence NOCONNECTION = "There is no connection. Please connect to the internet and try again.";
    private static final CharSequence ERROR = "Try again! There was an issue.";
    private static final CharSequence SUCCESS = "Check your email to reset your password!";


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initWidgets();
    }

    private void initWidgets() {
        email = findViewById(R.id.etChgPass);
        auth = FirebaseAuth.getInstance();
    }

    private boolean isInputValid(String strEmail) {
        if (strEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return false;
        }
        return true;
    }

    public void resetPass(View view) {
        String strEmail = email.getText().toString().trim();
        if (!isInputValid(strEmail)) {
            return;
        }
        auth.sendPasswordResetEmail(strEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User.logoutUser();
                        startActivity(new Intent(ForgetPasswordActivity.this, MainActivity.class));
                        Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isOnline()) {
                            Toast.makeText(getApplicationContext(), NOCONNECTION, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }
}