//
//  MapShapeOverlayViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

enum ShapeType {
    case circle
    case polygon
    case polyline
}

class MapShapeOverlayViewController: BaseViewController {
    let circle_min_coord_size = 2
    let polygon_min_coord_size = 3
    let polyline_min_coord_size = 2
    
    
    var overlay: GOverlay?
    var points: [GCoord] = []
    var positionMarkers:[GMarker] = []
    var radius:Double = 100
    
    var demoType: ShapeType = .circle
    
    let menuList:[String:Selector] = [
        "Circle":#selector(circleOverlay),
        "Polygon":#selector(polygonOverlay),
        "Polyline":#selector(polylineOverlay),
        "Clear Shape":#selector(clearOverlay)
    ]
    @IBOutlet weak var mapView: GMapView!
    
    override func viewDidLoad() {
        mapView.delegate = self
        self.addMenuButton()
        self.setNavigationTitle("Shape")
    }
    
    override func showMenu() {
        showAlert(title: "Shape", message: "", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
            }
        }
    }
}


extension MapShapeOverlayViewController: GMapViewDelegate {
    func mapView(_ mapView: GMapView!, didTapAt coord: GCoord!) {
        addPoint(coord: coord)
    }
}


extension MapShapeOverlayViewController {
    @objc
    func clearOverlay() {
        if let shape = overlay {
            mapView.remove(shape)
        }
        
        positionMarkers.forEach { (marker) in
            mapView.remove(marker)
        }
        
        points.removeAll()
    }
    
    @objc
    func circleOverlay() {
        clearOverlay()
        demoType = .circle
    }
    
    @objc
    func polygonOverlay() {
        clearOverlay()
        demoType = .polygon
    }
    
    @objc
    func polylineOverlay() {
        clearOverlay()
        demoType = .polyline
    }
    
    func addPoint(coord:GCoord) {
        points.append(coord)
        
        switch demoType {
        case .circle:
            applyCircle(coord)
            break
        case .polygon:
            applyPolygon(coord)
            break
        case .polyline:
            applyPolyline(coord)
            break
        }
    }
    
    
    func addPositionMarker (coord:GCoord) {
        if let marker = GMarker.init(position: coord) {
            mapView.add(marker)
            positionMarkers.append(marker)
        }
    }
    
    
    func applyPolygon(_ coord:GCoord) {
        addPositionMarker(coord: coord)
        if points.count < polygon_min_coord_size {
            
        } else if points.count == polygon_min_coord_size {
            overlay = GPolygon.init(options: [
                "points":points,
                "fillColor":UIColor.red
            ])
            mapView.add(overlay)
        } else {
            (overlay as! GPolygon).points = points
        }
    }
    
    func applyPolyline(_ coord: GCoord) {
        addPositionMarker(coord: coord)
        if points.count < polyline_min_coord_size {
            
        } else if points.count == polyline_min_coord_size {
            overlay = GPolyline.init(options: [
                "points":points,
                "color":UIColor.red
            ])
            
            mapView.add(overlay)
        } else {
            (overlay as! GPolyline).points = points
        }
    }
    
    func applyCircle(_ coord:GCoord) {
        if points.count < circle_min_coord_size {
            addPositionMarker(coord: coord)
        } else if points.count == circle_min_coord_size {
            if let radius = GUtmk.value(of: points.first)?.distance(to: GUtmk.value(of: points.last)) {
                overlay = GCircle.init(options: [
                    "origin":points.first!,
                    "fillColor":UIColor.red,
                    "radius":radius
                ])

                mapView.add(overlay)
            }
        } else {
            if let radius = GUtmk.value(of: points.first)?.distance(to: GUtmk.value(of: points.last)) {
                (overlay as! GCircle).radius = Int32(radius)
            }
        }
    }
}
