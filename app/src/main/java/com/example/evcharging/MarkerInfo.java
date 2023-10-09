package com.example.evcharging;

public class MarkerInfo {
    public double latitude;
    public double longitude;
    public String title;

    public MarkerInfo() {
        // Default constructor required for Firebase
    }

    public MarkerInfo(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }
}
