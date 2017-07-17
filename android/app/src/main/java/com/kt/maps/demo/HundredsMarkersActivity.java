package com.kt.maps.demo;

import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.GMap.OnIdleListener;
import com.kt.maps.internal.util.Logger;
import com.kt.geom.model.Coord;
import com.kt.maps.model.Point;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.overlay.Overlay;
import com.kt.maps.util.GMapKeyManager;

public class HundredsMarkersActivity extends Activity implements OnMapReadyListener , OnIdleListener {

    Marker marker1;
    private GMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.gMap = gMap;
        gMap.setOnIdleListener(this);
        int width = gMap.getView().getWidth();
        int height = gMap.getView().getHeight();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Point point = new Point(width * i * 0.1, height * j * 0.1);
                Coord coord = gMap.getCoordFromViewportPoint(point);
                if (coord == null)
                    break;
                Marker marker = new Marker(new MarkerOptions().position(coord));

                gMap.addOverlay(marker);
                if (i == 5 && j == 5) {
                    marker.setIcon(ResourceDescriptorFactory.fromAsset("blue_icon.png"));
                    marker1 = marker;
                }
            }
        }
    }

    public void onZoomIn(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomIn(), 300);
        marker1.resetZIndex();
    }

    public void onZoomOut(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomOut(), 300);
        marker1.bringToFront();
    }

    @Override
    public void onIdle(GMap map) {
        Collection<Overlay> overlays = map.getVisibleOverlays();
        Logger.d("HundredsMarkersActivity", "visible count: {}", overlays.size());
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
