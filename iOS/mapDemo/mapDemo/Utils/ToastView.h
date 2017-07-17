//
//  ToastView.h
//  SDKDemo
//
//  Created by gisdev on 2015. 2. 3..
//  Copyright (c) 2015년 kt. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ToastView : UIView

@property (strong, nonatomic) NSString *text;

+ (void)showToastInParentView: (UIView *)parentView withText:(NSString *)text withDuaration:(float)duration;

@end
