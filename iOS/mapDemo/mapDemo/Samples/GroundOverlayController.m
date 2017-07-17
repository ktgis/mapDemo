//
//  GroundOverlayController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 4. 2..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "GroundOverlayController.h"
#import <gmaps/gmaps.h>

@implementation GroundOverlayController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    [self createMapView];
    GUtmk *c1 = [[GUtmk alloc] initWithX:957590.0 y:1941834.0];
    GUtmk *c2 = [[GUtmk alloc] initWithX:959640.0 y:1943858.0];
    GUtmkBounds *bounds = [[GUtmkBounds alloc] initWithMin:c1 max:c2];
    GGroundOverlay *groundOverlay = [GGroundOverlay groundOverlayWithBounds:bounds
                                                                      image:[GImage imageWithUIImage:[UIImage imageNamed:@"c00000180.png"]]];
    [self.mapView addOverlay:groundOverlay];
}

+ (NSString *)description {
    return @"GroundOverlay";
}

+ (NSString *)detailText {
    return @"GroundOverlay Demo";
}

@end
