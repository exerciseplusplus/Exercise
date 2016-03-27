package com.example.exercise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.example.exercise.ui.FollowListActivity;
import com.example.exercise.ui.StatusListActivity;
import com.example.exercise.ui.StatusListAdapter;
import com.example.exercise.ui.StatusSendActivity;
import com.example.exercise.ui.UserListActivity;
import com.example.exercise.ui.base.BaseListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NetworkFragment extends Fragment {

	private View mParent;
	private FragmentActivity mActivity;

	private static final int SEND_REQUEST = 2;
	@InjectView(R.id.status_List)
	BaseListView<Status> statusList;

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
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();
		mParent = getView();
		App.regiserUser(AVUser.getCurrentUser());
		initList();
		statusList.setToastIfEmpty(false);
		statusList.onRefresh();
		setHasOptionsMenu(true);


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
					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
					builder.setMessage(R.string.status_deleteStatusTips).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new StatusNetAsyncTask(mActivity) {
								@Override
								protected void doInBack() throws Exception {
									StatusService.deleteStatus(item);
								}

								@Override
								protected void onPost(Exception e) {
									if (e != null) {
										StatusUtils.toast(mActivity, e.getMessage());
									} else {
										statusList.onRefresh();
									}
								}
							}.execute();
						}
					}).setNegativeButton(R.string.cancel, null);
					builder.show();
				}
			}
		}, new StatusListAdapter(mActivity));
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

	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		mActivity.getMenuInflater().inflate(R.menu.menu_main, menu);
		 super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.find_user)
		{
			Intent intent =new Intent(mActivity,UserListActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}




	@OnClick(R.id.followers)
	void goFollowers() {
		AVUser currentUser = AVUser.getCurrentUser();
		FollowListActivity.go(mActivity,
				FollowListActivity.TYPE_FOLLOWER,
				currentUser);
	}

	@OnClick(R.id.following)
	void goFollowing() {
		FollowListActivity.go(mActivity,
				FollowListActivity.TYPE_FOLLOWING,
				AVUser.getCurrentUser());
	}

	@OnClick(R.id.send)
	void goSend() {
		Intent intent = new Intent(mActivity, StatusSendActivity.class);
		startActivityForResult(intent, SEND_REQUEST);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == -1) {
			if (requestCode == SEND_REQUEST) {
				statusList.onRefresh();
			}
		}
	}



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
