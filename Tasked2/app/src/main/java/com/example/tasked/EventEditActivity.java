package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.Calendar;

/**
 * Class specified to the page for Event Activity creation.
 *
 * This activity should have start end date and time configuration functionality,
 * creation of event with name and
 */
public class EventEditActivity extends AppCompatActivity
{
    // Constants
    private final static CharSequence INVALIDTIME = "Invalid! Start time should be before End Time";

    Event event;
    private EditText etEventName, etEventDescription;
    private Button btnEventDate, btnStartEventTime, btnEndEventTime, btnCancel;

    private LocalTime startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        if (Event.isModify) {
            initModifiedEvent();
        } else {
            initNewEvent();
        }
        setEventView();
    }

    // Event is supposed to be modified
    private void initModifiedEvent() {
        this.event = Event.modifyEvent;
        startTime = this.event.getStartEventTime();
        endTime = this.event.getEndEventTime();

        // change text to previous data
        etEventName.setText(this.event.getName(), TextView.BufferType.EDITABLE);
        etEventDescription.setText(this.event.getDescription(), TextView.BufferType.EDITABLE);
        btnCancel.setText((CharSequence) "Delete");
    }

    private void initNewEvent() {
        // set default start and end time based on current time
        startTime = LocalTime.of(LocalTime.now().getHour(), 0); // only hour for easier readability
        endTime = startTime.plusHours(1);
        if (! isValidEndTime()) {
            // current time is past 11pm so endtime shld be 2359
            endTime = LocalTime.of(23, 59);
        }
    }

    private void initWidgets()
    {
        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        btnEventDate = findViewById(R.id.btnEventDatePicker);
        btnCancel = findViewById(R.id.btnCancel);

        // set default time interval
        btnStartEventTime = findViewById(R.id.btnStartEventTimePicker);
        btnEndEventTime = findViewById(R.id.btnEndEventTimePicker);

        // set time button onclick actions
        btnStartEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeUtil(true);
                setEventView();
            }
        });
        btnEndEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeUtil(false);
                setEventView();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Event.isModify) {
                    User.user.removeEvent(Event.modifyEvent);
                }
                Event.isModify = false;
                finish();
            }
        });

    }

    /**
     * Method to make sure that the set start time is valid.
     *
     * Invalid time
     * 1. endTime later than startTime
     * 2. endTime
     */
    private boolean isValidEndTime() {
        return startTime.isBefore(endTime);
    }

    /**
     * Assigned to onclick of the date button to change the date that is selected.
     *
     * @param view
     */
    public void selectDate(View view) {
        CalendarUtils.selectDateDialog(this, this::setEventView);
    }


    /**
     * Utility method for creation of time dialog box for start and end time selection.
     * 
     * @param isStartTime Checks if this timepicker is for the start or end time
     */
    private void selectTimeUtil(boolean isStartTime) {

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (isStartTime) {
                    startTime = LocalTime.of(hourOfDay, minute);
                } else {
                    endTime = LocalTime.of(hourOfDay, minute);
                }
                if (!isValidEndTime()) {
                    Toast.makeText(getApplicationContext(), INVALIDTIME, Toast.LENGTH_SHORT).show();
                }
                setEventView();
            }
        };

        LocalTime time;

        if (isStartTime) {
            time = startTime;
        } else {
            time = endTime;
        }

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                listener,
                time.getHour(),
                time.getMinute(),
                true);

        dialog.show();
    }

    /**
     * Method to refresh all text boxes to show updated values.
     */
    public void setEventView() {
        btnEventDate.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        btnStartEventTime.setText("From: " + CalendarUtils.formattedShortTime(startTime));
        btnEndEventTime.setText("To: " + CalendarUtils.formattedShortTime(endTime));
    }


    /**
     * Onclick method for saving this event
     *
     * @param view
     */
    public void saveEventAction(View view) {
        if (isValidEndTime()) {
            if (Event.isModify) {
                User.user.removeEvent(Event.modifyEvent);
                Event.isModify = false;
            }
            String eventName = etEventName.getText().toString();
            String description = etEventDescription.getText().toString();
            this.event = new Event(eventName, CalendarUtils.selectedDate, startTime, endTime, description);
            User.user.addEvent(this.event);
            finish(); // does not go to the new date but the date that was previously selected
        } else {
            Toast.makeText(getApplicationContext(), INVALIDTIME, Toast.LENGTH_SHORT).show();
        }
    }

}