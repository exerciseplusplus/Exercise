//
//  RunController.h
//  Exercise+
//
//  Created by wzy on 16/2/8.
//  Copyright © 2016年 王振宇. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RunController : UIViewController
@property (strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property(strong,nonatomic) UINavigationController* navigation;
@end
