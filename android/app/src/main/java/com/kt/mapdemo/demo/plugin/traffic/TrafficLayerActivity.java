package com.kt.mapdemo.demo.plugin.traffic;


import android.os.Bundle;

import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.GMapShared;
import com.kt.maps.OnMapReadyListener;

public class TrafficLayerActivity extends BaseActivity implements OnMapReadyListener {
    private GMap mapObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_layer);

        ((GMapFragment)getFragmentManager().findFragmentById(R.id.trafficLayerMap)).setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;

        // Custom Adapter를 설정 안하게 되면, SDK내부의 기본 adapter로 색상을 매칭하게 된다.
        this.mapObj.setGTrafficLayerAdaptor(new CustomTrafficAdapter(), getApplicationContext());
        this.mapObj.setNetworkLayerVisible(true);
        // 도로 종별에 따른 요청이 아닌 제한 속도를 요청시에는 해당 함수를 호출해야
        // GMapShared.getInstance(getApplicationContext()).getTrafficManager()을 통한 링크별 속도정보를 정확히 얻을 수 있다.
        // 단) 도로 종별로 요청시에는 해당 함수를 호출 할 필요가 없다.
        GMapShared.getInstance(getApplicationContext()).getTrafficManager().setType(CustomTrafficAdapter.RoadRequestType);

    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }

}