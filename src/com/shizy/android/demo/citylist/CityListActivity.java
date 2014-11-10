package com.shizy.android.demo.citylist;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.shizy.android.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CityListActivity extends Activity {

	private static final String TAG = CityListActivity.class.getSimpleName();
	
	private AlphabetView.OnAlphabetViewTouchListener mOnAlphabetViewTouchListener = new AlphabetView.OnAlphabetViewTouchListener() {
		@Override
		public void OnAlphabetViewTouch(String letter, int action) {
			if (action == MotionEvent.ACTION_UP) {
				mOverlay.setVisibility(View.GONE);
			}
			else {
//				int position = mAdapter.getIndex(letter);
//				if (position >= 0) {
//					mFriendList.setSelection(position);
//				}
				mOverlay.setText(letter);
				mOverlay.setVisibility(View.VISIBLE);
			}
		}
	};
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			
		}
	};
	
	private List<City> mCities;
	
	@InjectView(R.id.listview)
	ListView mListView;
	@InjectView(R.id.alphabetview)
	AlphabetView mAlphabetView;
	@InjectView(R.id.overlay)
	TextView mOverlay;
	
	private CityListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_citylist);
		initData();
		initView();
	}
	
	private void initData() {
		try {
			InputStream is = getAssets().open("city.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String json = new String(buffer, "UTF-8");
			if (json != null) {
				try {
					JSONArray array = new JSONArray(json);
					List<City> cities = new ArrayList<City>();
					City city = null;
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							city = new City(array.optJSONObject(i));
							cities.add(city);
						}
					}
					mCities = cities;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initView() {
		ButterKnife.inject(this);
		mListView.setOnItemClickListener(mOnItemClickListener);
		mAlphabetView.setOnAlphabetViewTouchListener(mOnAlphabetViewTouchListener);
		
		mAdapter = new CityListAdapter();
		mListView.setAdapter(mAdapter);
	}
	
	private class CityListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mCities == null ? 0 : mCities.size();
		}

		@Override
		public Object getItem(int position) {
			return mCities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(CityListActivity.this);
			}
			((TextView)convertView).setText(mCities.get(position).getName());
			return convertView;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			Log.d(TAG, "shizy---notifyDataSetChanged");
		}
	}
}
