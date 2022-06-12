package com.example.tasked;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Used to check click position and identify the date that was chosen
 */
public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView monthDate; // dates of the month
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        monthDate = itemView.findViewById(R.id.cellDates);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) monthDate.getText());
    }
}
