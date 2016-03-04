package com.example.exercise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.LogUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordDetailActivity extends Activity {

    Rundata rundata ;
    Cursor mCursor;

    // 地图相关
    MapView mMapView;
    BaiduMap mBaiduMap;
    Polyline mPolyline;
    BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");

    double [] longi=new double[100];
    double [] lati=new double[100];
    int pointNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        Intent intent=getIntent();
        String time=intent.getStringExtra("time");
        Log.d("GEO",time);

        rundata = new Rundata(this,"myDB.db",null,1);
        rundata.getWritableDatabase();

        getData(time);
        mMapView = (MapView) findViewById(R.id.bmapView2);
        mBaiduMap = mMapView.getMap();
        addLine(lati,longi);
    }

    public void getData(String startTime)
    {
        Cursor cursor=rundata.query(startTime);
        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor
                        .getColumnIndex("user"));
                String time = cursor.getString(cursor
                        .getColumnIndex("nowtime"));
                int seq = cursor.getInt(cursor
                        .getColumnIndex("seq"));
                double longitude = cursor.getDouble(cursor
                        .getColumnIndex("longitude"));
                double latitude = cursor.getDouble(cursor
                        .getColumnIndex("latitude"));
                Log.d("myDB", startTime);
                Log.d("myDB", time);
                Log.d("myDB", Integer.toString(seq));
                Log.d("myDB", Double.toString(longitude));
                Log.d("myDB", Double.toString(latitude));
                longi[pointNumber]=longitude;
                lati[pointNumber]=latitude;
                pointNumber+=1;

            } while (cursor.moveToNext());
        }
    }

    public void addLine(double []lat1,double []long1)
    {
        if (pointNumber==1)
            return;
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i=0;i<pointNumber;i++)
        {
            LatLng p1 = new LatLng(lat1[i],long1[i]);
            points.add(p1);
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

    }
}
