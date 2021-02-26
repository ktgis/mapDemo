package com.kt.mapdemo.demo.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.kt.geom.model.LatLng;
import com.kt.mapdemo.R;
import com.kt.mapdemo.base.BaseActivity;
import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.GMapResultCode;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.model.Viewpoint;

/*
 * 화면상에 표출 되는 지도의 위치, 방위각, 기울기 등을 제어하는 샘플 코드
 */
public class MapCameraActivity extends BaseActivity implements OnMapReadyListener {
    private GMap mapObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_camera);
        
        // map initialize
        ((GMapFragment)getFragmentManager().findFragmentById(R.id.cameraMap)).setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        mapObj = gMap;
    }

    @Override
    public void onFailReadyMap(GMapResultCode gMapResultCode) {

    }

    @Override
    public int getMenuLayout() {
        return R.menu.camera_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cameraPanBy:
                panBy();
                break;
            case R.id.cameraPanTo:
                panTo();
                break;
            case R.id.cameraRotateBy:
                rotateBy();
                break;
            case R.id.cameraRotateTo:
                rotateTo();
                break;
            case R.id.cameraZoomBy1minus:
                zoomBy1Minus();
                break;
            case R.id.cameraZoomBy1plus:
                zoomBy1Plus();
                break;
            case R.id.cameraZoomTo:
                zoomTo();
                break;
            case R.id.cameraAnimation:
                useViewpointChangeBuilder();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void panBy() {
        /*
         * 지도상의 위치가 아닌 display 상의 화면 기준.
         * panBy (x, y)
         * param x
         * param y
         */
        ViewpointChange viewpointChange = ViewpointChange.panBy(mapObj.getView().getWidth(), mapObj.getView().getHeight());
        mapObj.animate(viewpointChange);

    }

    private void panTo() {
        /*
         *  이동하고 싶은 특정위치로 지도를 이동
         *  panTo(coord) {@link com.kt.geom.model.Coord}, {@link LatLng} ,{@link com.kt.geom.model.Katech}, {@link com.kt.geom.model.UTMK}
         *  param coord     지도 상의 특정좌표.
         */
        ViewpointChange viewpointChange = ViewpointChange.panTo(
                new LatLng(37.50496396044253, 126.98003349536177)
        );

        mapObj.animate(viewpointChange);
    }

    private void rotateBy() {
        /*
         * 현재 지도의 방위각을 기준으로 지도를 특정 각도만큼 회전.
         * rotateBy(degree)
         * param degree    각도.
         */
        ViewpointChange viewpointChange = ViewpointChange.rotateBy(90);
        mapObj.animate(viewpointChange);
    }

    private void rotateTo() {
        /*
         * 지도의 방위각을 조정.
         * rotateTo(degree)
         * param degree     각도.
         */
        ViewpointChange viewpointChange = ViewpointChange.rotateTo(90);
        mapObj.animate(viewpointChange);
    }

    /**
     * 현재 줌을 기준으로 지도를 확대/축소 함.
     * zoomBy(amount)
     * param amount         확대/축소 값.
     *
     * if(amount > 0) 확대.
     * if(amount < 0) 축소
     *
     */
    private void zoomBy1Minus() {
        ViewpointChange viewpointChange = ViewpointChange.zoomBy(-1);
        mapObj.animate(viewpointChange);
    }

    private void zoomBy1Plus() {
        ViewpointChange viewpointChange = ViewpointChange.zoomBy(1);
        mapObj.animate(viewpointChange);
    }

    /**
     * 지도를 특정 줌으로 조정.
     *
     * zoomTo(zoom)
     * param zoom         지도 확대/축소 레벨
     *
     *  validation zoom value :
     *      zoom >= 0 &&
     *       zoom <= 14
     */
    private void zoomTo(){
        ViewpointChange viewpointChange = ViewpointChange.zoomTo(13);
        mapObj.animate(viewpointChange);
    }

    private void useViewpointChangeBuilder() {
        ViewpointChange viewpointChange = ViewpointChange.builder()
                .zoomTo(14)
                .panTo(new LatLng(37.50496396044253, 126.98003349536177))
                .rotateTo(60)
                .build();

        mapObj.animate(viewpointChange);
    }
}