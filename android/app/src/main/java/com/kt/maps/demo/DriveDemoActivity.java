package com.kt.maps.demo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.ViewpointChange;
import com.kt.maps.GMap.OnViewpointChangeListener;
import com.kt.maps.internal.RenderScheduler.OnFpsListener;
import com.kt.maps.model.Viewpoint;
import com.kt.maps.util.GMapKeyManager;

public class DriveDemoActivity extends Activity implements OnMapReadyListener, OnFpsListener,
        OnViewpointChangeListener {

    private GMap gMap;
    private DriveDemo driveDemo;
    private ImageView compassNeedle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_demo);

        GMapFragment fragment = (GMapFragment) getFragmentManager().findFragmentById(
                R.id.gmap);
        fragment.setOnMapReadyListener(this);

        compassNeedle = (ImageView) findViewById(R.id.compass_needle);
        compassNeedle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gMap != null) {
                    gMap.animate(ViewpointChange.builder().rotateTo(0).tiltTo(0).build());
                }
            }
        });
    }

    @Override
    public void onMapReady(GMap map) {
        this.gMap = map;
        driveDemo = new DriveDemo(this, map);
        map.setOnViewpointChangeListener(this);
        map.getRenderScheduler().setOnFpsListener(this);
        gMap.setGTrafficLayerAdaptor(new TrafficAdaptor());
    }

    public void flipAnimation(View view) {
        Button button = (Button) findViewById(R.id.animation_button);
        if (driveDemo.isAnimation()) {
            button.setText(R.string.animation);
            driveDemo.setAnimation(false);
        } else {
            button.setText(R.string.no_animation);
            driveDemo.setAnimation(true);
        }
    }

    public void startDrive(View view) {
        Button startButton = (Button) findViewById(R.id.start_button);
        if (driveDemo.isRunning()) {
            startButton.setText(R.string.start);
            driveDemo.stop();
        } else {
            startButton.setText(R.string.stop);
            driveDemo.start();
        }
    }

    public void onFinishDrive() {
        final float fps = gMap.getRenderScheduler().stopMeasureFps();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ((Button) findViewById(R.id.start_button)).setText(R.string.start);
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

                }, 5000);
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
    public void onViewpointChange(GMap map, Viewpoint viewpoint, boolean gesture) {
        float tiltDegree = viewpoint.tilt;
        float rotDegree = viewpoint.rotation;
        compassNeedle.setRotation(-rotDegree);
        compassNeedle.setRotationX(tiltDegree);

        driveDemo.onViewpointChange(map, viewpoint, gesture);
    }
    
    public void toggleTrafficInfo(View view) {
        gMap.setNetworkLayerVisible(!gMap.isNetworkLayerVisible());
    }


    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
