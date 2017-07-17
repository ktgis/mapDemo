//
//  ImagesTestViewController.m
//  SDKDemo
//
//  Created by Lee Hoyeon on 2015. 4. 7..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "ImagesTestViewController.h"
#import <gmaps/gmaps.h>

@interface ImagesTestViewController ()

@property (nonatomic) GMapView* mapView;

@end
@implementation ImagesTestViewController
+ (NSString *)description {
    return @"ImagesTest Demo";
}

+ (NSString *)detailText {
    return @"GImage";
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mapView = [[GMapView alloc]initWithFrame:self.view.frame];
    [self.view addSubview:self.mapView];
    
    // viewportPointFromCoord 를 여기서 바로 호출하면 잘못된값이 나온다. 다음과 같이 지도가 초기화될때까지 delay 를 주어야 한다.
    [self performSelector:@selector(addShapes) withObject:nil afterDelay:0.1];
    self.mapView.delegate = self;
}

- (void)addShapes {
    NSString *imageName = @"c00000180.png";
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:imageName ofType:nil];
    
    UIImage *uiimage1 =[UIImage imageWithContentsOfFile:imagePath];
    UIImage *uiimage2 =[UIImage imageNamed:imageName];
    
    GImage *image1 = [GImage imageWithFileName:imageName];
    GImage *image2 = [GImage imageWithUIImage:uiimage1];
    GImage *image3 = [GImage imageWithUIImage:uiimage2];

    GUtmkBounds *mb1 = [[GUtmkBounds alloc]initWithMin:[[GUtmk alloc]initWithX:957590.0 y:1941834.0] max:[[GUtmk alloc]initWithX:959640.0 y:1943858.0]];
    GGroundOverlay *groundOverlay1 = [GGroundOverlay groundOverlayWithBounds :mb1 image:image1];
    [self.mapView addOverlay:groundOverlay1];


    GUtmkBounds *mb2 = [[GUtmkBounds alloc]initWithMin:[[GUtmk alloc]initWithX:954590.0 y:1941834.0] max:[[GUtmk alloc]initWithX:957640.0 y:1943858.0]];
    GGroundOverlay *groundOverlay2 = [GGroundOverlay groundOverlayWithBounds :mb2 image:image2];
    [self.mapView addOverlay:groundOverlay2];
    
    
    GUtmkBounds *mb3 = [[GUtmkBounds alloc]initWithMin:[[GUtmk alloc]initWithX:950590.0 y:1941834.0] max:[[GUtmk alloc]initWithX:953640.0 y:1943858.0]];
    GGroundOverlay *groundOverlay3 = [GGroundOverlay groundOverlayWithBounds :mb3 image:image2];
    [self.mapView addOverlay:groundOverlay3];

}
@end
