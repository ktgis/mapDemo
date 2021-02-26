package com.kt.mapdemo.demo.plugin.location;

import android.location.Location;

public interface LocationHandler {
    public enum LocationRequestState {
        LastLocation, UpdateLocation
    }

    void didRecivedLocation(Location location, LocationRequestState state);
    void didRecivedSensor(double azimuth, double pitch, double roll);
}
