//
//  DemoTestModel.swift
//  demo
//
//  Created by 박주철 on 2021/01/24.
//  Copyright © 2021 박주철. All rights reserved.
//

import Foundation
import UIKit

struct DemoTestModel {
    var title: String
    var description: String?
    var identifier: String 
}


func testModelList() -> Dictionary<String, Array<DemoTestModel>> {
    var testSet: Dictionary<String, Array<DemoTestModel>> = Dictionary()
    testSet["1. start"] = [
        DemoTestModel(title: "Map(Xib)", description: "add Map in story board", identifier: "mapStroyBoard"),
        DemoTestModel(title: "Map(Code)", description: "add Map by code", identifier: "mapViewCode")
    ]
    testSet["2. camera"] = [
        DemoTestModel(title: "Camera", description: "map camera control", identifier: "cameraMap"),
        DemoTestModel(title: "Gesture", description: "map gesture delegate", identifier: "gestureMap")
    ]
    testSet["3. style"] = [
        DemoTestModel(title: "Layer", description: "layer visible", identifier: "layerMap"),
        DemoTestModel(title: "Map Theme", description: "map theme", identifier: "themeMap")
    ]
    testSet["4. overlay"] = [
        DemoTestModel(title: "InfoWindow", description: "infomation overlay", identifier: "infoWindowMap"),
        DemoTestModel(title: "Shape", description: "shape overlay", identifier: "shapeOverlayMap"),
        DemoTestModel(title: "Marker", description: "marker overlay", identifier: "markerOVerlayMap"),
        DemoTestModel(title: "Path", description: "path overlay", identifier: "pathOverlayMap")
    ]
    testSet["5. plugin"] = [
//        DemoTestModel(title: "Location", description: "User location", identifier: "locationMap"),
        DemoTestModel(title: "Symbol", description: "symbol tap", identifier: "symbolMap"),
        DemoTestModel(title: "Traffic", description: "Road traffic", identifier: "trafficMap")
    ]
    return testSet
}

func testKeySet () -> Array<String> {
    return [
        "1. start","2. camera","3. style", "4. overlay", "5. plugin"
    ]
}
