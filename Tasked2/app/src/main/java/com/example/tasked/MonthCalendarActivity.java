package com.example.tasked;

import static com.example.tasked.CalendarUtils.daysInMonthArray;
import static com.example.tasked.CalendarUtils.monthYearFromDate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Used to show the month in its widest view
 */
public class MonthCalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private Button monthYearText;
    private RecyclerView calendarRecyclerView;


    @Override
    protected void onResume() {
        refreshMonthView();
        super.onResume();
    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MonthCalendarActivity.this);

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

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.btnMonthYear);
    }

    public void selectMonthAction(View view) {
        CalendarUtils.selectDateDialog(this, this::refreshMonthView);
    }

    private void setMonthView() {
        refreshMonthView();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MonthCalendarActivity.this, 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
    }

    private void refreshMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
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
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    @Override
    public boolean onLongItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            startActivity(new Intent(MonthCalendarActivity.this, EventEditActivity.class));
            setMonthView();
        }
        return true;
    }

    public void weeklyAction(View view) {
        startActivity(new Intent(MonthCalendarActivity.this, WeekViewActivity.class));
    }

    public void profileAction(View view) {
        startActivity(new Intent(MonthCalendarActivity.this, ProfileActivity.class));
    }

    public void dailyAction(View view) {
        startActivity(new Intent(MonthCalendarActivity.this, DailyCalendarActivity.class));
    }
}








