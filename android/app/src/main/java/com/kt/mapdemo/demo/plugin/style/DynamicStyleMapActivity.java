package com.kt.mapdemo.demo.plugin.style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.kt.gmaputils.style.GMapStyleManager;
import com.kt.gmaputils.style.commands.StyleApplyCommand;
import com.kt.gmaputils.style.model.GMapPolygonModel;
import com.kt.gmaputils.style.model.GMapStyleColor;
import com.kt.gmaputils.style.model.GMapStyleModel;
import com.kt.gmaputils.style.model.GMapSystemImage;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;

public class DynamicStyleMapActivity extends BaseActivity
        implements OnMapReadyListener, SeekBar.OnSeekBarChangeListener {
    private GMap mapObj;

    private GMapStyleManager mapStyleManager;
    private GMapStyleModel model;

    private int currentEditPaneNumber;

    private SeekBar seekBarR, seekBarG, seekBarB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_style_map);

        ((GMapFragment)getFragmentManager().findFragmentById(R.id.dynamicStyleMap)).setOnMapReadyListener(this);

        seekBarR = (SeekBar) findViewById(R.id.colorRValue);
        seekBarG = (SeekBar) findViewById(R.id.colorGValue);
        seekBarB = (SeekBar) findViewById(R.id.colorBValue);

        seekBarR.setOnSeekBarChangeListener(this);
        seekBarG.setOnSeekBarChangeListener(this);
        seekBarB.setOnSeekBarChangeListener(this);
    }


    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;
        currentEditPaneNumber = -1;
        mapStyleManager = new GMapStyleManager(mapObj);
        /*
            Style load
            param style
            param systemIcon
            param systemIconInfo
         */
        StyleApplyCommand styleCommand = new StyleApplyCommand(R.raw.day_default_add_style, new GMapSystemImage(R.drawable.day_style_icon, R.raw.day_style_icon));
        mapStyleManager.execute(styleCommand);
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
         * 변경한 이력이 있을시 이전에 지도 혹은 이후에 지도에 영향을 줄 수 있기에 해당 변경 이력을 reset 해야함.
         */
        mapStyleManager.resetStyle();
    }

    @Override
    public int getMenuLayout() {
        return R.menu.dynamic_style_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.num61Pane:
                currentEditPaneNumber = 61;
                // 1 : background layer
                model = mapStyleManager.getModel(1, currentEditPaneNumber);
                break;
            case R.id.num64Pane:
                currentEditPaneNumber = 64;
                // 1 : background layer
                model = mapStyleManager.getModel(1, currentEditPaneNumber);
                break;
            case R.id.resetStyle:
                if (currentEditPaneNumber != -1) {
                    mapStyleManager.resetStyle();
                    model = mapStyleManager.getModel(1, currentEditPaneNumber);
                }
                break;
        }
        setSeekBarValue();
        return super.onOptionsItemSelected(item);
    }

    private void setSeekBarValue() {
        int r = ((GMapPolygonModel) model).getFillColor().getR();
        int g = ((GMapPolygonModel) model).getFillColor().getG();
        int b = ((GMapPolygonModel) model).getFillColor().getB();

        seekBarR.setProgress(r);
        seekBarG.setProgress(g);
        seekBarB.setProgress(b);
    }

    private void applyStyle() {
        if(model == null) return;
        GMapStyleColor color = ((GMapPolygonModel) model).getFillColor();
        color.setR(seekBarR.getProgress());
        color.setG(seekBarG.getProgress());
        color.setB(seekBarB.getProgress());


        ((GMapPolygonModel) model).setFillColor(color);
        mapStyleManager.applyModels(model);
    }

    /*
    SeekBar Listener
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        applyStyle();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
}