//
//  TrafficInfoController.h
//  SDKDemo
//
//  Created by gisdev on 2015. 5. 6..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "BaseSampleViewController.h"

@interface TrafficInfoController : BaseSampleViewController

@property id<GNetworkLayerDelegate> networkLayerDelegate;
@property id<GTrafficLayerAdaptor> adaptor;
@end
