//
//  AppDelegate.m
//  OllehMapSDKDemo
//
//  Created by ik on 7/30/13.
//  Copyright (c) 2013 kt. All rights reserved.
//

#import "AppDelegate.h"

#import "MasterViewController.h"
#import <gmaps/GMapShared.h>
#import "BaseSampleViewController.h"


@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    [GMapShared authKeyRegist:[NSBundle mainBundle]];
    
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    MasterViewController *masterViewController = [[MasterViewController alloc] init];
    masterViewController.appDelegate = self;
    UINavigationController *masterNavigationController = [[UINavigationController alloc] initWithRootViewController:masterViewController];
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        self.navigationController = masterNavigationController;
        self.window.rootViewController = self.navigationController;
    } else {
        UINavigationController *detailNavigationController = [[UINavigationController alloc] initWithRootViewController:[[UIViewController alloc] init]];
        self.splitViewController = [[UISplitViewController alloc] init];
        self.splitViewController.delegate = masterViewController;
        self.splitViewController.viewControllers = @[masterNavigationController, detailNavigationController];
        self.splitViewController.presentsWithGesture = NO;
        self.window.rootViewController = self.splitViewController;
    }
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)setSample:(BaseSampleViewController *)sample {
    UINavigationController *nav = [self.splitViewController.viewControllers objectAtIndex:1];
    [nav setViewControllers:[NSArray arrayWithObject:sample] animated:NO];
}

- (BaseSampleViewController*) sample {
    UINavigationController *nav = [self.splitViewController.viewControllers objectAtIndex:1];
    return [[nav viewControllers] objectAtIndex:0];
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
