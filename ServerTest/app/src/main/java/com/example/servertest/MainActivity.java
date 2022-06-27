package com.example.servertest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.servertest.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Dictionary;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Button newUserButton = (Button) findViewById(R.id.newUserButton);
        Button authenticateButton = (Button) findViewById(R.id.authenticateButton);
        TextInputEditText username = (TextInputEditText) findViewById(R.id.username);
        TextInputEditText password = (TextInputEditText) findViewById(R.id.password);
        TextView reply = (TextView) findViewById(R.id.reply);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread gfgThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ServerConnector sender = new ServerConnector();
                            JSONObject data = new JSONObject();
                            data.put("Username",username.getText().toString());
                            data.put("Password", password.getText().toString());
                            JSONObject full = new JSONObject();
                            full.put("Type", "New-User");
                            full.put("Data", data);
                            String returnText = sender.getPostResponseHttp(full);
                            reply.setText(returnText);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                gfgThread.start();
            }
        });

        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread gfgThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ServerConnector sender = new ServerConnector();
                            JSONObject data = new JSONObject();
                            data.put("Username",username.getText().toString());
                            data.put("Password", password.getText().toString());
                            JSONObject full = new JSONObject();
                            full.put("Type", "Authentication");
                            full.put("Data", data);
                            String returnText = sender.getPostResponseHttp(full);
                            reply.setText(returnText);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                gfgThread.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}