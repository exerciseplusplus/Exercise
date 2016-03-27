package com.example.exercise.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil;
import com.example.exercise.R;
import com.example.exercise.StatusUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/25.
 */
public class AVObjectAdapter extends BaseListAdapter<AVObject> {
    public AVObjectAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View conView, ViewGroup parent) {
        if (conView == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            conView = inflater.inflate(R.layout.user_row, null, false);
        }
        ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatarView);
        TextView nameView = ViewHolder.findViewById(conView, R.id.nameView);

        AVObject object = datas.get(position);
        LogUtil.log.d("ListView",object.getClassName());
        StatusUtils.displayAvatarForObject(object, avatarView);
        AVUser avUser=AVUser.getCurrentUser();
        Date datetime = (Date) object.get("timestamp");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time  =  df.format(datetime);
        if(object.getClassName()=="UpdownRecord")
        {
            String duration = object.get("duration").toString();
            String number = object.get("number").toString();
            StringBuffer sb = new StringBuffer(256);
            sb.append(time + "——" + avUser.getUsername());
            sb.append("\n锻炼时间——" + duration + "\n俯卧撑个数——" + number);
            nameView.setText(sb.toString());
        }
        else
        {

            String duration = object.get("duration").toString();
            String distance = object.get("distance").toString();
            StringBuffer sb = new StringBuffer(256);
            sb.append(time + "——" + avUser.getUsername());
            sb.append("\n跑步时间——"+duration+"\n跑步距离——"+distance);
            nameView.setText(sb.toString());
        }
        return conView;
    }
}
