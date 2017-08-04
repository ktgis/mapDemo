package com.kt.maps.demo;

import java.util.LinkedHashMap;

import com.kt.maps.GMap;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.geom.model.UTMK;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.util.GMapKeyManager;

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
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class LayersActivity extends Activity implements OnMapReadyListener,
        OnViewpointChangeListener {
    private final LinkedHashMap<String, Runnable> ITEMS = new LinkedHashMap<String, Runnable>() {
        {
            put("저레벨 배경", new Runnable() {

                @Override
                public void run() {
                    gMap.setLowLevelLayerVisible(!gMap.isLowLevelLayerVisible());
                }

            });
            put("배경", new Runnable() {

                @Override
                public void run() {
                    gMap.setBackgroundLayerVisible(!gMap.isBackgroundLayerVisible());
                }

            });
            put("도로", new Runnable() {

                @Override
                public void run() {
                    gMap.setRoadLayerVisible(!gMap.isRoadLayerVisible());
                }

            });
            put("네트워크", new Runnable() {

                @Override
                public void run() {
                    gMap.setNetworkLayerVisible(!gMap.isNetworkLayerVisible());
                }

            });
            put("빌딩", new Runnable() {

                @Override
                public void run() {
                    gMap.setBuildingLayerVisible(!gMap.isBuildingLayerVisible());
                }

            });
            put("저레벨 주기", new Runnable() {

                @Override
                public void run() {
                    gMap.setLowLevelLabelLayerVisible(!gMap.isLowLevelLabelLayerVisible());
                }

            });
            put("주기", new Runnable() {

                @Override
                public void run() {
                    gMap.setLabelLayerVisible(!gMap.isLabelLayerVisible());
                }

            });
            put("오버레이", new Runnable() {

                @Override
                public void run() {
                    gMap.setOverlayLayerVisible(!gMap.isOverlayLayerVisible());
                }

            });

        }
    };

    private GMap gMap;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ImageView compassNeedle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
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
                    gMap.animate(ViewpointChange.builder().rotateTo(0).tiltTo(0).build());
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GMap map) {
        this.gMap = map;
        UTMK center = new UTMK(964665, 1946082);
        map.moveTo(new Viewpoint(center, 11, 65, 0));
        map.addOverlay(new Marker(new MarkerOptions().position(center)));

        gMap.setOnViewpointChangeListener(this);
        gMap.setBuilding3dMinZoom(11);
        gMap.setGTrafficLayerAdaptor(new TrafficAdaptor(), getApplicationContext());
        gMap.setLowLevelLayerVisible(false);
        gMap.setBackgroundLayerVisible(false);
        gMap.setRoadLayerVisible(false);
        gMap.setBuildingLayerVisible(false);
        gMap.setLowLevelLabelLayerVisible(false);
        gMap.setLabelLayerVisible(false);
        gMap.setOverlayLayerVisible(false);
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
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        compassNeedle.setRotation(-viewpoint.rotation);
        compassNeedle.setRotationX(viewpoint.tilt * 1.5f);
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
