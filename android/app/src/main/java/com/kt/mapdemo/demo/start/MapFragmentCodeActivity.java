package com.kt.mapdemo.demo.start;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.MapOptions;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.model.Viewpoint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MapFragmentCodeActivity extends AppCompatActivity implements OnMapReadyListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        MapOptions options = new MapOptions();
        GMapFragment fragment = new GMapFragment(options);
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        Toast.makeText(getApplicationContext(),"MapReady", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }
}
