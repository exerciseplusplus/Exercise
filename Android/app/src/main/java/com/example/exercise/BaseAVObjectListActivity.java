package com.example.exercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.exercise.ui.PersonActivity;
import com.example.exercise.ui.base.AVObjectAdapter;
import com.example.exercise.ui.base.BaseListView;
import com.example.exercise.ui.base.UserAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BaseAVObjectListActivity extends Activity {
    @InjectView(R.id.userList)
    protected BaseListView<AVObject> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_users_layout);
        ButterKnife.inject(this);
        initList();
    }

    protected void refresh() {
        userList.onRefresh();
    }

    private void initList() {
        userList.init(new BaseListView.DataInterface<AVObject>() {

            @Override
            public List<AVObject> getDatas(int skip, int limit, List<AVObject> currentDatas) throws Exception {
                List<AVObject> objects = getObjectList(skip, limit);
                return objects;
            }

            @Override
            public void onItemSelected(AVObject item) {
                if(item.getClassName()=="RunRecord")
                {
                    Date datetime = (Date)item.get("timestamp");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time  =  df.format(datetime);
                    String duration = item.get("duration").toString();
                    String distance = item.get("distance").toString();

                    Intent intent =new Intent(BaseAVObjectListActivity.this,RecordDetailActivity.class);
                    intent.putExtra("time",time);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongPressed(final AVObject item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseAVObjectListActivity.this);
                builder.setMessage(R.string.record_delete_tips).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new StatusNetAsyncTask(BaseAVObjectListActivity.this) {
                            @Override
                            protected void doInBack() throws Exception {
                                StatusService.deleteRecord(item);
                            }

                            @Override
                            protected void onPost(Exception e) {
                                if (e != null) {
                                    StatusUtils.toast(BaseAVObjectListActivity.this, e.getMessage());
                                } else {
                                    userList.onRefresh();
                                }
                            }
                        }.execute();
                    }
                }).setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        }, new AVObjectAdapter(this));
    }

    protected abstract List<AVObject> getObjectList(int skip, int limit) throws Exception;
}
