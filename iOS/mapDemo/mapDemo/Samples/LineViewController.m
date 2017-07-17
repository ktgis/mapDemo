//
//  LineViewController.m
//  mapDemo
//
//  Created by jucher park on 2017. 5. 4..
//  Copyright © 2017년 kt. All rights reserved.
//

#import "LineViewController.h"

@interface LineViewController ()

@end

@implementation LineViewController

+ (NSString *)description {
    return @"LineView";
}

+ (NSString *)detailText {
    return @"draw line, draw shape";
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createMapView];
    [self createOptionButton];
    
}

-(void) createOptionButton {
    CGFloat xPivot = 10.0f;
    CGFloat yPivot = 10.0f +
    self.navigationController.navigationBar.frame.size.height +
    self.navigationController.navigationBar.frame.origin.y;
    _drawLineBtn = [UIButton new];
    _drawShapeBtn = [UIButton new];
    
    [_drawLineBtn setFrame:CGRectMake(xPivot, yPivot, 200, 30)];
    yPivot += _drawLineBtn.frame.size.height + 10;
    
    [_drawShapeBtn setFrame:CGRectMake(xPivot, yPivot, 200, 30)];
    yPivot += _drawShapeBtn.frame.size.height + 10;
    
    [_drawShapeBtn setTitle:@"DrawShape" forState:UIControlStateNormal];
    [_drawShapeBtn setBackgroundColor:[UIColor redColor]];
    [_drawLineBtn setTitle:@"DrawLine" forState:UIControlStateNormal];
    [_drawLineBtn setBackgroundColor:[UIColor redColor]];
    
    [_drawLineBtn addTarget:self action:@selector(actionDrawLine:) forControlEvents:UIControlEventTouchUpInside];
    [_drawShapeBtn addTarget:self action:@selector(actionDrawShape:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:_drawShapeBtn];
    [self.view addSubview:_drawLineBtn];
}

#pragma mark - button action
-(void) actionDrawLine:(id) sender {
    
}
-(void) actionDrawShape:(id) sender {
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
