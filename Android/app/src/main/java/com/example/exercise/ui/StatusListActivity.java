package com.example.exercise.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;

import com.example.exercise.App;
import com.example.exercise.R;
import com.example.exercise.Status;
import com.example.exercise.StatusNetAsyncTask;
import com.example.exercise.StatusService;
import com.example.exercise.StatusUtils;
import com.example.exercise.ui.base.BaseActivity;
import com.example.exercise.ui.base.BaseListView;

import java.util.ArrayList;
import java.util.List;

import static com.example.exercise.R.*;


/**
 * Created by lzw on 15/1/2.
 */
public class StatusListActivity extends BaseActivity {
  private static final int SEND_REQUEST = 2;
  @InjectView(id.status_List)
  BaseListView<Status> statusList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.status_list_layout);
    ButterKnife.inject(this);
    App.regiserUser(AVUser.getCurrentUser());
    initList();
    statusList.setToastIfEmpty(false);
    statusList.onRefresh();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    if (item.getItemId() == id.action_people) {
      Intent intent = new Intent(this, UserListActivity.class);
      startActivity(intent);
    } else if (item.getItemId() == id.logout) {
      AVUser.logOut();
      finish();
    }
    return super.onMenuItemSelected(featureId, item);
  }

  private void initList() {
    statusList.init(new BaseListView.DataInterface<Status>() {
      @Override
      public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
        long maxId;
        maxId = getMaxId(skip, currentDatas);
        if (maxId == 0) {
          return new ArrayList<>();
        } else {
          return StatusService.getStatusDatas(maxId, limit);
        }
      }

      @Override
      public void onItemLongPressed(final Status item) {
        AVStatus innerStatus = item.getInnerStatus();
        AVUser source = innerStatus.getSource();
        if (source.getObjectId().equals(AVUser.getCurrentUser().getObjectId())) {
          AlertDialog.Builder builder = new AlertDialog.Builder(StatusListActivity.this);
          builder.setMessage(string.status_deleteStatusTips).setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              new StatusNetAsyncTask(StatusListActivity.this) {
                @Override
                protected void doInBack() throws Exception {
                  StatusService.deleteStatus(item);
                }

                @Override
                protected void onPost(Exception e) {
                  if (e != null) {
                    StatusUtils.toast(StatusListActivity.this, e.getMessage());
                  } else {
                    statusList.onRefresh();
                  }
                }
              }.execute();
            }
          }).setNegativeButton(string.cancel, null);
          builder.show();
        }
      }
    }, new StatusListAdapter(StatusListActivity.this));
  }

  public static long getMaxId(int skip, List<Status> currentDatas) {
    long maxId;
    if (skip == 0) {
      maxId = Long.MAX_VALUE;
    } else {
      AVStatus lastStatus = currentDatas.get(currentDatas.size() - 1).getInnerStatus();
      maxId = lastStatus.getMessageId() - 1;
    }
    return maxId;
  }

  @OnClick(id.followers)
  void goFollowers() {
    AVUser currentUser = AVUser.getCurrentUser();
    FollowListActivity.go(StatusListActivity.this,
        FollowListActivity.TYPE_FOLLOWER,
        currentUser);
  }

  @OnClick(id.following)
  void goFollowing() {
    FollowListActivity.go(StatusListActivity.this,
        FollowListActivity.TYPE_FOLLOWING,
        AVUser.getCurrentUser());
  }

  @OnClick(id.send)
  void goSend() {
    Intent intent = new Intent(StatusListActivity.this, StatusSendActivity.class);
    startActivityForResult(intent, SEND_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == SEND_REQUEST) {
        statusList.onRefresh();
      }
    }
  }
}
