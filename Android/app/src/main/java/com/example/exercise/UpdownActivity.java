package com.example.exercise;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdownActivity extends Activity {

    private SensorManager sensorManager;
    private int count=0;
    private static  String TAG="TEST";
    private Sensor sensor;
    Button startButton ;
    TextView numberTextView;
    Chronometer chronometer;

    boolean isStart=false;
    boolean reStart=false;

    Date time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updown);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        startButton=(Button)findViewById(R.id.button_updown_start);
        numberTextView=(TextView)findViewById(R.id.count);

        chronometer=(Chronometer)findViewById(R.id.chronometer_updown_time);
        chronometer.setFormat("计时时间:(%s)");
        startButton.setOnClickListener(startListener);
    }
    OnClickListener startListener =new OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if (isStart)
            {
                isStart=false;
                Log.d("Updown", "end count");
                Log.d("Updown",count+"");
                numberTextView.setText("恭喜你完成俯卧撑"+count+"个");
                startButton.setText("重新开始锻炼");
                reStart=true;
                chronometer.stop();
                String stopTime=chronometer.getText().toString();
                Log.d("Updown", stopTime);
                putData(new Date(System.currentTimeMillis()));
            }else {
                if (reStart) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    isStart = true;
                    startButton.setText("停止");
                    count=0;
                    numberTextView.setText(count+"");
                    chronometer.start();
                } else {
                    Log.d("Updown", "start count");
                    Log.d("Updown", Integer.toString(count));
                    numberTextView.setText(count + "");
                    isStart = true;
                    startButton.setText("停止");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
                count = 0;
                time = new Date(System.currentTimeMillis());
            }
        }
    };
    public void  putData(Date now)
    {
        AVObject av =new AVObject("UpdownRecord");
        double duration = (now.getTime() - time.getTime())/1000;
        av.put("duration", duration);
        av.put("number", count);
        av.put("timestamp", time);
        AVUser avUser=AVUser.getCurrentUser();
        av.put("user",avUser);
        av.saveInBackground();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(mProximityListener , sensor, SensorManager.SENSOR_DELAY_FASTEST);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(mProximityListener );
        }
    }

    private SensorEventListener mProximityListener  = new SensorEventListener() {
        boolean isHigh=false,isLow=false,isHighAgain=false;
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }

        @Override
        public void onSensorChanged(SensorEvent arg0) {
            float[] val=arg0.values;
            if(val[0]>=sensor.getMaximumRange()){
                isHigh=true;

                //Log.e(TAG, "高度到达");
            }

            if(isHigh&&val[0]<=3){
                isLow=true;
                count++;
                Log.e(TAG, "低度到达");
                Log.e(TAG, "俯卧撑加1");
            }

            if(isLow&&val[0]>=sensor.getMaximumRange()){
                isHighAgain=true;
                //Log.e(TAG, "高度再次到达");
            }

            if(isHighAgain){
                numberTextView.setText(count + "");
                isHigh=isLow=isHighAgain=false;
            }
        }
    };
}