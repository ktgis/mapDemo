//
//  LayersViewController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 6. 19..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "LayersViewController.h"
#import <gmaps/gmaps.h>

@implementation LayersViewController

+ (NSString *)description {
    return @"Layers";
}

+ (NSString *)detailText {
    return @"Various layers consist map";
}


-(void) viewDidLoad {
    [super viewDidLoad];
    [self createMapView];
    
    GUtmk *c1 = [[GUtmk alloc]initWithX:964665 y:1946082];
    GViewpoint* viewpoint = [[GViewpoint alloc]initWithCenter:c1 zoom:11 rotation:0 tilt:65];
    [self.mapView moveTo: viewpoint];
    GMarker *marker1 = [GMarker markerWithOptions:@{
                                                        @"position" :c1
                                                        }];
    [self.mapView addOverlay:marker1];
    
    self.mapView.lowLevelLabelLayerVisible = NO;
    self.mapView.lowLevelBackgroundLayerVisible = NO;
    self.mapView.backgroundLayerVisible = NO;
    self.mapView.roadLayerVisible = NO;
    self.mapView.labelLayerVisible = NO;
    self.mapView.overlayLayerVisible = NO;
    self.mapView.buildingLayerVisible = NO;
    [self.mapView setBuilding3dMinZoom:11];

    const int height = 30;
    int ypos = 40;
    [self createToggleButton:@"저레벨 배경" action:@selector(toggleLowLevelBackground) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"배경" action:@selector(toggleBackground) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"도로" action:@selector(toggleRoad) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"네트워크" action:@selector(toggleNetwork) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"빌딩" action:@selector(toggleBuilding) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"저레벨 주기" action:@selector(toggleLowLevelLabel) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"주기" action:@selector(toggleLabel) ypos:ypos];
    ypos += height;
    [self createToggleButton:@"오버레이" action:@selector(toggleOverlay) ypos:ypos];
    
    self.trafficInfoAdaptor = [[GTrafficAdaptor alloc] init];
    self.mapView.trafficAdaptor = self.trafficInfoAdaptor;
}

-(void)createToggleButton:(NSString*)buttonTitle action:(SEL)action ypos:(int)ypos {
    UIButton* button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button setTitle:buttonTitle forState:UIControlStateNormal];
    button.frame = CGRectMake(10, self.navigationController.navigationBar.frame.size.height + ypos, 150, 30);
    button.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    button.contentEdgeInsets = UIEdgeInsetsMake(0, 10, 0, 0);
    [button addTarget:self action:action forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:button];    
}

-(void)toggleLowLevelBackground {
    self.mapView.lowLevelBackgroundLayerVisible = !self.mapView.lowLevelBackgroundLayerVisible;
}

-(void)toggleBackground {
    self.mapView.backgroundLayerVisible = !self.mapView.backgroundLayerVisible;
}

-(void)toggleRoad {
    self.mapView.roadLayerVisible = !self.mapView.roadLayerVisible;
}


-(void)toggleBuilding {
    self.mapView.buildingLayerVisible = !self.mapView.buildingLayerVisible;
}

-(void)toggleOverlay {
    self.mapView.overlayLayerVisible = !self.mapView.overlayLayerVisible;
}

-(void)toggleLabel {
    self.mapView.labelLayerVisible = !self.mapView.labelLayerVisible;
}

-(void)toggleLowLevelLabel {
    self.mapView.lowLevelLabelLayerVisible = !self.mapView.lowLevelLabelLayerVisible;
}

-(void)toggleNetwork {
    self.mapView.networkLayerVisible = !self.mapView.networkLayerVisible;
}

@end
