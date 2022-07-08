package com.example.tasked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText email, password1, password2;
    private String strEmail, strPassword1, strPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        initWidgets();
    }

    private void initWidgets() {
        mAuth = FirebaseAuth.getInstance();

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

        mAuth.createUserWithEmailAndPassword(strEmail, strPassword1)
                .addOnCompleteListener(RegisterAccount.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        User user = User.of(mAuth.getCurrentUser());

                        Toast.makeText(RegisterAccount.this, "Account created.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAccount.this, MonthCalendar.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterAccount.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}