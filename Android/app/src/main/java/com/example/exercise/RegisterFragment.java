package com.example.exercise;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class RegisterFragment extends Fragment{

    private View mParent;  
    private FragmentActivity mActivity;
    static AppConfig  app;
	
	Button runButton;
	Button updownButton;
    /**
     * Create a new instance of DetailsFragment, initialized to show the text at
     * 'index'.
     */
    public static RegisterFragment newInstance(int index) {
        RegisterFragment f = new RegisterFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container,
				false);

		return view;
	}
    
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        
        mActivity = getActivity();  
        mParent = getView(); 
		runButton = (Button)mParent.findViewById(R.id.button_register_run);
		updownButton = (Button)mParent.findViewById(R.id.button_register_updown);
		runButton.setOnClickListener(runListener);
		updownButton.setOnClickListener(updownListener);
       
    }
    OnClickListener runListener = new OnClickListener()
    {
    	public void onClick(View arg0) {
    		Log.d("Register","Hello");
    		//startAlarm();
			Intent intent =new Intent(mActivity,RunRecordActivity.class);
			startActivity(intent);
    	}
    };

    OnClickListener updownListener = new OnClickListener()
    {
    	public void onClick(View arg0) {
    		Log.d("Register","World");
            Intent intent =new Intent(mActivity,UpdownRecordActivity.class);
            startActivity(intent);
    	
    	}
    };
    
	private void startAlarm() {
		Log.d("GEO", "start alarm");
		
		AlarmManager am = (AlarmManager)mActivity.getSystemService(Context.ALARM_SERVICE);
		Intent collectIntent = new Intent(mActivity, RunActivity.class);
		PendingIntent collectSender 
			= PendingIntent.getService(mActivity, 0, collectIntent, 0);
		am.cancel(collectSender);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME
			, SystemClock.elapsedRealtime()
			, 10 * 1000
			, collectSender);
	}

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
