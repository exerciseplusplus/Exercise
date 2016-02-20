//
//  MapHeaderCell.h
//  Exercise+
//
//  Created by wzy on 16/2/9.
//  Copyright © 2016年 王振宇. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
@interface MapHeaderCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet MKMapView *mapView;

@end
