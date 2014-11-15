package com.shizy.android.demo.swiperefresh;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.shizy.android.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SwipeRefreshLayoutActivity extends Activity {

	private static final String TAG = SwipeRefreshLayoutActivity.class.getSimpleName();
	
	private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			refresh();
		}
	};
	
	private final Runnable mRefreshDone = new Runnable() {

        @Override
        public void run() {
        	mSwipeRefreshLayout.setRefreshing(false);
        	mSwipeRefreshLayout.setEnabled(true);
        }

    };
    
	private Handler mHandler = new Handler();
	
	@InjectView(R.id.swiperefresh)
	SwipeRefreshLayout mSwipeRefreshLayout;
	@InjectView(R.id.listview)
	ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swiperefreshlayout);
		initView();
	}
	
	private void initView() {
		ButterKnife.inject(this);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, Cheeses.asList());
        mListView.setAdapter(arrayAdapter);
        
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
                R.color.color4);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
	}
	
    private void refresh() {
    	Log.d(TAG, "shizy---refresh");
    	mSwipeRefreshLayout.setEnabled(false);
        mHandler.removeCallbacks(mRefreshDone);
        mHandler.postDelayed(mRefreshDone, 2000);
    }
}
