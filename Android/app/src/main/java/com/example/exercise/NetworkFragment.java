package com.example.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.exercise.ui.StatusListActivity;
import com.example.exercise.ui.UserListActivity;

public class NetworkFragment extends Fragment {

	private View mParent;
	private FragmentActivity mActivity;
	Button shareButton;
	Button friendButton;
	
	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static NetworkFragment newInstance(int index) {
		NetworkFragment f = new NetworkFragment();

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
		View view = inflater.inflate(R.layout.fragment_network, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();
		mParent = getView();
		shareButton = (Button)mParent.findViewById(R.id.button_network_share);
		friendButton = (Button)mParent.findViewById(R.id.button_network_friend);
		shareButton.setOnClickListener(shareListener);
		friendButton.setOnClickListener(friendListener);

	}

	View.OnClickListener shareListener = new View.OnClickListener()
	{
		public void onClick(View arg0) {
			Log.d("Register", "Hello");
			//startAlarm();
			Intent intent =new Intent(mActivity,StatusListActivity.class);
			startActivity(intent);
		}
	};

	View.OnClickListener friendListener = new View.OnClickListener()
	{
		public void onClick(View arg0) {
			Log.d("Register","World");
			Intent intent =new Intent(mActivity,UserListActivity.class);
			startActivity(intent);

		}
	};

	private void backHomeFragment() {
		getFragmentManager().beginTransaction()
				.hide(MainActivity.mFragments[1])
				.show(MainActivity.mFragments[0]).commit();
		FragmentIndicator.setIndicator(0);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
		}
	}
}
