//
//  GTrafficAdaptor.m
//  mapDemo
//
//  Created by KT on 2017. 7. 17..
//  Copyright © 2017년 kt. All rights reserved.
//

#import "GTrafficAdaptor.h"

@implementation GTrafficAdaptor

-(GTrafficState) speedToState:(NSNumber *)speed {
    /**
     * 속도에 대한 도로 교통 정보를 정의 하시면 됩니다.
     */
    
    return GTrafficCongested;
}

@end
