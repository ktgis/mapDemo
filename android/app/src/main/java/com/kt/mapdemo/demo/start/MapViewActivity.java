package com.kt.mapdemo.demo.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.kt.mapdemo.R;
import com.kt.maps.GMap;
import com.kt.maps.GMapResultCode;
import com.kt.maps.GMapView;
import com.kt.maps.OnMapReadyListener;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        GMapView mapView = findViewById(R.id.gMapView);
        // OnMapReady Listener
        mapView.setOnMapReadyListener(this);
    }



    @Override
    public void onMapReady(GMap gMap) {
        Toast.makeText(getApplicationContext(),"MapReady", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }
}
