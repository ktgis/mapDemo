//
//  GestureMapViewController.swift
//  demo
//
//  Created by KT on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

class GestureMapViewController: BaseViewController {
    
    @IBOutlet weak var mapView: GMapView!
    override func viewDidLoad() {
        mapView?.delegate = self
        self.setNavigationTitle("Gesture")
    }
}


extension GestureMapViewController: GMapViewDelegate {
    func mapView(_ mapView: GMapView!, didTapAt coord: GCoord!) {
        if let location = GLatLng.value(of: coord) {
            print("didTap(lat : \(location.lat) lng : \(location.lng)")
        }
    }
    
    func mapView(_ mapView: GMapView!, didLongPressAt coord: GCoord!) {
        if let location = GLatLng.value(of: coord) {
            print("didLongPressAt(lat : \(location.lat) lng : \(location.lng)")
        }
    }
    
    func mapView(_ mapView: GMapView!, didTap info: GMapLabelInfo!) -> Bool {
        print("didTap label (labelName : \(String(describing: info.labelName)) uuid : \(String(describing: info.labelId))")
        return false
    }
    
    func mapView(_ mapView: GMapView!, didLongPress info: GMapLabelInfo!) -> Bool {
        
        print("didLongPress label (labelName : \(String(describing: info.labelName)) uuid : \(String(describing: info.labelId))")
        return false
    }
    
    func mapView(_ mapView: GMapView!, didIdleAt viewpoint: GViewpoint!) {
        
    }
    
    /**
     *  Pan, Tilt, Rotate, Zoom 는  mapView(_ mapView: GMapView!, didChange viewpoint: GViewpoint!, withGesture gesture: Bool) 로 응답이 내려온다.
     *  viewpoint.center        : 지도의 중심점.
     *  viewpoint.rotation      : 지도의 방위각.
     *  viewpoint.zoom          : 지도의 줌레벨.
     *  viewpoint.tilt          : 지도의 기울기.
     */
    func mapView(_ mapView: GMapView!, didChange viewpoint: GViewpoint!, withGesture gesture: Bool) {
        print("{zoom : \(viewpoint.zoom), rotate:\(viewpoint.rotation),tilt:\(viewpoint.tilt )}")
    }
    
    

}
