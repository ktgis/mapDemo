package com.kt.mapdemo.demo.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.kt.mapdemo.R;
import com.kt.mapdemo.databinding.ActivityMapFragmentBinding;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;


public class MapFragmentActivity extends AppCompatActivity implements OnMapReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);


        GMapFragment fragment = (GMapFragment) getFragmentManager().findFragmentById(R.id.gMapFragment);
        // MapReady Listener
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        // Map Object
        Toast.makeText(getApplicationContext(),"MapReady", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }
}
