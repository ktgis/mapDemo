//
//  HundredsMarkerController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 4. 2..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "HundredsMarkerController.h"

#import <gmaps/gmaps.h>

@interface HundredsMarkerController() {
    GMarker* _marker1;
    NSMutableArray* _markers;
    BOOL _visible;
}
@end

@implementation HundredsMarkerController

- (void)viewDidLoad {
    _markers = [[NSMutableArray alloc]init];
    
    [super viewDidLoad];
    [self createMapView];
    
    float width = self.view.frame.size.width;
    float height = self.view.frame.size.height;
    for (int i=0; i<10; i++) {
        for (int j=0; j<10; j++) {
            id<GCoord> coord = [self.mapView coordFromViewportPoint:CGPointMake(width * i * 0.1, height * j * 0.1)];
            GMarker* m = [GMarker markerWithPosition:coord];
            [self.mapView addOverlay:m];
            if (i==5 && j==5) {
                m.icon = [GImage imageWithUIImage:[UIImage imageNamed:@"blue_icon.png"]];
                _marker1 = m;
            }
            [_markers addObject:m];
        }
    }
    
    self.navigationItem.rightBarButtonItem =
    [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(doit)];
    _visible = YES;

}

- (void)mapView:(GMapView *)mapView didIdleAtViewpoint:(GViewpoint *)viewpoint {
    NSArray* visibleOverlays = self.mapView.visibleOverlays;
    NSLog(@"visible overlay count: %ld", visibleOverlays.count);
}

- (void)doit {
    for(id marker in _markers) {
        [marker setVisible:!_visible];
    }
    _visible = !_visible;
}

- (void)onZoomIn {
    [super onZoomIn];
    [_marker1 resetZIndex];
}

- (void)onZoomOut {
    [super onZoomOut];
    [_marker1 bringToFront];
}


+ (NSString *)description {
    return @"Hundreds Markers";
}

+ (NSString *)detailText {
    return @"Hundreds Markers Demo";
}

@end
