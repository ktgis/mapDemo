//
//  LayersViewController.h
//  SDKDemo
//
//  Created by gisdev on 2015. 6. 19..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "BaseSampleViewController.h"
#import "GTrafficAdaptor.h"


@interface LayersViewController : BaseSampleViewController

@property GTrafficAdaptor* trafficInfoAdaptor;

-(void)toggleLowLevel;

-(void)toggleBackground;

-(void)toggleBuilding;

-(void)toggleOverlay;

-(void)toggleLabel;

-(void)toggleLowLevelLabel;

-(void)toggleNetwork;

@end
