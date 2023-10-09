package com.example.evcharging.Calander;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.evcharging.R;

import java.util.List;

class Calendar extends BaseAdapter {
    private final Context context;
    private final List<String> dates;

    public Calendar(Context context, List<String> dates) {
        this.context = context;
        this.dates = dates;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_cell, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        String date = dates.get(position);
        dateTextView.setText(date);

        // Add click event handling for date selection here

        return convertView;
    }
}
