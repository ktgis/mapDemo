/*
 *  Copyright (c) 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file
 *  except in compliance with license agreement with kt corp. Any redistribution
 *  or use of this software, with or without modification shall be strictly
 *  prohibited without prior written approval of kt corp, and the copyright
 *   notice above does not evidence any actual or intended publication of such
 *  software.
 *
 */

package com.kt.maps.demo;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.geom.model.Coord;

import com.kt.geom.model.UTMK;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.util.GMapKeyManager;

import java.util.Random;


public class ImageMapActivity extends Activity implements OnMapReadyListener, GMap.OnMapLongpressListener{
    private final String TAG = "ImageMapActivity";

    private GMap gMap;
    private boolean isAnimate;

    private Marker marker;
    private int btnClickCount = 0;
    private int longPressedCount = 0;

    private TextView tvTestInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_map);
        initComponent();
    }

    private void initComponent () {
        GMapFragment fragment = (GMapFragment) getFragmentManager().findFragmentById(
                R.id.gmap_image);
        fragment.setOnMapReadyListener(this);

        Button btnRandomMarker = (Button) findViewById(R.id.btnRandomMarker);
        tvTestInfo = (TextView) findViewById(R.id.tvTestInfo);



        btnRandomMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UTMK location =getRandomPosistion();
                int randomZoom = makeRandom(0, 14);
                btnClickCount ++;

                addMarker(location);
                changeViewPointWithZoom(location, randomZoom);

                showTestInfo("Button Click",location, randomZoom);
            }
        });
    }

    @Override
    public void onMapReady(GMap gMap) {
        Toast.makeText(this, "On Map Ready" , Toast.LENGTH_SHORT).show();
        this.gMap = gMap;
        this.gMap.setOnMapLongpressListener(this);

    }




    @Override
    public void onFailReadyMap(GMapResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongpress(GMap map, Coord location) {
        addMarker(location);
        longPressedCount ++;
        showTestInfo("long Press",(UTMK) location, map.getViewpoint().zoom);
    }

    public void onZoomIn(View view) {
        if (this.gMap == null) {
            return;
        }
        this.gMap.animate(ViewpointChange.builder().zoomBy(1.0).build());

    }

    public void onZoomOut(View view) {
        if (this.gMap == null) {
            return;
        }
        if (this.gMap == null) {
            return;
        }
        this.gMap.animate(ViewpointChange.builder().zoomBy(-1.0).build());
    }

    private UTMK getRandomPosistion (){
        double x = makeRandom(904472, 1092119);
        double y = makeRandom(1622969, 1980000);
        return new UTMK(x,y);
    }

    private int makeRandom(int min, int max) {
        int range = max - min;
        Random randomGenerator = new Random();

        return randomGenerator.nextInt(range) + min;
    }

    private void addMarker(Coord location){
        if ( marker != null ){
            this.gMap.removeOverlay(marker);
        }
        MarkerOptions options = new MarkerOptions().position(location);
        Marker marker = new Marker(options);

        this.marker = marker;
        this.gMap.addOverlay(marker);

        if (isAnimate) {
            /** Marker animation */
            marker.animate(Marker.AnimationType.DROP);
            isAnimate = false;
        } else {
            isAnimate = true;
        }
    }

    private void changeViewPointWithZoom (Coord location, int zoom){
        ViewpointChange.Builder viewpointChange = new ViewpointChange.Builder();
        viewpointChange.panTo(location).zoomTo(zoom);

        this.gMap.animate(viewpointChange.build(), 4000);
    }

    private void showTestInfo (String testType, UTMK location, float zoom ){
        String info = String.format("testType : %s\n" +
                "button click count : %d\n" +
                "longPress count :%d\n" +
                "location : (%.1f, %.1f)\n" +
                "zoom : %.1f",
                testType, btnClickCount, longPressedCount, location.x, location.y, zoom);

        tvTestInfo.setText(info);
    }
}
