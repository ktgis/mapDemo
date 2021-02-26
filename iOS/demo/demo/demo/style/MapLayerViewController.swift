//
//  MapLayerViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps


class MapLayerViewController: BaseViewController {
    
    @IBOutlet weak var mapView: GMapView!
    
    let menuList:[String:Selector] = [
        "Background":#selector(backgroundLayerVisibleChange),
        "Building":#selector(buildingLayerVisible),
        "Network":#selector(networkLayerVisible),
        "Label":#selector(labelLayerVisible),
        "LowBackground":#selector(lowLevelBackgroundVisible),
        "LowLabel":#selector(lowLevelLabelLayerVisible)
    ]
    
    
    override func viewDidLoad() {
        setNavigationTitle("MapLayer")
        addMenuButton()
        
        
    }
    
    override func showMenu() {
        showAlert(title: "Layer", message: "layer visible", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
            }
        }
    }
}


extension MapLayerViewController{
    @objc
    func backgroundLayerVisibleChange() {
        mapView.backgroundLayerVisible = !mapView.backgroundLayerVisible
    }
    
    @objc
    func buildingLayerVisible() {
        mapView.buildingLayerVisible = !mapView.buildingLayerVisible
    }
    
    @objc
    func networkLayerVisible() {
        mapView.networkLayerVisible = !mapView.networkLayerVisible
    }
    
    @objc
    func labelLayerVisible(){
        mapView.labelLayerVisible = !mapView.labelLayerVisible
    }

    @objc
    func lowLevelBackgroundVisible() {
        mapView.lowLevelBackgroundLayerVisible = !mapView.lowLevelBackgroundLayerVisible
    }
    
    @objc
    func lowLevelLabelLayerVisible() {
        mapView.lowLevelLabelLayerVisible = !mapView.lowLevelLabelLayerVisible
    }
}
