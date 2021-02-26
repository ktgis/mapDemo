//
//  InfoWindowViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

enum InfoWindowType {
    case defaultAdapter
    case customAdapter
}

class InfoWindowViewController: BaseViewController {
    
    @IBOutlet weak var mapView: GMapView!
    
    var overlays:[GOverlay] = []
    
    let menuList:[String: Selector] = [
        "Default Mode":#selector(modeDefault),
        "Custom Mode":#selector(modeCustom),
        "clearOverlays":#selector(clearOverlay)
    ]
    
    var demoType:InfoWindowType = .defaultAdapter
    
    
    override func viewDidLoad() {
        mapView.delegate = self
        
        setNavigationTitle("InfoWindow")
        addMenuButton()
    }
    
    override func showMenu() {
        showAlert(title: "InfoWindow", message: "infomation overlay", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
            }
        }
    }
}



extension InfoWindowViewController: GMapViewDelegate {
    func mapView(_ mapView: GMapView!, didTapAt coord: GCoord!) {
        if let infoWindow = GInfoWindow.infowWindow(options: [
            "position":coord!,
            "title":"infoWindow"
        ]) {
            mapView.add(infoWindow)
            overlays.append(infoWindow)
        }
    }
    
    
    func mapView(_ mapView: GMapView!, didShow infoWindow: GInfoWindow!) -> UIView! {
        switch demoType {
        case .customAdapter:
            let view = UIView.init(frame: CGRect.init(x: 0, y: 0, width: 130, height: 30))
            let label = UILabel.init(frame: view.frame)
            label.textAlignment = .center
            label.text = "custom label"
            view.addSubview(label)
            view.backgroundColor = UIColor.red
            return view
        case .defaultAdapter:
            return mapView.didShowDefaultInfoWindow(infoWindow)
        }
    }
}



extension InfoWindowViewController {
    @objc
    func clearOverlay() {
        overlays.forEach { (overlay) in
            mapView.remove(overlay)
        }
        
        overlays.removeAll()
    }

    @objc
    func modeDefault() {
        demoType = .defaultAdapter
    }
    
    @objc
    func modeCustom() {
        demoType = .customAdapter
    }
}
