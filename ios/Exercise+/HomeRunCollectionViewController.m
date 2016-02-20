//
//  HomeRunCollectionViewController.m
//  Exercise+
//
//  Created by wzy on 16/2/3.
//  Copyright © 2016年 王振宇. All rights reserved.
//

#import "HomeRunCollectionViewController.h"
#import <CSStickyHeaderFlowLayout/CSStickyHeaderFlowLayout.h>
#import "SectionHeader.h"
#import "HomeRunCell.h"
#import "MapHeaderCell.h"
#import "RunController.h"
#import "Run.h"
#import "PastRunViewController.h"
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

@interface HomeRunCollectionViewController ()<CLLocationManagerDelegate,MKMapViewDelegate,UICollectionViewDelegateFlowLayout>
@property (nonatomic, strong) NSArray *sections;
@property (nonatomic, strong) UINib *headerNib;
@property (nonatomic, strong) CLLocationManager *locationManager;
@property(nonatomic,strong)MKMapView *mapView;
@property (strong, nonatomic) NSArray *runArray;
@end

@implementation HomeRunCollectionViewController

static NSString * const reuseIdentifier = @"Cell";
//@{@"Twitter":@"http://twitter.com"},

- (id)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.sections = @[
                          @{@"锻炼记录":@"http://facebook.com"},
                           @{@"锻炼记录":@"http://facebook.com"},
                           @{@"锻炼记录":@"http://facebook.com"}
                          ];
        
        self.headerNib = [UINib nibWithNibName:@"MapHeader" bundle:nil];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self reloadLayout];
    [self.collectionView registerNib:self.headerNib forSupplementaryViewOfKind:CSStickyHeaderParallaxHeader
withReuseIdentifier:@"header"];
    self.collectionView.showsVerticalScrollIndicator = false;
}


-(void)reloadLayout{
    CSStickyHeaderFlowLayout *layout = (id)self.collectionViewLayout;
    
    if ([layout isKindOfClass:[CSStickyHeaderFlowLayout class]]) {
        layout.parallaxHeaderReferenceSize = CGSizeMake(self.view.frame.size.width, self.view.frame.size.height/3*2);
        layout.itemSize = CGSizeMake(layout.itemSize.width, layout.itemSize.height);
        NSLog(@"width%f",self.view.frame.size.width);

    }
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription
                                   entityForName:@"Run" inManagedObjectContext:self.managedObjectContext];
    [fetchRequest setEntity:entity];
    
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"timestamp" ascending:NO];
    [fetchRequest setSortDescriptors:@[sortDescriptor]];
    
     self.runArray = [self.managedObjectContext executeFetchRequest:fetchRequest error:nil];
    [self.collectionView reloadData];
   // [self reloadLayout];
}


- (IBAction)startToRun:(UIButton *)sender {
     RunController* runController = [[UIStoryboard storyboardWithName:@"RunActivity" bundle:nil] instantiateViewControllerWithIdentifier:@"runController"];
    runController.managedObjectContext = self.managedObjectContext;
    runController.navigation = self.navigationController;
    [self presentViewController:runController animated:true completion:nil];
}

- (void)startLocationUpdates
{
    // Create the location manager if this object does not
    // already have one.
    if (self.locationManager == nil) {
        self.locationManager = [[CLLocationManager alloc] init];
    }
    
    self.locationManager.delegate = self;
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.locationManager.activityType = CLActivityTypeFitness;
    
    // Movement threshold for new events.
    self.locationManager.distanceFilter = 10; // meters
    
    if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.locationManager requestWhenInUseAuthorization];
    }
    [self.locationManager startUpdatingLocation];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark <UICollectionViewDataSource>

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
     return [self.sections count];
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {

    return 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HomeRunCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"homeRunCell"
                                                             forIndexPath:indexPath];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%lu次", (unsigned long)[self.runArray count]];//[[obj allValues] firstObject];
    NSString* indicator = @"\uf3d3";
     NSString* document = @"\uf381";
    cell.indicatorLabel.text = indicator;
    cell.documentIcon.text = document;
    return cell;

}
- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath {
    if ([kind isEqualToString:UICollectionElementKindSectionHeader]) {
        
        NSDictionary *obj = self.sections[indexPath.section];
        
        SectionHeader *cell = [collectionView dequeueReusableSupplementaryViewOfKind:kind
                                                                   withReuseIdentifier:@"sectionHeader"
                                                                          forIndexPath:indexPath];
        
        cell.textLabel.text = [[obj allKeys] firstObject];
        return cell;
    } else if ([kind isEqualToString:CSStickyHeaderParallaxHeader]) {
        MapHeaderCell* cell = [collectionView dequeueReusableSupplementaryViewOfKind:kind
                                                                 withReuseIdentifier:@"header"
                                                                        forIndexPath:indexPath];
        [self startLocationUpdates];
        self.mapView = cell.mapView;
        self.mapView.delegate = self;
        
        cell.mapView.showsUserLocation = true;
        return cell;
    }
    return nil;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"indexPath%@",indexPath);
    PastRunViewController* runController = [[UIStoryboard storyboardWithName:@"RunActivity" bundle:nil] instantiateViewControllerWithIdentifier:@"pastRunViewController"];
    runController.runArray = self.runArray;
    [self.navigationController pushViewController:runController animated:true];
}
//- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section
//{
//    if (section == 0) {
//        return CGSizeZero;
//    }else {
//        return CGSizeMake(CGRectGetWidth(collectionView.bounds), 135);
//    }
//}

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation{
    MKCoordinateRegion region =
    MKCoordinateRegionMakeWithDistance(userLocation.coordinate, 500, 500);
    [self.mapView setRegion:region animated:YES];

}

//- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
//{
//    if ([[segue destinationViewController] isKindOfClass:[PastRunViewController class]]) {
//        PastRunViewController* viewController = (PastRunViewController*)[segue destinationViewController];
//        viewController.runArray = self.runArray;
//    }
//}


@end
