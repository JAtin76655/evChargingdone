package com.example.evcharging;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends Fragment {

    private TextView userEmailTextView;
    private TextView userFirstNameTextView;
    private TextView userLastNameTextView;
    private  TextView fullNameTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find the "Log Out" button by its ID
        Button logoutButton = view.findViewById(R.id.logoutButton);

        // Find TextViews for user email, first name, and last name by their IDs
        userEmailTextView = view.findViewById(R.id.userEmailTextView);
        userFirstNameTextView = view.findViewById(R.id.userFirstNameTextView);
        userLastNameTextView = view.findViewById(R.id.userLastNameTextView);
        fullNameTextView = view.findViewById(R.id.fullNameTextView);

        // Get the currently logged-in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is logged in
        if (user != null) {
            // Get the user's email address
            String userEmail = user.getEmail();

            // Set the user's email in the TextView
            userEmailTextView.setText("User Email: " + userEmail);

            // Get the user's unique ID
            String userId = user.getUid();

            // Fetch additional user data (first name and last name) from the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data from the database
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);

                        // Calculate the full name
                        String fullName = firstName + " " + lastName;
                        // Set user data in the TextViews
                        userFirstNameTextView.setText("First Name: " + firstName);
                        userLastNameTextView.setText("Last Name: " + lastName);
                        fullNameTextView.setText("" +fullName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that occur during data retrieval
                }
            });
        }

        // Set a click listener for the "Log Out" button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the logout method to sign the user out
                logoutUser();

                // Navigate back to the login screen
                navigateToLoginScreen();
            }
        });

        return view;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // Sign the user out
    }

    private void navigateToLoginScreen() {
        // Create an Intent to start the LoginActivity (replace with your login activity class)
        Intent intent = new Intent(getActivity(), Login.class);

        // Clear the back stack, so the user cannot navigate back to the profile fragment
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Start the LoginActivity
        startActivity(intent);

        // Optionally, you can finish the current activity (profile) to remove it from the back stack
        getActivity().finish();
    }
}
