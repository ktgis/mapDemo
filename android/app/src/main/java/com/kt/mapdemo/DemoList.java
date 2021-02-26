package com.kt.mapdemo;

import android.widget.LinearLayout;

import com.kt.mapdemo.base.BaseModel;
import com.kt.mapdemo.adapter.DemoModel;
import com.kt.mapdemo.adapter.HeaderModel;
import com.kt.mapdemo.demo.camera.MapCameraActivity;
import com.kt.mapdemo.demo.camera.MapGestureActivity;
import com.kt.mapdemo.demo.overlay.InfowindowActivity;
import com.kt.mapdemo.demo.overlay.MapShapeOverlayActivity;
import com.kt.mapdemo.demo.overlay.MarkerActivity;
import com.kt.mapdemo.demo.overlay.PathOverlayActivity;
import com.kt.mapdemo.demo.plugin.cluster.MarkerClusterMapActivity;
import com.kt.mapdemo.demo.plugin.location.LocationMapActivity;
import com.kt.mapdemo.demo.plugin.style.DynamicStyleMapActivity;
import com.kt.mapdemo.demo.plugin.symol.LabelTapActivity;
import com.kt.mapdemo.demo.plugin.traffic.TrafficLayerActivity;
import com.kt.mapdemo.demo.start.MapCoordinationActivity;
import com.kt.mapdemo.demo.start.MapFragmentActivity;
import com.kt.mapdemo.demo.start.MapFragmentCodeActivity;
import com.kt.mapdemo.demo.start.MapViewActivity;
import com.kt.mapdemo.demo.style.MapLayerActivity;
import com.kt.mapdemo.demo.style.MapStyleActivity;

import java.util.ArrayList;
import java.util.List;

public class DemoList {
    private static List<BaseModel> startList() {
        List<BaseModel> testList = new ArrayList<BaseModel>();
        testList.add(new HeaderModel("START"));
        testList.add(new DemoModel("map view", "add map(view)", MapViewActivity.class));
        testList.add(new DemoModel("map fragment", "add map(fragment)", MapFragmentActivity.class));
        testList.add(new DemoModel("map fragment", "add map(programmatic)", MapFragmentCodeActivity.class));
        testList.add(new DemoModel("map coordination", "map coordination", MapCoordinationActivity.class));
        return testList;
    }

    private static List<BaseModel> styleList() {
        List<BaseModel> testList = new ArrayList<BaseModel>();
        testList.add(new HeaderModel("Style"));
        testList.add(new DemoModel("Style Setting", "edit map style", MapStyleActivity.class));
        testList.add(new DemoModel("Layer Setting", "edit map layer", MapLayerActivity.class));
        return testList;
    }

    private static List<BaseModel> overlayList() {
        List<BaseModel> testList = new ArrayList<BaseModel>();
        testList.add(new HeaderModel("Overlay"));
        testList.add(new DemoModel("Shape Overlay","add shape overlay", MapShapeOverlayActivity.class));
        testList.add(new DemoModel("Marker Overlay","add marker overlay", MarkerActivity.class));
        testList.add(new DemoModel("InfoWindow Overlay","add info window overlay", InfowindowActivity.class));
        testList.add(new DemoModel("Path Overlay","add path overlay", PathOverlayActivity.class));
        return testList;
    }

    private static List<BaseModel> pluginList() {
        List<BaseModel> testList = new ArrayList<BaseModel>();
        testList.add(new HeaderModel("Plugin"));
        testList.add(new DemoModel("Traffic","Traffic state in map", TrafficLayerActivity.class));
        testList.add(new DemoModel("Dynamic Style","edit map style", DynamicStyleMapActivity.class));
        testList.add(new DemoModel("Label Tap","Label tap event", LabelTapActivity.class));
        testList.add(new DemoModel("Location","Location tracking", LocationMapActivity.class));
        testList.add(new DemoModel("Marker Cluster","Marker overlay clustering", MarkerClusterMapActivity.class));
        return testList;
    }

    private static List<BaseModel> cameraList() {
        List<BaseModel> testList = new ArrayList<BaseModel>();
        testList.add(new HeaderModel("Camera"));
        testList.add(new DemoModel("Camera","animate map camera", MapCameraActivity.class));
        testList.add(new DemoModel("Camera Gesture","gesture callback", MapGestureActivity.class));
        return testList;
    }

    public static List<BaseModel> makeTestModel() {
        List<BaseModel> models=  new ArrayList<>();
        models.addAll(startList());
        models.addAll(styleList());
        models.addAll(overlayList());
        models.addAll(pluginList());
        models.addAll(cameraList());
        return models;
    }
}
