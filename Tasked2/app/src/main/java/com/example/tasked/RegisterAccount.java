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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAccount extends AppCompatActivity {

    private static final CharSequence NOCONNECTION = "There is no connection. Please connect to the internet and try again.";

    private EditText email, password1, password2;
    private String strEmail, strPassword1, strPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        initWidgets();
    }

    private void initWidgets() {
        email = (EditText) findViewById(R.id.etRegEmail);
        password1 = (EditText) findViewById(R.id.etRegPassword1);
        password2 = (EditText) findViewById(R.id.etRegPassword2);

    }

    private boolean isInputValid() {
        strEmail = email.getText().toString().trim();
        strPassword1 = password1.getText().toString().trim();
        strPassword2 = password2.getText().toString().trim();

        if (strEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }

        if (strPassword1.isEmpty()) {
            password1.setError("Password is required");
            password1.requestFocus();
            return false;
        }

        if (strPassword2.isEmpty()) {
            password2.setError("Password is required");
            password2.requestFocus();
            return false;
        }

        if (!strPassword2.equals(strPassword1)) {
            password2.setError("The password is not the same");
            password2.requestFocus();
            return false;
        }

        if (strPassword1.length() < 7) {
            password1.setError("Password should be longer than 8 characters");
            password1.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return false;
        }
        return true;
    }

    public void registerAccount(View view) {
        if (!isInputValid()) {
            return;
        }
        if (!isOnline()) {
            Toast.makeText(RegisterAccount.this, NOCONNECTION, Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword1)
                .addOnCompleteListener(RegisterAccount.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        if (CalendarUtils.DB == null) {
                            CalendarUtils.DB = FirebaseDatabase.getInstance("https://tasked-44a12-default-rtdb.asia-southeast1.firebasedatabase.app/");
                            CalendarUtils.DB.setPersistenceEnabled(true);
                        }
                        User.of(mAuth.getCurrentUser(), strEmail);
                        Toast.makeText(getApplicationContext(), "Account created.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAccount.this, MonthCalendar.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterAccount.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}