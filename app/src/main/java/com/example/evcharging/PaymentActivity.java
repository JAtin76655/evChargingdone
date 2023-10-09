package com.example.evcharging;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class PaymentActivity extends AppCompatActivity {
    private Spinner chargingRateSpinner;
    private EditText cardNumberEditText, cardExpiryEditText, cardCvvEditText;
    private Button payButton;

    private Booking booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");

        chargingRateSpinner = findViewById(R.id.chargingRateSpinner);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardExpiryEditText = findViewById(R.id.cardExpiryEditText);
        cardCvvEditText = findViewById(R.id.cardCvvEditText);
        payButton = findViewById(R.id.payButton);
        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);

        // Get the booking object from the intent
        String bookingId = getIntent().getStringExtra("bookingId");
        booking = getIntent().getParcelableExtra("booking");

        // Set up the charging rate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.charging_rates,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chargingRateSpinner.setAdapter(adapter);
        chargingRateSpinner.setSelection(0);
        // Set the default selected item (first item in the array)
        // 0 represents the index of the item you want to select
        chargingRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Calculate the total amount based on the selected charging rate
                String selectedRate = ((String) parentView.getItemAtPosition(position));
                float ratePerHour = Integer.parseInt(selectedRate.replaceAll("[^0-9]", ""));
                float tamt = ratePerHour/60;
                float totalAmount = tamt * 150 ;
                totalAmountTextView.setText("Total Payable Amount: ₹" + totalAmount);
                // Display the calculated amount
                Toast.makeText(PaymentActivity.this, "Total Amount: $" + totalAmount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check the input and change the icon accordingly
                String input = s.toString();

                if (input.startsWith("4")) {
                    cardNumberEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visa, 0);
                } else if (input.startsWith("6")) {
                    cardNumberEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_rupay, 0);
                } else if(input.startsWith("5")){
                    // Use the default icon for other values
                    Toast.makeText(PaymentActivity.this,"Only Visa And Rupay Cards valid", Toast.LENGTH_SHORT).show();
                }
                else if (input.startsWith("2")){
                    Toast.makeText(PaymentActivity.this,"Only Visa And Rupay Cards valid", Toast.LENGTH_SHORT).show();
                }
                else {
                    cardNumberEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.card, 0);
                    Toast.makeText(PaymentActivity.this,"Please Enter A Valid Card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement payment processing logic here (e.g., connect to a payment gateway)
                // For manual credit card payment, you would need to securely handle the card details,
                // which is beyond the scope of this basic example.
                String selectedRate = chargingRateSpinner.getSelectedItem().toString();
                int ratePerHour = Integer.parseInt(selectedRate.replaceAll("[^0-9]", ""));
                int tamt = ratePerHour/60;
                int totalAmount = tamt * 150 ;

                String cardNumber = cardNumberEditText.getText().toString().trim();
                String cardExpiry = cardExpiryEditText.getText().toString().trim();
                String cardCvv = cardCvvEditText.getText().toString().trim();

                if (!isValidCardNumber(cardNumber)) {
                    cardNumberEditText.setError("Invalid card number");
                    return;
                }

                if (!isValidCardExpiry(cardExpiry)) {
                    cardExpiryEditText.setError("Invalid card expiry date (MM/YY)");
                    return;
                }

                if (!isValidCardCvv(cardCvv)) {
                    cardCvvEditText.setError("Invalid CVV");
                    return;
                }

                // Display the calculated amount
                //Toast.makeText(PaymentActivity.this, "Total Amount: $" + totalAmount, Toast.LENGTH_SHORT).show();

                // Update the payment amount in the Firebase Realtime Database

                DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings").child(bookingId);
                bookingRef.child("paymentAmount").setValue(totalAmount);

                // Display a payment success message (for demonstration purposes)
                Toast.makeText(PaymentActivity.this, "Payment Successful!", Toast.LENGTH_SHORT).show();

                // navigate back to the previous activity or perform other actions here
            }
        });
    }
    // Credit card number validation using Luhn algorithm
    private boolean isValidCardNumber(String cardNumber) {
      /*  if (cardNumber.length() != 16) {
            Toast.makeText(PaymentActivity.this, "Enter a valid Card Number", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        boolean isValid = sum % 10 == 0;

        if (isValid) {
            Toast.makeText(PaymentActivity.this, "Valid Card Number", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentActivity.this, "Invalid Card Number", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }


    // Credit card expiry date validation (MM/YY format)
    private boolean isValidCardExpiry(String cardExpiry) {
        // Implement validation for MM/YY format
        // Return true if valid, false otherwise
        // Example implementation:
        // Pattern pattern = Pattern.compile("(0[1-9]|1[0-2])/[0-9]{2}");
        // return pattern.matcher(cardExpiry).matches();
        Pattern pattern = Pattern.compile("^(0[1-9]|1[0-2])/[0-9]{2}$");

        return pattern.matcher(cardExpiry).matches();

    }

    // CVV validation (3 or 4 digits)
    private boolean isValidCardCvv(String cardCvv) {
        // Implement validation for 3 or 4-digit CVV
        // Return true if valid, false otherwise
        // Example implementation:
        Pattern pattern = Pattern.compile("[0-9]{3}");
        return pattern.matcher(cardCvv).matches();

    }
}
