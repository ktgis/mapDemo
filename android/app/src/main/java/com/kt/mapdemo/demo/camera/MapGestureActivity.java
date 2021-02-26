package com.kt.mapdemo.demo.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kt.geom.model.Coord;
import com.kt.geom.model.LatLng;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.model.GMapLabelInfo;
import com.kt.maps.model.Viewpoint;

public class MapGestureActivity extends BaseActivity
        implements OnMapReadyListener, GMap.OnMapTapListener,
        GMap.OnMapLongpressListener, GMap.OnViewpointChangeListener, GMap.OnMapLabelInfoListener, GMap.OnMapLabelInfoLongPressListener {
    private GMap mapObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_gesture);

        // map initialize
        ((GMapFragment)getFragmentManager().findFragmentById(R.id.cameraMap)).setOnMapReadyListener(this);
    }


    @Override
    public void onMapReady(GMap gMap) {
        mapObj = gMap;

        /*
         * gesture 들의 callback 을 받기 위해서는 다음과 같은 listener 들을 추가 해야 한다.
         */
        mapObj.setOnViewpointChangeListener(this);
        mapObj.setOnMapTapListener(this);
        mapObj.setOnMapLongpressListener(this);
        mapObj.setOnMapLabelInfoLongPressListener(this);
        mapObj.setOnMapLabelInfoListener(this);
    }

    @Override
    public int getMenuLayout() {
        return R.menu.gesture_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pan_gesture:
                mapObj.setPanGesturesEnabled(!mapObj.isPanGesturesEnabled());
                break;
            case R.id.rotate_gesture:
                mapObj.setRotateGesturesEnabled(!mapObj.isRotateGesturesEnabled());
                break;
            case R.id.tilt_gesture:
                mapObj.setTiltGesturesEnabled(!mapObj.isTiltGesturesEnabled());
                break;
            case R.id.zoom_gesture:
                mapObj.setZoomGesturesEnabled(!mapObj.isZoomGesturesEnabled());
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    @Override
    public void onMapLongpress(GMap gMap, Coord coord) {
        showToast("Map Long press Event");
    }

    @Override
    public void onMapTap(GMap gMap, Coord coord) {
        showToast("Map Tap Event");
    }

    /**
     *  PanGesture, TiltGesture, RotateGesture, ZoomGesture 는 {@link com.kt.maps.GMap.OnViewpointChangeListener} 로 응답이 내려온다.
     */
    @Override
    public void onViewpointChange(GMap gMap, Viewpoint viewpoint, boolean b) {
        LatLng latLng = LatLng.valueOf(viewpoint.center);
        showToast("onViewpointChange\n" +
                "lat, lng = {" + latLng.lat+ ", " + latLng.lng + "}\n" +
                "");
    }

    @Override
    public boolean onMapLabelInfo(GMap gMap, GMapLabelInfo gMapLabelInfo) {
        showToast("onMapLabelInfo\n" +
                "labelName : " + gMapLabelInfo.labelName+"\n" +
                "labelId : " + gMapLabelInfo.labelId);
        return false;
    }

    @Override
    public boolean onMapLabelInfoLongPress(GMap gMap, GMapLabelInfo gMapLabelInfo) {
        showToast("onMapLabelInfoLongPress\n" +
                "labelName : " + gMapLabelInfo.labelName+"\n" +
                "labelId : " + gMapLabelInfo.labelId);
        return false;
    }


    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}