//
//  BaisicMapViewController.m
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import "BasicMapViewController.h"
#import <gmaps/gmaps.h>
#import "ToastView.h"

static GUtmk* INITIAL_CENTER;
static GLatLng* INITIAL_CENTER_LATLNG;
static GKatech* INITIAL_CENTER_KATECH;
static GViewpoint* INITIAL_VIEWPOINT;
static GUtmk* OLLEH_SQUARE;
static GKatech* OLLEH_SQUARE_KATECH;
static GLatLng* OLLEH_SQUARE_LATLNG;

static GUtmk* startPoint;
static GUtmk* destPoint;


@implementation BasicMapViewController

+ (void) initialize {
    INITIAL_CENTER = [[GUtmk alloc] initWithX: 956820 y: 1942285];
    INITIAL_CENTER_KATECH = [[GKatech alloc] initWithX:312781.30864958704 y:542314.0857445739]; // 957007.2,1941979.5
    INITIAL_CENTER_LATLNG = [[GLatLng alloc] initWithLat:37.478788111314614 lng: 127.011623276644];
    OLLEH_SQUARE = [[GUtmk alloc] initWithX:953894 y:1952660];
    OLLEH_SQUARE_KATECH = [[GKatech alloc] initWithX:309909.3874752879 y:552708.5109550445]; //954080.8,1952355.8
    OLLEH_SQUARE_LATLNG = [[GLatLng alloc] initWithLat:37.57215919185796 lng:126.97787936105385];
    INITIAL_VIEWPOINT = [[GViewpoint alloc]initWithCenter:INITIAL_CENTER_LATLNG zoom:8 rotation:0 tilt:0];

    startPoint = [[GUtmk alloc]initWithX:957590.0 y:1941834.0];
    destPoint = [[GUtmk alloc]initWithX:959640.0 y:1943858.0];
}


- (void)viewDidLoad
{
    [super viewDidLoad];

    
    
    GMapOptions* mapOptions = [[GMapOptions alloc] init];
    mapOptions.viewpoint = INITIAL_VIEWPOINT;
    
    self.mapView = [[GMapView alloc] initWithFrame:self.view.frame mapOptions: mapOptions];
    [self.view addSubview:self.mapView];
    self.mapView.delegate = self;
    
    [self createControls];
    
    
    
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd
                                                                                           target:self
                                                                                           action:@selector(didTapAdd)];
    
    _versionLabel = [[UILabel alloc] initWithFrame:CGRectMake(10,
                                                              self.navigationController.navigationBar.frame.origin.y +
                                                              self.navigationController.navigationBar.frame.size.height, 120, 20)];
    _versionLabel.text = [NSString stringWithFormat:@"SDK version: %@", [GMapShared getSDKVersion]];
    _versionLabel.font = [UIFont systemFontOfSize:12];
    [self.view addSubview:_versionLabel];
    
    _infoLabel = [[UILabel alloc] initWithFrame:CGRectMake(10,
                                                           self.navigationController.navigationBar.frame.origin.y +
                                                           self.navigationController.navigationBar.frame.size.height + 20, 120, 15)];
    _infoLabel.font = [UIFont systemFontOfSize:12];
    [self.view addSubview:_infoLabel];

    self.navigationItem.rightBarButtonItem =
        [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(didTapAdd)];
    
    UIButton* toggleTrafficInfo = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [toggleTrafficInfo setTitle:@"Traffic Info" forState:UIControlStateNormal];
    toggleTrafficInfo.frame = CGRectMake(10, self.view.frame.size.height - 50, 110, 40);
    [toggleTrafficInfo addTarget:self action:@selector(toggleTrafficInfo) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:toggleTrafficInfo];
    
    self.trafficInfoAdaptor = [[GTrafficAdaptor alloc] init];
    self.mapView.trafficAdaptor = self.trafficInfoAdaptor;
    
    
    
}


-(void)toggleTrafficInfo {
    self.mapView.networkLayerVisible = !self.mapView.networkLayerVisible;
}

-(void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [self.mapView setFrame:self.view.bounds];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

+ (NSString *)description {
    return @"Basic Map Demo";
}

+ (NSString *)detailText {
    return @"Zoom, Touch Gestures";
}

#pragma mark - UI action methods

- (void) didTapAdd {
    
    
    //uncomment following lines to test fitBounds
    GUtmkBounds* destBounds = [[GUtmkBounds alloc]initWithMin:startPoint max:destPoint];
    [self.mapView animateViewpoint:[GViewpointChange fitBounds:destBounds] duration:1000];
}

#pragma mark - MapView delegate methods

- (void)mapView:(GMapView*)mapView didChangeViewpoint:(GViewpoint*)viewpoint withGesture:(BOOL)gesture {
    GUtmk* center =[GUtmk valueOf: self.mapView.viewpoint.center];
    NSLog(@"[BasicMapView] viewpoint change: (%lf, %lf), zoom: %f, rotation: %f, tilt: %f, gesture: %s",
          center.x, center.y, viewpoint.zoom, viewpoint.rotation, viewpoint.tilt, gesture ? "yes" : "no");
    GVisibleRegion* region = self.mapView.visibleRegion;
    GUtmk* nearLeft = [GUtmk valueOf: region.nearLeft];
    GUtmk* nearRight = [GUtmk valueOf: region.nearRight];
    GUtmk* farLeft = [GUtmk valueOf: region.farLeft];
    GUtmk* farRight = [GUtmk valueOf: region.farRight];
    NSLog(@"[BasicMapView] region {nearLeft: (%f,%f) nearRight: (%f,%f) farLeft: (%f,%f) farRight: (%f,%f)"
          , nearLeft.x, nearLeft.y, nearRight.x, nearRight.y, farLeft.x, farLeft.y, farRight.x, farRight.y);
    GUtmkBounds *utmkBounds = [GUtmkBounds valueOf: region.bounds];
    NSLog(@"[BasicMapView] bounds {min: (%f, %f} max: (%f, %f)}", utmkBounds.min.x, utmkBounds.min.y, utmkBounds.max.x, utmkBounds.max.y);
    GLatLng* latlng = [GLatLng valueOf:viewpoint.center];
    NSLog(@"[BasicMapView] latlng: %f, %f", latlng.lat, latlng.lng);
}

- (void)mapView:(GMapView*)mapView didIdleAtViewpoint:(GViewpoint*) viewpoint {
    NSLog(@"[didIdle] (zoom:%.1f)", viewpoint.zoom);
    _infoLabel.text = [NSString stringWithFormat:@"Resolution: %.2fm", [mapView getResolution]];
    // [ToastView showToastInParentView:self.view
    //                      withText:[NSString stringWithFormat:@"Idle (zoom:%.01f)", viewpoint.zoom] withDuaration:1];
}

- (void)mapView:(GMapView*)mapView didTapAtCoord:(id<GCoord>)coord {
    GUtmk* utmk = [GUtmk valueOf:coord];
    NSLog(@"[didTapAtCoord] coord:%.0f, %.0f", utmk.x, utmk.y);
    // [ToastView showToastInParentView:self.view
    //                        withText:[NSString stringWithFormat:@"Tap (%.0f, %.0f)", utmk.x, utmk.y] withDuaration:1];
}

- (void)mapView:(GMapView*)mapView didLongPressAtCoord:(id<GCoord>)coord {
    GUtmk* utmk = [GUtmk valueOf:coord];
    NSLog(@"[didLongPressAtCoord] coord:%.0f, %.0f", utmk.x, utmk.y);
    [ToastView showToastInParentView:self.view
                            withText:[NSString stringWithFormat:@"LongPress (%.0f, %.0f)", utmk.x, utmk.y] withDuaration:1];
    GMarker* marker = [GMarker markerWithPosition:coord];
    [self.mapView addOverlay:marker];
    [marker animate:MARKER_DROP];
}

- (BOOL)mapView:(GMapView*)mapView didTapOverlay:(GOverlay*)overlay {
    NSLog(@"Overlay %lu tapped", (unsigned long)overlay.uuid);
    if ([overlay isKindOfClass:[GMarker class]]) {
        [((GMarker*)overlay) animate:MARKER_FLICK];
    }
    
    [ToastView showToastInParentView:self.view
                            withText:[NSString stringWithFormat:@"Overlay %lu tapped", (unsigned long)overlay.uuid] withDuaration:1];
    return false;
}

- (void)mapView:(GMapView*)mapView didDragOverlay:(GOverlay *)overlay {
    NSLog(@"Overlay %lu is dragging", (unsigned long)overlay.uuid);
    [ToastView showToastInParentView:self.view
                            withText:[NSString stringWithFormat:@"Overlay %lu is dragging", (unsigned long)overlay.uuid] withDuaration:1];
}

@end
