package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is the login page and the first activity that the user will interact with.
 *
 */
public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView errorMessage;
    private int attemptsLeft = 5; // designated maximum attempts until user will be logged out
    // TODO: attemptsLeft tied to username/device instead of instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        errorMessage = (TextView) findViewById(R.id.tvError);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private boolean passwordManager (String username, String password) {
        // TODO: method to interact with the password management server
        return true;
    }

    private void validate (String username, String password) {
        if (passwordManager(username, password)) {
            Intent intent = new Intent(MainActivity.this, Calendar.class); // used to move to other activity
            startActivity(intent);
        }
        else {
            if (--attemptsLeft == 0) {
                login.setEnabled((false));

                // message saying that maximum attempts limit has been reached
                errorMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}