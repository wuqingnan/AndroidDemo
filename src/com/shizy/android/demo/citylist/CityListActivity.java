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
			
		}
	};
	
	private List<ICityListItem> mListItems;
	private HashMap<String, Integer> mSection;
	
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
					Alphabet alphabet = null;
					JSONArray list = null;
					JSONObject temp = null;
					for (int aIndex = 0; aIndex < array.length(); aIndex++) {
						temp = array.optJSONObject(aIndex);
						initial = temp.optString("initial");
						list = temp.optJSONArray("list");
						
						section.put(initial, items.size());
						
						alphabet = new Alphabet();
						alphabet.setLetter(initial);
						items.add(alphabet);
						
						for (int listIndex = 0; listIndex < list.length(); listIndex++) {
							temp = list.optJSONObject(listIndex);
							city = new City(temp);
							items.add(city);
						}
					}
					mSection = section;
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
		
		@Override
		public int getCount() {
			return mListItems == null ? 0 : mListItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mListItems.get(position);
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
			((TextView)convertView).setText(mListItems.get(position).getTitle());
			return convertView;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			Log.d(TAG, "shizy---notifyDataSetChanged");
		}

		public int getPositionForSection(String letter) {
			if (mSection.containsKey(letter)) {
				return mSection.get(letter);
			}
			return -1;
		}
	}
}
