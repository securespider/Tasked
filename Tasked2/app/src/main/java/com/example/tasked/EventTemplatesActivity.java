package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventTemplatesActivity extends AppCompatActivity {

    LinearLayout defaultTemplates;
    LinearLayout customTemplates;
    FloatingActionButton floatingActionButton; // to add custom templates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_templates);
        initWidgets();
    }

    private void initWidgets() {
        defaultTemplates = findViewById(R.id.llDefaultTemplates);
        customTemplates = findViewById(R.id.llCustomTemplates);
        floatingActionButton = findViewById(R.id.fab);
    }

    private Button addButton(String text, View.OnClickListener onClickListener) {
        Button result = new Button(EventTemplatesActivity.this);
        result.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setText(text);
        result.setTextSize(20);
        result.setOnClickListener(onClickListener);
        return result;
    }
}