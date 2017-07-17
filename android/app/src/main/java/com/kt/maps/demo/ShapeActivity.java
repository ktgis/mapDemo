package com.kt.maps.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.kt.maps.GMap;
import com.kt.maps.GMapFragment;
import com.kt.maps.OnMapReadyListener;
import com.kt.maps.model.ResourceDescriptorFactory;
import com.kt.geom.model.UTMK;
import com.kt.maps.overlay.DashedLine;
import com.kt.maps.overlay.DashedLineOptions;
import com.kt.maps.overlay.DashedPath;
import com.kt.maps.overlay.DashedPathOptions;
import com.kt.maps.overlay.Path;
import com.kt.maps.overlay.PathOptions;
import com.kt.maps.overlay.Polygon;
import com.kt.maps.overlay.PolygonOptions;
import com.kt.maps.overlay.Polyline;
import com.kt.maps.overlay.PolylineOptions;
import com.kt.maps.overlay.Circle;
import com.kt.maps.overlay.CircleOptions;
import com.kt.maps.overlay.RoutePath;
import com.kt.maps.overlay.RoutePathOptions;
import com.kt.maps.util.GMapKeyManager;

public class ShapeActivity extends Activity implements OnMapReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmap_fragment);
        GMapFragment fragment =
                (GMapFragment) getFragmentManager().findFragmentById(R.id.gmap);
        fragment.setOnMapReadyListener(this);
    }

    @Override
    public void onMapReady(GMap gMap) {
        
        Circle circle = new Circle(new CircleOptions().radius(500).origin(new UTMK(956744, 1940444))
        .fillColor(Color.RED));
        gMap.addOverlay(circle);
        
        Polyline polyline = new Polyline(new PolylineOptions()
                .addPoints(new UTMK(951590, 1940834), new UTMK(955190, 1940134))
                .addPoint(new UTMK(953690, 1942134))
                .color(Color.RED)
                .width(5));
        gMap.addOverlay(polyline);

        Polygon polygon = new Polygon(new PolygonOptions()
                .addPoints(new UTMK(956744, 1943444), new UTMK(956652, 1943284),
                        new UTMK(956410, 1943304), new UTMK(956588, 1943122),
                        new UTMK(956562, 1942908), new UTMK(956742, 1943024),
                        new UTMK(956922, 1942918), new UTMK(956882, 1943120),
                        new UTMK(957040, 1943292), new UTMK(956832, 1943296))
                .addHole(new UTMK(956652, 1943284), new UTMK(956588, 1943122),
                        new UTMK(956742, 1943024), new UTMK(956652, 1943284))
                .fillColor(Color.YELLOW)
                .strokeColor(Color.GREEN)
                .strokeWidth(1));
        gMap.addOverlay(polygon);
        DashedLine path = new DashedLine(new DashedLineOptions()
        .addPoints(new UTMK(957016, 1943938), new UTMK(957090, 1943343),
                new UTMK(957117, 1943223), new UTMK(957238, 1942963),
                new UTMK(957394, 1942658), new UTMK(957525, 1942697),
                new UTMK(957703, 1942728), new UTMK(957918, 1942750),
                new UTMK(957944, 1942745), new UTMK(957968, 1942733),
                new UTMK(958035, 1942691), new UTMK(958073, 1942661),
                new UTMK(958089, 1942643), new UTMK(958105, 1942620),
                new UTMK(958118, 1942595), new UTMK(958130, 1942564),
                new UTMK(958185, 1942378), new UTMK(958204, 1942312),
                new UTMK(958305, 1941972), new UTMK(958349, 1941869),
                new UTMK(958397, 1941775), new UTMK(958442, 1941676),
                new UTMK(958404, 1941643), new UTMK(958539, 1941455),
                new UTMK(958481, 1941386), new UTMK(958465, 1941401))

        .width(10).color(Color.MAGENTA));
        gMap.addOverlay(path);
        
        DashedPath dPath = new DashedPath(new DashedPathOptions()
        .addPoints(
                new UTMK(955244, 1943297),
                new UTMK(956492, 1943737),
                new UTMK(956164, 1944553),
                new UTMK(956168, 1944913),
                new UTMK(956732, 1945185),
                new UTMK(958, 194))
        .bufferWidth(30));
        dPath.setColor(Color.BLACK);
        gMap.addOverlay(dPath);
        
        Path arrowPath = new Path(new PathOptions()
        .addPoints(
                new UTMK(956836, 1942881),
                new UTMK(956972, 1942497),
                new UTMK(956404, 1942153))
        .bufferWidth(30)
        .hasLineCap(false)
        .hasArrow(true));
        
        arrowPath.setFillColor(Color.rgb(33, 158, 255));
        gMap.addOverlay(arrowPath);
        
        RoutePath imagePath = new RoutePath(new RoutePathOptions()
        .addPoints(
                new UTMK(952590, 1941134),
                new UTMK(956640, 1949058),
                new UTMK(957340, 1943258),
                new UTMK(952590, 1949434))
        .bufferWidth(50)
        .hasPeriodicImage(true)
        .period(500)
        .strokeWidth(1)
        .strokeColor(Color.BLACK)
        .periodicImage(ResourceDescriptorFactory.fromAsset("slash.png")));
        
        imagePath.setFillColor(Color.rgb(255, 229, 63));
        gMap.addOverlay(imagePath);
    }
    @Override
    public void onFailReadyMap(GMapKeyManager.ResultCode code) {
        Toast.makeText(this, "Result Code : " + code.toString(), Toast.LENGTH_SHORT).show();
    }
}
