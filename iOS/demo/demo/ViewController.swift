//
//  ViewController.swift
//  demo
//
//  Created by 박주철 on 2021/01/24.
//  Copyright © 2021 박주철. All rights reserved.
//

import UIKit

class ViewController: BaseViewController {
    
    @IBOutlet weak var demoListView: UITableView!
    
    var demoList:Dictionary<String, Array<DemoTestModel>> = testModelList()
    var keySet: [String] = testKeySet()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        demoListView.dataSource = self
        demoListView.delegate = self
        demoListView.estimatedRowHeight = 55
        demoListView.rowHeight = UITableView.automaticDimension

    }

    func getDemoModel(indexPath: IndexPath) -> DemoTestModel? {
        let section = indexPath.section
        let key = keySet[section]
        if let value = demoList[key] {
            return value[indexPath.row]
        }
        return nil
    }
}

//MARK: - UITableViewDataSource
extension ViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return keySet.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let key = keySet[section]
        
        if let value = demoList[key] {
            return value.count
        }
        
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "DemoCell", for: indexPath) as! DemoCell
        cell.model = getDemoModel(indexPath: indexPath)
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let section = section
        let key = keySet[section]
        
        let frame = CGRect.init(origin: view.frame.origin, size: CGSize(width: view.frame.size.width, height: 30))
        let view = UIView.init(frame: frame)
        let title = UILabel.init(frame: frame)
        title.text = "\(key)"
        title.backgroundColor = .gray
        view.addSubview(title)
        return view
    }
}

//MARK: - UITableViewDelegate
extension ViewController:UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let model = getDemoModel(indexPath: indexPath) {
            let storyBoard = UIStoryboard(name: "Main", bundle: nil)
            let mapViewController = storyBoard.instantiateViewController(identifier: model.identifier)
            self.show(mapViewController, sender: nil)
        }
    }
}
