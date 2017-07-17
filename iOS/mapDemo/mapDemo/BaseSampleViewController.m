//
//  DetailViewController.m
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import "BaseSampleViewController.h"

@implementation BaseSampleViewController

- (id)init
{
    self = [super init];
    if (self) {
        self.title = [[self class] description];
    }
    return self;
}

- (void)dealloc
{
    NSLog(@"destroy ViewController ");
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)createMapView {
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

    self.mapView = [[GMapView alloc]initWithFrame:self.view.frame];
    [self.view addSubview:self.mapView];
    self.mapView.delegate = self;
    [self createControls];
}

- (void)createControls {
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
}

- (void)onZoomIn {
    [self.mapView animateViewpoint:[GViewpointChange zoomIn]
                          duration:300];
}

- (void)onZoomOut {
    [self.mapView animateViewpoint:[GViewpointChange zoomOut]
                          duration:300];
}

#pragma mark -

+ (NSString *)detailText {
    return nil;
}

@end
