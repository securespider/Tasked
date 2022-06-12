package com.example.tasked;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

/**
 * Used to generate the list of dates in a month and display as a Calendar
 */
class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> datesOfMonth;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<String> datesOfMonth, OnItemListener onItemListener)
    {
        this.datesOfMonth = datesOfMonth;
        this.onItemListener = onItemListener; // used to check for touch inputs
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_dates, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        // maximum of 31 days - each row 7 days
        // in worst case, we start the month on a saturday, so we need max of 6 rows for all dates
        layoutParams.height = (int) (parent.getHeight() / 6);

        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        holder.monthDate.setText(datesOfMonth.get(position));
    }

    @Override
    public int getItemCount()
    {
        return datesOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}