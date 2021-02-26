package com.kt.mapdemo.demo.style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;

/*
 * 지도의 Layer들의 표출여부를 정할 수 있다.
 * Layer 구족
 * 1. LowLevelLayer         // 저레벨 배경
 * 2. BackgroundLayer       // 배경
 * 3. BuildingLayer         // 건물
 * 4. NetworkLayer          // 교통정보 라인.
 * 5. LowLevelLabelLayer    // 저레벨 주기
 * 6. LabelLayer            // 주기
 * 7. OverlayLayer
 */
public class MapLayerActivity extends BaseActivity implements OnMapReadyListener {
    private GMap mapObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_layer);

        GMapFragment mapFragment = (GMapFragment) getFragmentManager().findFragmentById(R.id.mapLayer);
        mapFragment.setOnMapReadyListener(this);
    }


    @Override
    public void onMapReady(GMap gMap) {
        mapObj = gMap;
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    @Override
    public int getMenuLayout() {
        return R.menu.layer_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backgroundVisible:
                mapObj.setBackgroundLayerVisible(!mapObj.isBackgroundLayerVisible());
                break;
            case R.id.buildingVisible:
                mapObj.setBuildingLayerVisible(!mapObj.isBuildingLayerVisible());
                break;
            case R.id.labelVisible:
                mapObj.setLabelLayerVisible(!mapObj.isLabelLayerVisible());
                break;
            case R.id.networkVisible:
                mapObj.setNetworkLayerVisible(!mapObj.isNetworkLayerVisible());
                break;
            case R.id.lowBackgroundVisible:
                mapObj.setLowLevelLayerVisible(!mapObj.isLowLevelLayerVisible());
                break;
            case R.id.lowLabelVisible:
                mapObj.setLowLevelLabelLayerVisible(!mapObj.isLowLevelLabelLayerVisible());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
