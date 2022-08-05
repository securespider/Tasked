package com.example.tasked;

import static com.example.tasked.CalendarUtils.daysInWeekArray;
import static com.example.tasked.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private Button monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWeekView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.btnMonthYear2);
        eventListView = findViewById(R.id.eventListView);
    }

    public void selectWeekAction(View view) {
        CalendarUtils.selectDateDialog(this, this::refreshWeekView);
    }

    private void setWeekView() {
        refreshWeekView();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
    }

    private void refreshWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setWeekView();
        }
    }

    @Override
    public boolean onLongItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            startActivity(new Intent(WeekViewActivity.this, EventEditActivity.class));
            setWeekView();
        }
        return true;
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            Event chosenEvent = eventAdapter.getItem(position);
            Event.isModify = true;
            Event.modifyEvent = chosenEvent;
            Log.v("onclickListener", "listener created in weekview");
            startActivity(new Intent(WeekViewActivity.this, EventEditActivity.class));
        });
    }

    public void newEventAction(View view)
    {
        Intent intent = new Intent(WeekViewActivity.this, EventEditActivity.class);
        startActivity(intent);
    }

    public void dailyAction(View view) {
        startActivity(new Intent(WeekViewActivity.this, DailyCalendarActivity.class));
    }

    public void profileAction(View view) {
        startActivity(new Intent(WeekViewActivity.this, ProfileActivity.class));
    }

    public void templateEventAction(View view) {
        startActivity(new Intent(WeekViewActivity.this, EventTemplatesActivity.class));
    }

    public void monthlyAction(View view) {
        Intent intent = new Intent(WeekViewActivity.this, MonthCalendarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}