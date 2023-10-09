package com.example.evcharging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private List<Booking> bookingList;

    // Constructor to initialize the list of bookings
    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views within each item
        Booking booking = bookingList.get(position);

        // Replace these with your actual view IDs
        holder.chargingStationTextView.setText("Charging Station ID: " + booking.getChargingStationId());
        holder.dateTextView.setText("Date: " + booking.getDay() + "/" + booking.getMonth() + "/" + booking.getYear());
        holder.timeTextView.setText("Time: " + booking.getHour() + ":" + booking.getMinute());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chargingStationTextView;
        TextView dateTextView;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your views here (replace with your actual view IDs)
            chargingStationTextView = itemView.findViewById(R.id.chargingStationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
