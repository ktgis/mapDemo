package com.kt.maps.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.util.GMapKeyManager;

import java.util.LinkedHashMap;


public class MultiLanguageActivity extends Activity
        implements OnMapReadyListener {

    private GMap gMap;

    private final LinkedHashMap<String, Runnable> MENU_ITEMS = new LinkedHashMap<String, Runnable>() {
        {
            put("한국어", new Runnable() {
                @Override
                public void run() {
                    gMap.setLanguage(GMap.Language.KOREAN);
                }
            });
            put("English", new Runnable() {
                @Override
                public void run() {
                    gMap.setLanguage(GMap.Language.ENGLISH);
                }
            });
            put("中文", new Runnable() {
                @Override
                public void run() {
                    gMap.setLanguage(GMap.Language.CHINESE);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.multi_language_demo);

        setupMap();
        setupLayout();
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.gMap = gMap;
    }

    private void setupMap() {
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    private void setupLayout() {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item,
                MENU_ITEMS.keySet().toArray(new String[MENU_ITEMS.size()])));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Runnable) MENU_ITEMS.values().toArray()[position]).run();
                drawerLayout.closeDrawer(drawerList);
            }
        });

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
