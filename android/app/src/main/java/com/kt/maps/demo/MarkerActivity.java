package com.kt.maps.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.geom.model.LatLng;
import com.kt.maps.model.Point;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.geom.model.UTMK;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerCaptionOptions;
import com.kt.maps.overlay.MarkerCaptionOptions.PositionType;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.util.GMapKeyManager;

public class MarkerActivity extends Activity implements OnMapReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_fragment);
        GMapFragment fragment = (GMapFragment) getFragmentManager().findFragmentById(
                R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(37.509698681161424, 127.06790571433105))
                .icon(ResourceDescriptorFactory.fromAsset("blue_icon.png"))
                .captionOptions(new MarkerCaptionOptions().setTitle("draggable")).draggable(true)
                .visible(true);
        Marker marker = new Marker(options);
        marker.setIconSize(new Point(40, 40));
        marker.setTitle("markerId: " + marker.getId());
        gMap.addOverlay(marker);

        UTMK position = new UTMK(959640, 1943858);
        Marker dmarker = new Marker(new MarkerOptions().position(position).captionOptions(
                new MarkerCaptionOptions().setTitle("DEFAULT MARKER!")));
        dmarker.setTitle("markerId: " + dmarker.getId());
        dmarker.setIconSize(new Point(60,60));
        gMap.addOverlay(dmarker);

        Marker marker2 = new Marker(new MarkerOptions().position(position));
        marker2.setTitle("markerId: " + marker2.getId());
        marker2.setSubTitle(marker2.getPosition().toString());
        marker2.setAnchor(new Point(-2.0, -2.0));
        marker2.setCaption(new MarkerCaptionOptions().setTitle("TOP(ANCHOR(-2, -2))")
                .setStrokeColor(Color.BLACK).setColor(Color.MAGENTA).setSize(7).setPositionType(PositionType.TOP));
        gMap.addOverlay(marker2);

        Marker marker3 = new Marker(new MarkerOptions().position(position).captionOptions(
                new MarkerCaptionOptions().setTitle("LEFT(ANCHOR(2, 2))").setColor(Color.MAGENTA)
                        .setSize(4).setPositionType(PositionType.LEFT)));
        marker3.setTitle("markerId: " + marker3.getId());
        marker3.setAnchor(new Point(2.0, 2.0));
        marker3.setSubTitle(marker3.getPosition().toString());
        gMap.addOverlay(marker3);

        Marker marker4 = new Marker(new MarkerOptions().position(position));
        marker4.setTitle("markerId: " + marker4.getId());
        marker4.setSubTitle(marker4.getPosition().toString());
        marker4.setAnchor(new Point(2.0, -2.0));
        marker4.setCaption(new MarkerCaptionOptions().setTitle("BOTTOM(ANCHOR(2, -2))")
                .setColor(Color.MAGENTA).setSize(7).setPositionType(PositionType.BOTTOM));
        gMap.addOverlay(marker4);

        Marker marker5 = new Marker(new MarkerOptions().position(position).captionOptions(
                new MarkerCaptionOptions().setTitle("RIGHT(ANCHOR(-2, 2))").setColor(Color.MAGENTA)
                        .setSize(4).setPositionType(PositionType.RIGHT)));
        marker5.setTitle("markerId: " + marker5.getId());
        marker5.setAnchor(new Point(-2.0, 2.0));
        marker5.setSubTitle(marker5.getPosition().toString());
        gMap.addOverlay(marker5);
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}