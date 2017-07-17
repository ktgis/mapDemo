//
//  StylesViewController.m
//  SDKDemo
//
//  Created by gisdev on 2015. 2. 9..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "StylesViewController.h"

@interface StylesViewController ()

@property GMapView* mapView;
@property NSString* styleFile;
@property NSArray* styleFiles;

@end

@implementation StylesViewController

+ (NSString *)description {
    return @"Styles";
}

+ (NSString *)detailText {
    return @"Dynamic Style";
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createMapView];
    [self syncStyleFiles];

    NSMutableArray *items = [[NSMutableArray alloc] initWithObjects:@"기본", @"주행", @"N주행", @"외곽X", nil];

    for(NSString *filename in _styleFiles) {
        [items addObject:[[filename lastPathComponent] stringByDeletingPathExtension]];
    }

    UISegmentedControl *segmentedControl = [[UISegmentedControl alloc] initWithItems:items];
    [segmentedControl addTarget:self action:@selector(changeStyle:) forControlEvents:UIControlEventValueChanged];
    segmentedControl.selectedSegmentIndex = 0;

    self.navigationItem.titleView = segmentedControl;
}

- (void)changeStyle:(UISegmentedControl *) segment {

    NSString* stylePath;
    NSString* imagePath;
    NSString* imageInfoPath;

    switch (segment.selectedSegmentIndex) {
        case 0: // 기본
            stylePath = DEFAULT_MAP_STYLE;
            imagePath = DEFAULT_IMAGE;
            imageInfoPath = DEFAULT_IMAGE_INFO;
            break;
        case 1: // 주간주행
            stylePath = [[NSBundle mainBundle] pathForResource:@"day_drive.json" ofType:nil];
            imagePath = DEFAULT_IMAGE;
            imageInfoPath = DEFAULT_IMAGE_INFO;
            break;
        case 2: // 야간주행
            stylePath = [[NSBundle mainBundle] pathForResource:@"night_drive.json" ofType:nil];
            imagePath = [[NSBundle mainBundle] pathForResource:@"new_totalimage.png" ofType:nil];
            imageInfoPath = [[NSBundle mainBundle] pathForResource:@"new_totalimage.txt" ofType:nil];
            break;
        case 3: // 외곽선없음
            stylePath = [[NSBundle mainBundle] pathForResource:@"borderless.json" ofType:nil];
            imagePath = DEFAULT_IMAGE;
            imageInfoPath = DEFAULT_IMAGE_INFO;
            break;
        default: {
            long index = segment.selectedSegmentIndex - 4;
            NSString* filename = [_styleFiles objectAtIndex:index];
            stylePath = [[self style_folder_path] stringByAppendingString:[NSString stringWithFormat:@"/%@", filename]];
            imagePath = DEFAULT_IMAGE;
            imageInfoPath = DEFAULT_IMAGE_INFO;
            break;
        }
    }
    [self.mapView setStyle:stylePath];
    [self.mapView setSystemImage:imagePath imageInfoPath:imageInfoPath];
}

- (NSString *) home_path {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    if ([paths count] > 0) {
        NSString *documentsDirectory = [paths objectAtIndex:0];
        return documentsDirectory;
    }
    return [[NSBundle mainBundle] bundlePath];
}

- (NSString *) style_folder_path {
    NSString *home_path = [self home_path];
    return [NSString stringWithFormat:@"%@/styles", home_path];
}

- (void) syncStyleFiles {
    NSFileManager *fm = [NSFileManager defaultManager];
    NSArray *dirContents = [fm contentsOfDirectoryAtPath:[self style_folder_path] error:nil];
    NSPredicate *fltr = [NSPredicate predicateWithFormat:@"self ENDSWITH '.json'"];
    _styleFiles = [dirContents filteredArrayUsingPredicate:fltr];
}

-(void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                        duration:(NSTimeInterval)duration {
    [self.mapView setFrame:self.view.bounds];
}

@end
