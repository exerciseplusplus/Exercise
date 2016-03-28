package com.example.exercise;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.example.exercise.ui.*;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment {
	private ExpandableListView expandableListView;

	List<AVUser> userList;

	private List<String> group_list;

	private List<String> item_lt;

	private List<List<String>> item_list;

	private List<List<Integer>> item_list2;

	private List<List<Integer>> gr_list2;

	private MyExpandableListViewAdapter adapter;
	private View mParent;
	private FragmentActivity mActivity;
	private static final int send_msg_code =  0x101;
	View view;
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
		view = inflater.inflate(R.layout.fragment_recommend, container,
				false);

		//getData();
		new Thread(new Runnable(){
			public void run()
			{
				try {
					userList=StatusService.recommendUsers(0,10);
				} catch (AVException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(send_msg_code);
			}

		}).start();

		expandableListView = (ExpandableListView)view.findViewById(R.id.expendlist);

		// 监听组点击
		expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
		{
			@SuppressLint("NewApi")
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
			{
				if (item_list.get(groupPosition).isEmpty())
				{
					return true;
				}
				return false;
			}
		});


		// 监听每个分组里子控件的点击事件
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				//Toast.makeText(getActivity(), "group=" + groupPosition + "---child=" + childPosition + "---" + item_list.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
				if (groupPosition == 1) {
					Intent intent = new Intent(mActivity, RunPlanActivity.class);
					intent.putExtra("planId", childPosition);
					startActivity(intent);
				} else {
					PersonActivity.go(mActivity, userList.get(childPosition));
				}
				return false;
			}
		});

		return view;
	}


	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			int what = msg.what;
			if(what == send_msg_code){


				setData(view);
			}
		}
	};

	public void setData(View view)
	{
		group_list = new ArrayList<String>();
		group_list.add("好友推荐");
		group_list.add("运动推荐");


		item_lt = new ArrayList<String>();
		for(int i=0;i<userList.size();i++)
			item_lt.add(userList.get(i).getUsername());


		item_list = new ArrayList<List<String>>();
		item_list.add(item_lt);

		item_lt = new ArrayList<String>();
		item_lt.add("锻炼肌肉");
		item_lt.add("新手跑步锻炼");
		item_lt.add("4K米跑步锻炼");
		item_lt.add("8K米跑步锻炼");

		item_list.add(item_lt);

		List<Integer> tmp_list = new ArrayList<Integer>();
		for(int i=0;i<userList.size();i++)
			tmp_list.add(StatusUtils.setAvatar(userList.get(i)));

		item_list2 = new ArrayList<List<Integer>>();
		item_list2.add(tmp_list);

		List<Integer> tmp_list2 = new ArrayList<Integer>();
		tmp_list2.add(R.drawable.pushup);
		tmp_list2.add(R.drawable.plan1);
		tmp_list2.add(R.drawable.plan2);
		tmp_list2.add(R.drawable.plan3);

		item_list2.add(tmp_list2);


		expandableListView.setGroupIndicator(null);

		adapter = new MyExpandableListViewAdapter(getActivity());

		expandableListView.setAdapter(adapter);
	}


	// 用过ListView的人一定很熟悉，只不过这里是BaseExpandableListAdapter
	class MyExpandableListViewAdapter extends BaseExpandableListAdapter
	{

		private Context context;

		public MyExpandableListViewAdapter(Context context)
		{
			this.context = context;
		}
		@Override
		public int getGroupCount()
		{
			return group_list.size();
		}

		@Override
		public int getChildrenCount(int groupPosition)
		{
			return item_list.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition)
		{
			return group_list.get(groupPosition);
		}
		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			return item_list.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}
		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}
		@Override
		public boolean hasStableIds()
		{
			return true;
		}

		/**
		 *
		 * 获取显示指定组的视图对象
		 *
		 * @param groupPosition 组位置
		 * @param isExpanded 该组是展开状态还是伸缩状态
		 * @param convertView 重用已有的视图对象
		 * @param parent 返回的视图对象始终依附于的视图组
		 * @return
		 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
		 *      android.view.ViewGroup)
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			GroupHolder groupHolder = null;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_group, null);
				groupHolder = new GroupHolder();
				groupHolder.txt = (TextView)convertView.findViewById(R.id.txt);
				groupHolder.img = (ImageView)convertView.findViewById(R.id.img);
				convertView.setTag(groupHolder);
			}
			else
			{
				groupHolder = (GroupHolder)convertView.getTag();
			}

			if (!isExpanded)
			{
				groupHolder.img.setBackgroundResource(R.drawable.drawable_expand_close);
			}
			else
			{
				groupHolder.img.setBackgroundResource(R.drawable.drawable_expand_open);
			}

			groupHolder.txt.setText(group_list.get(groupPosition));
			return convertView;
		}

		/**
		 *
		 * 获取一个视图对象，显示指定组中的指定子元素数据。
		 *
		 * @param groupPosition 组位置
		 * @param childPosition 子元素位置
		 * @param isLastChild 子元素是否处于组中的最后一个
		 * @param convertView 重用已有的视图(View)对象
		 * @param parent 返回的视图(View)对象始终依附于的视图组
		 * @return
		 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
		 *      android.view.ViewGroup)
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
		{
			ItemHolder itemHolder = null;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_item, null);
				itemHolder = new ItemHolder();
				itemHolder.txt = (TextView)convertView.findViewById(R.id.txt);
				itemHolder.img = (ImageView)convertView.findViewById(R.id.img);
				convertView.setTag(itemHolder);
			}
			else
			{
				itemHolder = (ItemHolder)convertView.getTag();
			}
			itemHolder.txt.setText(item_list.get(groupPosition).get(childPosition));
			itemHolder.img.setBackgroundResource(item_list2.get(groupPosition).get(childPosition));
			return convertView;
		}

		/**
		 *
		 * 是否选中指定位置上的子元素。
		 *
		 * @param groupPosition
		 * @param childPosition
		 * @return
		 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return true;
		}

	}

	class GroupHolder
	{
		public TextView txt;

		public ImageView img;
	}

	class ItemHolder {
		public ImageView img;

		public TextView txt;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mParent = getView();
		mActivity = getActivity();
	}

	View.OnClickListener userListener = new View.OnClickListener()
	{
		public void onClick(View arg0) {
			Log.d("Recommend", "Hello");
			Intent intent = new Intent(mActivity, RecommendUserActivity.class);
			startActivity(intent);

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
