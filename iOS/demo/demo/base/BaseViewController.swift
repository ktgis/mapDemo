//
//  BaseViewController.swift
//  demo
//
//  Created by KT on 2021/01/25.
//  Copyright © 2021 박주철. All rights reserved.
//

import UIKit


class BaseViewController: UIViewController {
    func setNavigationTitle(_ title:String) {
        navigationItem.title = title
    }
    
    func addMenuButton (){
        let rightButton = UIBarButtonItem.init(title: "+", style: .done, target: self, action:#selector(showMenu))
        navigationItem.setRightBarButton(rightButton, animated: false)
    }
    
    @objc
    open func showMenu() {
        // do nothing
    }
    
    func showAlert(title: String? ,
                         message:String?,
                         closeText:String = "닫기",
                         actions:[String],
                         completion: @escaping (_ text:String, _ index: NSInteger)->Void) {
        let alert: UIAlertController = UIAlertController.init(title: title, message: message, preferredStyle: .alert)
        
        for i in 0..<actions.count {
            let action: UIAlertAction = UIAlertAction.init(title: actions[i], style: .default) { (action) in
                completion(actions[i], i)
            }
            alert.addAction(action)
        }
        let close: UIAlertAction = UIAlertAction.init(title: closeText, style: .default) { (action) in
            
        }
        alert.addAction(close)
        
        alert.modalPresentationStyle = .popover
        
        present(alert, animated: true, completion: {
            
        })
    }
    
    func showAction(title :String,
                    message :String,
                    closeText:String = "닫기",
                    actions :[String],
                    completion :@escaping (_ text :String, _ index :NSInteger) -> Void) {
        let alert :UIAlertController    = UIAlertController.init(title: title, message: message, preferredStyle: .actionSheet)
        for i in 0 ..< actions.count {
            let action :UIAlertAction = UIAlertAction.init(title: actions[i],
                                                           style: .default) { (action) in
                completion(actions[i], i)
            }
            alert.addAction(action)
        }
        
        let action :UIAlertAction = UIAlertAction.init(title: closeText,
                                                       style: .cancel) { (action) in
            
        }
        alert.addAction(action)
        
        
        alert.modalPresentationStyle = .popover
        alert.view.tintColor = UIColor.black

        present(alert, animated: true, completion: {
            
        })
    }
}
