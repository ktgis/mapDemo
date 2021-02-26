//
//  MarkerViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

enum MarkerDemoType {
    case addMarker
    case moveMarker
    case captionMarker
    case animationMarker
    case flatMarker
}

class MarkerViewController: BaseViewController {
    
    @IBOutlet weak var mapView: GMapView!
    
    let menuList: [String: Selector] = [
        "Add Marker":#selector(addMarkerInit),
        "Move Marker":#selector(moveMarkerInit),
        "Add Caption":#selector(addCaptionMarker),
        "Marker Animation":#selector(markerAnimation),
        "Flat Marker":#selector(markerFlat),
        "Clear Markers":#selector(clearMarkers)
    ]
    
    var markerList:[GMarker] = []
    var demoType: MarkerDemoType = .addMarker
    
    override func viewDidLoad() {
        mapView.delegate = self
        setNavigationTitle("Marker")
        addMenuButton()
    }
    
    override func showMenu() {
        showAlert(title: "", message: "", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
            }
        }
    }
}

//MARK:- GMapViewDelegate
extension MarkerViewController: GMapViewDelegate {
    func mapView(_ mapView: GMapView!, didTapAt coord: GCoord!) {
        switch demoType {
        case .addMarker:
            addMarker(coord: coord)
            break
        case .moveMarker:
            moveMarker(coord: coord)
        case .flatMarker:
            addFlatMarker(coord: coord)
            break
        case .animationMarker:
            animationMarker()
            break
        default:
            break
        }
    }
}


//MARK:- MarkerViewController function
extension MarkerViewController {
    @objc
    func addMarkerInit() {
        demoType = .addMarker
        clearMarkers()
    }
    
    func addMarker(coord:GCoord) {
        if let marker = GMarker.init(options: [
            "position":coord
        ]) {
            markerList.append(marker)
            mapView.add(marker)
        }
    }
    
    @objc
    func moveMarkerInit() {
        demoType = .moveMarker
        clearMarkers()
        addMarkerInCenter()
    }
    

    /*
    Marker의 position을 변경하면, Marker의 표출되는 위치가 변경 된다.
    Coord : 지도위의 좌표로 Utmk, Latlng, Katech등이 있다.
     */
    func moveMarker(coord: GCoord)  {
        markerList.forEach { (marker) in
            marker.position = coord
        }
    }
    
    @objc
    func addCaptionMarker() {
        demoType = .captionMarker
        clearMarkers()
        
        if let marker = GMarker.init(options: [
            "position":mapView.viewpoint.center!,
            "captionOptions": [
                "captionText":"Marker Caption",
                "captionColor":UIColor.magenta,
                "captionPositionType":CaptionPositionType.typeBottom
            ]
        ]) {
            markerList.append(marker)
            mapView.add(marker)
        }
    }
    
    @objc
    func markerAnimation() {
        demoType = .animationMarker
        clearMarkers()
        addMarkerInCenter()
    }
    
    @objc
    func markerFlat() {
        demoType = .flatMarker
        clearMarkers()
        addFlatMarkerInCenter()
    }
    
    @objc
    func clearMarkers() {
        markerList.forEach { (marker) in
            mapView.remove(marker)
        }
        
        markerList.removeAll()
    }
    
    
    /*
    Marker에 애니메이션을 줄수 있고, 해당 Animation은
    FLICK   : 잠시 커졌다가 다시 돌아오는 애니메이션 (강조)
    DROP    : 떨어지는 애니메이션
    이상 2가지가 있다.
     */
    func animationMarker (){
        markerList.forEach { (marker) in
            marker.animate(.MARKER_DROP)
        }
    }
    
    // 표출되고 있는 지도의 중심점에 Marker Overlay를 생성한다.
    func addMarkerInCenter() {
        addMarker(coord: mapView.viewpoint.center)
    }
    
    func addFlatMarker(coord:GCoord) {
        if let marker = GMarker.init(options: [
            "position":coord,
            "flat":true
        ]) {
            markerList.append(marker)
            mapView.add(marker)
        }

    }
    
    func addFlatMarkerInCenter() {
        addFlatMarker(coord: mapView.viewpoint.center)
    }
    
}
