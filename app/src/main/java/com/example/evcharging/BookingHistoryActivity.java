package com.example.evcharging;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingHistoryActivity extends AppCompatActivity {
    private TextView bookingDetailsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        bookingDetailsTextView = findViewById(R.id.bookingDetailsTextView);

        // Fetch old bookings from the database
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder bookingDetails = new StringBuilder();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking booking = snapshot.getValue(Booking.class);

                    if (booking != null) {
                        // Append each booking's details to the StringBuilder
                        bookingDetails.append("Charging Station ID: ").append(booking.getChargingStationId()).append("\n");
                        bookingDetails.append("Date: ").append(booking.getDay()).append("/").append(booking.getMonth()).append("/").append(booking.getYear()).append("\n");
                        bookingDetails.append("Time: ").append(booking.getHour()).append(":").append(booking.getMinute()).append("\n\n");
                    }
                }

                // Display the booking details in the TextView
                bookingDetailsTextView.setText(bookingDetails.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }
}
