package com.example.evcharging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button bookNowButton;

    private FirebaseAuth mAuth;
    private DatabaseReference bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        mAuth = FirebaseAuth.getInstance();
        bookingRef = FirebaseDatabase.getInstance().getReference("bookings");

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        bookNowButton = findViewById(R.id.bookNowButton);

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected date and time from date and time pickers
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Create a Booking object with the selected data
                Booking booking = new Booking(
                        mAuth.getCurrentUser().getUid(),
                        "Charging Station ID", // Replace with the actual charging station ID
                        day, month, year, hour, minute
                );

                // Save the booking data to Firebase
                String bookingId = bookingRef.push().getKey();
                bookingRef.child(bookingId).setValue(booking);

                // Implement payment integration here
                // You may use an intent to start the payment activity

                // Display a confirmation message
                Toast.makeText(BookingActivity.this, "Booking Successful!", Toast.LENGTH_SHORT).show();
                // Start the PaymentActivity and pass booking data
                Intent paymentIntent = new Intent(BookingActivity.this, PaymentActivity.class);
                paymentIntent.putExtra("bookingId", bookingId);
                paymentIntent.putExtra("booking", booking); // Pass the booking object as an extra
                startActivity(paymentIntent);
                
            }
        });
    }
}
