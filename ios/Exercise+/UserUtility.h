//
//  UserUtility.h
//  Exercise+
//
//  Created by wzy on 16/2/1.
//  Copyright © 2016年 王振宇. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVOSCloud/AVOSCloud.h>
@interface UserUtility : NSObject
+ (UserUtility*) sharedUserUtility;
+ (instancetype)allocWithZone:(struct _NSZone *)zone;
- (BOOL)isLogIn;
@property(nonatomic) NSString* userName;
@property(nonatomic) NSString* passWord;
@end
