package com.kt.mapdemo.demo.style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.model.ResourceDescriptorFactory;

/*
 * 지도의 배경, 건물, 주기 등의 색상을 변경.
 */
public class MapStyleActivity extends BaseActivity implements OnMapReadyListener {
    private boolean isUseStyleAnimation = false;
    private GMap mapObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_style);

        GMapFragment mapFragment = (GMapFragment) getFragmentManager().findFragmentById(R.id.mapStyle);
        mapFragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        mapObj = gMap;
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    private enum StyleType {
        Default(R.raw.day_default_add_style),
        DarkDefault(R.raw.night_default_add_style),
        Drive(R.raw.day_drive_add_style),
        DarkDrive(R.raw.night_drive_add_style);

        int resId;
        StyleType(int resId){
            this.resId = resId;
        }
    }

    private void applyStyle(StyleType type) {

        if (isUseStyleAnimation) {
            // 스타일 변경시 이전 색상에서 바뀔 생각이 매끄럽게 변경.
            mapObj.animateStyle(ResourceDescriptorFactory.fromResource(type.resId),1000);
        } else {
            // 스타일 변경.
            mapObj.setStyleAndUpdateCurrentLayer(ResourceDescriptorFactory.fromResource(type.resId));
        }

        switch (type) {
            /*
             *  주간 스타일의 경우 주간 스타일 아이콘을 적용해 주면 더 매끄러운 아이콘이 나옴.
             */
            case Default:
            case Drive:
                mapObj.setSyetemImage(ResourceDescriptorFactory.fromResource(R.drawable.day_style_icon), ResourceDescriptorFactory.fromResource(R.raw.day_style_icon));
                break;
            case DarkDefault:
            case DarkDrive:
                mapObj.setSyetemImage(ResourceDescriptorFactory.fromResource(R.drawable.night_style_icon), ResourceDescriptorFactory.fromResource(R.raw.night_style_icon));
                break;
        }
    }
    //////////////////
    //  menu bar
    //////////////////

    @Override
    public int getMenuLayout() {
        return R.menu.style_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.defaultStyle:
                applyStyle(StyleType.Default);
                return true;
            case R.id.darkStyle:
                applyStyle(StyleType.DarkDefault);
                return true;
            case R.id.driveStyle:
                applyStyle(StyleType.Drive);
                return true;
            case R.id.darkDriveStyle:
                applyStyle(StyleType.DarkDrive);
                return true;
            case R.id.animationUse:
                isUseStyleAnimation = !isUseStyleAnimation;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
