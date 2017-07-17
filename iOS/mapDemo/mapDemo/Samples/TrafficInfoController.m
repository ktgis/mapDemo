//
//  TrafficInfoController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 5. 6..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "TrafficInfoController.h"
#import "GTrafficAdaptor.h"
#import <gmaps/gmaps.h>

@interface TrafficInfoController()
    @property UILabel* infoLabel;
@end

@implementation TrafficInfoController

+ (NSString *)description {
    return @"TrafficInfo";
}

+ (NSString *)detailText {
    return @"Traffic Info Sample";
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createMapView];
    
    [self.mapView changeViewpoint: [[[GViewpointChange builder] zoomTo: 2] build]];
    
    /** 
     * Adaptor가 없을시 자체 적인 로직으로 도로의 교통정보를 표출합니다.
     */
    GTrafficAdaptor* trafficAdaptor = [[GTrafficAdaptor alloc] init];
    self.mapView.trafficAdaptor = trafficAdaptor;
    self.adaptor = trafficAdaptor;
    
    [self.mapView setNetworkLayerVisible:YES];
    
    UILabel* label = [[UILabel alloc]initWithFrame:CGRectMake(10,
                                                              self.navigationController.navigationBar.frame.origin.y +
                                                              self.navigationController.navigationBar.frame.size.height, 120, 25)];
    label.font = [UIFont systemFontOfSize:10];
    [self.view addSubview:label];
    _infoLabel = label;
}

- (void)mapView:(GMapView *)mapView didChangeViewpoint:(GViewpoint *)viewpoint withGesture:(BOOL)gesture {
    _infoLabel.text = [NSString stringWithFormat:@"zoom: %.1f", viewpoint.zoom ];
}

@end
