package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the login page and the first activity that the user will interact with.
 *
 */
public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login, forgetPassword, register;
    private TextView errorMessage;
    private CharSequence WRONGPASSWORD = "The username or password was incorrect.";
    private int attemptsLeft = 5; // designated maximum attempts until user will be logged out
    // TODO: attemptsLeft tied to username/device instead of instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets() {
        username = (EditText) findViewById(R.id.etName);
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

    private boolean passwordManager (String username, String password) {
        // TODO: method to interact with the password management server
        if (username == "wrongUser"){
            return false;
        }
        return true;
    }

    /**
     * Onclick method for login button
     * @param view
     */
    public void validate (View view) {
        String stUsername = this.username.getText().toString(),
                stPassword = this.password.getText().toString();
        if (passwordManager(stUsername, stPassword)) {
            Intent intent = new Intent(this, MonthCalendar.class); // used to move to other activity
            startActivity(intent);
        }
        else {
            Toast.makeText(this, WRONGPASSWORD, Toast.LENGTH_SHORT).show();
            if (--attemptsLeft == 0) {
                login.setEnabled((false));

                // message saying that maximum attempts limit has been reached
                errorMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}