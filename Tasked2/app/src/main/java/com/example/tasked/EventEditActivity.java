package com.example.tasked;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

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
        btnStartEventTime.setOnClickListener(v -> {
            selectTimeUtil(true);
            setEventView();
        });
        btnEndEventTime.setOnClickListener(v -> {
            selectTimeUtil(false);
            setEventView();
        });

        btnCancel.setOnClickListener(v -> {
            if (Event.isModify) {
                User.removeEvent(Event.modifyEvent);
            }
            Event.isModify = false;
            finish();
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

        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
            if (isStartTime) {
                startTime = LocalTime.of(hourOfDay, minute);
            } else {
                endTime = LocalTime.of(hourOfDay, minute);
            }
            if (!isValidEndTime()) {
                Toast.makeText(getApplicationContext(), INVALIDTIME, Toast.LENGTH_SHORT).show();
            }
            setEventView();
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
        String strDate = "Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        String strStart = "From: " + CalendarUtils.formattedShortTime(startTime);
        String strEnd = "To: " + CalendarUtils.formattedShortTime(endTime);
        btnEventDate.setText(strDate);
        btnStartEventTime.setText(strStart);
        btnEndEventTime.setText(strEnd);
    }


    /**
     * Onclick method for saving this event
     *
     */
    public void saveEventAction(View view) {
        if (isValidEndTime()) {
            if (Event.isModify) {
                User.removeEvent(Event.modifyEvent);
                Event.isModify = false;
            }
            String eventName = etEventName.getText().toString();
            String description = etEventDescription.getText().toString();
            this.event = new Event(eventName, CalendarUtils.selectedDate, startTime, endTime, description);
            User.addEvent(this.event, EventEditActivity.this);
            finish(); // does not go to the new date but the date that was previously selected
        } else {
            Toast.makeText(getApplicationContext(), INVALIDTIME, Toast.LENGTH_SHORT).show();
        }
    }

}