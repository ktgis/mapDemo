package com.kt.mapdemo.demo.start;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.kt.geom.model.Coord;
import com.kt.geom.model.Katech;
import com.kt.geom.model.LatLng;
import com.kt.geom.model.UTMK;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;

public class MapCoordinationActivity extends BaseActivity
        implements OnMapReadyListener, GMap.OnMapTapListener {
    private GMap mapObj;
    private TextView infoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_coordination);

        infoView = (TextView) findViewById(R.id.coordinationInfo);

        // map initialize
        ((GMapFragment)getFragmentManager().findFragmentById(R.id.coordinationMap)).setOnMapReadyListener(this);
    }


    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;
        // TapGestureRecognizer Listener
        this.mapObj.setOnMapTapListener(this);
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    @Override
    public void onMapTap(GMap gMap, Coord coord) {
        // utmk 좌표계 변환.
        UTMK utmk = UTMK.valueOf(coord);
        // LatLng 좌표계 변환.
        LatLng latLng = LatLng.valueOf(coord);
        // Katech 좌표계 변환.
        Katech katech = Katech.valueOf(coord);

        showInfo("onMapTap",
                "{"+utmk.x + "," +utmk.y+"}",
                "{"+latLng.lat + "," +latLng.lng+"}",
                "{"+katech.x + "," +katech.y+"}");
    }

    private void showInfo(String eventType,
                          String utmkInfo,
                          String latLngInfo,
                          String katechInfo){
        String info = "eventType" + eventType + "\n" +
                "utmk : "+ utmkInfo + "\n" +
                "latLng : "+ latLngInfo + "\n" +
                "katech : "+katechInfo ;
        infoView.setText(info);
    }
}