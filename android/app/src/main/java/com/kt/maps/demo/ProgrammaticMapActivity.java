package com.kt.maps.demo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.MapOptions;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.internal.util.Logger;
import com.kt.geom.model.UTMK;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.util.GMapKeyManager;

public class ProgrammaticMapActivity extends Activity implements OnMapReadyListener,
        OnViewpointChangeListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        MapOptions options = new MapOptions();
        options.viewpoint(new Viewpoint(Constants.올레스퀘어, 11));
        GMapFragment fragment = new GMapFragment(options);
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        Viewpoint viewpoint = gMap.getViewpoint();
        Log.d("ProgrammaticMapActivity", "center: " + viewpoint.center);

        gMap.setOnViewpointChangeListener(this);
    }

    @Override
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        UTMK center = UTMK.valueOf(viewpoint.center);
        Logger.d("ProgrammaticMapActivity",
                "ViewpointChanged. viewpoint={center: ({},{}), zoom: {}, tilt: {}, rotation: {}, gesture: {}}",
                center.x, center.y, viewpoint.zoom, viewpoint.tilt, viewpoint.rotation, gesture);
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
