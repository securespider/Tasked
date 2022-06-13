package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private Button btnEventDate, btnEventTime;

    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        setEventView();
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        btnEventDate = findViewById(R.id.btnEventDatePicker);
        btnEventTime = findViewById(R.id.btnStartTimePicker);
    }

    /**
     * Assigned to onclick of the date button to change the date that is selected.
     *
     * @param view
     */
    public void selectDate(View view) {
        CalendarUtils.selectDateDialog(this, this::setEventView);
    }

    public void selectTime(View view) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = LocalTime.of(hourOfDay, minute);
                setEventView();
            }
        };

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                listener,
                time.getHour(),
                time.getMinute(),
                true);
        dialog.show();
    }

    public void setEventView() {
        btnEventDate.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        btnEventTime.setText(CalendarUtils.formattedTime(time));
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish(); // does not go to the new date but the date that was previously selected
    }
}