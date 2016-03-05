package com.example.exercise;

import android.app.Activity;
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


    Button startButton ;
    Button countButton ;
    TextView numberTextView;
    Chronometer chronometer;

    int number=0;
    int duration=0;
    boolean isStart=false;
    boolean reStart=false;

    Date time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updown);

        startButton=(Button)findViewById(R.id.button_updown_start);
        countButton=(Button)findViewById(R.id.button_updown_count);

        numberTextView=(TextView)findViewById(R.id.editText_updown_number);

        chronometer=(Chronometer)findViewById(R.id.chronometer_updown_time);
        chronometer.setFormat("计时时间:(%s)");

        startButton.setOnClickListener(startListener);
        countButton.setOnClickListener(countListener);

    }
    OnClickListener startListener =new OnClickListener()
    {
        @Override
        public void onClick(View v) {
            if (isStart)
            {
                Log.d("Updown", "end count");
                Log.d("Updown", Integer.toString(number));
                startButton.setText("重新开始");

                isStart=false;
                reStart=true;
                chronometer.stop();
                String stopTime=chronometer.getText().toString();
                Log.d("Updown", stopTime);
                putData( new Date(System.currentTimeMillis()));

            }else
            {
                if (reStart)
                {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    isStart=true;
                    startButton.setText("停止");
                    chronometer.start();
                }
                else
                {
                    Log.d("Updown", "start count");
                    Log.d("Updown", Integer.toString(number));
                    numberTextView.setText(R.string.updown_number);
                    isStart=true;
                    startButton.setText("停止");
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
                number=0;
                time=new Date(System.currentTimeMillis());

            }
        }
    };

    OnClickListener countListener =new OnClickListener()
    {
        @Override
        public void onClick(View v) {

            String count= Integer.toString(number)+"个俯卧撑";
            numberTextView.setText(count);
            Log.d("Updown", Integer.toString(number));
            if (isStart)
                number++;
        }
    };

    public void  putData(Date now)
    {
        AVObject av =new AVObject("UpdownRecord");
        double duration = (now.getTime() - time.getTime())/1000;
        av.put("duration", duration);
        av.put("number", number);
        av.put("timestamp", time);
        AVUser avUser=AVUser.getCurrentUser();
        av.put("user",avUser);
        av.saveInBackground();
    }

}
