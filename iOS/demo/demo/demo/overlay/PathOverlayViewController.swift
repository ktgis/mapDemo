//
//  PathOverlayViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

enum PathType {
    case path
    case arrow
    case traffic
    case route
}

class PathOverlayViewController: BaseViewController {
    let split_size_of_point = 100
    
    @IBOutlet weak var mapView: GMapView!
    @IBOutlet weak var routeSeekbar: UISlider!
    
    var demoType: PathType = .path
    
    var pathOverlay:GOverlay?
    
    let menuList:[String:Selector] = [
        "Path Overlay":#selector(showPath),
        "Arrow Path":#selector(showArrow),
        "Route Overlay":#selector(showRoute),
        "Traffic Overlay":#selector(showTraffic),
    ]
    
    override func viewDidLoad() {
        setNavigationTitle("Path")
        addMenuButton()
    }
    
    override func showMenu() {
        showAlert(title: "Path", message: "", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
                switch self.demoType {
                case .arrow, .path:
                    self.routeSeekbar.value = 0
                    self.routeSeekbar.isEnabled = false
                    break
                case .route, .traffic:
                    self.routeSeekbar.value = 0
                    self.routeSeekbar.isEnabled = true
                    break
                }
            }
        }
    }
    
    @IBAction func valueChanged(_ sender: Any) {
        switch demoType {
        case .route:
            splitRoute(index: routeSeekbar.value)
            break
        case .traffic:
            splitTraffic(index: routeSeekbar.value)
            break
        default:
            break
        }
    }
}

extension PathOverlayViewController {
    @objc
    func showPath() {
        demoType = .path
        removePath()
        if let path = GPath.init(options: [
            "points":Array(routePathCoord[0...30]),
            "fillColor":UIColor.red,
            "bufferWidth":5 * mapView.getResolution()
        ]) {
            pathOverlay = path
            mapView.add(path)
        }
    }
    
    @objc
    func removePath () {
        if let overlay = pathOverlay {
            mapView.remove(overlay)
        }
    }
    
    @objc
    func showArrow() {
        demoType = .arrow
        removePath()
        if let path = GPath.init(options: [
            "points":Array(routePathCoord[0...30]),
            "fillColor":UIColor.red,
            "bufferWidth":5 * mapView.getResolution(),
            "hasArrow":true
        ]) {
            pathOverlay = path
            mapView.add(path)
        }
    }
    
    @objc
    func showRoute() {
        demoType = .route
        
        removePath()
        if let route = GRoute.init(options: [
            "points":Array(routePathCoord),
            "fillColor":UIColor.blue,
            "bufferWidth":5 * mapView.getResolution()
        ]) {
            pathOverlay = route
            mapView.add(route)
        }
    }
    
    
    func splitRoute(index: Float) {
        var indexCoord = Int(Float(routePathCoord.count) * index)
        if indexCoord != 0 {
           indexCoord -= 1
        }
        
        (pathOverlay as! GRoute).splitCoord = routePathCoord[indexCoord]
    }
    
    @objc
    func showTraffic() {
        removePath()
        demoType = .traffic
        
        if let traffic = GTrafficRoute.init(options: [
            "links":Array(makeTrafficLinkList()),
            "bufferWidth": 5 * mapView.getResolution()
        ]) {
            pathOverlay = traffic
            mapView.add(traffic)
        }
    }
    
    func makeTrafficLink (from: Int, to:Int, linkId:Int) -> GTrafficLink{
        return GTrafficLink.init(Array(routePathCoord[from...to]),
                                 color: randomColor(),
                                 linkId: NSNumber(value: linkId)
        )
    }
    
    func makeTrafficLinkList() -> [GTrafficLink]{
        var linkList: [GTrafficLink] = []
        var index = 0
        for i in stride(from: 0, to: routePathCoord.count, by: split_size_of_point) {
            if i + split_size_of_point >= routePathCoord.count {
                linkList.append(makeTrafficLink(from: i, to: routePathCoord.count - 1, linkId: index))
            } else {
                linkList.append(makeTrafficLink(from: i, to: i + split_size_of_point, linkId: index))
            }
            
            index += 1
        }
        
        return linkList
    }
    
    func randomColor() -> UIColor {
        return colorList[Int.random(in: 0...2)]
    }
    
    
    func splitTraffic(index: Float) {
        var indexCoord = Int(Float(routePathCoord.count) * index)
        if indexCoord != 0 {
           indexCoord -= 1
        }
        
        (pathOverlay as! GTrafficRoute).setSplitCoord(routePathCoord[indexCoord],
                                                      linkIndex: indexCoord / split_size_of_point as NSNumber)
    }
}
