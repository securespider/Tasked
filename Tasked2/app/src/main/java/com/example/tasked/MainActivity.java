package com.example.tasked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This is the login page and the first activity that the user will interact with.
 *
 */
public class MainActivity extends AppCompatActivity {

    // Firebase declaration
    private FirebaseAuth mAuth;


    
    // Widget declarations
    private EditText email, password;
    private Button login, forgetPassword, register;
    private TextView errorMessage;
    private CharSequence WRONGPASSWORD = "The email or password was incorrect.";
    private int attemptsLeft = 5; // designated maximum attempts until user will be logged out
    // TODO: attemptsLeft tied to email/device instead of instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets() {
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegisterAccount);
        forgetPassword = (Button) findViewById(R.id.btnForgetPassword);
        errorMessage = (TextView) findViewById(R.id.tvError);

        // Onclick methods for register and forgetPassword
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterAccount.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });
    }

    private boolean isInputValid(String strEmail, String strPassword) {
        if (strEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }

        if (strPassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return false;
        }
        return true;
    }



    /**
     * Onclick method for login button
     * @param view
     */
    public void validate (View view) {
        String strEmail = this.email.getText().toString().trim(),
                strPassword = this.password.getText().toString().trim();

        if (!isInputValid(strEmail, strPassword)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        CalendarUtils.user = mAuth.getCurrentUser();
                        Intent intent = new Intent(MainActivity.this, MonthCalendar.class); // used to move to other activity
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, WRONGPASSWORD, Toast.LENGTH_SHORT).show();
                        if (--attemptsLeft == 0) {
                            login.setEnabled((false));

                            // message saying that maximum attempts limit has been reached
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
        }
}