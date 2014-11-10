package com.shizy.android.demo;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	private ListView mListView;
	private DemoAdapter mAdapter;

	private LayoutInflater mInflater;

	private ArrayList<ActivityInfo> mActivities = null;

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.setClassName(getApplicationContext(),
					mActivities.get(position).name);
			startActivity(intent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), PackageManager.GET_ACTIVITIES);
			mActivities = new ArrayList<ActivityInfo>(
					Arrays.asList(pi.activities));
			String name = getClass().getName();
			for (int i = 0; i < mActivities.size(); i++) {
				if (name.equals(mActivities.get(i).name)) {
					mActivities.remove(i);
					break;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mInflater = LayoutInflater.from(this);
		mListView = getListView();
		mAdapter = new DemoAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	public class DemoAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mActivities.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mActivities.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_1, null);
				holder = new ViewHolder();
				holder.mTextView = (TextView) convertView
						.findViewById(android.R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mTextView
					.setText(mActivities.get(position).name
							.substring(mActivities.get(position).name
									.lastIndexOf('.') + 1));
			return convertView;
		}

	}

	class ViewHolder {
		TextView mTextView;
	}
}
