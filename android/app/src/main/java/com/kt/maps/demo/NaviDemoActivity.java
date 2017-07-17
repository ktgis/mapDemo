/*
 * Copyright (c) 2014 kt corp. All rights reserved.
 *
 * This is a proprietary software of kt corp, and you may not use this file
 * except in compliance with license agreement with kt corp. Any redistribution
 * or use of this software, with or without modification shall be strictly
 * prohibited without prior written approval of kt corp, and the copyright
 * notice above does not evidence any actual or intended publication of such
 * software.
 */
package com.kt.maps.demo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kt.maps.GMap;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.geom.model.LatLng;
import com.kt.geom.model.UTMK;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.util.GMapKeyManager;

public class NaviDemoActivity extends Activity implements OnMapReadyListener,
        OnViewpointChangeListener, ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, SensorEventListener {

    private static final Viewpoint INITIAL_VIEWPOINT = new Viewpoint(new LatLng(37.478788111314614,
            127.011623276644), 8, 0, 0);
    private GMap gMap;
    // private LocationClient locClient;
    private GoogleApiClient client;
    private LocationRequest locRequest;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    float[] mGravity;
    float[] mGeomagnetic;

    private static String TAG = "NaviDemoActivity";

    private float rotationDegree = 0.0f;
    private ImageView locationTrackToggle;
    private Marker locationMarker;

    private long eventTimestamp;
    private boolean markerAttached = false;
    private boolean trackingLocation = false;
    private Viewpoint prevViewpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_demo);

        initOllehMap();
        // create api client
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        // request periodic location update
        locRequest = LocationRequest.create();
        // set as high accuracy mode.
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // set interval as 1sec.
        locRequest.setInterval(1000);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // geoMagneticSensor =
        // sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        locationTrackToggle = (ImageView) findViewById(R.id.toggle_location_tracking);

        locationTrackToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "button clicked!!");
                if (trackingLocation) {
                    // turn off location tracking
                    turnOffTracking();
                } else {
                    // turn on location tracking
                    turnOnTracking();

                }
            }
        });
    }

    private void initOllehMap() {
        GMapFragment fragment = (GMapFragment) getFragmentManager().findFragmentById(
                R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // connect google api client
        if (client != null) {
            client.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (client.isConnected()) {
            turnOffTracking();
            client.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void turnOnTracking() {
        if (client != null && client.isConnected()) {
            // request periodic location update
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locRequest, this);
            // register accelerometer and magnetometer to retrieve device
            // orientation
            sensorManager.registerListener(NaviDemoActivity.this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(NaviDemoActivity.this, magnetometer,
                    SensorManager.SENSOR_DELAY_GAME);

            NaviDemoActivity.this.locationTrackToggle.setImageResource(R.drawable.btn_current_on);

            // set map's center with retrieved last location.
            Location location = LocationServices.FusedLocationApi.getLastLocation(client);
            if (location == null) {
                Toast.makeText(this, "Can't retrieve Location data. Please check your settings.",
                        Toast.LENGTH_SHORT).show();
                turnOffTracking();
                return;
            }
            LatLng lastLoc = new LatLng(location.getLatitude(), location.getLongitude());
            ViewpointChange initvp = ViewpointChange.builder().panTo(lastLoc)
                    .rotateTo(rotationDegree).build();

            gMap.animate(initvp, 500);

            // show marker
            locationMarker.setPosition(lastLoc);
            // attach location marker on map
            if (!markerAttached) {
                gMap.addOverlay(locationMarker);
                markerAttached = true;
            }
            // change toggle button image
            NaviDemoActivity.this.locationTrackToggle.setImageResource(R.drawable.btn_current_on);
            trackingLocation = true;
        }
    }

    private void turnOffTracking() {
        if (client != null && client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
        // locationMarker.setVisible(false);
        // change toggle button image
        NaviDemoActivity.this.locationTrackToggle.setImageResource(R.drawable.btn_current_off);
        // stop listening to sensor change
        sensorManager.unregisterListener(NaviDemoActivity.this);

        // rotate map to 0 degree (exact north)
        // ViewpointChange rotateVpc = ViewpointChange.builder()
        // .rotateTo(0)
        // .build();

        // gMap.animate(rotateVpc, 500);
        trackingLocation = false;
    }

    @Override
    public void onMapReady(GMap gMap) {
        // TODO Auto-generated method stub
        if (gMap == null) {
            Log.e(TAG, "Fail to get olleh map !");
        } else {
            Log.d(TAG, "Success to get olleh map !");
            gMap.moveTo(INITIAL_VIEWPOINT);
            gMap.setOnViewpointChangeListener(this);
            this.gMap = gMap;

            MarkerOptions options = new MarkerOptions()
                    .icon(ResourceDescriptorFactory.fromResource(R.drawable.an_mycar01_gps01))
                    .flat(true).visible(false);
            locationMarker = new Marker(options);
        }
    }

    @Override
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        if (gesture
                && prevViewpoint != null
                && (!viewpoint.center.equals(prevViewpoint.center) || viewpoint.rotation != prevViewpoint.rotation)) {
            turnOffTracking();
        }
        this.prevViewpoint = viewpoint;
    }

    // implementation of OnConnectionFailedListener interface
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub
        Toast.makeText(
                this,
                String.format("Connection failed[%d] to Google Play Services.",
                        result.getErrorCode()), Toast.LENGTH_SHORT).show();
    }

    // implementation of ConnectionCallbacks interface
    @Override
    public void onConnected(Bundle arg0) {
        // Display the connection status
        Toast.makeText(this, "Connected to Google Play Services.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location loc) {

        float bearing = loc.getBearing();
        UTMK utmkLoc = UTMK.valueOf(new LatLng(loc.getLatitude(), loc.getLongitude()));
        Log.d(TAG, String.format("Updated location in UTMK:[%f, %f], bearing:%f", utmkLoc.x,
                utmkLoc.y, bearing));

        Viewpoint vp = new Viewpoint(utmkLoc, gMap.getViewpoint().zoom,
                gMap.getViewpoint().tilt, rotationDegree);

        locationMarker.setPosition(utmkLoc);
        gMap.moveTo(vp);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        // Don't rotate map if event fired too shortly after previous event
        if (eventTimestamp > 0l && (event.timestamp - eventTimestamp) < (long) 200 * 1000 * 1000) {
            return;
        }

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                // orientation contains: azimuth, pitch and roll. we use
                // azimuth.
                float angle = (float) Math.toDegrees(orientation[0]);
                if (Math.abs(rotationDegree - angle) < 1.0f) {
                    return;
                }
                rotationDegree = angle;
                Viewpoint current = gMap.getViewpoint();
                Viewpoint vp = new Viewpoint(current.center, current.zoom, current.tilt,
                        rotationDegree);
                gMap.moveTo(vp);
            }
            eventTimestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Disconnected to Google Play Services.", Toast.LENGTH_SHORT).show();
        // trackingLocation = false;
        // change toggle button image
        NaviDemoActivity.this.locationTrackToggle.setImageResource(R.drawable.btn_current_off);
        gMap.removeOverlay(locationMarker);

        // rotate map to 0 degree (exact north)
        ViewpointChange rotateVpc = ViewpointChange.builder().rotateTo(0).build();

        gMap.animate(rotateVpc, 1000);
    }

    public void onZoomIn(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomIn());
    }

    public void onZoomOut(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomOut());
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
