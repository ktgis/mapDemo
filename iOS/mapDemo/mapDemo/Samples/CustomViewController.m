#import "CustomViewController.h"

@implementation CustomViewController
+ (NSString *)description {
    return @"Custom View Demo";
}

+ (NSString *)detailText {
    return @"Custom View Test";
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:@"CustomView" bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}
@end
