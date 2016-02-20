//
//  UserUtility.m
//  Exercise+
//
//  Created by wzy on 16/2/1.
//  Copyright © 2016年 王振宇. All rights reserved.
//

#import "UserUtility.h"

static UserUtility* sharedUserUtility = nil;
@interface UserUtility()


@end

@implementation UserUtility
+ (UserUtility*) sharedUserUtility {
    
    @synchronized(self) {
        if (sharedUserUtility == nil) {
            sharedUserUtility = [[self alloc] init];
        }
    }
    return sharedUserUtility;
}

-(NSString *)userName{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"userName"];
}
-(void)setUserName:(NSString *)userName{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:userName forKey:@"userName"];
    [defaults synchronize];
}

-(void)setPassWord:(NSString *)passWord{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:passWord forKey:@"passWord"];
    [defaults synchronize];
}

-(NSString *)passWord{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"passWord"];
}

-(BOOL)isLogIn {
    if ((self.userName == nil)||(self.passWord == nil)) {
        return false;
    }
    return true;
}

+(instancetype)allocWithZone:(struct _NSZone *)zone {
    @synchronized(self) {
        if (sharedUserUtility == nil) {
            sharedUserUtility = [super allocWithZone:zone];
        }
    }
    return sharedUserUtility;
}

@end
