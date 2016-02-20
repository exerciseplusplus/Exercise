package com.example.exercise;


import com.avos.avoscloud.AVOSCloud;

import android.app.Application;

public class AppConfig extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// U need your AVOS key and so on to run the code.
		AVOSCloud.initialize(this,
				"bbOVrjU0kGBQxkAqVLrENRJH-gzGzoHsz",
				"TrYcf9AP3jAsbKndb91oavzw");
		AVOSCloud.useAVCloudCN();
	}
}

