package com.example.tasked;

import static com.example.tasked.CalendarUtils.daysInMonthArray;
import static com.example.tasked.CalendarUtils.monthYearFromDate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Used to show the month in its widest view
 */
public class MonthCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private Button monthYearText;
    private RecyclerView calendarRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MonthCalendar.this);

        //Setting message manually and performing action on button click
        builder.setMessage("You are about to logout. Do you wish to continue?")
                .setCancelable(false)
                .setPositiveButton("Cancel", (dialog, id) -> dialog.cancel())
                .setNegativeButton("Logout", (dialog, id) -> {
                    //  Logout activities
                    User.logoutUser();
                    finish();
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.btnMonthYear);

    }

    public void selectMonthAction(View view) {
        CalendarUtils.selectDateDialog(this, this::setMonthView);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view) {
        startActivity(new Intent(MonthCalendar.this, WeekViewActivity.class));
    }

    public void profileAction(View view) {
        startActivity(new Intent(MonthCalendar.this, Profile.class));
    }
}








