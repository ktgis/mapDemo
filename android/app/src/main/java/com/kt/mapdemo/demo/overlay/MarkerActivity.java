package com.kt.mapdemo.demo.overlay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kt.geom.model.Coord;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerCaptionOptions;
import com.kt.maps.overlay.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/*
지도 위에 Marker Overlay를 표출.
 */
public class MarkerActivity extends BaseActivity
        implements OnMapReadyListener , GMap.OnMapTapListener {

    enum MarkerDemoType {
        AddMarker,
        MoveMarker,
        CaptionMarker,
        AnimationMarker,
        FlatMarker
    }
    private List<Marker> markerList;
    private GMap mapObj;
    private MarkerDemoType currentDemoType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        ((GMapFragment) getFragmentManager().findFragmentById(R.id.markerOverlay)).setOnMapReadyListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.markerAdd:
                currentDemoType = MarkerDemoType.AddMarker;
                clearMarkers();
                break;
            case R.id.markerMove:
                currentDemoType = MarkerDemoType.MoveMarker;
                clearMarkers();
                addMarkerInMapCenter();
                break;
            case R.id.markerCaption:
                currentDemoType = MarkerDemoType.CaptionMarker;
                clearMarkers();
                addCaptionMarker();
                break;
            case R.id.markerAnimation:
                currentDemoType = MarkerDemoType.AnimationMarker;
                clearMarkers();
                addMarkerInMapCenter();
                break;
            case R.id.markerFlat:
                currentDemoType = MarkerDemoType.FlatMarker;
                clearMarkers();
                break;
            case R.id.markerClear:
                clearMarkers();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMenuLayout() {
        return R.menu.marker_menu;
    }

    /**
     * {@link OnMapReadyListener}
     */
    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;
        this.mapObj.setOnMapTapListener(this);
        markerList = new ArrayList<>();
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    /**
     * {@link com.kt.maps.GMap.OnMapTapListener}
     * @param gMap      mapObject
     * @param coord     tap point in map
     */
    @Override
    public void onMapTap(GMap gMap, Coord coord) {
        switch (currentDemoType) {
            case AddMarker:
                addMarker(coord);
                break;
            case MoveMarker:
                moveMarker(coord);
                break;
            case FlatMarker:
                addFlatMarker(coord);
                break;
            case AnimationMarker:
                animateMarker();
                break;
        }
    }


    private void clearMarkers() {
        for (Marker marker : markerList) {
            marker.hideInfoWindow();
            mapObj.removeOverlay(marker);
        }
    }

    /*
    특정 위치의 마커를 추가.
     */
    private void addMarker(Coord position) {
        Marker marker = new Marker(new MarkerOptions().position(position));
        markerList.add(marker);
        mapObj.addOverlay(marker);
    }

    /*
    Marker에 애니메이션을 줄수 있고, 해당 Animation은
    FLICK   : 잠시 커졌다가 다시 돌아오는 애니메이션 (강조)
    DROP    : 떨어지는 애니메이션
    이상 2가지가 있다.
     */
    private void animateMarker() {
        for (Marker marker : markerList) {
            marker.animate(Marker.AnimationType.FLICK);
        }
    }

    // 표출되고 있는 지도의 중심점에 Marker Overlay를 생성한다.
    private void addMarkerInMapCenter() {
        Marker marker = new Marker(new MarkerOptions().position(
                mapObj.getViewpoint().center
        ));

        markerList.add(marker);
        mapObj.addOverlay(marker);
    }

    /*
    Marker의 position을 변경하면, Marker의 표출되는 위치가 변경 된다.
    Coord : 지도위의 좌표로 Utmk, Latlng, Katech등이 있다.
     */
    private void moveMarker(Coord position) {
        for (Marker marker : markerList) {
            marker.setPosition(position);
        }
    }

    /**
    Marker에 caption을 줘서 정보를 추가로 정보를 표출 할 수 있다.
     Caption의 위치는 {@link com.kt.maps.overlay.MarkerCaptionOptions.PositionType}의 타입으로 정의한다.
     */
    private void addCaptionMarker(){
        Marker captionMarker = new Marker(new MarkerOptions()
                .position(mapObj.getViewpoint().center)
                .captionOptions(new MarkerCaptionOptions()
                        .setTitle("Marker Caption")
                        .setColor(Color.MAGENTA)
                        .setPositionType(MarkerCaptionOptions.PositionType.LEFT)
                )
                .title("Marker")
        );

        markerList.add(captionMarker);
        mapObj.addOverlay(captionMarker);
    }

    /*
    FlatMarker
    일반 Marker Overlay 는 화면의 기울기가 발생하여도, 그 형태가 유지 되지만, Flat option을 적용한 Marker는 지도의 기울기를 반영하여
    Marker의 이미지를 기울인다.
     */
    private void addFlatMarker(Coord coord) {
        Marker marker = new Marker(new MarkerOptions().flat(true).position(coord));
        markerList.add(marker);
        mapObj.addOverlay(marker);
    }
}
