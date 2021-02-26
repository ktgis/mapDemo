//
//  DemoCell.swift
//  demo
//
//  Created by 박주철 on 2021/01/24.
//  Copyright © 2021 박주철. All rights reserved.
//

import UIKit


class DemoCell: UITableViewCell {
    @IBOutlet weak var demoContent: UILabel!
    
    var model: DemoTestModel? {
        didSet {
            let content = NSMutableAttributedString()
            if let title = model?.title {
                content.append(NSMutableAttributedString.makeBoldAttrText(text: title,
                                                                          size: 19,
                                                                          color: .black)
                )
            }
            
            if let description = model?.description {
                content.append(NSMutableAttributedString.makeBoldAttrText(text: "\n"+description,
                                                                          size: 12,
                                                                          color: .gray)
                )
            }
            
            demoContent.attributedText = content
        }
    }
    
}
