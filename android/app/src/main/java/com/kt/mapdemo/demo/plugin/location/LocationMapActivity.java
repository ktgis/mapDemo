package com.kt.mapdemo.demo.plugin.location;

import
        androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kt.geom.model.Coord;
import com.kt.geom.model.LatLng;
import com.kt.geom.model.UTMK;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;

public class LocationMapActivity extends BaseActivity
        implements OnMapReadyListener, GMap.OnViewpointChangeListener {
    enum LocationTrackingMode {
        GPS,
        Compass,
        None
    }

    private final int RequestPermissionCode = 10001;
    private GMap mapObj;
    private ImageView compassNeedle;

    private Marker locationMarker;
    private LocationTrackingMode currentTrackingMode = LocationTrackingMode.None;
    private MyLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        ((GMapFragment) getFragmentManager().findFragmentById(R.id.locationMap)).setOnMapReadyListener(this);
        compassNeedle = (ImageView) findViewById(R.id.sampleMapCompass);

        locationMarker = new Marker(new MarkerOptions());
        locationManager = new MyLocationManager(this);
        locationManager.setLocationHandler(handler);

        requestPermissions();
    }

    private void requestPermissions(){
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        requestPermissions(permissions, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getMenuLayout() {
        return R.menu.location_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location_compass:
                currentTrackingMode = LocationTrackingMode.Compass;
                locationManager.stopSensor();
                locationManager.startLocationUpdate();
                break;
            case R.id.location_gps:
                currentTrackingMode = LocationTrackingMode.GPS;
                locationManager.stopLocationUpdate();
                locationManager.startSensor();
                break;
            case R.id.location_none:
                currentTrackingMode = LocationTrackingMode.None;
                locationManager.stopLocationUpdate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 정북방향으로 지도를 돌리고, 기울기를 0도로 변경함.
    public void onCompassToNorth(View view) {
        this.mapObj.animate(ViewpointChange.builder().tiltTo(0).rotateTo(0).build());
    }

    public void onAnimateLastLocation(View view) {
        locationManager.getLastLocation();
    }

    /**
     *  {@link OnMapReadyListener}
     */

    @Override
    public void onMapReady(GMap gMap) {
        gMap.setOnViewpointChangeListener(this);
        this.mapObj = gMap;
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {
    }

    /**
     * {@link com.kt.maps.GMap.OnViewpointChangeListener}
     */
    @Override
    public void onViewpointChange(GMap gMap, Viewpoint viewpoint, boolean b) {
        // 기울기
        float tiltDegree = viewpoint.tilt;
        // 화면의 방위각.
        float rotDegree = viewpoint.rotation;
        compassNeedle.setRotation(-rotDegree);
        compassNeedle.setRotationX(tiltDegree);

        if (tiltDegree == 0 && rotDegree == 0) {
            compassNeedle.setVisibility(View.GONE);
        } else {
            compassNeedle.setVisibility(View.VISIBLE);
        }
    }

    //Location Handler
    private LocationHandler handler = new LocationHandler() {
        // 좌표가 변할 시 들어옴.
        @Override
        public void didRecivedLocation(Location location, LocationRequestState state) {
            switch (state) {
                case LastLocation:
                    mapObj.animate(ViewpointChange.builder()
                            .panTo(coordFromLocation(location))
                            .zoomTo(12)
                            .build()
                    );
                    locationMarker.setPosition(coordFromLocation(location));
                    if (!locationMarker.isAdded()) {
                        mapObj.addOverlay(locationMarker);
                    }

                    break;
                case UpdateLocation:
                    mapObj.moveTo(
                            new Viewpoint(coordFromLocation(location), 12)// 0, location.getBearing())
                    );
                    locationMarker.setPosition(coordFromLocation(location));
                    if (!locationMarker.isAdded()) {
                        mapObj.addOverlay(locationMarker);
                    }
                    break;
            }
        }

        // 방위각이 변경될 시 들어옴.
        @Override
        public void didRecivedSensor(double azimuth, double pitch, double roll) {
            ViewpointChange vc = ViewpointChange.builder().rotateTo(azimuth).build();
            mapObj.animate(vc);
        }
    };


    // Location -> Coord (좌표계) 변환 함수.
    private Coord coordFromLocation(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }


}