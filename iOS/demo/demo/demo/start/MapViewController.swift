//
//  MapViewController.swift
//  demo
//
//  Created by KT on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

class MapViewController: BaseViewController {
    var mapView: GMapView?
    override func viewDidLoad() {
        createMapView()
    }
    
    
    func createMapView() {
        mapView = GMapView.init(frame: self.view.frame,
                                mapOptions: GMapOptions.init())
        if let map = mapView {
            self.view.addSubview(map)
        }
    }
    
}
