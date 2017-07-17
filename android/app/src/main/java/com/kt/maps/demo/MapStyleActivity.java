package com.kt.maps.demo;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMap.OnIdleListener;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.internal.RenderScheduler.OnFpsListener;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.util.GMapKeyManager;

public class MapStyleActivity extends Activity implements OnMapReadyListener,
        OnViewpointChangeListener, OnIdleListener, OnFpsListener {

    private GMap gMap;
    private ImageView compassNeedle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private final LinkedHashMap<String, Runnable> ITEMS = new LinkedHashMap<String, Runnable>() {
        {
            put("일반", new Runnable() {
                @Override
                public void run() {
                    gMap.setStyle(ResourceDescriptorFactory
                            .fromResource(R.raw.com_kt_maps_style));
                    gMap.setSyetemImage(ResourceDescriptorFactory
                            .fromResource(R.drawable.com_kt_maps_totalimage),
                            ResourceDescriptorFactory.fromResource(R.raw.com_kt_maps_totalimage));
                }
            });

            put("주간주행", new Runnable() {
                @Override
                public void run() {
                    gMap.setStyle(ResourceDescriptorFactory.fromResource(R.raw.day_drive));
                    gMap.setSyetemImage(ResourceDescriptorFactory
                            .fromResource(R.drawable.com_kt_maps_totalimage),
                            ResourceDescriptorFactory.fromResource(R.raw.com_kt_maps_totalimage));
                }
            });

            put("야간주행", new Runnable() {
                @Override
                public void run() {
                    gMap.setStyle(ResourceDescriptorFactory.fromResource(R.raw.night_drive));
                    gMap.setSyetemImage(ResourceDescriptorFactory
                            .fromResource(R.drawable.new_totalimage),
                            ResourceDescriptorFactory.fromResource(R.raw.new_totalimage));
                }
            });

            put("테두리없이", new Runnable() {
                @Override
                public void run() {
                    gMap.setStyle(ResourceDescriptorFactory.fromResource(R.raw.borderless));
                    gMap.setSyetemImage(ResourceDescriptorFactory
                            .fromResource(R.drawable.com_kt_maps_totalimage),
                            ResourceDescriptorFactory.fromResource(R.raw.com_kt_maps_totalimage));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_style);
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, ITEMS
                .keySet().toArray(new String[0])));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Runnable) ITEMS.values().toArray()[position]).run();
                drawerLayout.closeDrawer(drawerList);
            }

        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_launcher,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        compassNeedle = (ImageView) findViewById(R.id.compass_needle);
        compassNeedle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gMap != null) {
                    gMap.animate(ViewpointChange.rotateTo(0));
                }
            }
        });
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.gMap = gMap;
        gMap.setOnViewpointChangeListener(this);
        gMap.setOnIdleListener(this);
        gMap.getRenderScheduler().setOnFpsListener(this);
    }

    public void onZoomIn(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomIn());
    }

    public void onZoomOut(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomOut());
    }

    @Override
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        compassNeedle.setRotation(-viewpoint.rotation);
        compassNeedle.setRotationX(viewpoint.tilt * 1.5f);

    }

    @Override
    public void fps(final float fps) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final TextView tv = (TextView) findViewById(R.id.fps_info);
                tv.setText(String.format("%.1ffps", fps));
            }
        });
    }

    @Override
    public void onIdle(GMap map) {
        float zoom = gMap.getViewpoint().zoom;
        ((TextView) findViewById(R.id.mapInfo)).setText(String.format("zoom: %.1f", zoom));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}