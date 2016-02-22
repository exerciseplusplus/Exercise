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
 * ��demo����չʾ��ν�϶�λSDKʵ�ֶ�λ����ʹ��MyLocationOverlay���ƶ�λλ�� ͬʱչʾ���ʹ���Զ���ͼ����Ʋ����ʱ��������
 */
public class RunActivity extends Activity {

    // ��λ���
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

    // UI���
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    Button startButton;
    boolean isFirstLoc = true; // �Ƿ��״ζ�λ
    TextView TextViewLocInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        requestLocButton = (Button) findViewById(R.id.button_run_change);
        startButton = (Button) findViewById(R.id.button_run_start);
        TextViewLocInfo = (TextView)findViewById(R.id.textView_geo);
        mCurrentMode = LocationMode.NORMAL;
        requestLocButton.setText("��ͨ");

        requestLocButton.setOnClickListener(requestListener);
        startButton.setOnClickListener(startListener);

        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(radioButtonListener);

        // ��ͼ��ʼ��
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // ������λͼ��
        mBaiduMap.setMyLocationEnabled(true);
        // ��λ��ʼ��
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ��gps
	    option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
	    option.setIgnoreKillProcess(true);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��  
        option.setCoorType("bd09ll"); // ������������
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);

    }
    
    OnClickListener requestListener = new OnClickListener() {
        public void onClick(View v) {
            switch (mCurrentMode) {
                case NORMAL:
                    requestLocButton.setText("����");
                    mCurrentMode = LocationMode.FOLLOWING;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                    break;
                case COMPASS:
                    requestLocButton.setText("��ͨ");
                    mCurrentMode = LocationMode.NORMAL;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                    break;
                case FOLLOWING:
                    requestLocButton.setText("����");
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
    		     startButton.setText("��ʼ");
    		     mLocClient.stop();
    		}else 
    		{
    		     startButton.setText("ֹͣ");
    		     mLocClient.start();
    		     handler.post(task);
    		}
    	}
    };

    /**
     * ��λSDK��������
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view ���ٺ��ڴ����½��յ�λ��
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
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
            sb.append("\n���λ�ø��´�����");
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
                handler.postDelayed(this,5*1000);//�����ӳ�ʱ�䣬�˴���5��
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
        // �˳�ʱ���ٶ�λ
        mLocClient.stop();
        // �رն�λͼ��
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
