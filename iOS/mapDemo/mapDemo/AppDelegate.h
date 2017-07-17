//
//  AppDelegate.h
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import <UIKit/UIKit.h>

@class BaseSampleViewController;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UINavigationController *navigationController;
@property (strong, nonatomic) UISplitViewController *splitViewController;
@property (strong, nonatomic) BaseSampleViewController* sample;

@end
