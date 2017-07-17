//
//  InfoWindowDemoViewController.h
//  SDKDemo
//
//  Created by gisdev on 2015. 2. 5..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "InfoWindowDemoViewController.h"
#import <gmaps/gmaps.h>
#import "ToastView.h"

static GUtmk* INITIAL_CENTER;
static GViewpoint* INITIAL_VIEWPOINT;
static GUtmk* OLLEH_SQUARE;

static GUtmk* OLLEH_CAMPUS;
static GUtmk* SEOCHO_IC;
static GUtmk* CUSTOM;

@interface InfoWindowDemoViewController ()

@property (nonatomic) GMarker* ollehCampus;
@property (nonatomic) GMarker* seochoIc;
@property (nonatomic) GMarker* gasStation;
@property (nonatomic) GInfoWindow* gasStationInfoWindow;

@end

@implementation InfoWindowDemoViewController

+ (void)initialize {
    INITIAL_CENTER = [[GUtmk alloc] initWithX: 956820 y: 1942285];
    OLLEH_SQUARE = [[GUtmk alloc] initWithX:953894 y:1952660];
    OLLEH_CAMPUS = [[GUtmk alloc] initWithX:957085 y:1944017];
    SEOCHO_IC = [[GUtmk alloc] initWithX:958094 y:1942790];
    CUSTOM = [[GUtmk alloc] initWithX:959021 y:1942696];
    
    INITIAL_VIEWPOINT = [[GViewpoint alloc] initWithCenter:INITIAL_CENTER
                                                      zoom:8
                                                  rotation:0
                                                      tilt:0];
    
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self createMapView];
    
    _ollehCampus = [GMarker markerWithOptions:@{
                                                @"position" :OLLEH_CAMPUS,
                                                @"title" : @"default",
                                                @"subTitle" : @"올레캠퍼스",
                                                @"flat": @true
                                                }];
    _seochoIc = [GMarker markerWithOptions:@{
                                             @"position" :SEOCHO_IC,
                                             @"title" : @"draggable",
                                             @"draggable": @true,
                                             @"flat": @true
                                             }];
    _gasStation = [GMarker markerWithOptions:@{
                                               @"position" :CUSTOM,
                                               @"title" : @"1500",
                                               @"flat": @true
                                               }];
    
    [self.mapView addOverlay:_ollehCampus];
    [self.mapView addOverlay:_seochoIc];
    [self.mapView addOverlay:_gasStation];
}

-(void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                        duration:(NSTimeInterval)duration {
    [self.mapView setFrame:self.view.bounds];
}

+ (NSString *)description {
    return @"InfoWindow Demo";
}

+ (NSString *)detailText {
    return @"InfoWindows";
}

#pragma mark - MapView delegate methods

- (void)mapView:(GMapView*)mapView didLongPressAtCoord:(id<GCoord>)coord {
    GUtmk* utmk = [GUtmk valueOf:coord];
    NSLog(@"[didLongPressAtCoord] coord:%.0f, %.0f", utmk.x, utmk.y);
    [ToastView showToastInParentView:self.view
                            withText:[NSString stringWithFormat:@"InfoWindow added (%.0f, %.0f)", utmk.x, utmk.y]
                       withDuaration:1];
    
    
    GInfoWindow* infoWindow =
        [GInfoWindow infowWindowWithOptions:@{@"position": coord,
                                              @"title": @"overlay",
                                              @"subTitle": [NSMutableString stringWithFormat:@"%d, %d", (int)utmk.x, (int)utmk.y]
                                              }];
    
    [self.mapView addOverlay:infoWindow];
}

- (BOOL)mapView:(GMapView*)mapView didTapOverlay:(GOverlay*)overlay {
    NSLog(@"Overlay %lu tapped", (unsigned long)overlay.uuid);
    
    
    if (_gasStation == overlay) {
        if (!_gasStationInfoWindow) {
            _gasStationInfoWindow = [GInfoWindow infowWindowWithOptions:@{
                                                                         @"position" :[_gasStation position],
                                                                         @"title" : [_gasStation title]
                                                                         }];
        }
        [self.mapView removeOverlay:_gasStation];
        [self.mapView addOverlay:_gasStationInfoWindow];
        
    } else if (_gasStationInfoWindow == overlay) {
        [self.mapView removeOverlay:_gasStationInfoWindow];
        [self.mapView addOverlay:_gasStation];
    } else if ([overlay isKindOfClass:[GInfoWindow class]]) {
        [ToastView showToastInParentView:self.view
                                withText:@"InfoWindow clicked"
                           withDuaration:1];
        if (overlay != [self.mapView getInfoWindow]) {
            // overlay로 추가한 infowindow만 삭제한다.
            [self.mapView removeOverlay:overlay];
        }
    } else if ([overlay isKindOfClass:[GMarker class]]) {
        [ToastView showToastInParentView:self.view
                                withText:@"Marker clicked"
                           withDuaration:1];
        return false;
    }
    return true;
}

- (UIView *)mapView:(GMapView*)mapView didShowInfoWindow:(GInfoWindow*)infoWindow {
    if (_gasStationInfoWindow == infoWindow) {
        
        UIImageView *view = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"infowindow.png"]];
        UIImageView *photo = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"gas.png"]];
        UILabel *title = [[UILabel alloc] initWithFrame:CGRectMake(photo.frame.size.width + 6, 10, 100, 20)];
        
        photo.frame = CGRectMake(6, 10, photo.frame.size.width, photo.frame.size.height);
        [title setFont:[UIFont boldSystemFontOfSize:20]];
        title.adjustsFontSizeToFitWidth = YES;
        title.text = infoWindow.title;
        
        [view addSubview:photo];
        [view addSubview:title];
        
        return view;
    }
    return [self.mapView didShowDefaultInfoWindow: infoWindow];
}

@end
