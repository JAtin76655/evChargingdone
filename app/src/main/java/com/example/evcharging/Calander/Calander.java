package com.example.evcharging.Calander;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.evcharging.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calander extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView calendarGridView = findViewById(R.id.calendarGridView);
        List<String> dates = generateDatesForCalendar();
        Calendar adapter = new Calendar(this, dates);
        calendarGridView.setAdapter(adapter);
    }

    private List<String> generateDatesForCalendar() {
        List<String> dates = new ArrayList<>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());

        int daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        int startDay = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        // Add empty cells for the days before the 1st day of the month
        for (int i = 1; i < startDay; i++) {
            dates.add("");
        }

        // Add days of the month
        for (int i = 1; i <= daysInMonth; i++) {
            dates.add(sdf.format(calendar.getTime()));
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }
}
