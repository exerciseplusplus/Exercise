package com.example.exercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avos.avoscloud.LogUtil;
import com.squareup.timessquare.CalendarPickerView;


public class RunPlanActivity extends Activity {

    int planId;
    String planName;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    String planInfo[][]=new String[28][2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_plan);
        Intent intent=getIntent();
        planId=intent.getIntExtra("planId",0);
        switch (planId)
        {
            case 0:planName="锻炼肌肉";break;
            case 1:planName="新手跑步锻炼";break;
            case 2:planName="4K米跑步锻炼";break;
            case 3:planName="8K米跑步锻炼";break;
            default:break;
        }

        getData();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 1);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Date now=new Date(System.currentTimeMillis());
                long days = ((date.getTime() - now.getTime())/(24*60*60*1000));
                if(now.getDate()==date.getDate())
                    days-=1;
                int dayWeek=0;
                try {
                    dayWeek=dayForWeek(now);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Log.e("Recommend",Long.toString(days));
                Log.e("Recommend", Integer.toString(now.getDate()));
                Log.e("Recommend", Integer.toString(date.getDate()));
                int key =(int)((days+dayWeek)%28);
                showInfo(planInfo[key][0], planInfo[key][1]);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }
    public  int dayForWeek(Date date) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    private void showInfo(String title,String message) {
        new AlertDialog.Builder(this)
                .setTitle(title).setMessage(message)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }
    private void getData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("RunRecommend");
        final AVUser avUser = AVUser.getCurrentUser();
        query.whereEqualTo("name", planName);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list2, AVException e) {
                if (e == null) {
                    for (int i = 0; i < list2.size(); i++) {
                        AVObject record = list2.get(i);
                        planInfo[i*7+0][0] = record.get("MonTopic").toString();
                        planInfo[i*7+0][1] = record.get("MonDetail").toString();
                        planInfo[i*7+1][0] = "今天休息";
                        planInfo[i*7+1][1] = "要养足精神为以后的锻炼哦";
                        planInfo[i*7+2][0] = record.get("WedTopic").toString();
                        planInfo[i*7+2][1] = record.get("WedDetail").toString();
                        planInfo[i*7+3][0] = "今天休息";
                        planInfo[i*7+3][1] = "要养足精神为以后的锻炼哦";
                        planInfo[i*7+4][0] = record.get("FriTopic").toString();
                        planInfo[i*7+4][1] = record.get("FriDetail").toString();
                        planInfo[i*7+5][0] = "今天休息";
                        planInfo[i*7+5][1] = "要养足精神为以后的锻炼哦";
                        planInfo[i*7+6][0] = "今天休息";
                        planInfo[i*7+6][1] = "要养足精神为以后的锻炼哦";
                    }
                }
            }
        });
    }
}
