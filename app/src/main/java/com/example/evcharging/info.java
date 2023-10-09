package com.example.evcharging;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.evcharging.MarkerInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class info extends Fragment {
    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button saveButton;
    private DatabaseReference databaseRef;

    public info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("markers");

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.editTextName);
        latitudeEditText = view.findViewById(R.id.editTextLatitude);
        longitudeEditText = view.findViewById(R.id.editTextLongitude);
        saveButton = view.findViewById(R.id.buttonSave);

        // Set a click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adding a new marker
                addNewMarkerToFirebase();
            }
        });

        return view;
    }

    private void addNewMarkerToFirebase() {
        // Get marker information from user input
        String name = nameEditText.getText().toString();
        double latitude = Double.parseDouble(latitudeEditText.getText().toString());
        double longitude = Double.parseDouble(longitudeEditText.getText().toString());

        // Create a MarkerInfo object with the new data
        MarkerInfo newMarker = new MarkerInfo(latitude, longitude, name);

        // Generate a unique key for the new marker
        String newMarkerKey = databaseRef.push().getKey();

        // Save the new marker information to Firebase
        databaseRef.child(newMarkerKey).setValue(newMarker);

        // Optionally, show a success message or navigate back to the map fragment
    }
}
