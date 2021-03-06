package com.shizy.android.demo.citylist;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.shizy.android.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
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
				int position = mAdapter.getPositionForSection(letter);
				if (position >= 0) {
					mListView.setSelection(position);
				}
				mOverlay.setText(letter);
				mOverlay.setVisibility(View.VISIBLE);
			}
		}
	};
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if (id >= 0) {
				ICityListItem item = mAdapter.getItem((int)id);
				if (item.getType() == ICityListItem.TYPE_CITY) {
					Log.d(TAG, "shizy---City: " + item.getTitle());
				}
			}
		}
	};
	
	private List<ICityListItem> mListItems;
	private HashMap<String, Integer> mSections;
	
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
					List<ICityListItem> items = new ArrayList<ICityListItem>();
					HashMap<String, Integer> section = new HashMap<String, Integer>();
					JSONArray array = new JSONArray(json);
					
					City city = null;
					String initial = null;
					Section alphabet = null;
					JSONArray list = null;
					JSONObject temp = null;
					for (int aIndex = 0; aIndex < array.length(); aIndex++) {
						temp = array.optJSONObject(aIndex);
						initial = temp.optString("initial");
						list = temp.optJSONArray("list");
						
						section.put(initial, items.size());
						
						alphabet = new Section();
						alphabet.setLetter(initial);
						items.add(alphabet);
						
						for (int listIndex = 0; listIndex < list.length(); listIndex++) {
							temp = list.optJSONObject(listIndex);
							city = new City(temp);
							items.add(city);
						}
					}
					mSections = section;
					mListItems = items;
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
		
		private LayoutInflater mInflater;
		
		public CityListAdapter() {
			mInflater = LayoutInflater.from(CityListActivity.this);
		}
		
		@Override
		public int getCount() {
			return mListItems == null ? 0 : mListItems.size();
		}

		@Override
		public ICityListItem getItem(int position) {
			return mListItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return ICityListItem.TYPE_COUNT;
		}
		
		@Override
		public int getItemViewType(int position) {
			return getItem(position).getType();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ICityListItem item = getItem(position);
			if (convertView == null) {
				switch (item.getType()) {
				case ICityListItem.TYPE_SECTION:
					convertView = mInflater.inflate(R.layout.citylist_section, null);
					break;
				case ICityListItem.TYPE_CITY:
					convertView = mInflater.inflate(R.layout.citylist_city, null);
					break;
				}
			}
			((TextView)convertView.findViewById(R.id.title)).setText(item.getTitle());
			return convertView;
		}
		
		public int getPositionForSection(String letter) {
			if (mSections.containsKey(letter)) {
				return mSections.get(letter);
			}
			return -1;
		}
	}
}
