package com.example.tasked;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParams.height = parent.getHeight() / 6;
        else // week view
            layoutParams.height = parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position);
        Set<String> eventCat = Event.eventCatForDate(date);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(CalendarUtils.selectedDate))
            holder.parentView.setBackgroundColor(Color.LTGRAY);

        if (!eventCat.isEmpty()) {
            holder.parentView.setBackgroundColor(Color.parseColor("#FFFFC2"));
            if (date.equals(CalendarUtils.selectedDate))
                holder.parentView.setBackgroundColor(Color.parseColor("#FFF3A1"));
            if (eventCat.contains("red"))
                holder.parentView.findViewById(R.id.imgEventRed).setVisibility(View.VISIBLE);
            if (eventCat.contains("blue"))
                holder.parentView.findViewById(R.id.imgEventBlue).setVisibility(View.VISIBLE);
            if (eventCat.contains("green"))
                holder.parentView.findViewById(R.id.imgEventGreen).setVisibility(View.VISIBLE);
        }



        if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth()))
            holder.dayOfMonth.setTextColor(Color.BLACK);
        else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener {
        void onItemClick(int position, LocalDate date);

        boolean onLongItemClick(int position, LocalDate date);
    }
}
