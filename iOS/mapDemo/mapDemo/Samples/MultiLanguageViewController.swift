//
//  MultiLanguageViewController.swift
//  mapDemo
//
//  Copyright (c) 2016년 kt. All rights reserved.
//

import UIKit

class MultiLanguageViewController: BaseSampleViewController {
    
    override class func description() -> String {
        return "Multi-Language Demo"
    }
    
    override class func detailText() -> String {
        return "Korean, English, Chinese, ..."
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        createMapView()
        
        let segmentedControl = UISegmentedControl(items: ["한국어", "English", "中文"])
        segmentedControl.addTarget(self, action: #selector(changeLanguage), for: .valueChanged)
        segmentedControl.selectedSegmentIndex = 0
        navigationItem.titleView = segmentedControl
    }
    
    @objc fileprivate func changeLanguage(_ segment: UISegmentedControl) {
        var language: GLanguage = .korean
        
        switch segment.selectedSegmentIndex {
        case 1:
            language = .english
            break
        case 2:
            language = .chinese
            break
        default:
            language = .korean
            break
        }
        
        mapView.setLanguage(language)
    }


}
