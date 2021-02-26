package com.kt.mapdemo.demo.plugin.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.kt.geom.model.Coord;
import com.kt.geom.model.UTMK;
import com.kt.gmaputils.clustering.MarkerClusterManager;
import com.kt.gmaputils.clustering.algo.ClusterOption;
import com.kt.gmaputils.clustering.model.Cluster;
import com.kt.gmaputils.clustering.model.ClusterTapListener;
import com.kt.gmaputils.clustering.render.GMapClusterRender;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.mapdemo.demo.overlay.MapShapeOverlayActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.model.Point;
import com.kt.maps.overlay.Marker;
import com.kt.maps.overlay.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.nio.channels.ClosedSelectorException;

public class MarkerClusterMapActivity extends BaseActivity
        implements OnMapReadyListener, GMap.OnMapTapListener, GMap.OnIdleListener, ClusterTapListener {
    private final String TAG = MarkerClusterMapActivity.class.getSimpleName();
    private GMap mapObj;
    private MarkerClusterManager clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_cluster_map);

        ((GMapFragment)getFragmentManager().findFragmentById(R.id.markerClusterMap)).setOnMapReadyListener(this);
    }

    @Override
    public int getMenuLayout() {
        return R.menu.cluster_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearCluster:
                removeAllMarkers();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // remove markers
    private void removeAllMarkers() {
        clusterManager.clearMarkers();
        // 특정 마커만 제거.
        /*
         * param marker : 제거를 원하는 마커.
         */
//        clusterManager.removeMarker(marker);
    }

    @Override
    public void onMapReady(GMap gMap) {
        mapObj = gMap;
        mapObj.setOnMapTapListener(this);
        initMarkerCluster();
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) { }

    // cluster initialize
    private void initMarkerCluster() {
        mapObj.setOnIdleListener(this);
        /*
         * marker cluster initialize
         * param GMap               marker가 추가될 맵 객체
         * param OnIdleListener     map이 idle상태일때 추가되기에 Map객체에 setting된 onIdleListener 를 넘겨줘야 한다.
         */
        clusterManager = new MarkerClusterManager(mapObj, this);
        ClusterOption options = new ClusterOption(
                25.0,               // clustering radius (dp 단위)   * default : 25
                12,        // max Zoom 12 level부터는 클러스링 하지 않고, 모든 마커를 보여줌. * default : 13
                3,          // clustering iteration             * default : 3
                true        // animation                        * default : true
        );
        // set cluster option
        clusterManager.setClusterOption(options);
        // cluster marker click event
        // 해당 탭 이벤트를 설정 안할 시 기본 셋팅을 사용하며, 기본 셋팅은 해당 cluster에 있는 Marker들을 표출 할 수 있도록 줌을 변경해준다.
//        clusterManager.setClusterTapListener(this);

        // Clustering Marker image
//        clusterManager.setRenderer(new GMapClusterRender() {
//            @NotNull
//            @Override
//            public View renderCluster(@NotNull Context context, @NotNull Cluster cluster) {
        // return view
//                return null;
//            }
//
//            @NotNull
//            @Override
//            public Point clusterSize() {
        // size (width, height)
//                return null;
//            }
//        });
    }

    @Override
    public void onMapTap(GMap gMap, Coord coord) {
        Marker marker = new Marker(new MarkerOptions().position(coord));
        // cluster에 마커를 추가시 지도에 자동으로 추가 된다.
        clusterManager.addMarker(marker);
    }

    @Override
    public void onIdle(GMap gMap) {
        Log.d(TAG, "GMap OnIdle Listener");
    }


    @Override
    public void onClusterClick(@NotNull GMap gMap, @NotNull Cluster cluster) {
        Log.d(TAG, "cluster info : \n" +
                "size : " + cluster.getItems().size() +"\n" +
                "coord : " + UTMK.valueOf(cluster.getCentroid()).toString());
    }
}
