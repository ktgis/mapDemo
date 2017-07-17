//
//  ShapesViewController.m
//  SDKDemo
//
//  Created by Lee Hoyeon on 2015. 4. 2..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "ShapesViewController.h"
#import <gmaps/gmaps.h>

@interface ShapesViewController ()

@property (nonatomic) GMapView* mapView;

@end

@implementation ShapesViewController

+ (NSString *)description {
    return @"Shapes Demo";
}

+ (NSString *)detailText {
    return @"Shapes";
}

- (void)viewDidLoad {
    [super viewDidLoad];

    GViewpoint *vPoint = [[GViewpoint alloc]initWithCenter:[[GUtmk alloc]initWithX:957340.0 y:1943258.0] zoom:9 rotation:0 tilt:0];
    GMapOptions* mapOptions = [[GMapOptions alloc] init];
    mapOptions.viewpoint = vPoint;

    self.mapView = [[GMapView alloc] initWithFrame:self.view.frame mapOptions: mapOptions];
    [self.view addSubview:self.mapView];
    
    
    // viewportPointFromCoord 를 여기서 바로 호출하면 잘못된값이 나온다. 다음과 같이 지도가 초기화될때까지 delay 를 주어야 한다.
    [self performSelector:@selector(addShapes) withObject:nil afterDelay:0.1];
    self.mapView.delegate = self;
}

- (void)addShapes {

    GUtmk *c1 = [[GUtmk alloc]initWithX:957590.0 y:1941834.0];
    GUtmk *c2 = [[GUtmk alloc]initWithX:957590.0 y:1943858.0];
    GUtmk *c3 = [[GUtmk alloc]initWithX:959340.0 y:1943858.0];
    GUtmk *c4 = [[GUtmk alloc]initWithX:959340.0 y:1941334.0];

    NSArray *points = [NSArray arrayWithObjects: c1, c3, c2, c4, nil];

    GPolygon *polygon = [GPolygon polygonWithPoints:points];
    
    polygon.strokeColor = [UIColor blackColor];
    polygon.strokeWidth = 2;
    polygon.fillColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.5];
    [self.mapView addOverlay:polygon];

    NSLog(@"Area : [%f]", [polygon getArea]);
    
    GUtmk *pathC1 = [[GUtmk alloc]initWithX:952590.0 y:1941134.0];
    GUtmk *pathC2 = [[GUtmk alloc]initWithX:956640.0 y:1949058.0];
    GUtmk *pathC3 = [[GUtmk alloc]initWithX:957340.0 y:1943258.0];
    GUtmk *pathC4 = [[GUtmk alloc]initWithX:952590.0 y:1949434.0];

    NSArray *pathPoints = [NSArray arrayWithObjects: pathC1, pathC2, pathC3, pathC4, nil];

    NSArray *linePath = @[
                          [[GUtmk alloc]initWithX:956640.0 y:1943258.0],
                          [[GUtmk alloc]initWithX:952590.0 y:1941134.0]];
    GPolyline *polyline = [GPolyline polylineWithPoints:linePath];

    [self.mapView addOverlay:polyline];

    polyline.color = [UIColor magentaColor];
    polyline.width = 3;

    GCircle *circle = [GCircle circleWithOrigin:[[GUtmk alloc]initWithX:955640.0 y:1940258.0] Radius:1000];
    circle.fillColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.1];
    circle.strokeColor = [UIColor blackColor];
    circle.strokeWidth = 1;
    [self.mapView addOverlay:circle];
    
    GRoute *path = [GRoute routeWithPoints:pathPoints BufferWidth:50];
    path.fillColor = [UIColor colorWithRed:1.0 green:0.9 blue:0.25 alpha:1.0];
    path.hasPeriodicImage = true;
    path.period = 300;
    path.periodicImage = [GImage imageWithFileName:@"slash.png"];
    path.strokeColor = [UIColor blackColor];
    path.strokeWidth = 1.0;
    [self.mapView addOverlay:path];
    
    
    NSArray *arrowPathPoints = [NSArray arrayWithObjects:
                                [[GUtmk alloc]initWithX:956836 y:1942881],
                                [[GUtmk alloc]initWithX:956972 y:1942497],
                                [[GUtmk alloc]initWithX:956404 y:1942153], nil];
    GPath *arrowPath = [GPath pathWithPoints:arrowPathPoints BufferWidth:50];
    arrowPath.fillColor = [UIColor colorWithRed:0.0 green:0.7 blue:1.0 alpha:1.0];
    arrowPath.hasArrow = true;
    arrowPath.strokeColor = [UIColor colorWithRed:0.0 green:0.5 blue:0.8 alpha:1.0];
    arrowPath.strokeWidth = 1.0;
    arrowPath.hasLineCap = false;
    [self.mapView addOverlay:arrowPath];

    NSLog(@"length : [%f]", [arrowPath getLength]);
    
    NSArray *dashedPathPoints = [NSArray arrayWithObjects:
                                 [[GUtmk alloc]initWithX:955244 y:1943297],
                                 [[GUtmk alloc]initWithX:956492 y:1943737],
                                 [[GUtmk alloc]initWithX:956164 y:1944553],
                                 [[GUtmk alloc]initWithX:956168 y:1944913],
                                 [[GUtmk alloc]initWithX:956732 y:1945185],
                                 [[GUtmk alloc]initWithX:100 y:100],nil];

    
    GDashedPath *dashedPath = [GDashedPath dashedPathWithPoints:dashedPathPoints BufferWidth:30];
    dashedPath.color = [UIColor colorWithRed:0.92 green:0.12 blue:0.2 alpha:1.0];
    [self.mapView addOverlay:dashedPath];

    NSArray *cPathPoints = [NSArray arrayWithObjects:
                            [[GUtmk alloc]initWithX:915640 y:1940258],
                            [[GUtmk alloc]initWithX:925640 y:1940258],
                            [[GUtmk alloc]initWithX:935640 y:1940258],
                            [[GUtmk alloc]initWithX:945640 y:1940258],
                            [[GUtmk alloc]initWithX:955640 y:1940258],
                            [[GUtmk alloc]initWithX:965640 y:1940258],
                            [[GUtmk alloc]initWithX:975640 y:1940258], nil];
    
    GPath *paths[6];
    NSArray *colors = [NSArray arrayWithObjects:
                       [UIColor redColor],
                       [UIColor orangeColor],
                       [UIColor yellowColor],
                       [UIColor greenColor],
                       [UIColor blueColor],
                       [UIColor purpleColor], nil];
    for (int i = 0; i < 6; i++) {
        NSMutableArray *ps = [[NSMutableArray alloc]init];
        [ps addObject:cPathPoints[i]];
        [ps addObject:cPathPoints[i+1]];
        
        paths[i] = [GPath pathWithPoints:ps BufferWidth:500];
        paths[i].fillColor = colors[i];
    }
//
//    for (int i = 0; i < 6; i++) {
//        [self.mapView addOverlay:paths[i]];
//    }
    
    for (int i = 5; i >= 0; i--) {
        [self.mapView addOverlay:paths[i]];
    }
}
@end
