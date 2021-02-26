//
//  CameraViewController.swift
//  demo
//
//  Created by KT on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps
//import geom

class CameraViewController: BaseViewController {
    @IBOutlet weak var mapView: GMapView!
    
    let menuList:[String:Selector] = [
        "panBy":#selector(panBy),
        "panTo":#selector(panTo),
        "rotateBy":#selector(rotateBy),
        "rotateTo":#selector(rotateTo),
        "zoom1Minus":#selector(zoomBy1Minus),
        "zoom1Plus":#selector(zoomBy1Plus),
        "zoomTo":#selector(zoomTo),
        "useViewpointChangeBuilder":#selector(useViewpointChangeBuilder)
    ]
    
    override func viewDidLoad() {
        self.setNavigationTitle("Camera")
        self.addMenuButton()
    }
    
    override func showMenu() {
        
        showAlert(title: "Map Control", message: "select option", actions: Array(menuList.keys)) { (title, index) in
            if let call = self.menuList[title] {
                self.perform(call)
            }
        }
    }
}


extension CameraViewController {
    /*
     * 지도상의 위치가 아닌 display 상의 화면 기준.
     * panBy (x, y)
     * param x
     * param y
     */
    @objc func panBy() {
        if let map = mapView {
            let viewpointChange = GViewpointChange.panBy(x: Double(map.frame.width) , y: Double(map.frame.height))
            map.animateViewpoint(viewpointChange)
        }
    }
    
    /*
     *  이동하고 싶은 특정위치로 지도를 이동
     *  panTo(coord) {@link com.kt.geom.model.Coord}, {@link LatLng} ,{@link com.kt.geom.model.Katech}, {@link com.kt.geom.model.UTMK}
     *  param coord     지도 상의 특정좌표.
     */
    @objc func panTo() {
        if let map = mapView {
            let pos = GLatLng.init(lat: 37.50496396044253, lng: 126.98003349536177)
            let viewpointChange = GViewpointChange.pan(to: pos)
            map.animateViewpoint(viewpointChange)
        }
    }
    
    /*
     * 현재 지도의 방위각을 기준으로 지도를 특정 각도만큼 회전.
     * rotateBy(degree)
     * param degree    각도.
     */
    @objc func rotateBy() {
        if let map = mapView {
            let rotateViewPoint = GViewpointChange.rotate(by: 90)
            map.animateViewpoint(rotateViewPoint)
        }
    }
    
    /*
     * 지도의 방위각을 조정.
     * rotateTo(degree)
     * param degree     각도.
     */
    @objc func rotateTo() {
        if let map = mapView {
            let rotateToViewPoint = GViewpointChange.rotate(to: 90)
            map.animateViewpoint(rotateToViewPoint)
        }
    }
    
    /**
     * 현재 줌을 기준으로 지도를 확대/축소 함.
     * zoomBy(amount)
     * param amount         확대/축소 값.
     *
     * if(amount > 0) 확대.
     * if(amount < 0) 축소
     *
     */
    @objc func zoomBy1Minus() {
        if let map = mapView {
            let zoomMinusViewPoint = GViewpointChange.zoom(by: -1)
            map.animateViewpoint(zoomMinusViewPoint)
        }
    }
    
    @objc func zoomBy1Plus() {
        if let map = mapView {
            let zoomPlusViewPoint = GViewpointChange.zoom(by: 1)
            map.animateViewpoint(zoomPlusViewPoint)
        }
    }
    
    /**
     * 지도를 특정 줌으로 조정.
     *
     * zoomTo(zoom)
     * param zoom         지도 확대/축소 레벨
     *
     *  validation zoom value :
     *      zoom >= 0 &&
     *       zoom <= 14
     */
    @objc func zoomTo() {
        if let map = mapView {
            let zoomViewpointChange = GViewpointChange.zoom(to: 13)
            map.animateViewpoint(zoomViewpointChange)
        }
    }
    
    @objc func useViewpointChangeBuilder () {
        if let map = mapView {
            /**
             zoomTo     : 14
             panTo      : (lat: 37.50496396044253, lng: 126.98003349536177)
             rotateTo   : 90
             */
            let viewpointChange = GViewpointChangeBuilder.init()
                .zoom(to: 14)
                .pan(to: GLatLng.init(lat: 37.50496396044253, lng: 126.98003349536177))
                .rotate(to: 90)?.build()
            
            map.animateViewpoint(viewpointChange)
        }
    }
}
