//
//  DetailViewController.h
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <gmaps/gmaps.h>

@interface BaseSampleViewController : UIViewController <GMapViewDelegate>

@property (nonatomic) GMapView *mapView;

+ (NSString *)detailText;

- (void)createMapView;
- (void)createControls;
- (void)onZoomIn;
- (void)onZoomOut;

@end
