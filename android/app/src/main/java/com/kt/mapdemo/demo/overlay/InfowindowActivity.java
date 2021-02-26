package com.kt.mapdemo.demo.overlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kt.geom.model.Coord;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.overlay.InfoWindow;
import com.kt.maps.overlay.InfoWindowOptions;

import java.util.ArrayList;
import java.util.List;

/*
 * InfoWindow : 정보를 갖는 view를 지도위에 표출 하는 오버레
 */
public class InfowindowActivity extends BaseActivity
        implements OnMapReadyListener, GMap.OnMapTapListener, InfoWindow.InfoWindowAdapter {

    // custom infowindow type
    private final int CustomInfoWindowType = 1001;

    private GMap mapObj;

    private List<InfoWindow> infoWindowList;

    enum InfoWindowType {
        Custom(1001),
        Default(1000);

        int value;

        InfoWindowType(int value) {
            this.value = value;
        }
    }

    private InfoWindowType currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infowindow);

        ((GMapFragment) getFragmentManager().findFragmentById(R.id.infowindowOverlay)).setOnMapReadyListener(this);
        currentType = InfoWindowType.Default;
    }

    @Override
    public int getMenuLayout() {
        return R.menu.info_window_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.makeCustomInfoWindow:
                currentType = InfoWindowType.Custom;
                break;
            case R.id.makeDefaultInfoWindow:
                currentType = InfoWindowType.Default;
                break;
            case R.id.clearInfoWindow:
                clearInfoWindows();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;
        // set infowindow adapter
        this.mapObj.setInfoWindowAdapter(this);
        this.mapObj.setOnMapTapListener(this);
        infoWindowList = new ArrayList<>();
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }

    @Override
    public void onMapTap(GMap gMap, Coord coord) {
        // add info window overlay
        InfoWindow infoWindow = new InfoWindow(new InfoWindowOptions()
                .title("InfoWindowTitle")
                .position(coord)
        );
        infoWindow.tagInfo = currentType.value;
        infoWindowList.add(infoWindow);
        mapObj.addOverlay(infoWindow);
    }

    private void clearInfoWindows() {
        for (InfoWindow infoWindow : infoWindowList) {
            mapObj.removeOverlay(infoWindow);
        }
        infoWindowList.clear();
    }

    /**
     * {@link com.kt.maps.overlay.InfoWindow.InfoWindowAdapter} 정보성 view로 지도 위에 올라가게 될 view overlay
     * custom infowindow
     * @param gMap
     * @param infoWindow
     * @return
     */
    @Override
    public View getInfoWindowView(GMap gMap, InfoWindow infoWindow) {
        if (infoWindow.tagInfo == CustomInfoWindowType) {
            // make CustomView
            View customView = getLayoutInflater().inflate(R.layout.view_info_window, null);
            /*
             * bind view
             */
            TextView title = (TextView) customView.findViewById(R.id.infoWindowTitle);
            String titleText = "markerId : " + infoWindow.getId();
            title.setText(titleText);

            return customView;

        } else {
            return gMap.getDefaultInfoWindowAdapter().getInfoWindowView(gMap, infoWindow);
        }
    }
}
