package com.kt.mapdemo.demo.plugin.symol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.kt.geom.model.UTMK;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.model.GMapLabelInfo;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.maps.util.GMapLabelOption;

public class LabelTapActivity extends BaseActivity
        implements OnMapReadyListener , GMap.OnMapLabelInfoListener, GMap.OnMapLabelInfoLongPressListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_tap);

        ((GMapFragment)getFragmentManager().findFragmentById(R.id.labelTapMap)).setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        // 지도의 스타일 지정.
        gMap.setStyle(ResourceDescriptorFactory.fromResource(R.raw.day_default_add_style));
        // 지도의 주기 아이콘 지정.
        gMap.setSyetemImage(ResourceDescriptorFactory.fromResource(R.drawable.day_style_icon), ResourceDescriptorFactory.fromResource(R.raw.day_style_icon));
        // 지도의 label(symbol)아이콘을 tap 할시 받는 callback
        gMap.setOnMapLabelInfoListener(this);
        // 지도의 label(symbol)아이콘을 long press 할시 받는 callback
        gMap.setOnMapLabelInfoLongPressListener(this);
        // label(symbol) 선택시 지정 할 이벤트.
        // Mode Description
        // Magnification : 확대. ( 배율 설정 가능) * ratio 설정 가능.
        // None : 아무 변화 없음.
        gMap.setGMapLabelFocusOption(new GMapLabelOption().setMode(GMapLabelOption.FocusMode.Magnification));
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }

    private void showToast(GMapLabelInfo label , String pressType) {
        UTMK pos = UTMK.valueOf(label.position);
        String text = "labelName : " + label.labelName + "\nlabel Id : "+ label.labelId + "\nposition : {" + pos.x + ", " + pos.y +"}\npressType " + pressType;
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * if return false then do not call map tap
     *
     * @param gMap          map object
     * @param gMapLabelInfo label info
     * @return handle map tap event
     */
    @Override
    public boolean onMapLabelInfo(GMap gMap, GMapLabelInfo gMapLabelInfo) {
        showToast(gMapLabelInfo, "onMapLabelInfo");
        return false;
    }

    /**
     * if return false then do not call map long press
     *
     * @param gMap          map object
     * @param gMapLabelInfo label info
     * @return handle map long press event
     */
    @Override
    public boolean onMapLabelInfoLongPress(GMap gMap, GMapLabelInfo gMapLabelInfo) {
        showToast(gMapLabelInfo, "onMapLabelInfoLongPress");
        return false;
    }
}