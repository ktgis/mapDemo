//
//  DaraImportViewController.h
//  SDKDemo
//
//  Created by moon on 2015. 2. 11..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import "BaseSampleViewController.h"
#import <gmaps/gmaps.h>

@interface DataImportViewController : BaseSampleViewController <GImportStateDelegate>

@property long inserterHandle;

@end