package com.kt.maps.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.geom.model.Bounds;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.geom.model.UTMKBounds;
import com.kt.maps.overlay.GroundOverlay;
import com.kt.maps.overlay.GroundOverlayOptions;
import com.kt.maps.util.GMapKeyManager;

public class GroundOverlayActivity extends Activity implements OnMapReadyListener {

    private static final Bounds GROUNDOVERLAY_BOUNDS =
            new UTMKBounds(957590, 1941834, 959640, 1943858);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_fragment);
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        GroundOverlay groundOverlay = new GroundOverlay(new GroundOverlayOptions()
                .bounds(GROUNDOVERLAY_BOUNDS)
                .image(ResourceDescriptorFactory.fromAsset("c00000180.png")));
        gMap.addOverlay(groundOverlay);
    }


    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
