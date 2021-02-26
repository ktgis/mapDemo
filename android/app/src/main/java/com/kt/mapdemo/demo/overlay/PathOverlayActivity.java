package com.kt.mapdemo.demo.overlay;

import androidx.annotation.NonNull;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.kt.geom.model.UTMK;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.overlay.Overlay;
import com.kt.maps.overlay.Path;
import com.kt.maps.overlay.PathOptions;
import com.kt.maps.overlay.RouteOverlay;
import com.kt.maps.overlay.RoutePath;
import com.kt.maps.overlay.RoutePathOptions;
import com.kt.maps.overlay.TrafficRouteLink;
import com.kt.maps.overlay.TrafficRouteLinkOptions;
import com.kt.maps.overlay.TrafficRoutePath;
import com.kt.maps.overlay.TrafficRoutePathOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PathOverlayActivity extends BaseActivity
        implements OnMapReadyListener, PathOverlayData {
    private final int SplitSizeOfPoint = 100;
    private final double DefaultPathBuffer = 5;
    private GMap mapObj;
    private SeekBar routeProgress;

    private Overlay pathOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_overlay);

        ((GMapFragment) getFragmentManager().findFragmentById(R.id.pathOverlayMap)).setOnMapReadyListener(this);

        routeProgress = (SeekBar) findViewById(R.id.routeProgress);
        routeProgress.setMax(points.length);
        routeProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i >= points.length) {
                    setSplitCoord(i - 1);
                } else {
                    setSplitCoord(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @Override
    public void onMapReady(GMap gMap) {
        this.mapObj = gMap;
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }

    @Override
    public int getMenuLayout() {
        return R.menu.path_overlay_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        removePath();
        switch (item.getItemId()) {
            case R.id.pathOverlay:
                routeProgress.setVisibility(View.GONE);
                addPathOverlay();
                break;
            case R.id.arrowPathOverlay:
                routeProgress.setVisibility(View.GONE);
                addArrowPathOverlay();
                break;
            case R.id.routePathOverlay:
                routeProgress.setVisibility(View.VISIBLE);
                addRoutePathOverlay();
                routeProgress.setProgress(0);
                break;
            case R.id.trafficRoutePathOverlay:
                routeProgress.setVisibility(View.VISIBLE);
                addTrafficRouteOverlay();
                routeProgress.setProgress(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removePath() {
        if (pathOverlay != null) {
            mapObj.removeOverlay(pathOverlay);
        }
    }

    /*
    선형 overlay
     */
    private void addPathOverlay() {
        pathOverlay = new Path(new PathOptions()
                .addPoints(getSubPointArray(0, 100))                        // Path의 선형 정보.
                .fillColor(Color.RED)                                       // Path의 색상.
                .bufferWidth(DefaultPathBuffer * mapObj.getResolution())    // Path의 두께.
        );
        mapObj.addOverlay(pathOverlay);
    }

    /*
    끝이 화살표로 되어있는 선형 overlay
     */
    private void addArrowPathOverlay() {
        pathOverlay = new Path(new PathOptions()
                .addPoints(getSubPointArray(0, 100))                        // Path의 선형 정보.
                .fillColor(Color.BLUE)                                      // Path의 색상.
                .hasArrow(true)                                             // Path의 끝이 화살표로 표출
                .bufferWidth(DefaultPathBuffer * mapObj.getResolution())    // Path의 두께.
        );
        mapObj.addOverlay(pathOverlay);
    }

    private void setSplitCoord(int index) {
        if(pathOverlay == null) return;

        if(pathOverlay instanceof RoutePath
                || pathOverlay instanceof TrafficRoutePath) {
            ((RouteOverlay) pathOverlay).setSplitCoord(points[index], index / SplitSizeOfPoint);
        }
    }

    private void addRoutePathOverlay() {
        pathOverlay = new RoutePath(new RoutePathOptions()
                .addPoints(points)
                .fillColor(Color.BLUE)
                .bufferWidth(DefaultPathBuffer * mapObj.getResolution())
        );

        mapObj.addOverlay(pathOverlay);
    }

    private void addTrafficRouteOverlay() {
        pathOverlay = new TrafficRoutePath(new TrafficRoutePathOptions()
                .addLinks(makeRouteLinkList())
                .bufferWidth(DefaultPathBuffer * mapObj.getResolution())
        );
        mapObj.addOverlay(pathOverlay);
    }

    private List<UTMK> getSubPointArray(int from, int to) {
        List<UTMK> point = Arrays.asList(points);
        return point.subList(from, to);
    }


    private List<TrafficRouteLink> makeRouteLinkList() {
        List<TrafficRouteLink> links = new ArrayList<>();
        for (int i = 0 , index = 0; i < points.length; i+= SplitSizeOfPoint, index++) {
            if (i + SplitSizeOfPoint >= points.length) {
                // last
                links.add(makeRouteLink(i, points.length-1, index));
            } else {
                links.add(makeRouteLink(i, i+ SplitSizeOfPoint, index));
            }
        }
        return links;
    }

    private TrafficRouteLink makeRouteLink(int from, int to, int linkId) {
        return new TrafficRouteLink(new TrafficRouteLinkOptions()
                .setColor(getRandomColor())
                .setLinkId(linkId)
                .addPoints(getSubPointArray(from, to))
        );
    }

    private int getRandomColor() {
        Random random = new Random();
        return colorList[random.nextInt(colorList.length)];
    }
}