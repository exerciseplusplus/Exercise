package com.example.exercise;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RecommendFragment extends Fragment {

	private View mParent;
	private FragmentActivity mActivity;
	Button userButton;
	Button methodButton;
	/**5
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static RecommendFragment newInstance(int index) {
		RecommendFragment f = new RecommendFragment();

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
		View view = inflater.inflate(R.layout.fragment_recommend, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mParent = getView();
		mActivity = getActivity();

		userButton = (Button)mParent.findViewById(R.id.button_recommend_user);
		methodButton = (Button)mParent.findViewById(R.id.button_recommend_method);
		userButton.setOnClickListener(userListener);
		methodButton.setOnClickListener(methodListener);
		
	}

	View.OnClickListener userListener = new View.OnClickListener()
	{
		public void onClick(View arg0) {
			Log.d("Recommend", "Hello");
		}
	};

	View.OnClickListener methodListener = new View.OnClickListener()
	{
		public void onClick(View arg0) {
			Log.d("Recommend","World");
		}
	};


	private void backHomeFragment() {
		getFragmentManager().beginTransaction()
				.hide(MainActivity.mFragments[2])
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
