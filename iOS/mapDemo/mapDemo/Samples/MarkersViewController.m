
//
//  MarkersViewController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 2. 6..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "MarkersViewController.h"
#import "ToastView.h"

@implementation MarkersViewController

+ (NSString *)description {
    return @"Marker Demo";
}

+ (NSString *)detailText {
    return @"Markers";
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createMapView];
    
    // viewportPointFromCoord 를 여기서 바로 호출하면 잘못된값이 나온다. 다음과 같이 지도가 초기화될때까지 delay 를 주어야 한다.
    [self performSelector:@selector(addMarker) withObject:nil afterDelay:0.1];
}

- (void)addMarker {
    CGPoint point = CGPointMake(self.view.frame.size.width / 2.0, self.view.frame.size.height * 9.0 / 10.0);
    
    id<GCoord> pos1 = [self.mapView coordFromViewportPoint:point];
    GMarker* marker1 = [GMarker markerWithPosition:pos1];
    [self.mapView addOverlay:marker1];
    
    id<GCoord> coord = marker1.position;
    CGPoint point2 = [self.mapView viewportPointFromCoord:coord];
    
    [ToastView showToastInParentView:self.view
                            withText:[NSString stringWithFormat:@"Original: (%f, %f) Converted: (%f, %f)",
                                      point.x, point.y, point2.x, point2.y] withDuaration:10];

    GImage *mImage = [GImage imageWithUIImage:[UIImage imageNamed:@"pink_icon.png"]];
    GUtmk *pos2 = [[GUtmk alloc]initWithX:957590.0 y:1941834.0];
    NSDictionary *captionOption = @{
                             @"captionText" :@"purple marker",
                             @"captionSize" : @6,
                             @"captionColor": [UIColor purpleColor],
                             @"captionStrokeColor": [UIColor whiteColor],
                             @"captionPositionType" : [NSNumber numberWithInteger:CaptionPositionTypeTop]
                             };
    GMarker *purpleMarker = [GMarker markerWithOptions:@{
                                                             @"position" :pos2,
                                                             @"icon": mImage,
                                                             @"flat": @true
                                                             }];
    purpleMarker.captionOptions = captionOption;
    
    GUtmk *pos3 = [[GUtmk alloc]initWithX:959640.0 y:1943858.0];
    GMarker *defaultMarker = [GMarker markerWithPosition:pos3];
    defaultMarker.caption = @"test";
    defaultMarker.draggable = @true;
    defaultMarker.iconSize = CGPointMake(200, 200);

    [self.mapView addOverlay:purpleMarker];
    [self.mapView addOverlay:defaultMarker];

    
    
    GMarker *leftMarker = [GMarker markerWithPosition:[[GUtmk alloc]initWithX:pos3.x + 1000 y:pos3.y - 1000]];
    GMarker *rightMarker = [GMarker markerWithPosition:[[GUtmk alloc]initWithX:pos3.x + 1000 y:pos3.y + 1000]];
    GMarker *topMarker = [GMarker markerWithPosition:[[GUtmk alloc]initWithX:pos3.x - 1000 y:pos3.y - 1000]];
    GMarker *bottomMarker = [GMarker markerWithPosition:[[GUtmk alloc]initWithX:pos3.x - 1000 y:pos3.y + 1000]];


    
    leftMarker.captionOptions =@{
                                 @"captionText" :@"leftMarker",
                                 @"captionPositionType" : [NSNumber numberWithInteger:CaptionPositionTypeLeft]
                                 };
    rightMarker.captionOptions =@{
                                  @"captionText" :@"rightMarker",
                                  @"captionPositionType" : [NSNumber numberWithInteger:CaptionPositionTypeRight]
                                  };
    topMarker.captionOptions =@{
                                @"captionText" :@"topMarker",
                                @"captionPositionType" : [NSNumber numberWithInteger:CaptionPositionTypeTop]
                                };
    bottomMarker.captionOptions =@{
                                   @"captionText" :@"bottomMarker",
                                   @"captionPositionType" : [NSNumber numberWithInteger:CaptionPositionTypeBottom]
                                   };
    
    leftMarker.flat=true;
    rightMarker.flat=true;
    topMarker.flat=true;
    bottomMarker.flat=true;
    
    [self.mapView addOverlay:leftMarker];
    [self.mapView addOverlay:rightMarker];
    [self.mapView addOverlay:topMarker];
    [self.mapView addOverlay:bottomMarker];
}

#pragma mark - MapView delegate methods

- (void)mapView:(GMapView*)mapView didLongPressAtCoord:(id<GCoord>)coord {
    GUtmk* utmk = [GUtmk valueOf:coord];
    NSLog(@"[didLongPressAtCoord] coord:%.0f, %.0f", utmk.x, utmk.y);
    GMarker* marker = [GMarker markerWithPosition:coord];
    [self.mapView addOverlay:marker];
    [marker animate:MARKER_DROP];
}

- (BOOL)mapView:(GMapView*)mapView didTapOverlay:(GOverlay*)overlay {
    NSLog(@"Overlay %lu tapped", (unsigned long)overlay.uuid);
    if ([overlay isKindOfClass:[GMarker class]]) {
        [((GMarker*)overlay) animate:MARKER_FLICK];
    }
    return false;
}

@end
