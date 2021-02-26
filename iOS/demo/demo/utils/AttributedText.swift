//
//  AttributedText.swift
//  demo
//
//  Created by 박주철 on 2021/01/24.
//  Copyright © 2021 박주철. All rights reserved.
//

import Foundation
import UIKit


extension NSMutableAttributedString {
    
    static func makeBoldAttrText(text:String,
                            size : CGFloat = 10,
                            color: UIColor = .black) -> NSMutableAttributedString {
        let attrs = [NSAttributedString.Key.font : UIFont.boldSystemFont(ofSize: 10),
                     NSAttributedString.Key.foregroundColor : color]
        
        return NSMutableAttributedString(string:text, attributes:attrs)
    }
    
    static func makeFontAttrText(text:String,
                          font:UIFont,
                          color: UIColor = .black) -> NSMutableAttributedString {
        let attrs = [NSAttributedString.Key.font : font,
                      NSAttributedString.Key.foregroundColor : color]
        
        return NSMutableAttributedString(string:text, attributes:attrs)
    }
}
