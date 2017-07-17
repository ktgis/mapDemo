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

/**
 * Created by kt on 2017. 7. 17..
 */

public class TrafficAdaptor implements GMap.GTrafficLayerAdaptor {
    @Override
    public GMap.GTrafficState speedToState(int speed) {
        /**
         * 속도에 대한 도로 교통 정보를 정의 하시면 됩니다.
         */
        return GMap.GTrafficState.GTrafficSlow;
    }
}
