//
//  LocationProvider.swift
//  demo
//
//  Created by KT on 2021/02/01.
//  Copyright © 2021 박주철. All rights reserved.
//

import Foundation
import CoreLocation

class LocationProvider: NSObject {
    var locationManager: CLLocationManager
    
    override
    init() {
        locationManager = CLLocationManager.init()
        
        super.init()
        
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
    }
    
    func checkPermission()-> Bool {
        switch CLLocationManager.authorizationStatus() {
        case .authorizedAlways, .authorizedWhenInUse:
            print("permission access")
            return true
        case .denied, .notDetermined, .restricted:
            print("permission deny")
            return false
        default:
            return false
        }
    }
    
    func requestAuth() {
        locationManager.requestAlwaysAuthorization()
    }
    
    func getLastLocation () {
        locationManager.requestLocation()
    }
    
    func startUpdate() {
        locationManager.startUpdatingLocation()
    }
    
    func stopUpdate() {
        locationManager.stopUpdatingLocation()
    }
}



extension LocationProvider: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        print("location update call")
    }
    
    // >= 14
    //    @available(iOS, introduced: 4.2, deprecated: 14.0)
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        
    }
    // < 14
    //    @available(iOS 4.0, *)
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("error \(error)")
    }
}
