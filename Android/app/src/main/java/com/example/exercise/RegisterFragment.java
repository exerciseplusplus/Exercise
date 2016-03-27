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
import android.widget.ImageView;
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

    ImageView runImage;
    ImageView runRecordImage;
    ImageView updownImage;
    ImageView updownRecordImage;

    TextView runText;
    TextView runRecordText;
    TextView updownText;
    TextView updownRecordText;

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

        runImage=(ImageView)mParent.findViewById(R.id.image_register_run);
        runRecordImage=(ImageView)mParent.findViewById(R.id.image_register_run_record);
        updownImage=(ImageView)mParent.findViewById(R.id.image_register_updown);
        updownRecordImage=(ImageView)mParent.findViewById(R.id.image_register_updown_record);

        runText=(TextView)mParent.findViewById(R.id.textView_register_run);
        runRecordText=(TextView)mParent.findViewById(R.id.textView_register_run_record);
        updownText=(TextView)mParent.findViewById(R.id.textView_register_updown);
        updownRecordText=(TextView)mParent.findViewById(R.id.textView_register_updown_record);
        runText.setClickable(true);runRecordText.setClickable(true);
        updownText.setClickable(true);updownRecordText.setClickable(true);

        runImage.setOnClickListener(runListener);
        runRecordImage.setOnClickListener(runRecordListener);
        updownImage.setOnClickListener(updownListener);
        updownRecordImage.setOnClickListener(updownRecordListener);

        runText.setOnClickListener(runListener);
        runRecordText.setOnClickListener(runRecordListener);
        updownText.setOnClickListener(updownListener);
        updownRecordText.setOnClickListener(updownRecordListener);
    }
    OnClickListener runListener = new OnClickListener()
    {
    	public void onClick(View arg0) {
    		Log.d("Register","Hello");
    		//startAlarm();
			Intent intent =new Intent(mActivity,RunActivity.class);
			startActivity(intent);
    	}
    };

    OnClickListener runRecordListener = new OnClickListener()
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
            Intent intent =new Intent(mActivity,UpdownActivity.class);
            startActivity(intent);

    	}
    };

    OnClickListener updownRecordListener = new OnClickListener()
    {
        public void onClick(View arg0) {
            Log.d("Register","World");
            Intent intent =new Intent(mActivity,UpdownRecordActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
