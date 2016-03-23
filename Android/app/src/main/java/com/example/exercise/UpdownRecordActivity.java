package com.example.exercise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdownRecordActivity extends Activity {

    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private static final int send_msg_code =  0x101;
    Button startButton;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updown_record);

        getData();

        new Thread(new Runnable(){
            public void run()
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(send_msg_code);
            }

        }).start();

        lv=(ListView)findViewById(R.id.list_view3);

        startButton=(Button)findViewById(R.id.button_updown_record_start);
        startButton.setOnClickListener(startListener);

    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == send_msg_code){

                SimpleAdapter adapter = new SimpleAdapter(UpdownRecordActivity.this,list,R.layout.vlist,new String[]{"title","info","img"},
                        new int[]{R.id.title,R.id.info,R.id.img});
                lv.setAdapter(adapter);

            }
        }
    };

    View.OnClickListener startListener = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            Intent intent =new Intent(UpdownRecordActivity.this,UpdownActivity.class);
            startActivity(intent);
        }
    };


    private List<Map<String, Object>> getData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("UpdownRecord");
        final AVUser avUser=AVUser.getCurrentUser();
        query.whereEqualTo("user", avUser);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list2, AVException e) {
                if (e == null) {
                    for (int i = 0; i < list2.size(); i++) {
                        AVObject record = list2.get(i);
                        Date datetime = (Date) record.get("timestamp");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = df.format(datetime);
                        String duration = record.get("duration").toString();
                        String number = record.get("number").toString();

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", time + "——" + avUser.getUsername());
                        map.put("info", "锻炼时间——" + duration + "\n俯卧撑个数——" + number);
                        switch (i % 9 + 1) {
                            case 1:
                                map.put("img", R.drawable.one);
                                break;
                            case 2:
                                map.put("img", R.drawable.two);
                                break;
                            case 3:
                                map.put("img", R.drawable.three);
                                break;
                            case 4:
                                map.put("img", R.drawable.four);
                                break;
                            case 5:
                                map.put("img", R.drawable.five);
                                break;
                            case 6:
                                map.put("img", R.drawable.six);
                                break;
                            case 7:
                                map.put("img", R.drawable.seven);
                                break;
                            case 8:
                                map.put("img", R.drawable.eight);
                                break;
                            case 9:
                                map.put("img", R.drawable.nine);
                                break;
                        }
                        list.add(map);

                        Log.d("ListView", time);
                        Log.d("ListView", duration);
                        Log.d("ListView", number);

                    }
                }
            }
        });
        return list;
    }

}