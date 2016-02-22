package com.example.exercise;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class RunActivity extends Activity {

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myLocationListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    
    private Handler handler = new Handler(); 
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private static int LOCATION_COUTNS = 0;
    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    Button startButton;
    boolean isFirstLoc = true; // 是否首次定位
    TextView TextViewLocInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        requestLocButton = (Button) findViewById(R.id.button_run_change);
        startButton = (Button) findViewById(R.id.button_run_start);
        TextViewLocInfo = (TextView)findViewById(R.id.textView_geo);
        mCurrentMode = LocationMode.NORMAL;
        requestLocButton.setText("普通");

        requestLocButton.setOnClickListener(requestListener);
        startButton.setOnClickListener(startListener);

        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(radioButtonListener);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
	    option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	    option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);

    }
    
    OnClickListener requestListener = new OnClickListener() {
        public void onClick(View v) {
            switch (mCurrentMode) {
                case NORMAL:
                    requestLocButton.setText("跟随");
                    mCurrentMode = LocationMode.FOLLOWING;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                    break;
                case COMPASS:
                    requestLocButton.setText("普通");
                    mCurrentMode = LocationMode.NORMAL;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                    break;
                case FOLLOWING:
                    requestLocButton.setText("罗盘");
                    mCurrentMode = LocationMode.COMPASS;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                    break;
                default:
                    break;
            }
        }
    };
    OnClickListener startListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{

    		if (mLocClient.isStarted()) 
    		{
    		     startButton.setText("开始");
    		     mLocClient.stop();
    		}else 
    		{
    		     startButton.setText("停止");
    		     mLocClient.start();
    		     handler.post(task);
    		}
    	}
    };

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            Log.d("GEO",Double.toString(locData.latitude));
            Log.d("GEO",Double.toString(locData.longitude));
            Log.d("GEO",Double.toString(location.getOperators()));
            
            StringBuffer sb = new StringBuffer(256);
            sb.append("Time : ");
            sb.append(location.getTime());
            sb.append("\nError code : ");
            sb.append(location.getLocType());
            sb.append("\nLatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nLontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nRadius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
             sb.append("\nSpeed : ");
             sb.append(location.getSpeed());
             sb.append("\nSatellite : ");
             sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
             sb.append("\nAddress : ");
             sb.append(location.getAddrStr());
            }
            LOCATION_COUTNS ++;
            sb.append("\n检查位置更新次数：");
            sb.append(String.valueOf(LOCATION_COUTNS));
            
            TextViewLocInfo.setText(sb.toString());
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private Runnable task = new Runnable() {  
        public void run() {   
            // TODO Auto-generated method stub
                handler.postDelayed(this,5*1000);//设置延迟时间，此处是5秒
                Log.d("GEO","restart");
                mLocClient.requestLocation();
        }   
    };


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
