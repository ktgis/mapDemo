//
//  BaisicMapViewController.h
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import "BaseSampleViewController.h"
#import "GTrafficAdaptor.h"

@interface BasicMapViewController : BaseSampleViewController

@property GTrafficAdaptor* trafficInfoAdaptor;
@property (nonatomic) UILabel* infoLabel; // resolution 정보
@property (nonatomic) UILabel* versionLabel; // SDK version

-(void)toggleTrafficInfo;

@end
