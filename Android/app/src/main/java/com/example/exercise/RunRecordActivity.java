package com.example.exercise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


public class RunRecordActivity extends Activity {

    Button startButton;
    ListView lv;
    Rundata rundata ;
    Cursor mCursor;

    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private static final int send_msg_code =  0x101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_record);

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

        rundata = new Rundata(this,"myDB.db",null,1);
        rundata.getWritableDatabase();

        lv=(ListView)findViewById(R.id.list_view);

        startButton=(Button)findViewById(R.id.button_run_start);
        startButton.setOnClickListener(startListener);


    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == send_msg_code){

                SimpleAdapter adapter = new SimpleAdapter(RunRecordActivity.this,list,R.layout.vlist,new String[]{"title","info","img"},
                        new int[]{R.id.title,R.id.info,R.id.img});
                lv.setAdapter(adapter);
                lv.setOnItemClickListener( onItemClickListener);

            }
        }
    };

    View.OnClickListener startListener = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            Intent intent =new Intent(RunRecordActivity.this,RunActivity.class);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener onItemClickListener =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            AVQuery<AVObject> query = new AVQuery<AVObject>("RunRecord");
            final AVUser avUser=AVUser.getCurrentUser();
            query.whereEqualTo("user", avUser);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list2, AVException e) {
                    if(e==null)
                    {
                        AVObject record=list2.get(position);
                        Date datetime = (Date)record.get("timestamp");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time  =  df.format(datetime);
                        String duration = record.get("duration").toString();
                        String distance = record.get("distance").toString();

                        Intent intent =new Intent(RunRecordActivity.this,RecordDetailActivity.class);
                        intent.putExtra("time",time);
                        startActivity(intent);
                    }
                }
            });
        }
    };

    private List<Map<String, Object>> getData() {


        AVQuery<AVObject> query = new AVQuery<AVObject>("RunRecord");
        final AVUser avUser=AVUser.getCurrentUser();
        query.whereEqualTo("user", avUser);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list2, AVException e) {
                if(e==null)
                {
                    for(int i = 0; i < list2.size(); i++)
                    {
                        AVObject record=list2.get(i);
                        Date datetime = (Date)record.get("timestamp");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time  =  df.format(datetime);
                        String duration = record.get("duration").toString();
                        String distance = record.get("distance").toString();

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", time+"——"+avUser.getUsername());
                        map.put("info","跑步时间——"+duration+"\n跑步距离——"+distance);
                        map.put("img", R.drawable.ic_launcher);
                        list.add(map);

                        Log.d("ListView", time);
                        Log.d("ListView",duration);
                        Log.d("ListView",distance);

                    }
                }
            }
        });

//        Cursor cursor=rundata.select();
//        if (cursor.moveToFirst()) {
//            do {
//                String user = cursor.getString(cursor.getColumnIndex("user"));
//                String time = cursor.getString(cursor.getColumnIndex("starttime"));
//                int seq = cursor.getInt(cursor.getColumnIndex("seq"));
//                double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
//                double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
//
//                StringBuffer info = new StringBuffer(256);
//                info.append("longitude : "+Double.toString(longitude));
//                info.append("\nlantitude : "+Double.toString(latitude));
//
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("title", time+"——"+user);
//                map.put("info",info.toString());
//                map.put("img", R.drawable.ic_launcher);
//                list.add(map);
//
//                Log.d("ListView",info.toString());
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
        return list;
    }

}
