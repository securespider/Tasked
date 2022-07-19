package com.example.tasked;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This is the login page and the first activity that the user will interact with.
 *
 */
public class MainActivity extends AppCompatActivity {


    
    // Widget declarations
    private EditText email, password;
    private Button login, forgetPassword, register;
    private TextView errorMessage;
    private static final CharSequence WRONGPASSWORD = "The email or password was incorrect.";
    private static final CharSequence NOCONNECTION = "There is no connection. Please connect to the internet and try again.";
    private int attemptsLeft = 5; // designated maximum attempts until user will be logged out
    // TODO: attemptsLeft tied to email/device instead of instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
    }

    @Override
    protected void onResume() {
        email.setText("");
        password.setText("");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private void initWidgets() {

        email = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegisterAccount);
        forgetPassword = (Button) findViewById(R.id.btnForgetPassword);
        errorMessage = (TextView) findViewById(R.id.tvError);

        // Onclick methods for register and forgetPassword
        register.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RegisterAccount.class)));

        forgetPassword.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ForgetPassword.class)));
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
     */
    public void validate (View view) {
        closeKeyboard(view);
        String strEmail = this.email.getText().toString().trim(),
                strPassword = this.password.getText().toString().trim();

        if (!isInputValid(strEmail, strPassword)) {
            return;
        }

        if (!isOnline()) {
            Toast.makeText(MainActivity.this, NOCONNECTION, Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        if (CalendarUtils.DB == null) {
                            CalendarUtils.DB = FirebaseDatabase.getInstance("https://tasked-44a12-default-rtdb.asia-southeast1.firebasedatabase.app/");
                            CalendarUtils.DB.setPersistenceEnabled(true);
                        }
                        User.of(mAuth.getCurrentUser(), strEmail);

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

    private void closeKeyboard(View view)
    {
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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}