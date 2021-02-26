package com.kt.mapdemo;

import android.app.Application;

import com.kt.maps.util.GMapKeyManager;

public class MapDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 발급 받은 키 등록
        setGisKey();
    }


    /**
     * set GIS Key
     */
    private void setGisKey() {
        // 발급 받은 키.
        GMapKeyManager.getInstance().init(getApplicationContext(), MapConstValue.GisKey);
    }
}
