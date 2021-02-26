package com.kt.mapdemo.demo.plugin.traffic;

import com.kt.maps.GMap;
import com.kt.maps.GMapShared;

public class CustomTrafficAdapter implements GMap.GTrafficLayerAdaptor {
    public static final GMap.GTrafficStateRequestType RoadRequestType = GMap.GTrafficStateRequestType.GPANE;

    @Override
    public GMap.GTrafficStateRequestType setTypeForRequestSpeedToState() {
        //도로 종별에 대한 구분
        return RoadRequestType;
        //제한 속도에 대한 구분
//        return GMap.GTrafficStateRequestType.GMAX;

    }

    /**
     * typeValue의 {@link GMap.GTrafficLayerAdaptor#setTypeForRequestSpeedToState()}의 값에 따라 바뀜.
     * {@link com.kt.maps.GMap.GTrafficStateRequestType#GPANE   -> 도로 종별}
     * {@link com.kt.maps.GMap.GTrafficStateRequestType#GMAX    -> 도로별 제한속도}
     * @param typeValue 도로 종별
     * @param speed     속도
     * @return
     */
    @Override
    public GMap.GTrafficState speedToState(int typeValue, int speed) {
        //도로 종별에 대한 구분
        if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GDETAILED_ROAD.getValue())) {
            if (speed > 20) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 10) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 5) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GSMALL_ROAD.getValue())) {
            if (speed > 30) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 20) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 10) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GMIDDLE_ROAD.getValue())
                || typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GBIG_ROAD.getValue())) {
            if (speed > 70) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 50) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 30) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GPROVINCIAL_ROAD.getValue())) {
            if (speed > 50) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 30) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 10) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GNATIONAL_ROAD.getValue())) {
            if (speed > 60) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 40) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 30) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GCITY_HIGHWAY.getValue())) {
            if (speed > 70) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 50) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 30) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GHIGHWAY.getValue())) {
            if (speed > 80) {
                return GMap.GTrafficState.GTrafficLight; // 원할
            } else if (speed > 60) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 40) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        }

        //제한 속도에 대한 구분
        /*
        if ( ((float)speed / (float) typeValue) >= 1.0 ) {
            return GMap.GTrafficState.GTrafficLight; // 원할
        } else if ( ((float)speed / (float) typeValue) >= 0.8 ) {
            return GMap.GTrafficState.GTrafficSlow; // 원할
        } else if ( ((float)speed / (float) typeValue) >= 0.6 ) {
            return GMap.GTrafficState.GTrafficDelay; // 원할
        } else if ( ((float)speed / (float) typeValue) >= 0.4 ) {
            return GMap.GTrafficState.GTrafficCongested; // 원할
        }
        */

        return GMap.GTrafficState.GTrafficUnknown;
    }
}