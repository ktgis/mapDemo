//
//  SymbolViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/28.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps


class SymbolViewController: BaseViewController {
    @IBOutlet weak var mapView: GMapView!
 
    
    override func viewDidLoad() {
        mapView.delegate = self
        
        let labelFocusOption = GMapLabelOption.initMode(.MagnifactionEffect)
        mapView.setGMapLabelFocus(labelFocusOption)
    }
}


extension SymbolViewController: GMapViewDelegate {
    func mapView(_ mapView: GMapView!, didTap info: GMapLabelInfo!) -> Bool {
        print("label name: \(String(describing: info.labelName)), id:\(String(describing: info.labelId))")
        return false
    }
    
    func mapView(_ mapView: GMapView!, didLongPress info: GMapLabelInfo!) -> Bool {
        print("label name: \(String(describing: info.labelName)), id:\(String(describing: info.labelId))")
        return false
    }
}
