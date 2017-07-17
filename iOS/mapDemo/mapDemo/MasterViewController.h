//
//  MasterViewController.h
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import <UIKit/UIKit.h>

@class BaseSampleViewController;
@class AppDelegate;

@interface MasterViewController : UITableViewController <UISplitViewControllerDelegate>

@property (nonatomic, assign) AppDelegate *appDelegate;
@property (strong, nonatomic) BaseSampleViewController *sampleMapViewController;

@end
