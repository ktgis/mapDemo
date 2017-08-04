package com.kt.maps.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapRoadType;
import com.kt.maps.GMapShared;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.util.GMapKeyManager;

@SuppressLint("UseSparseArrays")
public class TrafficInfoActivity extends Activity implements OnMapReadyListener,
        OnViewpointChangeListener {
    static final String TAG = "TrafficInfoActivity";
    private GMap gMap;
    private ImageView compassNeedle;
    private TextView mapInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);
        this.mapInfo = (TextView)findViewById(R.id.mapInfo);
        this.mapInfo.setVisibility(View.VISIBLE);
    }

    public void onZoomIn(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomIn(), 300);
    }

    public void onZoomOut(View view) {
        if (gMap != null) {
            gMap.animate(ViewpointChange.zoomOut(), 300);
        }
    }

    @Override
    public void onMapReady(GMap map) {
        this.gMap = map;
        // map.moveTo(new Viewpoint(Constants.예술의전당, 4));
        map.setGTrafficLayerAdaptor(new TrafficAdaptor(), getApplicationContext());

        map.setNetworkLayerVisible(true);

        compassNeedle = (ImageView) findViewById(R.id.compass_needle);
        compassNeedle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gMap != null) {
                    gMap.animate(ViewpointChange.builder().rotateTo(0).tiltTo(0).build());
                }
            }
        });
        mapInfo.setText("zoom: " + String.format("%.1f", map.getViewpoint().zoom));

    }

    @Override
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        compassNeedle.setRotation(-viewpoint.rotation);
        compassNeedle.setRotationX(viewpoint.tilt);
        mapInfo.setText("zoom: " + String.format("%.1f", viewpoint.zoom));
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
