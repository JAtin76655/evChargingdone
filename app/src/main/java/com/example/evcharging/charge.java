package com.example.evcharging;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class charge extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap myMap;
    private DatabaseReference databaseRef;
    private List<Marker> markerList = new ArrayList<>();
    private final LatLng desiredLocation = new LatLng(22.3072, 73.1812); // Specify the desired location coordinates


    public charge() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("markers");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Set an OnMarkerClickListener to handle marker clicks
        myMap.setOnMarkerClickListener(this);

        float zoomLevel = 13f; // Specify your desired zoom level
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(desiredLocation, zoomLevel));

        // Listen for changes in the Firebase database and add markers to the map
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                MarkerInfo markerInfo = dataSnapshot.getValue(MarkerInfo.class);

                if (markerInfo != null) {
                    LatLng markerLatLng = new LatLng(markerInfo.latitude, markerInfo.longitude);
                    Marker marker = myMap.addMarker(new MarkerOptions().position(markerLatLng).title(markerInfo.title));
                    marker.setTag(dataSnapshot.getKey()); // Store the database key as a tag for the marker
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle marker data changes if needed
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Handle marker removal if needed
                String markerKey = dataSnapshot.getKey();
                Marker marker = findMarkerByTag(markerKey);
                if (marker != null) {
                    marker.remove();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle marker movement if needed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });

        // Create a demo dataset in Firebase if needed
        createDemoDataset();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Handle marker click event here
        // You can use marker.getTitle() to get the title of the clicked marker
        // For example, display a toast with the marker title
        Toast.makeText(getContext(), "Marker Clicked: " + marker.getTitle(), Toast.LENGTH_SHORT).show();

        // Save marker information to Firebase
        saveMarkerToFirebase(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude);

        Intent intent = new Intent(getActivity(), BookingActivity.class);
        startActivity(intent);
        focusOnMarker(marker);
        return true; // Return true to consume the click event
    }

    private void saveMarkerToFirebase(String title, double latitude, double longitude) {
        // Generate a unique key for the marker
        String markerKey = databaseRef.push().getKey();

        // Create a MarkerInfo object and save it to Firebase
        MarkerInfo markerInfo = new MarkerInfo(latitude, longitude, title);
        databaseRef.child(markerKey).setValue(markerInfo);
    }
    private void focusOnMarker(Marker marker) {
        if (myMap != null && marker != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(marker.getPosition()) // Set the target to the marker's position
                    .zoom(13f) // Set the desired zoom level
                    .build();

            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
    private void createDemoDataset() {
        // You can create a demo dataset in Firebase by manually adding markers
        // Here's an example of how to add a marker to the database:
        // String demoMarkerKey = "demoMarker1";
        //  MarkerInfo demoMarkerInfo = new MarkerInfo(22.298737, 73.189231, "EvCity");
        // databaseRef.child(demoMarkerKey).setValue(demoMarkerInfo);
    }

    private Marker findMarkerByTag(String tag) {
        for (Marker marker :markerList) {
            if (tag.equals(marker.getTag())) {
                return marker;
            }
        }
        return null;
    }
}
