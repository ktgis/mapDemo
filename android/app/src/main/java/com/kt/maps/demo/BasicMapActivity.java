/*
 * Copyright (c) 2014 kt corp. All rights reserved.
 *
 * This is a proprietary software of kt corp, and you may not use this file
 * except in compliance with license agreement with kt corp. Any redistribution
 * or use of this software, with or without modification shall be strictly
 * prohibited without prior written approval of kt corp, and the copyright
 * notice above does not evidence any actual or intended publication of such
 * software.
 */
package com.kt.maps.demo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMap.OnAnimationEndListener;
import com.kt.maps.GMap.OnIdleListener;
import com.kt.maps.GMap.OnMapLongpressListener;
import com.kt.maps.GMap.OnMapTapListener;
import com.kt.maps.GMap.OnOverlayDragListener;
import com.kt.maps.GMap.OnOverlayTapListener;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapShared;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.internal.RenderScheduler.OnFpsListener;
import com.kt.maps.internal.util.Logger;
import com.kt.geom.model.Bounds;
import com.kt.geom.model.Coord;
import com.kt.geom.model.Katech;
import com.kt.geom.model.KatechBounds;
import com.kt.geom.model.LatLng;
import com.kt.geom.model.LatLngBounds;
import com.kt.maps.model.Point;
import com.kt.geom.model.UTMK;
import com.kt.geom.model.UTMKBounds;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.model.VisibleRegion;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;
import com.kt.maps.overlay.Overlay;
import com.kt.maps.overlay.Path;
import com.kt.maps.overlay.PathOptions;
import com.kt.maps.overlay.RoutePath;
import com.kt.maps.overlay.RoutePathOptions;
import com.kt.maps.util.GMapKeyManager;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("unused")
public class BasicMapActivity extends Activity implements OnMapReadyListener, OnMapTapListener,
        OnViewpointChangeListener, OnMapLongpressListener, OnOverlayTapListener, OnIdleListener,
        OnFpsListener, OnOverlayDragListener, OnAnimationEndListener {
    private static final String TAG = "BasicMapActivity";
    private static final Viewpoint INITIAL_VIEWPOINT =
            new Viewpoint(new LatLng(37.478788111314614, 127.011623276644), 8, 0, 0);

    private static final Bounds 동작대교 =
            new LatLngBounds(37.50496396044253, 126.98003349536177,
                    37.51575873924737, 126.98335333044616);
    private static final Bounds 동작대교_Katech =
            new KatechBounds(new Katech(310018, 545248), new Katech(310325, 546443));
    private static int PATH_BUFFER_IN_DP = 5;

    private GMap gMap;
    private TextView mapInfoView;
    private ImageView compassNeedle;
    private Stack<Coord> buildingFocusStack;
    private int prevZoom;
    private int focusColorKey = 0;
    private RoutePath routePath;
    private boolean applyPoiPath;
    private Marker pivotMarker;

    private final LinkedHashMap<String, Runnable> ITEMS = new LinkedHashMap<String, Runnable>() {
        {
            put("toggle mapInfoView", new Runnable() {
                @Override
                public void run() {
                    if (mapInfoView.getVisibility() == View.VISIBLE) {
                        mapInfoView.setVisibility(View.INVISIBLE);
                    } else {
                        mapInfoView.setVisibility(View.VISIBLE);

                    }
                }
            });
            put("저레벨주기", new Runnable() {
                public void run() {
                    gMap.setLowLevelLabelLayerVisible(!gMap.isLowLevelLabelLayerVisible());
                }
            });


            put("교통정보", new Runnable() {
                @Override
                public void run() {
                    gMap.setNetworkLayerVisible(!gMap.isNetworkLayerVisible());
                }
            });
            put("reset", new Runnable() {
                @Override
                public void run() {
                    gMap.change(ViewpointChange.moveTo(INITIAL_VIEWPOINT));
                }
            });
            put("pan right top", new Runnable() {
                @Override
                public void run() {
                    int width = gMap.getView().getWidth();
                    int height = gMap.getView().getHeight();
                    ViewpointChange change = ViewpointChange.builder()
                            .panBy(-width, height)
                            .build();
                    measureFps(1000);
                    gMap.animate(change, 2000, GMap.AnimationTiming.DROP);
                }
            });
            put("pan right bottom", new Runnable() {
                @Override
                public void run() {
                    int width = gMap.getView().getWidth();
                    int height = gMap.getView().getHeight();
                    ViewpointChange change = ViewpointChange.builder()
                            .panBy(-width, -height)
                            .build();
                    measureFps(1000);
                    gMap.animate(change, 1000, GMap.AnimationTiming.SIN);
                }
            });
            put("pan left bottom", new Runnable() {
                @Override
                public void run() {
                    int width = gMap.getView().getWidth();
                    int height = gMap.getView().getHeight();
                    ViewpointChange change = ViewpointChange.builder()
                            .panBy(width, -height)
                            .build();
                    measureFps(1000);
                    gMap.animate(change, 1000, GMap.AnimationTiming.LINEAR);
                }
            });
            put("pan left top", new Runnable() {
                @Override
                public void run() {
                    int width = gMap.getView().getWidth();
                    int height = gMap.getView().getHeight();
                    ViewpointChange change = ViewpointChange.builder()
                            .panBy(width, height)
                            .build();
                    measureFps(1000);
                    gMap.animate(change, 1000);
                }
            });
            put("zoom in", new Runnable() {
                @Override
                public void run() {
                    measureFps(1000);
                    gMap.animate(ViewpointChange.zoomIn(), 1000);
                }
            });
            put("zoom out", new Runnable() {
                @Override
                public void run() {
                    measureFps(1000);
                    gMap.animate(ViewpointChange.zoomOut(), 1000);
                }
            });
            put("fitBounds(동작대교)", new Runnable() {
                @Override
                public void run() {
                    measureFps(1000);
                    ViewpointChange change = ViewpointChange.fitBounds(동작대교, 50);
                    gMap.animate(change, 1000);
                }
            });

            put("zoom to 0", new Runnable() {
                @Override
                public void run() {
                    ViewpointChange change = ViewpointChange.builder()
                            .zoomTo(0).build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });
            put("zoom to 14", new Runnable() {
                @Override
                public void run() {
                    ViewpointChange change = ViewpointChange.builder()
                            .zoomTo(14).build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });

            put("rotate(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .rotateBy(90).build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });
            put("zoom in(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .zoomBy(1).build();
                    measureFps(1000);
                    gMap.animate(change, 1000);
                }
            });
            put("zoom out(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .zoomBy(-1).build();
                    measureFps(1000);
                    gMap.animate(change, 1000);
                }
            });
            put("rotate+zoom(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .rotateBy(90).zoomBy(1).build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });
            put("pan by(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    Point panPoint = getViewportPoint(0.5, 0.5);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .panBy(0, pivot.y - panPoint.y)
                            .build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });
            put("panBy+rotate(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    Point panPoint = getViewportPoint(0.5, 0.5);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .panBy(0, pivot.y - panPoint.y)
                            .rotateBy(90).build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });
            put("pan to(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    Point panPoint = getViewportPoint(0.8, 0.5);
                    Coord coord = new UTMK(958541.25, 1942285.0);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .panTo(coord)
                            .build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });
            put("pan to + rotate(pivot)", new Runnable() {

                @Override
                public void run() {
                    if (pivotMarker == null) {
                        UTMK pivotCoord = (UTMK) gMap.getCoordFromViewportPoint(pivotPoint);
                        pivotMarker = new Marker(new MarkerOptions().position(pivotCoord));
                        gMap.addOverlay(pivotMarker);

                        Point panPoint = getViewportPoint(0.45, 0.9);
                        UTMK panCoord = (UTMK) gMap.getCoordFromViewportPoint(panPoint);
                        Marker marker = new Marker(new MarkerOptions().position(panCoord).title(
                                "Start"));
                        gMap.addOverlay(marker);

                        float bufferWidth = gMap.getResolution() * PATH_BUFFER_IN_DP;
                        Path path = new Path(new PathOptions()
                                .addPoints(
                                        gMap.getCoordFromViewportPoint(panPoint),
                                        gMap.getCoordFromViewportPoint(pivotPoint)
                                )
                                .fillColor(Color.RED)
                                .strokeColor(Color.BLACK)
                                .strokeWidth(1)
                                .bufferWidth(bufferWidth));
                        gMap.addOverlay(path);

                    }

                    Coord coord = gMap.getCoordFromViewportPoint(panPoint);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivotPoint)
                            .panTo(coord)
                            .rotateBy(-90)
                            .build();
                    gMap.animate(change, 5000);
                }
            });
            put("pan to + zoom(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    Point panPoint = getViewportPoint(0.8, 0.5);
                    Coord coord = new UTMK(958541.25, 1942285.0);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .panTo(coord)
                            .zoomBy(1)
                            .build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });

            put("pan to+rotate+zoom(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    Point panPoint = getViewportPoint(0.8, 0.5);
                    Coord coord = new UTMK(958541.25, 1942285.0);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .panTo(coord)
                            .rotateBy(90)
                            .zoomBy(1).build();
                    measureFps(1500);
                    gMap.animate(change, 1500);
                }
            });

            put("rotate right(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    measureFps(1000);
                    gMap.animate(
                            ViewpointChange.builder().pivot(pivot).rotateBy(90)
                                    .build(), 1000);
                }
            });
            put("rotate left(pivot)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    measureFps(1000);
                    gMap.animate(
                            ViewpointChange.builder().pivot(pivot).rotateBy(-90)
                                    .build(), 1000);
                }
            });

            put("pan to(pivot) (no animation)", new Runnable() {
                @Override
                public void run() {
                    Point pivot = getViewportPoint(0.5, 0.9);
                    Point panPoint = getViewportPoint(0.5, 0.5);
                    Coord coord = new UTMK(958541.25, 1942285.0);
                    ViewpointChange change = ViewpointChange.builder().pivot(pivot)
                            .panTo(coord)
                            .build();
                    measureFps(500);
                    gMap.change(change);
                }
            });
            put("pan gestures", new Runnable() {
                public void run() {
                    gMap.setPanGesturesEnabled(!gMap.isPanGesturesEnabled());
                }
            });
            put("zoom gestures", new Runnable() {
                public void run() {
                    gMap.setZoomGesturesEnabled(!gMap.isZoomGesturesEnabled());
                }
            });
            put("rotate gestures", new Runnable() {
                public void run() {
                    gMap.setRotateGesturesEnabled(!gMap.isRotateGesturesEnabled());
                }
            });
            put("tilt gestures", new Runnable() {
                public void run() {
                    gMap.setTiltGesturesEnabled(!gMap.isTiltGesturesEnabled());
                }
            });
        }
    };
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Point panPoint;
    private Point pivotPoint;

    private void setMapCreateListener() {
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    /**
     * @param widthRatio
     *            0~1
     * @param heightRatio
     *            0~1
     */
    private Point getViewportPoint(double widthRatio, double heightRatio) {
        int width = gMap.getView().getWidth();
        int height = gMap.getView().getHeight();
        return new Point(width * widthRatio, height * heightRatio);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.fps_test);
            // 세로모드에 정의된 GMapFragment 가 존재하면 제거
            Fragment fragment = getFragmentManager().findFragmentById(R.id.gmap);
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
        else {
            setContentView(R.layout.basic_demo);
            // 가로모드에 정의된 GMapFragment 가 존재하면 제거
            Fragment fragment = getFragmentManager().findFragmentById(R.id.gmap2);
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onMapReady(GMap gMap) {

        // test code..
        // gMap.setClientId("this-id-is-dummy");

        Logger.v(BasicMapActivity.class, "[onMapReady] viewpoint: {}", gMap.getViewpoint());
        Logger.v(BasicMapActivity.class, "[onMapReday] visible region: {}",
                gMap.getVisibleRegion());
        gMap.setOnViewpointChangeListener(this);
        gMap.setOnIdleListener(this);
        gMap.setOnMapLongpressListener(this);
        gMap.setOnOverlayTapListener(this);
        gMap.setOnOverlayDragListener(this);
        gMap.setOnMapTapListener(this);
        gMap.getRenderScheduler().setOnFpsListener(this);
        gMap.setGTrafficLayerAdaptor(new TrafficAdaptor(), getApplicationContext());
        gMap.setOnAnimationEndListener(this);
        this.gMap = gMap;

        // gMap.moveTo(INITIAL_VIEWPOINT);
        this.pivotPoint = getViewportPoint(0.5, 0.9);
        this.panPoint = getViewportPoint(0.45, 0.9);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);

        setMapCreateListener();
        //        Logger.d(TAG, "==== SDK Tile data format : " + GMapShared.getSupportedTileFormat());

        GMapShared omShared = GMapShared.getInstance(BasicMapActivity.this);
        //test code for set/getUseLocalDataOnly
        //        Logger.d(TAG, "===== (before setting) UseLocalDataOnly : " + omShared.getUseLocalDataOnly());
        //        omShared.setUseLocalDataOnly(true);
        //        Logger.d(TAG, "===== (after setting) UseLocalDataOnly : " + omShared.getUseLocalDataOnly());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                ITEMS.keySet().toArray(new String[0])));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Runnable) ITEMS.values().toArray()[position]).run();
                drawerLayout.closeDrawer(drawerList);
            }

        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_launcher,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        TextView sdkInfo = (TextView) findViewById(R.id.sdk_info);
        sdkInfo.setText(String.format("SDK version: %s", GMapShared.getSDKVersion()));
        mapInfoView = (TextView) findViewById(R.id.mapInfo);
        compassNeedle = (ImageView) findViewById(R.id.compass_needle);
        compassNeedle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gMap != null) {
                    gMap.animate(ViewpointChange.builder().rotateTo(0).tiltTo(0).build());
                }
            }
        });

        buildingFocusStack = new Stack<Coord>();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onZoomIn(View view) {
        if (gMap != null)
            gMap.animate(ViewpointChange.zoomIn(), 300);
    }

    public void onZoomOut(View view) {
        if (gMap != null) {
            gMap.animate(ViewpointChange.zoomOut(), 300);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onMapLongpress(GMap map, Coord location) {
        MarkerOptions options = new MarkerOptions().position(location);
        Marker marker = new Marker(options);
        map.addOverlay(marker);
        marker.animate(Marker.AnimationType.DROP);
        Viewpoint viewpoint = map.getViewpoint();
        UTMK center = UTMK.valueOf(viewpoint.center);
        Logger.d(BasicMapActivity.class,
                "Map Longpressed. viewpoint={center: ({},{}), zoom: {}, tilt: {}, rotation: {}}",
                center.x, center.y, viewpoint.zoom, viewpoint.tilt, viewpoint.rotation);
    }

    @Override
    public boolean onOverlayTap(GMap map, Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            // marker.setIcon(ResourceDescriptorFactory.fromAsset("blue_icon.png"));
            marker.animate(Marker.AnimationType.FLICK);
        }
        return false;
    }

    @Override
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        UTMK center = UTMK.valueOf(viewpoint.center);
        Logger.d(BasicMapActivity.class,
                "ViewpointChanged. viewpoint={center: ({},{}), zoom: {}, "
                        + "tilt: {}, rotation: {}, gesture: {}}",
                center.x, center.y, viewpoint.zoom, viewpoint.tilt, viewpoint.rotation, gesture);
        float tiltDegree = viewpoint.tilt;
        float rotDegree = viewpoint.rotation;

        VisibleRegion visibleRegion = map.getVisibleRegion();
        UTMK nearLeft = UTMK.valueOf(visibleRegion.nearLeft);
        UTMK nearRight = UTMK.valueOf(visibleRegion.nearRight);
        UTMK farLeft = UTMK.valueOf(visibleRegion.farLeft);
        UTMK farRight = UTMK.valueOf(visibleRegion.farRight);
        UTMKBounds bounds = UTMKBounds.valueOf(visibleRegion.getBounds());
        TextView zoomInfo = (TextView) findViewById(R.id.zoom_info);
        zoomInfo.setText(String.format("Zoom: %.1f", gMap.getViewpoint().zoom));

        mapInfoView.setText(String.format(
                "Center: %.0f, %.0f\nTilt: %.1f\nRotation: %.1f\nResolution: %.2f"
                ,center.x, center.y, tiltDegree, rotDegree, map.getResolution()
                // ,nearLeft.x, nearLeft.y, nearRight.x, nearRight.y,
                // farLeft.x, farLeft.y, farRight.x, farRight.y,
                // bounds.min().x, bounds.min().y,
                // bounds.max().x, bounds.max().y
                ));

        compassNeedle.setRotation(-rotDegree);
        compassNeedle.setRotationX(tiltDegree);

        this.pivotPoint = getViewportPoint(0.5, 0.9);

        if (pivotMarker != null)
            pivotMarker.setPosition(gMap.getCoordFromViewportPoint(getViewportPoint(0.5, 0.9)));

        if (routePath != null) {
            if (this.prevZoom != viewpoint.zoom) {
                routePath.setBufferWidth(PATH_BUFFER_IN_DP * map.getResolution());
            }
            if (Math.abs(this.prevZoom - (int) viewpoint.zoom) > 1) {
                this.prevZoom = (int) viewpoint.zoom;
                if (routePath != null) {
                    if (this.prevZoom < 5) {
                        routePath.setPeriod(6400);
                    } else if (this.prevZoom < 8) {
                        routePath.setPeriod(1000);
                    } else if (this.prevZoom < 12) {
                        routePath.setPeriod(500);
                    } else {
                        routePath.setPeriod(50);
                    }
                }
            }
        }
    }

    public void measureFps(long time) {
        gMap.getRenderScheduler().startMeasureFps();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                stopMeasureFps(2000);
            }
        }, time);
    }

    public void stopMeasureFps(final long disappearInfoDelay) {
        final float fps = gMap.getRenderScheduler().stopMeasureFps();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final TextView tv = (TextView) findViewById(R.id.fps_info_center);
                tv.setText(String.format("%.1f fps", fps));
                tv.setVisibility(View.VISIBLE);
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setVisibility(View.INVISIBLE);
                            }

                        });
                    }

                }, disappearInfoDelay);
            }

        });
    }

    @Override
    public void onIdle(GMap map) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final TextView tv = (TextView) findViewById(R.id.idle_info);
                tv.setText("IDLE!!");
                tv.setVisibility(View.VISIBLE);

                final TextView zoomInfo = (TextView) findViewById(R.id.zoom_info);
                zoomInfo.setText(String.format("Zoom: %.1f", gMap.getViewpoint().zoom));

                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                tv.setVisibility(View.INVISIBLE);
                                tv.setText("");
                            }
                        });
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void fps(final float fps) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final TextView tv = (TextView) findViewById(R.id.fps_info);
                tv.setText(String.format("%.1ffps", fps));
            }
        });

    }

    @Override
    public void onOverlayDragstart(GMap map, Overlay overlay) {
        Logger.v(TAG, "OverlayDragStart: {}", overlay.getId());
    }

    @Override
    public void onOverlayDrag(GMap map, Overlay overlay) {
        Coord position = ((Marker) overlay).getPosition();
        Logger.v(TAG, "OverlayDrag: {}", position);
    }

    @Override
    public void onOverlayDragend(GMap map, Overlay overlay) {
        Logger.v(TAG, "OverlayDragEnd: {}", overlay.getId());
    }

    @Override
    public void onMapTap(GMap map, Coord location) {
        Point point = gMap.getViewportPointFromCoord(location);
        Logger.v(TAG, "Tap Point: {}, {}", point.x, point.y);
    }

    @Override
    public void onAnimationComplete() {
        Logger.v(TAG, "animation complete");
    }

    @Override
    public void onAnimationCancel() {
        Logger.v(TAG, "animation cancel");
    }

    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
