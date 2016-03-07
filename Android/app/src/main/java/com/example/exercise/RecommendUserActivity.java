package com.example.exercise;
import android.os.Bundle;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.exercise.StatusService;
import com.example.exercise.ui.BaseUserListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzw on 15/5/4.
 */
public class RecommendUserActivity extends BaseUserListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();
    }


    @Override
    protected List<AVUser> getUserList(int skip, int limit) throws Exception {

        setTitle(R.string.recommend_user);
        return StatusService.recommendUsers(0,10);
    }

}
