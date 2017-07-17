package com.kt.maps.demo;

import com.kt.geom.model.Bounds;
import com.kt.geom.model.GenericBounds;
import com.kt.geom.model.LatLng;
import com.kt.geom.model.UTMK;
import com.kt.maps.model.Viewpoint;

public class Constants {

    static final UTMK 올레스퀘어 = new UTMK(953894, 1952660);
    static final LatLng 예술의전당 = new LatLng(37.478788111314614, 127.011623276644);
    static final Viewpoint INITIAL_VIEWPOINT = new Viewpoint(예술의전당, 8, 0, 0);
    static final Bounds 동작대교 =
            new GenericBounds<LatLng>(new LatLng(37.50496396044253, 126.98003349536177),
                    new LatLng(37.51575873924737, 126.98335333044616));

}
