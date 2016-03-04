package com.example.exercise;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    Button startButton;
    boolean isFirstLoc = true; // 是否首次定位
    boolean isStart = false; // 是否开始定位
    boolean reStart=false;  //是否重新定位
    TextView TextViewLocInfo;
    Chronometer chronometer;

    //存储相关
    AVUser avuser=AVUser.getCurrentUser();
    Rundata rundata ;
    Cursor mCursor;
    String startTime;
    Date time=new Date(System.currentTimeMillis());
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        startButton = (Button) findViewById(R.id.button_run_start);
        TextViewLocInfo = (TextView)findViewById(R.id.textView_geo);
        mCurrentMode = LocationMode.NORMAL;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime  =  df.format(time);
        chronometer=(Chronometer)findViewById(R.id.chronometer_run_time);
        chronometer.setFormat("计时时间:(%s)");

        startButton.setOnClickListener(startListener);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps;
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);

        rundata = new Rundata(this,"myDB.db",null,1);
        rundata.getWritableDatabase();

    }




    OnClickListener startListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            if (isStart)
            {
                startButton.setText("开始");
                mLocClient.stop();
                //showData();
                isStart=false;
                reStart=true;
                chronometer.stop();
                computeData();
            }else
            {
                if (reStart)
                {

                    isStart=true;
                    startButton.setText("停止");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    mLocClient.start();

                }
                else
                {
                    isStart=true;
                    startButton.setText("停止");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    mLocClient.start();
                    //handler.post(task);
                    // Toast.makeText(RunActivity.this, "add Successed!", Toast.LENGTH_SHORT).show();
                }
                time=new Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTime  =  df.format(time);

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
            LOCATION_COUTNS ++;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            Log.d("GEO",Double.toString(locData.latitude));
            Log.d("GEO", Double.toString(locData.longitude));
            Log.d("GEO", Double.toString(location.getOperators()));

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

            sb.append("\n检查位置更新次数：");
            sb.append(String.valueOf(LOCATION_COUTNS));

            TextViewLocInfo.setText(sb.toString());
            if (isFirstLoc) {
                isFirstLoc = false;
                startTime=location.getTime();
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            rundata.insert(avuser.getUsername(), startTime,location.getTime(), LOCATION_COUTNS, location.getLongitude(), location.getLatitude());
//            rundata.insert(avuser.getUsername(), startTime,location.getTime(), LOCATION_COUTNS+1, location.getLongitude()+0.005, location.getLatitude()+0.005);
//            rundata.insert(avuser.getUsername(), startTime,location.getTime(), LOCATION_COUTNS+2, location.getLongitude()-0.005, location.getLatitude()+0.01);
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

    public void computeData()
    {
        int i=0;
        Date d1=null;
        double olatitude=0,olongitude=0,distance=0,duration=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            d1 = df.parse(startTime);
        }
        catch (Exception e)
        {
        }
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

                try{
                    d1 = df.parse(startTime);
                    Date d2 = df.parse(time);
                    duration = (d2.getTime() - d1.getTime())/1000;
                }
                catch (Exception e)
                {
                }


                if (i==0)
                {
                    olatitude=latitude;
                    olongitude=longitude;
                    i=1;
                }
                else
                {
                    float[] results=new float[1];
                    Location.distanceBetween(latitude, longitude, olatitude, olongitude, results);
                    distance+= results[0];
                    olatitude=latitude;
                    olongitude=longitude;
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.d("myDB", Double.toString(distance));
        Log.d("myDB", Double.toString(duration));

        AVObject av =new AVObject("RunRecord");
        av.put("duration", duration);
        av.put("distance", distance);
        av.put("timestamp", d1);

        AVUser avUser=AVUser.getCurrentUser();
        Log.d("myDB", avUser.getObjectId());
        JSONObject jo=new JSONObject();
        jo.put("ClassName","_User");
        jo.put("objectId", avUser.getObjectId());
        av.put("user",avUser);
        av.saveInBackground();

    }


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
