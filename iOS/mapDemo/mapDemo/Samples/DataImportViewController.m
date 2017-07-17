//
//  BaisicMapViewController.m
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import "DataImportViewController.h"
#import <gmaps/gmaps.h>
#import "ToastView.h"
#import <Foundation/Foundation.h>


static GLatLng* INITIAL_CENTER_LATLNG;
static GViewpoint* INITIAL_VIEWPOINT;

@interface DataImportViewController ()

@end

@implementation DataImportViewController

+ (void) initialize {
    INITIAL_CENTER_LATLNG = [[GLatLng alloc] initWithLat:37.478788111314614 lng: 127.011623276644];
    INITIAL_VIEWPOINT = [[GViewpoint alloc]initWithCenter:INITIAL_CENTER_LATLNG zoom:8 rotation:0 tilt:0];
    
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    
    GMapOptions* mapOptions = [[GMapOptions alloc] init];
    mapOptions.viewpoint = INITIAL_VIEWPOINT;
    
    

    self.mapView = [[GMapView alloc] initWithFrame:self.view.frame mapOptions: mapOptions];
    
    [self.view addSubview:self.mapView];
    
    self.mapView.delegate = self;
    
    GMapShared* mapShared = [GMapShared sharedInstance];
    
    [mapShared addImportStateDeletgate:self];
    
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    
    
    UIButton *btnZoomIn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnZoomIn setBackgroundImage:[UIImage imageNamed:@"zoomin_btn.png"]
                         forState:UIControlStateNormal];
    btnZoomIn.frame = CGRectMake(CGRectGetWidth(self.view.frame) - 50,
                                 CGRectGetHeight(self.view.frame) - 95 ,
                                 40.0, 40.0);
    [self.view addSubview:btnZoomIn];
    btnZoomIn.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleTopMargin;
    
    UIButton *btnZoomOut = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnZoomOut setBackgroundImage:[UIImage imageNamed:@"zoomout_btn.png"]
                          forState:UIControlStateNormal];
    btnZoomOut.frame = CGRectMake(CGRectGetWidth(self.view.frame) - 50,
                                  CGRectGetHeight(self.view.frame) - 50,
                                  40.0, 40.0);
    [self.view addSubview:btnZoomOut];
    btnZoomOut.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleTopMargin;
    
    [btnZoomIn addTarget:self
                  action:@selector(onZoomIn)
        forControlEvents:UIControlEventTouchUpInside];
    
    [btnZoomOut addTarget:self
                   action:@selector(onZoomOut)
         forControlEvents:UIControlEventTouchUpInside];
    
    
    //import button
    UIButton *button = (UIButton*) [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button setTitle:@"Start Import" forState:UIControlStateNormal];
    button.frame = CGRectMake(10, CGRectGetHeight(self.view.frame) - 50, 100, 40);
    [button addTarget:self
               action:@selector(startImport)
     forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];
    
    //cancel button
    UIButton *cancel = (UIButton*) [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [cancel setTitle:@"Stop Import" forState:UIControlStateNormal];
    cancel.frame = CGRectMake(120, CGRectGetHeight(self.view.frame) - 50, 100, 40);
    [cancel addTarget:self
               action:@selector(stopImport)
     forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:cancel];
    
    //replace button
    UIButton *replace = (UIButton*) [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [replace setTitle:@"Replace" forState:UIControlStateNormal];
    replace.frame = CGRectMake(230, CGRectGetHeight(self.view.frame) - 50, 100, 40);
    [replace addTarget:self
               action:@selector(replace)
     forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:replace];
    
    
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(didTapAdd)];

}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

+ (NSString *)description {
    return @"Data Import Demo";
}

+ (NSString *)detailText {
    return @"Import tile data";
}

#pragma mark - UI action methods

- (void)onZoomIn {
    [self.mapView animateViewpoint:[GViewpointChange zoomIn]
                          duration:300];
}

- (void)onZoomOut {
    [self.mapView animateViewpoint:[GViewpointChange zoomOut]
                          duration:300];
}

- (void) didTapAdd {
    
    GMapShared* mapShared = [GMapShared sharedInstance];
    [mapShared clearTileData];
}

- (void) startImport {
    NSLog(@"didTabAdd invoked.");
    GMapShared* mapShared = [GMapShared sharedInstance];
    self.inserterHandle = [mapShared importTileData:[[self home_path] stringByAppendingString:@"/import_test.db"]];
}

-(void) stopImport {
    GMapShared* mapShared = [GMapShared sharedInstance];
    [mapShared cancelImport:self.inserterHandle];
}

- (void) replace {
    GMapShared* mapShared = [GMapShared sharedInstance];
    [mapShared replaceTileData:[[self home_path] stringByAppendingString:@"/replace_test.db"]];
}

#pragma mark - MapView delegate methods

- (void)didChangeViewpoint:(GViewpoint*)viewpoint {
    GUtmk* center =[GUtmk valueOf: self.mapView.viewpoint.center];
    NSLog(@"[BasicMapView] viewpoint change: (%lf, %lf), zoom: %f, rotation: %f, tilt: %f",
          center.x, center.y, viewpoint.zoom, viewpoint.rotation, viewpoint.tilt);
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

- (void)didUpdateProgress:(long)handle progress:(int)progress total:(int)total {
    NSLog(@"DataImportViewController:didUpdateProgress invoked with handle:%ld, progress:%d total:%d", handle, progress, total);
}

- (void)didFinish:(long)handle total:(int)total {
    NSLog(@"DataImportViewController:didFinish invoked with handle:%ld, total:%d", handle, total);
}

- (void)didError:(long)handle errorCode:(int)errorCode {
    switch (errorCode) {
        case IMPORT_ERRORCODE_DB_ERROR:
            NSLog(@"Data import db error handle:%ld.", handle);
            break;
        case IMPORT_ERRORCODE_IO_ERROR:
            NSLog(@"Data import disk io error handle:%ld.", handle);
            break;
        case IMPORT_ERRORCODE_PARSE_ERROR:
            NSLog(@"Data import parse error handle:%ld.", handle);
            break;
            
        default:
            break;
    }
}

- (NSString*) home_path {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    if ([paths count] > 0) {
        NSString *documentsDirectory = [paths objectAtIndex:0];
        return documentsDirectory;
    }
    
    NSLog(@"not found documents");
    return [self bundle_path];
}

- (NSString*) cache_path {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    if ([paths count] > 0) {
        NSString *cachesDirectory = [paths objectAtIndex:0];
        return cachesDirectory;
    }
    
    NSLog(@"not found caches");
    return [self bundle_path];
}

/**
 * ios: bundle
 * android: asset
 */
- (NSString*) bundle_path {
    return [[NSBundle mainBundle] bundlePath];
}

@end
