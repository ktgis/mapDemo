//
//  MapStyleViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

enum StyleType {
    case dayDefault
    case dayDrive
    case nightDrive
    case nightDefault
    func stylePath ()-> String? {
        switch self {
        case .dayDefault:
            return Bundle.main.path(forResource: "day_default_add_style", ofType: "json")
        case .dayDrive:
            return Bundle.main.path(forResource: "day_drive_add_style", ofType: "json")
        case .nightDefault:
            return Bundle.main.path(forResource: "night_default_add_style", ofType: "json")
        case .nightDrive:
            return Bundle.main.path(forResource: "night_drive_add_style", ofType: "json")
        }
    }
    
}

class MapStyleViewController: BaseViewController {
    var isAnimationUse: Bool = false
    
    let menuList:[String:Selector] = [
        "default":#selector(applyDefault),
        "day drive":#selector(applyDayDrive),
        "night default":#selector(applyNightDefault),
        "night drive":#selector(applyNightDrive),
        "use Animtaion":#selector(useAnimation)
    ]
    
    
    @IBOutlet weak var mapView: GMapView!
    
    override func viewDidLoad() {
        setNavigationTitle("MapStyle")
        addMenuButton()
    }
    
    override func showMenu() {
        showAlert(title: "Map Style", message: "apply map theme", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
            }
        }
    }
}


extension MapStyleViewController {
    @objc
    func applyDefault() {
        applyStyle(.dayDefault)
    }
    
    @objc
    func applyDayDrive() {
        applyStyle(.dayDrive)
    }
    
    @objc
    func applyNightDefault() {
        applyStyle(.nightDefault)
    }
    
    @objc
    func applyNightDrive() {
        applyStyle(.nightDrive)
    }
    
    @objc
    func useAnimation() {
        isAnimationUse = !isAnimationUse
    }
    
    func applyStyle(_ type: StyleType) {
        let stylePath:String? = type.stylePath()
        /**
         * resource의 type을 png ->icon으로 변경해야 정상적으로 노출이 가능함.
         */
        switch type {
        case .dayDrive,
             .dayDefault:
            mapView.setSystemImage(Bundle.main.path(forResource: "day_style_icon", ofType: "png"),
                                   imageInfoPath: Bundle.main.path(forResource: "day_style_icon", ofType: "txt"))
            break
        case .nightDefault,
             .nightDrive:
            mapView.setSystemImage(Bundle.main.path(forResource: "night_style_icon", ofType: "png"),
                                   imageInfoPath: Bundle.main.path(forResource: "night_style_icon", ofType: "txt"))
            break
        }
            
        if isAnimationUse {
            mapView.animateStyle(stylePath)
        } else {
            mapView.setStyleAndUpdateCurrentLayer(stylePath)
        }
    }
}
