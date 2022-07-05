package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterAccount extends AppCompatActivity {

    private EditText username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        initWidgets();
    }

    private void initWidgets() {
        username = findViewById(R.id.etRegName);
        email = findViewById(R.id.etRegEmail);
        password = findViewById(R.id.etRegPassword);
    }

    public void registerAccount(View view) {
        finish();
    }
}