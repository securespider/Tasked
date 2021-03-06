package com.example.tasked;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class SimpleImageArrayAdapter extends ArrayAdapter<Integer> {
    private final Integer[] images;
    private final int dimension;

    public SimpleImageArrayAdapter(Context context, Integer[] images, float dimension) {
        super(context, android.R.layout.simple_spinner_item, images);
        this.images = images;
        this.dimension = (int) dimension;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.requestLayout();
        imageView.setBackgroundResource(images[position]);
        imageView.setLayoutParams(new AbsListView.LayoutParams(dimension, dimension));
        return imageView;
    }
}