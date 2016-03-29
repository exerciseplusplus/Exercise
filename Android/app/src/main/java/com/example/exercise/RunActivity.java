package com.example.exercise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.example.exercise.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;

import com.example.exercise.service.LocationService;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import android.app.Activity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

/***
 * 定位滤波demo，实际定位场景中，可能会存在很多的位置抖动，此示例展示了一种对定位结果进行的平滑优化处理
 * 实际测试下，该平滑策略在市区步行场景下，有明显平滑效果，有效减少了部分抖动，开放算法逻辑，希望能够对开发者提供帮助
 * 注意：该示例场景仅用于对定位结果优化处理的演示，里边相关的策略或算法并不一定适用于您的使用场景，请注意！！！
 *
 * @author baidu
 *
 */
public class RunActivity extends Activity {
   public static float[] EARTH_WEIGHT = {0.1f,0.2f,0.4f,0.6f,0.8f};
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    TextView TextViewLocInfo;
    private LocationService locService;
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果

    int count=0;
    double curSpeed = 0;
    AVUser avuser=AVUser.getCurrentUser();
    Rundata rundata ;
    Cursor mCursor;
    String startTime;
    Date time=new Date(System.currentTimeMillis());
    boolean isFirstLoc = true; // 是否首次定位
    boolean isStart = false; // 是否开始定位
    boolean reStart=false;  //是否重新定位
    Chronometer chronometer;
    Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        mMapView = (MapView) findViewById(R.id.bmapView);
        startButton = (Button) findViewById(R.id.button_run_start);
        TextViewLocInfo = (TextView)findViewById(R.id.textView_geo);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime  =  df.format(time);
        chronometer=(Chronometer)findViewById(R.id.chronometer_run_time);
        chronometer.setFormat("计时时间:(%s)");

        startButton.setOnClickListener(startListener);
        rundata = new Rundata(this,"myDB.db",null,1);
        rundata.getWritableDatabase();

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        locService = ((App) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");

        mOption.setOpenGps(true); // 打开gps;
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setCoorType("bd09ll"); // 设置坐标类型
        mOption.setScanSpan(5000);
        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        //locService.start();
    }

    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            count++;
            Log.d("Loc",Integer.toString(count));
            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
                Message locMsg = locHander.obtainMessage();
                Bundle locData;
                locData = Algorithm(location);
                if (locData != null) {
                    locData.putParcelable("loc", location);
                    locMsg.setData(locData);
                    locHander.sendMessage(locMsg);
                    MyLocationData locData2 = new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                                    // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
                    mBaiduMap.setMyLocationData(locData2);
                    Log.d("GEO", Double.toString(location.getLongitude()));
                    Log.d("GEO", Double.toString(location.getOperators()));

                    StringBuffer sb = new StringBuffer(256);
                    sb.append("Latitude : ");
                    sb.append(location.getLatitude());
                    sb.append("\nLontitude : ");
                    sb.append(location.getLongitude());
                    if (location.getLocType() == BDLocation.TypeGpsLocation){
                        sb.append("\nSpeed : ");
                        sb.append(location.getSpeed());
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                        sb.append("\nSpeed : ");
                        sb.append(curSpeed);
                    }

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

                    rundata.insert(avuser.getUsername(), startTime,location.getTime(),count, location.getLongitude(), location.getLatitude());

                }
            }
        }
    };

    OnClickListener startListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            if (isStart)
            {
                startButton.setText("开始");
                locService.stop();
                //showData();
                isStart=false;
                reStart=true;
                chronometer.stop();
                computeData();
                count=0;
            }else
            {
                if (reStart)
                {

                    isStart=true;
                    startButton.setText("停止");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    locService.start();

                }
                else
                {
                    isStart=true;
                    startButton.setText("停止");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    locService.start();
                    //handler.post(task);
                    // Toast.makeText(RunActivity.this, "add Successed!", Toast.LENGTH_SHORT).show();
                }
                time=new Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                startTime  =  df.format(time);

            }
        }
    };

    /***
     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
     *
     * @param BDLocation
     * @return Bundle
     */
    private Bundle Algorithm(BDLocation location) {
        Bundle locData = new Bundle();
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);

        }
        return locData;
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private Handler locHander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                BDLocation location = msg.getData().getParcelable("loc");
                int iscal = msg.getData().getInt("iscalculate");
                if (location != null) {
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    // 构建Marker图标
                    BitmapDescriptor bitmap = null;

                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo); // 推算结果

                    // 构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                    // 在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        //WriteLog.getInstance().close();
        locService.unregisterListener(listener);
        locService.stop();
        mMapView.onDestroy();
    }

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
        av.put("user",avUser);
        av.saveInBackground();

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    /**
     * 封装定位结果和时间的实体类
     *
     * @author baidu
     *
     */
    class LocationEntity {
        BDLocation location;
        long time;
    }
}
