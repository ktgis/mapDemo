package com.kt.mapdemo.demo.plugin.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyLocationManager implements SensorEventListener {
    private final Activity mContext;

    private final FusedLocationProviderClient fusedLocationProviderClient;

    // LocationCallBack
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (locationHandler != null)
                    locationHandler.didRecivedLocation(location, LocationHandler.LocationRequestState.UpdateLocation);
            }
        }
    };

    private final LocationRequest locationRequest;
    private LocationHandler locationHandler;

    private final Object updateLock = new Object();
    private Boolean isUpdateProgress;

    public MyLocationManager(Activity mContext) {
        this(mContext, 1000);
    }

    public MyLocationManager(Activity mContext, int interval) {
        this.mContext = mContext;

        // Location init
        isUpdateProgress = false;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(interval)
                .setFastestInterval(interval / 2);

        // Sensor init
        sensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];

        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
    }

    ////////////////////////////////////////
    //  FusedLocationProviderClient Code
    ////////////////////////////////////////

    public void setLocationHandler(LocationHandler handler) {
        this.locationHandler = handler;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mContext, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (locationHandler != null)
                    locationHandler.didRecivedLocation(location, LocationHandler.LocationRequestState.LastLocation);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdate() {
        synchronized (updateLock) {
            if (isUpdateProgress) {
                stopLocationUpdate();
            }
            isUpdateProgress = true;
            fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        }
    }

    public void stopLocationUpdate() {
        synchronized (updateLock) {
            isUpdateProgress = false;
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    ////////////////////////////////
    // Sensor Code
    ////////////////////////////////
    SensorManager sensorManager;

    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;

    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;


    public void startSensor() {
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSensor() {
        sensorManager.unregisterListener(this, sensorAccelerometer);
        sensorManager.unregisterListener(this, sensorMagneticField);
    }

    /**
     *  {@link SensorEventListener}
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch(sensorEvent.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(sensorEvent.values, 0, valuesAccelerometer, 0, 3);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(sensorEvent.values, 0, valuesMagneticField, 0, 3);
                break;
        }

        boolean success = SensorManager.getRotationMatrix(
                matrixR,
                matrixI,
                valuesAccelerometer,
                valuesMagneticField);

        if(success){
            SensorManager.getOrientation(matrixR, matrixValues);

            double azimuth = Math.toDegrees(matrixValues[0]);
            double pitch = Math.toDegrees(matrixValues[1]);
            double roll = Math.toDegrees(matrixValues[2]);

            locationHandler.didRecivedSensor(azimuth, pitch, roll);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
