//
//  ViewpointChangeTestController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 2. 9..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "MapApiTestController.h"
#import <gmaps/gmaps.h>

#define _동작대교 [[GLatLngBounds alloc]initWithMin:[[GLatLng alloc]initWithLat:37.50496396044253 lng:126.98003349536177] max:[[GLatLng alloc]initWithLat:37.51575873924737 lng:126.98335333044616]]
#define _동작대교_Katech [[GKatechBounds alloc]initWithMin:[[GKatech alloc]initWithX:310018 y:545248] max:[[GKatech alloc]initWithX:310325 y:546443]]

@interface MapApiTestController () {
    CGPoint _pivot;
    GUtmk* _target;
    GMarker* _marker1;
    GMarker* _marker2;
    GMarker* _pivotMarker;
    GMarker* _targetMarker;
}

@end

@implementation MapApiTestController

+ (NSString *)description {
    return @"Map API";
}

+ (NSString *)detailText {
    return @"Map API Test";
}

-(void)viewDidLoad {
    [super viewDidLoad];
    [self createMapView];
    
    [self.mapView changeViewpoint: [[[[[GViewpointChange builder]tiltTo:0]zoomTo:11] panTo:[[GUtmk alloc] initWithX:953894 y:1952660]] build]];
    self.navigationItem.rightBarButtonItem =
        [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(doit)];
    _pivot.x = self.view.frame.size.width / 2.0;
    _pivot.y = self.view.frame.size.height * 9.0 / 10.0;
    
    id<GCoord> pivotCoord = [self.mapView coordFromViewportPoint:_pivot];
    GMarker* pivotMarker = [GMarker markerWithPosition:pivotCoord];
    [self.mapView addOverlay:pivotMarker];
    _pivotMarker = pivotMarker;
    
    _target = [self.mapView coordFromViewportPoint: CGPointMake(self.view.frame.size.width * 6.0 / 10.0, self.view.frame.size.height * 9.0/ 10.0)];
    GMarker* marker = [GMarker markerWithPosition:_target];
    [self.mapView addOverlay:marker];
    _targetMarker = marker;
}

- (void)mapView:(GMapView *)mapView didChangeViewpoint:(GViewpoint *)viewpoint withGesture:(BOOL)gesture {
    id<GCoord> pivotCoord = [self.mapView coordFromViewportPoint:_pivot];
    _pivotMarker.position = pivotCoord;
}

- (void)mapView:(GMapView *)mapView didIdleAtViewpoint:(GViewpoint *)viewpoint {
    NSLog(@"idle");
    _target = [self.mapView coordFromViewportPoint: CGPointMake(self.view.frame.size.width * 6.0 / 10.0, self.view.frame.size.height * 9.0/ 10.0)];
    _targetMarker.position = _target;
}

-(void)doit {
    // [self.mapView animateViewpoint:[GViewpointChange fitBounds: _동작대교_Katech padding:200]];
    [self.mapView animateViewpoint:[[[[[[GViewpointChange builder] panTo:_target] rotateBy: 70]pivot:_pivot] zoomBy:.5] build]  duration:3000 animationTiming:GAnimationTimingLinear];
}

@end
