/*
 *  Copyright (c) 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file
 *  except in compliance with license agreement with kt corp. Any redistribution
 *  or use of this software, with or without modification shall be strictly
 *  prohibited without prior written approval of kt corp, and the copyright
 *   notice above does not evidence any actual or intended publication of such
 *  software.
 *
 */

package com.kt.maps.demo;

import com.kt.maps.GMap;
import com.kt.maps.GMapRoadType;
import com.kt.maps.GMapShared;

/**
 * Created by kt on 2017. 7. 17..
 */

public class TrafficAdaptor implements GMap.GTrafficLayerAdaptor {

    @Override
    public GMap.GTrafficStateRequestType setTypeForRequestSpeedToState() {
        /**
         * 속도에 대한 도로 교통 정보요청을 위한 타입 세팅 제한속도(MAX), 도로종별(PANE) 둘중에 하나를 선택
         */

        //도로 종별에 대한 구분
        return GMap.GTrafficStateRequestType.GPANE;

        //제한 속도에 대한 구분
//        return GMap.GTrafficStateRequestType.GMAX;
    }

    @Override
    public GMap.GTrafficState speedToState(int typeValue, int speed) {
        /**
         * 속도에 대한 도로 교통 정보를 정의 하시면 됩니다.
         */

        //도로 종별에 대한 구분
        if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GDETAILED_ROAD.getValue())) {
            if (speed > 20) {
                return GMap.GTrafficState.GTrafficLight;
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
                return GMap.GTrafficState.GTrafficLight;
            } else if (speed > 20) {
                return GMap.GTrafficState.GTrafficSlow;
            } else if (speed > 10) {
                return GMap.GTrafficState.GTrafficDelay;
            } else if (speed >= 0) {
                return GMap.GTrafficState.GTrafficCongested;
            }
            return GMap.GTrafficState.GTrafficUnknown;
        } else if ( typeValue == GMapShared.getPaneIdByName(GMap.GRoadTypeString.GMIDDLE_AND_BIG_ROAD.getValue())) {
            if (speed > 70) {
                return GMap.GTrafficState.GTrafficLight;
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
                return GMap.GTrafficState.GTrafficLight;
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
                return GMap.GTrafficState.GTrafficLight;
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
                return GMap.GTrafficState.GTrafficLight;
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
                return GMap.GTrafficState.GTrafficLight;
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
            return GMap.GTrafficState.GTrafficLight;
        } else if ( ((float)speed / (float) typeValue) >= 0.8 ) {
            return GMap.GTrafficState.GTrafficSlow;
        } else if ( ((float)speed / (float) typeValue) >= 0.6 ) {
            return GMap.GTrafficState.GTrafficDelay;
        } else if ( ((float)speed / (float) typeValue) >= 0.4 ) {
            return GMap.GTrafficState.GTrafficCongested; 
        }
        */

        return GMap.GTrafficState.GTrafficUnknown;
    }
}
