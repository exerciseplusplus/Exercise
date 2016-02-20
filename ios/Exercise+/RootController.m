//
//  RootController.m
//  Exercise+
//
//  Created by wzy on 16/1/28.
//  Copyright © 2016年 王振宇. All rights reserved.
//

#import "RootController.h"
#import "UserUtility.h"
#import "RunActivityNavigationViewController.h"
#import "HomeRunCollectionViewController.h"
@interface RootController ()

@end

@implementation RootController

- (void)viewDidLoad {
    [super viewDidLoad];
    RunActivityNavigationViewController* runActivityConatroller = [[UIStoryboard storyboardWithName:@"RunActivity" bundle:nil] instantiateViewControllerWithIdentifier:@"runActivity"];
    HomeRunCollectionViewController* homeRun = (HomeRunCollectionViewController*)runActivityConatroller.topViewController;
    homeRun.managedObjectContext = self.managedObjectContext;
    self.viewControllers = [NSArray arrayWithObjects:runActivityConatroller ,nil];
    //[self initTabBarItem];
    // Do any additional setup after loading the view.
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    if ([[UserUtility sharedUserUtility] isLogIn]) {
        
    }else {
        [self performSegueWithIdentifier: @"showLoginView" sender:self];
    }
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
