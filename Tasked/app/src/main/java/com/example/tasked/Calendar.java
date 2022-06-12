package com.example.tasked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.example.tasked.CalendarInfo.daysInMonthArray;
import static com.example.tasked.CalendarInfo.daysInWeekArray;

public class Calendar extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private Button nextMonth; // left arrow
    private Button prevMonth; // right arrow
    private Button selectMonth; // text is the selected month and it also serves as a more specific date selector
    private RecyclerView rvCalendar;


    /**
     * Default method for activity creation.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        this.nextMonth = findViewById(R.id.btnNextMonth);
        this.prevMonth = findViewById(R.id.btnPrevMonth);
        this.selectMonth = findViewById(R.id.btnSelectMonth);
        this.rvCalendar = findViewById(R.id.rvCalendar);
        CalendarInfo.selectedDate = LocalDate.now();


        // TODO: Make date interactive ie scroll wheel for changing specific date

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarInfo.selectedDate = CalendarInfo.selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarInfo.selectedDate = CalendarInfo.selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        CalendarInfo.selectedDate = LocalDate.now();
        setMonthView();
    }

    private void setMonthView()
    {
        selectMonth.setText(monthYearFromDate(CalendarInfo.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarInfo.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        rvCalendar.setLayoutManager(layoutManager);
        rvCalendar.setAdapter(calendarAdapter);
    }


    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(CalendarInfo.selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
