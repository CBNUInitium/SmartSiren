package com.kms.smartsiren.Utility;

import android.location.Location;

public class LocationManager {
    private static LocationManager instance;
    private Location currentLocation;

    private LocationManager() {}

    public static synchronized LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}