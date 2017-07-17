package com.kt.maps.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMap.OnMapLongpressListener;
import com.kt.maps.GMap.OnOverlayTapListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.geom.model.Coord;
import com.kt.geom.model.UTMK;
import com.kt.maps.overlay.InfoWindow;
import com.kt.maps.overlay.InfoWindow.InfoWindowAdapter;
import com.kt.maps.overlay.InfoWindowOptions;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.overlay.Overlay;
import com.kt.maps.util.GMapKeyManager;

public class InfoWindowActivity extends Activity implements OnMapReadyListener,
        OnMapLongpressListener, OnOverlayTapListener, InfoWindowAdapter {

    private GMap gMap;

    private static final UTMK OLLEH_CAMPUS = new UTMK(957085.70, 1944017.52);
    private static final UTMK SEOCHO_IC = new UTMK(958094.06, 1942790.83);
    private static final UTMK CUSTOM = new UTMK(959021, 1942696);

    private Marker ollehCampus;
    private Marker seochoIc;
    private Marker gasStation;
    private InfoWindow gasStationInfoWindow;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_fragment);
        GMapFragment fragment = (GMapFragment) getFragmentManager().findFragmentById(
                R.id.gmap);
        fragment.setOnMapReadyListener(this);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @Override
    public void onMapReady(GMap gMap) {

        this.gMap = gMap;

        gMap.setOnMapLongpressListener(this);
        gMap.setOnOverlayTapListener(this);
        gMap.setInfoWindowAdapter(this);

        // 마커 추가
        ollehCampus = new Marker(new MarkerOptions().position(OLLEH_CAMPUS).title("default")
                .draggable(true).subTitle(OLLEH_CAMPUS.toString()));
        seochoIc = new Marker(new MarkerOptions().position(SEOCHO_IC).title("draggable")
                .draggable(true));
        gasStation = new Marker(new MarkerOptions().position(CUSTOM).title("1500").draggable(true));

        gMap.addOverlay(ollehCampus);
        gMap.addOverlay(seochoIc);
        gMap.addOverlay(gasStation);

    }

    private void showMessage(String message) {
        toast.cancel();
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onOverlayTap(GMap map, Overlay overlay) {
        if (overlay.equals(gasStation)) {
            if (gasStationInfoWindow == null) {
                InfoWindowOptions options = new InfoWindowOptions().position(
                        gasStation.getPosition()).title(gasStation.getTitle());
                gasStationInfoWindow = new InfoWindow(options);
            }
            gMap.removeOverlay(gasStation);
            gMap.addOverlay(gasStationInfoWindow);
        } else if (overlay.equals(gasStationInfoWindow)) {
            gMap.removeOverlay(gasStationInfoWindow);
            gMap.addOverlay(gasStation);
        } else if (overlay instanceof InfoWindow) {
            showMessage("InfoWindow clicked");
            if (!overlay.equals(map.getInfoWindow())) {
                gMap.removeOverlay(overlay);
            }
        } else if (overlay instanceof Marker) {
            showMessage("Marker clicked");
            return false;
        }
        return true;
    }

    @Override
    public void onMapLongpress(GMap map, Coord location) {
        showMessage("InfoWindow added");

        InfoWindowOptions options = new InfoWindowOptions().position(location).title("overlay")
                .subTitle(location.toString());
        InfoWindow infoWindow = new InfoWindow(options);
        gMap.addOverlay(infoWindow);
    }

    @Override
    public View getInfoWindowView(GMap map, InfoWindow infoWindow) {

        if (infoWindow.equals(gasStationInfoWindow)) { // custom infowindow
            String title = infoWindow.getTitle();

            if (title == null) {
                return null;
            }

            final View view = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.gas);

            TextView titleView = ((TextView) view.findViewById(R.id.title));
            SpannableString titleText = new SpannableString(title);
            titleView.setText(titleText);
            return view;
        }
        return map.getDefaultInfoWindowAdapter().getInfoWindowView(map, infoWindow);
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
