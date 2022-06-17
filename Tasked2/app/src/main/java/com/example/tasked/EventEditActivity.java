package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
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
    private final static CharSequence INVALIDTIME = "Invalid Time. Please choose a different end time";
    
    
    private EditText etEventName;
    private Button btnEventDate, btnStartEventTime, btnEndEventTime;

    private LocalTime startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        initWidgets();
        setEventView();
    }

    private void initWidgets()
    {
        // set default start and end time based on current time
        startTime = LocalTime.now().withMinute(0).withNano(0).withSecond(0); // only hour for easier readability
        endTime = startTime.plusHours(1);
        if (! isValidEndTime()) {
            // current time is past 11pm so endtime shld be 2359
            endTime = LocalTime.of(23, 59);
        }

        etEventName = findViewById(R.id.etEventName);
        btnEventDate = findViewById(R.id.btnEventDatePicker);

        // set default time interval
        btnStartEventTime = findViewById(R.id.btnStartEventTimePicker);
        btnEndEventTime = findViewById(R.id.btnEndEventTimePicker);

        // set time button onclick actions
        btnStartEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = selectTimeUtil(startTime);
                setEventView();
            }
        });
        btnEndEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime = selectTimeUtil(endTime);
                while (!isValidEndTime()) {
                    Toast.makeText(getApplicationContext(), INVALIDTIME, Toast.LENGTH_SHORT).show();
                    endTime = selectTimeUtil(endTime);
                }
                setEventView();
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
        return startTime.isAfter(endTime);
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
     * @param time the default time shown in the box
     * @return New selected time the user has chosen
     */
    private LocalTime selectTimeUtil(LocalTime time) {

        final LocalTime[] result = new LocalTime[1];
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                result[0] = LocalTime.of(hourOfDay, minute);
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
        return result[0];
    }

    /**
     * Method to refresh all text boxes to show updated values.
     */
    public void setEventView() {
        btnEventDate.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        btnStartEventTime.setText("From: " + CalendarUtils.formattedTime(startTime));
        btnEndEventTime.setText("To: " + CalendarUtils.formattedTime(endTime));
    }

    /**
     * Onclick method for saving this event
     * @param view
     */
    public void saveEventAction(View view)
    {
        String eventName = etEventName.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, startTime, endTime);
        Event.eventsList.add(newEvent);
        finish(); // does not go to the new date but the date that was previously selected
    }
}