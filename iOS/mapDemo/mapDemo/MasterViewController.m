//
//  MasterViewController.m
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import "MasterViewController.h"

#import "AppDelegate.h"
#import "BaseSampleViewController.h"
#import <gmaps/gmaps.h>

#include <objc/runtime.h>

@interface MasterViewController () {
    NSArray *_samples;
    BOOL _isIphone;
    UIPopoverController *_popover;
    UIBarButtonItem *_splitViewButtonItem;
}
@end

@implementation MasterViewController
							
- (void)viewDidLoad
{
    [super viewDidLoad];
     _samples = [self findAllOf:[BaseSampleViewController class]];
    _isIphone = [[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone;
    self.title = NSLocalizedString(@"Map Demo", @"Map Demo");
    
    if(!_isIphone) {
        self.clearsSelectionOnViewWillAppear = NO;
        self.contentSizeForViewInPopover = CGSizeMake(320.0, 600.0);
       [self loadDemo:0];
    }
    
    NSLog(@"====== supported tileformat rev : %d", [GMapShared getSupportedTileFormat]);

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSArray *)findAllOf:(Class)defaultClass {
    int count = objc_getClassList(NULL, 0);
    if (count <= 0) {
        @throw @"Couldn't retrieve Obj-C class-list";
        return nil;//[NSArray arrayWithObject:defaultClass];
    }
    
    NSMutableArray *output = [NSMutableArray array];
    Class *classes = (Class *) malloc(sizeof(Class) * count);
    objc_getClassList(classes, count);
    for (int i = 0; i < count; ++i) {        
        if (defaultClass == class_getSuperclass(classes[i])) {
            [output addObject:classes[i]];
        }
    }
    [output sortUsingComparator:^NSComparisonResult(Class cls1, Class cls2) {
        return [[cls1 description] compare:[cls2 description]];
    }];
    
    free(classes);
    return [NSArray arrayWithArray:output];
}

#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _samples.count;
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle
                                      reuseIdentifier:CellIdentifier];
        if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
    }


    Class class = [_samples objectAtIndex:indexPath.row];
    cell.textLabel.text = [class description];
    cell.detailTextLabel.text = [class detailText];
    
    return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return NO;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self loadDemo:indexPath.row];
}

- (void)loadDemo:(NSInteger)index {
    Class sampleClass = _samples[index];
    _sampleMapViewController = [[sampleClass alloc] init];
    
    if (_isIphone) {
        [self.navigationController pushViewController:self.sampleMapViewController animated:YES];
    } else {
        [self.appDelegate setSample:self.sampleMapViewController];
        [_popover dismissPopoverAnimated:YES];
    }
    [self updateLeftBarButtonItem];
}

#pragma mark - SplitView

- (void)splitViewController:(UISplitViewController *)splitController
     willHideViewController:(UIViewController *)viewController
          withBarButtonItem:(UIBarButtonItem *)barButtonItem
       forPopoverController:(UIPopoverController *)popoverController {
    _popover = popoverController;
    _splitViewButtonItem = barButtonItem;
    _splitViewButtonItem.title = NSLocalizedString(@"SDKDemo", @"SDKDemo");
    _splitViewButtonItem.style = UIBarButtonItemStyleDone;
    [self updateLeftBarButtonItem];
}

- (void)splitViewController:(UISplitViewController *)splitController
     willShowViewController:(UIViewController *)viewController
  invalidatingBarButtonItem:(UIBarButtonItem *)barButtonItem {
    _popover = nil;
    _splitViewButtonItem = nil;
    [self updateLeftBarButtonItem];
}

- (void)updateLeftBarButtonItem {
    _sampleMapViewController.navigationItem.leftBarButtonItem = _splitViewButtonItem;
}

@end
