//
//  TrafficViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/28.
//  Copyright © 2021 박주철. All rights reserved.
//

import gmaps

class TrafficViewController: BaseViewController {
    
    @IBOutlet weak var mapView: GMapView!
    
    override func viewDidLoad() {
        
        // Custom Adapter를 설정 안하게 되면, SDK내부의 기본 adapter로 색상을 매칭하게 된다.
        mapView.setTrafficAdaptor(self)
        mapView.networkLayerVisible = true
        
        // 도로 종별에 따른 요청이 아닌 제한 속도를 요청시에는 해당 함수를 호출해야
        // GMapShared.getInstance(getApplicationContext()).getTrafficManager()을 통한 링크별 속도정보를 정확히 얻을 수 있다.
        // 단) 도로 종별로 요청시에는 해당 함수를 호출 할 필요가 없다.

        let mapShared: GMapShared = GMapShared.sharedInstance() as! GMapShared
        mapShared.trafficManager.setTrafficType(PANE)
    }
}

enum GRoadType {
    case detail
    case small
    case middle
    case big
    case provincial
    case national
    case cityHighway
    case highway
    case unknown
}



extension TrafficViewController: GTrafficLayerAdaptor {
    func setTypeForRequestSpeedToState() -> GTrafficStateRequestType {
        return .GPANE
    }
    
    func speed(toState typeValue: NSNumber!, speed: NSNumber!) -> GTrafficState {
        switch convertRoadType(value: typeValue) {
        case .detail:
            if speed.intValue > 20 {
                return .light
            } else if speed.intValue > 10 {
                return .slow
            } else if speed.intValue > 5 {
                return .delay
            } else {
                return .congested
            }
        case .small:
            if speed.intValue > 30 {
                return .light
            } else if speed.intValue > 20 {
                return .slow
            } else if speed.intValue > 10 {
                return .delay
            } else {
                return .congested
            }
        case .middle, .big:
            if speed.intValue > 70 {
                return .light
            } else if speed.intValue > 50 {
                return .slow
            } else if speed.intValue > 30 {
                return .delay
            } else {
                return .congested
            }
        case .provincial:
            if speed.intValue > 50 {
                return .light
            } else if speed.intValue > 30 {
                return .slow
            } else if speed.intValue > 10 {
                return .delay
            } else {
                return .congested
            }
        case .national:
            if speed.intValue > 60 {
                return .light
            } else if speed.intValue > 40 {
                return .slow
            } else if speed.intValue > 30 {
                return .delay
            } else {
                return .congested
            }
        case .cityHighway:
            if speed.intValue > 70 {
                return .light
            } else if speed.intValue > 50 {
                return .slow
            } else if speed.intValue > 30 {
                return .delay
            } else {
                return .congested
            }
        case .highway:
            if speed.intValue > 80 {
                return .light
            } else if speed.intValue > 60 {
                return .slow
            } else if speed.intValue > 40 {
                return .delay
            } else {
                return .congested
            }
        default:
            return .congested
        }
    }
    
    
    func convertRoadType(value:NSNumber) -> GRoadType{
        if value == 61 {
            return .detail
        } else if value == 62 {
            return .small
        } else if value == 63 {
            return .middle
        } else if value == 64 {
            return .big
        } else if value == 65 {
            return .provincial
        } else if value == 66 {
            return .national
        } else if value == 67 {
            return .cityHighway
        } else if value == 68 {
            return .highway
        } else {
            return .middle
        }
    }
    
}
