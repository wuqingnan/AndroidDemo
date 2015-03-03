package com.shizy.android.demo.anim;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.shizy.android.demo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AnimationActivity extends Activity {

	private static final String LOG_TAG = AnimationActivity.class.getSimpleName();
	
	private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
	
	private final Animation mAnimation = new Animation() {
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			Log.e(LOG_TAG, "interpolatedTime: " + interpolatedTime);
			int targetTop = (int) (300 * interpolatedTime);
			int offset = targetTop - mTextView.getTop();
			Log.e(LOG_TAG, "interpolatedTime offset: " + offset);
			mTextView.bringToFront();
			mTextView.offsetTopAndBottom(offset);
		}
	};
	
	@InjectView(R.id.textView)
	TextView mTextView;
	
	private int mMediumAnimationDuration;
	private DecelerateInterpolator mDecelerateInterpolator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anim);
		ButterKnife.inject(this);
		init();
	}
	
	private void init() {
		mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
		mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
	}
	
	public void onButtonClick(View view) {
		mAnimation.reset();
		mAnimation.setDuration(mMediumAnimationDuration);
		mAnimation.setInterpolator(mDecelerateInterpolator);
		mTextView.clearAnimation();
		mTextView.startAnimation(mAnimation);
	}
}
