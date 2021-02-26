package com.kt.mapdemo.demo.overlay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kt.geom.model.Coord;
import com.kt.geom.model.UTMK;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.overlay.Circle;
import com.kt.maps.overlay.CircleOptions;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.overlay.Overlay;
import com.kt.maps.overlay.Polygon;
import com.kt.maps.overlay.PolygonOptions;
import com.kt.maps.overlay.Polyline;
import com.kt.maps.overlay.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MapShapeOverlayActivity extends BaseActivity
        implements OnMapReadyListener, GMap.OnMapTapListener {

    private final int CIRCLE_MIN_COORD_SIZE = 2;
    private final int POLYGON_MIN_COORD_SIZE = 3;
    private final int POLYLINE_MIN_COORD_SIZE = 2;

    private GMap mapObj;

    private List<Coord> points;
    private int radius;
    private Overlay overlay;

    private List<Marker> positionMarkers;

    private enum OverlayType {
        Polygon,
        Polyline,
        Circle
    }

    private OverlayType currentDrawType = OverlayType.Polygon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_shape_overlay);

        ((GMapFragment) getFragmentManager().findFragmentById(R.id.shapeMapOverlay)).setOnMapReadyListener(this);
    }


    /*
        Map Ready Listener
     */
    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;
        points = new ArrayList<>();
        positionMarkers = new ArrayList<>();
        this.mapObj.setOnMapTapListener(this);

    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }


    /*
        Map tap Listener
     */

    /**
     * {@link com.kt.maps.GMap.OnMapTapListener}
     * @param gMap      map object
     * @param coord     tap position in map
     */
    @Override
    public void onMapTap(GMap gMap, Coord coord) {
        addPoint(coord);
    }


    @Override
    public int getMenuLayout() {
        return R.menu.shape_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.polygon:
                clearShapeOverlay();
                applyMenu(OverlayType.Polygon);
                break;
            case R.id.polyline:
                clearShapeOverlay();
                applyMenu(OverlayType.Polyline);
                break;
            case R.id.circle:
                clearShapeOverlay();
                applyMenu(OverlayType.Circle);
                break;
            case R.id.clearShape:
                clearShapeOverlay();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
        private function
     */
    private void clearShapeOverlay() {
        if (overlay != null) {
            mapObj.removeOverlay(overlay);
            for (Marker positionMarker : positionMarkers) {
                mapObj.removeOverlay(positionMarker);
            }
        }
        points.clear();
    }

    private void applyMenu(OverlayType type) {
        switch (type) {
            case Circle:
                currentDrawType = OverlayType.Circle;
                overlay = new Circle(new CircleOptions()
                        .radius(500)
                        .fillColor(Color.RED)
                );
                // Circle의 경우 origin이 필수로 있어야 하기에
                // addOverlay를 하기전에 addPoint를 해준다.
                addPoint(mapObj.getViewpoint().center);
                break;
            case Polygon:
                currentDrawType = OverlayType.Polygon;
                overlay = new Polygon(new PolygonOptions()
                        .fillColor(Color.RED));
                break;
            case Polyline:
                currentDrawType = OverlayType.Polyline;
                overlay = new Polyline(new PolylineOptions().color(Color.RED));
                break;
        }
        mapObj.addOverlay(overlay);
    }

    private void addPoint(Coord point) {
        points.add(point);
        switch (currentDrawType) {
            case Polyline:
                applyPolyline(point);
                break;
            case Polygon:
                applyPolygon(point);
                break;
            case Circle:
                applyCircle(point);
                break;
        }
    }

    private void addPositionMarker (Coord position) {
        Marker positionMarker = new Marker(new MarkerOptions().position(position));
        positionMarkers.add(positionMarker);
        mapObj.addOverlay(positionMarker);
    }

    /*
        Circle  Overlay
        Circle Overlay의 경우 최소 2개이상의 좌표가 있어야 형성이 가능함.
        point1 : 원의 중심저.
        point2 : 원의 반지름.
     */
    private void applyCircle (Coord position) {
        if (points.size() < CIRCLE_MIN_COORD_SIZE) {
            addPositionMarker(position);
            ((Circle) overlay).setOrigin(position);
            showToast(CIRCLE_MIN_COORD_SIZE, points.size());
        } else {
            if (!overlay.isAdded()) {
                mapObj.addOverlay(overlay);
            }
            UTMK center = (UTMK) ((Circle) overlay).getOrigin();
            double length = center.distanceTo(position);

            ((Circle) overlay).setRadius((int) length);
        }
    }

    /*
        Polygon Overlay
        polygon overlay의 경우 최소 3개의 좌표가 있어야 면형을 형성할 수 있다.
     */
    private void applyPolygon(Coord position) {
        addPositionMarker(position);
        if (points.size() < POLYGON_MIN_COORD_SIZE) {
            showToast(POLYGON_MIN_COORD_SIZE, points.size());
        } else {
            if (!overlay.isAdded()) {
                mapObj.addOverlay(overlay);
            }
            ((Polygon) overlay).setPoints(points);
        }
    }

    /*
        Polyline Overlay
        line overlay의 경우 최소 2개이상의 좌표가 있어야 형성이 가능함.
     */
    private void applyPolyline(Coord position) {
        addPositionMarker(position);
        if (points.size() < POLYLINE_MIN_COORD_SIZE) {
            showToast(POLYLINE_MIN_COORD_SIZE, points.size());
        } else {
            if (!overlay.isAdded()) {
                mapObj.addOverlay(overlay);
            }
            ((Polyline) overlay).setPoints(points);
        }
    }

    private void showToast (int minSize, int currentSize) {
        Toast.makeText(getApplicationContext(), (minSize - currentSize) + " 의 점이 더 필요 합니다.", Toast.LENGTH_SHORT).show();
    }
}
