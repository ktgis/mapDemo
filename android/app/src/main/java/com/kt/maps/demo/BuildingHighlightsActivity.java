package com.kt.maps.demo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

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
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.GMap.OnIdleListener;
import com.kt.maps.GMap.OnMapLongpressListener;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.geom.model.Coord;
import com.kt.geom.model.UTMK;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.util.GMapKeyManager;

public class BuildingHighlightsActivity extends Activity implements OnMapReadyListener,
OnViewpointChangeListener, OnIdleListener, OnMapLongpressListener {
    private GMap gMap;
    private ImageView compassNeedle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    
    private Stack<Coord> buildingFocusStack;
    private List<Coord> focusPointList;
    
    
    private final LinkedHashMap<String, Runnable> ITEMS = new LinkedHashMap<String, Runnable>() {
        {
            put("Highlights - AddList", new Runnable() {
                @Override
                public void run() {
                    gMap.addBuildingFocuses(focusPointList);
                }
            });
            put("Highlights - RemoveOne", new Runnable() {
                @Override
                public void run() {
                    if (buildingFocusStack.size() > 0) {
                        gMap.removeBuildingFocus(buildingFocusStack.pop());
                    }
                }
            });
            put("Highlights - RemoveList", new Runnable() {
                @Override
                public void run() {
                    gMap.removeBuildingFocuses(focusPointList);
                }
            });
            put("Highlights - ClearAll", new Runnable() {
                @Override
                public void run() {
                    gMap.clearBuildingFocuses();
                }
            });
            put("3D - TurnOn", new Runnable() {
                @Override
                public void run() {
                    gMap.setBuilding3dEnabled(true);
                }
            });
            put("3D - TurnOff", new Runnable() {
                @Override
                public void run() {
                    gMap.setBuilding3dEnabled(false);
                }
            });
            put("3D - MinZoom12", new Runnable() {
                @Override
                public void run() {
                    gMap.setBuilding3dMinZoom(12);
                }
            });
            put("3D - MinZoom14", new Runnable() {
                @Override
                public void run() {
                    gMap.setBuilding3dMinZoom(14);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_highlights);
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                ITEMS.keySet().toArray(new String[0])));
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
        
        buildingFocusStack = new Stack<Coord>();
        focusPointList = new ArrayList<Coord>();
        focusPointList.add(new UTMK(956811, 1942342)); //예술의 전당 음악
        focusPointList.add(new UTMK(957014, 1942348)); //문화개발원
        focusPointList.add(new UTMK(956543, 1942158)); //국림국악원
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.gMap = gMap;
        gMap.setOnViewpointChangeListener(this);
        gMap.setOnIdleListener(this);
        gMap.setOnMapLongpressListener(this);
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
    public void onIdle(GMap map) {
        float zoom = gMap.getViewpoint().zoom;
        ((TextView) findViewById(R.id.mapInfo)).setText(String.format("zoom: %.1f", zoom));
    }

    @Override
    public void onMapLongpress(GMap map, Coord location) {
        //add building focus for location
        this.gMap.addBuildingFocus(location);
        buildingFocusStack.add(location);
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
